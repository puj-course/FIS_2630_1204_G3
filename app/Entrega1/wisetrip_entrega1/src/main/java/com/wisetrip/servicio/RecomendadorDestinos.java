
package com.wisetrip.servicio;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;

import com.wisetrip.modelo.Ciudad;
import com.wisetrip.modelo.Importancia;
import com.wisetrip.modelo.EstadoAtributo;
import com.wisetrip.modelo.ValidacionCiudades;
import com.wisetrip.modelo.PreferenciasUsuario;
import com.wisetrip.modelo.ResultadoRecomendacion;
import com.wisetrip.negocio.Recomendador;
import com.wisetrip.negocio.GestorPreferencias;

@Service
public class RecomendadorDestinos implements Recomendador {

    private final boolean presupuestoObligatorio;
    private final GestorPreferencias gestorPreferencias;

    public RecomendadorDestinos(
        @Value("${wisetrip.presupuesto.obligatorio:true}") boolean presupuestoObligatorio,
        GestorPreferencias gestorPreferencias) {
        this.presupuestoObligatorio = presupuestoObligatorio;
        this.gestorPreferencias = gestorPreferencias;
    }

    //Puntaje (0 a 1) de viabilidad presupuestal. Dentro del limite todos
    //reciben 1: no se premia gastar mas ni ahorrar. El exceso se penaliza.

    public double calcularPuntajePresupuesto(double costo, double presupuesto) {
        if (presupuesto <= 0) {
            return 0.0;
        }
        if (costo <= presupuesto) {
            return 1.0;
        } else {
            double exceso = (costo - presupuesto) / presupuesto;
            //Primero calcula 1 - exceso y luego devuelve el mayor entre ese resultado
            // y 0.0. Así, el valor que devuelve tiene un mínimo de 0.
            return Math.max(0.0, 1 - exceso);
        }
    }

    // Gustar suma 1; prefiero suma 3 o resta 1 si incumple.
    // Calcula qué tanto coincide una oferta con tus gustos (devuelve un número entre 0.0 y 1.0)
    public double calcularCoincidenciaPonderada(Map<String, Boolean> oferta,
                                                Map<String, Importancia> gustos) {
        // Guarda la suma máxima de puntos posibles
        int pesoTotal = 0;
        // Guarda los puntos que realmente ganaste
        int pesoLogrado = 0;

        // Recorre cada categoría de preguntas (ej. Ubicación, Precio)
        for (var categoria : gestorPreferencias.listarCategorias()) {
            // Recorre cada pregunta dentro de esa categoría
            for (var pregunta : categoria.getPreguntas()) {
                // Busca qué tan importante es esa pregunta en los gustos
                Importancia importancia = gustos.get(pregunta.getClave());

                // Revisa si esa pregunta le importa al viajero y si da puntos
                //importancia.java de gustar y prefiero

                if (importancia != null && importancia.esPuntuable()) {
                    // Suma el valor de la pregunta al puntaje máximo posible
                    pesoTotal += importancia.getPeso();

                    // Mira si la oferta cumple (true), no cumple (false) o no se sabe (null)
                    Boolean cumple = oferta.get(pregunta.getClave());

                    // Si la oferta sí cumple, suma todos los puntos de esa pregunta
                    if (Boolean.TRUE.equals(cumple)) {
                        pesoLogrado += importancia.getPeso();
                        // Si la oferta NOOOO cumple y era algo que "preferías", resta 1 punto
                    } else if (importancia == Importancia.PREFIERO && Boolean.FALSE.equals(cumple)) {
                        pesoLogrado--;
                    }
                    // Si "cumple" es null (desconocido), no suma ni resta nada
                }
            }
        }

        // Si no hubo preguntas puntuables, devuelve 0.0.
        // Si las hubo, divide puntos ganados entre el total y asegura que nunca sea menor a 0.0.
        return pesoTotal == 0 ? 0.0 : Math.max(0.0, (double) pesoLogrado / pesoTotal);
    }


    @Override
    public ResultadoRecomendacion calcularPuntajeTotal(
            Ciudad ciudad,
            PreferenciasUsuario preferencias) {
        long dias = preferencias.getDuracionDias();
        double diario = ciudad.getCostoPromedio();
        double costo = diario * dias; // Costo de la estancia por persona.
        double presupuesto = preferencias.getPresupuestoUsd();

        double pCosto = calcularPuntajePresupuesto(costo, presupuesto);
        double pGustos = calcularCoincidenciaPonderada(
                ciudad.getAtributos(), preferencias.getAtributos());
        boolean provisional = preferencias.getAtributos().entrySet().stream()
                .anyMatch(e -> e.getValue() != null && e.getValue().esPuntuable()
                        && ciudad.estadoAtributo(e.getKey()) == EstadoAtributo.NO_SABEMOS);

        return new ResultadoRecomendacion(ciudad, pGustos, pCosto, pGustos,
                preferencias.tienePreferenciasPuntuables(), provisional);
    }

    /**
     * Diferencia absoluta entre el costo del viaje (diario × días) y el presupuesto.
     * Menor valor = más cerca del presupuesto del usuario.
     */
    public double diferenciaPresupuesto(Ciudad ciudad, PreferenciasUsuario preferencias) {
        double costoViaje = ciudad.getCostoPromedio() * preferencias.getDuracionDias();
        return Math.abs(costoViaje - preferencias.getPresupuestoUsd());
    }

    @Override
    public boolean excedePresupuesto(Ciudad ciudad, PreferenciasUsuario preferencias) {
        double costo = ciudad.getCostoPromedio() * preferencias.getDuracionDias();
        // Un costo desconocido no confirma un exceso.
        return presupuestoObligatorio && Double.isFinite(costo) && costo > 0
                && costo > preferencias.getPresupuestoUsd();
    }

    @Override
    public ValidacionCiudades validarCiudades(
            List<Ciudad> ciudades, PreferenciasUsuario preferencias) {

        List<Ciudad> validas = new ArrayList<>();
        List<Ciudad> descartadas = new ArrayList<>();
        List<Ciudad> pendientes = new ArrayList<>();

        for (Ciudad ciudad : ciudades) {
            double costo = ciudad.getCostoPromedio() * preferencias.getDuracionDias();

            boolean faltaInformacion = false;
            if (!Double.isFinite(costo)) {
                faltaInformacion = true;
            } else if (costo <= 0) {
                faltaInformacion = true;
            }

            boolean incumple = excedePresupuesto(ciudad, preferencias);

            for (var requisito : preferencias.getAtributos().entrySet()) {
                var preferenciaDelAtributo = requisito.getValue();

                if (preferenciaDelAtributo != null) {
                    if (preferenciaDelAtributo.esIndispensable()) {
                        var atributo = requisito.getKey();
                        EstadoAtributo estado = ciudad.estadoAtributo(atributo);

                        if (estado == EstadoAtributo.NO_CUMPLE) {
                            incumple = true;
                        }

                        if (estado == EstadoAtributo.NO_SABEMOS) {
                            faltaInformacion = true;
                        }
                    }
                }
            }

            if (incumple) {
                descartadas.add(ciudad);
            } else if (faltaInformacion) {
                pendientes.add(ciudad);
            } else {
                validas.add(ciudad);
            }
        }

        return new ValidacionCiudades(validas, descartadas, pendientes);
    }

    @Override
    public List<ResultadoRecomendacion> puntuarCiudadesValidas(ValidacionCiudades validacion,
                                                               PreferenciasUsuario preferencias) {
        List<ResultadoRecomendacion> resultados = new ArrayList<>();
        for (Ciudad ciudad : validacion.validas()) {
            resultados.add(calcularPuntajeTotal(ciudad, preferencias));
        }

        // Desempate / orden por cercanía al presupuesto cuando no hay nada que
        // puntuar (solo indispensables o ningún gustar/prefiero) o todos empatan.
        boolean ordenarPorPresupuesto = !preferencias.tienePreferenciasPuntuables()
                || todosMismoPuntaje(resultados);

        if (ordenarPorPresupuesto) {
            resultados.sort(Comparator.comparingDouble(
                    r -> diferenciaPresupuesto(r.getCiudad(), preferencias)));
        } else {
            resultados.sort(Comparator
                    .comparingDouble(ResultadoRecomendacion::getPuntajeTotal).reversed()
                    .thenComparingDouble(r -> diferenciaPresupuesto(r.getCiudad(), preferencias)));
        }

        return resultados;
    }

    private static boolean todosMismoPuntaje(List<ResultadoRecomendacion> resultados) {
        if (resultados.size() <= 1) {
            return true;
        }
        double primero = resultados.get(0).getPuntajeTotal();
        for (ResultadoRecomendacion resultado : resultados) {
            if (Double.compare(resultado.getPuntajeTotal(), primero) != 0) {
                return false;
            }
        }
        return true;
    }
}
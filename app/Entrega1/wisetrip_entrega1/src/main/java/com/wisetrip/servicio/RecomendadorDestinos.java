
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
// HU143: NPM explicito = 7; excluye constructores, heredados y generados.
// NOC = 0: sin hijas directas en src/main/java al 2026-11-11. Recalcular ante cambios.
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
        }

        // HU142: se elimino el else porque el caso anterior ya termina con return.
        // El calculo se separo en pasos para explicar la penalizacion por exceso.
        double montoExcedido = costo - presupuesto;
        double proporcionExcedida = montoExcedido / presupuesto;
        double puntajePenalizado = 1 - proporcionExcedida;

        // El puntaje nunca puede ser menor que cero.
        return Math.max(0.0, puntajePenalizado);
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

        if (pesoTotal == 0) {
            return 0.0;
        }

        double proporcionLograda = (double) pesoLogrado / pesoTotal;
        return Math.max(0.0, proporcionLograda);
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
        // HU142: stream().anyMatch() se cambio por un for con condiciones explicitas.
        // La coincidencia es provisional si falta un dato que participa en el puntaje.
        boolean provisional = false;
        for (Map.Entry<String, Importancia> preferencia : preferencias.getAtributos().entrySet()) {
            Importancia importancia = preferencia.getValue();

            if (importancia != null) {
                if (importancia.esPuntuable()) {
                    String atributo = preferencia.getKey();
                    EstadoAtributo estado = ciudad.estadoAtributo(atributo);

                    if (estado == EstadoAtributo.NO_SABEMOS) {
                        provisional = true;
                        // Como anyMatch(), termina al encontrar la primera coincidencia.
                        break;
                    }
                }
            }
        }

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

        // HU142: la cadena de && se cambio por comprobaciones separadas con retorno temprano.
        if (!presupuestoObligatorio) {
            return false;
        }

        // Un costo no numerico o infinito no permite confirmar un exceso.
        if (!Double.isFinite(costo)) {
            return false;
        }

        // Un costo cero o negativo se considera desconocido.
        if (costo <= 0) {
            return false;
        }

        return costo > preferencias.getPresupuestoUsd();
    }

    @Override
    public ValidacionCiudades validarCiudades(
            List<Ciudad> ciudades, PreferenciasUsuario preferencias) {

        // HU142: nombres abreviados y tipos var se cambiaron por nombres y tipos explicitos.
        List<Ciudad> ciudadesValidas = new ArrayList<>();
        List<Ciudad> ciudadesDescartadas = new ArrayList<>();
        List<Ciudad> ciudadesPendientes = new ArrayList<>();

        for (Ciudad ciudad : ciudades) {
            double costoEstancia = ciudad.getCostoPromedio() * preferencias.getDuracionDias();

            boolean faltaInformacion = false;
            if (!Double.isFinite(costoEstancia)) {
                faltaInformacion = true;
            } else if (costoEstancia <= 0) {
                faltaInformacion = true;
            }

            boolean tieneIncumplimientoConfirmado = excedePresupuesto(ciudad, preferencias);

            // Entre las preferencias de atributos, solo las indispensables afectan esta clasificacion.
            for (Map.Entry<String, Importancia> requisito : preferencias.getAtributos().entrySet()) {
                Importancia preferenciaDelAtributo = requisito.getValue();

                if (preferenciaDelAtributo != null) {
                    if (preferenciaDelAtributo.esIndispensable()) {
                        String atributo = requisito.getKey();
                        EstadoAtributo estado = ciudad.estadoAtributo(atributo);

                        if (estado == EstadoAtributo.NO_CUMPLE) {
                            tieneIncumplimientoConfirmado = true;
                        }

                        if (estado == EstadoAtributo.NO_SABEMOS) {
                            faltaInformacion = true;
                        }
                    }
                }
            }

            // Un incumplimiento confirmado tiene prioridad, aunque tambien falten datos.
            if (tieneIncumplimientoConfirmado) {
                ciudadesDescartadas.add(ciudad);
            } else if (faltaInformacion) {
                ciudadesPendientes.add(ciudad);
            } else {
                ciudadesValidas.add(ciudad);
            }
        }

        return new ValidacionCiudades(ciudadesValidas, ciudadesDescartadas, ciudadesPendientes);
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

        // HU142: la cadena de comparadores se separo en reglas con nombres explicitos.
        // La flecha indica que, para cada resultado, calculamos su distancia al presupuesto.
        Comparator<ResultadoRecomendacion> porCercaniaAlPresupuesto = Comparator.comparingDouble(
                resultado -> diferenciaPresupuesto(resultado.getCiudad(), preferencias));

        if (ordenarPorPresupuesto) {
            // Primero aparece la ciudad cuyo costo esta mas cerca del presupuesto.
            resultados.sort(porCercaniaAlPresupuesto);
        } else {
            // comparingDouble ordena de menor a mayor; reversed invierte ese orden.
            Comparator<ResultadoRecomendacion> porMayorPuntaje = Comparator
                    .comparingDouble(ResultadoRecomendacion::getPuntajeTotal).reversed();

            // thenComparing usa el presupuesto solo cuando los puntajes son iguales.
            Comparator<ResultadoRecomendacion> porPuntajeYDesempate = porMayorPuntaje
                    .thenComparing(porCercaniaAlPresupuesto);
            resultados.sort(porPuntajeYDesempate);
        }

        return resultados;
    }

    private static boolean todosMismoPuntaje(List<ResultadoRecomendacion> resultados) {
        if (resultados.size() <= 1) {
            return true;
        }
        double puntajeDeReferencia = resultados.get(0).getPuntajeTotal();
        for (ResultadoRecomendacion resultado : resultados) {
            double puntajeActual = resultado.getPuntajeTotal();
            // Double.compare devuelve cero cuando ambos puntajes son iguales.
            if (Double.compare(puntajeActual, puntajeDeReferencia) != 0) {
                return false;
            }
        }
        return true;
    }
}

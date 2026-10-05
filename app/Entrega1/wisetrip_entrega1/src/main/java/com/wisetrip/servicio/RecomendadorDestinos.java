//toca entenderlo mucho mas
//algoritmo de recomendacion por presupuesto y preferencias

package com.wisetrip.servicio;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.LinkedHashMap;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;

import com.wisetrip.modelo.Ciudad;
import com.wisetrip.modelo.Importancia;
import com.wisetrip.modelo.EstadoAtributo;
import com.wisetrip.modelo.ValidacionCiudades;
import com.wisetrip.modelo.PreferenciasUsuario;
import com.wisetrip.modelo.ResultadoRecomendacion;

@Service
public class RecomendadorDestinos {



    private final boolean presupuestoObligatorio;

    public RecomendadorDestinos(
            @Value("${wisetrip.presupuesto.obligatorio:true}") boolean presupuestoObligatorio) {
        this.presupuestoObligatorio = presupuestoObligatorio;
    }

    /**
     * Puntaje (0 a 1) de viabilidad presupuestal. Dentro del limite todos
     * reciben 1: no se premia gastar mas ni ahorrar. El exceso se penaliza.
     */
    public double calcularPuntajePresupuesto(double costo, double presupuesto) {
        if (presupuesto <= 0) {
            return 0.0;
        }
        if (costo <= presupuesto) {
            return 1.0;
        } else {
            double exceso = (costo - presupuesto) / presupuesto;
            return Math.max(0.0, 1 - exceso);
        }
    }

    /**
     * Coincidencia ponderada de ME_GUSTARIA / LO_PREFIERO.
     * Los indispensables ({@link Importancia#si}) no suman puntos.
     */
    public double calcularPuntajePreferencias(Map<String, Boolean> oferta,
                                              Map<String, Importancia> gustos) {
        return calcularCoincidenciasPorCategoria(oferta, gustos).values().stream()
                .mapToDouble(Double::doubleValue).average().orElse(0.0);
    }

    /**
     * Coincidencia de 0 a 1 por categoria del cuestionario.
     * Solo participan respuestas con peso positivo en Importancia.
     * Los datos desconocidos conservan su peso en el denominador.
     * Se omiten categorias sin preferencias puntuables.
     */
    public Map<String, Double> calcularCoincidenciasPorCategoria(Map<String, Boolean> oferta,
                                                                Map<String, Importancia> gustos) {
        Map<String, Double> coincidencias = new LinkedHashMap<>();
        for (var categoria : PreferenciasServicio.CATEGORIAS) {
            int pesoTotal = 0;
            int pesoLogrado = 0;
            for (var pregunta : categoria.getPreguntas()) {
                Importancia importancia = gustos.get(pregunta.getClave());
                if (importancia == null || importancia.peso <= 0) continue;
                pesoTotal += importancia.peso;
                if (Boolean.TRUE.equals(oferta.get(pregunta.getClave()))) {
                    pesoLogrado += importancia.peso;
                }
            }
            if (pesoTotal > 0) {
                coincidencias.put(categoria.getNombre(), (double) pesoLogrado / pesoTotal);
            }
        }
        return coincidencias;
    }

    public ResultadoRecomendacion calcularPuntajeTotal(Ciudad ciudad, PreferenciasUsuario preferencias) {
        long dias = preferencias.getDuracionDias();
        double diario = ciudad.getCostoPromedio();
        double costo = diario * dias; // Costo de la estancia por persona.
        double presupuesto = preferencias.getPresupuesto();

        double pCosto = calcularPuntajePresupuesto(costo, presupuesto);
        double pGustos = calcularPuntajePreferencias(
                ciudad.getAtributos(), preferencias.getAtributos());
        boolean provisional = preferencias.getAtributos().entrySet().stream()
                .anyMatch(e -> e.getValue() != null && e.getValue().peso > 0
                        && ciudad.estadoAtributo(e.getKey()) == EstadoAtributo.noSabemos);

        return new ResultadoRecomendacion(ciudad, pGustos, pCosto, pGustos,
                preferencias.tienePreferenciasPuntuables(), provisional);
    }

    /**
     * Diferencia absoluta entre el costo del viaje (diario × días) y el presupuesto.
     * Menor valor = más cerca del presupuesto del usuario.
     */
    public double diferenciaPresupuesto(Ciudad ciudad, PreferenciasUsuario preferencias) {
        double costoViaje = ciudad.getCostoPromedio() * preferencias.getDuracionDias();
        return Math.abs(costoViaje - preferencias.getPresupuesto());
    }

    public List<ResultadoRecomendacion> recomendarDestinos(List<Ciudad> ciudades,
                                                           PreferenciasUsuario preferencias) {
        return puntuarCiudadesValidas(validarCiudades(ciudades, preferencias), preferencias);
    }

    public ValidacionCiudades validarCiudades(List<Ciudad> ciudades, PreferenciasUsuario preferencias) {
        List<Ciudad> validas = new ArrayList<>();
        List<Ciudad> descartadas = new ArrayList<>();
        List<Ciudad> pendientes = new ArrayList<>();
        for (Ciudad ciudad : ciudades) {
            double costo = ciudad.getCostoPromedio() * preferencias.getDuracionDias();
            boolean faltaInformacion = !Double.isFinite(costo) || costo <= 0;
            boolean incumple = !faltaInformacion && presupuestoObligatorio
                    && costo > preferencias.getPresupuesto();
            for (var requisito : preferencias.getAtributos().entrySet()) {
                if (requisito.getValue() != Importancia.si) continue;
                EstadoAtributo estado = ciudad.estadoAtributo(requisito.getKey());
                incumple |= estado == EstadoAtributo.noCumple;
                faltaInformacion |= estado == EstadoAtributo.noSabemos;
            }
            // Un incumplimiento confirmado basta para descartar, aunque falten otros datos.
            if (incumple) descartadas.add(ciudad);
            else if (faltaInformacion) pendientes.add(ciudad);
            else validas.add(ciudad);
        }
        return new ValidacionCiudades(validas, descartadas, pendientes);
    }

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

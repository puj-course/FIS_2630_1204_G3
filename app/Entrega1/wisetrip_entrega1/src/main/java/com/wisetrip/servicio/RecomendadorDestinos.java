//toca entenderlo mucho mas 
//algoritmo de recomendacion por presupuesto y preferencias

package com.wisetrip.servicio;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.wisetrip.modelo.Ciudad;
import com.wisetrip.modelo.PreferenciasUsuario;
import com.wisetrip.modelo.ResultadoRecomendacion;

@Service
public class RecomendadorDestinos {

    private static final double PESO_PRESUPUESTO = 0.4;
    private static final double PESO_PREFERENCIAS = 0.6;

    /**
     * Puntaje (0 a 1) segun que tan bien el costo de la ciudad se ajusta
     * al presupuesto del usuario.
     */

    public double calcularPuntajePresupuesto(double costo, double presupuesto) {
        if (presupuesto <= 0) {
            return 0.0;
        }
        if (costo <= presupuesto) {
            double uso = costo / presupuesto;
            return 0.7 + 0.3 * uso;
        } else {
            double exceso = (costo - presupuesto) / presupuesto;
            return Math.max(0.0, 1 - exceso);
        }
    }

    /**
     * Proporcion de preferencias activas del usuario que la ciudad cumple.
     */
    public double calcularPuntajePreferencias(Map<String, Boolean> oferta,
                                              Map<String, Boolean> gustos) {
        List<String> activos = new ArrayList<>();
        for (Map.Entry<String, Boolean> respuesta : gustos.entrySet()) {
            if (Boolean.TRUE.equals(respuesta.getValue())) {
                activos.add(respuesta.getKey());
            }
        }

        if (activos.isEmpty()) {
            return 1.0;   // sin preferencias activas no se penaliza a nadie
        }

        long aciertos = activos.stream()
                .filter(gusto -> Boolean.TRUE.equals(oferta.get(gusto)))
                .count();

        return (double) aciertos / activos.size();
    }

    public ResultadoRecomendacion calcularPuntajeTotal(Ciudad ciudad, PreferenciasUsuario preferencias) {
        long dias = preferencias.getDuracionDias();
        double diario = ciudad.getCostoPromedio();
        double costo = diario * dias; // Costo de la estancia por persona.
        double presupuesto = preferencias.getPresupuesto();

        double pCosto = calcularPuntajePresupuesto(costo, presupuesto);
        double pGustos = calcularPuntajePreferencias(
                ciudad.getAtributos(), preferencias.getAtributos());
        double total = PESO_PRESUPUESTO * pCosto
                            + PESO_PREFERENCIAS * pGustos;

        return new ResultadoRecomendacion(ciudad, total, pCosto, pGustos);
    }

    public List<ResultadoRecomendacion> recomendarDestinos(List<Ciudad> ciudades,
                                                           PreferenciasUsuario preferencias) {
        List<ResultadoRecomendacion> resultados = new ArrayList<>();
        for (Ciudad ciudad : ciudades) {
            if (Double.isFinite(ciudad.getCostoPromedio()) && ciudad.getCostoPromedio() > 0) {
                resultados.add(calcularPuntajeTotal(ciudad, preferencias));
            }
        }
        resultados.sort(Comparator.comparingDouble(ResultadoRecomendacion::getPuntajeTotal).reversed());
        return resultados;
    }
}

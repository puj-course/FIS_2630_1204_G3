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
    public double calcularPuntajePresupuesto(double costoCiudad, double presupuesto) {
        if (presupuesto <= 0) {
            return 0.0;
        }
        if (costoCiudad <= presupuesto) {
            double proporcionUso = costoCiudad / presupuesto;
            return 0.7 + 0.3 * proporcionUso;
        } else {
            double exceso = (costoCiudad - presupuesto) / presupuesto;
            return Math.max(0.0, 1 - exceso);
        }
    }

    /**
     * Proporcion de preferencias activas del usuario que la ciudad cumple.
     * La ciudad cumple un atributo si su valor es mayor que cero.
     */
    public double calcularPuntajePreferencias(Map<String, Integer> atributosCiudad,
                                              Map<String, Boolean> atributosUsuario) {
        List<String> preferenciasActivas = new ArrayList<>();
        for (Map.Entry<String, Boolean> entry : atributosUsuario.entrySet()) {
            if (Boolean.TRUE.equals(entry.getValue())) {
                preferenciasActivas.add(entry.getKey());
            }
        }

        if (preferenciasActivas.isEmpty()) {
            return 1.0;   // sin preferencias activas no se penaliza a nadie
        }

        long coincidencias = preferenciasActivas.stream()
                .filter(attr -> {
                    Integer valor = atributosCiudad.get(attr);
                    return valor != null && valor > 0;
                })
                .count();

        return (double) coincidencias / preferenciasActivas.size();
    }

    public ResultadoRecomendacion calcularPuntajeTotal(Ciudad ciudad, PreferenciasUsuario preferencias) {
        double puntajePresupuesto = calcularPuntajePresupuesto(
                ciudad.getCostoPromedio(), preferencias.getPresupuesto());
        double puntajePreferencias = calcularPuntajePreferencias(
                ciudad.getAtributos(), preferencias.getAtributos());
        double puntajeTotal = PESO_PRESUPUESTO * puntajePresupuesto
                            + PESO_PREFERENCIAS * puntajePreferencias;

        return new ResultadoRecomendacion(ciudad, puntajeTotal, puntajePresupuesto, puntajePreferencias);
    }

    public List<ResultadoRecomendacion> recomendarDestinos(List<Ciudad> ciudades,
                                                           PreferenciasUsuario preferencias) {
        List<ResultadoRecomendacion> resultados = new ArrayList<>();
        for (Ciudad ciudad : ciudades) {
            resultados.add(calcularPuntajeTotal(ciudad, preferencias));
        }
        resultados.sort(Comparator.comparingDouble(ResultadoRecomendacion::getPuntajeTotal).reversed());
        return resultados;
    }
}
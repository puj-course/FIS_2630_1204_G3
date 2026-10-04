//toca entenderlo mucho mas
//algoritmo de recomendacion por presupuesto y preferencias

package com.wisetrip.servicio;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.wisetrip.modelo.Ciudad;
import com.wisetrip.modelo.Importancia;
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
     * Coincidencia ponderada de ME_GUSTARIA / LO_PREFIERO.
     * Los indispensables ({@link Importancia#si}) no suman puntos.
     */
    public double calcularPuntajePreferencias(Map<String, Boolean> oferta,
                                              Map<String, Importancia> gustos) {
        int pesoTotal = 0;
        int pesoLogrado = 0;

        for (Map.Entry<String, Importancia> respuesta : gustos.entrySet()) {
            Importancia importancia = respuesta.getValue();
            if (importancia == null || importancia.peso <= 0) {
                continue;
            }
            pesoTotal += importancia.peso;
            if (Boolean.TRUE.equals(oferta.get(respuesta.getKey()))) {
                pesoLogrado += importancia.peso;
            }
        }

        if (pesoTotal == 0) {
            return 1.0;   // sin gustar/prefiero no se penaliza a nadie
        }

        return (double) pesoLogrado / pesoTotal;
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
        List<ResultadoRecomendacion> resultados = new ArrayList<>();
        for (Ciudad ciudad : ciudades) {
            if (Double.isFinite(ciudad.getCostoPromedio()) && ciudad.getCostoPromedio() > 0) {
                resultados.add(calcularPuntajeTotal(ciudad, preferencias));
            }
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

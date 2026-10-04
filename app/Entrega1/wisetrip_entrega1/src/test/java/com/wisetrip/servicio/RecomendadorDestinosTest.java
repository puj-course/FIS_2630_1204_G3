package com.wisetrip.servicio;

import com.wisetrip.modelo.Ciudad;
import com.wisetrip.modelo.PreferenciasUsuario;
import com.wisetrip.modelo.ResultadoRecomendacion;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RecomendadorDestinosTest {
    private final RecomendadorDestinos recomendador = new RecomendadorDestinos(false);

    @Test
    void gastarHastaElLimiteNoMejoraElPuntaje() {
        assertEquals(1.0, recomendador.calcularPuntajePresupuesto(500, 1000));
        assertEquals(1.0, recomendador.calcularPuntajePresupuesto(1000, 1000));
        assertEquals(0.8, recomendador.calcularPuntajePresupuesto(1200, 1000), 1e-9);
        assertEquals(0.0, recomendador.calcularPuntajePresupuesto(2500, 1000));
        assertEquals(0.0, recomendador.calcularPuntajePresupuesto(500, 0));
    }

    @Test
    void sinPreferenciasOrdenaPorCercaniaAlPresupuestoDeTodaLaEstancia() {
        Ciudad economica = ciudad(250);
        Ciudad alLimite = ciudad(500);
        Ciudad excedida = ciudad(550);
        PreferenciasUsuario preferencias = new PreferenciasUsuario(1000, Map.of(), 2);

        var resultados = recomendador.recomendarDestinos(
                List.of(excedida, economica, alLimite), preferencias);

        assertEquals(List.of(alLimite, excedida, economica), resultados.stream()
                .map(ResultadoRecomendacion::getCiudad).toList());
        assertEquals(resultados.get(0).getPuntajeTotal(), resultados.get(2).getPuntajeTotal());
        assertEquals(List.of(alLimite, economica), recomendador.recomendarDestinos(
                List.of(alLimite, economica), preferencias).stream()
                .map(ResultadoRecomendacion::getCiudad).toList());
    }

    @Test
    void presupuestoObligatorioDescartaExcedidasAntesDePuntuar() {
        Ciudad economica = ciudad(250);
        Ciudad alLimite = ciudad(500);
        Ciudad excedida = ciudad(500.01);
        var puntuadas = new java.util.ArrayList<Ciudad>();
        var estricto = new RecomendadorDestinos(true) {
            @Override
            public ResultadoRecomendacion calcularPuntajeTotal(
                    Ciudad ciudad, PreferenciasUsuario preferencias) {
                puntuadas.add(ciudad);
                return super.calcularPuntajeTotal(ciudad, preferencias);
            }
        };
        var preferencias = new PreferenciasUsuario(1000, Map.of(), 2);

        var resultados = estricto.recomendarDestinos(
                List.of(excedida, economica, alLimite), preferencias);

        assertEquals(List.of(economica, alLimite), puntuadas);
        assertEquals(List.of(alLimite, economica), resultados.stream()
                .map(ResultadoRecomendacion::getCiudad).toList());
    }

    @Test
    void presupuestoObligatorioPuedeDejarSinCandidatos() {
        var estricto = new RecomendadorDestinos(true);
        var resultados = estricto.recomendarDestinos(List.of(ciudad(600)),
                new PreferenciasUsuario(1000, Map.of(), 2));
        assertEquals(List.of(), resultados);
    }

    private Ciudad ciudad(double costoDiario) {
        Ciudad ciudad = new Ciudad();
        ciudad.setCostoPromedio(costoDiario);
        return ciudad;
    }
}

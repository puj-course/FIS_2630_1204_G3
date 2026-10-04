package com.wisetrip.servicio;

import com.wisetrip.modelo.Ciudad;
import com.wisetrip.modelo.Importancia;
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

    @Test
    void soloPuntuaValidasYSeparaIncumplimientoDeInformacionAusente() {
        Ciudad valida = ciudad(100);
        valida.setAtributos(Map.of("playa", true, "museos", true));
        Ciudad incumple = ciudad(100);
        incumple.setAtributos(Map.of("playa", false, "museos", true));
        Ciudad pendiente = ciudad(100);
        pendiente.setAtributos(Map.of("museos", true));
        Ciudad sinCosto = ciudad(Double.NaN);
        sinCosto.setAtributos(Map.of("playa", true));
        var preferencias = new PreferenciasUsuario(1000,
                Map.of("playa", Importancia.si, "museos", Importancia.prefiero), 2);
        var puntuadas = new java.util.ArrayList<Ciudad>();
        var estricto = new RecomendadorDestinos(true) {
            @Override
            public ResultadoRecomendacion calcularPuntajeTotal(Ciudad ciudad, PreferenciasUsuario p) {
                puntuadas.add(ciudad);
                return super.calcularPuntajeTotal(ciudad, p);
            }
        };
        var ciudades = List.of(incumple, pendiente, sinCosto, valida);
        var validacion = estricto.validarCiudades(ciudades, preferencias);
        assertEquals(List.of(valida), validacion.validas());
        assertEquals(List.of(incumple), validacion.descartadas());
        assertEquals(List.of(pendiente, sinCosto), validacion.pendientes());
        estricto.recomendarDestinos(ciudades, preferencias);
        assertEquals(List.of(valida), puntuadas);
    }

    @Test
    void incumplimientoConfirmadoPrevaleceAunqueOtroRequisitoSeaDesconocido() {
        Ciudad ciudad = ciudad(100);
        ciudad.setAtributos(Map.of("playa", false));
        var preferencias = new PreferenciasUsuario(1000,
                Map.of("playa", Importancia.si, "museos", Importancia.si), 2);
        var validacion = recomendador.validarCiudades(List.of(ciudad), preferencias);
        assertEquals(List.of(ciudad), validacion.descartadas());
        assertEquals(List.of(), validacion.pendientes());
    }

    @Test
    void coincidenciasPorCategoriaUsanImportanciaYConservanDatosFaltantesEnElMaximo() {
        var gustos = Map.of("playa", Importancia.gustar,
                "montana", Importancia.prefiero, "nieve", Importancia.no,
                "desierto", Importancia.si, "museos", Importancia.prefiero,
                "religioso", Importancia.gustar, "compras", Importancia.no);
        var oferta = Map.of("playa", true, "nieve", true, "desierto", true,
                "museos", true, "religioso", false);

        var coincidencias = recomendador.calcularCoincidenciasPorCategoria(oferta, gustos);

        assertEquals(2, coincidencias.size());
        assertEquals(1.0 / 3, coincidencias.get("Paisaje y clima"), 1e-9);
        assertEquals(2.0 / 3, coincidencias.get("Cultura"), 1e-9);
        assertEquals(Map.of(), recomendador.calcularCoincidenciasPorCategoria(
                oferta, Map.of("playa", Importancia.si, "museos", Importancia.no)));
    }

    private Ciudad ciudad(double costoDiario) {
        Ciudad ciudad = new Ciudad();
        ciudad.setCostoPromedio(costoDiario);
        return ciudad;
    }
}

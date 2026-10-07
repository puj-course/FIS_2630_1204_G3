package com.wisetrip.servicio;

import com.wisetrip.modelo.Ciudad;
import com.wisetrip.modelo.Importancia;
import com.wisetrip.modelo.PreferenciasUsuario;
import com.wisetrip.modelo.ResultadoRecomendacion;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
        assertEquals(1.0 / 4, coincidencias.get("Paisaje y clima"), 1e-9);
        assertEquals(3.0 / 4, coincidencias.get("Cultura"), 1e-9);
        assertEquals(Map.of(), recomendador.calcularCoincidenciasPorCategoria(
                oferta, Map.of("playa", Importancia.si, "museos", Importancia.no)));
    }

    @Test
    void ciudadConLoPreferidoGanaAunqueOtraAcumuleGustosSecundarios() {
        var gustos = Map.of("playa", Importancia.prefiero, "museos", Importancia.gustar,
                "compras", Importancia.gustar, "oferta_gastronomica", Importancia.gustar);
        Ciudad conPlaya = ciudad(100);
        conPlaya.setAtributos(Map.of("playa", true, "museos", false,
                "compras", false, "oferta_gastronomica", false));
        Ciudad soloSecundarios = ciudad(100);
        soloSecundarios.setAtributos(Map.of("playa", false, "museos", true,
                "compras", true, "oferta_gastronomica", true));
        var resultados = recomendador.recomendarDestinos(List.of(soloSecundarios, conPlaya),
                new PreferenciasUsuario(1000, gustos, 1));
        assertEquals(List.of(conPlaya, soloSecundarios), resultados.stream()
                .map(ResultadoRecomendacion::getCiudad).toList());
        assertEquals(0.5, resultados.get(0).getPuntajeTotal(), 1e-9);
        assertEquals(0.25, resultados.get(1).getPuntajeTotal(), 1e-9);
    }

    @Test
    void elCastigoEsProporcionalAlosPreferidosQueFaltan() {
        var gustos = Map.of("playa", Importancia.prefiero, "montana", Importancia.prefiero);
        var ninguno = Map.of("playa", false, "montana", false);
        var uno = Map.of("playa", true, "montana", false);
        var ambos = Map.of("playa", true, "montana", true);
        assertEquals(0.5, recomendador.calcularFactorPrefiero(ninguno, gustos), 1e-9);
        assertEquals(0.75, recomendador.calcularFactorPrefiero(uno, gustos), 1e-9);
        assertEquals(1.0, recomendador.calcularFactorPrefiero(ambos, gustos), 1e-9);
        assertEquals(0.5 * 0.75, recomendador.calcularPuntajePreferencias(uno, gustos), 1e-9);
    }

    @Test
    void loPreferidoPesaTresVecesMasQueMeGustaria() {
        assertEquals(1, Importancia.gustar.peso);
        assertEquals(3, Importancia.prefiero.peso);
        var gustos = Map.of("playa", Importancia.gustar, "museos", Importancia.prefiero);
        var soloGustar = Map.of("playa", true, "museos", false);
        var soloPrefiero = Map.of("playa", false, "museos", true);
        assertEquals(1.0 / 4, recomendador.calcularCoincidenciaPonderada(soloGustar, gustos), 1e-9);
        assertEquals(3.0 / 4, recomendador.calcularCoincidenciaPonderada(soloPrefiero, gustos), 1e-9);
    }

    @Test
    void indispensableDescartaLaCiudadAunqueTengaMuchosGustosSecundarios() {
        var gustos = Map.of("playa", Importancia.si, "museos", Importancia.gustar,
                "compras", Importancia.gustar, "oferta_gastronomica", Importancia.gustar);
        Ciudad sinPlaya = ciudad(100);
        sinPlaya.setAtributos(Map.of("playa", false, "museos", true,
                "compras", true, "oferta_gastronomica", true));
        Ciudad conPlaya = ciudad(100);
        conPlaya.setAtributos(Map.of("playa", true, "museos", false,
                "compras", false, "oferta_gastronomica", false));
        var preferencias = new PreferenciasUsuario(1000, gustos, 1);
        var validacion = recomendador.validarCiudades(List.of(sinPlaya, conPlaya), preferencias);
        assertEquals(List.of(conPlaya), validacion.validas());
        assertEquals(List.of(sinPlaya), validacion.descartadas());
        assertEquals(List.of(conPlaya), recomendador.recomendarDestinos(
                List.of(sinPlaya, conPlaya), preferencias).stream()
                .map(ResultadoRecomendacion::getCiudad).toList());
    }

    @Test
    void todasLasCiudadesSemillaQuedanEnExactamenteUnaLista() {
        var semillas = com.wisetrip.datos.DatosCiudades.CIUDADES;
        assertEquals(44, semillas.size());
        var ciudades = new java.util.ArrayList<Ciudad>();
        for (int i = 0; i < semillas.size(); i++) {
            Ciudad c = semillas.get(i).aCiudad(50 + 10 * i);   // costos distintos
            c.setAtributos(semillas.get(i).atributosManuales());
            ciudades.add(c);
        }
        var preferencias = new PreferenciasUsuario(1500,
                Map.of("urbano", Importancia.si, "cultura_historia", Importancia.prefiero,
                        "romantico", Importancia.gustar), 3);
        var validacion = new RecomendadorDestinos(true).validarCiudades(ciudades, preferencias);
        int total = validacion.validas().size() + validacion.descartadas().size()
                + validacion.pendientes().size();
        assertEquals(44, total);
        var todas = new java.util.HashSet<Ciudad>();
        todas.addAll(validacion.validas());
        todas.addAll(validacion.descartadas());
        todas.addAll(validacion.pendientes());
        assertEquals(44, todas.size());   // ninguna repetida
    }


    @Test
    void empateDeCoincidenciaConservaCercaniaAlPresupuesto() {
        Ciudad economica = ciudad(500);
        Ciudad cercana = ciudad(900);
        economica.setAtributos(Map.of("playa", true));
        cercana.setAtributos(Map.of("playa", true));
        var resultados = recomendador.recomendarDestinos(List.of(economica, cercana),
                new PreferenciasUsuario(1000, Map.of("playa", Importancia.gustar), 1));
        assertEquals(List.of(cercana, economica), resultados.stream()
                .map(ResultadoRecomendacion::getCiudad).toList());
    }

    @Test
    void desconocidosHacenProvisionalLaCoincidenciaSoloSiSonPuntuables() {
        Ciudad ciudad = ciudad(100);
        ciudad.setAtributos(Map.of("playa", true));
        var resultado = recomendador.calcularPuntajeTotal(ciudad, new PreferenciasUsuario(1000,
                Map.of("playa", Importancia.gustar, "montana", Importancia.prefiero), 1));
        assertEquals(1.0 / 4 * 0.5, resultado.getPuntajeTotal(), 1e-9);
        assertTrue(resultado.isCoincidenciaProvisional());
        assertTrue(resultado.isPreferenciasPuntuables());
        var sinIntereses = recomendador.calcularPuntajeTotal(ciudad, new PreferenciasUsuario(1000,
                Map.of("playa", Importancia.si, "montana", Importancia.no), 1));
        assertEquals(0.0, sinIntereses.getPuntajeTotal());
        assertFalse(sinIntereses.isPreferenciasPuntuables());
        assertFalse(sinIntereses.isCoincidenciaProvisional());
    }

    private Ciudad ciudad(double costoDiario) {
        Ciudad ciudad = new Ciudad();
        ciudad.setCostoPromedio(costoDiario);
        return ciudad;
    }
}

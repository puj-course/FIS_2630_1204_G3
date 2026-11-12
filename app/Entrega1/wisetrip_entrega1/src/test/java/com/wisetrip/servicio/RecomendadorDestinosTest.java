package com.wisetrip.servicio;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.wisetrip.modelo.Ciudad;
import com.wisetrip.modelo.Importancia;
import com.wisetrip.modelo.PreferenciasUsuario;
import com.wisetrip.modelo.ResultadoRecomendacion;
import com.wisetrip.modelo.ValidacionCiudades;

/**
 * Pruebas de RecomendadorDestinos. Usa el PreferenciasServicio real como gestor de preguntas
 * (no tiene dependencias externas), así que no hacen falta mocks.
 */
@DisplayName("RecomendadorDestinos")
class RecomendadorDestinosTest {

    private RecomendadorDestinos recomendador;

    @BeforeEach
    void setUp() {
        recomendador = new RecomendadorDestinos(true, new PreferenciasServicio());
    }

    private static Ciudad ciudad(String nombre, double costoDiario, Map<String, Boolean> atributos) {
        Ciudad ciudad = new Ciudad();
        ciudad.setNombre(nombre);
        ciudad.setPais("Colombia");
        ciudad.setCostoPromedio(costoDiario);
        ciudad.setAtributos(atributos);
        return ciudad;
    }

    private static PreferenciasUsuario preferencias(double presupuesto, long dias, Map<String, Importancia> atributos) {
        return new PreferenciasUsuario(presupuesto, atributos, dias);
    }

    // ------------------------------------------------------------ calcularPuntajePresupuesto

    @Test
    @DisplayName("puntaje de presupuesto: sin presupuesto el puntaje es 0")
    void puntajePresupuesto_presupuestoCero_cero() {
        assertEquals(0.0, recomendador.calcularPuntajePresupuesto(100, 0), 0.0001);
    }

    @Test
    @DisplayName("puntaje de presupuesto: si el costo cabe en el presupuesto el puntaje es 1")
    void puntajePresupuesto_costoDentro_uno() {
        assertEquals(1.0, recomendador.calcularPuntajePresupuesto(400, 500), 0.0001);
        assertEquals(1.0, recomendador.calcularPuntajePresupuesto(500, 500), 0.0001);
    }

    @Test
    @DisplayName("puntaje de presupuesto: el puntaje baja con el exceso y nunca es negativo")
    void puntajePresupuesto_exceso_descuentaYNoBajaDeCero() {
        assertEquals(0.5, recomendador.calcularPuntajePresupuesto(750, 500), 0.0001);
        assertEquals(0.0, recomendador.calcularPuntajePresupuesto(1000, 500), 0.0001);
        assertEquals(0.0, recomendador.calcularPuntajePresupuesto(5000, 500), 0.0001);
    }

    // ------------------------------------------------------------ calcularCoincidenciaPonderada

    @Test
    @DisplayName("coincidencia: si la ciudad cumple lo que prefiero el puntaje es 1")
    void coincidencia_cumpleTodo_uno() {
        double puntaje = recomendador.calcularCoincidenciaPonderada(
                Map.of("playa", true), Map.of("playa", Importancia.PREFIERO));

        assertEquals(1.0, puntaje, 0.0001);
    }

    @Test
    @DisplayName("coincidencia: si no cumple algo que prefiero, el puntaje no baja de 0")
    void coincidencia_incumplePrefiero_noEsNegativo() {
        double puntaje = recomendador.calcularCoincidenciaPonderada(
                Map.of("playa", false), Map.of("playa", Importancia.PREFIERO));

        assertEquals(0.0, puntaje, 0.0001);
    }

    @Test
    @DisplayName("coincidencia: pondera por peso (prefiero=3, gustar=1)")
    void coincidencia_mezcla_ponderaPorPeso() {
        double puntaje = recomendador.calcularCoincidenciaPonderada(
                Map.of("playa", true, "lujo", true, "museos", false),
                Map.of("playa", Importancia.PREFIERO, "lujo", Importancia.GUSTAR, "museos", Importancia.GUSTAR));

        // peso total = 3 + 1 + 1 = 5; logrado = 3 + 1 + 0 = 4
        assertEquals(0.8, puntaje, 0.0001);
    }

    @Test
    @DisplayName("coincidencia: un dato desconocido de la ciudad no suma")
    void coincidencia_datoDesconocido_noSuma() {
        double puntaje = recomendador.calcularCoincidenciaPonderada(
                Map.of(), Map.of("playa", Importancia.GUSTAR));

        assertEquals(0.0, puntaje, 0.0001);
    }

    @Test
    @DisplayName("coincidencia: sin preferencias puntuables el puntaje es 0")
    void coincidencia_sinPuntuables_cero() {
        double puntaje = recomendador.calcularCoincidenciaPonderada(
                Map.of("playa", true), Map.of("playa", Importancia.SI, "lujo", Importancia.NO));

        assertEquals(0.0, puntaje, 0.0001);
    }

    // ------------------------------------------------------------ calcularPuntajeTotal / diferencia / excede

    @Test
    @DisplayName("calcularPuntajeTotal: ciudad que cumple y cabe en el presupuesto")
    void calcularPuntajeTotal_cumpleYCabe() {
        Ciudad playa = ciudad("Santa Marta", 100, Map.of("playa", true));
        PreferenciasUsuario prefs = preferencias(500, 3, Map.of("playa", Importancia.PREFIERO));

        ResultadoRecomendacion resultado = recomendador.calcularPuntajeTotal(playa, prefs);

        assertEquals(1.0, resultado.getPuntajePresupuesto(), 0.0001);
        assertEquals(1.0, resultado.getPuntajePreferencias(), 0.0001);
        assertEquals(1.0, resultado.getPuntajeTotal(), 0.0001);
        assertTrue(resultado.isPreferenciasPuntuables());
        assertFalse(resultado.isCoincidenciaProvisional());
    }

    @Test
    @DisplayName("calcularPuntajeTotal: es provisional si falta un dato de la ciudad que el usuario pide")
    void calcularPuntajeTotal_datoFaltante_esProvisional() {
        Ciudad sinDato = ciudad("Leticia", 100, Map.of());
        PreferenciasUsuario prefs = preferencias(500, 3, Map.of("playa", Importancia.GUSTAR));

        ResultadoRecomendacion resultado = recomendador.calcularPuntajeTotal(sinDato, prefs);

        assertTrue(resultado.isCoincidenciaProvisional());
    }

    @Test
    @DisplayName("diferenciaPresupuesto: distancia absoluta entre el costo del viaje y el presupuesto")
    void diferenciaPresupuesto_distanciaAbsoluta() {
        Ciudad ciudad = ciudad("Cali", 100, Map.of());

        assertEquals(200.0, recomendador.diferenciaPresupuesto(ciudad, preferencias(500, 3, Map.of())), 0.0001);
        assertEquals(100.0, recomendador.diferenciaPresupuesto(ciudad, preferencias(200, 3, Map.of())), 0.0001);
    }

    @Test
    @DisplayName("excedePresupuesto: solo es true cuando el costo supera el presupuesto")
    void excedePresupuesto_segunElCosto() {
        Ciudad ciudad = ciudad("Cali", 100, Map.of());

        assertTrue(recomendador.excedePresupuesto(ciudad, preferencias(299, 3, Map.of())));
        assertFalse(recomendador.excedePresupuesto(ciudad, preferencias(300, 3, Map.of())));
    }

    @Test
    @DisplayName("excedePresupuesto: sin costo conocido no se considera que exceda")
    void excedePresupuesto_costoDesconocido_false() {
        Ciudad sinCosto = ciudad("Cali", 0, Map.of());

        assertFalse(recomendador.excedePresupuesto(sinCosto, preferencias(1, 3, Map.of())));
    }

    @Test
    @DisplayName("excedePresupuesto: si el presupuesto no es obligatorio nunca descarta por costo")
    void excedePresupuesto_noObligatorio_false() {
        RecomendadorDestinos flexible = new RecomendadorDestinos(false, new PreferenciasServicio());
        Ciudad cara = ciudad("Cali", 1000, Map.of());

        assertFalse(flexible.excedePresupuesto(cara, preferencias(10, 3, Map.of())));
    }

    // ------------------------------------------------------------ validarCiudades

    @Test
    @DisplayName("validarCiudades clasifica en válidas, descartadas y pendientes")
    void validarCiudades_clasificaLasCiudades() {
        Ciudad valida = ciudad("Valida", 100, Map.of("playa", true));
        Ciudad cara = ciudad("Cara", 500, Map.of("playa", true));
        Ciudad sinCosto = ciudad("SinCosto", 0, Map.of("playa", true));
        PreferenciasUsuario prefs = preferencias(500, 3, Map.of());

        ValidacionCiudades resultado = recomendador.validarCiudades(List.of(valida, cara, sinCosto), prefs);

        assertEquals(List.of(valida), resultado.validas());
        assertEquals(List.of(cara), resultado.descartadas());
        assertEquals(List.of(sinCosto), resultado.pendientes());
    }

    @Test
    @DisplayName("validarCiudades: un requisito indispensable que la ciudad no cumple la descarta")
    void validarCiudades_requisitoIndispensableIncumplido_descarta() {
        Ciudad sinPlaya = ciudad("Bogota", 100, Map.of("playa", false));
        PreferenciasUsuario prefs = preferencias(500, 3, Map.of("playa", Importancia.SI));

        ValidacionCiudades resultado = recomendador.validarCiudades(List.of(sinPlaya), prefs);

        assertEquals(List.of(sinPlaya), resultado.descartadas());
        assertTrue(resultado.validas().isEmpty());
    }

    @Test
    @DisplayName("validarCiudades: un requisito indispensable sin dato deja la ciudad pendiente")
    void validarCiudades_requisitoIndispensableSinDato_pendiente() {
        Ciudad sinDato = ciudad("Leticia", 100, Map.of());
        PreferenciasUsuario prefs = preferencias(500, 3, Map.of("playa", Importancia.SI));

        ValidacionCiudades resultado = recomendador.validarCiudades(List.of(sinDato), prefs);

        assertEquals(List.of(sinDato), resultado.pendientes());
    }

    // ------------------------------------------------------------ puntuarCiudadesValidas

    @Test
    @DisplayName("puntuarCiudadesValidas: sin preferencias puntuables ordena por cercanía al presupuesto")
    void puntuar_sinPuntuables_ordenaPorPresupuesto() {
        Ciudad lejos = ciudad("Lejos", 100, Map.of());   // costo 300, diferencia 200
        Ciudad cerca = ciudad("Cerca", 160, Map.of());   // costo 480, diferencia 20
        ValidacionCiudades validacion = new ValidacionCiudades(List.of(lejos, cerca), List.of(), List.of());

        List<ResultadoRecomendacion> orden = recomendador.puntuarCiudadesValidas(validacion, preferencias(500, 3, Map.of()));

        assertEquals("Cerca", orden.get(0).getCiudad().getNombre());
        assertEquals("Lejos", orden.get(1).getCiudad().getNombre());
    }

    @Test
    @DisplayName("puntuarCiudadesValidas: con puntajes distintos ordena de mayor a menor")
    void puntuar_conPuntajesDistintos_ordenaPorPuntaje() {
        Ciudad sinPlaya = ciudad("SinPlaya", 160, Map.of("playa", false));
        Ciudad conPlaya = ciudad("ConPlaya", 100, Map.of("playa", true));
        ValidacionCiudades validacion = new ValidacionCiudades(List.of(sinPlaya, conPlaya), List.of(), List.of());

        List<ResultadoRecomendacion> orden = recomendador.puntuarCiudadesValidas(
                validacion, preferencias(500, 3, Map.of("playa", Importancia.PREFIERO)));

        assertEquals("ConPlaya", orden.get(0).getCiudad().getNombre());
        assertEquals("SinPlaya", orden.get(1).getCiudad().getNombre());
    }

    @Test
    @DisplayName("puntuarCiudadesValidas: con la misma coincidencia desempata por presupuesto")
    void puntuar_conEmpate_desempataPorPresupuesto() {
        Ciudad lejos = ciudad("Lejos", 100, Map.of("playa", true));
        Ciudad cerca = ciudad("Cerca", 160, Map.of("playa", true));
        ValidacionCiudades validacion = new ValidacionCiudades(List.of(lejos, cerca), List.of(), List.of());

        List<ResultadoRecomendacion> orden = recomendador.puntuarCiudadesValidas(
                validacion, preferencias(500, 3, Map.of("playa", Importancia.PREFIERO)));

        assertEquals("Cerca", orden.get(0).getCiudad().getNombre());
    }

    // ------------------------------------------------------------ casos borde

    @Test
    @DisplayName("coincidencia: un 'prefiero' sin dato de la ciudad conserva su peso pero no resta")
    void coincidencia_prefieroDesconocido_noResta() {
        double puntaje = recomendador.calcularCoincidenciaPonderada(
                Map.of("lujo", true),
                Map.of("playa", Importancia.PREFIERO, "lujo", Importancia.GUSTAR));

        // peso total = 3 + 1 = 4; logrado = 0 (playa desconocida, no resta) + 1 = 1
        assertEquals(0.25, puntaje, 0.0001);
    }

    @Test
    @DisplayName("calcularPuntajeTotal: respuestas sin valor o indispensables no vuelven provisional el puntaje")
    void calcularPuntajeTotal_preferenciaNulaOIndispensable_noEsProvisional() {
        Ciudad sinDatos = ciudad("Leticia", 100, Map.of());
        Map<String, Importancia> gustos = new LinkedHashMap<>();
        gustos.put("playa", null);
        gustos.put("museos", Importancia.SI);

        ResultadoRecomendacion resultado = recomendador.calcularPuntajeTotal(sinDatos, preferencias(500, 3, gustos));

        assertFalse(resultado.isCoincidenciaProvisional());
    }

    @Test
    @DisplayName("excedePresupuesto: borde, un costo no numerico (NaN) no confirma un exceso")
    void excedePresupuesto_costoNoNumerico_false() {
        Ciudad costoInvalido = ciudad("Cali", Double.NaN, Map.of());

        assertFalse(recomendador.excedePresupuesto(costoInvalido, preferencias(1, 3, Map.of())));
    }

    @Test
    @DisplayName("validarCiudades: borde, una ciudad con costo no numerico queda pendiente")
    void validarCiudades_costoNoNumerico_pendiente() {
        Ciudad costoInvalido = ciudad("Cali", Double.NaN, Map.of());

        ValidacionCiudades resultado = recomendador.validarCiudades(
                List.of(costoInvalido), preferencias(500, 3, Map.of()));

        assertEquals(List.of(costoInvalido), resultado.pendientes());
    }

    @Test
    @DisplayName("validarCiudades: las respuestas sin valor o que no son indispensables no descartan la ciudad")
    void validarCiudades_preferenciasNoIndispensables_seIgnoran() {
        Ciudad sinDatos = ciudad("Cali", 100, Map.of("museos", false));
        Map<String, Importancia> gustos = new LinkedHashMap<>();
        gustos.put("playa", null);
        gustos.put("museos", Importancia.GUSTAR);

        ValidacionCiudades resultado = recomendador.validarCiudades(List.of(sinDatos), preferencias(500, 3, gustos));

        assertEquals(List.of(sinDatos), resultado.validas());
    }

    @Test
    @DisplayName("puntuarCiudadesValidas: borde, con una sola ciudad la devuelve sin problema")
    void puntuar_unaSolaCiudad_laDevuelve() {
        Ciudad unica = ciudad("Unica", 100, Map.of("playa", true));
        ValidacionCiudades validacion = new ValidacionCiudades(List.of(unica), List.of(), List.of());

        List<ResultadoRecomendacion> orden = recomendador.puntuarCiudadesValidas(
                validacion, preferencias(500, 3, Map.of("playa", Importancia.PREFIERO)));

        assertEquals(1, orden.size());
        assertEquals("Unica", orden.get(0).getCiudad().getNombre());
    }

    @Test
    @DisplayName("puntuarCiudadesValidas: si solo algunas empatan, ordena por puntaje y desempata esas por presupuesto")
    void puntuar_empateParcial_ordenaPorPuntajeYDesempata() {
        Ciudad lejos = ciudad("Lejos", 100, Map.of("playa", true));      // puntaje 1, diferencia 200
        Ciudad cerca = ciudad("Cerca", 160, Map.of("playa", true));      // puntaje 1, diferencia 20
        Ciudad sinPlaya = ciudad("SinPlaya", 150, Map.of("playa", false)); // puntaje 0
        ValidacionCiudades validacion = new ValidacionCiudades(
                List.of(lejos, sinPlaya, cerca), List.of(), List.of());

        List<ResultadoRecomendacion> orden = recomendador.puntuarCiudadesValidas(
                validacion, preferencias(500, 3, Map.of("playa", Importancia.PREFIERO)));

        assertEquals("Cerca", orden.get(0).getCiudad().getNombre());
        assertEquals("Lejos", orden.get(1).getCiudad().getNombre());
        assertEquals("SinPlaya", orden.get(2).getCiudad().getNombre());
    }
}

package com.wisetrip.servicio;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.wisetrip.modelo.Ciudad;
import com.wisetrip.modelo.Importancia;
import com.wisetrip.modelo.PreferenciasUsuario;
import com.wisetrip.modelo.ResultadoRecomendacion;
import com.wisetrip.modelo.ValidacionCiudades;

/**
 * Pruebas unitarias de RecomendadorDestinos: puntaje por presupuesto,
 * coincidencia ponderada de gustos, filtro de requisitos indispensables
 * y orden final de las recomendaciones.
 *
 * Usa el catálogo real de preguntas (PreferenciasServicio), que vive en
 * memoria; no hay base de datos ni Geoapify.
 */
@DisplayName("RecomendadorDestinos")
class RecomendadorDestinosTest {

    private static final double DELTA = 0.0001;
    private static final long DIAS = 5;

    private RecomendadorDestinos recomendador;

    @BeforeEach
    void setUp() {
        recomendador = new RecomendadorDestinos(true, new PreferenciasServicio());
    }

    /** Ciudad con costo diario y atributos dados como pares clave, valor (null = no sabemos). */
    private static Ciudad ciudad(String nombre, double costoDiario, Object... atributos) {
        Ciudad ciudad = new Ciudad();
        ciudad.setNombre(nombre);
        ciudad.setCostoPromedio(costoDiario);
        Map<String, Boolean> mapa = new HashMap<>();
        for (int i = 0; i < atributos.length; i += 2) {
            mapa.put((String) atributos[i], (Boolean) atributos[i + 1]);
        }
        ciudad.setAtributos(mapa);
        return ciudad;
    }

    private static PreferenciasUsuario preferencias(double presupuestoUsd, Map<String, Importancia> gustos) {
        return new PreferenciasUsuario(presupuestoUsd, gustos, DIAS);
    }

    private static List<String> nombres(List<Ciudad> ciudades) {
        return ciudades.stream().map(Ciudad::getNombre).toList();
    }

    // ------------------------------------------------------------ calcularPuntajePresupuesto

    @Nested
    @DisplayName("calcularPuntajePresupuesto")
    class PuntajePresupuesto {

        @Test
        @DisplayName("dentro del presupuesto el puntaje es 1 (no premia ahorrar)")
        void dentroDelPresupuesto_uno() {
            assertEquals(1.0, recomendador.calcularPuntajePresupuesto(300, 1000), DELTA);
        }

        @Test
        @DisplayName("borde: costo igual al presupuesto también es 1")
        void costoIgualAlPresupuesto_uno() {
            assertEquals(1.0, recomendador.calcularPuntajePresupuesto(1000, 1000), DELTA);
        }

        @Test
        @DisplayName("pasarse un 20% baja el puntaje a 0.8")
        void excesoDelVeintePorCiento() {
            assertEquals(0.8, recomendador.calcularPuntajePresupuesto(1200, 1000), DELTA);
        }

        @Test
        @DisplayName("borde: un exceso mayor al 100% nunca da puntaje negativo")
        void excesoEnorme_ceroNoNegativo() {
            assertEquals(0.0, recomendador.calcularPuntajePresupuesto(2500, 1000), DELTA);
        }

        @Test
        @DisplayName("borde: presupuesto en cero da puntaje 0")
        void presupuestoCero_cero() {
            assertEquals(0.0, recomendador.calcularPuntajePresupuesto(100, 0), DELTA);
        }
    }

    // ------------------------------------------------------------ calcularCoincidenciaPonderada

    @Nested
    @DisplayName("calcularCoincidenciaPonderada")
    class Coincidencia {

        // playa vale 3 (prefiero) y museos vale 1 (gustar): peso total 4
        private final Map<String, Importancia> gustos =
                Map.of("playa", Importancia.prefiero, "museos", Importancia.gustar);

        @Test
        @DisplayName("si cumple todo, la coincidencia es 1")
        void cumpleTodo_uno() {
            assertEquals(1.0, recomendador.calcularCoincidenciaPonderada(
                    Map.of("playa", true, "museos", true), gustos), DELTA);
        }

        @Test
        @DisplayName("cumplir solo lo 'prefiero' da 3 de 4")
        void soloPrefiero_tresCuartos() {
            assertEquals(0.75, recomendador.calcularCoincidenciaPonderada(
                    Map.of("playa", true, "museos", false), gustos), DELTA);
        }

        @Test
        @DisplayName("incumplir un 'prefiero' resta 1 punto")
        void incumplePrefiero_resta() {
            // logrado = 1 (museos) - 1 (playa incumplida) = 0
            assertEquals(0.0, recomendador.calcularCoincidenciaPonderada(
                    Map.of("playa", false, "museos", true), gustos), DELTA);
        }

        @Test
        @DisplayName("un dato desconocido no suma pero tampoco resta")
        void desconocido_noResta() {
            // playa desconocida: solo suma museos -> 1 de 4
            assertEquals(0.25, recomendador.calcularCoincidenciaPonderada(
                    Map.of("museos", true), gustos), DELTA);
        }

        @Test
        @DisplayName("borde: sin gustos puntuables la coincidencia es 0")
        void sinGustosPuntuables_cero() {
            assertEquals(0.0, recomendador.calcularCoincidenciaPonderada(
                    Map.of("playa", true), Map.of("playa", Importancia.si)), DELTA);
        }
    }

    // ------------------------------------------------------------ calcularPuntajeTotal

    @Test
    @DisplayName("calcularPuntajeTotal marca provisional si falta el dato de un gusto")
    void puntajeTotal_datoFaltante_provisional() {
        PreferenciasUsuario prefs = preferencias(1000, Map.of("playa", Importancia.gustar));
        Ciudad sinDato = ciudad("Bogota", 100);

        ResultadoRecomendacion resultado = recomendador.calcularPuntajeTotal(sinDato, prefs);

        assertTrue(resultado.isCoincidenciaProvisional());
        assertEquals(1.0, resultado.getPuntajePresupuesto(), DELTA); // 100 x 5 = 500 <= 1000
    }

    // ------------------------------------------------------------ excedePresupuesto

    @Nested
    @DisplayName("excedePresupuesto")
    class ExcedePresupuesto {

        private final PreferenciasUsuario prefs = preferencias(400, Map.of());

        @Test
        @DisplayName("costo del viaje mayor al presupuesto lo excede")
        void costoMayor_excede() {
            assertTrue(recomendador.excedePresupuesto(ciudad("Paris", 100), prefs)); // 500 > 400
        }

        @Test
        @DisplayName("borde: costo desconocido (0) no confirma un exceso")
        void costoDesconocido_noExcede() {
            assertFalse(recomendador.excedePresupuesto(ciudad("Sin datos", 0), prefs));
        }

        @Test
        @DisplayName("si el presupuesto no es obligatorio nunca excede")
        void presupuestoNoObligatorio_noExcede() {
            RecomendadorDestinos flexible = new RecomendadorDestinos(false, new PreferenciasServicio());

            assertFalse(flexible.excedePresupuesto(ciudad("Paris", 100), prefs));
        }
    }

    // ------------------------------------------------------------ validarCiudades

    @Test
    @DisplayName("validarCiudades separa válidas, descartadas y pendientes")
    void validarCiudades_separaEnTresGrupos() {
        PreferenciasUsuario prefs = preferencias(400, Map.of("playa", Importancia.si));
        List<Ciudad> ciudades = List.of(
                ciudad("ConPlaya", 50, "playa", true),        // 250 <= 400 y cumple -> válida
                ciudad("SinPlaya", 50, "playa", false),       // incumple -> descartada
                ciudad("PlayaDesconocida", 50),               // no sabemos -> pendiente
                ciudad("MuyCara", 100, "playa", true),        // 500 > 400 -> descartada
                ciudad("SinCosto", 0, "playa", true),         // costo desconocido -> pendiente
                ciudad("CaraYDesconocida", 100));             // exceso confirmado gana -> descartada

        ValidacionCiudades validacion = recomendador.validarCiudades(ciudades, prefs);

        assertEquals(List.of("ConPlaya"), nombres(validacion.validas()));
        assertEquals(List.of("SinPlaya", "MuyCara", "CaraYDesconocida"), nombres(validacion.descartadas()));
        assertEquals(List.of("PlayaDesconocida", "SinCosto"), nombres(validacion.pendientes()));
    }

    // ------------------------------------------------------------ puntuarCiudadesValidas

    @Test
    @DisplayName("ordena por puntaje y desempata por cercanía al presupuesto")
    void puntuar_ordenaPorPuntajeYDesempata() {
        PreferenciasUsuario prefs = preferencias(1000,
                Map.of("playa", Importancia.gustar, "museos", Importancia.gustar));
        ValidacionCiudades validacion = new ValidacionCiudades(List.of(
                ciudad("Lejana", 150, "playa", true),                  // 0.5, costo 750
                ciudad("Completa", 100, "playa", true, "museos", true), // 1.0, costo 500
                ciudad("Cercana", 190, "playa", true)),                 // 0.5, costo 950
                List.of(), List.of());

        List<ResultadoRecomendacion> ranking = recomendador.puntuarCiudadesValidas(validacion, prefs);

        assertEquals(List.of("Completa", "Cercana", "Lejana"),
                ranking.stream().map(r -> r.getCiudad().getNombre()).toList());
    }

    @Test
    @DisplayName("sin gustos puntuables ordena solo por cercanía al presupuesto")
    void puntuar_sinGustos_ordenaPorPresupuesto() {
        PreferenciasUsuario prefs = preferencias(1000, Map.of("playa", Importancia.si));
        ValidacionCiudades validacion = new ValidacionCiudades(List.of(
                ciudad("Diferencia500", 100, "playa", true),
                ciudad("Diferencia50", 190, "playa", true),
                ciudad("Diferencia250", 150, "playa", true)),
                List.of(), List.of());

        List<ResultadoRecomendacion> ranking = recomendador.puntuarCiudadesValidas(validacion, prefs);

        assertEquals(List.of("Diferencia50", "Diferencia250", "Diferencia500"),
                ranking.stream().map(r -> r.getCiudad().getNombre()).toList());
    }
}

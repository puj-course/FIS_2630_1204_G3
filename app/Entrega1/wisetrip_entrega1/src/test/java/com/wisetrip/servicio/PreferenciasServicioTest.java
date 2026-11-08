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

import com.wisetrip.modelo.CategoriaPreferencia;
import com.wisetrip.modelo.Importancia;
import com.wisetrip.modelo.Preferencias;
import com.wisetrip.modelo.Pregunta;

/**
 * Pruebas unitarias de PreferenciasServicio: cuestionario de preferencias
 * del viajero (validación de respuestas, importancias y resumen).
 */
@DisplayName("PreferenciasServicio")
class PreferenciasServicioTest {

    private PreferenciasServicio servicio;
    private Preferencias preferencias;

    @BeforeEach
    void setUp() {
        servicio = new PreferenciasServicio();
        preferencias = new Preferencias();
        preferencias.setRespuestas(todasCon("no"));
    }

    /** Responde todas las preguntas del cuestionario con el mismo valor. */
    private Map<String, String> todasCon(String valor) {
        Map<String, String> respuestas = new LinkedHashMap<>();
        for (CategoriaPreferencia categoria : servicio.listarCategorias()) {
            for (Pregunta pregunta : categoria.getPreguntas()) {
                respuestas.put(pregunta.getClave(), valor);
            }
        }
        return respuestas;
    }

    // ------------------------------------------------------------ catálogo

    @Test
    @DisplayName("el cuestionario tiene 9 categorías y 29 preguntas")
    void catalogo_categoriasYPreguntas() {
        assertEquals(9, servicio.listarCategorias().size());
        assertEquals(29, servicio.totalPreguntas());
    }

    // ------------------------------------------------------------ validarPreferencias

    @Test
    @DisplayName("todas las preguntas respondidas con valores válidos no tienen errores")
    void validar_todoRespondido_sinErrores() {
        assertTrue(servicio.validarPreferencias(preferencias).isEmpty());
    }

    @Test
    @DisplayName("sin respuestas, cada pregunta reporta que falta responderla")
    void validar_sinRespuestas_todasFaltan() {
        Map<String, String> errores = servicio.validarPreferencias(new Preferencias());

        assertEquals(servicio.totalPreguntas(), errores.size());
        assertEquals("Falta responder esta pregunta.", errores.get("playa"));
    }

    @Test
    @DisplayName("una respuesta que no es un nivel de importancia se rechaza")
    void validar_respuestaInvalida_error() {
        preferencias.getRespuestas().put("playa", "talvez");

        Map<String, String> errores = servicio.validarPreferencias(preferencias);

        assertEquals(1, errores.size());
        assertEquals("Respuesta no válida.", errores.get("playa"));
    }

    @Test
    @DisplayName("borde: una respuesta en blanco cuenta como sin responder")
    void validar_respuestaEnBlanco_falta() {
        preferencias.getRespuestas().put("museos", "  ");

        assertEquals("Falta responder esta pregunta.",
                servicio.validarPreferencias(preferencias).get("museos"));
    }

    // ------------------------------------------------------------ conversiones

    @Test
    @DisplayName("obtenerImportancias convierte cada respuesta a su nivel")
    void obtenerImportancias_convierte() {
        preferencias.getRespuestas().put("playa", "prefiero");
        preferencias.getRespuestas().put("museos", "si");

        Map<String, Importancia> niveles = servicio.obtenerImportancias(preferencias);

        assertEquals(Importancia.prefiero, niveles.get("playa"));
        assertEquals(Importancia.si, niveles.get("museos"));
        assertEquals(Importancia.no, niveles.get("compras"));
    }

    @Test
    @DisplayName("obtenerAtributosSeleccionados marca gustar, prefiero y si como true")
    void atributosSeleccionados_marcaLosAfirmativos() {
        preferencias.getRespuestas().put("playa", "gustar");
        preferencias.getRespuestas().put("museos", "prefiero");
        preferencias.getRespuestas().put("compras", "si");

        Map<String, Boolean> atributos = servicio.obtenerAtributosSeleccionados(preferencias);

        assertTrue(atributos.get("playa"));
        assertTrue(atributos.get("museos"));
        assertTrue(atributos.get("compras"));
        assertFalse(atributos.get("nieve"));
    }

    @Test
    @DisplayName("contarAfirmativas cuenta solo gustar, prefiero y si")
    void contarAfirmativas_cuentaSoloLosAfirmativos() {
        preferencias.getRespuestas().put("playa", "gustar");
        preferencias.getRespuestas().put("museos", "prefiero");
        preferencias.getRespuestas().put("compras", "si");

        assertEquals(3, servicio.contarAfirmativas(preferencias));
    }

    @Test
    @DisplayName("borde: si todo es 'no' no hay afirmativas")
    void contarAfirmativas_todoNo_cero() {
        assertEquals(0, servicio.contarAfirmativas(preferencias));
    }

    // ------------------------------------------------------------ resumen

    @Test
    @DisplayName("resumenPorCategoria muestra las etiquetas de lo elegido en cada categoría")
    void resumen_muestraEtiquetasPorCategoria() {
        preferencias.getRespuestas().put("playa", "gustar");
        preferencias.getRespuestas().put("nieve", "si");

        Map<String, List<String>> resumen = servicio.resumenPorCategoria(preferencias);

        assertEquals(List.of("Playa", "Nieve"), resumen.get("Paisaje y clima"));
        assertTrue(resumen.get("Cultura").isEmpty());
        assertEquals(9, resumen.size());
    }
}

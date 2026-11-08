package com.wisetrip.servicio;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.wisetrip.modelo.CategoriaPreferencia;
import com.wisetrip.modelo.Importancia;
import com.wisetrip.modelo.Pregunta;
import com.wisetrip.modelo.Preferencias;

@DisplayName("PreferenciasServicio")
class PreferenciasServicioTest {

    private PreferenciasServicio servicio;

    @BeforeEach
    void setUp() {
        servicio = new PreferenciasServicio();
    }

    /** Preferencias con la misma respuesta para todas las preguntas del cuestionario. */
    private Preferencias respondiendoTodoCon(String valor) {
        Preferencias preferencias = new Preferencias();
        for (CategoriaPreferencia categoria : servicio.listarCategorias()) {
            for (Pregunta pregunta : categoria.getPreguntas()) {
                preferencias.getRespuestas().put(pregunta.getClave(), valor);
            }
        }
        return preferencias;
    }

    @Test
    @DisplayName("listarCategorias devuelve las 9 categorías del cuestionario")
    void listarCategorias_devuelveNueveCategorias() {
        assertEquals(9, servicio.listarCategorias().size());
    }

    @Test
    @DisplayName("totalPreguntas suma las preguntas de todas las categorías")
    void totalPreguntas_sumaTodasLasCategorias() {
        int esperado = servicio.listarCategorias().stream().mapToInt(c -> c.getPreguntas().size()).sum();

        assertEquals(esperado, servicio.totalPreguntas());
        assertEquals(29, servicio.totalPreguntas());
    }

    @Test
    @DisplayName("validarPreferencias: con todas las preguntas respondidas no hay errores")
    void validarPreferencias_todasRespondidas_sinErrores() {
        Map<String, String> errores = servicio.validarPreferencias(respondiendoTodoCon("gustar"));

        assertTrue(errores.isEmpty());
    }

    @Test
    @DisplayName("validarPreferencias: sin respuestas, cada pregunta genera un error")
    void validarPreferencias_sinRespuestas_unErrorPorPregunta() {
        Map<String, String> errores = servicio.validarPreferencias(new Preferencias());

        assertEquals(servicio.totalPreguntas(), errores.size());
        assertEquals("Falta responder esta pregunta.", errores.get("playa"));
    }

    @Test
    @DisplayName("validarPreferencias: una respuesta en blanco cuenta como no respondida")
    void validarPreferencias_respuestaEnBlanco_error() {
        Preferencias preferencias = respondiendoTodoCon("si");
        preferencias.getRespuestas().put("playa", "  ");

        Map<String, String> errores = servicio.validarPreferencias(preferencias);

        assertEquals(1, errores.size());
        assertEquals("Falta responder esta pregunta.", errores.get("playa"));
    }

    @Test
    @DisplayName("validarPreferencias: una respuesta fuera de las cuatro opciones es inválida")
    void validarPreferencias_respuestaInvalida_error() {
        Preferencias preferencias = respondiendoTodoCon("no");
        preferencias.getRespuestas().put("hispanohablante", "quizas");

        Map<String, String> errores = servicio.validarPreferencias(preferencias);

        assertEquals(1, errores.size());
        assertEquals("Respuesta no válida.", errores.get("hispanohablante"));
    }

    @Test
    @DisplayName("obtenerImportancias convierte cada respuesta del formulario en su nivel")
    void obtenerImportancias_convierteRespuestas() {
        Preferencias preferencias = respondiendoTodoCon("no");
        preferencias.getRespuestas().put("playa", "prefiero");
        preferencias.getRespuestas().put("lujo", "si");
        preferencias.getRespuestas().put("hispanohablante", "gustar");

        Map<String, Importancia> niveles = servicio.obtenerImportancias(preferencias);

        assertEquals(servicio.totalPreguntas(), niveles.size());
        assertEquals(Importancia.PREFIERO, niveles.get("playa"));
        assertEquals(Importancia.SI, niveles.get("lujo"));
        assertEquals(Importancia.GUSTAR, niveles.get("hispanohablante"));
        assertEquals(Importancia.NO, niveles.get("museos"));
    }

    @Test
    @DisplayName("obtenerImportancias: una respuesta desconocida lanza IllegalArgumentException")
    void obtenerImportancias_respuestaDesconocida_lanzaExcepcion() {
        Preferencias preferencias = respondiendoTodoCon("no");
        preferencias.getRespuestas().put("playa", "quizas");

        assertThrows(IllegalArgumentException.class, () -> servicio.obtenerImportancias(preferencias));
    }

    @Test
    @DisplayName("obtenerAtributosSeleccionados: gustar, prefiero y si son true; no y vacío son false")
    void obtenerAtributosSeleccionados_marcaLasAfirmativas() {
        Preferencias preferencias = new Preferencias();
        preferencias.getRespuestas().put("playa", "gustar");
        preferencias.getRespuestas().put("lujo", "prefiero");
        preferencias.getRespuestas().put("hispanohablante", "si");
        preferencias.getRespuestas().put("museos", "no");

        Map<String, Boolean> atributos = servicio.obtenerAtributosSeleccionados(preferencias);

        assertEquals(servicio.totalPreguntas(), atributos.size());
        assertTrue(atributos.get("playa"));
        assertTrue(atributos.get("lujo"));
        assertTrue(atributos.get("hispanohablante"));
        assertEquals(false, atributos.get("museos"));
        assertEquals(false, atributos.get("urbano"));
    }

    @Test
    @DisplayName("resumenPorCategoria: lista las etiquetas de lo elegido en su categoría")
    void resumenPorCategoria_listaLoElegido() {
        Preferencias preferencias = new Preferencias();
        preferencias.getRespuestas().put("playa", "si");
        preferencias.getRespuestas().put("nieve", "prefiero");
        preferencias.getRespuestas().put("lujo", "gustar");

        Map<String, List<String>> resumen = servicio.resumenPorCategoria(preferencias);

        assertEquals(9, resumen.size());
        assertEquals(List.of("Playa", "Nieve"), resumen.get("Paisaje y clima"));
        assertEquals(List.of("Lujo"), resumen.get("Estilo de viaje"));
        assertTrue(resumen.get("Cultura").isEmpty());
    }

    @Test
    @DisplayName("contarAfirmativas cuenta solo gustar, prefiero y si")
    void contarAfirmativas_cuentaSoloAfirmativas() {
        Preferencias preferencias = new Preferencias();
        preferencias.getRespuestas().put("playa", "gustar");
        preferencias.getRespuestas().put("lujo", "prefiero");
        preferencias.getRespuestas().put("golf", "si");
        preferencias.getRespuestas().put("museos", "no");
        preferencias.getRespuestas().put("urbano", "");

        assertEquals(3L, servicio.contarAfirmativas(preferencias));
    }
}

package com.wisetrip.servicio;

import com.wisetrip.controlador.PlanificacionControlador;
import com.wisetrip.modelo.Importancia;
import com.wisetrip.modelo.Preferencias;
import com.wisetrip.modelo.Usuario;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.ui.ExtendedModelMap;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class PreferenciasServicioTest {
    private final PreferenciasServicio servicio = new PreferenciasServicio();

    @Test
    void guardaLosCuatroNivelesSinConvertirInteresesEnIndispensables() {
        Preferencias preferencias = completas();
        preferencias.getRespuestas().put("playa", "gustar");
        preferencias.getRespuestas().put("montana", "prefiero");
        preferencias.getRespuestas().put("museos", "si");
        assertTrue(servicio.validarPreferencias(preferencias).isEmpty());
        var niveles = servicio.obtenerImportancias(preferencias);
        assertEquals(Importancia.gustar, niveles.get("playa"));
        assertEquals(Importancia.prefiero, niveles.get("montana"));
        assertEquals(Importancia.si, niveles.get("museos"));
        assertEquals(Importancia.no, niveles.get("nieve"));
        assertEquals(3, servicio.contarAfirmativas(preferencias));
        assertEquals(2, servicio.resumenPorCategoria(preferencias).get("Paisaje y clima").size());
        assertTrue(servicio.obtenerAtributosSeleccionados(preferencias).get("playa"));
        assertFalse(servicio.obtenerAtributosSeleccionados(preferencias).get("nieve"));

        var sesion = new MockHttpSession();
        sesion.setAttribute("usuarioActivo", mock(Usuario.class));
        var controlador = new PlanificacionControlador(servicio,
                mock(FechasServicio.class), mock(PresupuestoServicio.class));
        assertEquals("redirect:/fechas", controlador.guardarPreferencias(
                preferencias, sesion, new ExtendedModelMap()));
        assertEquals(niveles, sesion.getAttribute("importanciasSeleccionadas"));
        assertSame(preferencias, sesion.getAttribute("preferenciasViaje"));
    }

    @Test
    void sigueExigiendoTodasLasRespuestasYRechazaNivelesDesconocidos() {
        Preferencias preferencias = completas();
        preferencias.getRespuestas().remove("playa");
        preferencias.getRespuestas().put("museos", "inventado");
        var errores = servicio.validarPreferencias(preferencias);
        assertEquals(2, errores.size());
        assertTrue(errores.containsKey("playa"));
        assertTrue(errores.containsKey("museos"));
    }

    private Preferencias completas() {
        Preferencias preferencias = new Preferencias();
        for (var categoria : servicio.listarCategorias()) {
            for (var pregunta : categoria.getPreguntas()) {
                preferencias.getRespuestas().put(pregunta.getClave(), "no");
            }
        }
        return preferencias;
    }
}

package com.wisetrip.controlador;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.ui.ConcurrentModel;

import com.wisetrip.modelo.FechasViaje;
import com.wisetrip.modelo.Preferencias;
import com.wisetrip.modelo.Presupuesto;
import com.wisetrip.modelo.Ubicacion;
import com.wisetrip.modelo.Usuario;
import com.wisetrip.negocio.GestorPreferencias;
import com.wisetrip.negocio.GestorPresupuesto;
import com.wisetrip.negocio.IFechasServicio;
import com.wisetrip.servicio.PresupuestoConMargenDecorador;
import com.wisetrip.servicio.UsuarioSesionFacade;

/**
 * Pruebas unitarias de PlanificacionControlador: los pasos de preferencias
 * (HU#25), fechas (HU#26), presupuesto y resumen del viaje.
 *
 * Los servicios se simulan con Mockito; la sesión y el modelo son objetos de
 * prueba de Spring.
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@DisplayName("PlanificacionControlador")
class PlanificacionControladorTest {

    @Mock
    private GestorPreferencias gestorPreferencias;
    @Mock
    private GestorPresupuesto gestorPresupuesto;
    @Mock
    private PresupuestoConMargenDecorador decoradorMargen;
    @Mock
    private IFechasServicio fechasServicio;
    @Mock
    private UsuarioSesionFacade usuarioSesionFacade;

    private PlanificacionControlador controlador;
    private MockHttpSession sesion;
    private ConcurrentModel model;
    private Usuario usuario;

    @BeforeEach
    void setUp() {
        controlador = new PlanificacionControlador(gestorPreferencias, gestorPresupuesto, decoradorMargen,
                fechasServicio, usuarioSesionFacade);
        sesion = new MockHttpSession();
        model = new ConcurrentModel();
        usuario = new Usuario();
        usuario.setIdUsuario(3);
        when(usuarioSesionFacade.prepararEncabezado(sesion, model)).thenReturn(usuario);
        when(gestorPreferencias.listarCategorias()).thenReturn(List.of());
        when(gestorPreferencias.totalPreguntas()).thenReturn(29);
    }

    private void conOrigen() {
        SesionViaje.de(sesion, usuario).setOrigen(new Ubicacion("Colombia", "Bogotá", null));
    }

    @Test
    @DisplayName("sin sesión iniciada, cada paso redirige al login")
    void sinUsuario_todoRedirigeAlLogin() {
        when(usuarioSesionFacade.prepararEncabezado(sesion, model)).thenReturn(null);

        assertEquals(Nombres.REDIRECT_LOGIN, controlador.mostrarPreferencias(sesion, model));
        assertEquals(Nombres.REDIRECT_LOGIN, controlador.guardarPreferencias(new Preferencias(), sesion, model));
        assertEquals(Nombres.REDIRECT_LOGIN, controlador.mostrarFechas(sesion, model));
        assertEquals(Nombres.REDIRECT_LOGIN, controlador.guardarFechas(new FechasViaje(), sesion, model));
        assertEquals(Nombres.REDIRECT_LOGIN, controlador.mostrarPresupuesto(sesion, model));
        assertEquals(Nombres.REDIRECT_LOGIN, controlador.guardarPresupuesto(new Presupuesto(), sesion, model));
        assertEquals(Nombres.REDIRECT_LOGIN, controlador.mostrarResumen(sesion, model));
    }

    // ------------------------------------------------------------ preferencias

    @Nested
    @DisplayName("Preferencias (HU#25)")
    class PasoPreferencias {

        @Test
        @DisplayName("sin origen registrado manda primero a /origen")
        void mostrar_sinOrigen_redirigeAOrigen() {
            assertEquals("redirect:/origen", controlador.mostrarPreferencias(sesion, model));
        }

        @Test
        @DisplayName("con origen muestra el cuestionario con las respuestas guardadas")
        void mostrar_conOrigen_cargaCuestionario() {
            conOrigen();
            Preferencias guardadas = new Preferencias();
            sesion.setAttribute(Nombres.PREFERENCIAS_VIAJE, guardadas);

            assertEquals(Nombres.PREFERENCIAS, controlador.mostrarPreferencias(sesion, model));
            assertSame(guardadas, model.getAttribute(Nombres.PREFERENCIAS));
            assertEquals(29, model.getAttribute("totalPreguntas"));
        }

        @Test
        @DisplayName("respuestas incompletas vuelven al cuestionario indicando cuántas faltan")
        void guardar_conErrores_vuelveAlCuestionario() {
            when(gestorPreferencias.validarPreferencias(any())).thenReturn(Map.of("playa", "Falta responder."));

            String vista = controlador.guardarPreferencias(new Preferencias(), sesion, model);

            assertEquals(Nombres.PREFERENCIAS, vista);
            assertEquals(1, model.getAttribute("faltantes"));
            assertNull(sesion.getAttribute(Nombres.PREFERENCIAS_VIAJE));
        }

        @Test
        @DisplayName("respuestas completas se guardan en sesión y siguen a /fechas")
        void guardar_valido_sigueAFechas() {
            when(gestorPreferencias.validarPreferencias(any())).thenReturn(Map.of());
            sesion.setAttribute(Nombres.DESTINO_ELEGIDO, "destino viejo");
            Preferencias preferencias = new Preferencias();

            String vista = controlador.guardarPreferencias(preferencias, sesion, model);

            assertEquals("redirect:/fechas", vista);
            assertSame(preferencias, sesion.getAttribute(Nombres.PREFERENCIAS_VIAJE));
            assertNull(sesion.getAttribute(Nombres.DESTINO_ELEGIDO)); // se invalidó la selección anterior
        }
    }

    // ------------------------------------------------------------ fechas

    @Nested
    @DisplayName("Fechas (HU#26)")
    class PasoFechas {

        @Test
        @DisplayName("sin preferencias manda primero a /preferencias")
        void mostrar_sinPreferencias_redirige() {
            assertEquals("redirect:/preferencias", controlador.mostrarFechas(sesion, model));
        }

        @Test
        @DisplayName("con preferencias muestra el formulario de fechas")
        void mostrar_conPreferencias_muestraFormulario() {
            sesion.setAttribute(Nombres.PREFERENCIAS_VIAJE, new Preferencias());

            assertEquals(Nombres.FECHAS, controlador.mostrarFechas(sesion, model));
            assertTrue(model.getAttribute(Nombres.FECHAS) instanceof FechasViaje);
        }

        @Test
        @DisplayName("fechas válidas se guardan y siguen a /presupuesto")
        void guardar_valido_sigueAPresupuesto() {
            when(fechasServicio.validarFechas(any(FechasViaje.class), anyInt(), anyInt())).thenReturn(Map.of());
            FechasViaje fechas = new FechasViaje();

            assertEquals(Nombres.REDIRECT_PRESUPUESTO, controlador.guardarFechas(fechas, sesion, model));
            assertSame(fechas, sesion.getAttribute(Nombres.FECHAS_VIAJE));
        }

        @Test
        @DisplayName("fechas inválidas vuelven al formulario con los errores")
        void guardar_conErrores_vuelveAlFormulario() {
            when(fechasServicio.validarFechas(any(FechasViaje.class), anyInt(), anyInt()))
                    .thenReturn(Map.of("fechaFin", "La fecha de regreso debe ser posterior."));

            assertEquals(Nombres.FECHAS, controlador.guardarFechas(new FechasViaje(), sesion, model));
            assertTrue(model.containsAttribute(Nombres.ERRORES));
        }

        @Test
        @DisplayName("si no se pueden revisar los cruces de viajes muestra un error amable")
        void guardar_falloAlRevisarCruces_errorAmable() {
            sesion.setAttribute("idViajeGuardado", 8);
            when(fechasServicio.validarFechas(any(FechasViaje.class), anyInt(), anyInt()))
                    .thenThrow(new IllegalStateException("BD caída"));

            assertEquals(Nombres.FECHAS, controlador.guardarFechas(new FechasViaje(), sesion, model));
            @SuppressWarnings("unchecked")
            Map<String, String> errores = (Map<String, String>) model.getAttribute(Nombres.ERRORES);
            assertTrue(errores.get("fechaFin").contains("No pudimos comprobar"));
        }
    }

    // ------------------------------------------------------------ presupuesto

    @Nested
    @DisplayName("Presupuesto")
    class PasoPresupuesto {

        @Test
        @DisplayName("sin fechas manda primero a /fechas")
        void mostrar_sinFechas_redirige() {
            assertEquals("redirect:/fechas", controlador.mostrarPresupuesto(sesion, model));
        }

        @Test
        @DisplayName("con fechas muestra el formulario con las monedas del destino")
        void mostrar_conFechas_cargaMonedas() {
            sesion.setAttribute(Nombres.FECHAS_VIAJE, new FechasViaje());
            sesion.setAttribute(Nombres.PAIS_DESTINO, "Colombia");
            Map<String, String> monedas = Map.of("COP", "Peso colombiano");
            when(gestorPresupuesto.monedasDisponibles("Colombia")).thenReturn(monedas);

            assertEquals(Nombres.PRESUPUESTO, controlador.mostrarPresupuesto(sesion, model));
            assertSame(monedas, model.getAttribute("monedas"));
        }

        @Test
        @DisplayName("presupuesto inválido vuelve al formulario")
        void guardar_conErrores_vuelveAlFormulario() {
            when(gestorPresupuesto.validarPresupuesto(any(), any())).thenReturn(Map.of("monto", "Ingresa un monto."));

            assertEquals(Nombres.PRESUPUESTO, controlador.guardarPresupuesto(new Presupuesto(), sesion, model));
            assertNull(sesion.getAttribute(Nombres.PRESUPUESTO_VIAJE));
        }

        @Test
        @DisplayName("presupuesto válido se guarda con su valor en USD y sigue al resumen")
        void guardar_valido_guardaEnUsd() {
            Presupuesto presupuesto = new Presupuesto("4000000", "COP");
            when(gestorPresupuesto.validarPresupuesto(any(), any())).thenReturn(Map.of());
            when(gestorPresupuesto.convertirAUsd(presupuesto)).thenReturn(1000.0);

            assertEquals("redirect:/resumen", controlador.guardarPresupuesto(presupuesto, sesion, model));
            assertEquals(1000.0, sesion.getAttribute("presupuestoEnUsd"));
        }
    }

    // ------------------------------------------------------------ resumen

    @Nested
    @DisplayName("Resumen")
    class PasoResumen {

        @Test
        @DisplayName("sin origen manda a /origen")
        void sinOrigen_redirige() {
            assertEquals("redirect:/origen", controlador.mostrarResumen(sesion, model));
        }

        @Test
        @DisplayName("con todo el viaje armado muestra preferencias, presupuesto y margen")
        void conTodo_muestraElResumen() {
            conOrigen();
            Presupuesto presupuesto = new Presupuesto("4000000", "COP");
            sesion.setAttribute(Nombres.PREFERENCIAS_VIAJE, new Preferencias());
            sesion.setAttribute(Nombres.PRESUPUESTO_VIAJE, presupuesto);
            sesion.setAttribute("presupuestoEnUsd", 1000.0);
            when(gestorPreferencias.contarAfirmativas(any())).thenReturn(4L);
            when(gestorPresupuesto.formatear(anyDouble())).thenReturn("1.000");
            when(gestorPresupuesto.nombreMoneda(anyString())).thenReturn("Peso colombiano");
            when(decoradorMargen.calcularMargenImprevistos(presupuesto)).thenReturn(50.0);

            assertEquals("resumen", controlador.mostrarResumen(sesion, model));
            assertEquals(4L, model.getAttribute("afirmativas"));
            assertEquals("Peso colombiano", model.getAttribute("nombreMoneda"));
            assertEquals(50.0, model.getAttribute("margenImprevistos"));
            assertEquals("1.000", model.getAttribute("usdFormateado"));
        }

        @Test
        @DisplayName("borde: solo con el origen muestra el resumen sin las demás secciones")
        void soloOrigen_resumenParcial() {
            conOrigen();

            assertEquals("resumen", controlador.mostrarResumen(sesion, model));
            assertFalse(model.containsAttribute("resumenPreferencias"));
            assertFalse(model.containsAttribute("margenImprevistos"));
            assertFalse(model.containsAttribute("usdFormateado"));
        }
    }
}

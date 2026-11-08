package com.wisetrip.controlador;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.ui.ConcurrentModel;

import com.wisetrip.modelo.Ciudad;
import com.wisetrip.modelo.EstadoCategoriaGasto;
import com.wisetrip.modelo.FechasViaje;
import com.wisetrip.modelo.Presupuesto;
import com.wisetrip.modelo.RepartoPresupuesto;
import com.wisetrip.modelo.ResultadoRecomendacion;
import com.wisetrip.modelo.ResumenPresupuesto;
import com.wisetrip.modelo.Usuario;
import com.wisetrip.negocio.IGestionReparto;
import com.wisetrip.negocio.IGestorGastos;
import com.wisetrip.negocio.IRepositorioViajes;
import com.wisetrip.servicio.PresupuestoServicio;
import com.wisetrip.servicio.UsuarioSesionFacade;

/**
 * Pruebas unitarias de PlanControlador, enfocadas en la pantalla Mi viaje
 * (GET /viaje) y su sección de gastos (HU#89).
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@DisplayName("PlanControlador")
class PlanControladorTest {

    private static final int ID_VIAJE = 12;

    @Mock
    private IGestionReparto gestionReparto;
    @Mock
    private PresupuestoServicio presupuestoServicio;
    @Mock
    private UsuarioSesionFacade usuarioSesionFacade;
    @Mock
    private IRepositorioViajes viajeDAO;
    @Mock
    private IGestorGastos gestorGastos;

    private PlanControlador controlador;
    private MockHttpSession sesion;
    private ConcurrentModel model;

    @BeforeEach
    void setUp() {
        controlador = new PlanControlador(gestionReparto, presupuestoServicio, usuarioSesionFacade,
                viajeDAO, gestorGastos);
        sesion = new MockHttpSession();
        model = new ConcurrentModel();

        Usuario usuario = new Usuario();
        usuario.setIdUsuario(3);
        when(usuarioSesionFacade.prepararEncabezado(sesion, model)).thenReturn(usuario);
        when(presupuestoServicio.convertirAUsd(any(Presupuesto.class))).thenReturn(1000.0);
        when(presupuestoServicio.formatear(anyDouble())).thenReturn("1.000");

        Ciudad cartagena = new Ciudad();
        cartagena.setNombre("Cartagena");
        FechasViaje fechas = new FechasViaje();
        fechas.setFechaInicio("2026-12-01");
        fechas.setFechaFin("2026-12-03");
        sesion.setAttribute(Nombres.DESTINO_ELEGIDO, new ResultadoRecomendacion(cartagena, 1, 1, 1, true, false));
        sesion.setAttribute(Nombres.FECHAS_VIAJE, fechas);
        sesion.setAttribute(Nombres.PRESUPUESTO_VIAJE, new Presupuesto("1000", "USD"));
        sesion.setAttribute(Nombres.REPARTO_PRESUPUESTO, new RepartoPresupuesto());
    }

    @Test
    @DisplayName("con viaje guardado carga los gastos, el estado por categoría y el resumen")
    void viajeGuardado_cargaLosGastos() {
        sesion.setAttribute("idViajeGuardado", ID_VIAJE);
        List<EstadoCategoriaGasto> estados = List.of(
                new EstadoCategoriaGasto("hospedaje", new BigDecimal("350"), new BigDecimal("100")));
        ResumenPresupuesto resumen = new ResumenPresupuesto(
                new BigDecimal("1000"), new BigDecimal("100"), new BigDecimal("900"), "USD");
        when(gestorGastos.consultarGastos(ID_VIAJE)).thenReturn(List.of());
        when(gestorGastos.consultarEstadoCategorias(ID_VIAJE)).thenReturn(estados);
        when(gestorGastos.consultarResumen(ID_VIAJE)).thenReturn(resumen);

        String vista = controlador.mostrarViaje(sesion, model);

        assertEquals("viaje", vista);
        assertSame(estados, model.getAttribute("estadoCategorias"));
        assertSame(resumen, model.getAttribute("resumenGastos"));
        assertEquals("Alimentación", ((Map<?, ?>) model.getAttribute("nombresCategoria")).get("alimentacion"));
        assertEquals(3L, model.getAttribute("dias"));
    }

    @Test
    @DisplayName("si el viaje aún no está guardado muestra la página sin la sección de gastos")
    void viajeSinGuardar_sinGastos() {
        String vista = controlador.mostrarViaje(sesion, model);

        assertEquals("viaje", vista);
        assertFalse(model.containsAttribute("gastosReales"));
        verifyNoInteractions(gestorGastos);
    }

    @Test
    @DisplayName("si la base de datos falla la página se muestra igual y avisa que no hay gastos")
    void falloAlCargarGastos_paginaIgualSeMuestra() {
        sesion.setAttribute("idViajeGuardado", ID_VIAJE);
        when(gestorGastos.consultarGastos(ID_VIAJE)).thenThrow(new IllegalStateException("BD caída"));

        String vista = controlador.mostrarViaje(sesion, model);

        assertEquals("viaje", vista);
        assertTrue((Boolean) model.getAttribute("gastosNoDisponibles"));
    }

    @Test
    @DisplayName("sin reparto guardado manda primero a /plan")
    void sinReparto_redirigeAlPlan() {
        sesion.removeAttribute(Nombres.REPARTO_PRESUPUESTO);

        assertEquals("redirect:/plan", controlador.mostrarViaje(sesion, model));
    }

    @Test
    @DisplayName("sin fechas manda a /fechas")
    void sinFechas_redirigeAFechas() {
        sesion.removeAttribute(Nombres.FECHAS_VIAJE);

        assertEquals("redirect:/fechas", controlador.mostrarViaje(sesion, model));
    }

    @Test
    @DisplayName("sin sesión iniciada redirige al login")
    void sinUsuario_redirigeAlLogin() {
        when(usuarioSesionFacade.prepararEncabezado(sesion, model)).thenReturn(null);

        assertEquals(Nombres.REDIRECT_LOGIN, controlador.mostrarViaje(sesion, model));
    }

    @Test
    @DisplayName("el enlace viejo de itinerario lleva a la sección de Mi viaje")
    void itinerario_redirigeAMiViaje() {
        assertEquals("redirect:/viaje#itinerario", controlador.mostrarItinerario());
    }
}

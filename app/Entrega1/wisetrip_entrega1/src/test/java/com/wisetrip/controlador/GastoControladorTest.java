package com.wisetrip.controlador;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.web.servlet.mvc.support.RedirectAttributesModelMap;

import com.wisetrip.modelo.FechasViaje;
import com.wisetrip.modelo.Gasto;
import com.wisetrip.modelo.Usuario;
import com.wisetrip.negocio.IGestorGastos;
import com.wisetrip.servicio.UsuarioSesionFacade;

/**
 * Pruebas unitarias de GastoControlador (HU#89): registrar un gasto real
 * desde la pantalla Mi viaje.
 *
 * El servicio de gastos y la fachada de sesión se simulan con Mockito; la
 * sesión y los atributos flash son objetos de prueba de Spring.
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@DisplayName("GastoControlador")
class GastoControladorTest {

    private static final String VOLVER = "redirect:/viaje#presupuesto";
    private static final int ID_VIAJE = 12;

    @Mock
    private IGestorGastos gestorGastos;
    @Mock
    private UsuarioSesionFacade usuarioSesionFacade;

    private GastoControlador controlador;
    private MockHttpSession sesion;
    private RedirectAttributesModelMap flash;

    @BeforeEach
    void setUp() {
        controlador = new GastoControlador(gestorGastos, usuarioSesionFacade);
        sesion = new MockHttpSession();
        flash = new RedirectAttributesModelMap();
        when(usuarioSesionFacade.usuarioActivo(sesion)).thenReturn(new Usuario());
        sesion.setAttribute("idViajeGuardado", ID_VIAJE);
        FechasViaje fechas = new FechasViaje();
        fechas.setFechaInicio("2026-12-01");
        fechas.setFechaFin("2026-12-10");
        sesion.setAttribute("fechasViaje", fechas);
        when(gestorGastos.validarGasto(anyInt(), any(Gasto.class))).thenReturn(Map.of());
        when(gestorGastos.advertenciasGasto(anyInt(), any(Gasto.class))).thenReturn(Map.of());
    }

    private String registrar(String categoria, String descripcion, String monto, String fecha) {
        return controlador.registrarGasto(categoria, descripcion, monto, fecha, sesion, flash);
    }

    @SuppressWarnings("unchecked")
    private Map<String, String> errores() {
        return (Map<String, String>) flash.getFlashAttributes().get("erroresGasto");
    }

    // ------------------------------------------------------------ acceso

    @Test
    @DisplayName("sin sesión iniciada redirige al login")
    void sinUsuario_redirigeAlLogin() {
        when(usuarioSesionFacade.usuarioActivo(sesion)).thenReturn(null);

        assertEquals("redirect:/login", registrar("hospedaje", "Hotel", "100", "2026-12-02"));
        verifyNoInteractions(gestorGastos);
    }

    @Test
    @DisplayName("si el viaje aún no está guardado vuelve a Mi viaje")
    void sinViajeGuardado_vuelveAMiViaje() {
        sesion.removeAttribute("idViajeGuardado");

        assertEquals("redirect:/viaje", registrar("hospedaje", "Hotel", "100", "2026-12-02"));
        verifyNoInteractions(gestorGastos);
    }

    // ------------------------------------------------------------ registro correcto

    @Test
    @DisplayName("gasto válido se registra en USD y avisa que quedó guardado")
    void gastoValido_seRegistra() {
        String vista = registrar("alimentacion", "  Almuerzo  ", "25,5", "2026-12-03");

        ArgumentCaptor<Gasto> captor = ArgumentCaptor.forClass(Gasto.class);
        verify(gestorGastos).registrarGasto(eq(ID_VIAJE), captor.capture());
        Gasto gasto = captor.getValue();
        assertEquals(VOLVER, vista);
        assertEquals("Almuerzo", gasto.descripcion());
        assertEquals(new BigDecimal("25.50"), gasto.monto());
        assertEquals("USD", gasto.moneda());
        assertEquals(true, flash.getFlashAttributes().get("gastoRegistrado"));
    }

    @Test
    @DisplayName("si la categoría flexible se excede, se registra y muestra el aviso")
    void gastoConAdvertencia_registraYAvisa() {
        when(gestorGastos.advertenciasGasto(anyInt(), any(Gasto.class)))
                .thenReturn(Map.of("cupoCategoria", "Ya superaste el cupo de imprevistos."));

        registrar("imprevistos", "Taxi", "80", "2026-12-04");

        verify(gestorGastos).registrarGasto(eq(ID_VIAJE), any(Gasto.class));
        assertEquals("Ya superaste el cupo de imprevistos.", flash.getFlashAttributes().get("avisoGasto"));
    }

    @Test
    @DisplayName("borde: sin fechas del viaje en sesión no limita la fecha del gasto")
    void sinFechasDelViaje_noLimitaLaFecha() {
        sesion.removeAttribute("fechasViaje");

        registrar("transporte", "Bus", "10", "2030-01-01");

        verify(gestorGastos).registrarGasto(eq(ID_VIAJE), any(Gasto.class));
    }

    // ------------------------------------------------------------ errores

    @Test
    @DisplayName("si el servicio bloquea el gasto (control estricto) no se registra")
    void servicioBloquea_noRegistra() {
        when(gestorGastos.validarGasto(anyInt(), any(Gasto.class)))
                .thenReturn(Map.of("cupoCategoria", "El gasto supera el cupo asignado a 'hospedaje'."));

        String vista = registrar("hospedaje", "Hotel", "900", "2026-12-02");

        assertEquals(VOLVER, vista);
        assertTrue(errores().containsKey("cupoCategoria"));
        assertTrue(flash.getFlashAttributes().containsKey("gastoForm"));
        verify(gestorGastos, never()).registrarGasto(anyInt(), any(Gasto.class));
    }

    @Test
    @DisplayName("monto que no es número pide un monto válido")
    void montoInvalido_error() {
        registrar("hospedaje", "Hotel", "abc", "2026-12-02");

        assertEquals("Ingresa un monto válido, por ejemplo 25.50.", errores().get("monto"));
        verifyNoInteractions(gestorGastos);
    }

    @Test
    @DisplayName("borde: monto demasiado grande para la base de datos se rechaza")
    void montoGigante_error() {
        registrar("hospedaje", "Hotel", "10000000000", "2026-12-02");

        assertEquals("El monto es demasiado grande.", errores().get("monto"));
    }

    @Test
    @DisplayName("fecha vacía pide la fecha del gasto")
    void fechaVacia_error() {
        registrar("hospedaje", "Hotel", "100", "");

        assertEquals("Ingresa la fecha del gasto.", errores().get("fecha"));
    }

    @Test
    @DisplayName("borde: fecha fuera de las fechas del viaje se rechaza")
    void fechaFueraDelViaje_error() {
        registrar("hospedaje", "Hotel", "100", "2026-12-11");

        assertTrue(errores().get("fecha").contains("2026-12-01"));
        verifyNoInteractions(gestorGastos);
    }

    @Test
    @DisplayName("si la base de datos falla muestra un error general sin romper la página")
    void falloAlGuardar_errorGeneral() {
        when(gestorGastos.registrarGasto(anyInt(), any(Gasto.class)))
                .thenThrow(new IllegalStateException("BD caída"));

        String vista = registrar("hospedaje", "Hotel", "100", "2026-12-02");

        assertEquals(VOLVER, vista);
        assertEquals("No pudimos registrar el gasto. Intenta nuevamente.", errores().get("general"));
    }
}

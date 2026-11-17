package com.wisetrip.controlador;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
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

    //la vista a la que se vuelve después de registrar un gasto
    private static final String VOLVER = "redirect:/viaje#presupuesto";
    //id del viaje guardado se usa en la sesion de prueba
    private static final int ID_VIAJE = 12;

    @Mock
    private IGestorGastos gestorGastos;
    @Mock
    private UsuarioSesionFacade usuarioSesionFacade;

    private GastoControlador controlador;
    private MockHttpSession sesion;
    private RedirectAttributesModelMap flash; //datos que solo van a verse en una redirección

   // antes de cada test se crea un controlador nuevo y una sesión nueva
    @BeforeEach
    void setUp() {
        controlador = new GastoControlador(gestorGastos, usuarioSesionFacade);
        sesion = new MockHttpSession();
        flash = new RedirectAttributesModelMap();
        //hay un usuario con sesion iniciada
        when(usuarioSesionFacade.usuarioActivo(sesion)).thenReturn(new Usuario());
        // el viajes esta guadado y tiene fecha inico y fin 
        sesion.setAttribute("idViajeGuardado", ID_VIAJE);
        FechasViaje fechas = new FechasViaje();
        fechas.setFechaInicio("2026-12-01");
        fechas.setFechaFin("2026-12-10");
        sesion.setAttribute("fechasViaje", fechas);
        // el servicio de gastos no bloquea ningún gasto y no da advertencias
        when(gestorGastos.validarGasto(anyInt(), any(Gasto.class))).thenReturn(Map.of());
        when(gestorGastos.advertenciasGasto(anyInt(), any(Gasto.class))).thenReturn(Map.of());
    }

    private String registrar(String categoria, String descripcion, String monto, String fecha) {
        return controlador.registrarGasto(categoria, descripcion, monto, fecha, sesion, flash);
    }

    //lee el mapa de errores que el controlador puso en los atributos flash
    @SuppressWarnings("unchecked")
    private Map<String, String> errores() {
        return (Map<String, String>) flash.getFlashAttributes().get("erroresGasto");
    }

    // ------------------------------------------------------------ acceso

    @Test
    @DisplayName("sin sesión iniciada redirige al login")
    void sinUsuario_redirigeAlLogin() {
        //nadie ha iniciado sesión, el servicio devuelve null
        when(usuarioSesionFacade.usuarioActivo(sesion)).thenReturn(null);

        assertEquals("redirect:/login", registrar("hospedaje", "Hotel", "100", "2026-12-02"));
        //sin usuario registrado no se llama al servicio de gastos
        verifyNoInteractions(gestorGastos);
    }

    @Test
    @DisplayName("si el viaje aún no está guardado vuelve a Mi viaje")
    void sinViajeGuardado_vuelveAMiViaje() {
        // sin id de viaje no se asolta el servicio de gastos
        sesion.removeAttribute("idViajeGuardado");

        assertEquals("redirect:/viaje", registrar("hospedaje", "Hotel", "100", "2026-12-02"));
        verifyNoInteractions(gestorGastos);
    }

    // ------------------------------------------------------------ registro correcto

    @Test
    @DisplayName("gasto válido se registra en USD y avisa que quedó guardado")
    void gastoValido_seRegistra() {
        String vista = registrar("alimentacion", "  Almuerzo  ", "25,5", "2026-12-03");

        // captor para leer el gasto que se pasó al servicio de gastos
        ArgumentCaptor<Gasto> captor = ArgumentCaptor.forClass(Gasto.class);
        verify(gestorGastos).registrarGasto(eq(ID_VIAJE), captor.capture());
        Gasto gasto = captor.getValue();
        assertEquals(VOLVER, vista);
        // limpia espacios, convierte coma a punto, guarda en USD y avisa que se registró
        assertEquals("Almuerzo", gasto.descripcion());
        assertEquals(new BigDecimal("25.50"), gasto.monto());
        assertEquals("USD", gasto.moneda());
        assertEquals(true, flash.getFlashAttributes().get("gastoRegistrado"));
    }

    @Test
    @DisplayName("si la categoría flexible se excede, se registra y muestra el aviso")
    void gastoConAdvertencia_registraYAvisa() {
        // el servicio devuelve un aviso de que se superó el cupo de la categoría flexible
        when(gestorGastos.advertenciasGasto(anyInt(), any(Gasto.class)))
                .thenReturn(Map.of("cupoCategoria", "Ya superaste el cupo de imprevistos."));

        registrar("imprevistos", "Taxi", "80", "2026-12-04");

        // el gasto se registra a pesar de la advertencia
        verify(gestorGastos).registrarGasto(eq(ID_VIAJE), any(Gasto.class));
        // el aviso se pasa a la vista de Mi viaje
        assertEquals("Ya superaste el cupo de imprevistos.", flash.getFlashAttributes().get("avisoGasto"));
    }

    @Test
    @DisplayName("borde: sin fechas del viaje en sesión no limita la fecha del gasto")
    void sinFechasDelViaje_noLimitaLaFecha() {
        sesion.removeAttribute("fechasViaje");

        // registra un gasto con una fecha lejana, fuera del rango de vaije
        registrar("transporte", "Bus", "10", "2030-01-01");

        // como no hay fechas del viaje, el gasto se registra sin errores
        verify(gestorGastos).registrarGasto(eq(ID_VIAJE), any(Gasto.class));
    }

    // ------------------------------------------------------------ errores

    @Test
    @DisplayName("si el servicio bloquea el gasto (control estricto) no se registra")
    void servicioBloquea_noRegistra() {
        // después de validar el gasto, el servicio devuelve un error de cupo de categoría
        when(gestorGastos.validarGasto(anyInt(), any(Gasto.class)))
                .thenReturn(Map.of("cupoCategoria", "El gasto supera el cupo asignado a 'hospedaje'."));

        String vista = registrar("hospedaje", "Hotel", "900", "2026-12-02");

        // el gasto no se registra y se vuelve a la vista de Mi viaje con los errores
        assertEquals(VOLVER, vista);
        //constainsKey es un metodo de map que devuelve true si la llave existe en el mapa
        assertTrue(errores().containsKey("cupoCategoria"));
        // el formulario del gasto se pasa al flash
        assertTrue(flash.getFlashAttributes().containsKey("gastoForm"));
        //no guarda nada
        verify(gestorGastos, never()).registrarGasto(anyInt(), any(Gasto.class));
    }

    @Test
    @DisplayName("monto que no es número pide un monto válido")
    void montoInvalido_error() {
        registrar("hospedaje", "Hotel", "abc", "2026-12-02");

        // el error de monto se pasa a la vista de Mi viaje
        assertEquals("Ingresa un monto válido, por ejemplo 25.50.", errores().get("monto"));
        //no guarda nada
        verifyNoInteractions(gestorGastos);
    }

    @Test
    @DisplayName("borde: monto demasiado grande para la base de datos se rechaza")
    void montoGigante_error() {
        // registra un gasto con un monto demasiado grande para la base de datos
        registrar("hospedaje", "Hotel", "10000000000", "2026-12-02");

        assertEquals("El monto es demasiado grande.", errores().get("monto"));
    }

    @Test
    @DisplayName("fecha vacía pide la fecha del gasto")
    void fechaVacia_error() {
        // registra un gasto sin fecha
        registrar("hospedaje", "Hotel", "100", "");

        assertEquals("Ingresa la fecha del gasto.", errores().get("fecha"));
    }

    @Test
    @DisplayName("borde: un día antes del inicio o después del fin se rechaza; el primer y último día se aceptan")
    void fechasEnLosLimitesDelViaje() {
        // el mensaje de error que se espera cuando la fecha del gasto está fuera del rango del viaje
        String mensaje = "La fecha debe estar entre el 2026-12-01 y el 2026-12-10.";

        // registra un gasto con fecha fuera del rango del viaje
        registrar("hospedaje", "Hotel", "100", "2026-11-30");   // un día antes del inicio
        assertEquals(mensaje, errores().get("fecha"));
        registrar("hospedaje", "Hotel", "100", "2026-12-11");   // un día después del fin
        assertEquals(mensaje, errores().get("fecha"));
        verifyNoInteractions(gestorGastos);

        registrar("hospedaje", "Hotel", "100", "2026-12-01");   // primer día: válido
        registrar("hospedaje", "Hotel", "100", "2026-12-10");   // último día: válido
        // se registran los gastos válidos
        verify(gestorGastos, times(2)).registrarGasto(eq(ID_VIAJE), any(Gasto.class));
    }


    @Test
    @DisplayName("si la base de datos falla muestra un error general sin romper la página")
    void falloAlGuardar_errorGeneral() {
        // el servicio de gastos lanza una excepción al intentar guardar el gasto
        when(gestorGastos.registrarGasto(anyInt(), any(Gasto.class)))
                .thenThrow(new IllegalStateException("BD caída"));

        String vista = registrar("hospedaje", "Hotel", "100", "2026-12-02");

        assertEquals(VOLVER, vista);
        assertEquals("No pudimos registrar el gasto. Intenta nuevamente.", errores().get("general"));
    }
}
package com.wisetrip.controlador;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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
import org.springframework.web.servlet.mvc.support.RedirectAttributesModelMap;

import com.wisetrip.modelo.FechasViaje;
import com.wisetrip.modelo.RepartoPresupuesto;
import com.wisetrip.modelo.Ubicacion;
import com.wisetrip.modelo.Usuario;
import com.wisetrip.modelo.Viaje;
import com.wisetrip.negocio.IRepositorioViajes;
import com.wisetrip.negocio.IViajeServicio;
import com.wisetrip.servicio.FechasServicio;
import com.wisetrip.servicio.UsuarioSesionFacade;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@DisplayName("ViajeControlador")
class ViajeControladorTest {

    @Mock
    private IViajeServicio viajeServicio;
    @Mock
    private UsuarioSesionFacade usuarioSesionFacade;
    @Mock
    private IRepositorioViajes viajes;
    @Mock
    private FechasServicio fechasServicio;

    private ViajeControlador controlador;
    private MockHttpSession sesion;
    private ConcurrentModel model;
    private RedirectAttributesModelMap flash;
    private Usuario usuario;

    @BeforeEach
    void setUp() {
        controlador = new ViajeControlador(viajeServicio, usuarioSesionFacade, viajes, fechasServicio);
        sesion = new MockHttpSession();
        model = new ConcurrentModel();
        flash = new RedirectAttributesModelMap();
        usuario = new Usuario();
        usuario.setIdUsuario(3);
        when(usuarioSesionFacade.prepararEncabezado(sesion, model)).thenReturn(usuario);
        when(usuarioSesionFacade.usuarioActivo(sesion)).thenReturn(usuario);
        when(fechasServicio.hoy()).thenReturn(LocalDate.now().toString());
    }

    private Viaje viajeGuardado(int id, int diaInicio, int diaFin, boolean conReparto) {
        FechasViaje fechas = new FechasViaje();
        fechas.setFechaInicio(LocalDate.now().plusDays(diaInicio).toString());
        fechas.setFechaFin(LocalDate.now().plusDays(diaFin).toString());
        Viaje viaje = new Viaje(usuario);
        viaje.setIdViaje(id);
        viaje.setFechas(fechas);
        if (conReparto) {
            viaje.setReparto(new RepartoPresupuesto());
        }
        return viaje;
    }

    // ---------------------------------------------------------------- mostrarViajes

    @Test
    @DisplayName("mostrarViajes sin usuario redirige al login")
    void mostrarViajes_sinUsuario_redirigeAlLogin() {
        when(usuarioSesionFacade.prepararEncabezado(sesion, model)).thenReturn(null);

        assertEquals("redirect:/login", controlador.mostrarViajes(sesion, model));
    }

    @Test
    @DisplayName("mostrarViajes sin viajes vigentes limpia la sesión y va a pedir el origen")
    void mostrarViajes_sinViajes_vaAlOrigen() {
        sesion.setAttribute("presupuestoEnUsd", 500.0);
        when(viajes.listarVigentes(usuario, LocalDate.now())).thenReturn(List.of());

        String vista = controlador.mostrarViajes(sesion, model);

        assertEquals("redirect:/origen", vista);
        assertNull(sesion.getAttribute("presupuestoEnUsd"));
    }

    @Test
    @DisplayName("mostrarViajes con viajes vigentes los muestra")
    void mostrarViajes_conViajes_losMuestra() {
        List<Viaje> guardados = List.of(viajeGuardado(10, 1, 5, true));
        when(viajes.listarVigentes(usuario, LocalDate.now())).thenReturn(guardados);

        String vista = controlador.mostrarViajes(sesion, model);

        assertEquals("mis-viajes", vista);
        assertSame(guardados, model.getAttribute("viajes"));
    }

    @Test
    @DisplayName("mostrarViajes con error de consulta muestra la vista con un aviso")
    void mostrarViajes_errorDeConsulta_muestraAviso() {
        when(viajes.listarVigentes(any(Usuario.class), any(LocalDate.class)))
                .thenThrow(new IllegalStateException("base de datos caída"));

        String vista = controlador.mostrarViajes(sesion, model);

        assertEquals("mis-viajes", vista);
        assertEquals("No pudimos cargar tus viajes. Intenta nuevamente.", model.getAttribute("errorConsulta"));
    }

    // ---------------------------------------------------------------- continuarViaje

    @Test
    @DisplayName("continuarViaje sin usuario redirige al login")
    void continuarViaje_sinUsuario_redirigeAlLogin() {
        when(usuarioSesionFacade.usuarioActivo(sesion)).thenReturn(null);

        assertEquals("redirect:/login", controlador.continuarViaje(10, sesion, flash));
    }

    @Test
    @DisplayName("continuarViaje con un viaje que no existe avisa y vuelve a la lista")
    void continuarViaje_noEncontrado_avisa() {
        when(viajes.buscarPorId(10, usuario)).thenReturn(Optional.empty());

        String vista = controlador.continuarViaje(10, sesion, flash);

        assertEquals("redirect:/viajes", vista);
        assertEquals("No encontramos ese viaje en tu cuenta.", flash.getFlashAttributes().get("errorContinuar"));
    }

    @Test
    @DisplayName("continuarViaje con un viaje que ya terminó avisa y vuelve a la lista")
    void continuarViaje_yaTermino_avisa() {
        when(viajes.buscarPorId(10, usuario)).thenReturn(Optional.of(viajeGuardado(10, -6, -2, true)));

        String vista = controlador.continuarViaje(10, sesion, flash);

        assertEquals("redirect:/viajes", vista);
        assertEquals("Este viaje ya termino.", flash.getFlashAttributes().get("errorContinuar"));
    }

    @Test
    @DisplayName("continuarViaje con un viaje vigente y con reparto lo restaura en la sesión")
    void continuarViaje_conReparto_restauraYVaAlViaje() {
        Viaje viaje = viajeGuardado(10, 1, 5, true);
        sesion.setAttribute("usuarioActivo", usuario);
        when(viajes.buscarPorId(10, usuario)).thenReturn(Optional.of(viaje));

        String vista = controlador.continuarViaje(10, sesion, flash);

        assertEquals("redirect:/viaje", vista);
        assertEquals(10, sesion.getAttribute("idViajeGuardado"));
        assertSame(viaje, sesion.getAttribute("viaje"));
    }

    @Test
    @DisplayName("continuarViaje sin reparto guardado manda a confirmar el reparto")
    void continuarViaje_sinReparto_vaAlPlan() {
        Viaje viaje = viajeGuardado(10, 1, 5, false);
        sesion.setAttribute("usuarioActivo", usuario);
        when(viajes.buscarPorId(10, usuario)).thenReturn(Optional.of(viaje));

        String vista = controlador.continuarViaje(10, sesion, flash);

        assertEquals("redirect:/plan", vista);
        assertEquals("Este viaje no tiene un reparto guardado. Confirma uno para continuar.",
                flash.getFlashAttributes().get("avisoReparto"));
    }

    @Test
    @DisplayName("continuarViaje con un viaje que no se puede restaurar avisa del error")
    void continuarViaje_viajeNoRestaurable_avisa() {
        Viaje sinGuardar = viajeGuardado(0, 1, 5, true);   // id 0: restaurar lanza IllegalArgumentException
        sesion.setAttribute("usuarioActivo", usuario);
        when(viajes.buscarPorId(10, usuario)).thenReturn(Optional.of(sinGuardar));

        String vista = controlador.continuarViaje(10, sesion, flash);

        assertEquals("redirect:/viajes", vista);
        assertEquals("No pudimos abrir el viaje. Intenta nuevamente.", flash.getFlashAttributes().get("errorContinuar"));
    }

    @Test
    @DisplayName("continuarViaje con error del repositorio avisa del error")
    void continuarViaje_errorDelRepositorio_avisa() {
        when(viajes.buscarPorId(10, usuario)).thenThrow(new IllegalStateException("base de datos caída"));

        String vista = controlador.continuarViaje(10, sesion, flash);

        assertEquals("redirect:/viajes", vista);
        assertEquals("No pudimos abrir el viaje. Intenta nuevamente.", flash.getFlashAttributes().get("errorContinuar"));
    }

    // ---------------------------------------------------------------- iniciarNuevoViaje

    @Test
    @DisplayName("iniciarNuevoViaje sin usuario redirige al login")
    void iniciarNuevoViaje_sinUsuario_redirigeAlLogin() {
        assertEquals("redirect:/login", controlador.iniciarNuevoViaje(sesion));
    }

    @Test
    @DisplayName("iniciarNuevoViaje limpia el viaje de la sesión y va a pedir el origen")
    void iniciarNuevoViaje_limpiaLaSesion() {
        sesion.setAttribute("usuarioActivo", usuario);
        sesion.setAttribute("presupuestoEnUsd", 500.0);

        String vista = controlador.iniciarNuevoViaje(sesion);

        assertEquals("redirect:/origen", vista);
        assertNull(sesion.getAttribute("presupuestoEnUsd"));
        assertSame(usuario, sesion.getAttribute("usuarioActivo"));
    }

    // ---------------------------------------------------------------- origen

    @Test
    @DisplayName("mostrarOrigen sin usuario redirige al login")
    void mostrarOrigen_sinUsuario_redirigeAlLogin() {
        when(usuarioSesionFacade.prepararEncabezado(sesion, model)).thenReturn(null);

        assertEquals("redirect:/login", controlador.mostrarOrigen(sesion, model));
    }

    @Test
    @DisplayName("mostrarOrigen sin origen guardado muestra el formulario vacío y sin ciudades")
    void mostrarOrigen_sinOrigen_formularioVacio() {
        when(viajeServicio.listarPaises()).thenReturn(List.of("Colombia", "Perú"));

        String vista = controlador.mostrarOrigen(sesion, model);

        assertEquals("origen", vista);
        assertTrue(model.getAttribute("ubicacion") instanceof Ubicacion);
        assertEquals(List.of("Colombia", "Perú"), model.getAttribute("paises"));
        assertEquals(List.of(), model.getAttribute("ciudades"));
    }

    @Test
    @DisplayName("mostrarOrigen con origen guardado carga las ciudades de ese país")
    void mostrarOrigen_conOrigen_cargaLasCiudades() {
        Ubicacion guardada = new Ubicacion("Colombia", "Bogotá", null);
        SesionViaje.de(sesion, usuario).setOrigen(guardada);
        when(viajeServicio.listarCiudades("Colombia")).thenReturn(List.of("Bogotá", "Cali"));

        String vista = controlador.mostrarOrigen(sesion, model);

        assertEquals("origen", vista);
        assertSame(guardada, model.getAttribute("ubicacion"));
        assertEquals(List.of("Bogotá", "Cali"), model.getAttribute("ciudades"));
    }

    @Test
    @DisplayName("guardarOrigen sin usuario redirige al login")
    void guardarOrigen_sinUsuario_redirigeAlLogin() {
        when(usuarioSesionFacade.prepararEncabezado(sesion, model)).thenReturn(null);

        assertEquals("redirect:/login", controlador.guardarOrigen(new Ubicacion(), sesion, model));
    }

    @Test
    @DisplayName("guardarOrigen con errores vuelve al formulario con los errores")
    void guardarOrigen_conErrores_vuelveAlFormulario() {
        Ubicacion ubicacion = new Ubicacion("Colombia", "Lima", null);
        Map<String, String> errores = Map.of("ciudad", "Esa ciudad no pertenece al pais seleccionado.");
        when(viajeServicio.validarUbicacion(ubicacion)).thenReturn(errores);
        when(viajeServicio.listarPaises()).thenReturn(List.of("Colombia"));
        when(viajeServicio.listarCiudades("Colombia")).thenReturn(List.of("Bogotá"));

        String vista = controlador.guardarOrigen(ubicacion, sesion, model);

        assertEquals("origen", vista);
        assertSame(errores, model.getAttribute("errores"));
        assertEquals(List.of("Colombia"), model.getAttribute("paises"));
        assertEquals(List.of("Bogotá"), model.getAttribute("ciudades"));
    }

    @Test
    @DisplayName("guardarOrigen válido limpia el detalle, guarda el origen en el viaje y sigue a las preferencias")
    void guardarOrigen_valido_guardaElOrigen() {
        Ubicacion ubicacion = new Ubicacion("Colombia", "Bogotá", "  Centro  ");
        when(viajeServicio.validarUbicacion(ubicacion)).thenReturn(Map.of());

        String vista = controlador.guardarOrigen(ubicacion, sesion, model);

        assertEquals("redirect:/preferencias", vista);
        assertEquals("Centro", ubicacion.getDetalle());
        assertSame(ubicacion, SesionViaje.de(sesion, usuario).getOrigen());
    }

    @Test
    @DisplayName("guardarOrigen válido sin detalle también guarda el origen")
    void guardarOrigen_sinDetalle_guardaElOrigen() {
        Ubicacion ubicacion = new Ubicacion("Colombia", "Bogotá", null);
        when(viajeServicio.validarUbicacion(ubicacion)).thenReturn(Map.of());

        String vista = controlador.guardarOrigen(ubicacion, sesion, model);

        assertEquals("redirect:/preferencias", vista);
        assertSame(ubicacion, SesionViaje.de(sesion, usuario).getOrigen());
    }
}

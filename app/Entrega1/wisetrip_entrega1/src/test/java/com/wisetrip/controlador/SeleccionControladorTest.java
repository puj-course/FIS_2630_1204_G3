package com.wisetrip.controlador;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributesModelMap;

import com.wisetrip.modelo.Ciudad;
import com.wisetrip.modelo.FechasViaje;
import com.wisetrip.modelo.ResultadoRecomendacion;
import com.wisetrip.modelo.SeleccionDestinos;
import com.wisetrip.modelo.Ubicacion;
import com.wisetrip.modelo.Usuario;
import com.wisetrip.negocio.IFechasServicio;
import com.wisetrip.negocio.IRepositorioPreferencias;
import com.wisetrip.negocio.IRepositorioViajes;
import com.wisetrip.negocio.ISeleccionDestino;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@DisplayName("SeleccionControlador")
class SeleccionControladorTest {

    private static final int ID_CIUDAD = 15;
    private static final int ID_USUARIO = 3;
    private static final String ORIGEN = "Bogotá, Colombia";

    @Mock
    private ISeleccionDestino seleccionDestino;
    @Mock
    private IRepositorioViajes viajeDAO;
    @Mock
    private IRepositorioPreferencias preferenciaDAO;
    @Mock
    private IFechasServicio fechasServicio;

    private SeleccionControlador controlador;
    private MockHttpSession sesion;
    private RedirectAttributesModelMap flash;
    private Usuario usuario;
    private Map<String, Boolean> atributos;
    private FechasViaje fechas;
    private ResultadoRecomendacion resultado;

    @BeforeEach
    void setUp() {
        controlador = new SeleccionControlador(seleccionDestino, viajeDAO, preferenciaDAO, fechasServicio);
        sesion = new MockHttpSession();
        flash = new RedirectAttributesModelMap();
        usuario = new Usuario();
        usuario.setIdUsuario(ID_USUARIO);
        atributos = Map.of("playa", true);
        fechas = new FechasViaje();
        fechas.setFechaInicio(LocalDate.now().plusDays(1).toString());
        fechas.setFechaFin(LocalDate.now().plusDays(4).toString());
        Ciudad ciudad = new Ciudad();
        ciudad.setId(ID_CIUDAD);
        ciudad.setNombre("Cartagena");
        ciudad.setPais("Colombia");
        resultado = new ResultadoRecomendacion(ciudad, 0.9, 0.8, 0.7, true, false);
    }

    /** Deja la sesión lista para elegir un destino: usuario, cuestionario, presupuesto, fechas, destinos y origen. */
    private void sesionCompleta() {
        SeleccionDestinos seleccion = new SeleccionDestinos(List.of(resultado), "Tus destinos");
        sesion.setAttribute("usuarioActivo", usuario);
        sesion.setAttribute("atributosSeleccionados", atributos);
        sesion.setAttribute("presupuestoEnUsd", 1000.0);
        sesion.setAttribute("fechasViaje", fechas);
        sesion.setAttribute("seleccionDestinos", seleccion);
        SesionViaje.de(sesion, usuario).setOrigen(new Ubicacion("Colombia", "Bogotá", null));
        when(seleccionDestino.hayDestinos(seleccion)).thenReturn(true);
        when(seleccionDestino.elegir(seleccion, ID_CIUDAD)).thenReturn(Optional.of(resultado));
    }

    // ---------------------------------------------------------------- redirecciones

    @Test
    @DisplayName("sin usuario activo redirige al login")
    void sinUsuario_redirigeAlLogin() {
        assertEquals("redirect:/login", controlador.elegirDestino(ID_CIUDAD, sesion, flash));
    }

    @Test
    @DisplayName("sin respuestas del cuestionario redirige a las preferencias")
    void sinAtributos_redirigeAPreferencias() {
        sesion.setAttribute("usuarioActivo", usuario);

        assertEquals("redirect:/preferencias", controlador.elegirDestino(ID_CIUDAD, sesion, flash));
    }

    @Test
    @DisplayName("sin presupuesto válido redirige al presupuesto (nulo, cero, negativo o no finito)")
    void presupuestoInvalido_redirigeAlPresupuesto() {
        sesion.setAttribute("usuarioActivo", usuario);
        sesion.setAttribute("atributosSeleccionados", atributos);

        assertEquals("redirect:/presupuesto", controlador.elegirDestino(ID_CIUDAD, sesion, flash));
    }

    @Test
    @DisplayName("sin fechas válidas redirige a las fechas")
    void fechasInvalidas_redirigeALasFechas() {
        sesion.setAttribute("usuarioActivo", usuario);
        sesion.setAttribute("atributosSeleccionados", atributos);
        sesion.setAttribute("presupuestoEnUsd", 1000.0);
        sesion.setAttribute("fechasViaje", new FechasViaje());

        assertEquals("redirect:/fechas", controlador.elegirDestino(ID_CIUDAD, sesion, flash));
    }

    @Test
    @DisplayName("si no hay destinos recomendados redirige a las recomendaciones")
    void sinDestinos_redirigeALasRecomendaciones() {
        sesionCompleta();
        sesion.removeAttribute("seleccionDestinos");

        assertEquals("redirect:/recomendaciones", controlador.elegirDestino(ID_CIUDAD, sesion, flash));
    }

    @Test
    @DisplayName("si el destino elegido no está en la lista redirige a las recomendaciones")
    void destinoNoEncontrado_redirigeALasRecomendaciones() {
        sesionCompleta();
        SeleccionDestinos seleccion = (SeleccionDestinos) sesion.getAttribute("seleccionDestinos");
        when(seleccionDestino.elegir(seleccion, 999)).thenReturn(Optional.empty());

        assertEquals("redirect:/recomendaciones", controlador.elegirDestino(999, sesion, flash));
    }

    @Test
    @DisplayName("sin origen completo redirige a pedir el origen")
    void sinOrigen_redirigeAlOrigen() {
        sesionCompleta();
        SesionViaje.de(sesion, usuario).setOrigen(new Ubicacion());

        assertEquals("redirect:/origen", controlador.elegirDestino(ID_CIUDAD, sesion, flash));
    }

    @Test
    @DisplayName("si las fechas tienen errores vuelve a las fechas y no guarda el viaje")
    void erroresDeFechas_redirigeALasFechasSinGuardar() {
        sesionCompleta();
        Map<String, String> errores = Map.of("fechaFin", "Las fechas se cruzan con otro viaje.");
        when(fechasServicio.validarFechas(fechas, ID_USUARIO, 0)).thenReturn(errores);

        String vista = controlador.elegirDestino(ID_CIUDAD, sesion, flash);

        assertEquals("redirect:/fechas", vista);
        assertSame(errores, flash.getFlashAttributes().get("errores"));
        verify(viajeDAO, never()).insertar(anyInt(), anyInt(), any(), anyDouble(), anyString());
    }

    // ---------------------------------------------------------------- guardado

    @Test
    @DisplayName("un viaje nuevo se inserta, guarda las preferencias y deja el destino en la sesión")
    void viajeNuevo_seInsertaYRedirigeAlPlan() {
        sesionCompleta();
        when(fechasServicio.validarFechas(fechas, ID_USUARIO, 0)).thenReturn(Map.of());
        when(viajeDAO.insertar(ID_USUARIO, ID_CIUDAD, fechas, 1000.0, ORIGEN)).thenReturn(77);

        String vista = controlador.elegirDestino(ID_CIUDAD, sesion, flash);

        assertEquals("redirect:/plan", vista);
        assertEquals(77, sesion.getAttribute("idViajeGuardado"));
        verify(preferenciaDAO).guardarActivas(77, atributos);
        assertSame(resultado, sesion.getAttribute("destinoElegido"));
        assertEquals("Colombia", sesion.getAttribute("paisDestino"));
    }

    @Test
    @DisplayName("un viaje ya guardado se actualiza en lugar de insertarse")
    void viajeExistente_seActualiza() {
        sesionCompleta();
        sesion.setAttribute("idViajeGuardado", 40);
        when(fechasServicio.validarFechas(fechas, ID_USUARIO, 40)).thenReturn(Map.of());

        String vista = controlador.elegirDestino(ID_CIUDAD, sesion, flash);

        assertEquals("redirect:/plan", vista);
        verify(viajeDAO).actualizar(40, ID_USUARIO, ID_CIUDAD, fechas, 1000.0, ORIGEN);
        verify(viajeDAO, never()).insertar(anyInt(), anyInt(), any(), anyDouble(), anyString());
        verify(preferenciaDAO).guardarActivas(40, atributos);
    }

    @Test
    @DisplayName("si el guardado lanza IllegalArgumentException muestra su mensaje y vuelve a recomendaciones")
    void errorDeArgumento_muestraElMensaje() {
        sesionCompleta();
        when(viajeDAO.insertar(anyInt(), anyInt(), any(), anyDouble(), anyString()))
                .thenThrow(new IllegalArgumentException("El destino ya no está disponible."));

        String vista = controlador.elegirDestino(ID_CIUDAD, sesion, flash);

        assertEquals("redirect:/recomendaciones", vista);
        assertEquals("El destino ya no está disponible.", flash.getFlashAttributes().get("errorGuardado"));
    }

    @Test
    @DisplayName("si el guardado lanza IllegalStateException muestra un aviso genérico")
    void errorDeEstado_muestraAvisoGenerico() {
        sesionCompleta();
        when(viajeDAO.insertar(anyInt(), anyInt(), any(), anyDouble(), anyString()))
                .thenThrow(new IllegalStateException("fallo de base de datos"));

        String vista = controlador.elegirDestino(ID_CIUDAD, sesion, flash);

        assertEquals("redirect:/recomendaciones", vista);
        assertEquals("No pudimos completar el guardado de tu viaje. Elige el destino nuevamente para reintentar.",
                flash.getFlashAttributes().get("errorGuardado"));
    }
}

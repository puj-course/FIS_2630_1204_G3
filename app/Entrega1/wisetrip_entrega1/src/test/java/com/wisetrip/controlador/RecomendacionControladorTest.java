package com.wisetrip.controlador;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
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

import com.wisetrip.datos.CiudadSemilla;
import com.wisetrip.modelo.Ciudad;
import com.wisetrip.modelo.FechasViaje;
import com.wisetrip.modelo.Importancia;
import com.wisetrip.modelo.Presupuesto;
import com.wisetrip.modelo.PreferenciasUsuario;
import com.wisetrip.modelo.ResultadoRecomendacion;
import com.wisetrip.modelo.SeleccionDestinos;
import com.wisetrip.modelo.Usuario;
import com.wisetrip.modelo.ValidacionCiudades;
import com.wisetrip.negocio.CiudadNegocio;
import com.wisetrip.negocio.Recomendador;
import com.wisetrip.negocio.SeleRecomendaciones;
import com.wisetrip.servicio.LlenarAtributosCiudad;
import com.wisetrip.servicio.UsuarioSesionFacade;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@DisplayName("RecomendacionControlador")
class RecomendacionControladorTest {

    @Mock
    private CiudadNegocio ciudadDAO;
    @Mock
    private LlenarAtributosCiudad llenarAtributosCiudad;
    @Mock
    private Recomendador recomendador;
    @Mock
    private SeleRecomendaciones selectorDestinos;
    @Mock
    private UsuarioSesionFacade usuarioSesionFacade;

    private RecomendacionControlador controlador;
    private MockHttpSession sesion;
    private ConcurrentModel model;
    private Usuario usuario;

    @BeforeEach
    void setUp() {
        controlador = new RecomendacionControlador(
                ciudadDAO, llenarAtributosCiudad, recomendador, selectorDestinos, usuarioSesionFacade);
        sesion = new MockHttpSession();
        model = new ConcurrentModel();
        usuario = new Usuario();
        usuario.setIdUsuario(3);
        when(usuarioSesionFacade.prepararEncabezado(sesion, model)).thenReturn(usuario);
    }

    private static FechasViaje fechas(int duracionDias) {
        FechasViaje fechas = new FechasViaje();
        fechas.setFechaInicio(LocalDate.now().plusDays(1).toString());
        fechas.setFechaFin(LocalDate.now().plusDays(duracionDias).toString());
        return fechas;
    }

    private static Ciudad ciudad(String nombre, String pais) {
        Ciudad ciudad = new Ciudad();
        ciudad.setNombre(nombre);
        ciudad.setPais(pais);
        return ciudad;
    }

    private Map<String, Boolean> atributos() {
        return Map.of("playa", true);
    }

    private Map<String, Importancia> importancias() {
        return Map.of("playa", Importancia.GUSTAR);
    }

    // ---------------------------------------------------------------- rutas de redirección

    @Test
    @DisplayName("sin usuario activo redirige al login")
    void sinUsuario_redirigeAlLogin() {
        when(usuarioSesionFacade.prepararEncabezado(sesion, model)).thenReturn(null);

        assertEquals("redirect:/login", controlador.mostrarRecomendaciones(sesion, model));
    }

    @Test
    @DisplayName("sin respuestas del cuestionario redirige a las preferencias")
    void sinAtributos_redirigeAPreferencias() {
        assertEquals("redirect:/preferencias", controlador.mostrarRecomendaciones(sesion, model));
    }

    @Test
    @DisplayName("sin importancias seleccionadas redirige a las preferencias")
    void sinImportancias_redirigeAPreferencias() {
        sesion.setAttribute("atributosSeleccionados", atributos());

        assertEquals("redirect:/preferencias", controlador.mostrarRecomendaciones(sesion, model));
    }

    @Test
    @DisplayName("sin presupuesto en dólares redirige al presupuesto")
    void sinPresupuesto_redirigeAlPresupuesto() {
        sesion.setAttribute("atributosSeleccionados", atributos());
        sesion.setAttribute("importanciasSeleccionadas", importancias());

        assertEquals("redirect:/presupuesto", controlador.mostrarRecomendaciones(sesion, model));
    }

    @Test
    @DisplayName("sin fechas redirige a las fechas")
    void sinFechas_redirigeALasFechas() {
        sesion.setAttribute("atributosSeleccionados", atributos());
        sesion.setAttribute("importanciasSeleccionadas", importancias());
        sesion.setAttribute("presupuestoEnUsd", 1000.0);

        assertEquals("redirect:/fechas", controlador.mostrarRecomendaciones(sesion, model));
    }

    @Test
    @DisplayName("con fechas que no cubren ni un día redirige a las fechas")
    void fechasSinDuracion_redirigeALasFechas() {
        sesion.setAttribute("atributosSeleccionados", atributos());
        sesion.setAttribute("importanciasSeleccionadas", importancias());
        sesion.setAttribute("presupuestoEnUsd", 1000.0);
        sesion.setAttribute("fechasViaje", new FechasViaje());

        assertEquals("redirect:/fechas", controlador.mostrarRecomendaciones(sesion, model));
    }

    // ---------------------------------------------------------------- camino completo

    @Test
    @DisplayName("con todos los datos calcula las recomendaciones y las muestra")
    void conTodosLosDatos_muestraLasRecomendaciones() {
        Map<String, Boolean> atributos = atributos();
        Map<String, Importancia> importancias = importancias();
        FechasViaje fechas = fechas(3);
        Presupuesto presupuesto = new Presupuesto("1000", "USD");
        sesion.setAttribute("atributosSeleccionados", atributos);
        sesion.setAttribute("importanciasSeleccionadas", importancias);
        sesion.setAttribute("presupuestoEnUsd", 1000.0);
        sesion.setAttribute("fechasViaje", fechas);
        sesion.setAttribute("presupuestoViaje", presupuesto);

        Ciudad cartagena = ciudad("Cartagena", "Colombia");     // existe en los datos semilla
        Ciudad desconocida = ciudad("Atlantis", "Colombia");    // no existe en los datos semilla
        Ciudad cara = ciudad("Medellín", "Colombia");           // existe, pero excede el presupuesto
        when(ciudadDAO.obtenerTodas()).thenReturn(List.of(cartagena, desconocida, cara));
        when(recomendador.excedePresupuesto(any(Ciudad.class), any(PreferenciasUsuario.class))).thenReturn(false);
        when(recomendador.excedePresupuesto(eq(cara), any(PreferenciasUsuario.class))).thenReturn(true);

        ValidacionCiudades validacion = new ValidacionCiudades(List.of(cartagena), List.of(cara), List.of(desconocida));
        when(recomendador.validarCiudades(any(), any(PreferenciasUsuario.class))).thenReturn(validacion);
        ResultadoRecomendacion resultado = new ResultadoRecomendacion(cartagena, 0.9, 0.8, 0.7, true, false);
        when(recomendador.puntuarCiudadesValidas(any(ValidacionCiudades.class), any(PreferenciasUsuario.class)))
                .thenReturn(List.of(resultado));
        SeleccionDestinos seleccion = new SeleccionDestinos(List.of(resultado), "Estos son tus destinos");
        when(selectorDestinos.seleccionarMejoresDestinos(List.of(resultado))).thenReturn(seleccion);

        String vista = controlador.mostrarRecomendaciones(sesion, model);

        assertEquals("recomendaciones", vista);
        assertSame(seleccion, model.getAttribute("seleccion"));
        assertSame(seleccion, sesion.getAttribute("seleccionDestinos"));
        assertEquals(List.of(desconocida), model.getAttribute("ciudadesPendientes"));
        assertSame(fechas, model.getAttribute("fechas"));
        assertSame(presupuesto, model.getAttribute("presupuesto"));
        // solo se enriquece la ciudad que cabe en el presupuesto y existe en los datos semilla
        verify(llenarAtributosCiudad).enriquecer(eq(cartagena), any(CiudadSemilla.class), eq(atributos), eq(importancias));
        verify(llenarAtributosCiudad, never()).enriquecer(eq(desconocida), any(), any(), any());
        verify(llenarAtributosCiudad, never()).enriquecer(eq(cara), any(), any(), any());
    }
}

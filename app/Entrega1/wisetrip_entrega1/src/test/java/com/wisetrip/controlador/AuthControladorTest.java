package com.wisetrip.controlador;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;

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
import org.springframework.web.servlet.mvc.support.RedirectAttributesModelMap;

import com.wisetrip.modelo.Usuario;
import com.wisetrip.negocio.IAutenticacionUsuario;
import com.wisetrip.negocio.IRegistroUsuario;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@DisplayName("AuthControlador")
class AuthControladorTest {

    @Mock
    private IRegistroUsuario registro;
    @Mock
    private IAutenticacionUsuario autenticacion;

    private AuthControlador controlador;
    private MockHttpSession sesion;
    private ConcurrentModel model;
    private RedirectAttributesModelMap flash;

    @BeforeEach
    void setUp() {
        controlador = new AuthControlador(registro, autenticacion);
        sesion = new MockHttpSession();
        model = new ConcurrentModel();
        flash = new RedirectAttributesModelMap();
    }

    private static Usuario usuario(int id, String nombre, String correo) {
        Usuario usuario = new Usuario();
        usuario.setIdUsuario(id);
        usuario.setNombreCompleto(nombre);
        usuario.setCorreo(correo);
        return usuario;
    }

    // ---------------------------------------------------------------- registro

    @Test
    @DisplayName("mostrarRegistro agrega un usuario vacío al modelo y muestra el formulario")
    void mostrarRegistro_agregaUsuarioVacio() {
        String vista = controlador.mostrarRegistro(model);

        assertEquals("registro", vista);
        assertTrue(model.getAttribute("usuario") instanceof Usuario);
    }

    @Test
    @DisplayName("procesarRegistro con errores vuelve al formulario y no registra")
    void procesarRegistro_conErrores_vuelveAlFormulario() {
        Usuario nuevo = usuario(0, "Valeria Cortes", "mal");
        Map<String, String> errores = Map.of("correo", "El formato del correo no es valido.");
        when(registro.validarRegistro(nuevo, "clave1")).thenReturn(errores);

        String vista = controlador.procesarRegistro(nuevo, "clave1", sesion, model, flash);

        assertEquals("registro", vista);
        assertSame(errores, model.getAttribute("errores"));
        verify(registro, never()).registrar(any(Usuario.class));
        assertNull(sesion.getAttribute("usuarioActivo"));
    }

    @Test
    @DisplayName("procesarRegistro válido registra, abre la sesión y redirige")
    void procesarRegistro_valido_registraYRedirige() {
        Usuario nuevo = usuario(7, "Valeria Cortes", "valeria@correo.com");
        when(registro.validarRegistro(nuevo, "clave1")).thenReturn(Map.of());

        String vista = controlador.procesarRegistro(nuevo, "clave1", sesion, model, flash);

        assertEquals("redirect:/registro-exitoso", vista);
        verify(registro).registrar(nuevo);
        assertSame(nuevo, sesion.getAttribute("usuarioActivo"));
        assertEquals("Valeria Cortes", flash.getFlashAttributes().get("nombre"));
        assertEquals(nuevo.getCorreo(), flash.getFlashAttributes().get("correo"));
        assertEquals("Valeria", flash.getFlashAttributes().get("primerNombre"));
        assertEquals(nuevo.iniciales(), flash.getFlashAttributes().get("inicialesUsuario"));
    }

    @Test
    @DisplayName("registroExitoso y mostrarLogin devuelven sus vistas")
    void vistasSimples() {
        assertEquals("registro-exitoso", controlador.registroExitoso());
        assertEquals("login", controlador.mostrarLogin());
    }

    // ---------------------------------------------------------------- login

    @Test
    @DisplayName("procesarLogin sin correo muestra el error y no consulta la autenticación")
    void procesarLogin_sinCorreo_error() {
        String vista = controlador.procesarLogin("  ", "clave1", sesion, model);

        assertEquals("login", vista);
        assertEquals("Ingresa tu correo y contrasena.", model.getAttribute("error"));
        verifyNoInteractions(autenticacion);
    }

    @Test
    @DisplayName("procesarLogin sin contraseña muestra el error y conserva el correo escrito")
    void procesarLogin_sinPassword_error() {
        String vista = controlador.procesarLogin("valeria@correo.com", "", sesion, model);

        assertEquals("login", vista);
        assertEquals("Ingresa tu correo y contrasena.", model.getAttribute("error"));
        assertEquals("valeria@correo.com", model.getAttribute("correo"));
        verifyNoInteractions(autenticacion);
    }

    @Test
    @DisplayName("procesarLogin con credenciales incorrectas muestra el error")
    void procesarLogin_credencialesIncorrectas_error() {
        when(autenticacion.iniciarSesion("valeria@correo.com", "mala")).thenReturn(null);

        String vista = controlador.procesarLogin("valeria@correo.com", "mala", sesion, model);

        assertEquals("login", vista);
        assertEquals("Correo o contrasena incorrectos.", model.getAttribute("error"));
        assertEquals("valeria@correo.com", model.getAttribute("correo"));
        assertNull(sesion.getAttribute("usuarioActivo"));
    }

    @Test
    @DisplayName("procesarLogin correcto guarda el usuario en la sesión y redirige a los viajes")
    void procesarLogin_correcto_abreSesion() {
        Usuario existente = usuario(5, "Valeria Cortes", "valeria@correo.com");
        when(autenticacion.iniciarSesion("valeria@correo.com", "clave1")).thenReturn(existente);

        String vista = controlador.procesarLogin("valeria@correo.com", "clave1", sesion, model);

        assertEquals("redirect:/viajes", vista);
        assertSame(existente, sesion.getAttribute("usuarioActivo"));
    }

    @Test
    @DisplayName("al entrar con otro usuario se borra el viaje que había en la sesión")
    void procesarLogin_otroUsuario_reiniciaElViaje() {
        sesion.setAttribute("usuarioActivo", usuario(1, "Otra Persona", "otra@correo.com"));
        sesion.setAttribute("presupuestoEnUsd", 500.0);
        Usuario nuevo = usuario(2, "Valeria Cortes", "valeria@correo.com");
        when(autenticacion.iniciarSesion("valeria@correo.com", "clave1")).thenReturn(nuevo);

        controlador.procesarLogin("valeria@correo.com", "clave1", sesion, model);

        assertNull(sesion.getAttribute("presupuestoEnUsd"));
        assertSame(nuevo, sesion.getAttribute("usuarioActivo"));
    }

    @Test
    @DisplayName("al entrar de nuevo con el mismo usuario se conserva el viaje de la sesión")
    void procesarLogin_mismoUsuario_conservaElViaje() {
        sesion.setAttribute("usuarioActivo", usuario(2, "Valeria Cortes", "valeria@correo.com"));
        sesion.setAttribute("presupuestoEnUsd", 500.0);
        Usuario mismo = usuario(2, "Valeria Cortes", "valeria@correo.com");
        when(autenticacion.iniciarSesion("valeria@correo.com", "clave1")).thenReturn(mismo);

        controlador.procesarLogin("valeria@correo.com", "clave1", sesion, model);

        assertEquals(500.0, sesion.getAttribute("presupuestoEnUsd"));
    }

    // ---------------------------------------------------------------- logout

    @Test
    @DisplayName("cerrarSesion invalida la sesión y vuelve al inicio")
    void cerrarSesion_invalidaLaSesion() {
        String vista = controlador.cerrarSesion(sesion);

        assertEquals("redirect:/", vista);
        assertTrue(sesion.isInvalid());
    }
}

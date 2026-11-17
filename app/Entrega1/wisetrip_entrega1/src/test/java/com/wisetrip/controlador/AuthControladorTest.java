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
import org.springframework.mock.web.MockHttpSession;
import org.springframework.ui.ConcurrentModel;
import org.springframework.web.servlet.mvc.support.RedirectAttributesModelMap;

import com.wisetrip.modelo.Usuario;
import com.wisetrip.negocio.IAutenticacionUsuario;
import com.wisetrip.negocio.IRegistroUsuario;

@ExtendWith(MockitoExtension.class)
@DisplayName("AuthControlador")
class AuthControladorTest {

    @Mock
    private IRegistroUsuario registro;
    @Mock
    private IAutenticacionUsuario autenticacion;

    private AuthControlador controlador; //clase probada
    private MockHttpSession sesion; //mantiene la sesión del usuario activo
    private ConcurrentModel model; // modelo que el controldaor llena en la vista
    private RedirectAttributesModelMap flash; //datos que solo van a verse en una redirección

    //antes de cada test se crea un controlador nuevo y una sesión nueva
    @BeforeEach
    void setUp() {
        controlador = new AuthControlador(registro, autenticacion);
        sesion = new MockHttpSession();
        model = new ConcurrentModel();
        flash = new RedirectAttributesModelMap();
    }

    //arma un usuario de prueba
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

        //devuelve el nombre de la vista 
        assertEquals("registro", vista);
        //el modelo tiene un usuario vacío para llenar el formulario
        assertTrue(model.getAttribute("usuario") instanceof Usuario);
    }

    @Test
    @DisplayName("procesarRegistro con errores vuelve al formulario y no registra")
    void procesarRegistro_conErrores_vuelveAlFormulario() {
        //prepara la invocación del servicio de registro para que devuelva errores de formato 
        Usuario nuevo = usuario(0, "Valeria Cortes", "mal");

        //map.of es un metodo de java 9 crea un mapa de lectura. usa llave como correo y valor el mensaje
        Map<String, String> errores = Map.of("correo", "El formato del correo no es valido.");
        when(registro.validarRegistro(nuevo, "clave1")).thenReturn(errores);

        String vista = controlador.procesarRegistro(nuevo, "clave1", sesion, model, flash);

        //vuelve a la vista y pasa los errores 
        assertEquals("registro", vista);
        assertSame(errores, model.getAttribute("errores"));

        //no se llama al registro porque hay errores
        verify(registro, never()).registrar(any(Usuario.class));
        assertNull(sesion.getAttribute("usuarioActivo"));
    }

    @Test
    @DisplayName("procesarRegistro válido registra, abre la sesión y redirige")
    void procesarRegistro_valido_registraYRedirige() {
        // crea el mapa de errores vacío para simular que el registro es válido
        Usuario nuevo = usuario(7, "Valeria Cortes", "valeria@correo.com");
        when(registro.validarRegistro(nuevo, "clave1")).thenReturn(Map.of());

        String vista = controlador.procesarRegistro(nuevo, "clave1", sesion, model, flash);

        //redirige a la vista de registro exitoso
        assertEquals("redirect:/registro-exitoso", vista);

        //guarda el usuario y deja la sesión abierta
        verify(registro).registrar(nuevo);
        assertSame(nuevo, sesion.getAttribute("usuarioActivo"));
        //los datos del usuario se pasan a la vista de registro exitoso
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
    @DisplayName("procesarLogin con correo o contraseña nulos o vacíos muestra el error y no consulta la autenticación")
    void procesarLogin_datosIncompletos_error() {
        String[][] casos = {
                {null, "clave1"},                  // correo nulo
                {"  ", "clave1"},                  // correo en blanco
                {"valeria@correo.com", null},      // contraseña nula
                {"valeria@correo.com", ""}         // contraseña vacía
        };

        for (String[] caso : casos) {

            //se crea un modelo nuevo por cada caso 
            ConcurrentModel modelo = new ConcurrentModel();

            String vista = controlador.procesarLogin(caso[0], caso[1], sesion, modelo);
            
            //los casos dan el mismo error y vuelven al login 
            assertEquals("login", vista);
            assertEquals("Ingresa tu correo y contrasena.", modelo.getAttribute("error"));
            assertEquals(caso[0], modelo.getAttribute("correo"));
        }
        //con los datos incompletos no se hace la autenticación
        verifyNoInteractions(autenticacion);
    }

    @Test
    @DisplayName("procesarLogin con credenciales incorrectas muestra el error")
    void procesarLogin_credencialesIncorrectas_error() {
        //se devulve null cuando el correo y la contraseña no coinciden con un usuario
        when(autenticacion.iniciarSesion("valeria@correo.com", "mala")).thenReturn(null);

        String vista = controlador.procesarLogin("valeria@correo.com", "mala", sesion, model);

        assertEquals("login", vista);
        assertEquals("Correo o contrasena incorrectos.", model.getAttribute("error"));
        assertEquals("valeria@correo.com", model.getAttribute("correo"));
        //no abre sesion
        assertNull(sesion.getAttribute("usuarioActivo"));
    }

    @Test
    @DisplayName("procesarLogin correcto guarda el usuario en la sesión y redirige a los viajes")
    void procesarLogin_correcto_abreSesion() {
        Usuario existente = usuario(5, "Valeria Cortes", "valeria@correo.com");
        // cuando el correo y la contraseña coinciden, devuelve el usuario existente
        when(autenticacion.iniciarSesion("valeria@correo.com", "clave1")).thenReturn(existente);

        String vista = controlador.procesarLogin("valeria@correo.com", "clave1", sesion, model);

        assertEquals("redirect:/viajes", vista);
        assertSame(existente, sesion.getAttribute("usuarioActivo"));
    }

    @Test
    @DisplayName("al entrar con otro usuario se borra el viaje que había en la sesión")
    void procesarLogin_otroUsuario_reiniciaElViaje() {
        //la sesión tiene un usuario activo y un viaje guardado
        sesion.setAttribute("usuarioActivo", usuario(1, "Otra Persona", "otra@correo.com"));
        sesion.setAttribute("presupuestoEnUsd", 500.0);
        //inicia sesión con otro usuario
        Usuario nuevo = usuario(2, "Valeria Cortes", "valeria@correo.com");
        when(autenticacion.iniciarSesion("valeria@correo.com", "clave1")).thenReturn(nuevo);

        controlador.procesarLogin("valeria@correo.com", "clave1", sesion, model);

        //el viaje y el presupuesto de la sesión se reinician
        assertNull(sesion.getAttribute("presupuestoEnUsd"));
        assertSame(nuevo, sesion.getAttribute("usuarioActivo"));
    }

    @Test
    @DisplayName("al entrar de nuevo con el mismo usuario se conserva el viaje de la sesión")
    void procesarLogin_mismoUsuario_conservaElViaje() {
        //la sesión tiene un usuario activo y un viaje guardado
        sesion.setAttribute("usuarioActivo", usuario(2, "Valeria Cortes", "valeria@correo.com"));
        sesion.setAttribute("presupuestoEnUsd", 500.0);
        //inicia sesión con el mismo usuario
        Usuario mismo = usuario(2, "Valeria Cortes", "valeria@correo.com");
        when(autenticacion.iniciarSesion("valeria@correo.com", "clave1")).thenReturn(mismo);

        controlador.procesarLogin("valeria@correo.com", "clave1", sesion, model);
        //el viaje y el presupuesto de la sesión se conservan con el mismo usuario
        assertEquals(500.0, sesion.getAttribute("presupuestoEnUsd"));
    }

    // ---------------------------------------------------------------- logout

    @Test
    @DisplayName("cerrarSesion invalida la sesión y vuelve al inicio")
    void cerrarSesion_invalidaLaSesion() {
        String vista = controlador.cerrarSesion(sesion);

        assertEquals("redirect:/", vista);
        //la sesión queda inválida y no se puede usar más
        assertTrue(sesion.isInvalid());
    }
}
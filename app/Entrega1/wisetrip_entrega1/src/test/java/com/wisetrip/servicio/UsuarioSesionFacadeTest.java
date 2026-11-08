package com.wisetrip.servicio;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.ui.ExtendedModelMap;

import com.wisetrip.modelo.Usuario;

/**
 * Pruebas unitarias de UsuarioSesionFacade (patrón Facade).
 *
 * La sesión HTTP y el modelo de la vista son objetos de prueba de Spring
 * (MockHttpSession y ExtendedModelMap): no hace falta levantar el servidor.
 */
@DisplayName("UsuarioSesionFacade")
class UsuarioSesionFacadeTest {

    private UsuarioSesionFacade facade;
    private MockHttpSession sesion;
    private ExtendedModelMap model;

    @BeforeEach
    void setUp() {
        facade = new UsuarioSesionFacade();
        sesion = new MockHttpSession();
        model = new ExtendedModelMap();
    }

    private static Usuario usuario(String nombre) {
        Usuario usuario = new Usuario();
        usuario.setNombreCompleto(nombre);
        return usuario;
    }

    // ------------------------------------------------------------ iniciales

    @Test
    @DisplayName("iniciales: nombre y apellido dan dos letras en mayúscula")
    void iniciales_nombreYApellido() {
        assertEquals("AR", facade.iniciales(usuario("ana rodriguez")));
    }

    @Test
    @DisplayName("iniciales: con un solo nombre da una letra")
    void iniciales_unSoloNombre() {
        assertEquals("A", facade.iniciales(usuario("ana")));
    }

    @Test
    @DisplayName("borde: espacios de más no cambian las iniciales")
    void iniciales_espaciosDeMas() {
        assertEquals("MA", facade.iniciales(usuario("   Maria    Alejandra  Rodriguez ")));
    }

    @Test
    @DisplayName("borde: usuario null o sin nombre muestra '?'")
    void iniciales_sinNombre_interrogacion() {
        assertEquals("?", facade.iniciales(null));
        assertEquals("?", facade.iniciales(usuario(null)));
        assertEquals("?", facade.iniciales(usuario("   ")));
    }

    // ------------------------------------------------------------ usuarioActivo

    @Test
    @DisplayName("usuarioActivo devuelve el usuario guardado en la sesión")
    void usuarioActivo_conSesion() {
        Usuario ana = usuario("Ana Rodriguez");
        sesion.setAttribute("usuarioActivo", ana);

        assertSame(ana, facade.usuarioActivo(sesion));
    }

    @Test
    @DisplayName("usuarioActivo devuelve null si nadie ha iniciado sesión")
    void usuarioActivo_sinSesion_null() {
        assertNull(facade.usuarioActivo(sesion));
    }

    // ------------------------------------------------------------ prepararEncabezado

    @Test
    @DisplayName("prepararEncabezado carga el usuario y sus iniciales en el modelo")
    void prepararEncabezado_cargaElModelo() {
        Usuario ana = usuario("Ana Rodriguez");
        sesion.setAttribute("usuarioActivo", ana);

        Usuario resultado = facade.prepararEncabezado(sesion, model);

        assertSame(ana, resultado);
        assertSame(ana, model.getAttribute("usuario"));
        assertEquals("AR", model.getAttribute("inicialesUsuario"));
    }

    @Test
    @DisplayName("sin sesión devuelve null y no toca el modelo (el controlador redirige a /login)")
    void prepararEncabezado_sinSesion_modeloVacio() {
        assertNull(facade.prepararEncabezado(sesion, model));
        assertFalse(model.containsAttribute("usuario"));
        assertFalse(model.containsAttribute("inicialesUsuario"));
    }
}

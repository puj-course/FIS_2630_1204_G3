package com.wisetrip.modelo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Pruebas unitarias de UsuarioBuilder (patrón Builder, rol ConcreteBuilder).
 * Revisan que cada paso arme su parte del Usuario y que getResult()
 * entregue el producto terminado y deje el builder listo para otro.
 */
@DisplayName("UsuarioBuilder")
class UsuarioBuilderTest {

    private UsuarioBuilder builder;

    @BeforeEach
    void setUp() {
        builder = new UsuarioBuilder();
    }

    @Test
    @DisplayName("todos los pasos arman un Usuario completo")
    void todosLosPasos_armanUsuarioCompleto() {
        builder.construirIdentidad(7, "Ana Rodriguez");
        builder.construirDocumento("CC", "1012345678", "2000-05-10");
        builder.construirCredenciales("ana@wisetrip.com", "secreta123", "admin");
        builder.construirContacto("555");

        Usuario usuario = builder.getResult();

        assertEquals(7, usuario.getIdUsuario());
        assertEquals("Ana Rodriguez", usuario.getNombreCompleto());
        assertEquals("CC", usuario.getTipoDocumento());
        assertEquals("1012345678", usuario.getNumeroDocumento());
        assertEquals("2000-05-10", usuario.getFechaNacimiento());
        assertEquals("ana@wisetrip.com", usuario.getCorreo());
        assertEquals("secreta123", usuario.getPassword());
        assertEquals("admin", usuario.getRol());
        assertEquals("555", usuario.getChatId());
    }

    @Test
    @DisplayName("sin rol en la base de datos usa el rol por defecto 'cliente'")
    void rolNull_usaCliente() {
        builder.construirCredenciales("ana@wisetrip.com", "secreta123", null);

        assertEquals("cliente", builder.getResult().getRol());
    }

    @Test
    @DisplayName("borde: un rol en blanco también usa 'cliente'")
    void rolEnBlanco_usaCliente() {
        builder.construirCredenciales("ana@wisetrip.com", "secreta123", "   ");

        assertEquals("cliente", builder.getResult().getRol());
    }

    @Test
    @DisplayName("getResult deja el builder limpio para armar otro usuario")
    void getResult_reiniciaElBuilder() {
        builder.construirIdentidad(1, "Primero");
        Usuario primero = builder.getResult();

        Usuario segundo = builder.getResult();

        assertNotSame(primero, segundo);
        assertNull(segundo.getNombreCompleto());
    }

    @Test
    @DisplayName("reiniciar descarta lo que se había armado")
    void reiniciar_descartaLoArmado() {
        builder.construirIdentidad(9, "Descartado");

        builder.reiniciar();

        assertEquals(0, builder.getResult().getIdUsuario());
    }
}

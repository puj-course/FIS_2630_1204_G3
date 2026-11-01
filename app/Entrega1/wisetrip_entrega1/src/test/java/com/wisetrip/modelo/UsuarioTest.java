package com.wisetrip.modelo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTimeout;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Duration;
import java.time.LocalDate;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Pruebas unitarias de Usuario.validarDatos (CP48 - CP57).
 * Cubren las reglas de correo y contraseña, y los dos arreglos hechos a partir de SonarCloud:
 * la llave del mapa de errores y la expresión regular del correo (sin backtracking).
 */
@DisplayName("Usuario.validarDatos")
class UsuarioTest {

    private static final String CLAVE_OK = "secreta1";

    private Usuario usuario;

    @BeforeEach
    void setUp() {
        usuario = new Usuario();
        usuario.setNombreCompleto("Valeria Cortes");
        usuario.setTipoDocumento("CC");
        usuario.setNumeroDocumento("1234567890");
        usuario.setFechaNacimiento("2000-05-10");
        usuario.setCorreo("valeria@javeriana.edu.co");
        usuario.setPassword(CLAVE_OK);
    }

    @Test
    @DisplayName("CP48 validarDatos: datos completos y válidos no generan errores")
    void cp48_validarDatos_datosValidos_sinErrores() {
        Map<String, String> errores = usuario.validarDatos(CLAVE_OK);

        assertTrue(errores.isEmpty());
    }

    @Test
    @DisplayName("CP49 validarDatos: correo sin arroba devuelve error 'correo'")
    void cp49_validarDatos_correoSinArroba_error() {
        usuario.setCorreo("sinarroba.com");

        Map<String, String> errores = usuario.validarDatos(CLAVE_OK);

        assertTrue(errores.containsKey("correo"));
    }

    @Test
    @DisplayName("CP50 validarDatos: borde, puntos seguidos en el dominio se rechazan (regex corregida)")
    void cp50_validarDatos_correoConPuntosSeguidos_error() {
        usuario.setCorreo("a@b..com");

        Map<String, String> errores = usuario.validarDatos(CLAVE_OK);

        assertTrue(errores.containsKey("correo"));
    }

    @Test
    @DisplayName("CP51 validarDatos: borde, contraseña de 5 caracteres es demasiado corta")
    void cp51_validarDatos_passwordDeCincoCaracteres_error() {
        usuario.setPassword("12345");

        Map<String, String> errores = usuario.validarDatos("12345");

        assertTrue(errores.containsKey("password"));
    }

    @Test
    @DisplayName("CP52 validarDatos: borde, contraseña de 6 caracteres es válida")
    void cp52_validarDatos_passwordDeSeisCaracteres_sinErrores() {
        usuario.setPassword("123456");

        Map<String, String> errores = usuario.validarDatos("123456");

        assertTrue(errores.isEmpty());
    }

    @Test
    @DisplayName("CP53 validarDatos: contraseñas que no coinciden devuelven error 'confirmarPassword'")
    void cp53_validarDatos_passwordsDistintas_error() {
        Map<String, String> errores = usuario.validarDatos("otra-clave");

        assertTrue(errores.containsKey("confirmarPassword"));
    }

    @Test
    @DisplayName("CP54 validarDatos: contraseña vacía devuelve errores en 'password' y 'confirmarPassword'")
    void cp54_validarDatos_passwordVacia_dosErrores() {
        usuario.setPassword("");

        Map<String, String> errores = usuario.validarDatos("");

        assertTrue(errores.containsKey("password"));
        assertTrue(errores.containsKey("confirmarPassword"));
    }

    @Test
    @DisplayName("CP55 validarDatos: borde, quien cumple 18 años hoy puede registrarse")
    void cp55_validarDatos_cumple18Hoy_sinErrores() {
        usuario.setFechaNacimiento(LocalDate.now().minusYears(18).toString());

        Map<String, String> errores = usuario.validarDatos(CLAVE_OK);

        assertTrue(errores.isEmpty());
    }

    @Test
    @DisplayName("CP56 validarDatos: borde, a un día de cumplir 18 años no puede registrarse")
    void cp56_validarDatos_unDiaAntesDeLos18_error() {
        usuario.setFechaNacimiento(LocalDate.now().minusYears(18).plusDays(1).toString());

        Map<String, String> errores = usuario.validarDatos(CLAVE_OK);

        assertTrue(errores.get("fechaNacimiento").contains("mayor de 18"));
    }

    @Test
    @DisplayName("CP57 validarDatos: un correo gigante se rechaza rápido (sin backtracking)")
    void cp57_validarDatos_correoGigante_respondeRapido() {
        usuario.setCorreo("a@" + "a.".repeat(20000) + "!");

        Map<String, String> errores = assertTimeout(Duration.ofSeconds(2), () -> usuario.validarDatos(CLAVE_OK));

        assertEquals("El formato del correo no es valido.", errores.get("correo"));
    }
}
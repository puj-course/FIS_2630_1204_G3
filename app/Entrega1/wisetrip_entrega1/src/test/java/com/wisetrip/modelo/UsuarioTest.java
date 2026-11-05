package com.wisetrip.modelo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTimeout;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Duration;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * Pruebas de validación de Usuario, incluyendo los casos CP48 - CP57.
 * Cubren correo, contraseña, fechas inválidas y límites de edad con fecha de Bogotá.
 */
@DisplayName("Usuario.validarDatos")
class UsuarioTest {

    private static final String CLAVE_OK = "secreta1";
    private static final ZoneId ZONA_BOGOTA = ZoneId.of("America/Bogota");

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
        usuario.setFechaNacimiento(LocalDate.now(ZONA_BOGOTA).minusYears(18).toString());

        Map<String, String> errores = usuario.validarDatos(CLAVE_OK);

        assertTrue(errores.isEmpty());
    }

    @Test
    @DisplayName("CP56 validarDatos: borde, a un día de cumplir 18 años no puede registrarse")
    void cp56_validarDatos_unDiaAntesDeLos18_error() {
        usuario.setFechaNacimiento(LocalDate.now(ZONA_BOGOTA).minusYears(18).plusDays(1).toString());

        Map<String, String> errores = usuario.validarDatos(CLAVE_OK);

        assertTrue(errores.get("fechaNacimiento").contains("mayor de 18"));
    }

    @Test
    @DisplayName("CP57 validarDatos: un correo gigante se rechaza rápido (sin backtracking)")
    void cp57_validarDatos_correoGigante_respondeRapido() {
        usuario.setCorreo("a@" + "a.".repeat(20000) + "@");

        Map<String, String> errores = assertTimeout(Duration.ofSeconds(2), () -> usuario.validarDatos(CLAVE_OK));

        assertEquals("El formato del correo no es valido.", errores.get("correo"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"no-es-fecha", "2025-02-29", "2026-13-01"})
    @DisplayName("validarDatos: fechas mal formadas o inexistentes devuelven un error")
    void validarDatos_fechaInvalida_error(String fecha) {
        usuario.setFechaNacimiento(fecha);

        Map<String, String> errores = usuario.validarDatos(CLAVE_OK);

        assertEquals(Map.of("fechaNacimiento", "Ingresa una fecha valida."), errores);
    }

    @Test
    @DisplayName("validarDatos: una fecha de nacimiento futura en Bogotá se rechaza")
    void validarDatos_fechaFutura_error() {
        usuario.setFechaNacimiento(LocalDate.now(ZONA_BOGOTA).plusDays(1).toString());

        Map<String, String> errores = usuario.validarDatos(CLAVE_OK);

        assertEquals(Map.of("fechaNacimiento", "La fecha de nacimiento no puede ser futura."), errores);
    }

    @Test
    @DisplayName("validarDatos: quien cumple 120 años hoy está dentro del límite")
    void validarDatos_cumple120Hoy_sinErrores() {
        usuario.setFechaNacimiento(LocalDate.now(ZONA_BOGOTA).minusYears(120).toString());

        assertTrue(usuario.validarDatos(CLAVE_OK).isEmpty());
    }

    @Test
    @DisplayName("validarDatos: una edad de 121 años supera el límite")
    void validarDatos_cumple121Hoy_error() {
        usuario.setFechaNacimiento(LocalDate.now(ZONA_BOGOTA).minusYears(121).toString());

        assertEquals(Map.of("fechaNacimiento", "Ingresa una fecha de nacimiento valida."),
                usuario.validarDatos(CLAVE_OK));
    }

    @Test
    @DisplayName("calcularEdad: el cumpleaños de mañana todavía no aumenta la edad")
    void calcularEdad_cumpleanosManana_edadAnterior() {
        usuario.setFechaNacimiento(LocalDate.now(ZONA_BOGOTA).minusYears(30).plusDays(1).toString());

        assertEquals(29, usuario.calcularEdad());
    }

    @Test
    @DisplayName("calcularEdad: el cumpleaños de hoy aumenta la edad")
    void calcularEdad_cumpleanosHoy_edadCumplida() {
        usuario.setFechaNacimiento(LocalDate.now(ZONA_BOGOTA).minusYears(30).toString());

        assertEquals(30, usuario.calcularEdad());
    }
}

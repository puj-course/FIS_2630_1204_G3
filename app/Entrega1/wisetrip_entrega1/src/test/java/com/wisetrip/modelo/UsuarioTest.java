package com.wisetrip.modelo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTimeout;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Duration;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

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
        // Antes de cada prueba creamos un usuario con datos válidos.
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

    @Test
    @DisplayName("Una fecha escrita con texto devuelve un error")
    void validarDatos_fechaConTexto_error() {
        // Datos de entrada
        usuario.setFechaNacimiento("no-es-fecha");

        // Resultado obtenido
        Map<String, String> errores = usuario.validarDatos(CLAVE_OK);
        String resultadoObtenido = errores.get("fechaNacimiento");

        // Resultado esperado y comparación
        String resultadoEsperado = "Ingresa una fecha valida.";
        assertEquals(resultadoEsperado, resultadoObtenido);
        assertEquals(1, errores.size());
    }

    @Test
    @DisplayName("El 29 de febrero de un año no bisiesto devuelve un error")
    void validarDatos_diaInexistente_error() {
        usuario.setFechaNacimiento("2025-02-29");

        Map<String, String> errores = usuario.validarDatos(CLAVE_OK);
        String resultadoObtenido = errores.get("fechaNacimiento");
        String resultadoEsperado = "Ingresa una fecha valida.";

        assertEquals(resultadoEsperado, resultadoObtenido);
        assertEquals(1, errores.size());
    }

    @Test
    @DisplayName("Una fecha con mes 13 devuelve un error")
    void validarDatos_mesInexistente_error() {
        usuario.setFechaNacimiento("2026-13-01");

        Map<String, String> errores = usuario.validarDatos(CLAVE_OK);
        String resultadoObtenido = errores.get("fechaNacimiento");
        String resultadoEsperado = "Ingresa una fecha valida.";

        assertEquals(resultadoEsperado, resultadoObtenido);
        assertEquals(1, errores.size());
    }

    @Test
    @DisplayName("validarDatos: una fecha de nacimiento futura en Bogotá se rechaza")
    void validarDatos_fechaFutura_error() {
        usuario.setFechaNacimiento(LocalDate.now(ZONA_BOGOTA).plusDays(1).toString());

        Map<String, String> errores = usuario.validarDatos(CLAVE_OK);

        String resultadoObtenido = errores.get("fechaNacimiento");
        String resultadoEsperado = "La fecha de nacimiento no puede ser futura.";

        assertEquals(resultadoEsperado, resultadoObtenido);
        assertEquals(1, errores.size());
    }

    @Test
    @DisplayName("validarDatos: quien cumple 120 años hoy está dentro del límite")
    void validarDatos_cumple120Hoy_sinErrores() {
        usuario.setFechaNacimiento(LocalDate.now(ZONA_BOGOTA).minusYears(120).toString());

        Map<String, String> errores = usuario.validarDatos(CLAVE_OK);
        int resultadoObtenido = errores.size();
        int resultadoEsperado = 0;

        assertEquals(resultadoEsperado, resultadoObtenido);
    }

    @Test
    @DisplayName("validarDatos: una edad de 121 años supera el límite")
    void validarDatos_cumple121Hoy_error() {
        usuario.setFechaNacimiento(LocalDate.now(ZONA_BOGOTA).minusYears(121).toString());

        Map<String, String> errores = usuario.validarDatos(CLAVE_OK);
        String resultadoObtenido = errores.get("fechaNacimiento");
        String resultadoEsperado = "Ingresa una fecha de nacimiento valida.";

        assertEquals(resultadoEsperado, resultadoObtenido);
        assertEquals(1, errores.size());
    }

    @Test
    @DisplayName("calcularEdad: el cumpleaños de mañana todavía no aumenta la edad")
    void calcularEdad_cumpleanosManana_edadAnterior() {
        usuario.setFechaNacimiento(LocalDate.now(ZONA_BOGOTA).minusYears(30).plusDays(1).toString());

        int resultadoObtenido = usuario.calcularEdad();
        int resultadoEsperado = 29;

        assertEquals(resultadoEsperado, resultadoObtenido);
    }

    @Test
    @DisplayName("calcularEdad: el cumpleaños de hoy aumenta la edad")
    void calcularEdad_cumpleanosHoy_edadCumplida() {
        usuario.setFechaNacimiento(LocalDate.now(ZONA_BOGOTA).minusYears(30).toString());

        int resultadoObtenido = usuario.calcularEdad();
        int resultadoEsperado = 30;

        assertEquals(resultadoEsperado, resultadoObtenido);
    }

    // ------------------------------------------------------------ campos vacios

    @Test
    @DisplayName("validarDatos: un formulario sin ningun dato devuelve un error por cada campo obligatorio")
    void validarDatos_todoNulo_errorEnCadaCampo() {
        Usuario vacio = new Usuario();

        Map<String, String> errores = vacio.validarDatos(null);

        assertEquals("Ingresa tu nombre completo.", errores.get("nombreCompleto"));
        assertEquals("Selecciona un tipo de documento.", errores.get("tipoDocumento"));
        assertEquals("Ingresa tu numero de documento.", errores.get("numeroDocumento"));
        assertEquals("Ingresa tu fecha de nacimiento.", errores.get("fechaNacimiento"));
        assertEquals("Ingresa tu correo electronico.", errores.get("correo"));
        assertEquals("Ingresa una contrasena.", errores.get("password"));
        assertEquals("Confirma tu contrasena.", errores.get("confirmarPassword"));
        assertEquals(7, errores.size());
    }

    @Test
    @DisplayName("validarDatos: borde, campos con solo espacios cuentan como vacios")
    void validarDatos_camposEnBlanco_errorDeObligatorio() {
        usuario.setNombreCompleto("   ");
        usuario.setTipoDocumento(" ");
        usuario.setNumeroDocumento("  ");
        usuario.setFechaNacimiento(" ");
        usuario.setCorreo("   ");

        Map<String, String> errores = usuario.validarDatos(CLAVE_OK);

        assertEquals("Ingresa tu nombre completo.", errores.get("nombreCompleto"));
        assertEquals("Selecciona un tipo de documento.", errores.get("tipoDocumento"));
        assertEquals("Ingresa tu numero de documento.", errores.get("numeroDocumento"));
        assertEquals("Ingresa tu fecha de nacimiento.", errores.get("fechaNacimiento"));
        assertEquals("Ingresa tu correo electronico.", errores.get("correo"));
        assertEquals(5, errores.size());
    }

    @Test
    @DisplayName("validarDatos: borde, un nombre de 2 letras es demasiado corto")
    void validarDatos_nombreDeDosLetras_error() {
        usuario.setNombreCompleto(" Al ");

        Map<String, String> errores = usuario.validarDatos(CLAVE_OK);

        assertEquals("El nombre es demasiado corto.", errores.get("nombreCompleto"));
        assertEquals(1, errores.size());
    }

    @Test
    @DisplayName("validarDatos: un documento con letras se rechaza")
    void validarDatos_documentoConLetras_error() {
        usuario.setNumeroDocumento("12AB34");

        Map<String, String> errores = usuario.validarDatos(CLAVE_OK);

        assertEquals("El documento debe tener entre 5 y 15 digitos.", errores.get("numeroDocumento"));
        assertEquals(1, errores.size());
    }

    @Test
    @DisplayName("validarDatos: sin contraseña no se compara con la confirmacion")
    void validarDatos_passwordNulaConConfirmacion_soloErrorDePassword() {
        usuario.setPassword(null);

        Map<String, String> errores = usuario.validarDatos(CLAVE_OK);

        assertEquals("Ingresa una contrasena.", errores.get("password"));
        assertEquals(1, errores.size());
    }

    // ------------------------------------------------------------ normalizar y setCorreo

    @Test
    @DisplayName("normalizar: quita espacios y deja el correo en minusculas")
    void normalizar_datosConEspacios_quedanLimpios() {
        usuario.setNombreCompleto("  Valeria Cortes  ");
        usuario.setNumeroDocumento(" 1234567890 ");
        usuario.setCorreo(" Valeria@Javeriana.EDU.co ");

        usuario.normalizar();

        assertEquals("Valeria Cortes", usuario.getNombreCompleto());
        assertEquals("1234567890", usuario.getNumeroDocumento());
        assertEquals("valeria@javeriana.edu.co", usuario.getCorreo());
    }

    @Test
    @DisplayName("normalizar: borde, con campos nulos no lanza excepcion y siguen nulos")
    void normalizar_camposNulos_siguenNulos() {
        Usuario vacio = new Usuario();

        vacio.normalizar();

        assertNull(vacio.getNombreCompleto());
        assertNull(vacio.getNumeroDocumento());
        assertNull(vacio.getCorreo());
    }

    @Test
    @DisplayName("setCorreo: borde, un correo nulo queda nulo")
    void setCorreo_nulo_quedaNulo() {
        usuario.setCorreo(null);

        assertNull(usuario.getCorreo());
    }

    // ------------------------------------------------------------ otros metodos

    @Test
    @DisplayName("verificarPassword: solo es true si la contraseña coincide")
    void verificarPassword_coincideONo() {
        assertTrue(usuario.verificarPassword(CLAVE_OK));
        assertFalse(usuario.verificarPassword("otra-clave"));
    }

    @Test
    @DisplayName("verificarPassword: borde, un usuario sin contraseña nunca coincide")
    void verificarPassword_sinPassword_false() {
        usuario.setPassword(null);

        assertFalse(usuario.verificarPassword(null));
    }

    @Test
    @DisplayName("tieneTelegramVinculado: falso sin chat_id o con chat_id en blanco, verdadero con chat_id")
    void tieneTelegramVinculado_segunChatId() {
        assertFalse(usuario.tieneTelegramVinculado());

        usuario.vincularTelegram("   ");
        assertFalse(usuario.tieneTelegramVinculado());

        usuario.vincularTelegram(" 555 ");
        assertTrue(usuario.tieneTelegramVinculado());
        assertEquals("555", usuario.getChatId());
    }

    @Test
    @DisplayName("iniciales: dos letras con nombre y apellido, una con solo nombre")
    void iniciales_segunElNombre() {
        assertEquals("VC", usuario.iniciales());

        usuario.setNombreCompleto("  ana  ");
        assertEquals("A", usuario.iniciales());
    }

    @Test
    @DisplayName("iniciales: borde, sin nombre o con nombre en blanco devuelve '?'")
    void iniciales_sinNombre_interrogacion() {
        usuario.setNombreCompleto(null);
        assertEquals("?", usuario.iniciales());

        usuario.setNombreCompleto("   ");
        assertEquals("?", usuario.iniciales());
    }
}

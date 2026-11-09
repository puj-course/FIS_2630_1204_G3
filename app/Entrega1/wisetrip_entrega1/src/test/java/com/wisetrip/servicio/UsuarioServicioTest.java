package com.wisetrip.servicio;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.wisetrip.modelo.Notificacion;
import com.wisetrip.modelo.Usuario;
import com.wisetrip.negocio.INotificador;
import com.wisetrip.negocio.IUsuarioRepositorio;

/**
 * Pruebas unitarias de UsuarioServicio: registro, inicio de sesión y
 * vinculación con Telegram.
 *
 * El repositorio y los dos notificadores (correo y Telegram) se simulan con
 * Mockito: no se envían correos ni mensajes reales ni se toca la base de datos.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("UsuarioServicio")
class UsuarioServicioTest {

    private static final String CORREO = "ana@wisetrip.com";
    private static final String PASSWORD = "secreta123";

    @Mock
    private IUsuarioRepositorio repositorio;

    @Mock
    private INotificador notificadorCorreo;

    @Mock
    private INotificador notificadorTelegram;

    private UsuarioServicio servicio;

    @BeforeEach
    void setUp() {
        servicio = new UsuarioServicio(repositorio, notificadorCorreo, notificadorTelegram);
    }

    /** Usuario con todos los datos válidos (mayor de edad, correo y documento bien escritos). */
    private static Usuario usuarioValido() {
        Usuario usuario = new Usuario();
        usuario.setIdUsuario(10);
        usuario.setNombreCompleto("Ana Rodriguez");
        usuario.setTipoDocumento("CC");
        usuario.setNumeroDocumento("1012345678");
        usuario.setFechaNacimiento(LocalDate.now().minusYears(25).toString());
        usuario.setCorreo(CORREO);
        usuario.setPassword(PASSWORD);
        return usuario;
    }

    // ------------------------------------------------------------ validarRegistro

    @Test
    @DisplayName("validarRegistro: datos válidos y no repetidos no tienen errores")
    void validarRegistro_datosValidos_sinErrores() {
        Usuario usuario = usuarioValido();
        when(repositorio.existeDocumento("1012345678")).thenReturn(false);
        when(repositorio.existeCorreo(CORREO)).thenReturn(false);

        assertTrue(servicio.validarRegistro(usuario, PASSWORD).isEmpty());
    }

    @Test
    @DisplayName("validarRegistro: documento ya registrado da error en numeroDocumento")
    void validarRegistro_documentoRepetido_error() {
        Usuario usuario = usuarioValido();
        when(repositorio.existeDocumento("1012345678")).thenReturn(true);
        when(repositorio.existeCorreo(CORREO)).thenReturn(false);

        Map<String, String> errores = servicio.validarRegistro(usuario, PASSWORD);

        assertEquals("Ya existe una cuenta con este documento.", errores.get("numeroDocumento"));
    }

    @Test
    @DisplayName("validarRegistro: correo ya registrado da error en correo")
    void validarRegistro_correoRepetido_error() {
        Usuario usuario = usuarioValido();
        when(repositorio.existeDocumento("1012345678")).thenReturn(false);
        when(repositorio.existeCorreo(CORREO)).thenReturn(true);

        Map<String, String> errores = servicio.validarRegistro(usuario, PASSWORD);

        assertEquals("Ya existe una cuenta con este correo.", errores.get("correo"));
    }

    @Test
    @DisplayName("validarRegistro: si el formato ya es inválido no consulta la base de datos")
    void validarRegistro_formatoInvalido_noConsultaRepositorio() {
        Usuario usuario = usuarioValido();
        usuario.setNumeroDocumento("12");
        usuario.setCorreo("no-es-correo");

        Map<String, String> errores = servicio.validarRegistro(usuario, PASSWORD);

        assertTrue(errores.containsKey("numeroDocumento"));
        assertTrue(errores.containsKey("correo"));
        verifyNoInteractions(repositorio);
    }

    // ------------------------------------------------------------ registrar

    @Test
    @DisplayName("registrar: normaliza el correo, guarda y envía la bienvenida por correo")
    void registrar_guardaYEnviaBienvenida() {
        Usuario usuario = usuarioValido();
        usuario.setCorreo("  ANA@WiseTrip.com ");

        servicio.registrar(usuario);

        assertEquals(CORREO, usuario.getCorreo());
        verify(repositorio).registrar(usuario);
        verify(notificadorCorreo).enviar(eq(CORREO), any(Notificacion.class));
        verifyNoInteractions(notificadorTelegram);
    }

    @Test
    @DisplayName("registrar: si el correo de bienvenida falla, el registro no se rompe")
    void registrar_correoFalla_noRompeElRegistro() {
        Usuario usuario = usuarioValido();
        doThrow(new IllegalStateException("SMTP caído"))
                .when(notificadorCorreo).enviar(anyString(), any(Notificacion.class));

        assertDoesNotThrow(() -> servicio.registrar(usuario));
        verify(repositorio).registrar(usuario);
    }

    // ------------------------------------------------------------ iniciarSesion

    @Test
    @DisplayName("iniciarSesion: credenciales correctas devuelven el usuario")
    void iniciarSesion_credencialesCorrectas() {
        Usuario usuario = usuarioValido();
        when(repositorio.buscarPorCorreo(CORREO)).thenReturn(usuario);

        assertSame(usuario, servicio.iniciarSesion("  " + CORREO + " ", PASSWORD));
    }

    @Test
    @DisplayName("iniciarSesion: contraseña incorrecta devuelve null")
    void iniciarSesion_passwordIncorrecta_null() {
        when(repositorio.buscarPorCorreo(CORREO)).thenReturn(usuarioValido());

        assertNull(servicio.iniciarSesion(CORREO, "otra-clave"));
    }

    @Test
    @DisplayName("iniciarSesion: correo no registrado devuelve null")
    void iniciarSesion_correoNoRegistrado_null() {
        when(repositorio.buscarPorCorreo("nadie@wisetrip.com")).thenReturn(null);

        assertNull(servicio.iniciarSesion("nadie@wisetrip.com", PASSWORD));
    }

    @Test
    @DisplayName("borde: iniciarSesion con datos null devuelve null sin consultar la base de datos")
    void iniciarSesion_datosNull_noConsulta() {
        assertNull(servicio.iniciarSesion(null, PASSWORD));
        assertNull(servicio.iniciarSesion(CORREO, null));
        verifyNoInteractions(repositorio);
    }

    @Test
    @DisplayName("iniciarSesion: con Telegram vinculado avisa el inicio por Telegram")
    void iniciarSesion_conTelegram_avisa() {
        Usuario usuario = usuarioValido();
        usuario.setChatId("987654");
        when(repositorio.buscarPorCorreo(CORREO)).thenReturn(usuario);

        servicio.iniciarSesion(CORREO, PASSWORD);

        verify(notificadorTelegram).enviar(eq("987654"), any(Notificacion.class));
    }

    @Test
    @DisplayName("iniciarSesion: sin Telegram vinculado no envía nada")
    void iniciarSesion_sinTelegram_noAvisa() {
        when(repositorio.buscarPorCorreo(CORREO)).thenReturn(usuarioValido());

        servicio.iniciarSesion(CORREO, PASSWORD);

        verify(notificadorTelegram, never()).enviar(anyString(), any(Notificacion.class));
    }

    @Test
    @DisplayName("iniciarSesion: si Telegram falla, el inicio de sesión sigue funcionando")
    void iniciarSesion_telegramFalla_igualIniciaSesion() {
        Usuario usuario = usuarioValido();
        usuario.setChatId("987654");
        when(repositorio.buscarPorCorreo(CORREO)).thenReturn(usuario);
        doThrow(new IllegalStateException("Telegram caído"))
                .when(notificadorTelegram).enviar(anyString(), any(Notificacion.class));

        assertSame(usuario, servicio.iniciarSesion(CORREO, PASSWORD));
    }

    // ------------------------------------------------------------ vincularTelegram

    @Test
    @DisplayName("vincularTelegram: limpia el chat_id y lo guarda en el repositorio")
    void vincularTelegram_guardaChatIdLimpio() {
        Usuario usuario = usuarioValido();

        servicio.vincularTelegram(usuario, "  555 ");

        assertTrue(usuario.tieneTelegramVinculado());
        assertEquals("555", usuario.getChatId());
        verify(repositorio).vincularTelegram(10, "555");
    }

    @Test
    @DisplayName("borde: vincular con null deja el usuario sin Telegram")
    void vincularTelegram_null_quedaSinTelegram() {
        Usuario usuario = usuarioValido();

        servicio.vincularTelegram(usuario, null);

        assertFalse(usuario.tieneTelegramVinculado());
        verify(repositorio).vincularTelegram(10, null);
    }
}

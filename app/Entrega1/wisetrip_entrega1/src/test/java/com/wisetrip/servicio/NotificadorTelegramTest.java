package com.wisetrip.servicio;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.test.util.ReflectionTestUtils;

import com.wisetrip.modelo.Notificacion;

/**
 * Pruebas unitarias de NotificadorTelegram.
 *
 * El HttpClient se reemplaza por uno simulado: no se envían mensajes reales
 * a Telegram. Se revisa que una falla nunca rompa el flujo del usuario.
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@DisplayName("NotificadorTelegram")
class NotificadorTelegramTest {

    private final Notificacion mensaje = new Notificacion("Inicio de sesión", "Hola Ana, iniciaste sesión.");

    @Mock
    private HttpClient httpClient;
    @Mock
    private HttpResponse<String> respuesta;

    private NotificadorTelegram notificador;

    @BeforeEach
    void setUp() {
        notificador = new NotificadorTelegram();
        ReflectionTestUtils.setField(notificador, "httpClient", httpClient);
        ReflectionTestUtils.setField(notificador, "botToken", "token-de-prueba");
    }

    @AfterEach
    void limpiarInterrupcion() {
        Thread.interrupted();
    }

    @Test
    @DisplayName("envía el mensaje al chat del usuario con el texto codificado")
    void enviar_ok_armaLaUrlDelChat() throws Exception {
        when(respuesta.statusCode()).thenReturn(200);
        doReturn(respuesta).when(httpClient).send(any(HttpRequest.class), any());

        notificador.enviar("555", mensaje);

        ArgumentCaptor<HttpRequest> captor = ArgumentCaptor.forClass(HttpRequest.class);
        verify(httpClient).send(captor.capture(), any());
        String url = captor.getValue().uri().toString();
        assertTrue(url.contains("/bottoken-de-prueba/sendMessage"));
        assertTrue(url.contains("chat_id=555"));
        assertTrue(url.contains("text=Hola+Ana"));
    }

    @Test
    @DisplayName("borde: sin chat_id no intenta enviar nada")
    void enviar_sinChatId_noEnvia() {
        notificador.enviar("  ", mensaje);
        notificador.enviar(null, mensaje);

        verifyNoInteractions(httpClient);
    }

    @Test
    @DisplayName("si Telegram responde con error solo lo registra, sin lanzar excepción")
    void enviar_respuestaConError_noLanza() throws Exception {
        when(respuesta.statusCode()).thenReturn(400);
        when(respuesta.body()).thenReturn("{\"ok\":false}");
        doReturn(respuesta).when(httpClient).send(any(HttpRequest.class), any());

        assertDoesNotThrow(() -> notificador.enviar("555", mensaje));
    }

    @Test
    @DisplayName("si falla la red no lanza excepción (el login sigue)")
    void enviar_errorDeRed_noLanza() throws Exception {
        doThrow(new IOException("sin red")).when(httpClient).send(any(HttpRequest.class), any());

        assertDoesNotThrow(() -> notificador.enviar("555", mensaje));
    }

    @Test
    @DisplayName("si el envío se interrumpe conserva la interrupción del hilo")
    void enviar_interrumpido_restauraLaInterrupcion() throws Exception {
        doThrow(new InterruptedException()).when(httpClient).send(any(HttpRequest.class), any());

        notificador.enviar("555", mensaje);

        assertTrue(Thread.currentThread().isInterrupted());
    }
}

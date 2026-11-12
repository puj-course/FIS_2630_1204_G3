package com.wisetrip.servicio;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
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
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.Flow;

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
 * Pruebas unitarias de NotificadorSMS (Twilio).
 *
 * El HttpClient se reemplaza por uno simulado: no se envían SMS reales
 * ni se gastan mensajes de la cuenta. Se revisa que una falla nunca
 * rompa el registro ni el inicio de sesión.
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@DisplayName("NotificadorSMS")
class NotificadorSMSTest {

    private static final String DESTINO = "+573001234567";

    private final Notificacion mensaje = new Notificacion("Inicio de sesión", "Hola Ana, iniciaste sesión.");

    @Mock
    private HttpClient httpClient;
    @Mock
    private HttpResponse<String> respuesta;

    private NotificadorSMS notificador;

    @BeforeEach
    void setUp() {
        notificador = new NotificadorSMS();
        ReflectionTestUtils.setField(notificador, "httpClient", httpClient);
        ReflectionTestUtils.setField(notificador, "accountSid", "ACprueba");
        ReflectionTestUtils.setField(notificador, "authToken", "token-de-prueba");
        ReflectionTestUtils.setField(notificador, "numeroOrigen", "+15550001111");
        ReflectionTestUtils.setField(notificador, "plantilla", "");
    }

    @AfterEach
    void limpiarInterrupcion() {
        Thread.interrupted();
    }

    /** Lee el cuerpo (To, From, Body) de la petición sin enviarla. */
    private static String leerCuerpo(HttpRequest peticion) {
        StringBuilder texto = new StringBuilder();
        peticion.bodyPublisher().orElseThrow().subscribe(new Flow.Subscriber<ByteBuffer>() {
            @Override
            public void onSubscribe(Flow.Subscription suscripcion) {
                suscripcion.request(Long.MAX_VALUE);
            }

            @Override
            public void onNext(ByteBuffer parte) {
                texto.append(StandardCharsets.UTF_8.decode(parte));
            }

            @Override
            public void onError(Throwable error) {
                throw new IllegalStateException(error);
            }

            @Override
            public void onComplete() {
                // Nada que hacer: el cuerpo ya quedó leído.
            }
        });
        return texto.toString();
    }

    private HttpRequest enviarYCapturar() throws Exception {
        when(respuesta.statusCode()).thenReturn(201);
        when(respuesta.body()).thenReturn("{\"sid\":\"SM123\",\"status\":\"queued\"}");
        doReturn(respuesta).when(httpClient).send(any(HttpRequest.class), any());

        notificador.enviar(DESTINO, mensaje);

        ArgumentCaptor<HttpRequest> captor = ArgumentCaptor.forClass(HttpRequest.class);
        verify(httpClient).send(captor.capture(), any());
        return captor.getValue();
    }

    @Test
    @DisplayName("envía el SMS a la API de Twilio con la cuenta, la autenticación y los datos")
    void enviar_ok_armaLaPeticionDeTwilio() throws Exception {
        HttpRequest peticion = enviarYCapturar();

        assertEquals("POST", peticion.method());
        assertTrue(peticion.uri().toString().endsWith("/Accounts/ACprueba/Messages.json"));
        assertTrue(peticion.headers().firstValue("Authorization").orElse("").startsWith("Basic "));

        String cuerpo = leerCuerpo(peticion);
        assertTrue(cuerpo.contains("To=%2B573001234567"));
        assertTrue(cuerpo.contains("From=%2B15550001111"));
        assertTrue(cuerpo.contains("Body=Hola+Ana"));
    }

    @Test
    @DisplayName("con plantilla configurada (cuenta trial) manda la plantilla en vez del texto")
    void enviar_conPlantilla_mandaLaPlantilla() throws Exception {
        ReflectionTestUtils.setField(notificador, "plantilla", "sms_account_alerts");

        String cuerpo = leerCuerpo(enviarYCapturar());

        assertTrue(cuerpo.contains("Body=sms_account_alerts"));
        assertFalse(cuerpo.contains("Hola"));
    }

    @Test
    @DisplayName("borde: sin celular no intenta enviar nada")
    void enviar_sinCelular_noEnvia() {
        notificador.enviar("  ", mensaje);
        notificador.enviar(null, mensaje);

        verifyNoInteractions(httpClient);
    }

    @Test
    @DisplayName("borde: sin credenciales de Twilio no intenta enviar nada")
    void enviar_sinCredenciales_noEnvia() {
        ReflectionTestUtils.setField(notificador, "accountSid", "");

        notificador.enviar(DESTINO, mensaje);

        verifyNoInteractions(httpClient);
    }

    @Test
    @DisplayName("si Twilio responde con error solo lo registra, sin lanzar excepción")
    void enviar_respuestaConError_noLanza() throws Exception {
        when(respuesta.statusCode()).thenReturn(400);
        when(respuesta.body()).thenReturn("{\"code\":21211,\"message\":\"Invalid 'To' Phone Number\"}");
        doReturn(respuesta).when(httpClient).send(any(HttpRequest.class), any());

        assertDoesNotThrow(() -> notificador.enviar(DESTINO, mensaje));
    }

    @Test
    @DisplayName("si falla la red no lanza excepción (el registro sigue)")
    void enviar_errorDeRed_noLanza() throws Exception {
        doThrow(new IOException("sin red")).when(httpClient).send(any(HttpRequest.class), any());

        assertDoesNotThrow(() -> notificador.enviar(DESTINO, mensaje));
    }

    @Test
    @DisplayName("si el envío se interrumpe conserva la interrupción del hilo")
    void enviar_interrumpido_restauraLaInterrupcion() throws Exception {
        doThrow(new InterruptedException()).when(httpClient).send(any(HttpRequest.class), any());

        notificador.enviar(DESTINO, mensaje);

        assertTrue(Thread.currentThread().isInterrupted());
    }
}
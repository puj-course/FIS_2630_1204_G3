package com.wisetrip.servicio;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
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
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.test.util.ReflectionTestUtils;

/**
 * Pruebas unitarias de ServicioGeoapify.
 *
 * El HttpClient se reemplaza por uno simulado con Mockito: las pruebas no
 * salen a internet ni gastan créditos de la API (F.I.R.S.T: Independent).
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@DisplayName("ServicioGeoapify")
class ServicioGeoapifyTest {

    private static final String UN_LUGAR =
            "{\"features\":[{\"geometry\":{\"coordinates\":[-75.5,10.4]}}]}";

    @Mock
    private HttpClient clienteHttp;
    @Mock
    private HttpResponse<String> respuesta;

    private ServicioGeoapify servicio;

    @BeforeEach
    void setUp() {
        servicio = new ServicioGeoapify();
        ReflectionTestUtils.setField(servicio, "clienteHttp", clienteHttp);
        ReflectionTestUtils.setField(servicio, "claveApi", "clave-de-prueba");
    }

    @AfterEach
    void limpiarInterrupcion() {
        // Si una prueba dejó el hilo marcado como interrumpido, se limpia.
        Thread.interrupted();
    }

    private void responder(int estado, String cuerpo) throws Exception {
        when(respuesta.statusCode()).thenReturn(estado);
        when(respuesta.body()).thenReturn(cuerpo);
        doReturn(respuesta).when(clienteHttp).send(any(HttpRequest.class), any());
    }

    // ------------------------------------------------------------ coordenadas

    @Test
    @DisplayName("coordenadas devuelve {latitud, longitud} (Geoapify las manda al revés)")
    void coordenadas_ok_latitudPrimero() throws Exception {
        responder(200, UN_LUGAR);

        assertArrayEquals(new double[] {10.4, -75.5}, servicio.coordenadas("Cartagena", "Colombia"));
    }

    @Test
    @DisplayName("borde: ciudad no encontrada devuelve arreglo vacío, no null")
    void coordenadas_sinResultados_arregloVacio() throws Exception {
        responder(200, "{\"features\":[]}");

        assertEquals(0, servicio.coordenadas("Inventada", "Ninguno").length);
    }

    @Test
    @DisplayName("si falla la red devuelve arreglo vacío")
    void coordenadas_errorDeRed_arregloVacio() throws Exception {
        doThrow(new IOException("sin red")).when(clienteHttp).send(any(HttpRequest.class), any());

        assertEquals(0, servicio.coordenadas("Cartagena", "Colombia").length);
    }

    @Test
    @DisplayName("si la consulta se interrumpe devuelve vacío y conserva la interrupción del hilo")
    void coordenadas_interrumpida_restauraLaInterrupcion() throws Exception {
        doThrow(new InterruptedException()).when(clienteHttp).send(any(HttpRequest.class), any());

        double[] resultado = servicio.coordenadas("Cartagena", "Colombia");

        assertEquals(0, resultado.length);
        assertTrue(Thread.currentThread().isInterrupted());
    }

    // ------------------------------------------------------------ contarLugares

    @Test
    @DisplayName("contarLugares cuenta los lugares que devuelve Geoapify")
    void contarLugares_ok_cuenta() throws Exception {
        responder(200, "{\"features\":[{},{},{}]}");

        assertEquals(3, servicio.contarLugares(10.4, -75.5, "beach", 5000, 20));
    }

    @Test
    @DisplayName("borde: sin API key no consulta y devuelve null")
    void contarLugares_sinClave_null() {
        ReflectionTestUtils.setField(servicio, "claveApi", " ");

        assertNull(servicio.contarLugares(10.4, -75.5, "beach", 5000, 20));
        verifyNoInteractions(clienteHttp);
    }

    @Test
    @DisplayName("si Geoapify responde con error HTTP devuelve null")
    void contarLugares_errorHttp_null() throws Exception {
        responder(500, "error");

        assertNull(servicio.contarLugares(10.4, -75.5, "beach", 5000, 20));
    }

    @Test
    @DisplayName("si falla la red devuelve null")
    void contarLugares_errorDeRed_null() throws Exception {
        doThrow(new IOException("sin red")).when(clienteHttp).send(any(HttpRequest.class), any());

        assertNull(servicio.contarLugares(10.4, -75.5, "beach", 5000, 20));
    }

    @Test
    @DisplayName("si la consulta se interrumpe devuelve null y conserva la interrupción del hilo")
    void contarLugares_interrumpida_restauraLaInterrupcion() throws Exception {
        doThrow(new InterruptedException()).when(clienteHttp).send(any(HttpRequest.class), any());

        assertNull(servicio.contarLugares(10.4, -75.5, "beach", 5000, 20));
        assertTrue(Thread.currentThread().isInterrupted());
    }
}

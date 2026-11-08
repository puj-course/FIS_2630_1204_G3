package com.wisetrip.servicio;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.wisetrip.negocio.CacheRepositorio;

/**
 * Pruebas unitarias de ProxyLugares (patrón Proxy).
 *
 * El servicio real (Geoapify) y el caché en base de datos se simulan con
 * Mockito: así las pruebas no gastan créditos de la API ni necesitan
 * internet ni base de datos (F.I.R.S.T: Fast, Independent, Repeatable).
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("ProxyLugares")
class ProxyLugaresTest {

    private static final double LAT = 4.711;
    private static final double LON = -74.0721;
    private static final String CATEGORIAS = "catering.restaurant";
    private static final int RADIO = 5000;
    private static final int LIMITE = 20;

    @Mock
    private ServicioGeoapify servicioReal;

    @Mock
    private CacheRepositorio cache;

    private ProxyLugares proxy;

    @BeforeEach
    void setUp() {
        proxy = new ProxyLugares(servicioReal, cache);
    }

    @Test
    @DisplayName("si el conteo está en caché lo devuelve y NO llama a Geoapify")
    void enCache_noLlamaAlServicioReal() {
        when(cache.buscar(LAT, LON, CATEGORIAS, RADIO, LIMITE)).thenReturn(Optional.of(12));

        Integer cantidad = proxy.contarLugares(LAT, LON, CATEGORIAS, RADIO, LIMITE);

        assertEquals(12, cantidad);
        verifyNoInteractions(servicioReal);
        verify(cache, never()).guardar(anyDouble(), anyDouble(), anyString(), anyInt(), anyInt(), anyInt());
    }

    @Test
    @DisplayName("borde: un cero guardado en caché también se reutiliza")
    void ceroEnCache_seReutiliza() {
        when(cache.buscar(LAT, LON, CATEGORIAS, RADIO, LIMITE)).thenReturn(Optional.of(0));

        Integer cantidad = proxy.contarLugares(LAT, LON, CATEGORIAS, RADIO, LIMITE);

        assertEquals(0, cantidad);
        verifyNoInteractions(servicioReal);
    }

    @Test
    @DisplayName("si no está en caché consulta a Geoapify y guarda el resultado")
    void noEnCache_consultaYGuarda() {
        when(cache.buscar(LAT, LON, CATEGORIAS, RADIO, LIMITE)).thenReturn(Optional.empty());
        when(servicioReal.contarLugares(LAT, LON, CATEGORIAS, RADIO, LIMITE)).thenReturn(7);

        Integer cantidad = proxy.contarLugares(LAT, LON, CATEGORIAS, RADIO, LIMITE);

        assertEquals(7, cantidad);
        verify(cache).guardar(LAT, LON, CATEGORIAS, RADIO, LIMITE, 7);
    }

    @Test
    @DisplayName("borde: si Geoapify responde cero lugares, el cero sí se guarda")
    void geoapifyRespondeCero_seGuarda() {
        when(cache.buscar(LAT, LON, CATEGORIAS, RADIO, LIMITE)).thenReturn(Optional.empty());
        when(servicioReal.contarLugares(LAT, LON, CATEGORIAS, RADIO, LIMITE)).thenReturn(0);

        Integer cantidad = proxy.contarLugares(LAT, LON, CATEGORIAS, RADIO, LIMITE);

        assertEquals(0, cantidad);
        verify(cache).guardar(LAT, LON, CATEGORIAS, RADIO, LIMITE, 0);
    }

    @Test
    @DisplayName("si Geoapify falla (null) devuelve null y NO guarda en caché")
    void geoapifyFalla_noGuarda() {
        when(cache.buscar(LAT, LON, CATEGORIAS, RADIO, LIMITE)).thenReturn(Optional.empty());
        when(servicioReal.contarLugares(LAT, LON, CATEGORIAS, RADIO, LIMITE)).thenReturn(null);

        Integer cantidad = proxy.contarLugares(LAT, LON, CATEGORIAS, RADIO, LIMITE);

        assertNull(cantidad);
        verify(cache, never()).guardar(anyDouble(), anyDouble(), anyString(), anyInt(), anyInt(), anyInt());
    }

    @Test
    @DisplayName("coordenadas se delega directo al servicio real, sin caché")
    void coordenadas_seDelegaAlServicioReal() {
        double[] bogota = {LAT, LON};
        when(servicioReal.coordenadas("Bogota", "Colombia")).thenReturn(bogota);

        double[] resultado = proxy.coordenadas("Bogota", "Colombia");

        assertArrayEquals(bogota, resultado);
        verifyNoInteractions(cache);
    }

    @Test
    @DisplayName("borde: ciudad no encontrada devuelve arreglo vacío, no null")
    void coordenadas_ciudadNoEncontrada_arregloVacio() {
        when(servicioReal.coordenadas("Ciudad Inventada", "Ningun Pais")).thenReturn(new double[0]);

        double[] resultado = proxy.coordenadas("Ciudad Inventada", "Ningun Pais");

        assertEquals(0, resultado.length);
    }
}

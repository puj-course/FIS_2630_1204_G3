package com.wisetrip.servicio;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.wisetrip.modelo.Ubicacion;

/**
 * Pruebas unitarias de ViajeServicio: lista de países y ciudades de origen
 * y validación de la ubicación del viajero.
 */
@DisplayName("ViajeServicio")
class ViajeServicioTest {

    private ViajeServicio servicio;

    @BeforeEach
    void setUp() {
        servicio = new ViajeServicio();
    }

    // ------------------------------------------------------------ listas

    @Test
    @DisplayName("listarPaises incluye países de Latinoamérica como Colombia y México")
    void listarPaises_incluyeLatinoamerica() {
        assertTrue(servicio.listarPaises().contains("Colombia"));
        assertTrue(servicio.listarPaises().contains("México"));
    }

    @Test
    @DisplayName("listarCiudades de Colombia incluye Bogotá y Medellín, sin repetir")
    void listarCiudades_colombia_sinRepetidos() {
        var ciudades = servicio.listarCiudades("Colombia");

        assertTrue(ciudades.contains("Bogotá"));
        assertTrue(ciudades.contains("Medellín"));
        assertEquals(ciudades.size(), ciudades.stream().distinct().count());
    }

    @Test
    @DisplayName("borde: país inexistente o null devuelve una lista vacía")
    void listarCiudades_paisInexistente_vacia() {
        assertTrue(servicio.listarCiudades("Atlantida").isEmpty());
        assertTrue(servicio.listarCiudades(null).isEmpty());
    }

    // ------------------------------------------------------------ validarUbicacion

    @Test
    @DisplayName("ubicación válida no tiene errores")
    void validarUbicacion_valida_sinErrores() {
        Ubicacion ubicacion = new Ubicacion("Colombia", "Bogotá", "Barrio Chapinero");

        assertTrue(servicio.validarUbicacion(ubicacion).isEmpty());
    }

    @Test
    @DisplayName("sin país ni ciudad pide seleccionar ambos")
    void validarUbicacion_vacia_dosErrores() {
        Map<String, String> errores = servicio.validarUbicacion(new Ubicacion("", " ", null));

        assertEquals("Selecciona tu pais de origen.", errores.get("pais"));
        assertEquals("Selecciona tu ciudad de origen.", errores.get("ciudad"));
    }

    @Test
    @DisplayName("país que no está en la lista se rechaza")
    void validarUbicacion_paisInvalido() {
        Map<String, String> errores = servicio.validarUbicacion(new Ubicacion("Atlantida", "Bogotá", null));

        assertEquals("Selecciona un pais valido de la lista.", errores.get("pais"));
        assertFalse(errores.containsKey("ciudad"));
    }

    @Test
    @DisplayName("ciudad que no pertenece al país se rechaza")
    void validarUbicacion_ciudadDeOtroPais() {
        Map<String, String> errores = servicio.validarUbicacion(new Ubicacion("Colombia", "Lima", null));

        assertEquals("Esa ciudad no pertenece al pais seleccionado.", errores.get("ciudad"));
    }

    @Test
    @DisplayName("borde: detalle de 60 caracteres es válido")
    void validarUbicacion_detalleDeSesenta_valido() {
        Ubicacion ubicacion = new Ubicacion("Colombia", "Bogotá", "x".repeat(60));

        assertFalse(servicio.validarUbicacion(ubicacion).containsKey("detalle"));
    }

    @Test
    @DisplayName("borde: detalle de 61 caracteres se rechaza")
    void validarUbicacion_detalleDeSesentaYUno_error() {
        Ubicacion ubicacion = new Ubicacion("Colombia", "Bogotá", "x".repeat(61));

        assertEquals("El detalle no puede superar los 60 caracteres.",
                servicio.validarUbicacion(ubicacion).get("detalle"));
    }
}

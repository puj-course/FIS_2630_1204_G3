package com.wisetrip.servicio;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.wisetrip.modelo.Ubicacion;

@DisplayName("ViajeServicio")
class ViajeServicioTest {

    private ViajeServicio servicio;

    @BeforeEach
    void setUp() {
        servicio = new ViajeServicio();
    }

    @Test
    @DisplayName("listarPaises incluye los países de Latinoamérica")
    void listarPaises_incluyeLosPaises() {
        List<String> paises = servicio.listarPaises();

        assertFalse(paises.isEmpty());
        assertTrue(paises.contains("Colombia"));
        assertTrue(paises.contains("Argentina"));
        assertTrue(paises.contains("México"));
    }

    @Test
    @DisplayName("listarCiudades devuelve las ciudades de un país")
    void listarCiudades_paisValido() {
        List<String> ciudades = servicio.listarCiudades("Colombia");

        assertTrue(ciudades.contains("Bogotá"));
        assertTrue(ciudades.contains("Medellín"));
    }

    @Test
    @DisplayName("listarCiudades devuelve una lista vacía si el país es null o no existe")
    void listarCiudades_paisNullODesconocido_vacia() {
        assertTrue(servicio.listarCiudades(null).isEmpty());
        assertTrue(servicio.listarCiudades("Atlantis").isEmpty());
    }

    @Test
    @DisplayName("validarUbicacion: país y ciudad válidos no generan errores")
    void validarUbicacion_valida_sinErrores() {
        Map<String, String> errores = servicio.validarUbicacion(new Ubicacion("Colombia", "Bogotá", "Aeropuerto"));

        assertTrue(errores.isEmpty());
    }

    @Test
    @DisplayName("validarUbicacion: sin país pide seleccionar el país")
    void validarUbicacion_sinPais_error() {
        Map<String, String> errores = servicio.validarUbicacion(new Ubicacion("", "Bogotá", null));

        assertEquals("Selecciona tu pais de origen.", errores.get("pais"));
    }

    @Test
    @DisplayName("validarUbicacion: un país que no está en la lista es inválido")
    void validarUbicacion_paisDesconocido_error() {
        Map<String, String> errores = servicio.validarUbicacion(new Ubicacion("Atlantis", "Bogotá", null));

        assertEquals("Selecciona un pais valido de la lista.", errores.get("pais"));
    }

    @Test
    @DisplayName("validarUbicacion: sin ciudad pide seleccionar la ciudad")
    void validarUbicacion_sinCiudad_error() {
        Map<String, String> errores = servicio.validarUbicacion(new Ubicacion("Colombia", " ", null));

        assertEquals("Selecciona tu ciudad de origen.", errores.get("ciudad"));
    }

    @Test
    @DisplayName("validarUbicacion: una ciudad de otro país es inválida")
    void validarUbicacion_ciudadDeOtroPais_error() {
        Map<String, String> errores = servicio.validarUbicacion(new Ubicacion("Colombia", "Lima", null));

        assertEquals("Esa ciudad no pertenece al pais seleccionado.", errores.get("ciudad"));
    }

    @Test
    @DisplayName("validarUbicacion: borde, un detalle de 60 caracteres es válido")
    void validarUbicacion_detalleDe60_valido() {
        Ubicacion ubicacion = new Ubicacion("Colombia", "Bogotá", "a".repeat(60));

        assertFalse(servicio.validarUbicacion(ubicacion).containsKey("detalle"));
    }

    @Test
    @DisplayName("validarUbicacion: borde, un detalle de 61 caracteres es inválido")
    void validarUbicacion_detalleDe61_error() {
        Ubicacion ubicacion = new Ubicacion("Colombia", "Bogotá", "a".repeat(61));

        assertEquals("El detalle no puede superar los 60 caracteres.",
                servicio.validarUbicacion(ubicacion).get("detalle"));
    }
}

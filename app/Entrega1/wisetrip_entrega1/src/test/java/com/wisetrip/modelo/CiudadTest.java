package com.wisetrip.modelo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Ciudad")
class CiudadTest {

    private Ciudad ciudad;

    @BeforeEach
    void setUp() {
        ciudad = new Ciudad();
    }

    @Test
    @DisplayName("los datos básicos se guardan y se leen")
    void gettersYSetters() {
        ciudad.setId(7);
        ciudad.setNombre("Cartagena");
        ciudad.setPais("Colombia");
        ciudad.setCostoPromedio(85.5);
        ciudad.setLatitud(10.39);
        ciudad.setLongitud(-75.48);

        assertEquals(7, ciudad.getId());
        assertEquals("Cartagena", ciudad.getNombre());
        assertEquals("Colombia", ciudad.getPais());
        assertEquals(85.5, ciudad.getCostoPromedio(), 0.0001);
        assertEquals(10.39, ciudad.getLatitud(), 0.0001);
        assertEquals(-75.48, ciudad.getLongitud(), 0.0001);
    }

    @Test
    @DisplayName("una ciudad nueva empieza sin atributos")
    void ciudadNueva_sinAtributos() {
        assertTrue(ciudad.getAtributos().isEmpty());
    }

    @Test
    @DisplayName("setAtributos con null deja un mapa vacío")
    void setAtributos_null_mapaVacio() {
        ciudad.setAtributos(null);

        assertTrue(ciudad.getAtributos().isEmpty());
    }

    @Test
    @DisplayName("estadoAtributo distingue cumple, no cumple y no sabemos")
    void estadoAtributo_tresEstados() {
        ciudad.setAtributos(Map.of("playa", true, "nieve", false));

        assertEquals(EstadoAtributo.CUMPLE, ciudad.estadoAtributo("playa"));
        assertEquals(EstadoAtributo.NO_CUMPLE, ciudad.estadoAtributo("nieve"));
        assertEquals(EstadoAtributo.NO_SABEMOS, ciudad.estadoAtributo("golf"));
    }

    @Test
    @DisplayName("tiene es true solo cuando el atributo está en true")
    void tiene_soloConTrue() {
        ciudad.setAtributos(Map.of("playa", true, "nieve", false));

        assertTrue(ciudad.tiene("playa"));
        assertFalse(ciudad.tiene("nieve"));
        assertFalse(ciudad.tiene("golf"));
    }

    @Test
    @DisplayName("getTipo: playa tiene prioridad sobre montaña y naturaleza")
    void getTipo_playaPrimero() {
        ciudad.setAtributos(Map.of("playa", true, "montana", true, "naturaleza", true));

        assertEquals("playa", ciudad.getTipo());
    }

    @Test
    @DisplayName("getTipo: nieve o montaña dan tipo montaña")
    void getTipo_montana() {
        ciudad.setAtributos(Map.of("nieve", true));
        assertEquals("montana", ciudad.getTipo());

        ciudad.setAtributos(Map.of("montana", true));
        assertEquals("montana", ciudad.getTipo());
    }

    @Test
    @DisplayName("getTipo: naturaleza, y ciudad si no hay ninguno de los anteriores")
    void getTipo_naturalezaYCiudad() {
        ciudad.setAtributos(Map.of("naturaleza", true));
        assertEquals("naturaleza", ciudad.getTipo());

        ciudad.setAtributos(Map.of("urbano", true));
        assertEquals("ciudad", ciudad.getTipo());
    }

    @Test
    @DisplayName("getDescripcion une nombre y país")
    void getDescripcion_nombreYPais() {
        ciudad.setNombre("Cusco");
        ciudad.setPais("Perú");

        assertEquals("Cusco, Perú", ciudad.getDescripcion());
    }
}

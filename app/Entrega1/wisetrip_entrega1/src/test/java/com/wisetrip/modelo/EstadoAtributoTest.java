package com.wisetrip.modelo;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("EstadoAtributo")
class EstadoAtributoTest {

    @Test
    @DisplayName("desdeConteo: sin conteo no sabemos")
    void desdeConteo_null_noSabemos() {
        assertEquals(EstadoAtributo.NO_SABEMOS, EstadoAtributo.desdeConteo(null, 3));
    }

    @Test
    @DisplayName("desdeConteo: borde, un conteo igual al umbral cumple")
    void desdeConteo_igualAlUmbral_cumple() {
        assertEquals(EstadoAtributo.CUMPLE, EstadoAtributo.desdeConteo(3, 3));
    }

    @Test
    @DisplayName("desdeConteo: un conteo mayor al umbral cumple")
    void desdeConteo_mayorAlUmbral_cumple() {
        assertEquals(EstadoAtributo.CUMPLE, EstadoAtributo.desdeConteo(10, 3));
    }

    @Test
    @DisplayName("desdeConteo: borde, un conteo bajo el umbral no cumple")
    void desdeConteo_bajoElUmbral_noCumple() {
        assertEquals(EstadoAtributo.NO_CUMPLE, EstadoAtributo.desdeConteo(2, 3));
        assertEquals(EstadoAtributo.NO_CUMPLE, EstadoAtributo.desdeConteo(0, 1));
    }
}

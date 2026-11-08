package com.wisetrip.modelo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("ResultadoRecomendacion")
class ResultadoRecomendacionTest {

    @Test
    @DisplayName("guarda la ciudad, los puntajes y las banderas que recibe")
    void constructor_guardaLosDatos() {
        Ciudad ciudad = new Ciudad();

        ResultadoRecomendacion resultado = new ResultadoRecomendacion(ciudad, 0.9, 0.8, 0.7, true, false);

        assertSame(ciudad, resultado.getCiudad());
        assertEquals(0.9, resultado.getPuntajeTotal(), 0.0001);
        assertEquals(0.8, resultado.getPuntajePresupuesto(), 0.0001);
        assertEquals(0.7, resultado.getPuntajePreferencias(), 0.0001);
        assertTrue(resultado.isPreferenciasPuntuables());
        assertFalse(resultado.isCoincidenciaProvisional());
    }
}

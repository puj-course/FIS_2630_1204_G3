package com.wisetrip.modelo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Pruebas unitarias de EstadoCategoriaGasto (HU#89): restante, porcentaje
 * usado (para la barra de Mi viaje) y si la categoría ya se excedió.
 */
@DisplayName("EstadoCategoriaGasto")
class EstadoCategoriaGastoTest {

    private static EstadoCategoriaGasto estado(String cupo, String gastado) {
        return new EstadoCategoriaGasto("hospedaje", new BigDecimal(cupo), new BigDecimal(gastado));
    }

    @Test
    @DisplayName("restante es el cupo menos lo gastado")
    void restante_cupoMenosGastado() {
        assertEquals(0, new BigDecimal("150").compareTo(estado("400", "250").getRestante()));
    }

    @Test
    @DisplayName("borde: si se pasa del cupo el restante es negativo")
    void restante_negativoSiSePasa() {
        assertEquals(-1, estado("100", "130").getRestante().signum());
    }

    @Test
    @DisplayName("porcentaje usado se redondea al entero más cercano")
    void porcentajeUsado_redondea() {
        assertEquals(63, estado("400", "250").getPorcentajeUsado()); // 62.5 -> 63
    }

    @Test
    @DisplayName("borde: si se pasa del cupo la barra se queda en 100")
    void porcentajeUsado_topeCien() {
        assertEquals(100, estado("100", "300").getPorcentajeUsado());
    }

    @Test
    @DisplayName("borde: cupo cero sin gastos da 0%")
    void cupoCero_sinGastos_cero() {
        assertEquals(0, estado("0", "0").getPorcentajeUsado());
    }

    @Test
    @DisplayName("borde: cupo cero con gastos da 100%")
    void cupoCero_conGastos_cien() {
        assertEquals(100, estado("0", "10").getPorcentajeUsado());
    }

    @Test
    @DisplayName("isExcedido solo es verdadero si lo gastado supera el cupo")
    void isExcedido() {
        assertTrue(estado("100", "100.01").isExcedido());
        assertFalse(estado("100", "100").isExcedido());
    }

    @Test
    @DisplayName("los getters devuelven los datos del registro (los usa la vista)")
    void getters() {
        EstadoCategoriaGasto estado = estado("400", "250");

        assertEquals("hospedaje", estado.getCategoria());
        assertEquals(new BigDecimal("400"), estado.getCupo());
        assertEquals(new BigDecimal("250"), estado.getGastado());
    }
}

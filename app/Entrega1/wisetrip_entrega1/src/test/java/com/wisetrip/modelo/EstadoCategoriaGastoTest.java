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
        // Compara que el resultado de getRestante() con un cupo de 400 y gasto de 250 sea igual a 150 
        // (utilizando .compareTo() para la precisión exacta de BigDecimal)
        assertEquals(0, new BigDecimal("150").compareTo(estado("400", "250").getRestante()));
    }

    @Test
    @DisplayName("borde: si se pasa del cupo el restante es negativo")
    void restante_negativoSiSePasa() {

        // Verifica que si el gastado (130) es mayor que el cupo (100), 
        // el signo del restante sea negativo (-1)
        assertEquals(-1, estado("100", "130").getRestante().signum());
    }

    @Test
    @DisplayName("porcentaje usado se redondea al entero más cercano")
    void porcentajeUsado_redondea() {
        // Comprueba que 250 de 400 (62.5%) se redondea al entero más cercano, dando 63
        assertEquals(63, estado("400", "250").getPorcentajeUsado()); // 62.5 -> 63
    }

    @Test
    @DisplayName("borde: si se pasa del cupo la barra se queda en 100")
    void porcentajeUsado_topeCien() {
        // Comprueba que si gasta 300 teniendo un cupo de 100, 
        // el porcentaje no pasa del tope máximo del 100%
        assertEquals(100, estado("100", "300").getPorcentajeUsado());
    }

    @Test
    @DisplayName("borde: cupo cero sin gastos da 0%")
    void cupoCero_sinGastos_cero() {
        // Comprueba que un cupo de 0 sin ningún gasto devuelva un 0% de uso
        assertEquals(0, estado("0", "0").getPorcentajeUsado());
    }

    @Test
    @DisplayName("borde: cupo cero con gastos da 100%")
    void cupoCero_conGastos_cien() {
        // Comprueba que un cupo de 0 con cualquier gasto (10) sature la barra al 100%
        assertEquals(100, estado("0", "10").getPorcentajeUsado());
    }

    @Test
    @DisplayName("isExcedido solo es verdadero si lo gastado supera el cupo")
    void isExcedido() {
        // Comprueba que isExcedido() devuelva true si el gasto excede el cupo y false en caso contrario
        assertTrue(estado("100", "100.01").isExcedido());
        assertFalse(estado("100", "100").isExcedido());
    }

    @Test
    @DisplayName("los getters devuelven los datos del registro (los usa la vista)")
    void getters() {
        // Crea una instancia de prueba con valores conocidos
        EstadoCategoriaGasto estado = estado("400", "250");

        // Valida que el getter de categoría devuelva "hospedaje"
        assertEquals("hospedaje", estado.getCategoria());
        // Valida que el getter de cupo devuelva 400
        assertEquals(new BigDecimal("400"), estado.getCupo());
        // Valida que el getter de gastado devuelva 250
        assertEquals(new BigDecimal("250"), estado.getGastado());
    }
}

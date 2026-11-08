package com.wisetrip.modelo;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Pruebas de Presupuesto")
class PresupuestoTest {

    private Presupuesto presupuesto;

    @BeforeEach
    void prepararPresupuesto() {
        presupuesto = new Presupuesto();
    }

    @Test
    @DisplayName("Un monto entero se convierte a número")
    void montoEntero() {
        presupuesto.setMonto("1500");

        double resultadoEsperado = 1500;
        double resultadoObtenido = presupuesto.getMontoNumerico();

        assertEquals(resultadoEsperado, resultadoObtenido);
    }

    @Test
    @DisplayName("Los puntos separan los miles")
    void montoConMiles() {
        presupuesto.setMonto("1.500.000");

        double resultadoEsperado = 1500000;
        double resultadoObtenido = presupuesto.getMontoNumerico();

        assertEquals(resultadoEsperado, resultadoObtenido);
    }

    @Test
    @DisplayName("La coma separa los decimales")
    void montoConDecimales() {
        presupuesto.setMonto("1.500,50");

        double resultadoEsperado = 1500.50;
        double resultadoObtenido = presupuesto.getMontoNumerico();

        assertEquals(resultadoEsperado, resultadoObtenido);
    }

    @Test
    @DisplayName("Los espacios se eliminan antes de convertir")
    void montoConEspacios() {
        presupuesto.setMonto(" 1 500 ");

        double resultadoEsperado = 1500;
        double resultadoObtenido = presupuesto.getMontoNumerico();

        assertEquals(resultadoEsperado, resultadoObtenido);
    }

    @Test
    @DisplayName("Un monto con letras devuelve menos uno")
    void montoConLetras() {
        presupuesto.setMonto("abc");

        double resultadoEsperado = -1;
        double resultadoObtenido = presupuesto.getMontoNumerico();

        assertEquals(resultadoEsperado, resultadoObtenido);
    }

    @Test
    @DisplayName("Un monto vacío devuelve menos uno")
    void montoVacio() {
        presupuesto.setMonto("");

        double resultadoEsperado = -1;
        double resultadoObtenido = presupuesto.getMontoNumerico();

        assertEquals(resultadoEsperado, resultadoObtenido);
    }

    @Test
    @DisplayName("Un monto con solo espacios devuelve menos uno")
    void montoSoloEspacios() {
        presupuesto.setMonto("   ");

        double resultadoEsperado = -1;
        double resultadoObtenido = presupuesto.getMontoNumerico();

        assertEquals(resultadoEsperado, resultadoObtenido);
    }

    @Test
    @DisplayName("Un monto nulo devuelve menos uno")
    void montoNulo() {
        presupuesto.setMonto(null);

        double resultadoEsperado = -1;
        double resultadoObtenido = presupuesto.getMontoNumerico();

        assertEquals(resultadoEsperado, resultadoObtenido);
    }

    @Test
    @DisplayName("NaN no es un monto válido")
    void montoNoNumerico() {
        presupuesto.setMonto("NaN");

        double resultadoEsperado = -1;
        double resultadoObtenido = presupuesto.getMontoNumerico();

        assertEquals(resultadoEsperado, resultadoObtenido);
    }

    @Test
    @DisplayName("Un monto infinito devuelve menos uno")
    void montoInfinito() {
        presupuesto.setMonto("Infinity");

        double resultadoEsperado = -1;
        double resultadoObtenido = presupuesto.getMontoNumerico();

        assertEquals(resultadoEsperado, resultadoObtenido);
    }
}

package com.wisetrip.modelo;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Pruebas de FechasViaje")
class FechasViajeTest {

    private FechasViaje fechas;

    @BeforeEach
    void prepararFechas() {
        // Cada prueba empieza con un viaje del 10 al 12 de noviembre.
        fechas = new FechasViaje();
        fechas.setFechaInicio("2026-11-10");
        fechas.setFechaFin("2026-11-12");
    }

    @Test
    @DisplayName("La fecha final posterior a la inicial es válida")
    void rangoValido() {
        boolean resultadoEsperado = true;
        boolean resultadoObtenido = fechas.tieneRangoValido();

        assertEquals(resultadoEsperado, resultadoObtenido);
    }

    @Test
    @DisplayName("La fecha final anterior a la inicial no es válida")
    void fechaFinalAnterior() {
        fechas.setFechaFin("2026-11-09");

        boolean resultadoEsperado = false;
        boolean resultadoObtenido = fechas.tieneRangoValido();

        assertEquals(resultadoEsperado, resultadoObtenido);
    }

    @Test
    @DisplayName("Las fechas iguales no forman un rango válido")
    void fechasIguales() {
        fechas.setFechaFin("2026-11-10");

        boolean resultadoEsperado = false;
        boolean resultadoObtenido = fechas.tieneRangoValido();

        assertEquals(resultadoEsperado, resultadoObtenido);
    }

    @Test
    @DisplayName("Una fecha escrita con texto no es válida")
    void fechaConTexto() {
        fechas.setFechaInicio("fecha incorrecta");

        boolean resultadoEsperado = false;
        boolean resultadoObtenido = fechas.tieneRangoValido();

        assertEquals(resultadoEsperado, resultadoObtenido);
    }

    @Test
    @DisplayName("Una fecha inexistente no es válida")
    void fechaInexistente() {
        fechas.setFechaInicio("2026-02-30");

        boolean resultadoEsperado = false;
        boolean resultadoObtenido = fechas.tieneRangoValido();

        assertEquals(resultadoEsperado, resultadoObtenido);
    }

    @Test
    @DisplayName("Sin fecha inicial el rango no es válido")
    void sinFechaInicial() {
        fechas.setFechaInicio(null);

        boolean resultadoEsperado = false;
        boolean resultadoObtenido = fechas.tieneRangoValido();

        assertEquals(resultadoEsperado, resultadoObtenido);
    }

    @Test
    @DisplayName("Sin fecha final el rango no es válido")
    void sinFechaFinal() {
        fechas.setFechaFin(null);

        boolean resultadoEsperado = false;
        boolean resultadoObtenido = fechas.tieneRangoValido();

        assertEquals(resultadoEsperado, resultadoObtenido);
    }

    @Test
    @DisplayName("Del 10 al 12 son tres días, incluyendo inicio y fin")
    void duracionTresDias() {
        long resultadoEsperado = 3;
        long resultadoObtenido = fechas.getDuracionDias();

        assertEquals(resultadoEsperado, resultadoObtenido);
    }

    @Test
    @DisplayName("Un rango inválido tiene duración cero")
    void duracionRangoInvalido() {
        fechas.setFechaFin("2026-11-09");

        long resultadoEsperado = 0;
        long resultadoObtenido = fechas.getDuracionDias();

        assertEquals(resultadoEsperado, resultadoObtenido);
    }

    @Test
    @DisplayName("La lista contiene todos los días del viaje en orden")
    void listarDiasDelViaje() {
        List<LocalDate> resultadoEsperado = List.of(
                LocalDate.of(2026, 11, 10),
                LocalDate.of(2026, 11, 11),
                LocalDate.of(2026, 11, 12));
        List<LocalDate> resultadoObtenido = fechas.listarDias();

        assertEquals(resultadoEsperado, resultadoObtenido);
    }

    @Test
    @DisplayName("Un rango inválido devuelve una lista vacía")
    void listarDiasRangoInvalido() {
        fechas.setFechaFin("2026-11-09");

        List<LocalDate> resultadoEsperado = List.of();
        List<LocalDate> resultadoObtenido = fechas.listarDias();

        assertEquals(resultadoEsperado, resultadoObtenido);
    }
}

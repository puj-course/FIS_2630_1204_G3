package com.wisetrip.modelo;

import java.math.BigDecimal;
import java.time.LocalDate;

/** Registro individual de un gasto efectuado durante un viaje. */
public record Gasto(
        int idGasto,
        String categoria,
        String descripcion,
        BigDecimal monto,
        String moneda,
        LocalDate fecha) {
}

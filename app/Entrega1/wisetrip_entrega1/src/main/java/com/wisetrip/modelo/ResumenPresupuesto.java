package com.wisetrip.modelo;

import java.math.BigDecimal;

/** Resumen financiero de los gastos de un viaje. */
public record ResumenPresupuesto(
        BigDecimal presupuestoTotal,
        BigDecimal totalGastado,
        BigDecimal saldoDisponible,
        String moneda) {
}

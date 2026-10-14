package com.wisetrip.modelo;

import java.math.BigDecimal;

/** Presupuesto total y moneda asociados a un viaje persistido. */
public record PresupuestoViaje(
        int idViaje,
        BigDecimal montoTotal,
        String moneda) {
}

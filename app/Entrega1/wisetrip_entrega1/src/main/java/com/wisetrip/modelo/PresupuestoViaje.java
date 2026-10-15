package com.wisetrip.modelo;

import java.math.BigDecimal;

/**
 * Presupuesto de un viaje ya guardado: cuánto dinero tiene en total y en
 * qué moneda.
 *
 * Es un record, así que sus datos no cambian después de crearse.
 *
 * Lo usa GastosServicio para comparar los gastos contra el presupuesto:
 * - el monto total sirve para calcular el saldo disponible y el cupo de
 *   cada categoría según el reparto;
 * - la moneda sirve para revisar que cada gasto esté en la misma moneda
 *   del viaje.
 *
 * Las reglas sobre sus datos (que pertenezca al viaje pedido, que el monto
 * no sea negativo y que tenga moneda) las revisa GastosServicio al
 * obtenerlo.
 *
 * @param idViaje    identificador del viaje al que pertenece el presupuesto
 * @param montoTotal dinero total del viaje, en su moneda
 * @param moneda     código de la moneda del viaje (por ejemplo, "COP")
 */
public record PresupuestoViaje(
        int idViaje,
        BigDecimal montoTotal,
        String moneda) {
}
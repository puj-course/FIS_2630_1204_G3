package com.wisetrip.negocio;

import java.util.List;
import java.util.Map;

import com.wisetrip.modelo.Gasto;
import com.wisetrip.modelo.ResumenPresupuesto;

/**
 * Contrato de negocio para registrar y consultar los gastos de un viaje.
 * Los importes de gastos y presupuesto se expresan en la moneda del viaje.
 */
public interface IGestorGastos {

    /**
     * Valida el gasto, que su moneda coincida con la moneda del presupuesto
     * asociado al viaje, y que no supere el cupo que el reparto (HU#69/#79)
     * le asignó a su categoría en las categorías de política estricta.
     * Un mapa vacío indica que no se encontraron errores.
     */
    Map<String, String> validarGasto(int idViaje, Gasto gasto);

    /**
     * Advertencias no bloqueantes sobre el impacto del gasto en el cupo de
     * su categoría (p. ej. "imprevistos" puede superarse sin bloquear el
     * registro). Un mapa vacío indica que no hay advertencias.
     */
    Map<String, String> advertenciasGasto(int idViaje, Gasto gasto);

    /**
     * Registra un gasto válido y devuelve el gasto persistido con su
     * identificador asignado.
     */
    Gasto registrarGasto(int idViaje, Gasto gasto);

    /** Devuelve los gastos registrados para el viaje. */
    List<Gasto> consultarGastos(int idViaje);

    /**
     * Devuelve presupuesto total, total gastado y saldo disponible
     * para el viaje, expresados en la moneda del presupuesto.
     */
    ResumenPresupuesto consultarResumen(int idViaje);
}

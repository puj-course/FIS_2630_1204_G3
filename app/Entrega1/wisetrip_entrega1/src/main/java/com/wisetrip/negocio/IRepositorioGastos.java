package com.wisetrip.negocio;

import java.math.BigDecimal;
import java.util.List;

import com.wisetrip.modelo.Gasto;

/** Contrato de persistencia para los gastos asociados a un viaje. */
public interface IRepositorioGastos {

    /**
     * Persiste un gasto para el viaje y devuelve el registro con su
     * identificador asignado.
     */
    Gasto guardar(int idViaje, Gasto gasto);

    /** Lista los gastos de un viaje, ordenados por fecha descendente. */
    List<Gasto> listarPorViaje(int idViaje);

    /** Calcula el total de gastos del viaje en su moneda de registro. */
    BigDecimal totalGastadoPorViaje(int idViaje);

    /** Calcula el total ya gastado en una categoría del viaje. */
    BigDecimal totalGastadoPorCategoria(int idViaje, String categoria);
}

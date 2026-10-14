package com.wisetrip.negocio;

/**
 * Contrato para consultar el reparto por categorías (HU#69/#79) de un
 * viaje. Devuelve la abstracción IDistribuible: quien lo consulta solo
 * necesita los porcentajes por categoría, no la clase RepartoPresupuesto.
 */
public interface IRepositorioRepartoPresupuesto {

    /**
     * Busca el reparto por categorías del viaje.
     *
     * @throws IllegalArgumentException si no existe el viaje o no tiene reparto
     */
    IDistribuible buscarReparto(int idViaje);
}

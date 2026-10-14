package com.wisetrip.negocio;

/**
 * Interfaz de negocio para consultar el reparto del presupuesto de un
 * viaje entre sus categorías de gasto (HU#69 y HU#79).
 *
 * Devuelve la interfaz IDistribuible y no la clase RepartoPresupuesto:
 * quien lo consulta solo necesita los porcentajes de cada categoría y lo
 * que se puede calcular con ellos, no saber qué clase concreta los guarda.
 *
 * SOLID Inversión de dependencias: GastosServicio depende de esta
 * interfaz, no de la base de datos ni de RepartoPresupuesto.
 */
public interface IRepositorioRepartoPresupuesto {

    /**
     * Busca cómo se repartió el presupuesto del viaje entre sus categorías.
     *
     * @param idViaje identificador del viaje
     * @return el reparto del viaje, con el porcentaje de cada categoría
     * @throws IllegalArgumentException si el viaje no existe o todavía no
     *         tiene un reparto guardado
     */
    IDistribuible buscarReparto(int idViaje);
}
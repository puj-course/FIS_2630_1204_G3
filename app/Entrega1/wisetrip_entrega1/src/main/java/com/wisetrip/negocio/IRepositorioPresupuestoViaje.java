package com.wisetrip.negocio;

import com.wisetrip.modelo.PresupuestoViaje;

/**
 * Interfaz de negocio para consultar el presupuesto guardado de un viaje.
 *
 * Define qué necesita el módulo de gastos, sin decir de dónde sale el
 * dato. La clase que la implemente decide cómo buscarlo (por ejemplo, en
 * la base de datos).
 *
 * SOLID Inversión de dependencias: GastosServicio depende de esta
 * interfaz, no de la base de datos.
 */
public interface IRepositorioPresupuestoViaje {

    /**
     * Busca el presupuesto de un viaje.
     *
     * @param idViaje identificador del viaje
     * @return el presupuesto del viaje, con su monto total y la moneda que
     *         eligió el usuario
     * @throws IllegalArgumentException si el viaje no existe
     */
    PresupuestoViaje buscarPresupuesto(int idViaje);
}
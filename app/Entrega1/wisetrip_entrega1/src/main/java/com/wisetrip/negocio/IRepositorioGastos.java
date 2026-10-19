package com.wisetrip.negocio;

import java.math.BigDecimal;
import java.util.List;

import com.wisetrip.modelo.Gasto;

/**
 * Interfaz de negocio para guardar y consultar los gastos de un viaje.
 *
 * Define qué operaciones de persistencia necesita el módulo de gastos,
 * sin decir cómo se hacen. La clase que la implemente decide dónde se
 * guardan (por ejemplo, en PostgreSQL).
 *
 * Todos los importes se expresan en la moneda en que se registró el
 * gasto, que es la misma moneda del presupuesto del viaje.
 *
 * SOLID Inversión de dependencias: el servicio de gastos depende de esta
 * interfaz, no de la base de datos.
 * GRASP Fabricación pura: separa el acceso a datos del dominio.
 */
public interface IRepositorioGastos {

    /**
     * Guarda un gasto del viaje.
     *
     * @param idViaje viaje al que pertenece el gasto
     * @param gasto   gasto que se va a guardar
     * @return el mismo gasto ya guardado, con el identificador que le
     *         asignó la base de datos
     */
    Gasto guardar(int idViaje, Gasto gasto);

    /**
     * Lista los gastos de un viaje, del más reciente al más antiguo.
     *
     * @param idViaje viaje del que se quieren los gastos
     * @return gastos ordenados por fecha descendente; si no hay ninguno,
     *         la lista está vacía
     */
    List<Gasto> listarPorViaje(int idViaje);

    /**
     * Suma todo lo que se ha gastado en el viaje.
     *
     * @param idViaje viaje del que se quiere el total
     * @return total gastado en la moneda del viaje; si no hay gastos,
     *         devuelve cero
     */
    BigDecimal totalGastadoPorViaje(int idViaje);

    /**
     * Suma lo que se ha gastado en una sola categoría del viaje
     * (por ejemplo, "hospedaje" o "transporte").
     *
     * Se usa para saber cuánto queda del cupo que el reparto del
     * presupuesto le asignó a esa categoría.
     *
     * @param idViaje   viaje del que se quiere el total
     * @param categoria clave de la categoría de gasto
     * @return total gastado en esa categoría, en la moneda del viaje; si no
     *         hay gastos en ella, devuelve cero
     */
    BigDecimal totalGastadoPorCategoria(int idViaje, String categoria);
}
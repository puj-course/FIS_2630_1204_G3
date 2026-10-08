package com.wisetrip.negocio;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Interfaz de negocio del dominio: cualquier reparto de dinero entre
 * categorías de gasto.
 *los servicios trabajan con esta abstracción, no con una clase concreta.
 */
public interface IDistribuible {

    int PORCENTAJE_MAXIMO = 100;

    /** Claves de las categorías, en el orden en que se muestran. */
    List<String> CATEGORIAS = List.of(
            "hospedaje", "alimentacion", "transporte", "actividades", "imprevistos");

    /** Porcentaje asignado a una categoría. */
    int porcentajeDe(String categoria);

    /** Asigna un porcentaje a una categoría. */
    void asignar(String categoria, int porcentaje);

    /** Suma de todos los porcentajes asignados. */
    int getTotal();

    /** Porcentaje que todavía no se ha repartido. */
    default int porcentajeDisponible() {
        return Math.max(PORCENTAJE_MAXIMO - getTotal(), 0);
    }

    /** Cuánto se pasa del 100%, o 0 si no se pasa. */
    default int porcentajeExcedido() {
        return Math.max(getTotal() - PORCENTAJE_MAXIMO, 0);
    }

    default boolean excedeElMaximo() {
        return getTotal() > PORCENTAJE_MAXIMO;
    }

    default boolean estaCompleto() {
        return getTotal() == PORCENTAJE_MAXIMO;
    }

    default boolean tienePorcentajesNegativos() {
        for (String categoria : CATEGORIAS) {
            if (porcentajeDe(categoria) < 0) {
                return true;
            }
        }
        return false;
    }

    /** Hospedaje o alimentación deben tener algo de dinero. */
    default boolean cubreLosBasicos() {
        return porcentajeDe("hospedaje") > 0 || porcentajeDe("alimentacion") > 0;
    }

    /** Dinero que le corresponde a una categoría sobre un presupuesto total. */
    default double montoDe(String categoria, double presupuestoTotal) {
        return presupuestoTotal * porcentajeDe(categoria) / 100.0;
    }

    /** Reparte el presupuesto total entre todas las categorías (clave → monto). */
    default Map<String, Double> distribuir(double presupuestoTotal) {
        Map<String, Double> montos = new LinkedHashMap<>();
        for (String categoria : CATEGORIAS) {
            montos.put(categoria, montoDe(categoria, presupuestoTotal));
        }
        return montos;
    }
}
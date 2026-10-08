package com.wisetrip.negocio;

import java.util.Map;

/**
 * Interfaz de negocio: cálculo de montos a partir de un reparto.
 * SOLID Segregación de interfaces: quien solo calcula no depende de
 * la validación.
 */
public interface ICalculadoraReparto {

    /** Monto de cada categoría, con su nombre visible, en el orden de la vista. */
    Map<String, Double> calcularMontos(IDistribuible reparto, double presupuestoTotal);

    /** Cuánto queda por día para un monto dado. */
    double porDia(double monto, long dias);
}
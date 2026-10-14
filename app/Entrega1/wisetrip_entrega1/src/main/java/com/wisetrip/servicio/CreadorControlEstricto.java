package com.wisetrip.servicio;

import com.wisetrip.negocio.ControlCategoriaCreador;
import com.wisetrip.negocio.IControlCategoriaGasto;

/**
 * Patrón Factory Method (HU#89), rol ConcreteCreator.
 *
 * Crea la política de control estricto: la que no deja que el gasto de una
 * categoría supere el cupo que le asignó el reparto del presupuesto.
 *
 * GastosServicio la usa para hospedaje, alimentación, transporte y
 * actividades.
 */
public class CreadorControlEstricto extends ControlCategoriaCreador {

    /**
     * Factory Method: devuelve un ControlCategoriaEstricto.
     *
     * @return la política de control estricto
     */
    @Override
    protected IControlCategoriaGasto crearControl() {
        return new ControlCategoriaEstricto();
    }
}
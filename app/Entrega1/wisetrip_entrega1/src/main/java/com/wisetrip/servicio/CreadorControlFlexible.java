package com.wisetrip.servicio;

import com.wisetrip.negocio.ControlCategoriaCreador;
import com.wisetrip.negocio.IControlCategoriaGasto;

/**
 * Patrón Factory Method (HU#89), rol ConcreteCreator.
 *
 * Crea la política de control flexible: la que permite que el gasto de una
 * categoría supere su cupo, pero avisando.
 *
 * GastosServicio la usa para imprevistos.
 */
public class CreadorControlFlexible extends ControlCategoriaCreador {

    /**
     * Factory Method: devuelve un ControlCategoriaFlexible.
     *
     * @return la política de control flexible
     */
    @Override
    protected IControlCategoriaGasto crearControl() {
        return new ControlCategoriaFlexible();
    }
}
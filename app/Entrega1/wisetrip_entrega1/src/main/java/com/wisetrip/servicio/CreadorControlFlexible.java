package com.wisetrip.servicio;

import com.wisetrip.negocio.ControlCategoriaCreador;
import com.wisetrip.negocio.IControlCategoriaGasto;

/** ConcreteCreator: fabrica la política flexible de control de categoría. */
public class CreadorControlFlexible extends ControlCategoriaCreador {

    @Override
    protected IControlCategoriaGasto crearControl() {
        return new ControlCategoriaFlexible();
    }
}

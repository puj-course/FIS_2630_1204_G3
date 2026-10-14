package com.wisetrip.servicio;

import com.wisetrip.negocio.ControlCategoriaCreador;
import com.wisetrip.negocio.IControlCategoriaGasto;

/** ConcreteCreator: fabrica la política estricta de control de categoría. */
public class CreadorControlEstricto extends ControlCategoriaCreador {

    @Override
    protected IControlCategoriaGasto crearControl() {
        return new ControlCategoriaEstricto();
    }
}

package com.wisetrip.negocio;

import java.math.BigDecimal;

import com.wisetrip.modelo.Gasto;
import com.wisetrip.modelo.ResultadoControlCategoria;

/**
 * Creator del patrón Factory Method (HU#89).
 *
 * Define el método de fábrica {@link #crearControl()}, que cada
 * ConcreteCreator implementa para decidir qué {@link IControlCategoriaGasto}
 * (Product) usar, y un método plantilla ({@link #evaluarGasto}) que lo
 * usa sin conocer su tipo concreto.
 *
 * GastosServicio (el cliente) depende solo de esta clase abstracta: nunca
 * instancia con new una política de control directamente.
 */
public abstract class ControlCategoriaCreador {

    /** Factory method: cada subclase decide qué Product concreto crear. */
    protected abstract IControlCategoriaGasto crearControl();

    /** Método plantilla: delega la creación y usa el Product resultante. */
    public ResultadoControlCategoria evaluarGasto(
            Gasto gasto, BigDecimal cupoCategoria, BigDecimal totalGastadoCategoria) {
        IControlCategoriaGasto control = crearControl();
        return control.evaluar(gasto, cupoCategoria, totalGastadoCategoria);
    }
}

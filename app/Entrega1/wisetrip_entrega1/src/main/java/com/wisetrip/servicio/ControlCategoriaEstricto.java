package com.wisetrip.servicio;

import java.math.BigDecimal;

import com.wisetrip.modelo.Gasto;
import com.wisetrip.modelo.ResultadoControlCategoria;
import com.wisetrip.negocio.IControlCategoriaGasto;

/**
 * ConcreteProduct: política estricta. Se usa en categorías básicas
 * (hospedaje, alimentacion, transporte, actividades): no permite
 * registrar un gasto que haga superar el cupo que les asignó el reparto.
 */
public class ControlCategoriaEstricto implements IControlCategoriaGasto {

    @Override
    public ResultadoControlCategoria evaluar(
            Gasto gasto, BigDecimal cupoCategoria, BigDecimal totalGastadoCategoria) {

        BigDecimal totalConGasto = totalGastadoCategoria.add(gasto.monto());
        if (totalConGasto.compareTo(cupoCategoria) > 0) {
            return new ResultadoControlCategoria(true, true,
                    "El gasto supera el cupo asignado a '" + gasto.categoria()
                    + "' (" + cupoCategoria + " " + gasto.moneda()
                    + "). Ajusta el monto o el reparto del presupuesto.");
        }
        return ResultadoControlCategoria.sinProblemas();
    }
}

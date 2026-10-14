package com.wisetrip.servicio;

import java.math.BigDecimal;

import com.wisetrip.modelo.Gasto;
import com.wisetrip.modelo.ResultadoControlCategoria;
import com.wisetrip.negocio.IControlCategoriaGasto;

/**
 * ConcreteProduct: política flexible. Se usa en "imprevistos", pensada
 * como colchón del viaje: permite superar su cupo, pero lo advierte para
 * que el viajero lo note a tiempo en vez de bloquear el registro.
 */
public class ControlCategoriaFlexible implements IControlCategoriaGasto {

    @Override
    public ResultadoControlCategoria evaluar(
            Gasto gasto, BigDecimal cupoCategoria, BigDecimal totalGastadoCategoria) {

        BigDecimal totalConGasto = totalGastadoCategoria.add(gasto.monto());
        if (totalConGasto.compareTo(cupoCategoria) > 0) {
            return new ResultadoControlCategoria(true, false,
                    "Ya superaste el cupo previsto para 'imprevistos' (" + cupoCategoria
                    + " " + gasto.moneda() + "). Sigue registrado, pero vigila el resto del viaje.");
        }
        return ResultadoControlCategoria.sinProblemas();
    }
}

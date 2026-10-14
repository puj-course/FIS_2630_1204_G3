package com.wisetrip.negocio;

import java.math.BigDecimal;

import com.wisetrip.modelo.Gasto;
import com.wisetrip.modelo.ResultadoControlCategoria;

/**
 * Product del patrón Factory Method (HU#89): política que decide si un
 * gasto puede registrarse sin salirse del cupo asignado a su categoría
 * por el reparto del presupuesto (HU#69/#79).
 *
 * Las categorías no se controlan igual: unas no deben excederse nunca y
 * otras (como "imprevistos") son un colchón que puede superarse avisando.
 * Cada forma de controlar eso es un ConcreteProduct distinto.
 */
public interface IControlCategoriaGasto {

    /**
     * Evalúa si, al sumar este gasto a lo ya gastado en su categoría,
     * se supera el cupo (cupoCategoria = presupuesto total * % del reparto).
     */
    ResultadoControlCategoria evaluar(
            Gasto gasto, BigDecimal cupoCategoria, BigDecimal totalGastadoCategoria);
}

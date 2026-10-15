package com.wisetrip.negocio;

import java.math.BigDecimal;

import com.wisetrip.modelo.Gasto;
import com.wisetrip.modelo.ResultadoControlCategoria;

/**
 * Patrón Factory Method (HU#89), rol Product.
 *
 * Es la política que decide si un gasto se puede registrar sin salirse del
 * cupo que el reparto del presupuesto (HU#69 y HU#79) le asignó a su
 * categoría.
 *
 * No todas las categorías se controlan igual:
 * - algunas no se deben exceder nunca (control estricto);
 * - otras, como "imprevistos", son un colchón que se puede superar, pero
 *   avisando (control flexible).
 *
 * Cada forma de control es un ConcreteProduct distinto que implementa esta
 * interfaz (ControlCategoriaEstricto y ControlCategoriaFlexible). Los crea
 * el ControlCategoriaCreador que corresponde a cada categoría, y quien los
 * usa (GastosServicio) solo conoce esta interfaz.
 */
public interface IControlCategoriaGasto {

    /**
     * Revisa qué pasa con el cupo de la categoría si se suma este gasto a
     * lo que ya se ha gastado en ella.
     *
     * El cupo de la categoría es el presupuesto total multiplicado por el
     * porcentaje que el reparto le dio a esa categoría.
     *
     * @param gasto                 gasto que se quiere registrar
     * @param cupoCategoria         dinero máximo que el reparto le asignó a
     *                              la categoría
     * @param totalGastadoCategoria lo que ya se ha gastado en la categoría,
     *                              sin contar este gasto
     * @return resultado que indica si se excede el cupo, si eso bloquea el
     *         registro y el mensaje para mostrar
     */
    ResultadoControlCategoria evaluar(
            Gasto gasto, BigDecimal cupoCategoria, BigDecimal totalGastadoCategoria);
}
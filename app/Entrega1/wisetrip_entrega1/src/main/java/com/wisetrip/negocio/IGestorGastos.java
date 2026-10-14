package com.wisetrip.negocio;

import java.util.List;
import java.util.Map;

import com.wisetrip.modelo.Gasto;
import com.wisetrip.modelo.ResumenPresupuesto;

/**
 * Interfaz de negocio del Módulo de Control de Presupuesto y Gastos.
 *
 * Define cómo se registran y consultan los gastos de un viaje y cómo se
 * obtiene el estado del presupuesto (total, gastado y saldo disponible).
 *
 * Todos los importes, tanto de los gastos como del presupuesto, se expresan
 * en la moneda del viaje; no se hacen conversiones dentro de este contrato.
 *
 * SOLID Inversión de dependencias: los controladores y servicios dependen de
 * esta interfaz, no de la clase que la implementa.
 * SOLID Segregación de interfaces: agrupa solo las operaciones de gastos.
 */
public interface IGestorGastos {

    /**
     * Revisa si un gasto se puede registrar en el viaje.
     *
     * Comprueba que:
     * <ul>
     *   <li>los datos del gasto sean correctos;</li>
     *   <li>su moneda sea la misma del presupuesto del viaje;</li>
     *   <li>no supere el cupo que el reparto del presupuesto (HU#69 y HU#79)
     *       le asignó a su categoría, cuando esa categoría tiene política
     *       estricta (no se permite pasarse del cupo).</li>
     * </ul>
     *
     * @param idViaje viaje al que pertenece el gasto
     * @param gasto   gasto que se quiere registrar
     * @return errores encontrados, con la clave del campo y el mensaje para
     *         mostrar; si el mapa está vacío, el gasto es válido
     */
    Map<String, String> validarGasto(int idViaje, Gasto gasto);

    /**
     * Calcula avisos que no impiden registrar el gasto, pero que el viajero
     * debería conocer sobre el cupo de su categoría.
     *
     * Por ejemplo, la categoría "imprevistos" se puede superar sin bloquear
     * el registro, pero se avisa que ya pasó de lo que se había separado.
     *
     * @param idViaje viaje al que pertenece el gasto
     * @param gasto   gasto que se quiere registrar
     * @return advertencias, con su clave y el mensaje para mostrar; si el
     *         mapa está vacío, no hay nada que advertir
     */
    Map<String, String> advertenciasGasto(int idViaje, Gasto gasto);

    /**
     * Guarda un gasto que ya pasó la validación.
     *
     * Se espera que antes se haya llamado a {@link #validarGasto(int, Gasto)}
     * y que no haya devuelto errores.
     *
     * @param idViaje viaje al que pertenece el gasto
     * @param gasto   gasto válido que se va a guardar
     * @return el mismo gasto ya guardado, con el identificador que le asignó
     *         la base de datos
     */
    Gasto registrarGasto(int idViaje, Gasto gasto);

    /**
     * Lista los gastos que el viajero ha registrado en el viaje.
     *
     * @param idViaje viaje del que se quieren consultar los gastos
     * @return gastos registrados; si no hay ninguno, la lista está vacía
     */
    List<Gasto> consultarGastos(int idViaje);

    /**
     * Calcula el estado actual del presupuesto del viaje.
     *
     * @param idViaje viaje del que se quiere el resumen
     * @return presupuesto total, total gastado y saldo disponible, todo en
     *         la moneda del presupuesto
     */
    ResumenPresupuesto consultarResumen(int idViaje);
}
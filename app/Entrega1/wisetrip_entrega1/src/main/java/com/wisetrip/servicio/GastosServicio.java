package com.wisetrip.servicio;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.wisetrip.modelo.Gasto;
import com.wisetrip.modelo.PresupuestoViaje;
import com.wisetrip.modelo.ResultadoControlCategoria;
import com.wisetrip.modelo.ResumenPresupuesto;
import com.wisetrip.negocio.ControlCategoriaCreador;
import com.wisetrip.negocio.IDistribuible;
import com.wisetrip.negocio.IGestorGastos;
import com.wisetrip.negocio.IRepositorioGastos;
import com.wisetrip.negocio.IRepositorioPresupuestoViaje;
import com.wisetrip.negocio.IRepositorioRepartoPresupuesto;

/**
 * Registra y consulta los gastos de un viaje, y calcula el estado de su
 * presupuesto (total, gastado y saldo disponible).
 *
 * Implementa la interfaz de negocio IGestorGastos.
 *
 * Recibe por constructor tres repositorios, todos como interfaces:
 * - IRepositorioGastos: guarda los gastos y calcula sus totales.
 * - IRepositorioPresupuestoViaje: entrega el presupuesto del viaje.
 * - IRepositorioRepartoPresupuesto: entrega el reparto por categorías
 *   (HU#69 y HU#79).
 *
 * Patrón Factory Method (HU#89): esta clase es el cliente. Para revisar si
 * un gasto se sale del cupo de su categoría, usa el ControlCategoriaCreador
 * que le corresponde a esa categoría, sin conocer qué control concreto
 * crea.
 */
public class GastosServicio implements IGestorGastos {

    /**
     * Creador concreto (Factory Method, HU#89) que se usa para cada
     * categoría de gasto:
     * - Hospedaje, alimentación, transporte y actividades usan
     *   CreadorControlEstricto: su cupo no se debe exceder.
     * - Imprevistos usa CreadorControlFlexible: es un colchón que se puede
     *   superar.
     *
     * Para agregar una categoría o cambiar su política basta con modificar
     * este mapa; la lógica de validación no cambia.
     */
    private static final Map<String, ControlCategoriaCreador> CREADORES_CONTROL = Map.of(
            "hospedaje", new CreadorControlEstricto(),
            "alimentacion", new CreadorControlEstricto(),
            "transporte", new CreadorControlEstricto(),
            "actividades", new CreadorControlEstricto(),
            "imprevistos", new CreadorControlFlexible());

    private final IRepositorioGastos repositorioGastos;
    private final IRepositorioPresupuestoViaje repositorioPresupuesto;
    private final IRepositorioRepartoPresupuesto repositorioReparto;

    /**
     * Crea el servicio con sus repositorios.
     *
     * @param repositorioGastos      repositorio de los gastos del viaje
     * @param repositorioPresupuesto repositorio del presupuesto del viaje
     * @param repositorioReparto     repositorio del reparto por categorías
     * @throws NullPointerException si alguno de los repositorios es null
     */
    public GastosServicio(IRepositorioGastos repositorioGastos,
                          IRepositorioPresupuestoViaje repositorioPresupuesto,
                          IRepositorioRepartoPresupuesto repositorioReparto) {
        this.repositorioGastos = Objects.requireNonNull(repositorioGastos);
        this.repositorioPresupuesto = Objects.requireNonNull(repositorioPresupuesto);
        this.repositorioReparto = Objects.requireNonNull(repositorioReparto);
    }

    /**
     * Revisa si un gasto se puede registrar en el viaje.
     *
     * Pasos, en orden:
     * 1. El id del viaje debe ser positivo; si no, se detiene ahí.
     * 2. Se busca el presupuesto del viaje.
     * 3. El gasto no puede ser null; si lo es, se detiene ahí.
     * 4. Se revisan categoría, descripción, monto, fecha y moneda. La
     *    moneda debe coincidir con la del presupuesto.
     * 5. Solo si no hubo errores, se revisa el cupo de la categoría. Si la
     *    política de la categoría lo bloquea, se agrega el error
     *    "cupoCategoria".
     *
     * @return errores por campo; vacío si el gasto es válido
     */
    @Override
    public Map<String, String> validarGasto(int idViaje, Gasto gasto) {
        Map<String, String> errores = new LinkedHashMap<>();
        if (idViaje <= 0) {
            errores.put("idViaje", "El identificador del viaje debe ser positivo.");
            return errores;
        }

        PresupuestoViaje presupuesto = obtenerPresupuesto(idViaje);
        if (gasto == null) {
            errores.put("gasto", "Debes proporcionar los datos del gasto.");
            return errores;
        }

        if (gasto.categoria() == null || !IDistribuible.CATEGORIAS.contains(gasto.categoria())) {
            errores.put("categoria", "Selecciona una categoría válida.");
        }
        if (gasto.descripcion() == null || gasto.descripcion().isBlank()) {
            errores.put("descripcion", "Ingresa una descripción para el gasto.");
        }
        if (gasto.monto() == null || gasto.monto().signum() <= 0) {
            errores.put("monto", "El monto del gasto debe ser mayor que cero.");
        }
        if (gasto.fecha() == null) {
            errores.put("fecha", "Ingresa la fecha en que se realizó el gasto.");
        }
        if (gasto.moneda() == null || gasto.moneda().isBlank()) {
            errores.put("moneda", "El gasto debe indicar su moneda.");
        } else if (presupuesto.moneda() == null
                || !gasto.moneda().trim().equalsIgnoreCase(presupuesto.moneda().trim())) {
            errores.put("moneda", "La moneda del gasto debe coincidir con la del viaje.");
        }

        if (errores.isEmpty()) {
            ResultadoControlCategoria resultado = evaluarCupoCategoria(idViaje, gasto, presupuesto);
            if (resultado.bloqueante()) {
                errores.put("cupoCategoria", resultado.mensaje());
            }
        }
        return errores;
    }

    /**
     * Calcula los avisos que no impiden registrar el gasto.
     *
     * Si el gasto tiene errores de validación, no se calcula nada y se
     * devuelve un mapa vacío. Si es válido y supera el cupo de su categoría
     * sin que la política lo bloquee (por ejemplo, en imprevistos), se
     * agrega el aviso "cupoCategoria".
     *
     * @return advertencias por clave; vacío si no hay nada que advertir
     */
    @Override
    public Map<String, String> advertenciasGasto(int idViaje, Gasto gasto) {
        Map<String, String> advertencias = new LinkedHashMap<>();
        if (!validarGasto(idViaje, gasto).isEmpty()) {
            return advertencias;
        }

        PresupuestoViaje presupuesto = obtenerPresupuesto(idViaje);
        ResultadoControlCategoria resultado = evaluarCupoCategoria(idViaje, gasto, presupuesto);
        if (resultado.excedeCupo() && !resultado.bloqueante()) {
            advertencias.put("cupoCategoria", resultado.mensaje());
        }
        return advertencias;
    }

    /**
     * Revisa el gasto contra el cupo de su categoría (HU#89).
     *
     * Une dos datos:
     * - el cupo de la categoría, calculado con el reparto del presupuesto
     *   (HU#69 y HU#79) sobre el monto total;
     * - lo que ya se ha gastado en esa categoría.
     *
     * Aquí actúa el Factory Method: el método toma el ControlCategoriaCreador
     * de la categoría y le pide evaluar el gasto. No conoce
     * ControlCategoriaEstricto ni ControlCategoriaFlexible.
     *
     * @return resultado con si excede el cupo, si bloquea y su mensaje
     * @throws NullPointerException si el repositorio no devuelve el reparto
     *         o el total gastado en la categoría
     */
    private ResultadoControlCategoria evaluarCupoCategoria(
            int idViaje, Gasto gasto, PresupuestoViaje presupuesto) {
        IDistribuible reparto = Objects.requireNonNull(
                repositorioReparto.buscarReparto(idViaje),
                "El repositorio debe devolver el reparto del viaje.");
        BigDecimal cupoCategoria = BigDecimal.valueOf(
                reparto.montoDe(gasto.categoria(), presupuesto.montoTotal().doubleValue()));
        BigDecimal totalGastadoCategoria = Objects.requireNonNull(
                repositorioGastos.totalGastadoPorCategoria(idViaje, gasto.categoria()),
                "El repositorio debe devolver el total gastado por categoría.");

        ControlCategoriaCreador creador = CREADORES_CONTROL.get(gasto.categoria());
        return creador.evaluarGasto(gasto, cupoCategoria, totalGastadoCategoria);
    }

    /**
     * Valida el gasto y, si es válido, lo guarda.
     *
     * @return el gasto guardado, con el identificador que le asignó el
     *         repositorio
     * @throws IllegalArgumentException si el gasto tiene errores de
     *         validación
     * @throws NullPointerException si el repositorio no devuelve el gasto
     *         guardado
     */
    @Override
    public Gasto registrarGasto(int idViaje, Gasto gasto) {
        Map<String, String> errores = validarGasto(idViaje, gasto);
        if (!errores.isEmpty()) {
            throw new IllegalArgumentException("No se puede registrar el gasto: " + errores);
        }
        return Objects.requireNonNull(
                repositorioGastos.guardar(idViaje, gasto),
                "El repositorio debe devolver el gasto persistido.");
    }

    /**
     * Lista los gastos del viaje.
     *
     * Antes comprueba que el viaje tenga un presupuesto válido.
     *
     * @return copia que no se puede modificar de la lista de gastos
     * @throws NullPointerException si el repositorio no devuelve la lista
     */
    @Override
    public List<Gasto> consultarGastos(int idViaje) {
        obtenerPresupuesto(idViaje);
        return List.copyOf(Objects.requireNonNull(
                repositorioGastos.listarPorViaje(idViaje),
                "El repositorio debe devolver una lista de gastos."));
    }

    /**
     * Calcula el resumen del presupuesto del viaje.
     *
     * El saldo disponible es el monto total menos el total gastado.
     *
     * @return monto total, total gastado, saldo disponible y moneda
     * @throws NullPointerException si el repositorio no devuelve el total
     *         gastado
     * @throws IllegalStateException si el total gastado es negativo
     */
    @Override
    public ResumenPresupuesto consultarResumen(int idViaje) {
        PresupuestoViaje presupuesto = obtenerPresupuesto(idViaje);
        BigDecimal totalGastado = Objects.requireNonNull(
                repositorioGastos.totalGastadoPorViaje(idViaje),
                "El repositorio debe devolver el total gastado.");
        if (totalGastado.signum() < 0) {
            throw new IllegalStateException("El total gastado no puede ser negativo.");
        }

        BigDecimal saldoDisponible = presupuesto.montoTotal().subtract(totalGastado);
        return new ResumenPresupuesto(
                presupuesto.montoTotal(),
                totalGastado,
                saldoDisponible,
                presupuesto.moneda());
    }

    /**
     * Busca el presupuesto del viaje y comprueba que sea coherente.
     *
     * @return el presupuesto del viaje
     * @throws IllegalArgumentException si el id del viaje no es positivo
     * @throws NullPointerException si el repositorio no devuelve el
     *         presupuesto
     * @throws IllegalStateException si el presupuesto es de otro viaje, si
     *         su monto total es null o negativo, o si no tiene moneda
     */
    private PresupuestoViaje obtenerPresupuesto(int idViaje) {
        if (idViaje <= 0) {
            throw new IllegalArgumentException("El identificador del viaje debe ser positivo.");
        }
        PresupuestoViaje presupuesto = Objects.requireNonNull(
                repositorioPresupuesto.buscarPresupuesto(idViaje),
                "El repositorio debe devolver el presupuesto del viaje.");
        if (presupuesto.idViaje() != idViaje) {
            throw new IllegalStateException("El repositorio devolvió el presupuesto de otro viaje.");
        }
        if (presupuesto.montoTotal() == null || presupuesto.montoTotal().signum() < 0) {
            throw new IllegalStateException("El presupuesto del viaje debe ser un monto no negativo.");
        }
        if (presupuesto.moneda() == null || presupuesto.moneda().isBlank()) {
            throw new IllegalStateException("El presupuesto del viaje debe tener una moneda.");
        }
        return presupuesto;
    }
}
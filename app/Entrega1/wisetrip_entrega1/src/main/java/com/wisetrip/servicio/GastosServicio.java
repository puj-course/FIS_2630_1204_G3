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

public class GastosServicio implements IGestorGastos {

    /**
     * Qué ConcreteCreator (Factory Method, HU#89) usar para cada categoría.
     * Las básicas no deben excederse; "imprevistos" es un colchón flexible.
     * Agregar o cambiar la política de una categoría solo toca este mapa,
     * no la lógica de validación.
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

    public GastosServicio(IRepositorioGastos repositorioGastos,
                          IRepositorioPresupuestoViaje repositorioPresupuesto,
                          IRepositorioRepartoPresupuesto repositorioReparto) {
        this.repositorioGastos = Objects.requireNonNull(repositorioGastos);
        this.repositorioPresupuesto = Objects.requireNonNull(repositorioPresupuesto);
        this.repositorioReparto = Objects.requireNonNull(repositorioReparto);
    }

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
     * Punto de integración de HU#89: cruza el reparto por categorías
     * (HU#69/#79) con lo ya registrado en Gastos para saber si este gasto
     * se sale del cupo de su categoría, y con qué política evaluarlo.
     *
     * Aquí es donde actúa el Factory Method: este método no conoce
     * ControlCategoriaEstricto ni ControlCategoriaFlexible, solo el
     * ControlCategoriaCreador que le corresponde a la categoría.
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

    @Override
    public List<Gasto> consultarGastos(int idViaje) {
        obtenerPresupuesto(idViaje);
        return List.copyOf(Objects.requireNonNull(
                repositorioGastos.listarPorViaje(idViaje),
                "El repositorio debe devolver una lista de gastos."));
    }

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

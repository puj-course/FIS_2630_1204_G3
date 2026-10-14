package com.wisetrip.servicio;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.wisetrip.modelo.Gasto;
import com.wisetrip.modelo.PresupuestoViaje;
import com.wisetrip.modelo.RepartoPresupuesto;
import com.wisetrip.negocio.IRepositorioGastos;
import com.wisetrip.negocio.IRepositorioPresupuestoViaje;
import com.wisetrip.negocio.IRepositorioRepartoPresupuesto;

class GastosServicioTest {

    private static final int ID_VIAJE = 12;

    private FakeRepositorioGastos repositorioGastos;
    private GastosServicio servicio;

    @BeforeEach
    void setUp() {
        repositorioGastos = new FakeRepositorioGastos();
        IRepositorioPresupuestoViaje repositorioPresupuesto =
                idViaje -> new PresupuestoViaje(idViaje, new BigDecimal("100.00"), "COP");
        // Reparto sugerido: hospedaje 35%, alimentacion 25%, transporte 20%,
        // actividades 15%, imprevistos 5% => cupos de 35.00, 25.00, 20.00, 15.00 y 5.00.
        IRepositorioRepartoPresupuesto repositorioReparto =
                idViaje -> new RepartoPresupuesto();
        servicio = new GastosServicio(repositorioGastos, repositorioPresupuesto, repositorioReparto);
    }

    @Test
    void registraGastoValidoEnRepositorio() {
        Gasto gasto = gasto("hospedaje", "Hotel", "25.50", "COP");

        Gasto registrado = servicio.registrarGasto(ID_VIAJE, gasto);

        assertEquals(gasto, registrado);
        assertEquals(1, repositorioGastos.gastos.size());
    }

    @Test
    void rechazaGastoConMonedaDistintaALaDelViaje() {
        Gasto gasto = gasto("alimentacion", "Almuerzo", "15.00", "USD");

        Map<String, String> errores = servicio.validarGasto(ID_VIAJE, gasto);

        assertTrue(errores.containsKey("moneda"));
        assertThrows(IllegalArgumentException.class, () -> servicio.registrarGasto(ID_VIAJE, gasto));
        assertEquals(0, repositorioGastos.gastos.size());
    }

    @Test
    void calculaSaldoDisponibleEnMonedaDelViaje() {
        repositorioGastos.totalGastado = new BigDecimal("35.25");

        var resumen = servicio.consultarResumen(ID_VIAJE);

        assertEquals(new BigDecimal("100.00"), resumen.presupuestoTotal());
        assertEquals(new BigDecimal("35.25"), resumen.totalGastado());
        assertEquals(new BigDecimal("64.75"), resumen.saldoDisponible());
        assertEquals("COP", resumen.moneda());
    }

    @Test
    void permiteQueElSaldoReflejeUnExcesoDeGasto() {
        repositorioGastos.totalGastado = new BigDecimal("120.00");

        var resumen = servicio.consultarResumen(ID_VIAJE);

        assertEquals(new BigDecimal("-20.00"), resumen.saldoDisponible());
    }

    // ===== Factory Method (HU#89): política por categoría =====

    @Test
    void bloqueaGastoQueSuperaElCupoDeUnaCategoriaEstricta() {
        // hospedaje tiene cupo de 35.00 (35% de 100.00); ya se gastaron 30.00.
        repositorioGastos.totalGastadoPorCategoria.put("hospedaje", new BigDecimal("30.00"));
        Gasto gasto = gasto("hospedaje", "Hotel extra", "10.00", "COP");

        Map<String, String> errores = servicio.validarGasto(ID_VIAJE, gasto);

        assertTrue(errores.containsKey("cupoCategoria"));
        assertThrows(IllegalArgumentException.class, () -> servicio.registrarGasto(ID_VIAJE, gasto));
        assertEquals(0, repositorioGastos.gastos.size());
    }

    @Test
    void permiteSuperarElCupoDeImprevistosPeroAdvierte() {
        // imprevistos tiene cupo de 5.00 (5% de 100.00); ya se gastaron 4.00.
        repositorioGastos.totalGastadoPorCategoria.put("imprevistos", new BigDecimal("4.00"));
        Gasto gasto = gasto("imprevistos", "Taxi imprevisto", "3.00", "COP");

        Map<String, String> errores = servicio.validarGasto(ID_VIAJE, gasto);
        Gasto registrado = servicio.registrarGasto(ID_VIAJE, gasto);
        Map<String, String> advertencias = servicio.advertenciasGasto(ID_VIAJE, gasto);

        assertTrue(errores.isEmpty(), "imprevistos no debe bloquear el registro");
        assertEquals(gasto, registrado);
        assertTrue(advertencias.containsKey("cupoCategoria"));
    }

    private static Gasto gasto(String categoria, String descripcion, String monto, String moneda) {
        return new Gasto(0, categoria, descripcion, new BigDecimal(monto), moneda, LocalDate.now());
    }

    private static final class FakeRepositorioGastos implements IRepositorioGastos {
        private final java.util.ArrayList<Gasto> gastos = new java.util.ArrayList<>();
        private final Map<String, BigDecimal> totalGastadoPorCategoria = new java.util.HashMap<>();
        private BigDecimal totalGastado = BigDecimal.ZERO;

        @Override
        public Gasto guardar(int idViaje, Gasto gasto) {
            gastos.add(gasto);
            return gasto;
        }

        @Override
        public List<Gasto> listarPorViaje(int idViaje) {
            return List.copyOf(gastos);
        }

        @Override
        public BigDecimal totalGastadoPorViaje(int idViaje) {
            return totalGastado;
        }

        @Override
        public BigDecimal totalGastadoPorCategoria(int idViaje, String categoria) {
            return totalGastadoPorCategoria.getOrDefault(categoria, BigDecimal.ZERO);
        }
    }
}

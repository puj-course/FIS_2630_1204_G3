package com.wisetrip.servicio;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import com.wisetrip.modelo.EstadoCategoriaGasto;
import com.wisetrip.modelo.Gasto;
import com.wisetrip.modelo.PresupuestoViaje;
import com.wisetrip.modelo.RepartoPresupuesto;
import com.wisetrip.modelo.ResumenPresupuesto;
import com.wisetrip.negocio.IRepositorioGastos;
import com.wisetrip.negocio.IRepositorioPresupuestoViaje;
import com.wisetrip.negocio.IRepositorioRepartoPresupuesto;

/**
 * Pruebas unitarias de GastosServicio (CP01 - CP16).
 * Los repositorios se simulan con Mockito, así que no se toca la base de datos (F.I.R.S.T: Fast, Isolated).
 * Cada prueba sigue el patrón Arrange - Act - Assert.
 *
 * Datos base: viaje 1, presupuesto de 1.000.000 COP, reparto sugerido por WiseTrip
 * (hospedaje 35% = 350.000, imprevistos 5% = 50.000).
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@DisplayName("GastosServicio")
class GastosServicioTest {

    private static final int ID_VIAJE = 1;
    private static final String COP = "COP";
    private static final BigDecimal TOTAL = new BigDecimal("1000000");

    @Mock
    private IRepositorioGastos repositorioGastos;
    @Mock
    private IRepositorioPresupuestoViaje repositorioPresupuesto;
    @Mock
    private IRepositorioRepartoPresupuesto repositorioReparto;

    private GastosServicio servicio;

    @BeforeEach
    void setUp() {
        servicio = new GastosServicio(repositorioGastos, repositorioPresupuesto, repositorioReparto);
        when(repositorioPresupuesto.buscarPresupuesto(ID_VIAJE))
                .thenReturn(new PresupuestoViaje(ID_VIAJE, TOTAL, COP));
        when(repositorioReparto.buscarReparto(ID_VIAJE)).thenReturn(new RepartoPresupuesto());
        when(repositorioGastos.totalGastadoPorCategoria(anyInt(), any())).thenReturn(BigDecimal.ZERO);
        when(repositorioGastos.totalGastadoPorViaje(ID_VIAJE)).thenReturn(BigDecimal.ZERO);
    }

    private static Gasto gasto(String categoria, String monto) {
        return new Gasto(0, categoria, "Gasto de prueba", new BigDecimal(monto), COP, LocalDate.of(2026, 11, 20));
    }

    // ---------------------------------------------------------------- validarGasto

    @Test
    @DisplayName("CP01 validarGasto: gasto válido dentro del cupo no genera errores")
    void cp01_validarGasto_gastoValido_sinErrores() {
        // Arrange
        Gasto gasto = gasto("hospedaje", "100000");
        // Act
        Map<String, String> errores = servicio.validarGasto(ID_VIAJE, gasto);
        // Assert
        assertTrue(errores.isEmpty());
    }

    @Test
    @DisplayName("CP02 validarGasto: idViaje no positivo se rechaza sin consultar repositorios")
    void cp02_validarGasto_idViajeCero_error() {
        Map<String, String> errores = servicio.validarGasto(0, gasto("hospedaje", "100000"));

        assertEquals(1, errores.size());
        assertTrue(errores.containsKey("idViaje"));
        verifyNoInteractions(repositorioPresupuesto, repositorioReparto, repositorioGastos);
    }

    @Test
    @DisplayName("CP03 validarGasto: gasto null devuelve error 'gasto'")
    void cp03_validarGasto_gastoNull_error() {
        Map<String, String> errores = servicio.validarGasto(ID_VIAJE, null);

        assertTrue(errores.containsKey("gasto"));
    }

    @Test
    @DisplayName("CP04 validarGasto: moneda distinta a la del viaje devuelve error 'moneda'")
    void cp04_validarGasto_monedaDistinta_error() {
        Gasto enDolares = new Gasto(0, "hospedaje", "Hotel", new BigDecimal("100"), "USD", LocalDate.of(2026, 11, 20));

        Map<String, String> errores = servicio.validarGasto(ID_VIAJE, enDolares);

        assertTrue(errores.containsKey("moneda"));
        assertTrue(!errores.containsKey("cupoCategoria"), "Con errores previos no se evalúa el cupo");
    }

    @Test
    @DisplayName("CP05 validarGasto: borde, monto igual al cupo exacto se permite")
    void cp05_validarGasto_montoIgualAlCupo_sinErrores() {
        // cupo de hospedaje = 35% de 1.000.000 = 350.000
        Gasto gasto = gasto("hospedaje", "350000");

        Map<String, String> errores = servicio.validarGasto(ID_VIAJE, gasto);

        assertTrue(errores.isEmpty());
    }

    @Test
    @DisplayName("CP06 validarGasto: borde, un centavo sobre el cupo se bloquea (política estricta)")
    void cp06_validarGasto_montoSobreElCupo_bloqueado() {
        Gasto gasto = gasto("hospedaje", "350000.01");

        Map<String, String> errores = servicio.validarGasto(ID_VIAJE, gasto);

        assertTrue(errores.containsKey("cupoCategoria"));
        assertTrue(errores.get("cupoCategoria").contains("hospedaje"));
    }

    @Test
    @DisplayName("CP07 validarGasto: monto cero devuelve error 'monto'")
    void cp07_validarGasto_montoCero_error() {
        Map<String, String> errores = servicio.validarGasto(ID_VIAJE, gasto("transporte", "0"));

        assertTrue(errores.containsKey("monto"));
    }

    // ---------------------------------------------------------------- advertenciasGasto

    @Test
    @DisplayName("CP08 advertenciasGasto: imprevistos supera el cupo, no se bloquea pero se advierte")
    void cp08_advertenciasGasto_imprevistosExcede_soloAdvierte() {
        // cupo de imprevistos = 5% de 1.000.000 = 50.000; ya se gastaron 40.000
        when(repositorioGastos.totalGastadoPorCategoria(ID_VIAJE, "imprevistos"))
                .thenReturn(new BigDecimal("40000"));
        Gasto gasto = gasto("imprevistos", "20000");

        Map<String, String> errores = servicio.validarGasto(ID_VIAJE, gasto);
        Map<String, String> advertencias = servicio.advertenciasGasto(ID_VIAJE, gasto);

        assertTrue(errores.isEmpty(), "Imprevistos usa control flexible: no bloquea");
        assertTrue(advertencias.containsKey("cupoCategoria"));
    }

    @Test
    @DisplayName("CP09 advertenciasGasto: un gasto inválido no genera advertencias")
    void cp09_advertenciasGasto_gastoInvalido_vacio() {
        Gasto sinDescripcion = new Gasto(0, "hospedaje", " ", new BigDecimal("100"), COP, LocalDate.of(2026, 11, 20));

        Map<String, String> advertencias = servicio.advertenciasGasto(ID_VIAJE, sinDescripcion);

        assertTrue(advertencias.isEmpty());
    }

    // ---------------------------------------------------------------- registrarGasto

    @Test
    @DisplayName("CP10 registrarGasto: gasto válido se guarda y se devuelve con el id asignado")
    void cp10_registrarGasto_gastoValido_seGuarda() {
        Gasto entrada = gasto("alimentacion", "50000");
        Gasto guardado = new Gasto(55, "alimentacion", "Gasto de prueba", new BigDecimal("50000"), COP,
                LocalDate.of(2026, 11, 20));
        when(repositorioGastos.guardar(ID_VIAJE, entrada)).thenReturn(guardado);

        Gasto resultado = servicio.registrarGasto(ID_VIAJE, entrada);

        assertEquals(55, resultado.idGasto());
        verify(repositorioGastos).guardar(ID_VIAJE, entrada);
    }

    @Test
    @DisplayName("CP11 registrarGasto: gasto inválido lanza IllegalArgumentException y no guarda")
    void cp11_registrarGasto_gastoInvalido_lanzaExcepcion() {
        Gasto invalido = gasto("categoria-inexistente", "50000");

        assertThrows(IllegalArgumentException.class, () -> servicio.registrarGasto(ID_VIAJE, invalido));
        verify(repositorioGastos, never()).guardar(anyInt(), any(Gasto.class));
    }

    // ---------------------------------------------------------------- consultarGastos

    @Test
    @DisplayName("CP12 consultarGastos: devuelve una copia que no se puede modificar")
    void cp12_consultarGastos_devuelveListaInmutable() {
        List<Gasto> original = new ArrayList<>(List.of(gasto("transporte", "10000")));
        when(repositorioGastos.listarPorViaje(ID_VIAJE)).thenReturn(original);

        List<Gasto> resultado = servicio.consultarGastos(ID_VIAJE);

        assertEquals(1, resultado.size());
        Gasto nuevo = gasto("transporte", "1");
        assertThrows(UnsupportedOperationException.class, () -> resultado.add(nuevo));
    }

    // ---------------------------------------------------------------- consultarResumen

    @Test
    @DisplayName("CP13 consultarResumen: saldo disponible = total - gastado")
    void cp13_consultarResumen_calculaSaldo() {
        when(repositorioGastos.totalGastadoPorViaje(ID_VIAJE)).thenReturn(new BigDecimal("250000"));

        ResumenPresupuesto resumen = servicio.consultarResumen(ID_VIAJE);

        assertEquals(0, new BigDecimal("750000").compareTo(resumen.saldoDisponible()));
        assertEquals(COP, resumen.moneda());
    }

    @Test
    @DisplayName("CP14 consultarResumen: borde, si se gasta todo el presupuesto el saldo es cero")
    void cp14_consultarResumen_gastadoIgualAlTotal_saldoCero() {
        when(repositorioGastos.totalGastadoPorViaje(ID_VIAJE)).thenReturn(TOTAL);

        ResumenPresupuesto resumen = servicio.consultarResumen(ID_VIAJE);

        assertEquals(0, resumen.saldoDisponible().signum());
    }

    @Test
    @DisplayName("CP15 consultarResumen: total gastado negativo lanza IllegalStateException")
    void cp15_consultarResumen_gastadoNegativo_lanzaExcepcion() {
        when(repositorioGastos.totalGastadoPorViaje(ID_VIAJE)).thenReturn(new BigDecimal("-1"));

        assertThrows(IllegalStateException.class, () -> servicio.consultarResumen(ID_VIAJE));
    }

    @Test
    @DisplayName("CP16 consultarResumen: presupuesto de otro viaje lanza IllegalStateException")
    void cp16_consultarResumen_presupuestoDeOtroViaje_lanzaExcepcion() {
        when(repositorioPresupuesto.buscarPresupuesto(ID_VIAJE))
                .thenReturn(new PresupuestoViaje(99, TOTAL, COP));

        assertThrows(IllegalStateException.class, () -> servicio.consultarResumen(ID_VIAJE));
    }

    // ---------------------------------------------------------------- descripción (HU#89)

    @Test
    @DisplayName("CP17 validarGasto: borde, descripción de más de 200 caracteres se rechaza")
    void cp17_validarGasto_descripcionMuyLarga_error() {
        Gasto largo = new Gasto(0, "hospedaje", "x".repeat(201), new BigDecimal("100"), COP,
                LocalDate.of(2026, 11, 20));

        Map<String, String> errores = servicio.validarGasto(ID_VIAJE, largo);

        assertEquals("La descripción puede tener máximo 200 caracteres.", errores.get("descripcion"));
    }

    // ---------------------------------------------------------------- consultarEstadoCategorias

    @Test
    @DisplayName("CP18 consultarEstadoCategorias: una fila por categoría con su cupo y lo gastado")
    void cp18_consultarEstadoCategorias_cupoYGastadoPorCategoria() {
        when(repositorioGastos.listarPorViaje(ID_VIAJE)).thenReturn(List.of(
                gasto("hospedaje", "100000"), gasto("hospedaje", "50000"), gasto("transporte", "20000")));

        List<EstadoCategoriaGasto> estados = servicio.consultarEstadoCategorias(ID_VIAJE);

        assertEquals(5, estados.size());
        EstadoCategoriaGasto hospedaje = estados.get(0);
        assertEquals("hospedaje", hospedaje.categoria());
        assertEquals(0, new BigDecimal("350000").compareTo(hospedaje.cupo()));   // 35% de 1.000.000
        assertEquals(0, new BigDecimal("150000").compareTo(hospedaje.gastado()));
        assertEquals(0, new BigDecimal("20000").compareTo(estados.get(2).gastado()));
    }

    @Test
    @DisplayName("CP19 consultarEstadoCategorias: sin gastos todas las categorías quedan en cero")
    void cp19_consultarEstadoCategorias_sinGastos_todoEnCero() {
        when(repositorioGastos.listarPorViaje(ID_VIAJE)).thenReturn(List.of());

        List<EstadoCategoriaGasto> estados = servicio.consultarEstadoCategorias(ID_VIAJE);

        assertTrue(estados.stream().allMatch(e -> e.gastado().signum() == 0));
    }
}
package com.wisetrip.servicio;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.wisetrip.modelo.Presupuesto;

/**
 * Pruebas unitarias de PresupuestoServicio (CP17 - CP33).
 * El servicio no tiene dependencias externas, así que no necesita mocks.
 * Patrón Arrange - Act - Assert en cada prueba.
 */
@DisplayName("PresupuestoServicio")
class PresupuestoServicioTest {

    private PresupuestoServicio servicio;

    @BeforeEach
    void setUp() {
        servicio = new PresupuestoServicio();
    }

    // ---------------------------------------------------------------- validarPresupuesto

    @Test
    @DisplayName("CP17 validarPresupuesto: monto y moneda válidos no generan errores")
    void cp17_validarPresupuesto_valido_sinErrores() {
        Presupuesto presupuesto = new Presupuesto("1.500.000", "COP");

        Map<String, String> errores = servicio.validarPresupuesto(presupuesto, "Colombia");

        assertTrue(errores.isEmpty());
    }

    @Test
    @DisplayName("CP18 validarPresupuesto: monto vacío devuelve error 'monto'")
    void cp18_validarPresupuesto_montoVacio_error() {
        Map<String, String> errores = servicio.validarPresupuesto(new Presupuesto("", "COP"), "Colombia");

        assertTrue(errores.containsKey("monto"));
    }

    @Test
    @DisplayName("CP19 validarPresupuesto: monto con letras devuelve error 'monto'")
    void cp19_validarPresupuesto_montoConLetras_error() {
        Map<String, String> errores = servicio.validarPresupuesto(new Presupuesto("abc", "COP"), "Colombia");

        assertTrue(errores.containsKey("monto"));
    }

    @Test
    @DisplayName("CP20 validarPresupuesto: monto negativo se rechaza")
    void cp20_validarPresupuesto_montoNegativo_error() {
        Map<String, String> errores = servicio.validarPresupuesto(new Presupuesto("-5", "COP"), "Colombia");

        assertTrue(errores.containsKey("monto"));
    }

    @Test
    @DisplayName("CP21 validarPresupuesto: borde, monto cero devuelve 'mayor que cero'")
    void cp21_validarPresupuesto_montoCero_error() {
        Map<String, String> errores = servicio.validarPresupuesto(new Presupuesto("0", "COP"), "Colombia");

        assertTrue(errores.get("monto").contains("mayor que cero"));
    }

    @Test
    @DisplayName("CP22 validarPresupuesto: borde, 1.000.000.000 (máximo) es válido")
    void cp22_validarPresupuesto_montoMaximo_valido() {
        Map<String, String> errores = servicio.validarPresupuesto(new Presupuesto("1.000.000.000", "COP"), "Colombia");

        assertTrue(errores.isEmpty());
    }

    @Test
    @DisplayName("CP23 validarPresupuesto: borde, 1.000.000.001 supera el máximo")
    void cp23_validarPresupuesto_montoSobreElMaximo_error() {
        Map<String, String> errores = servicio.validarPresupuesto(new Presupuesto("1.000.000.001", "COP"), "Colombia");

        assertTrue(errores.get("monto").contains("realista"));
    }

    @Test
    @DisplayName("CP24 validarPresupuesto: moneda que no está en la lista devuelve error 'moneda'")
    void cp24_validarPresupuesto_monedaInexistente_error() {
        Map<String, String> errores = servicio.validarPresupuesto(new Presupuesto("100", "XXX"), "Colombia");

        assertTrue(errores.containsKey("moneda"));
    }

    @Test
    @DisplayName("CP25 validarPresupuesto: moneda null devuelve error 'moneda'")
    void cp25_validarPresupuesto_monedaNull_error() {
        Map<String, String> errores = servicio.validarPresupuesto(new Presupuesto("100", null), "Colombia");

        assertTrue(errores.containsKey("moneda"));
    }

    // ---------------------------------------------------------------- convertirAUsd

    @Test
    @DisplayName("CP26 convertirAUsd: 4.000.000 COP equivalen a 1000 USD (tasa fija 4000)")
    void cp26_convertirAUsd_cop_aDolares() {
        double usd = servicio.convertirAUsd(new Presupuesto("4.000.000", "COP"));

        assertEquals(1000.0, usd, 0.001);
    }

    @Test
    @DisplayName("CP27 convertirAUsd: moneda desconocida devuelve 0")
    void cp27_convertirAUsd_monedaDesconocida_cero() {
        double usd = servicio.convertirAUsd(new Presupuesto("100", "XXX"));

        assertEquals(0.0, usd, 0.0);
    }

    @Test
    @DisplayName("CP28 convertirAUsd: monto inválido devuelve 0")
    void cp28_convertirAUsd_montoInvalido_cero() {
        double usd = servicio.convertirAUsd(new Presupuesto("abc", "COP"));

        assertEquals(0.0, usd, 0.0);
    }

    @Test
    @DisplayName("CP29 convertirAUsd: borde, redondea a 2 decimales (100 COP = 0.03 USD)")
    void cp29_convertirAUsd_redondeaADosDecimales() {
        double usd = servicio.convertirAUsd(new Presupuesto("100", "COP"));

        assertEquals(0.03, usd, 0.0001);
    }

    // ---------------------------------------------------------------- monedasDisponibles / nombreMoneda / formatear

    @Test
    @DisplayName("CP30 monedasDisponibles: devuelve las 19 monedas LATAM sin importar el país")
    void cp30_monedasDisponibles_igualParaCualquierPais() {
        Map<String, String> paraPeru = servicio.monedasDisponibles("Perú");
        Map<String, String> paraNull = servicio.monedasDisponibles(null);

        assertEquals(19, paraPeru.size());
        assertTrue(paraPeru.containsKey("COP") && paraPeru.containsKey("USD"));
        assertEquals(paraPeru, paraNull);
    }

    @Test
    @DisplayName("CP31 nombreMoneda: devuelve el nombre; si el código no existe devuelve el mismo código")
    void cp31_nombreMoneda_conocidaYDesconocida() {
        assertEquals("Peso colombiano (COP)", servicio.nombreMoneda("COP"));
        assertEquals("ZZZ", servicio.nombreMoneda("ZZZ"));
    }

    @Test
    @DisplayName("CP32 formatear: separador de miles, 2 decimales y borde en cero")
    void cp32_formatear_milesYDecimales() {
        assertEquals("1,234,567.89", servicio.formatear(1234567.891));
        assertEquals("0.00", servicio.formatear(0));
    }

    // ---------------------------------------------------------------- lógica de negocio: formato numérico es-CO

    @Test
    @DisplayName("CP33 getMontoNumerico: formato colombiano (punto = miles, coma = decimales)")
    void cp33_montoNumerico_formatoColombiano() {
        assertEquals(1500.5, new Presupuesto("1.500,50", "COP").getMontoNumerico(), 0.0001);
        // Caso particular: un solo punto se interpreta como separador de miles, no como decimal.
        assertEquals(15.0, new Presupuesto("1.5", "USD").getMontoNumerico(), 0.0001);
    }
}
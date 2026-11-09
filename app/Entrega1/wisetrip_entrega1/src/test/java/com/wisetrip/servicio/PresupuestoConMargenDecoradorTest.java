package com.wisetrip.servicio;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.wisetrip.modelo.Presupuesto;

/**
 * Pruebas unitarias de PresupuestoConMargenDecorador (patrón Decorator).
 *
 * El componente decorado (PresupuestoServicio) se simula con Mockito. Se
 * revisa el comportamiento que el decorador agrega (margen de imprevistos
 * del 5%) y que el resto de operaciones se delegan sin cambios.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("PresupuestoConMargenDecorador")
class PresupuestoConMargenDecoradorTest {

    private static final double DELTA = 0.0001;

    @Mock
    private PresupuestoServicio gestorBase;

    private PresupuestoConMargenDecorador decorador;
    private Presupuesto presupuesto;

    @BeforeEach
    void setUp() {
        decorador = new PresupuestoConMargenDecorador(gestorBase);
        presupuesto = new Presupuesto("1000", "USD");
    }

    // ------------------------------------------------------------ comportamiento agregado

    @Test
    @DisplayName("el margen de imprevistos es el 5% del presupuesto en USD")
    void margen_esElCincoPorCiento() {
        when(gestorBase.convertirAUsd(presupuesto)).thenReturn(1000.0);

        assertEquals(50.0, decorador.calcularMargenImprevistos(presupuesto), DELTA);
    }

    @Test
    @DisplayName("el margen se redondea a dos decimales")
    void margen_seRedondeaADosDecimales() {
        when(gestorBase.convertirAUsd(presupuesto)).thenReturn(123.456); // 5% = 6.1728

        assertEquals(6.17, decorador.calcularMargenImprevistos(presupuesto), DELTA);
    }

    @Test
    @DisplayName("borde: presupuesto en cero da margen cero")
    void presupuestoCero_margenCero() {
        when(gestorBase.convertirAUsd(presupuesto)).thenReturn(0.0);

        assertEquals(0.0, decorador.calcularMargenImprevistos(presupuesto), DELTA);
    }

    @Test
    @DisplayName("borde: presupuesto negativo (conversión fallida) da margen cero")
    void presupuestoNegativo_margenCero() {
        when(gestorBase.convertirAUsd(presupuesto)).thenReturn(-10.0);

        assertEquals(0.0, decorador.calcularMargenImprevistos(presupuesto), DELTA);
    }

    // ------------------------------------------------------------ delegación sin cambios

    @Test
    @DisplayName("validarPresupuesto se delega al componente base")
    void validarPresupuesto_seDelega() {
        Map<String, String> errores = Map.of("monto", "Ingresa un monto.");
        when(gestorBase.validarPresupuesto(presupuesto, "Colombia")).thenReturn(errores);

        assertSame(errores, decorador.validarPresupuesto(presupuesto, "Colombia"));
        verify(gestorBase).validarPresupuesto(presupuesto, "Colombia");
    }

    @Test
    @DisplayName("convertirAUsd devuelve lo mismo que el componente base")
    void convertirAUsd_seDelega() {
        when(gestorBase.convertirAUsd(presupuesto)).thenReturn(250.5);

        assertEquals(250.5, decorador.convertirAUsd(presupuesto), DELTA);
    }

    @Test
    @DisplayName("monedasDisponibles, nombreMoneda y formatear se delegan")
    void demasOperaciones_seDelegan() {
        Map<String, String> monedas = Map.of("COP", "Peso colombiano");
        when(gestorBase.monedasDisponibles("Colombia")).thenReturn(monedas);
        when(gestorBase.nombreMoneda("COP")).thenReturn("Peso colombiano");
        when(gestorBase.formatear(1500.0)).thenReturn("1.500");

        assertSame(monedas, decorador.monedasDisponibles("Colombia"));
        assertEquals("Peso colombiano", decorador.nombreMoneda("COP"));
        assertEquals("1.500", decorador.formatear(1500.0));
    }
}

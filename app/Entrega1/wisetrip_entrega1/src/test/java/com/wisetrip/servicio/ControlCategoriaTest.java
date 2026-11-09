package com.wisetrip.servicio;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.wisetrip.modelo.Gasto;
import com.wisetrip.modelo.ResultadoControlCategoria;
import com.wisetrip.negocio.ControlCategoriaCreador;

/**
 * Pruebas unitarias del patrón Factory Method de presupuesto (HU#89).
 *
 * Revisa los dos ConcreteProducts (ControlCategoriaEstricto y
 * ControlCategoriaFlexible) y los dos ConcreteCreators (CreadorControlEstricto
 * y CreadorControlFlexible) usados a través del Creator abstracto.
 *
 * No usan base de datos ni Spring: solo objetos en memoria (F.I.R.S.T: Fast,
 * Independent, Repeatable).
 */
@DisplayName("Factory Method de control de categorías")
class ControlCategoriaTest {

    private static final String USD = "USD";
    private static final BigDecimal CUPO = new BigDecimal("400.00");

    private static Gasto gasto(String categoria, String monto) {
        return new Gasto(0, categoria, "Gasto de prueba", new BigDecimal(monto), USD,
                LocalDate.of(2026, 12, 1));
    }

    // ------------------------------------------------------------ ConcreteProduct estricto

    @Nested
    @DisplayName("ControlCategoriaEstricto")
    class Estricto {

        private final ControlCategoriaEstricto control = new ControlCategoriaEstricto();

        @Test
        @DisplayName("gasto que queda dentro del cupo no tiene problemas")
        void dentroDelCupo_sinProblemas() {
            ResultadoControlCategoria resultado =
                    control.evaluar(gasto("hospedaje", "100"), CUPO, new BigDecimal("200"));

            assertFalse(resultado.excedeCupo());
            assertFalse(resultado.bloqueante());
            assertNull(resultado.mensaje());
        }

        @Test
        @DisplayName("borde: gastar exactamente el cupo está permitido")
        void exactamenteElCupo_permitido() {
            ResultadoControlCategoria resultado =
                    control.evaluar(gasto("hospedaje", "150"), CUPO, new BigDecimal("250"));

            assertFalse(resultado.excedeCupo());
        }

        @Test
        @DisplayName("pasarse del cupo excede y BLOQUEA el registro")
        void superaElCupo_bloquea() {
            ResultadoControlCategoria resultado =
                    control.evaluar(gasto("hospedaje", "80"), CUPO, new BigDecimal("350"));

            assertTrue(resultado.excedeCupo());
            assertTrue(resultado.bloqueante());
            assertTrue(resultado.mensaje().contains("hospedaje"));
            assertTrue(resultado.mensaje().contains("400.00 USD"));
        }

        @Test
        @DisplayName("borde: pasarse por un centavo también bloquea")
        void superaPorUnCentavo_bloquea() {
            ResultadoControlCategoria resultado =
                    control.evaluar(gasto("transporte", "0.01"), CUPO, new BigDecimal("400.00"));

            assertTrue(resultado.bloqueante());
        }
    }

    // ------------------------------------------------------------ ConcreteProduct flexible

    @Nested
    @DisplayName("ControlCategoriaFlexible")
    class Flexible {

        private final ControlCategoriaFlexible control = new ControlCategoriaFlexible();

        @Test
        @DisplayName("gasto que queda dentro del cupo no tiene problemas")
        void dentroDelCupo_sinProblemas() {
            ResultadoControlCategoria resultado =
                    control.evaluar(gasto("imprevistos", "50"), CUPO, BigDecimal.ZERO);

            assertFalse(resultado.excedeCupo());
            assertNull(resultado.mensaje());
        }

        @Test
        @DisplayName("pasarse del cupo excede pero NO bloquea: solo avisa")
        void superaElCupo_soloAvisa() {
            ResultadoControlCategoria resultado =
                    control.evaluar(gasto("imprevistos", "150"), new BigDecimal("100.00"), BigDecimal.ZERO);

            assertTrue(resultado.excedeCupo());
            assertFalse(resultado.bloqueante());
            assertTrue(resultado.mensaje().contains("imprevistos"));
        }

        @Test
        @DisplayName("el mensaje usa la categoría del gasto, no un texto fijo")
        void mensajeUsaLaCategoriaDelGasto() {
            ResultadoControlCategoria resultado =
                    control.evaluar(gasto("actividades", "500"), CUPO, BigDecimal.ZERO);

            assertTrue(resultado.mensaje().contains("'actividades'"));
        }
    }

    // ------------------------------------------------------------ Creators

    @Nested
    @DisplayName("ConcreteCreators usados como ControlCategoriaCreador")
    class Creadores {

        @Test
        @DisplayName("CreadorControlEstricto fabrica un ControlCategoriaEstricto")
        void creadorEstricto_fabricaEstricto() {
            assertEquals(ControlCategoriaEstricto.class,
                    new CreadorControlEstricto().crearControl().getClass());
        }

        @Test
        @DisplayName("CreadorControlFlexible fabrica un ControlCategoriaFlexible")
        void creadorFlexible_fabricaFlexible() {
            assertEquals(ControlCategoriaFlexible.class,
                    new CreadorControlFlexible().crearControl().getClass());
        }

        @Test
        @DisplayName("evaluarGasto con el creador estricto bloquea el exceso")
        void evaluarGasto_creadorEstricto_bloquea() {
            ControlCategoriaCreador creador = new CreadorControlEstricto();

            ResultadoControlCategoria resultado =
                    creador.evaluarGasto(gasto("alimentacion", "500"), CUPO, BigDecimal.ZERO);

            assertTrue(resultado.bloqueante());
        }

        @Test
        @DisplayName("evaluarGasto con el creador flexible solo avisa el exceso")
        void evaluarGasto_creadorFlexible_soloAvisa() {
            ControlCategoriaCreador creador = new CreadorControlFlexible();

            ResultadoControlCategoria resultado =
                    creador.evaluarGasto(gasto("imprevistos", "500"), CUPO, BigDecimal.ZERO);

            assertTrue(resultado.excedeCupo());
            assertFalse(resultado.bloqueante());
        }

        @Test
        @DisplayName("el mismo gasto da resultados distintos según el creador (polimorfismo)")
        void mismoGasto_distintoResultadoSegunCreador() {
            Gasto gasto = gasto("hospedaje", "500");

            boolean bloqueaEstricto = new CreadorControlEstricto()
                    .evaluarGasto(gasto, CUPO, BigDecimal.ZERO).bloqueante();
            boolean bloqueaFlexible = new CreadorControlFlexible()
                    .evaluarGasto(gasto, CUPO, BigDecimal.ZERO).bloqueante();

            assertTrue(bloqueaEstricto);
            assertFalse(bloqueaFlexible);
        }
    }
}

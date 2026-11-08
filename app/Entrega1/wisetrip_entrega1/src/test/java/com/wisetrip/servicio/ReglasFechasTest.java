package com.wisetrip.servicio;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.wisetrip.modelo.FechasViaje;

/**
 * Pruebas unitarias de cada regla de la cadena de validación de fechas
 * (patrón Chain of Responsibility), probadas por separado, y del
 * encadenamiento en ReglaValidacionFechas.
 *
 * FechasServicioTest prueba la cadena completa desde el servicio; aquí se
 * revisa cada eslabón aislado. "Hoy" se calcula con la misma zona horaria
 * que usan las reglas, para que las pruebas no caduquen (Repeatable).
 */
@DisplayName("Reglas de validación de fechas")
class ReglasFechasTest {

    private LocalDate hoy;

    @BeforeEach
    void setUp() {
        hoy = LocalDate.now(ReglaValidacionFechas.ZONA_HORARIA);
    }

    private static ContextoValidacionFechas contexto(String inicio, String fin) {
        FechasViaje fechas = new FechasViaje();
        fechas.setFechaInicio(inicio);
        fechas.setFechaFin(fin);
        return new ContextoValidacionFechas(fechas);
    }

    // ------------------------------------------------------------ ReglaFechaInicio

    @Nested
    @DisplayName("ReglaFechaInicio")
    class FechaInicio {

        private final ReglaFechaInicio regla = new ReglaFechaInicio();

        @Test
        @DisplayName("fecha futura válida se guarda en el contexto sin errores")
        void fechaFutura_seGuarda() {
            ContextoValidacionFechas ctx = contexto(hoy.plusDays(3).toString(), null);

            regla.validar(ctx);

            assertEquals(hoy.plusDays(3), ctx.getInicio());
            assertTrue(ctx.getErrores().isEmpty());
        }

        @Test
        @DisplayName("borde: hoy mismo es válido")
        void hoy_esValido() {
            ContextoValidacionFechas ctx = contexto(hoy.toString(), null);

            regla.validar(ctx);

            assertEquals(hoy, ctx.getInicio());
            assertFalse(ctx.getErrores().containsKey("fechaInicio"));
        }

        @Test
        @DisplayName("vacía pide seleccionar la fecha")
        void vacia_error() {
            ContextoValidacionFechas ctx = contexto("  ", null);

            regla.validar(ctx);

            assertEquals("Selecciona la fecha de inicio de tu viaje.", ctx.getErrores().get("fechaInicio"));
            assertNull(ctx.getInicio());
        }

        @Test
        @DisplayName("borde: ayer se rechaza por estar en el pasado")
        void ayer_error() {
            ContextoValidacionFechas ctx = contexto(hoy.minusDays(1).toString(), null);

            regla.validar(ctx);

            assertEquals("La fecha de inicio no puede ser anterior a hoy.", ctx.getErrores().get("fechaInicio"));
            assertNull(ctx.getInicio());
        }

        @Test
        @DisplayName("texto que no es fecha pide una fecha válida")
        void formatoInvalido_error() {
            ContextoValidacionFechas ctx = contexto("abc", null);

            regla.validar(ctx);

            assertEquals("Ingresa una fecha válida.", ctx.getErrores().get("fechaInicio"));
        }
    }

    // ------------------------------------------------------------ ReglaFechaFin

    @Nested
    @DisplayName("ReglaFechaFin")
    class FechaFin {

        private final ReglaFechaFin regla = new ReglaFechaFin();

        @Test
        @DisplayName("fecha válida se guarda en el contexto sin errores")
        void fechaValida_seGuarda() {
            ContextoValidacionFechas ctx = contexto(null, "2027-01-15");

            regla.validar(ctx);

            assertEquals(LocalDate.of(2027, 1, 15), ctx.getFin());
            assertTrue(ctx.getErrores().isEmpty());
        }

        @Test
        @DisplayName("null pide seleccionar la fecha de regreso")
        void nula_error() {
            ContextoValidacionFechas ctx = contexto(null, null);

            regla.validar(ctx);

            assertEquals("Selecciona la fecha de regreso.", ctx.getErrores().get("fechaFin"));
        }

        @Test
        @DisplayName("borde: 30 de febrero no existe y se rechaza")
        void fechaInexistente_error() {
            ContextoValidacionFechas ctx = contexto(null, "2027-02-30");

            regla.validar(ctx);

            assertEquals("Ingresa una fecha válida.", ctx.getErrores().get("fechaFin"));
            assertNull(ctx.getFin());
        }
    }

    // ------------------------------------------------------------ ReglaRelacionFechas

    @Nested
    @DisplayName("ReglaRelacionFechas")
    class RelacionFechas {

        private final ReglaRelacionFechas regla = new ReglaRelacionFechas();

        private ContextoValidacionFechas conRango(LocalDate inicio, LocalDate fin) {
            ContextoValidacionFechas ctx = contexto(null, null);
            ctx.setInicio(inicio);
            ctx.setFin(fin);
            return ctx;
        }

        @Test
        @DisplayName("si falta alguna fecha no compara ni agrega errores")
        void faltaUnaFecha_noCompara() {
            ContextoValidacionFechas ctx = conRango(hoy, null);

            regla.validar(ctx);

            assertTrue(ctx.getErrores().isEmpty());
        }

        @Test
        @DisplayName("fin antes del inicio se rechaza")
        void finAntesDelInicio_error() {
            ContextoValidacionFechas ctx = conRango(hoy.plusDays(5), hoy.plusDays(2));

            regla.validar(ctx);

            assertTrue(ctx.getErrores().get("fechaFin").contains("posterior"));
        }

        @Test
        @DisplayName("borde: 60 días contando inicio y fin es válido")
        void sesentaDias_valido() {
            ContextoValidacionFechas ctx = conRango(hoy, hoy.plusDays(59));

            regla.validar(ctx);

            assertTrue(ctx.getErrores().isEmpty());
        }

        @Test
        @DisplayName("borde: 61 días supera el máximo")
        void sesentaYUnDias_error() {
            ContextoValidacionFechas ctx = conRango(hoy, hoy.plusDays(60));

            regla.validar(ctx);

            assertTrue(ctx.getErrores().get("fechaFin").contains("60 días"));
        }

        @Test
        @DisplayName("si se cruza con otro viaje lo avisa")
        void cruceConOtroViaje_error() {
            ContextoValidacionFechas ctx = conRango(hoy, hoy.plusDays(3));
            ctx.setConsultaCruces(() -> true);

            regla.validar(ctx);

            assertTrue(ctx.getErrores().get("fechaFin").contains("cruzan"));
        }

        @Test
        @DisplayName("si el rango ya es inválido no consulta los cruces")
        void rangoInvalido_noConsultaCruces() {
            ContextoValidacionFechas ctx = conRango(hoy.plusDays(5), hoy.plusDays(1));
            boolean[] consultado = {false};
            ctx.setConsultaCruces(() -> {
                consultado[0] = true;
                return true;
            });

            regla.validar(ctx);

            assertFalse(consultado[0]);
        }
    }

    // ------------------------------------------------------------ Encadenamiento

    @Nested
    @DisplayName("Cadena (ReglaValidacionFechas)")
    class Cadena {

        @Test
        @DisplayName("siguiente() devuelve la regla encadenada para seguir encadenando")
        void siguiente_devuelveLaReglaEncadenada() {
            ReglaFechaInicio inicio = new ReglaFechaInicio();
            ReglaFechaFin fin = new ReglaFechaFin();

            assertSame(fin, inicio.siguiente(fin));
        }

        @Test
        @DisplayName("cada regla se ejecuta en el orden en que se encadenó")
        void reglasSeEjecutanEnOrden() {
            List<String> orden = new ArrayList<>();
            ReglaValidacionFechas primera = reglaQueAnota("primera", orden);
            primera.siguiente(reglaQueAnota("segunda", orden))
                   .siguiente(reglaQueAnota("tercera", orden));

            primera.validar(contexto(null, null));

            assertEquals(List.of("primera", "segunda", "tercera"), orden);
        }

        @Test
        @DisplayName("aunque la primera regla falle, la cadena sigue y reporta todos los errores")
        void primeraFalla_laCadenaSigue() {
            ReglaValidacionFechas cadena = new ReglaFechaInicio();
            cadena.siguiente(new ReglaFechaFin()).siguiente(new ReglaRelacionFechas());
            ContextoValidacionFechas ctx = contexto("", "no-es-fecha");

            cadena.validar(ctx);

            assertEquals(2, ctx.getErrores().size());
            assertEquals("Ingresa una fecha válida.", ctx.getErrores().get("fechaFin"));
        }

        /** Regla de prueba que solo anota su nombre cuando se ejecuta. */
        private ReglaValidacionFechas reglaQueAnota(String nombre, List<String> orden) {
            return new ReglaValidacionFechas() {
                @Override
                protected void validarPropio(ContextoValidacionFechas contexto) {
                    orden.add(nombre);
                }
            };
        }
    }
}

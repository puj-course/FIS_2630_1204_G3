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

    private final ReglaFechaInicio reglaInicio = new ReglaFechaInicio();
    private final ReglaFechaFin reglaFin = new ReglaFechaFin();
    private final ReglaRelacionFechas reglaRelacion = new ReglaRelacionFechas();

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

    private static ContextoValidacionFechas conRango(LocalDate inicio, LocalDate fin) {
        ContextoValidacionFechas ctx = contexto(null, null);
        ctx.setInicio(inicio);
        ctx.setFin(fin);
        return ctx;
    }

    /** Regla de prueba que solo anota su nombre cuando se ejecuta. */
    private static ReglaValidacionFechas reglaQueAnota(String nombre, List<String> orden) {
        return new ReglaValidacionFechas() {
            @Override
            protected void validarPropio(ContextoValidacionFechas contexto) {
                orden.add(nombre);
            }
        };
    }

    // ------------------------------------------------------------ ReglaFechaInicio

    @Test
    @DisplayName("ReglaFechaInicio: fecha futura válida se guarda en el contexto sin errores")
    void inicio_fechaFutura_seGuarda() {
        ContextoValidacionFechas ctx = contexto(hoy.plusDays(3).toString(), null);

        reglaInicio.validar(ctx);

        assertEquals(hoy.plusDays(3), ctx.getInicio());
        assertTrue(ctx.getErrores().isEmpty());
    }

    @Test
    @DisplayName("ReglaFechaInicio, borde: hoy mismo es válido")
    void inicio_hoy_esValido() {
        ContextoValidacionFechas ctx = contexto(hoy.toString(), null);

        reglaInicio.validar(ctx);

        assertEquals(hoy, ctx.getInicio());
        assertFalse(ctx.getErrores().containsKey("fechaInicio"));
    }

    @Test
    @DisplayName("ReglaFechaInicio: vacía pide seleccionar la fecha")
    void inicio_vacia_error() {
        ContextoValidacionFechas ctx = contexto("  ", null);

        reglaInicio.validar(ctx);

        assertEquals("Selecciona la fecha de inicio de tu viaje.", ctx.getErrores().get("fechaInicio"));
        assertNull(ctx.getInicio());
    }

    @Test
    @DisplayName("ReglaFechaInicio, borde: ayer se rechaza por estar en el pasado")
    void inicio_ayer_error() {
        ContextoValidacionFechas ctx = contexto(hoy.minusDays(1).toString(), null);

        reglaInicio.validar(ctx);

        assertEquals("La fecha de inicio no puede ser anterior a hoy.", ctx.getErrores().get("fechaInicio"));
        assertNull(ctx.getInicio());
    }

    @Test
    @DisplayName("ReglaFechaInicio: texto que no es fecha pide una fecha válida")
    void inicio_formatoInvalido_error() {
        ContextoValidacionFechas ctx = contexto("abc", null);

        reglaInicio.validar(ctx);

        assertEquals("Ingresa una fecha válida.", ctx.getErrores().get("fechaInicio"));
    }

    // ------------------------------------------------------------ ReglaFechaFin

    @Test
    @DisplayName("ReglaFechaFin: fecha válida se guarda en el contexto sin errores")
    void fin_fechaValida_seGuarda() {
        ContextoValidacionFechas ctx = contexto(null, "2027-01-15");

        reglaFin.validar(ctx);

        assertEquals(LocalDate.of(2027, 1, 15), ctx.getFin());
        assertTrue(ctx.getErrores().isEmpty());
    }

    @Test
    @DisplayName("ReglaFechaFin: null pide seleccionar la fecha de regreso")
    void fin_nula_error() {
        ContextoValidacionFechas ctx = contexto(null, null);

        reglaFin.validar(ctx);

        assertEquals("Selecciona la fecha de regreso.", ctx.getErrores().get("fechaFin"));
    }

    @Test
    @DisplayName("ReglaFechaFin, borde: 30 de febrero no existe y se rechaza")
    void fin_fechaInexistente_error() {
        ContextoValidacionFechas ctx = contexto(null, "2027-02-30");

        reglaFin.validar(ctx);

        assertEquals("Ingresa una fecha válida.", ctx.getErrores().get("fechaFin"));
        assertNull(ctx.getFin());
    }

    // ------------------------------------------------------------ ReglaRelacionFechas

    @Test
    @DisplayName("ReglaRelacionFechas: si falta alguna fecha no compara ni agrega errores")
    void relacion_faltaUnaFecha_noCompara() {
        ContextoValidacionFechas ctx = conRango(hoy, null);

        reglaRelacion.validar(ctx);

        assertTrue(ctx.getErrores().isEmpty());
    }

    @Test
    @DisplayName("ReglaRelacionFechas: fin antes del inicio se rechaza")
    void relacion_finAntesDelInicio_error() {
        ContextoValidacionFechas ctx = conRango(hoy.plusDays(5), hoy.plusDays(2));

        reglaRelacion.validar(ctx);

        assertTrue(ctx.getErrores().get("fechaFin").contains("posterior"));
    }

    @Test
    @DisplayName("ReglaRelacionFechas, borde: 60 días contando inicio y fin es válido")
    void relacion_sesentaDias_valido() {
        ContextoValidacionFechas ctx = conRango(hoy, hoy.plusDays(59));

        reglaRelacion.validar(ctx);

        assertTrue(ctx.getErrores().isEmpty());
    }

    @Test
    @DisplayName("ReglaRelacionFechas, borde: 61 días supera el máximo")
    void relacion_sesentaYUnDias_error() {
        ContextoValidacionFechas ctx = conRango(hoy, hoy.plusDays(60));

        reglaRelacion.validar(ctx);

        assertTrue(ctx.getErrores().get("fechaFin").contains("60 días"));
    }

    @Test
    @DisplayName("ReglaRelacionFechas: si se cruza con otro viaje lo avisa")
    void relacion_cruceConOtroViaje_error() {
        ContextoValidacionFechas ctx = conRango(hoy, hoy.plusDays(3));
        ctx.setConsultaCruces(() -> true);

        reglaRelacion.validar(ctx);

        assertTrue(ctx.getErrores().get("fechaFin").contains("cruzan"));
    }

    @Test
    @DisplayName("ReglaRelacionFechas: si el rango ya es inválido no consulta los cruces")
    void relacion_rangoInvalido_noConsultaCruces() {
        ContextoValidacionFechas ctx = conRango(hoy.plusDays(5), hoy.plusDays(1));
        boolean[] consultado = {false};
        ctx.setConsultaCruces(() -> {
            consultado[0] = true;
            return true;
        });

        reglaRelacion.validar(ctx);

        assertFalse(consultado[0]);
    }

    // ------------------------------------------------------------ Encadenamiento

    @Test
    @DisplayName("Cadena: siguiente() devuelve la regla encadenada para seguir encadenando")
    void cadena_siguiente_devuelveLaReglaEncadenada() {
        ReglaFechaInicio inicio = new ReglaFechaInicio();
        ReglaFechaFin fin = new ReglaFechaFin();

        assertSame(fin, inicio.siguiente(fin));
    }

    @Test
    @DisplayName("Cadena: cada regla se ejecuta en el orden en que se encadenó")
    void cadena_reglasSeEjecutanEnOrden() {
        List<String> orden = new ArrayList<>();
        ReglaValidacionFechas primera = reglaQueAnota("primera", orden);
        primera.siguiente(reglaQueAnota("segunda", orden))
               .siguiente(reglaQueAnota("tercera", orden));

        primera.validar(contexto(null, null));

        assertEquals(List.of("primera", "segunda", "tercera"), orden);
    }

    @Test
    @DisplayName("Cadena: aunque la primera regla falle, la cadena sigue y reporta todos los errores")
    void cadena_primeraFalla_laCadenaSigue() {
        ReglaValidacionFechas cadena = new ReglaFechaInicio();
        cadena.siguiente(new ReglaFechaFin()).siguiente(new ReglaRelacionFechas());
        ContextoValidacionFechas ctx = contexto("", "no-es-fecha");

        cadena.validar(ctx);

        assertEquals(2, ctx.getErrores().size());
        assertEquals("Ingresa una fecha válida.", ctx.getErrores().get("fechaFin"));
    }
}
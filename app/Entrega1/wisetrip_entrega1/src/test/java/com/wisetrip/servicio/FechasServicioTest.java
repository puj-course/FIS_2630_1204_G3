package com.wisetrip.servicio;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.wisetrip.modelo.FechasViaje;
import com.wisetrip.negocio.IRepositorioViajes;

/**
 * Pruebas unitarias de FechasServicio (CP34 - CP47).
 * Las fechas se calculan relativas a "hoy" para que las pruebas no caduquen con el tiempo
 * (principio Repeatable de F.I.R.S.T). El repositorio de viajes se simula con Mockito.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("FechasServicio")
class FechasServicioTest {

    private static final int ID_USUARIO = 7;
    private static final int ID_VIAJE_ACTUAL = 3;

    @Mock
    private IRepositorioViajes viajes;

    private FechasServicio servicio;
    private LocalDate hoy;

    @BeforeEach
    void setUp() {
        servicio = new FechasServicio(viajes);
        hoy = LocalDate.now(ReglaValidacionFechas.ZONA_HORARIA);
    }

    private static FechasViaje fechas(LocalDate inicio, LocalDate fin) {
        FechasViaje f = new FechasViaje();
        f.setFechaInicio(inicio == null ? null : inicio.toString());
        f.setFechaFin(fin == null ? null : fin.toString());
        return f;
    }

    // ---------------------------------------------------------------- validarFechas

    @Test
    @DisplayName("CP34 validarFechas: rango futuro válido no genera errores")
    void cp34_validarFechas_rangoValido_sinErrores() {
        Map<String, String> errores = servicio.validarFechas(fechas(hoy.plusDays(1), hoy.plusDays(5)));

        assertTrue(errores.isEmpty());
    }

    @Test
    @DisplayName("CP35 validarFechas: sin fechas devuelve errores en inicio y fin")
    void cp35_validarFechas_sinFechas_dosErrores() {
        Map<String, String> errores = servicio.validarFechas(new FechasViaje());

        assertEquals(2, errores.size());
        assertTrue(errores.containsKey("fechaInicio"));
        assertTrue(errores.containsKey("fechaFin"));
    }

    @Test
    @DisplayName("CP36 validarFechas: fecha de inicio en el pasado devuelve error 'fechaInicio'")
    void cp36_validarFechas_inicioEnElPasado_error() {
        Map<String, String> errores = servicio.validarFechas(fechas(hoy.minusDays(1), hoy.plusDays(3)));

        assertTrue(errores.containsKey("fechaInicio"));
    }

    @Test
    @DisplayName("CP37 validarFechas: formato de fecha inválido devuelve error 'fechaInicio'")
    void cp37_validarFechas_formatoInvalido_error() {
        FechasViaje f = fechas(null, hoy.plusDays(3));
        f.setFechaInicio("2026-13-45");

        Map<String, String> errores = servicio.validarFechas(f);

        assertTrue(errores.containsKey("fechaInicio"));
    }

    @Test
    @DisplayName("CP38 validarFechas: borde, iniciar hoy mismo es válido")
    void cp38_validarFechas_inicioHoy_valido() {
        Map<String, String> errores = servicio.validarFechas(fechas(hoy, hoy.plusDays(1)));

        assertTrue(errores.isEmpty());
    }

    @Test
    @DisplayName("CP39 validarFechas: borde, fin igual al inicio se rechaza (debe ser posterior)")
    void cp39_validarFechas_finIgualAlInicio_error() {
        Map<String, String> errores = servicio.validarFechas(fechas(hoy.plusDays(1), hoy.plusDays(1)));

        assertTrue(errores.get("fechaFin").contains("posterior"));
    }

    @Test
    @DisplayName("CP40 validarFechas: borde, viaje de exactamente 60 días es válido")
    void cp40_validarFechas_sesentaDias_valido() {
        Map<String, String> errores = servicio.validarFechas(fechas(hoy, hoy.plusDays(59)));

        assertTrue(errores.isEmpty());
    }

    @Test
    @DisplayName("CP41 validarFechas: borde, viaje de 61 días supera el máximo")
    void cp41_validarFechas_sesentaYUnDias_error() {
        Map<String, String> errores = servicio.validarFechas(fechas(hoy, hoy.plusDays(60)));

        assertTrue(errores.get("fechaFin").contains("60"));
    }

    @Test
    @DisplayName("CP42 validarFechas (con usuario): fechas que se cruzan con otro viaje devuelven error")
    void cp42_validarFechas_cruceConOtroViaje_error() {
        when(viajes.hayCruceFechas(eq(ID_USUARIO), eq(ID_VIAJE_ACTUAL), any(FechasViaje.class))).thenReturn(true);

        Map<String, String> errores = servicio.validarFechas(
                fechas(hoy.plusDays(1), hoy.plusDays(5)), ID_USUARIO, ID_VIAJE_ACTUAL);

        assertTrue(errores.get("fechaFin").contains("cruzan"));
    }

    @Test
    @DisplayName("CP43 validarFechas (con usuario): sin cruce consulta el repositorio y no hay errores")
    void cp43_validarFechas_sinCruce_sinErrores() {
        FechasViaje rango = fechas(hoy.plusDays(1), hoy.plusDays(5));
        when(viajes.hayCruceFechas(ID_USUARIO, ID_VIAJE_ACTUAL, rango)).thenReturn(false);

        Map<String, String> errores = servicio.validarFechas(rango, ID_USUARIO, ID_VIAJE_ACTUAL);

        assertTrue(errores.isEmpty());
        verify(viajes).hayCruceFechas(ID_USUARIO, ID_VIAJE_ACTUAL, rango);
    }

    // ---------------------------------------------------------------- calcularDuracion

    @Test
    @DisplayName("CP44 calcularDuracion: cuenta inicio y fin (1 al 5 de diciembre = 5 días)")
    void cp44_calcularDuracion_incluyeExtremos() {
        FechasViaje f = fechas(LocalDate.of(2026, 12, 1), LocalDate.of(2026, 12, 5));

        assertEquals(5, servicio.calcularDuracion(f));
    }

    @Test
    @DisplayName("CP45 calcularDuracion: fechas vacías devuelven 0")
    void cp45_calcularDuracion_sinFechas_cero() {
        assertEquals(0, servicio.calcularDuracion(new FechasViaje()));
    }

    @Test
    @DisplayName("CP46 calcularDuracion: borde, inicio igual al fin devuelve 0 (hallazgo: un viaje de 1 día cuenta como 0)")
    void cp46_calcularDuracion_mismoDia_cero() {
        LocalDate dia = LocalDate.of(2026, 12, 1);

        assertEquals(0, servicio.calcularDuracion(fechas(dia, dia)));
    }

    // ---------------------------------------------------------------- hoy

    @Test
    @DisplayName("CP47 hoy: devuelve la fecha actual en formato yyyy-MM-dd")
    void cp47_hoy_formatoIso() {
        LocalDate antes = LocalDate.now(ReglaValidacionFechas.ZONA_HORARIA);
        String valor = servicio.hoy();
        LocalDate despues = LocalDate.now(ReglaValidacionFechas.ZONA_HORARIA);

        LocalDate parseada = LocalDate.parse(valor);
        assertTrue(!parseada.isBefore(antes) && !parseada.isAfter(despues));
    }
}
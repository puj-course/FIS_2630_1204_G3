package com.wisetrip.servicio;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.wisetrip.modelo.RepartoPresupuesto;

/**
 * Pruebas unitarias de RepartoServicio (HU#69 y HU#79).
 *
 * Cada prueba arma un RepartoPresupuesto en memoria con sus setters (así se
 * pueden probar valores negativos, que asignar() rechaza) y revisa los
 * mensajes que el servicio prepara para la vista.
 */
@DisplayName("RepartoServicio")
class RepartoServicioTest {

    private RepartoServicio servicio;
    private RepartoPresupuesto reparto;

    @BeforeEach
    void setUp() {
        servicio = new RepartoServicio();
        reparto = new RepartoPresupuesto(); // arranca con el sugerido 35/25/20/15/5
    }

    @Test
    @DisplayName("el reparto sugerido (suma 100) no tiene errores")
    void repartoSugerido_sinErrores() {
        Map<String, String> errores = servicio.validarReparto(reparto);

        assertTrue(errores.isEmpty());
    }

    @Test
    @DisplayName("un porcentaje negativo devuelve solo el error 'general' y se detiene ahí")
    void porcentajeNegativo_errorGeneral() {
        reparto.setTransporte(-5);
        reparto.setHospedaje(0);
        reparto.setAlimentacion(0);

        Map<String, String> errores = servicio.validarReparto(reparto);

        assertEquals(1, errores.size());
        assertEquals("Ningún porcentaje puede ser negativo.", errores.get("general"));
    }

    @Test
    @DisplayName("si suma más de 100 dice por cuánto se pasa")
    void sumaMasDeCien_dicePorCuantoSePasa() {
        reparto.setHospedaje(60); // 60+25+20+15+5 = 125

        Map<String, String> errores = servicio.validarReparto(reparto);

        assertTrue(errores.get("general").contains("pasando por 25%"));
    }

    @Test
    @DisplayName("si suma menos de 100 dice cuánto falta")
    void sumaMenosDeCien_diceCuantoFalta() {
        reparto.setHospedaje(0); // 0+25+20+15+5 = 65

        Map<String, String> errores = servicio.validarReparto(reparto);

        assertEquals("Te faltan 35% por repartir.", errores.get("general"));
    }

    @Test
    @DisplayName("sin hospedaje ni alimentación devuelve el error 'basicos'")
    void sinHospedajeNiAlimentacion_errorBasicos() {
        reparto.setHospedaje(0);
        reparto.setAlimentacion(0);
        reparto.setTransporte(60); // 0+0+60+15+5 = 80 -> también falta

        Map<String, String> errores = servicio.validarReparto(reparto);

        assertTrue(errores.containsKey("basicos"));
        assertTrue(errores.containsKey("general"));
    }

    @Test
    @DisplayName("borde: con solo alimentación cubre lo básico")
    void soloAlimentacion_cubreBasicos() {
        reparto.setHospedaje(0);
        reparto.setAlimentacion(60); // 0+60+20+15+5 = 100

        Map<String, String> errores = servicio.validarReparto(reparto);

        assertFalse(errores.containsKey("basicos"));
        assertTrue(errores.isEmpty());
    }

    @Test
    @DisplayName("borde: todo en hospedaje (100/0/0/0/0) es válido")
    void todoEnHospedaje_valido() {
        reparto.setHospedaje(100);
        reparto.setAlimentacion(0);
        reparto.setTransporte(0);
        reparto.setActividades(0);
        reparto.setImprevistos(0);

        assertTrue(servicio.validarReparto(reparto).isEmpty());
    }
}

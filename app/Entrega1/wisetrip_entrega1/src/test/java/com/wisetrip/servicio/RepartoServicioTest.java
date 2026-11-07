package com.wisetrip.servicio;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.wisetrip.modelo.RepartoPresupuesto;

@DisplayName("RepartoServicio")
class RepartoServicioTest {

    private RepartoServicio servicio;
    private RepartoPresupuesto reparto;

    @BeforeEach
    void setUp() {
        servicio = new RepartoServicio();
        reparto = new RepartoPresupuesto();
    }

    @Test
    @DisplayName("el reparto sugerido no tiene errores")
    void validarReparto_sugerido_sinErrores() {
        assertTrue(servicio.validarReparto(reparto).isEmpty());
    }

    @Test
    @DisplayName("un porcentaje negativo se rechaza")
    void validarReparto_negativo_error() {
        reparto.setHospedaje(-5);

        Map<String, String> errores = servicio.validarReparto(reparto);

        assertEquals("Ningún porcentaje puede ser negativo.", errores.get("general"));
        assertEquals(1, errores.size());
    }

    @Test
    @DisplayName("si se pasa de 100 indica cuánto se pasó")
    void validarReparto_excede_indicaElExceso() {
        reparto.setHospedaje(50);   // total 115

        Map<String, String> errores = servicio.validarReparto(reparto);

        assertEquals("Te estás pasando por 15%. Ajusta el reparto para que sume 100%.", errores.get("general"));
    }

    @Test
    @DisplayName("si no llega a 100 indica cuánto falta")
    void validarReparto_incompleto_indicaLoQueFalta() {
        reparto.setHospedaje(25);   // total 90

        Map<String, String> errores = servicio.validarReparto(reparto);

        assertEquals("Te faltan 10% por repartir.", errores.get("general"));
    }

    @Test
    @DisplayName("si no deja nada para hospedaje ni alimentación pide ajustarlo")
    void validarReparto_sinBasicos_error() {
        reparto.setHospedaje(0);
        reparto.setAlimentacion(0);
        reparto.setTransporte(60);
        reparto.setActividades(30);
        reparto.setImprevistos(10);

        Map<String, String> errores = servicio.validarReparto(reparto);

        assertEquals("Deja algo para hospedaje o alimentación.", errores.get("basicos"));
        assertEquals(1, errores.size());
    }

    @Test
    @DisplayName("puede devolver el error general y el de básicos a la vez")
    void validarReparto_variosErrores() {
        reparto.setHospedaje(0);
        reparto.setAlimentacion(0);   // total 40 y sin básicos

        Map<String, String> errores = servicio.validarReparto(reparto);

        assertEquals(2, errores.size());
        assertTrue(errores.containsKey("general"));
        assertTrue(errores.containsKey("basicos"));
    }
}

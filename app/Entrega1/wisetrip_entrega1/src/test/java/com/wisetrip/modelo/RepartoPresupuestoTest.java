package com.wisetrip.modelo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("RepartoPresupuesto")
class RepartoPresupuestoTest {

    private RepartoPresupuesto reparto;

    @BeforeEach
    void setUp() {
        reparto = new RepartoPresupuesto();
    }

    @Test
    @DisplayName("empieza con el reparto sugerido, que suma 100")
    void inicio_repartoSugerido() {
        assertEquals(35, reparto.getHospedaje());
        assertEquals(25, reparto.getAlimentacion());
        assertEquals(20, reparto.getTransporte());
        assertEquals(15, reparto.getActividades());
        assertEquals(5, reparto.getImprevistos());
        assertEquals(100, reparto.getTotal());
        assertTrue(reparto.estaCompleto());
        assertTrue(reparto.esValido());
    }

    @Test
    @DisplayName("porcentajeDe devuelve el porcentaje de cada categoría")
    void porcentajeDe_cadaCategoria() {
        assertEquals(35, reparto.porcentajeDe("hospedaje"));
        assertEquals(25, reparto.porcentajeDe("alimentacion"));
        assertEquals(20, reparto.porcentajeDe("transporte"));
        assertEquals(15, reparto.porcentajeDe("actividades"));
        assertEquals(5, reparto.porcentajeDe("imprevistos"));
    }

    @Test
    @DisplayName("porcentajeDe con una categoría desconocida lanza IllegalArgumentException")
    void porcentajeDe_categoriaDesconocida_lanzaExcepcion() {
        assertThrows(IllegalArgumentException.class, () -> reparto.porcentajeDe("souvenirs"));
    }

    @Test
    @DisplayName("asignar cambia el porcentaje de la categoría")
    void asignar_cambiaElPorcentaje() {
        reparto.asignar("hospedaje", 40);
        reparto.asignar("alimentacion", 30);
        reparto.asignar("transporte", 10);
        reparto.asignar("actividades", 10);
        reparto.asignar("imprevistos", 10);

        assertEquals(40, reparto.getHospedaje());
        assertEquals(30, reparto.getAlimentacion());
        assertEquals(10, reparto.getTransporte());
        assertEquals(10, reparto.getActividades());
        assertEquals(10, reparto.getImprevistos());
        assertEquals(100, reparto.getTotal());
    }

    @Test
    @DisplayName("asignar: borde, 0 y 100 son válidos")
    void asignar_bordesValidos() {
        reparto.asignar("hospedaje", 0);
        reparto.asignar("alimentacion", 100);

        assertEquals(0, reparto.getHospedaje());
        assertEquals(100, reparto.getAlimentacion());
    }

    @Test
    @DisplayName("asignar: -1 y 101 lanzan IllegalArgumentException")
    void asignar_fueraDeRango_lanzaExcepcion() {
        assertThrows(IllegalArgumentException.class, () -> reparto.asignar("hospedaje", -1));
        assertThrows(IllegalArgumentException.class, () -> reparto.asignar("hospedaje", 101));
    }

    @Test
    @DisplayName("asignar con una categoría desconocida lanza IllegalArgumentException")
    void asignar_categoriaDesconocida_lanzaExcepcion() {
        assertThrows(IllegalArgumentException.class, () -> reparto.asignar("souvenirs", 10));
    }

    @Test
    @DisplayName("restaurarSugerido devuelve los porcentajes originales")
    void restaurarSugerido_vuelveAlSugerido() {
        reparto.setHospedaje(1);
        reparto.setAlimentacion(2);
        reparto.setTransporte(3);
        reparto.setActividades(4);
        reparto.setImprevistos(5);

        reparto.restaurarSugerido();

        assertEquals(100, reparto.getTotal());
        assertEquals(RepartoPresupuesto.SUGERIDO_HOSPEDAJE, reparto.getHospedaje());
    }

    @Test
    @DisplayName("esValido es false si algún porcentaje es negativo")
    void esValido_conNegativo_false() {
        reparto.setHospedaje(-5);
        reparto.setAlimentacion(65);

        assertTrue(reparto.tienePorcentajesNegativos());
        assertFalse(reparto.esValido());
    }

    @Test
    @DisplayName("esValido es false si no suma 100")
    void esValido_incompleto_false() {
        reparto.setHospedaje(10);

        assertFalse(reparto.estaCompleto());
        assertFalse(reparto.esValido());
    }

    @Test
    @DisplayName("esValido es false si no deja nada para hospedaje ni alimentación")
    void esValido_sinBasicos_false() {
        reparto.setHospedaje(0);
        reparto.setAlimentacion(0);
        reparto.setTransporte(60);
        reparto.setActividades(30);
        reparto.setImprevistos(10);

        assertEquals(100, reparto.getTotal());
        assertFalse(reparto.cubreLosBasicos());
        assertFalse(reparto.esValido());
    }

    @Test
    @DisplayName("porcentajeDisponible y porcentajeExcedido se calculan contra 100")
    void disponibleYExcedido() {
        reparto.setHospedaje(25);   // total 90
        assertEquals(10, reparto.porcentajeDisponible());
        assertEquals(0, reparto.porcentajeExcedido());
        assertFalse(reparto.excedeElMaximo());

        reparto.setHospedaje(50);   // total 115
        assertEquals(0, reparto.porcentajeDisponible());
        assertEquals(15, reparto.porcentajeExcedido());
        assertTrue(reparto.excedeElMaximo());
    }

    @Test
    @DisplayName("montoDe y distribuir reparten el presupuesto según los porcentajes")
    void montoDeYDistribuir() {
        assertEquals(350.0, reparto.montoDe("hospedaje", 1000.0), 0.0001);

        Map<String, Double> montos = reparto.distribuir(1000.0);

        assertEquals(5, montos.size());
        assertEquals(250.0, montos.get("alimentacion"), 0.0001);
        assertEquals(50.0, montos.get("imprevistos"), 0.0001);
    }
}

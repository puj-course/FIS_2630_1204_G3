package com.wisetrip.modelo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Viaje")
class ViajeTest {

    private Usuario usuario;
    private Viaje viaje;

    @BeforeEach
    void setUp() {
        usuario = new Usuario();
        usuario.setIdUsuario(5);
        viaje = new Viaje(usuario);
    }

    private static FechasViaje fechas(int diasDesdeHoyInicio, int diasDesdeHoyFin) {
        FechasViaje fechas = new FechasViaje();
        fechas.setFechaInicio(LocalDate.now().plusDays(diasDesdeHoyInicio).toString());
        fechas.setFechaFin(LocalDate.now().plusDays(diasDesdeHoyFin).toString());
        return fechas;
    }

    private static ResultadoRecomendacion destino(String pais) {
        Ciudad ciudad = new Ciudad();
        ciudad.setNombre("Ciudad");
        ciudad.setPais(pais);
        return new ResultadoRecomendacion(ciudad, 0.5, 0.5, 0.5, false, false);
    }

    @Test
    @DisplayName("un viaje nuevo no tiene nada definido")
    void viajeNuevo_sinDatos() {
        assertFalse(viaje.estaGuardado());
        assertFalse(viaje.tieneOrigen());
        assertFalse(viaje.tienePreferencias());
        assertFalse(viaje.tieneFechas());
        assertFalse(viaje.tienePresupuesto());
        assertFalse(viaje.tieneDestino());
        assertFalse(viaje.puedeRecomendar());
        assertEquals(0L, viaje.duracionDias());
        assertNull(viaje.destino());
        assertNull(viaje.paisDestino());
        assertSame(usuario, viaje.getUsuario());
    }

    @Test
    @DisplayName("los setters hacen que el viaje reconozca origen, preferencias, fechas y reparto")
    void setters_marcanLosPasosCompletados() {
        viaje.setOrigen(new Ubicacion("Colombia", "Bogotá", null));
        viaje.setPreferencias(new Preferencias());
        viaje.setFechas(fechas(1, 4));
        RepartoPresupuesto reparto = new RepartoPresupuesto();
        viaje.setReparto(reparto);

        assertTrue(viaje.tieneOrigen());
        assertTrue(viaje.tienePreferencias());
        assertTrue(viaje.tieneFechas());
        assertSame(reparto, viaje.getReparto());
        assertEquals(4L, viaje.duracionDias());
    }

    @Test
    @DisplayName("estaGuardado depende de que el id sea mayor que 0")
    void estaGuardado_segunElId() {
        viaje.setIdViaje(12);
        assertTrue(viaje.estaGuardado());
        assertEquals(12, viaje.getIdViaje());

        viaje.setIdViaje(0);
        assertFalse(viaje.estaGuardado());
    }

    @Test
    @DisplayName("definirPresupuesto guarda el presupuesto y reinicia el id y el destino")
    void definirPresupuesto_reiniciaIdYDestino() {
        viaje.setIdViaje(9);
        viaje.elegirDestino(destino("Perú"));

        viaje.definirPresupuesto(new Presupuesto("1000", "USD"), 1000.0);

        assertTrue(viaje.tienePresupuesto());
        assertEquals(1000.0, viaje.getPresupuestoEnUsd(), 0.0001);
        assertEquals("1000", viaje.getPresupuesto().getMonto());
        assertEquals(0, viaje.getIdViaje());
        assertFalse(viaje.tieneDestino());
    }

    @Test
    @DisplayName("elegirDestino permite consultar el destino y su país")
    void elegirDestino_guardaDestinoYPais() {
        ResultadoRecomendacion elegido = destino("Perú");

        viaje.elegirDestino(elegido);

        assertTrue(viaje.tieneDestino());
        assertSame(elegido, viaje.getDestinoElegido());
        assertEquals("Ciudad", viaje.destino().getNombre());
        assertEquals("Perú", viaje.paisDestino());
    }

    @Test
    @DisplayName("puedeRecomendar necesita presupuesto en dólares y al menos 1 día")
    void puedeRecomendar_necesitaPresupuestoYFechas() {
        viaje.definirPresupuesto(new Presupuesto("1000", "USD"), 1000.0);
        assertFalse(viaje.puedeRecomendar());

        viaje.setFechas(fechas(1, 3));
        assertTrue(viaje.puedeRecomendar());
    }

    @Test
    @DisplayName("crearPreferenciasUsuario lanza IllegalStateException si faltan presupuesto o fechas")
    void crearPreferenciasUsuario_sinDatos_lanzaExcepcion() {
        assertThrows(IllegalStateException.class, () -> viaje.crearPreferenciasUsuario(Map.of()));
    }

    @Test
    @DisplayName("crearPreferenciasUsuario arma las preferencias con presupuesto y duración del viaje")
    void crearPreferenciasUsuario_conDatos_armaLasPreferencias() {
        viaje.definirPresupuesto(new Presupuesto("1000", "USD"), 1000.0);
        viaje.setFechas(fechas(1, 5));

        PreferenciasUsuario preferencias = viaje.crearPreferenciasUsuario(Map.of("playa", Importancia.GUSTAR));

        assertEquals(1000.0, preferencias.getPresupuestoUsd(), 0.0001);
        assertEquals(5L, preferencias.getDuracionDias());
        assertTrue(preferencias.tienePreferencia("playa"));
    }

    @Test
    @DisplayName("puedeGuardarse necesita fechas, usuario con id y que no esté guardado")
    void puedeGuardarse_condiciones() {
        assertFalse(viaje.puedeGuardarse());           // sin fechas

        viaje.setFechas(fechas(1, 3));
        assertTrue(viaje.puedeGuardarse());

        viaje.setIdViaje(3);                           // ya guardado
        assertFalse(viaje.puedeGuardarse());
    }

    @Test
    @DisplayName("puedeGuardarse es false si el usuario no tiene id o no hay usuario")
    void puedeGuardarse_usuarioSinIdOSinUsuario() {
        Viaje sinId = new Viaje(new Usuario());
        sinId.setFechas(fechas(1, 3));
        assertFalse(sinId.puedeGuardarse());

        Viaje sinUsuario = new Viaje(null);
        sinUsuario.setFechas(fechas(1, 3));
        assertFalse(sinUsuario.puedeGuardarse());
    }
}

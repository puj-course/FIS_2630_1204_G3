package com.wisetrip.controlador;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpSession;

import com.wisetrip.modelo.Ciudad;
import com.wisetrip.modelo.FechasViaje;
import com.wisetrip.modelo.Presupuesto;
import com.wisetrip.modelo.RepartoPresupuesto;
import com.wisetrip.modelo.ResultadoRecomendacion;
import com.wisetrip.modelo.Usuario;
import com.wisetrip.modelo.Viaje;

/**
 * Pruebas unitarias de SesionViaje (CP58 - CP67).
 * La sesión es un MockHttpSession de Spring, así que no hace falta levantar el servidor.
 */
@DisplayName("SesionViaje")
class SesionViajeTest {

    private static final String ATRIBUTO_VIAJE = "viaje";

    private MockHttpSession sesion;
    private Usuario usuario;

    @BeforeEach
    void setUp() {
        sesion = new MockHttpSession();
        usuario = usuario(1);
        sesion.setAttribute(Nombres.USUARIO_ACTIVO, usuario);
    }

    private static Usuario usuario(int id) {
        Usuario nuevo = new Usuario();
        nuevo.setIdUsuario(id);
        return nuevo;
    }

    /** Viaje ya guardado en la base de datos, con fechas, presupuesto, destino y reparto. */
    private static Viaje viajeGuardado(Usuario dueno) {
        Viaje viaje = new Viaje(dueno);
        viaje.setFechas(new FechasViaje());
        viaje.definirPresupuesto(new Presupuesto("2000000", "COP"), 500.0);

        Ciudad ciudad = new Ciudad();
        ciudad.setNombre("Cartagena");
        ciudad.setPais("Colombia");
        viaje.elegirDestino(new ResultadoRecomendacion(ciudad, 90, 50, 40, true, false));

        viaje.setReparto(new RepartoPresupuesto());
        // Va al final porque definirPresupuesto deja el id en 0.
        viaje.setIdViaje(7);
        return viaje;
    }

    // ------------------------------------------------------------ de

    @Test
    @DisplayName("CP58 de: sin viaje en la sesión crea uno nuevo para el usuario y lo guarda")
    void cp58_de_sinViaje_creaUnoNuevo() {
        Viaje viaje = SesionViaje.de(sesion, usuario);

        assertSame(usuario, viaje.getUsuario());
        assertSame(viaje, sesion.getAttribute(ATRIBUTO_VIAJE));
    }

    @Test
    @DisplayName("CP59 de: si el viaje ya es del usuario devuelve el mismo, comparando por id")
    void cp59_de_mismoUsuario_devuelveElMismo() {
        Viaje primero = SesionViaje.de(sesion, usuario);

        Viaje segundo = SesionViaje.de(sesion, usuario(1));

        assertSame(primero, segundo);
    }

    @Test
    @DisplayName("CP60 de: si el viaje es de otro usuario crea uno nuevo para el usuario actual")
    void cp60_de_viajeDeOtroUsuario_creaUnoNuevo() {
        Viaje ajeno = new Viaje(usuario(2));
        sesion.setAttribute(ATRIBUTO_VIAJE, ajeno);

        Viaje viaje = SesionViaje.de(sesion, usuario);

        assertNotSame(ajeno, viaje);
        assertSame(usuario, viaje.getUsuario());
        assertSame(viaje, sesion.getAttribute(ATRIBUTO_VIAJE));
    }

    // ------------------------------------------------------------ restaurar

    @Test
    @DisplayName("CP61 restaurar: viaje guardado del usuario activo carga todos sus datos en la sesión")
    void cp61_restaurar_viajeValido_cargaLosDatos() {
        sesion.setAttribute("seleccionDestinos", "de una planificación anterior");
        Viaje viaje = viajeGuardado(usuario);

        SesionViaje.restaurar(sesion, viaje);

        assertSame(viaje, sesion.getAttribute(ATRIBUTO_VIAJE));
        assertEquals(7, sesion.getAttribute("idViajeGuardado"));
        assertSame(viaje.getFechas(), sesion.getAttribute(Nombres.FECHAS_VIAJE));
        assertSame(viaje.getPresupuesto(), sesion.getAttribute(Nombres.PRESUPUESTO_VIAJE));
        assertEquals(500.0, sesion.getAttribute("presupuestoEnUsd"));
        assertSame(viaje.getDestinoElegido(), sesion.getAttribute(Nombres.DESTINO_ELEGIDO));
        assertEquals("Colombia", sesion.getAttribute(Nombres.PAIS_DESTINO));
        assertSame(viaje.getReparto(), sesion.getAttribute(Nombres.REPARTO_PRESUPUESTO));
        // Lo de la planificación anterior se borra y la sesión sigue abierta.
        assertNull(sesion.getAttribute("seleccionDestinos"));
        assertSame(usuario, sesion.getAttribute(Nombres.USUARIO_ACTIVO));
    }

    @Test
    @DisplayName("CP62 restaurar: viaje nulo lanza IllegalArgumentException")
    void cp62_restaurar_viajeNulo_lanzaError() {
        assertThrows(IllegalArgumentException.class, () -> SesionViaje.restaurar(sesion, null));
    }

    @Test
    @DisplayName("CP63 restaurar: viaje que todavía no está guardado lanza IllegalArgumentException")
    void cp63_restaurar_viajeSinGuardar_lanzaError() {
        Viaje sinGuardar = new Viaje(usuario);

        assertThrows(IllegalArgumentException.class, () -> SesionViaje.restaurar(sesion, sinGuardar));
    }

    @Test
    @DisplayName("CP64 restaurar: sin usuario activo en la sesión lanza IllegalArgumentException")
    void cp64_restaurar_sinUsuarioActivo_lanzaError() {
        sesion.removeAttribute(Nombres.USUARIO_ACTIVO);
        Viaje viaje = viajeGuardado(usuario);

        assertThrows(IllegalArgumentException.class, () -> SesionViaje.restaurar(sesion, viaje));
    }

    @Test
    @DisplayName("CP65 restaurar: viaje de otro usuario lanza error y no toca la sesión")
    void cp65_restaurar_viajeDeOtroUsuario_lanzaErrorSinCambiarLaSesion() {
        Viaje ajeno = viajeGuardado(usuario(2));

        assertThrows(IllegalArgumentException.class, () -> SesionViaje.restaurar(sesion, ajeno));
        assertNull(sesion.getAttribute(ATRIBUTO_VIAJE));
    }

    // ------------------------------------------------------------ invalidarSeleccion

    @Test
    @DisplayName("CP66 invalidarSeleccion: borra la selección del destino y conserva preferencias y fechas")
    void cp66_invalidarSeleccion_borraSoloLaSeleccion() {
        sesion.setAttribute("seleccionDestinos", "x");
        sesion.setAttribute("seleccionRecomendada", "x");
        sesion.setAttribute(Nombres.DESTINO_ELEGIDO, "x");
        sesion.setAttribute(Nombres.PAIS_DESTINO, "x");
        sesion.setAttribute(Nombres.REPARTO_PRESUPUESTO, "x");
        sesion.setAttribute(Nombres.PREFERENCIAS_VIAJE, "x");
        sesion.setAttribute(Nombres.FECHAS_VIAJE, "x");

        SesionViaje.invalidarSeleccion(sesion);

        assertNull(sesion.getAttribute("seleccionDestinos"));
        assertNull(sesion.getAttribute("seleccionRecomendada"));
        assertNull(sesion.getAttribute(Nombres.DESTINO_ELEGIDO));
        assertNull(sesion.getAttribute(Nombres.PAIS_DESTINO));
        assertNull(sesion.getAttribute(Nombres.REPARTO_PRESUPUESTO));
        assertNotNull(sesion.getAttribute(Nombres.PREFERENCIAS_VIAJE));
        assertNotNull(sesion.getAttribute(Nombres.FECHAS_VIAJE));
    }

    // ------------------------------------------------------------ reiniciar

    @Test
    @DisplayName("CP67 reiniciar: borra toda la planificación sin cerrar la sesión del usuario")
    void cp67_reiniciar_borraLaPlanificacion() {
        String[] atributos = {
                ATRIBUTO_VIAJE, "idViajeGuardado", "seleccionDestinos", "seleccionRecomendada",
                Nombres.DESTINO_ELEGIDO, Nombres.PAIS_DESTINO, Nombres.PREFERENCIAS_VIAJE,
                "importanciasSeleccionadas", "atributosSeleccionados", Nombres.FECHAS_VIAJE,
                Nombres.PRESUPUESTO_VIAJE, "presupuestoEnUsd", Nombres.REPARTO_PRESUPUESTO
        };
        for (int i = 0; i < atributos.length; i++) {
            sesion.setAttribute(atributos[i], "x");
        }

        SesionViaje.reiniciar(sesion);

        for (int i = 0; i < atributos.length; i++) {
            assertNull(sesion.getAttribute(atributos[i]), "Quedó en la sesión: " + atributos[i]);
        }
        assertSame(usuario, sesion.getAttribute(Nombres.USUARIO_ACTIVO));
    }
}
package com.wisetrip.controlador;

import com.wisetrip.modelo.Usuario;
import com.wisetrip.modelo.Viaje;

import jakarta.servlet.http.HttpSession;

/**
 * Unico lugar donde se guarda y se busca el Viaje en la sesion HTTP.
 * Asi los controladores no repiten ese codigo ni conocen el nombre del atributo.
 */
final class SesionViaje {

    private static final String ATRIBUTO = "viaje";

    private SesionViaje() {
    }

    /**
     * Devuelve el viaje en curso del usuario. Si no hay uno, o es de otro usuario
     * (alguien inicio sesion con otra cuenta en la misma sesion), crea uno nuevo.
     */
    static Viaje de(HttpSession sesion, Usuario usuario) {
        Viaje viaje = (Viaje) sesion.getAttribute(ATRIBUTO);
        if (viaje == null || viaje.getUsuario().getIdUsuario() != usuario.getIdUsuario()) {
            viaje = new Viaje(usuario);
            sesion.setAttribute(ATRIBUTO, viaje);
        }
        return viaje;
    }

    /** Limpia la planificacion en sesion sin borrar viajes guardados ni cerrar la cuenta. */
    static void reiniciar(HttpSession sesion) {
        synchronized (sesion) {
            for (String atributo : new String[] {
                    ATRIBUTO, "idViajeGuardado", "seleccionDestinos", "seleccionRecomendada",
                    "destinoElegido", "paisDestino", "preferenciasViaje",
                    "importanciasSeleccionadas", "atributosSeleccionados", "fechasViaje",
                    "presupuestoViaje", "presupuestoEnUsd", "repartoPresupuesto"
            }) {
                sesion.removeAttribute(atributo);
            }
        }
    }
}

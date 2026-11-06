package com.wisetrip.controlador;


import com.wisetrip.modelo.Usuario;
import com.wisetrip.modelo.Viaje;

import jakarta.servlet.http.HttpSession;
import org.springframework.web.util.WebUtils;

/**
 * Unico lugar donde se guarda y se busca el Viaje en la sesion HTTP.
 * Asi los controladores no repiten ese codigo ni conocen el nombre del atributo.
 */
final class SesionViaje {

    private static final String ATRIBUTO = "viaje";
    private static final String DESTINO_ELEGIDO = "destinoElegido";
    private static final String PAIS_DESTINO = "paisDestino";
    private static final String REPARTO_PRESUPUESTO = "repartoPresupuesto";   

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

    /**
     * Candado de la sesion: el mismo objeto para todas las peticiones del usuario,
     * para que dos pestañas no modifiquen el viaje al mismo tiempo.
     */
    private static Object candado(HttpSession sesion) {
        return WebUtils.getSessionMutex(sesion);
    }

    /** Recupera los datos persistidos sin mezclar otra planificacion de la sesion. */
    static void restaurar(HttpSession sesion, Viaje viaje) {
        synchronized (candado(sesion)) {
            Usuario activo = (Usuario) sesion.getAttribute("usuarioActivo");
            if (viaje == null || !viaje.estaGuardado() || activo == null
                    || viaje.getUsuario().getIdUsuario() != activo.getIdUsuario()) {
                throw new IllegalArgumentException("El viaje no pertenece al usuario activo.");
            }
            reiniciar(sesion);
            sesion.setAttribute(ATRIBUTO, viaje);
            sesion.setAttribute("idViajeGuardado", viaje.getIdViaje());
            sesion.setAttribute("fechasViaje", viaje.getFechas());
            sesion.setAttribute("presupuestoViaje", viaje.getPresupuesto());
            sesion.setAttribute("presupuestoEnUsd", viaje.getPresupuestoEnUsd());
            sesion.setAttribute(DESTINO_ELEGIDO, viaje.getDestinoElegido());
            sesion.setAttribute(PAIS_DESTINO, viaje.paisDestino());
            sesion.setAttribute(REPARTO_PRESUPUESTO, viaje.getReparto());
        }
    }

    /** Descarta resultados calculados con preferencias, fechas o presupuesto anteriores. */
    static void invalidarSeleccion(HttpSession sesion) {
        synchronized (candado(sesion)) {
            sesion.removeAttribute("seleccionDestinos");
            sesion.removeAttribute("seleccionRecomendada");
            sesion.removeAttribute(DESTINO_ELEGIDO);
            sesion.removeAttribute(PAIS_DESTINO);
            sesion.removeAttribute(REPARTO_PRESUPUESTO);
        }
    }

    /** Limpia la planificacion en sesion sin borrar viajes guardados ni cerrar la cuenta. */
    static void reiniciar(HttpSession sesion) {
        synchronized (candado(sesion)) {
            for (String atributo : new String[] {
                    ATRIBUTO, "idViajeGuardado", "seleccionDestinos", "seleccionRecomendada",
                    DESTINO_ELEGIDO, PAIS_DESTINO, "preferenciasViaje",
                    "importanciasSeleccionadas", "atributosSeleccionados", "fechasViaje",
                    "presupuestoViaje", "presupuestoEnUsd", REPARTO_PRESUPUESTO
            }) {
                sesion.removeAttribute(atributo);
            }
        }
    }
}

package com.wisetrip.controlador;

import com.wisetrip.modelo.Usuario;
import com.wisetrip.modelo.Viaje;

import org.springframework.web.util.WebUtils;
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

    /** Recupera los datos persistidos sin mezclar otra planificacion de la sesion. */
    static void restaurar(HttpSession sesion, Viaje viaje) {
        synchronized (WebUtils.getSessionMutex(sesion)) {
            Usuario activo = (Usuario) sesion.getAttribute(Nombres.USUARIO_ACTIVO);
            if (viaje == null || !viaje.estaGuardado() || activo == null
                    || viaje.getUsuario().getIdUsuario() != activo.getIdUsuario()) {
                throw new IllegalArgumentException("El viaje no pertenece al usuario activo.");
            }
            reiniciar(sesion);
            sesion.setAttribute(ATRIBUTO, viaje);
            sesion.setAttribute("idViajeGuardado", viaje.getIdViaje());
            sesion.setAttribute(Nombres.FECHAS_VIAJE, viaje.getFechas());
            sesion.setAttribute(Nombres.PRESUPUESTO_VIAJE, viaje.getPresupuesto());
            sesion.setAttribute("presupuestoEnUsd", viaje.getPresupuestoEnUsd());
            sesion.setAttribute(Nombres.DESTINO_ELEGIDO, viaje.getDestinoElegido());
            sesion.setAttribute(Nombres.PAIS_DESTINO, viaje.paisDestino());
            sesion.setAttribute(Nombres.REPARTO_PRESUPUESTO, viaje.getReparto());
        }
    }

    /** Descarta resultados calculados con preferencias, fechas o presupuesto anteriores. */
    static void invalidarSeleccion(HttpSession sesion) {
        synchronized (WebUtils.getSessionMutex(sesion)) {
            sesion.removeAttribute("seleccionDestinos");
            sesion.removeAttribute("seleccionRecomendada");
            sesion.removeAttribute(Nombres.DESTINO_ELEGIDO);
            sesion.removeAttribute(Nombres.PAIS_DESTINO);
            sesion.removeAttribute(Nombres.REPARTO_PRESUPUESTO);
        }
    }

    /** Limpia la planificacion en sesion sin borrar viajes guardados ni cerrar la cuenta. */
    static void reiniciar(HttpSession sesion) {
        synchronized (WebUtils.getSessionMutex(sesion)) {
            for (String atributo : new String[] {
                    ATRIBUTO, "idViajeGuardado", "seleccionDestinos", "seleccionRecomendada",
                    Nombres.DESTINO_ELEGIDO, Nombres.PAIS_DESTINO, Nombres.PREFERENCIAS_VIAJE,
                    "importanciasSeleccionadas", "atributosSeleccionados", Nombres.FECHAS_VIAJE,
                    Nombres.PRESUPUESTO_VIAJE, "presupuestoEnUsd", Nombres.REPARTO_PRESUPUESTO
            }) {
                sesion.removeAttribute(atributo);
            }
        }
    }
}

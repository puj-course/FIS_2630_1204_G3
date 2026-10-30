package com.wisetrip.negocio;

/**
 *  un lugar con país, ciudad y un punto de partida
 * opcional.
 * Usa extends para heredar el contrato de IValidable: toda ubicación
 * sabe validarse.
 * quien necesita una ubicación depende
 * de este contrato, no de la clase Ubicacion.
 */
public interface IUbicable extends IValidable {

    String getPais();

    String getCiudad();

    String getDetalle();

    /** Tiene país y ciudad. */
    boolean estaCompleta();

    /** Tiene un punto de partida escrito (barrio, aeropuerto, terminal). */
    boolean tieneDetalle();

    /** Texto listo para mostrar: "Aeropuerto principal - Bogotá, Colombia". */
    String getDescripcion();

    /** Compara ciudad y país sin importar mayúsculas. */
    default boolean esMismaCiudadQue(IUbicable otra) {
        if (otra == null || getCiudad() == null || getPais() == null) {
            return false;
        }
        return getCiudad().equalsIgnoreCase(otra.getCiudad())
                && getPais().equalsIgnoreCase(otra.getPais());
    }
}
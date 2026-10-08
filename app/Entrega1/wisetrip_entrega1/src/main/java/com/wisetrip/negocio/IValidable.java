package com.wisetrip.negocio;

import java.util.Map;

/**
 *  un objeto que sabe validar sus propios datos.
 * cada objeto valida lo que conoce.
 */
public interface IValidable {

    /**
     * Devuelve los errores encontrados, con la clave del campo.
     * Si el mapa está vacío, los datos son válidos.
     */
    Map<String, String> validar();

    default boolean esValido() {
        return validar().isEmpty();
    }
}
package com.wisetrip.modelo;

/**
 * Determina el resultado de un atributo automatico segun el conteo de Geoapify:
 * Alcanza el umbral: cumple; por debajo: no cumple; fallo: desconocido.
 */

public enum EstadoAtributo {
    CUMPLE,
    NO_CUMPLE,
    NO_SABEMOS;

    /**
     * Evalua si el conteo de lugares de Geoapify cumple con el umbral minimo de la pregunta.
     *
     * conteo Numero de lugares hallados o null si falla la consulta.
     * umbral Cantidad mínima requerida.
     */
    public static EstadoAtributo desdeConteo(Integer conteo, int umbral) {
        if (conteo == null) {
            return NO_SABEMOS;
        }
        if (conteo >= umbral) {
            return CUMPLE;
        }
        // La consulta respondio, pero no alcanza el minimo.
        return NO_CUMPLE;
    }
}

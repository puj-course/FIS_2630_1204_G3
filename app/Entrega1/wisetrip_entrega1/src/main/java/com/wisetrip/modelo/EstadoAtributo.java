package com.wisetrip.modelo;

/**
 * Determina el resultado de un atributo automatico segun el conteo de Geoapify:
 * si es igual o supera el umbral cumple, si es cero no cumple, y si falla o no alcanza el umbral no se sabe.
 */

public enum EstadoAtributo {
    cumple,
    noCumple,
    noSabemos;

    /**
     * Evalua si el conteo de lugares de Geoapify cumple con el umbral minimo de la pregunta.
     *
     * conteo Numero de lugares hallados o null si falla la consulta.
     * umbral Cantidad mínima requerida.
     */
    public static EstadoAtributo desdeConteo(Integer conteo, int umbral) {
        if (conteo == null) {
            return noSabemos;
        }
        if (conteo >= umbral) {
            return cumple;
        }
        if (conteo == 0) {
            return noCumple;
        }
        return noSabemos;
    }
}

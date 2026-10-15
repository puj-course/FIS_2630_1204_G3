package com.wisetrip.modelo;

/**
 * Resultado de evaluar un gasto contra el cupo de su categoría.
 * {@code bloqueante} distingue si el exceso debe impedir el registro
 * (política estricta) o solo advertirlo (política flexible).
 */
public record ResultadoControlCategoria(
        boolean excedeCupo,
        boolean bloqueante,
        String mensaje) {

    public static ResultadoControlCategoria sinProblemas() {
        return new ResultadoControlCategoria(false, false, null);
    }
}

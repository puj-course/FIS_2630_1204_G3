package com.wisetrip.servicio;

/**
 * Handler del patrón Chain of Responsibility: cada regla de validación
 * de fechas extiende esta clase, valida lo suyo, y decide si la cadena
 * debe seguir con la siguiente regla.
 */
public abstract class ReglaValidacionFechas {

    private ReglaValidacionFechas siguiente;

    /** Encadena la siguiente regla y la devuelve, para poder seguir encadenando. */
    public ReglaValidacionFechas siguiente(ReglaValidacionFechas siguiente) {
        this.siguiente = siguiente;
        return siguiente;
    }

        /** Punto de entrada de la cadena: valida esta regla y pasa el turno a la siguiente. */
        public final void validar(ContextoValidacionFechas contexto) {
        validarPropio(contexto);
        if (siguiente != null) {
            siguiente.validar(contexto);
        }
    }

    /** Valida lo que le corresponde a esta regla. */
    protected abstract void validarPropio(ContextoValidacionFechas contexto);
}
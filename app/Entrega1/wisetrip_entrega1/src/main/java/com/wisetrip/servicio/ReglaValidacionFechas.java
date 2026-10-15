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

    /** Punto de entrada de la cadena: valida esta regla y pasa el turno si corresponde. */
    public final void validar(ContextoValidacionFechas contexto) {
        boolean continuar = validarPropio(contexto);
        if (continuar && siguiente != null) {
            siguiente.validar(contexto);
        }
    }

    /** Valida lo que le corresponde a esta regla. Devuelve si la cadena debe continuar. */
    protected abstract boolean validarPropio(ContextoValidacionFechas contexto);
}
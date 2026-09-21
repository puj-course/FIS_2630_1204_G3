/**
 * Preferencias de viaje.
 * Modelo que representa una pregunta del cuestionario de preferencias,
 * identificada por una clave y su texto correspondiente.
 */

package com.wisetrip.modelo;

public class Pregunta {

    private final String clave;
    private final String texto;

    public Pregunta(String clave, String texto) {
        this.clave = clave;
        this.texto = texto;
    }

    public String getClave() { return clave; }
    public String getTexto() { return texto; }
}

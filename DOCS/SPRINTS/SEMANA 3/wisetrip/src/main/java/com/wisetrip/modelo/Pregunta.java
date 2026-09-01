package com.wisetrip.modelo;

/**
 * Representa una pregunta Si/No del banco de preferencias.
 * "clave" es el identificador tecnico (ej. "playa") y "texto" es lo que ve el usuario.
 */
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

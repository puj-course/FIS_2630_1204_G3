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
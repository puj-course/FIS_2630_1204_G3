package com.wisetrip.modelo;

import java.util.List;

/**
 * Agrupa las preguntas de una categoria del documento "Preguntas de Preferencia"
 * (ej. Tipo de destino, Clima, Aventura, Gastronomia...).
 */
public class CategoriaPreferencia {

    private final String nombre;
    private final List<Pregunta> preguntas;

    public CategoriaPreferencia(String nombre, List<Pregunta> preguntas) {
        this.nombre = nombre;
        this.preguntas = preguntas;
    }

    public String getNombre() { return nombre; }
    public List<Pregunta> getPreguntas() { return preguntas; }
}

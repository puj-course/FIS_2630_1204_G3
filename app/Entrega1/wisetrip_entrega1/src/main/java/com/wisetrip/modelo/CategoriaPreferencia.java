package com.wisetrip.modelo;

import java.util.List;

public class CategoriaPreferencia {

    private final String nombre;
    private final String descripcion;
    private final List<Pregunta> preguntas;

    public CategoriaPreferencia(String nombre, String descripcion, List<Pregunta> preguntas) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.preguntas = preguntas;
    }

    public String getNombre() { return nombre; }
    public String getDescripcion() { return descripcion; }
    public List<Pregunta> getPreguntas() { return preguntas; }
}
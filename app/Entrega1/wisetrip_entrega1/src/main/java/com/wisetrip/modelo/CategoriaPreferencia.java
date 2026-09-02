package com.wisetrip.modelo;

import java.util.List;

public class CategoriaPreferencia {

    private final String nombre;
    private final String icono;
    private final String descripcion;
    private final List<Pregunta> preguntas;

    public CategoriaPreferencia(String nombre, String icono, String descripcion, List<Pregunta> preguntas) {
        this.nombre = nombre;
        this.icono = icono;
        this.descripcion = descripcion;
        this.preguntas = preguntas;
    }

    public String getNombre() { return nombre; }
    public String getIcono() { return icono; }
    public String getDescripcion() { return descripcion; }
    public List<Pregunta> getPreguntas() { return preguntas; }
}
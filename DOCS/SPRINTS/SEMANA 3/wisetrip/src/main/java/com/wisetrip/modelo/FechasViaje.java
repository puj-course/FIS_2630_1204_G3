package com.wisetrip.modelo;

public class FechasViaje {

    private String fechaInicio;
    private String fechaFin;

    // Constructor vacio: Spring lo necesita para llenar el objeto con los datos del formulario
    public FechasViaje() {
    }

    public String getFechaInicio() { return fechaInicio; }
    public void setFechaInicio(String fechaInicio) { this.fechaInicio = fechaInicio; }

    public String getFechaFin() { return fechaFin; }
    public void setFechaFin(String fechaFin) { this.fechaFin = fechaFin; }
}

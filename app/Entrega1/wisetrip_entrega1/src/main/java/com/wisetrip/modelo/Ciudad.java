package com.wisetrip.modelo;

import java.util.HashMap;
import java.util.Map;

public class Ciudad {

    private int id;
    private String nombre;
    private String pais;
    private double costoPromedio;
    private double latitud;
    private double longitud;
    private Map<String, Boolean> atributos;

    public Ciudad() {
        this.atributos = new HashMap<>();
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getPais() { return pais; }
    public void setPais(String pais) { this.pais = pais; }
    public double getCostoPromedio() { return costoPromedio; }
    public void setCostoPromedio(double costoPromedio) { this.costoPromedio = costoPromedio; }
    public double getLatitud() { return latitud; }
    public void setLatitud(double latitud) { this.latitud = latitud; }
    public double getLongitud() { return longitud; }
    public void setLongitud(double longitud) { this.longitud = longitud; }

    public Map<String, Boolean> getAtributos() { return atributos; }
    public void setAtributos(Map<String, Boolean> atributos) {
        this.atributos = atributos != null ? atributos : new HashMap<>();
    }
}
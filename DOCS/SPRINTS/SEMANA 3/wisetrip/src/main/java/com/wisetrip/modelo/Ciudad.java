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
    private int idNivel;
    private Map<String, Integer> atributos;

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

    public int getIdNivel() { return idNivel; }
    public void setIdNivel(int idNivel) { this.idNivel = idNivel; }

    public Map<String, Integer> getAtributos() {
        return atributos;
    }

    public void setAtributos(Map<String, Integer> atributos) {
        this.atributos = atributos != null
                ? atributos
                : new HashMap<>();
    }

    public Integer valorAtributo(String nombreAtributo) {
        return atributos.get(nombreAtributo);
    }

    public boolean tieneAtributo(String nombreAtributo) {
        return atributos.containsKey(nombreAtributo);
    }

    //Primera entrega:
     //el presupuesto funciona como filtro duro.

    public boolean aceptaPresupuesto(double presupuesto) {
        if (presupuesto <= 0) {
            return false;
        }

        return costoPromedio <= presupuesto;
    }
}
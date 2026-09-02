package com.wisetrip.modelo;

import java.util.LinkedHashMap;
import java.util.Map;

public class Ciudad {

    private int id;
    private String nombre;
    private String pais;
    private double latitud;
    private double longitud;
    private double costoPromedio;
    private int idNivel;

    /** nombre del atributo -> valor. 0 significa que la ciudad no lo tiene. */
    private Map<String, Integer> atributos = new LinkedHashMap<>();

    public Ciudad() {
    }

    public Ciudad(int id, String nombre, String pais, double costoPromedio,
                  Map<String, Integer> atributos) {
        this.id = id;
        this.nombre = nombre;
        this.pais = pais;
        this.costoPromedio = costoPromedio;
        this.atributos = atributos;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getPais() { return pais; }
    public void setPais(String pais) { this.pais = pais; }

    public double getLatitud() { return latitud; }
    public void setLatitud(double latitud) { this.latitud = latitud; }

    public double getLongitud() { return longitud; }
    public void setLongitud(double longitud) { this.longitud = longitud; }

    public double getCostoPromedio() { return costoPromedio; }
    public void setCostoPromedio(double costoPromedio) { this.costoPromedio = costoPromedio; }

    public int getIdNivel() { return idNivel; }
    public void setIdNivel(int idNivel) { this.idNivel = idNivel; }

    public Map<String, Integer> getAtributos() { return atributos; }
    public void setAtributos(Map<String, Integer> atributos) { this.atributos = atributos; }

    /** La ciudad cumple un atributo si su valor es mayor que cero. */
    public boolean tiene(String atributo) {
        Integer valor = atributos.get(atributo);
        return valor != null && valor > 0;
    }

    public String getDescripcion() {
        return nombre + ", " + pais;
    }
}
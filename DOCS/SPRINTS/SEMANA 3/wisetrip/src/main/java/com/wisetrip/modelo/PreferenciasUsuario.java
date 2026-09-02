package com.wisetrip.modelo;

import java.util.LinkedHashMap;
import java.util.Map;

public class PreferenciasUsuario {

    private final double presupuesto;   // en USD, para poder comparar
    private final Map<String, Boolean> atributos;

    public PreferenciasUsuario(double presupuesto, Map<String, Boolean> atributos) {
        this.presupuesto = presupuesto;
        this.atributos = atributos != null ? atributos : new LinkedHashMap<>();
    }

    public double getPresupuesto() { return presupuesto; }

    public Map<String, Boolean> getAtributos() { return atributos; }
}
/**
 * Preferencias de viaje.
 * Modelo que agrupa el presupuesto (en USD) y los atributos de preferencia, esto cambio
 * ya que ahora, usamos mas que si o no, se afina un poco mas la recomendacion.
 */

package com.wisetrip.modelo;

import java.util.LinkedHashMap;
import java.util.Map;

public class PreferenciasUsuario {

    private final long duracionDias;
    private final double presupuesto;   // en USD, para poder comparar
    private final Map<String, Importancia> atributos;

    public PreferenciasUsuario(double presupuesto, Map<String, Boolean> atributos, long duracionDias) {
        if (duracionDias < 1) throw new IllegalArgumentException("La duracion debe ser positiva.");
        this.duracionDias = duracionDias;
        this.presupuesto = presupuesto;
        this.atributos = atributos != null ? atributos : new LinkedHashMap<>();
    }

    public long getDuracionDias() { return duracionDias; }

    public double getPresupuesto() { return presupuesto; }

    public Map<String, Boolean> getAtributos() { return atributos; }
}

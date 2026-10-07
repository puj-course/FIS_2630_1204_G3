/**
 * Preferencias de viaje.
 * Modelo que agrupa el presupuesto (en USD) y los atributos de preferencia
 * con su nivel de importancia, para afinar la recomendacion.
 */

package com.wisetrip.modelo;

import java.util.LinkedHashMap;
import java.util.Map;

public class PreferenciasUsuario {

    private final long duracionDias;
    private final double presupuesto;   // en USD, para poder comparar
    private final Map<String, Importancia> atributos;

    public PreferenciasUsuario(double presupuesto, Map<String, Importancia> atributos, long duracionDias) {
        if (duracionDias < 1) throw new IllegalArgumentException("La duracion debe ser positiva.");
        this.duracionDias = duracionDias;
        this.presupuesto = presupuesto;
        this.atributos = atributos != null ? new LinkedHashMap<>(atributos) : new LinkedHashMap<>();
    }

    /**
     * Convierte el mapa legado si/no a {@link Importancia}
     * ({@code true} → {@link Importancia#si}, {@code false} → {@link Importancia#no}).
     */
    public static Map<String, Importancia> desdeBooleanos(Map<String, Boolean> siNo) {
        Map<String, Importancia> mapa = new LinkedHashMap<>();
        if (siNo == null) {
            return mapa;
        }
        for (Map.Entry<String, Boolean> entry : siNo.entrySet()) {
            mapa.put(entry.getKey(),
                    Boolean.TRUE.equals(entry.getValue()) ? Importancia.si : Importancia.no);
        }
        return mapa;
    }

    public long getDuracionDias() { return duracionDias; }

    public double getPresupuesto() { return presupuesto; }

    public Map<String, Importancia> getAtributos() { return atributos; }

    /** Hay al menos un ME_GUSTARIA o LO_PREFIERO (peso &gt; 0) para puntuar. */
    public boolean tienePreferenciasPuntuables() {
        return atributos.values().stream()
                .anyMatch(importancia -> importancia != null && importancia.getPeso() > 0);
    }
}

package com.wisetrip.modelo;

import java.util.LinkedHashMap;
import java.util.Map;

/** Datos del viajero que usa el algoritmo de recomendación. */
public class PreferenciasUsuario {

    private final Map<String, Importancia> atributos;
    private final Double presupuestoUsd;
    private final long duracionDias;

    public PreferenciasUsuario(Double presupuestoUsd,
                               Map<String, Importancia> atributos,
                               long duracionDias) {
        this.atributos = atributos != null ? new LinkedHashMap<>(atributos) : new LinkedHashMap<>();
        this.presupuestoUsd = presupuestoUsd;
        this.duracionDias = duracionDias;
    }

    public Map<String, Importancia> getAtributos() {
        return atributos;
    }

    public Double getPresupuestoUsd() {
        return presupuestoUsd;
    }

    public long getDuracionDias() {
        return duracionDias;
    }

    /**
     * Indica si el usuario tiene al menos una preferencia que puede
     * utilizarse para realizar una puntuación.
     */
    public boolean tienePreferenciasPuntuables() {
        return atributos.values().stream()
                .anyMatch(importancia ->
                        importancia != null && importancia.esPuntuable());
    }
}

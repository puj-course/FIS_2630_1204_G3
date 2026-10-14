package com.wisetrip.modelo;

import java.util.LinkedHashMap;
import java.util.Map;

public class PreferenciasUsuario {

    private Map<String, Importancia> atributos = new LinkedHashMap<>();
    private Double presupuestoUsd;
    private long duracionDias;

    public PreferenciasUsuario() {
    }

    public PreferenciasUsuario(Map<String, Importancia> atributos) {
        if (atributos != null) {
            this.atributos = new LinkedHashMap<>(atributos);
        }
    }

    public PreferenciasUsuario(Double presupuestoUsd,
                               Map<String, Importancia> atributos,
                               long duracionDias) {
        this(atributos);
        this.presupuestoUsd = presupuestoUsd;
        this.duracionDias = duracionDias;
    }

    public Map<String, Importancia> getAtributos() {
        return atributos;
    }

    public void setAtributos(Map<String, Importancia> atributos) {
        this.atributos = atributos;
    }

    public Double getPresupuestoUsd() {
        return presupuestoUsd;
    }

    public void setPresupuestoUsd(Double presupuestoUsd) {
        this.presupuestoUsd = presupuestoUsd;
    }

    public long getDuracionDias() {
        return duracionDias;
    }

    public void setDuracionDias(long duracionDias) {
        this.duracionDias = duracionDias;
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

    /**
     * Indica si el usuario manifestó interés por un atributo.
     */
    public boolean tienePreferencia(String atributo) {
        Importancia importancia = atributos.get(atributo);

        return importancia != null
                && importancia.estaSeleccionada();
    }

    /**
     * Indica si un atributo es indispensable para el usuario.
     */
    public boolean esIndispensable(String atributo) {
        Importancia importancia = atributos.get(atributo);

        return importancia != null
                && importancia.esIndispensable();
    }

    /**
     * Obtiene la importancia que el usuario asignó a un atributo.
     */
    public Importancia importanciaDe(String atributo) {
        return atributos.get(atributo);
    }

    /**
     * Obtiene el peso de una preferencia.
     */
    public int pesoDe(String atributo) {
        Importancia importancia = atributos.get(atributo);

        if (importancia == null) {
            return 0;
        }

        return importancia.getPeso();
    }
}
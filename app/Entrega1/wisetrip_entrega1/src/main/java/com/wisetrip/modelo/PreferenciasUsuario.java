package com.wisetrip.modelo;

import java.util.LinkedHashMap;
import java.util.Map;
import com.wisetrip.negocio.CatalogoPreguntas;

public class PreferenciasUsuario {

    private Map<String, Importancia> atributos = new LinkedHashMap<>();
    private Double presupuestoUsd;
    private long duracionDias;

    public PreferenciasUsuario() {
    }

    public PreferenciasUsuario(Map<String, Importancia> atributos) {
        if (atributos != null) {
            setAtributos(atributos);
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
        this.atributos = new LinkedHashMap<>();
        if (atributos == null) return;
        atributos.forEach((clave, importancia) -> {
            var pregunta = CatalogoPreguntas.porId(clave);
            // Las respuestas de perfil se conservan en el cuestionario,
            // pero no son requisitos ni gustos comparables con un destino.
            if (pregunta != null && !pregunta.esDePerfil()) {
                this.atributos.put(clave, importancia);
            }
        });
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


}

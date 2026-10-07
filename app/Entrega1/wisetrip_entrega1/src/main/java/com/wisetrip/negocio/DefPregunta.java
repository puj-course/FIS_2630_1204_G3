package com.wisetrip.negocio;

import com.wisetrip.modelo.TipoAtributo;

public class DefPregunta {

    public final String id;
    public final String texto;
    public final TipoAtributo tipo;
    public final String categorias;
    public final int umbral;

    public DefPregunta(
            String id,
            String texto,
            TipoAtributo tipo,
            String categorias,
            int umbral) {

        this.id = id;
        this.texto = texto;
        this.tipo = tipo;
        this.categorias = categorias;
        this.umbral = umbral;
    }

    /**
     * indica si la pregunta utiliza categorias
     * provenientes de fuentes externas
     */
    public boolean esAutomatica() {
        return tipo == TipoAtributo.AUTO;
    }

    /**
     * indica si la pregunta debe ser interpretada
     * directamente como una preferencia del usuario
     */
    public boolean esManual() {
        return tipo == TipoAtributo.MANUAL;
    }

    /**
     * indica si la pregunta corresponde a informacion
     * del perfil del viajero
     */
    public boolean esDePerfil() {
        return tipo == TipoAtributo.PERFIL;
    }

    /**
     * indica si la pregunta tiene categorias asociadas
     */
    public boolean tieneCategorias() {
        return categorias != null && !categorias.isBlank();
    }

    /**
     * indica si la pregunta tiene un umbral definido
     * para la evaluacion de una preferencia
     */
    public boolean tieneUmbral() {
        return umbral > 0;
    }
}
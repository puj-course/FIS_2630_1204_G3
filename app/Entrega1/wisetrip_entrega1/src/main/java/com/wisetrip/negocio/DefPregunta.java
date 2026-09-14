package com.wisetrip.negocio;

import com.wisetrip.modelo.Peso;
import com.wisetrip.modelo.TipoAtributo;

public class DefPregunta {

    public final String id;
    public final String texto;
    public final TipoAtributo tipo;
    public final String categorias;
    public final int umbral;
    public final Peso peso;

    public DefPregunta(String id, String texto, TipoAtributo tipo, String categorias, int umbral, Peso peso) {
        this.id = id;
        this.texto = texto;
        this.tipo = tipo;
        this.categorias = categorias;
        this.umbral = umbral;
        this.peso = peso;
    }
}

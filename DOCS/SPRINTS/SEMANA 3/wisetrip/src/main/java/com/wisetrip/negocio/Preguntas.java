package com.wisetrip.negocio;

public class Preguntas {public final String id;

    public final String texto;

    public final TipoAtributo tipo;

    public final String categorias;


     //Cantidad minima de resultados Geoapify

    public final int umbral;

    public final Peso peso;

    public Pregunta(
            String id,
            String texto,
            TipoAtributo tipo,
            String categorias,
            int umbral,
            Peso peso
    ) {

        this.id = id;
        this.texto = texto;
        this.tipo = tipo;
        this.categorias = categorias;
        this.umbral = umbral;
        this.peso = peso;
    }
}

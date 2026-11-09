package com.wisetrip.modelo;

import java.util.Locale;

public enum Importancia {
    NO(0),
    GUSTAR(1),
    PREFIERO(3),
    SI(0);

    private final int peso;

    Importancia(int peso) {
        this.peso = peso;
    }

    public int getPeso() {
        return peso;
    }

    public boolean esPuntuable() {
        return this == GUSTAR || this == PREFIERO;
    }

    public boolean estaSeleccionada() {
        return this != NO;
    }

    public boolean esIndispensable() {
        return this == SI;
    }

    /** Valor en minusculas que envian los formularios (no, gustar, prefiero, si). */
    public String valor() {
        return name().toLowerCase(Locale.ROOT);
    }

    /** Convierte el valor del formulario (en minusculas) a la constante correspondiente. */
    public static Importancia desdeValor(String valor) {
        return valueOf(valor.toUpperCase(Locale.ROOT));
    }
}

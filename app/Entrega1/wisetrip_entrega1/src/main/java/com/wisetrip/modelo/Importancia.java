package com.wisetrip.modelo;

public enum Importancia {
    no(0),
    gustar(1),
    prefiero(3),
    si(0);

    private final int peso;

    Importancia(int peso) {
        this.peso = peso;
    }

    public int getPeso() {
        return peso;
    }

    public boolean esPuntuable() {
        return this == gustar || this == prefiero;
    }

    public boolean estaSeleccionada() {
        return this != no;
    }

    public boolean esIndispensable() {
        return this == si;
    }
}
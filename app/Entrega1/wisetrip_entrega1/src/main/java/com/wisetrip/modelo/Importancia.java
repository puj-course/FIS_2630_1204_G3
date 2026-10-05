package com.wisetrip.modelo;

/** Importancia elegida por el usuario para cada preferencia. */
public enum Importancia {
    no(0),       // No se tiene en cuenta.
    gustar(1),   // Me gustaria.
    prefiero(2), // Lo prefiero: aporta el doble que gustar.
    si(0);       // Es indispensable: se evalua como requisito, no suma puntos.

    public final int peso;

    Importancia(int peso) {
        this.peso = peso;
    }
}

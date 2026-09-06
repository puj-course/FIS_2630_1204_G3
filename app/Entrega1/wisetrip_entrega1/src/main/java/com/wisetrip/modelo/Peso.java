package com.wisetrip.modelo;

public enum Peso {
    GUSTO(10),
    FUERTE(20),
    EXCLUYENTE(35);

    public final int puntaje;

    Peso(int puntaje) { this.puntaje = puntaje; }
}
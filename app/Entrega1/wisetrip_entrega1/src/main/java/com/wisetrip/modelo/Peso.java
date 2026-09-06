//esto toca completarlo en el otro sprint 
//Niveles de peso para el puntaje de preferencias

package com.wisetrip.modelo;

public enum Peso {
    GUSTO(10),
    FUERTE(20),
    EXCLUYENTE(35);

    public final int puntaje;

    Peso(int puntaje) { this.puntaje = puntaje; }
}

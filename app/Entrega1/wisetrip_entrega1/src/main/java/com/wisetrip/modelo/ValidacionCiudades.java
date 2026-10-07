package com.wisetrip.modelo;

import java.util.List;

//Separa incumplimientos confirmados de requisitos que no se pudieron verificar
//ya para mostrar las que son

public record ValidacionCiudades(List<Ciudad> validas, List<Ciudad> descartadas,
                                List<Ciudad> pendientes) {
    public ValidacionCiudades {
        validas = List.copyOf(validas);
        descartadas = List.copyOf(descartadas);
        pendientes = List.copyOf(pendientes);
    }
}

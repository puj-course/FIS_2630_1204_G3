package com.wisetrip.modelo;

import java.util.List;

//Separa incumplimientos confirmados de requisitos que no se pudieron verificar
//ya para mostrar las que son

// HU143: NPM explicito = 0; excluye constructores, heredados y generados.
// NOC = 0: sin hijas directas en src/main/java al 2026-11-11. Recalcular ante cambios.
// Este record genera 6 metodos publicos: 3 accesores, equals, hashCode y toString.
public record ValidacionCiudades(List<Ciudad> validas, List<Ciudad> descartadas,
                                List<Ciudad> pendientes) {
    public ValidacionCiudades {
        validas = List.copyOf(validas);
        descartadas = List.copyOf(descartadas);
        pendientes = List.copyOf(pendientes);
    }
}

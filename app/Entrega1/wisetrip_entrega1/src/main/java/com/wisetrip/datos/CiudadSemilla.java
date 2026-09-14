package com.wisetrip.datos;

import com.wisetrip.modelo.Ciudad;
import java.util.Map;

public record CiudadSemilla(
        String nombre,
        String pais,
        String nivelCosto, // "bajo", "medio", "alto"
        double latitud,
        double longitud,
        Map<String, Boolean> atributosManuales
) {
    public Ciudad aCiudad() {
        Ciudad c = new Ciudad();
        c.setNombre(nombre);
        c.setPais(pais);
        c.setLatitud(latitud);
        c.setLongitud(longitud);
        c.setCostoPromedio(costoDeNivel(nivelCosto));
        return c;
    }

    private static double costoDeNivel(String nivel) {
        return switch (nivel) {
            case "bajo" -> 500.0;
            case "medio" -> 800.0;
            case "alto" -> 1300.0;
            default -> 700.0;
        };
    }
}
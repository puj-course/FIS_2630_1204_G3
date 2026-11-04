package com.wisetrip.datos;
import java.util.Map;

public record CiudadSemilla(
        String nombre,
        String pais,
        double latitud,
        double longitud,
        Map<String, Boolean> atributosManuales
) {

}

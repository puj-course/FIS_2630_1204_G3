package com.wisetrip.datos;

import com.wisetrip.modelo.Ciudad;
import java.util.Map;

public record CiudadSemilla(
        String nombre,
        String pais,
        double latitud,
        double longitud,
        Map<String, Boolean> atributosManuales
) {
    public Ciudad aCiudad(double costoDiarioUsdPorPersona) {
        Ciudad c = new Ciudad();
        c.setNombre(nombre);
        c.setPais(pais);
        c.setLatitud(latitud);
        c.setLongitud(longitud);
        c.setCostoPromedio(costoDiarioUsdPorPersona);
        return c;
    }


}

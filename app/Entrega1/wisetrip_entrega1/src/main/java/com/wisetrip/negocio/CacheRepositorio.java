package com.wisetrip.negocio;

import java.util.Optional;

public interface CacheRepositorio {

    //Vacio: consultar Geoapify
    // Con 0: ya se consulto y no hay lugares ent no se repite

    Optional<Integer> buscar(double latitud, double longitud, String categoria,
                             int radioMetros, int limite);

    // Guarda el conteo de la consulta, incluido cero.
    void guardar(double latitud, double longitud, String categoria,
                 int radioMetros, int limite, int respuesta);
}

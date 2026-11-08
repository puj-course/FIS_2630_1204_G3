package com.wisetrip.negocio;

public interface ProovedorLugares {

    //aqui esta lo del gof proxy
    // Devuelve {latitud, longitud}; un arreglo vacio si no se encontro la ciudad.
    double[] coordenadas(String ciudad, String pais);

    Integer contarLugares(double lat, double lon, String categorias, int radioMetros, int limite);
}

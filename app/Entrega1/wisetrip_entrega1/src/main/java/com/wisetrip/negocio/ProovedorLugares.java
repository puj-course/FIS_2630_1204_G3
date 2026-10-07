package com.wisetrip.negocio;

public interface ProoveedorLugares {

    double[] coordenadas(String ciudad, String pais);

    Integer contarLugares(double lat, double lon, String categorias, int radioMetros, int limite);
}
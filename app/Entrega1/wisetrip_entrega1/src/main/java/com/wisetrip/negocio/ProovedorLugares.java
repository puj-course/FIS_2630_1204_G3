package com.wisetrip.negocio;

public interface ProovedorLugares {

    //aqui esta lo del gof proxy
    double[] coordenadas(String ciudad, String pais);

    Integer contarLugares(double lat, double lon, String categorias, int radioMetros, int limite);
}

package com.wisetrip.modelo;

import java.util.List;

public class SeleccionDestinos {

    private final List<ResultadoRecomendacion> destinos;
    private final String mensaje;

    public SeleccionDestinos(List<ResultadoRecomendacion> destinos, String mensaje) {
        this.destinos = destinos;
        this.mensaje = mensaje;
    }

    public List<ResultadoRecomendacion> getDestinos() { return destinos; }
    public String getMensaje() { return mensaje; }

    public boolean isVacio() { return destinos == null || destinos.isEmpty(); }
    public boolean isIncompleto() { return destinos != null && destinos.size() < 3; }
}
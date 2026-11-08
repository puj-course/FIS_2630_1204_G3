package com.wisetrip.modelo;

import java.io.Serializable;

import java.util.List;

/** Destinos seleccionados y mensaje que se muestran al viajero. */
public class SeleccionDestinos implements Serializable {
    private static final long serialVersionUID = 1L;


    private final List<ResultadoRecomendacion> destinos;
    private final String mensaje;

    public SeleccionDestinos(List<ResultadoRecomendacion> destinos, String mensaje) {
        this.destinos = List.copyOf(destinos);
        this.mensaje = mensaje;
    }

    public List<ResultadoRecomendacion> getDestinos() {
        return destinos;
    }

    public String getMensaje() {
        return mensaje;
    }

    public boolean isVacio() {
        return destinos.isEmpty();
    }
}

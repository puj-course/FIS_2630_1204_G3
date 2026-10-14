package com.wisetrip.servicio;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.wisetrip.modelo.ResultadoRecomendacion;
import com.wisetrip.modelo.SeleccionDestinos;
import com.wisetrip.negocio.ISeleccionDestino;

/**
 * HU#68: elige, entre los destinos recomendados, el que el viajero
 * seleccionó.
 */
@Service
public class SeleccionServicio implements ISeleccionDestino {

    @Override
    public boolean hayDestinos(SeleccionDestinos seleccion) {
        return seleccion != null && !seleccion.isVacio();
    }

    @Override
    public Optional<ResultadoRecomendacion> elegir(SeleccionDestinos seleccion, int idCiudad) {
        if (seleccion == null) {
            return Optional.empty();
        }
        return seleccion.getDestinos().stream()
                .filter(destino -> destino.getCiudad().getId() == idCiudad)
                .findFirst();
    }
}

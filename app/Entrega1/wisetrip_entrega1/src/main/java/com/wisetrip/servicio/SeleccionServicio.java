package com.wisetrip.servicio;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.wisetrip.modelo.ResultadoRecomendacion;
import com.wisetrip.modelo.SeleccionDestinos;
import com.wisetrip.negocio.ISeleccionDestino;

/**
 * HU#68: resuelve cuál de los destinos recomendados eligió el viajero.
 *
 * Implementa la interfaz de negocio ISeleccionDestino.
 *  saca del controlador la regla de buscar
 * el destino elegido; el controlador solo atiende la petición.
 *  toda la lógica de elegir destino vive aquí.
 */
@Service
public class SeleccionServicio implements ISeleccionDestino {

    @Override
    public boolean hayDestinos(SeleccionDestinos seleccion) {
        return seleccion != null && !seleccion.isVacio();
    }

    @Override
    public Optional<ResultadoRecomendacion> elegir(SeleccionDestinos seleccion, int idCiudad) {
        if (!hayDestinos(seleccion)) {
            return Optional.empty();
        }
        return seleccion.getDestinos().stream()
                .filter(r -> r.getCiudad() != null && r.getCiudad().getId() == idCiudad)
                .findFirst();
    }
}
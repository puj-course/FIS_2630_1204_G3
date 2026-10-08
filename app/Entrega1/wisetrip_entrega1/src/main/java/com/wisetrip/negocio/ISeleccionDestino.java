package com.wisetrip.negocio;

import java.util.Optional;

import com.wisetrip.modelo.ResultadoRecomendacion;
import com.wisetrip.modelo.SeleccionDestinos;

/**
 * Interfaz de negocio: elegir uno de los destinos recomendados.
 * SOLID Inversión de dependencias: el controlador depende de este
 * contrato, no de la clase que lo resuelve.
 */
public interface ISeleccionDestino {

    /** Indica si hay destinos recomendados entre los cuales elegir. */
    boolean hayDestinos(SeleccionDestinos seleccion);

    /**
     * Busca, entre los destinos recomendados, el que tiene ese id de ciudad.
     * Devuelve vacío si el id no corresponde a ninguno.
     */
    Optional<ResultadoRecomendacion> elegir(SeleccionDestinos seleccion, int idCiudad);
}
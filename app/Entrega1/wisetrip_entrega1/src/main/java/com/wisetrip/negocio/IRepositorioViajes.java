package com.wisetrip.negocio;

import com.wisetrip.modelo.FechasViaje;

/**
 * Interfaz de negocio para guardar viajes.
 * quien guarda un viaje depende de este contrato, no de PostgreSQL. 
 */
public interface IRepositorioViajes {

    /** Guarda el viaje y devuelve su id. */
    int insertar(int idUsuario, int idCiudad, FechasViaje fechas, double presupuestoUsd);
}
package com.wisetrip.negocio;

import com.wisetrip.modelo.FechasViaje;
import com.wisetrip.modelo.RepartoPresupuesto;

/**
 * Interfaz de negocio para guardar viajes.
 * quien guarda un viaje depende de este contrato, no de PostgreSQL. 
 */
public interface IRepositorioViajes {

    /** Guarda los porcentajes solo si el viaje pertenece al usuario indicado. */
    void guardarReparto(int idViaje, int idUsuario, RepartoPresupuesto reparto);

    /** Guarda el viaje y devuelve su id. */
    int insertar(int idUsuario, int idCiudad, FechasViaje fechas, double presupuestoUsd, String origen);

    /** Actualiza destino, fechas y presupuesto del viaje que pertenece al usuario indicado. */
    void actualizar(int idViaje, int idUsuario, int idCiudad, FechasViaje fechas, double presupuestoUsd, String origen);
}

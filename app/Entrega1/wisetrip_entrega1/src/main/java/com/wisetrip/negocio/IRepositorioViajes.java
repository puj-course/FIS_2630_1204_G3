package com.wisetrip.negocio;

import com.wisetrip.modelo.FechasViaje;
import com.wisetrip.modelo.RepartoPresupuesto;
import com.wisetrip.modelo.Usuario;
import com.wisetrip.modelo.Viaje;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Interfaz de negocio para guardar viajes.
 * quien guarda un viaje depende de este contrato, no de PostgreSQL. 
 */
public interface IRepositorioViajes {

    /** Lista los viajes del usuario que aun no han terminado en la fecha indicada. */
    List<Viaje> listarVigentes(Usuario usuario, LocalDate hoy);

    /** Busca un viaje exclusivamente dentro de los viajes del usuario. */
    Optional<Viaje> buscarPorId(int idViaje, Usuario usuario);

    /** Guarda los porcentajes solo si el viaje pertenece al usuario indicado. */
    void guardarReparto(int idViaje, int idUsuario, RepartoPresupuesto reparto);

    /** Guarda el viaje y devuelve su id. */
    int insertar(int idUsuario, int idCiudad, FechasViaje fechas, double presupuestoUsd, String origen);

    /** Actualiza destino, fechas y presupuesto del viaje que pertenece al usuario indicado. */
    void actualizar(int idViaje, int idUsuario, int idCiudad, FechasViaje fechas, double presupuestoUsd, String origen);
}

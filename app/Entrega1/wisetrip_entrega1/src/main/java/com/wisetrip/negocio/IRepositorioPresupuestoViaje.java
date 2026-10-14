package com.wisetrip.negocio;

import com.wisetrip.modelo.PresupuestoViaje;

/** Contrato para consultar el presupuesto persistido de un viaje. */
public interface IRepositorioPresupuestoViaje {

    /**
     * Busca el presupuesto asociado al viaje, incluyendo el importe total
     * y la moneda seleccionada por el usuario.
     *
     * @throws IllegalArgumentException si no existe el viaje
     */
    PresupuestoViaje buscarPresupuesto(int idViaje);
}

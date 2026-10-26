/**
 * Fechas de viaje.
 * Servicio que valida las fechas del viaje usando una cadena de reglas
 * independientes (patrón Chain of Responsibility): fecha de inicio,
 * fecha de fin, y la relación entre ambas (orden y duración máxima).
 * También calcula la duración y da la fecha de hoy para el formulario.
 */

package com.wisetrip.servicio;

import java.util.Map;

import org.springframework.stereotype.Service;

import com.wisetrip.negocio.IFechasServicio;
import com.wisetrip.modelo.FechasViaje; 

@Service
public class FechasServicio implements IFechasServicio {

    private final ReglaValidacionFechas cadenaValidacion;

    public FechasServicio() {
        ReglaValidacionFechas fechaInicio = new ReglaFechaInicio();
        ReglaValidacionFechas fechaFin = new ReglaFechaFin();
        ReglaValidacionFechas relacion = new ReglaRelacionFechas();

        fechaInicio.siguiente(fechaFin);
        fechaFin.siguiente(relacion);

        this.cadenaValidacion = fechaInicio;
    }

    @Override
    public Map<String, String> validarFechas(FechasViaje fechas) {
        ContextoValidacionFechas contexto = new ContextoValidacionFechas(fechas);
        cadenaValidacion.validar(contexto);
        return contexto.getErrores();
    }

}
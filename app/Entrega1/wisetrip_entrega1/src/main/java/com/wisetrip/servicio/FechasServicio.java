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
import java.time.LocalDate;

@Service
public class FechasServicio implements IFechasServicio {

    private final ReglaValidacionFechas cadenaValidacion;
    private final com.wisetrip.negocio.IRepositorioViajes viajes;

    public FechasServicio(com.wisetrip.negocio.IRepositorioViajes viajes) {
        this.viajes = viajes;
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

    @Override
    public long calcularDuracion(FechasViaje fechas) {
        return fechas.getDuracionDias();
    }

    @Override
    public Map<String, String> validarFechas(FechasViaje fechas, int idUsuario, int idViajeActual) {
        ContextoValidacionFechas contexto = new ContextoValidacionFechas(fechas);
        contexto.setConsultaCruces(() -> viajes.hayCruceFechas(idUsuario, idViajeActual, fechas));
        cadenaValidacion.validar(contexto);
        return contexto.getErrores();
    }

    /** Fecha de hoy en formato yyyy-MM-dd, para el atributo min del input date. */
    @Override
    public String hoy() {
        return LocalDate.now().toString();
    }
}

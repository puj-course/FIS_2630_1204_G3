package com.wisetrip.negocio;

import java.util.Map;
import com.wisetrip.modelo.FechasViaje;

public interface IFechasServicio {
    Map<String, String> validarFechas(FechasViaje fechas);
    long calcularDuracion(FechasViaje fechas);
    String hoy();
}

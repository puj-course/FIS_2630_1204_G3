package com.wisetrip.negocio;

import java.util.Map;
import com.wisetrip.modelo.FechasViaje;

public interface IFechasServicio {
    Map<String, String> validarFechas(FechasViaje fechas);
    Map<String, String> validarFechas(FechasViaje fechas, int idUsuario, int idViajeActual);
    long calcularDuracion(FechasViaje fechas);
    String hoy();
}
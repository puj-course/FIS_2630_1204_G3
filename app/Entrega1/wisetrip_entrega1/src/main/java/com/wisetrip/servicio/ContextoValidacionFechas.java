package com.wisetrip.servicio;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.Map;

import com.wisetrip.modelo.FechasViaje;

/**
 * Contexto de validación de fechas de viaje: contiene las fechas a validar,
 * los errores encontrados y los valores de inicio y fin convertidos a LocalDate.
 */
public class ContextoValidacionFechas {

    private final FechasViaje fechas;
    private final Map<String, String> errores = new LinkedHashMap<>();
    private LocalDate inicio;
    private LocalDate fin;

    public ContextoValidacionFechas(FechasViaje fechas) {
        this.fechas = fechas;
    }

    public FechasViaje getFechas() { return fechas; }
    public Map<String, String> getErrores() { return errores; }
    public void agregarError(String campo, String mensaje) { errores.put(campo, mensaje); }

    public LocalDate getInicio() { return inicio; }
    public void setInicio(LocalDate inicio) { this.inicio = inicio; }

    public LocalDate getFin() { return fin; }
    public void setFin(LocalDate fin) { this.fin = fin; }
}
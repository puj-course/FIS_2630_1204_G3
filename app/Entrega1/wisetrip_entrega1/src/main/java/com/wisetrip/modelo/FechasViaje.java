/**
 * Fechas de viaje.
 * Modelo que almacena la fecha de inicio y fin del viaje, y calcula
 * la cantidad de días de duración (devuelve 0 si las fechas no son válidas).
 */

// mejoras 
//Validar fechaFin >= fechaInicio en el setter
//Validar fechaInicio >= fechaActual (regla de negocio)
//Manejar formatos de fecha alternativos (DD/MM/YYYY)
//Agregar anotaciones @NotNull, @FutureOrPresent
//Agregar método esValido() para validación conjunta
//Lanzar excepción en lugar de retornar 0 en getDuracionDias()

package com.wisetrip.modelo;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class FechasViaje {

    private String fechaInicio;   // formato yyyy-MM-dd
    private String fechaFin;

    public FechasViaje() {
    }

    public String getFechaInicio() { return fechaInicio; }
    public void setFechaInicio(String fechaInicio) { this.fechaInicio = fechaInicio; }

    public String getFechaFin() { return fechaFin; }
    public void setFechaFin(String fechaFin) { this.fechaFin = fechaFin; }

    /** Cantidad de dias del viaje. Devuelve 0 si las fechas no son validas. */
    public long getDuracionDias() {
        try {
            LocalDate inicio = LocalDate.parse(fechaInicio);
            LocalDate fin = LocalDate.parse(fechaFin);
            return ChronoUnit.DAYS.between(inicio, fin) + 1;
        } catch (Exception e) {
            return 0;
        }
    }
}

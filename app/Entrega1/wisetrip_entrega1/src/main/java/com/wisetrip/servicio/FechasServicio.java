package com.wisetrip.servicio;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.wisetrip.modelo.FechasViaje;

@Service
public class FechasServicio {

    private static final int MAX_DIAS = 60;

    public Map<String, String> validarFechas(FechasViaje fechas) {
        Map<String, String> errores = new LinkedHashMap<>();

        LocalDate inicio = null;
        LocalDate fin = null;

        // Fecha de inicio
        if (fechas.getFechaInicio() == null || fechas.getFechaInicio().isBlank()) {
            errores.put("fechaInicio", "Selecciona la fecha de inicio de tu viaje.");
        } else {
            try {
                inicio = LocalDate.parse(fechas.getFechaInicio());
                if (inicio.isBefore(LocalDate.now())) {
                    errores.put("fechaInicio", "La fecha de inicio no puede ser anterior a hoy.");
                    inicio = null;
                }
            } catch (Exception e) {
                errores.put("fechaInicio", "Ingresa una fecha válida.");
            }
        }

        // Fecha de fin
        if (fechas.getFechaFin() == null || fechas.getFechaFin().isBlank()) {
            errores.put("fechaFin", "Selecciona la fecha de regreso.");
        } else {
            try {
                fin = LocalDate.parse(fechas.getFechaFin());
            } catch (Exception e) {
                errores.put("fechaFin", "Ingresa una fecha válida.");
            }
        }

        // Relacion entre ambas
        if (inicio != null && fin != null) {
            if (!fin.isAfter(inicio)) {
                errores.put("fechaFin", "La fecha de regreso debe ser posterior a la de inicio.");
            } else if (ChronoUnit.DAYS.between(inicio, fin) + 1 > MAX_DIAS) {
                errores.put("fechaFin", "Por ahora WiseTrip planea viajes de máximo " + MAX_DIAS + " días.");
            }
        }

        return errores;
    }

    public long calcularDuracion(FechasViaje fechas) {
        return fechas.getDuracionDias();
    }

    /** Fecha de hoy en formato yyyy-MM-dd, para el atributo min del input date. */
    public String hoy() {
        return LocalDate.now().toString();
    }
}
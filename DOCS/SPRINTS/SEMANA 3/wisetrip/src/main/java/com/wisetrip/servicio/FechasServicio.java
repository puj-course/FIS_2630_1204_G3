package com.wisetrip.servicio;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.wisetrip.modelo.FechasViaje;

@Service
public class FechasServicio {

    /**
     * Valida las fechas del viaje.
     * Devuelve un mapa vacio si todo esta bien, o con los mensajes de error.
     */
    public Map<String, String> validarFechas(FechasViaje fechas) {
        Map<String, String> errores = new LinkedHashMap<>();

        if (fechas.getFechaInicio() == null || fechas.getFechaInicio().isBlank()) {
            errores.put("fechaInicio", "Selecciona la fecha de inicio.");
        }
        if (fechas.getFechaFin() == null || fechas.getFechaFin().isBlank()) {
            errores.put("fechaFin", "Selecciona la fecha de finalizacion.");
        }

        // Si falta alguna fecha basica, no seguimos validando las reglas de negocio
        if (!errores.isEmpty()) {
            return errores;
        }

        try {
            LocalDate inicio = LocalDate.parse(fechas.getFechaInicio());
            LocalDate fin = LocalDate.parse(fechas.getFechaFin());
            LocalDate hoy = LocalDate.now();

            if (inicio.isBefore(hoy)) {
                errores.put("fechaInicio", "La fecha de inicio no puede ser anterior a hoy.");
            }
            if (!fin.isAfter(inicio)) {
                errores.put("fechaFin", "La fecha de finalizacion debe ser posterior a la fecha de inicio.");
            }
        } catch (DateTimeParseException e) {
            errores.put("fechaInicio", "Alguna de las fechas ingresadas no es valida.");
        }

        return errores;
    }
}

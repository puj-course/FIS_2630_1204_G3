package com.wisetrip.servicio;

import java.time.DateTimeException;
import java.time.LocalDate;

public class ReglaFechaInicio extends ReglaValidacionFechas {

    @Override
    protected boolean validarPropio(ContextoValidacionFechas contexto) {
        String valor = contexto.getFechas().getFechaInicio();

        if (valor == null || valor.isBlank()) {
            contexto.agregarError("fechaInicio", "Selecciona la fecha de inicio de tu viaje.");
            return true;
        }

        try {
            LocalDate inicio = LocalDate.parse(valor);
            if (inicio.isBefore(LocalDate.now())) {
                contexto.agregarError("fechaInicio", "La fecha de inicio no puede ser anterior a hoy.");
            } else {
                contexto.setInicio(inicio);
            }
        } catch (DateTimeException e) {
            contexto.agregarError("fechaInicio", "Ingresa una fecha válida.");
        }

        return true;
    }
}
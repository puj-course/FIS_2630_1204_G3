package com.wisetrip.servicio;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.ZoneId;

public class ReglaFechaInicio extends ReglaValidacionFechas {

    private static final String CAMPO_FECHA_INICIO = "fechaInicio";

    @Override
    protected void validarPropio(ContextoValidacionFechas contexto) {
        String valor = contexto.getFechas().getFechaInicio();

        if (valor == null || valor.isBlank()) {
            contexto.agregarError(CAMPO_FECHA_INICIO, "Selecciona la fecha de inicio de tu viaje.");
            return ;
        }

        try {
            LocalDate inicio = LocalDate.parse(valor);
            if (inicio.isBefore(LocalDate.now(ZoneId.systemDefault()))) {
              contexto.agregarError(CAMPO_FECHA_INICIO, "Selecciona la fecha de inicio de tu viaje.");
            } else {
                contexto.setInicio(inicio);
            }
        } catch (DateTimeException _) {
            contexto.agregarError(CAMPO_FECHA_INICIO, "Ingresa una fecha válida.");
        }
    }
}
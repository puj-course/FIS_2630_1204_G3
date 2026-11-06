package com.wisetrip.servicio;

import java.time.DateTimeException;
import java.time.LocalDate;

public class ReglaFechaFin extends ReglaValidacionFechas {

    @Override
    protected void validarPropio(ContextoValidacionFechas contexto) {
        String valor = contexto.getFechas().getFechaFin();

        if (valor == null || valor.isBlank()) {
            contexto.agregarError("fechaFin", "Selecciona la fecha de regreso.");
            return ;
        }

        try {
            contexto.setFin(LocalDate.parse(valor));
        } catch (DateTimeException _) {
            contexto.agregarError("fechaFin", "Ingresa una fecha válida.");
        }
    }
}
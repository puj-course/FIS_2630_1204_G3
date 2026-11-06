package com.wisetrip.servicio;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class ReglaRelacionFechas extends ReglaValidacionFechas {

    private static final int MAX_DIAS = 60;
        private static final String CAMPO_FECHA_FIN = "fechaFin";

    @Override
    protected void validarPropio(ContextoValidacionFechas contexto) {
        LocalDate inicio = contexto.getInicio();
        LocalDate fin = contexto.getFin();

        // Si alguna de las dos fechas no es válida, no tiene sentido compararlas.
        if (inicio == null || fin == null) {
            return ;
        }

        if (!fin.isAfter(inicio)) {
            contexto.agregarError(CAMPO_FECHA_FIN, "La fecha de regreso debe ser posterior a la de inicio.");
        } else if (ChronoUnit.DAYS.between(inicio, fin) + 1 > MAX_DIAS) {
            contexto.agregarError(CAMPO_FECHA_FIN, "Por ahora WiseTrip planea viajes de máximo " + MAX_DIAS + " días.");
        } else if (contexto.tieneCruceConOtroViaje()) {
            contexto.agregarError(CAMPO_FECHA_FIN, "Estas fechas se cruzan con otro viaje de tu cuenta. Elige otro rango.");
        }
    }
}

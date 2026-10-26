package com.wisetrip.servicio;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class ReglaRelacionFechas extends ReglaValidacionFechas {

    private static final int MAX_DIAS = 60;

    @Override
    protected boolean validarPropio(ContextoValidacionFechas contexto) {
        LocalDate inicio = contexto.getInicio();
        LocalDate fin = contexto.getFin();

        // Si alguna de las dos fechas no es válida, no tiene sentido compararlas.
        if (inicio == null || fin == null) {
            return false;
        }

        if (!fin.isAfter(inicio)) {
            contexto.agregarError("fechaFin", "La fecha de regreso debe ser posterior a la de inicio.");
        } else if (ChronoUnit.DAYS.between(inicio, fin) + 1 > MAX_DIAS) {
            contexto.agregarError("fechaFin", "Por ahora WiseTrip planea viajes de máximo " + MAX_DIAS + " días.");
        } else if (contexto.tieneCruceConOtroViaje()) {
            contexto.agregarError("fechaFin", "Estas fechas se cruzan con otro viaje de tu cuenta. Elige otro rango.");
        }

        return false;
    }
}

package com.wisetrip.modelo;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.Locale;

/**
 * Un día del itinerario con los rótulos que muestra el calendario
 * (por ejemplo: "2026-11-21", "Lun", "21", "Nov").
 */
public record DiaItinerario(String fecha, String nombreDia, String numero, String mes) {

    private static final Locale ESPANOL = Locale.of("es", "CO");

    public static DiaItinerario de(LocalDate dia) {
        return new DiaItinerario(
                dia.format(DateTimeFormatter.ISO_LOCAL_DATE),
                capitalizar(dia.getDayOfWeek().getDisplayName(TextStyle.SHORT, ESPANOL)),
                String.valueOf(dia.getDayOfMonth()),
                capitalizar(dia.getMonth().getDisplayName(TextStyle.SHORT, ESPANOL)));
    }

    // "lun." -> "Lun"
    private static String capitalizar(String texto) {
        String limpio = texto.replace(".", "");
        return limpio.substring(0, 1).toUpperCase(ESPANOL) + limpio.substring(1);
    }
}

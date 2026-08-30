package com.wisetrip.servicio;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.wisetrip.modelo.Presupuesto;

@Service
public class PresupuestoServicio {

    // Moneda(s) de cada uno de los 20 paises LATAM que maneja WiseTrip.
    // La primera de la lista es la moneda local; se incluye USD como referencia
    // adicional porque es comun usarlo como moneda de viaje en la region.
    private static final Map<String, List<String>> MONEDAS_POR_PAIS = crearMonedasPorPais();

    // Se usa solo cuando todavia no se conoce el pais destino
    // (por ejemplo, mientras esta pantalla se prueba de forma independiente).
    private static final List<String> TODAS_LAS_MONEDAS = List.of(
            "MXN", "GTQ", "HNL", "USD", "NIO", "CRC", "PAB", "BZD", "COP", "VES",
            "CUP", "DOP", "PEN", "BOB", "BRL", "CLP", "ARS", "UYU", "PYG"
    );

    /**
     * Tasas de cambio APROXIMADAS a USD (cuanto vale 1 unidad de esa moneda en dolares).
     * Son valores de referencia para que el algoritmo pueda comparar presupuestos
     * de distintos usuarios/paises en una sola moneda comun. No son tasas en tiempo
     * real: si el equipo necesita mas precision mas adelante, esto se puede reemplazar
     * por una llamada a una API de cambio de divisas.
     */
    private static final Map<String, Double> TASAS_A_USD = crearTasasACambio();

    private static Map<String, Double> crearTasasACambio() {
        Map<String, Double> tasas = new LinkedHashMap<>();
        tasas.put("USD", 1.0);
        tasas.put("MXN", 0.057);
        tasas.put("GTQ", 0.13);
        tasas.put("HNL", 0.040);
        tasas.put("NIO", 0.027);
        tasas.put("CRC", 0.0019);
        tasas.put("PAB", 1.0);     // el balboa esta fijado 1 a 1 con el dolar
        tasas.put("BZD", 0.50);    // el dolar beliceno esta fijado 2 a 1 con el dolar
        tasas.put("COP", 0.00025);
        tasas.put("VES", 0.0079);  // moneda muy volatil, valor aproximado
        tasas.put("CUP", 0.0083);  // moneda muy volatil, valor aproximado
        tasas.put("DOP", 0.017);
        tasas.put("PEN", 0.27);
        tasas.put("BOB", 0.145);
        tasas.put("BRL", 0.17);
        tasas.put("CLP", 0.0010);
        tasas.put("ARS", 0.0010);  // moneda muy volatil, valor aproximado
        tasas.put("UYU", 0.025);
        tasas.put("PYG", 0.00013);
        return tasas;
    }

    /**
     * Convierte el monto ingresado (en la moneda que eligio el usuario) a un
     * valor de referencia en USD, para que el algoritmo compare presupuestos
     * de forma consistente sin importar la moneda original.
     */
    public double convertirAUsd(String monto, String moneda) {
        double valor = Double.parseDouble(monto.trim());
        Double tasa = TASAS_A_USD.get(moneda.trim().toUpperCase());
        if (tasa == null) {
            return valor; // fallback: si la moneda no esta en la tabla, no se convierte
        }
        return valor * tasa;
    }

    private static Map<String, List<String>> crearMonedasPorPais() {
        Map<String, List<String>> mapa = new LinkedHashMap<>();
        mapa.put("México", List.of("MXN", "USD"));
        mapa.put("Guatemala", List.of("GTQ", "USD"));
        mapa.put("Honduras", List.of("HNL", "USD"));
        mapa.put("El Salvador", List.of("USD"));           // El Salvador usa el dolar oficialmente
        mapa.put("Nicaragua", List.of("NIO", "USD"));
        mapa.put("Costa Rica", List.of("CRC", "USD"));
        mapa.put("Panamá", List.of("PAB", "USD"));
        mapa.put("Belize", List.of("BZD", "USD"));
        mapa.put("Colombia", List.of("COP", "USD"));
        mapa.put("Venezuela", List.of("VES", "USD"));
        mapa.put("Cuba", List.of("CUP", "USD"));
        mapa.put("República Dominicana", List.of("DOP", "USD"));
        mapa.put("Ecuador", List.of("USD"));                // Ecuador usa el dolar oficialmente
        mapa.put("Perú", List.of("PEN", "USD"));
        mapa.put("Bolivia", List.of("BOB", "USD"));
        mapa.put("Brasil", List.of("BRL", "USD"));
        mapa.put("Chile", List.of("CLP", "USD"));
        mapa.put("Argentina", List.of("ARS", "USD"));
        mapa.put("Uruguay", List.of("UYU", "USD"));
        mapa.put("Paraguay", List.of("PYG", "USD"));
        return mapa;
    }

    /**
     * Devuelve las monedas disponibles segun el pais destino.
     * Si el pais es null o no se reconoce, devuelve todas las monedas
     * de los paises que maneja WiseTrip (fallback para pruebas independientes).
     */
    public List<String> monedasDisponibles(String paisDestino) {
        if (paisDestino != null && !paisDestino.isBlank()) {
            for (Map.Entry<String, List<String>> entrada : MONEDAS_POR_PAIS.entrySet()) {
                if (entrada.getKey().equalsIgnoreCase(paisDestino.trim())) {
                    return entrada.getValue();
                }
            }
        }
        return TODAS_LAS_MONEDAS;
    }

    /**
     * Valida el presupuesto y la moneda, teniendo en cuenta el pais destino
     * (si ya fue elegido) para saber que monedas son validas.
     */
    public Map<String, String> validarPresupuesto(Presupuesto presupuesto, String paisDestino) {
        Map<String, String> errores = new LinkedHashMap<>();

        if (presupuesto.getMonto() == null || presupuesto.getMonto().isBlank()) {
            errores.put("monto", "Ingresa tu presupuesto.");
        } else if (!presupuesto.getMonto().trim().matches("\\d+(\\.\\d{1,2})?")) {
            errores.put("monto", "El presupuesto debe ser un numero positivo (solo digitos).");
        } else if (Double.parseDouble(presupuesto.getMonto().trim()) <= 0) {
            errores.put("monto", "El presupuesto debe ser mayor a cero.");
        }

        List<String> disponibles = monedasDisponibles(paisDestino);

        if (presupuesto.getMoneda() == null || presupuesto.getMoneda().isBlank()) {
            errores.put("moneda", "Selecciona una moneda.");
        } else if (!disponibles.contains(presupuesto.getMoneda().trim().toUpperCase())) {
            errores.put("moneda", "Selecciona una moneda valida de la lista.");
        }

        return errores;
    }
}

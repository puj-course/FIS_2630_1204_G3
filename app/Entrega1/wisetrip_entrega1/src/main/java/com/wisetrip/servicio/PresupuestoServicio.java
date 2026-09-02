package com.wisetrip.servicio;

import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.wisetrip.modelo.Presupuesto;

@Service
public class PresupuestoServicio {

    /** Codigo de moneda -> nombre para mostrar. */
    private static final Map<String, String> MONEDAS = new LinkedHashMap<>();

    /** Pais destino -> codigo de su moneda local. */
    private static final Map<String, String> MONEDA_POR_PAIS = new LinkedHashMap<>();

    /** Cuantas unidades de cada moneda equivalen a 1 USD (valores aproximados). */
    private static final Map<String, Double> UNIDADES_POR_USD = new LinkedHashMap<>();

    static {
        MONEDAS.put("USD", "Dólar estadounidense (USD)");
        MONEDAS.put("MXN", "Peso mexicano (MXN)");
        MONEDAS.put("GTQ", "Quetzal guatemalteco (GTQ)");
        MONEDAS.put("HNL", "Lempira hondureño (HNL)");
        MONEDAS.put("NIO", "Córdoba nicaragüense (NIO)");
        MONEDAS.put("CRC", "Colón costarricense (CRC)");
        MONEDAS.put("PAB", "Balboa panameño (PAB)");
        MONEDAS.put("BZD", "Dólar beliceño (BZD)");
        MONEDAS.put("COP", "Peso colombiano (COP)");
        MONEDAS.put("VES", "Bolívar venezolano (VES)");
        MONEDAS.put("CUP", "Peso cubano (CUP)");
        MONEDAS.put("DOP", "Peso dominicano (DOP)");
        MONEDAS.put("PEN", "Sol peruano (PEN)");
        MONEDAS.put("BOB", "Boliviano (BOB)");
        MONEDAS.put("BRL", "Real brasileño (BRL)");
        MONEDAS.put("CLP", "Peso chileno (CLP)");
        MONEDAS.put("ARS", "Peso argentino (ARS)");
        MONEDAS.put("UYU", "Peso uruguayo (UYU)");
        MONEDAS.put("PYG", "Guaraní paraguayo (PYG)");

        MONEDA_POR_PAIS.put("México", "MXN");
        MONEDA_POR_PAIS.put("Guatemala", "GTQ");
        MONEDA_POR_PAIS.put("Honduras", "HNL");
        MONEDA_POR_PAIS.put("El Salvador", "USD");
        MONEDA_POR_PAIS.put("Nicaragua", "NIO");
        MONEDA_POR_PAIS.put("Costa Rica", "CRC");
        MONEDA_POR_PAIS.put("Panamá", "PAB");
        MONEDA_POR_PAIS.put("Belize", "BZD");
        MONEDA_POR_PAIS.put("Colombia", "COP");
        MONEDA_POR_PAIS.put("Venezuela", "VES");
        MONEDA_POR_PAIS.put("Cuba", "CUP");
        MONEDA_POR_PAIS.put("República Dominicana", "DOP");
        MONEDA_POR_PAIS.put("Ecuador", "USD");
        MONEDA_POR_PAIS.put("Perú", "PEN");
        MONEDA_POR_PAIS.put("Bolivia", "BOB");
        MONEDA_POR_PAIS.put("Brasil", "BRL");
        MONEDA_POR_PAIS.put("Chile", "CLP");
        MONEDA_POR_PAIS.put("Argentina", "ARS");
        MONEDA_POR_PAIS.put("Uruguay", "UYU");
        MONEDA_POR_PAIS.put("Paraguay", "PYG");

        // Tasas aproximadas y fijas. NO son en tiempo real.
        UNIDADES_POR_USD.put("USD", 1.0);
        UNIDADES_POR_USD.put("MXN", 17.0);
        UNIDADES_POR_USD.put("GTQ", 7.8);
        UNIDADES_POR_USD.put("HNL", 24.7);
        UNIDADES_POR_USD.put("NIO", 36.8);
        UNIDADES_POR_USD.put("CRC", 510.0);
        UNIDADES_POR_USD.put("PAB", 1.0);
        UNIDADES_POR_USD.put("BZD", 2.0);
        UNIDADES_POR_USD.put("COP", 4000.0);
        UNIDADES_POR_USD.put("VES", 36.0);
        UNIDADES_POR_USD.put("CUP", 24.0);
        UNIDADES_POR_USD.put("DOP", 59.0);
        UNIDADES_POR_USD.put("PEN", 3.7);
        UNIDADES_POR_USD.put("BOB", 6.9);
        UNIDADES_POR_USD.put("BRL", 5.0);
        UNIDADES_POR_USD.put("CLP", 950.0);
        UNIDADES_POR_USD.put("ARS", 900.0);
        UNIDADES_POR_USD.put("UYU", 39.0);
        UNIDADES_POR_USD.put("PYG", 7300.0);
    }

    /**
     * Monedas que se le ofrecen al usuario.
     * Si ya se conoce el pais destino, solo su moneda local y USD.
     * Si no, las 19 monedas de la region.
     */
    public Map<String, String> monedasDisponibles(String paisDestino) {
        if (paisDestino != null && MONEDA_POR_PAIS.containsKey(paisDestino)) {
            Map<String, String> reducidas = new LinkedHashMap<>();
            String local = MONEDA_POR_PAIS.get(paisDestino);
            reducidas.put(local, MONEDAS.get(local));
            if (!local.equals("USD")) {
                reducidas.put("USD", MONEDAS.get("USD") + " — referencia");
            }
            return reducidas;
        }
        return MONEDAS;
    }

    public Map<String, String> validarPresupuesto(Presupuesto presupuesto, String paisDestino) {
        Map<String, String> errores = new LinkedHashMap<>();

        // Monto
        if (presupuesto.getMonto() == null || presupuesto.getMonto().isBlank()) {
            errores.put("monto", "Ingresa el presupuesto que tienes disponible.");
        } else {
            double valor = presupuesto.getMontoNumerico();
            if (valor < 0) {
                errores.put("monto", "El presupuesto debe ser un número, sin letras ni símbolos.");
            } else if (valor == 0) {
                errores.put("monto", "El presupuesto debe ser mayor que cero.");
            } else if (valor > 1_000_000_000d) {
                errores.put("monto", "Ingresa un presupuesto realista.");
            }
        }

        // Moneda
        Map<String, String> permitidas = monedasDisponibles(paisDestino);
        if (presupuesto.getMoneda() == null || presupuesto.getMoneda().isBlank()) {
            errores.put("moneda", "Selecciona la moneda de tu presupuesto.");
        } else if (!permitidas.containsKey(presupuesto.getMoneda())) {
            errores.put("moneda", "Selecciona una moneda válida de la lista.");
        }

        return errores;
    }

    /** Equivalente aproximado en dolares, para que el algoritmo pueda comparar. */
    public double convertirAUsd(Presupuesto presupuesto) {
        double valor = presupuesto.getMontoNumerico();
        if (valor < 0) return 0;
        Double tasa = UNIDADES_POR_USD.get(presupuesto.getMoneda());
        if (tasa == null || tasa <= 0) return 0;
        return Math.round((valor / tasa) * 100.0) / 100.0;
    }

    public String nombreMoneda(String codigo) {
        return MONEDAS.getOrDefault(codigo, codigo);
    }

    /** Formatea un numero con separador de miles, para mostrarlo. */
    public String formatear(double valor) {
        return String.format(Locale.US, "%,.2f", valor);
    }
}
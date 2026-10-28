/**
 * Presupuesto y moneda
 * Modelo que representa el presupuesto ingresado por el usuario (monto como texto
 * para validar formato, y código de moneda ISO). Incluye utilidad para convertir
 * el monto a numero, devolviendo -1 si no es valido
 */
package com.wisetrip.modelo;

public class Presupuesto {

    private String monto;
    private String moneda;

    public Presupuesto() {
    }

    public String getMonto() {
        return monto;
    }

    public void setMonto(String monto) {
        this.monto = monto;
    }

    public String getMoneda() {
        return moneda;
    }

    public void setMoneda(String moneda) {
        this.moneda = moneda;
    }

    /**
     * convierte el monto almacenado a un valor numerico
     */
    public double getMontoNumerico() {
        if (monto == null || monto.isBlank()) {
            return -1;
        }

        try {
            String limpio = monto.trim()
                    .replace(".", "")
                    .replace(",", ".")
                    .replace(" ", "");

            double valor = Double.parseDouble(limpio);        
            return Double.isFinite(valor) ? valor : -1;

        } catch (NumberFormatException e) {
            return -1;
        }
    }
}
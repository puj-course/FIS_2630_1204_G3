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

    public Presupuesto(String monto, String moneda) {
        this.monto = monto;
        this.moneda = moneda;
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

            return Double.parseDouble(limpio);

        } catch (NumberFormatException e) {
            return -1;
        }
    }

    /**
     * indica si el presupuesto tiene un monto valido
     */
    public boolean esValido() {
        return getMontoNumerico() > 0;
    }

    /**
     * indica si el presupuesto alcanza para cubrir un costo
     */
    public boolean esSuficientePara(double costo) {
        return esValido() && getMontoNumerico() >= costo;
    }

    /**
     * indica si se ha especificado una moneda
     */
    public boolean tieneMoneda() {
        return moneda != null && !moneda.isBlank();
    }
}
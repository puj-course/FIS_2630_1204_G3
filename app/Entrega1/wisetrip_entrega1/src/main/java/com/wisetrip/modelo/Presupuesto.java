package com.wisetrip.modelo;

public class Presupuesto {

    private String monto;    // se recibe como texto para poder validar el formato
    private String moneda;   // codigo ISO: COP, MXN, USD...

    public Presupuesto() {
    }

    public String getMonto() { return monto; }
    public void setMonto(String monto) { this.monto = monto; }

    public String getMoneda() { return moneda; }
    public void setMoneda(String moneda) { this.moneda = moneda; }

    /** Convierte el texto a numero. Devuelve -1 si no es valido. */
      public double getMontoNumerico() {
        try {
            String limpio = monto.trim().replace(".", "").replace(",", ".").replace(" ", "");
            return Double.parseDouble(limpio);
        } catch (NullPointerException | NumberFormatException e) {
            return -1;
        }
    }
}
package com.wisetrip.modelo;

public class Presupuesto {

    private String monto;
    private String moneda;

    // Constructor vacio: Spring lo necesita para llenar el objeto con los datos del formulario
    public Presupuesto() {
    }

    public String getMonto() { return monto; }
    public void setMonto(String monto) { this.monto = monto; }

    public String getMoneda() { return moneda; }
    public void setMoneda(String moneda) { this.moneda = moneda; }
}

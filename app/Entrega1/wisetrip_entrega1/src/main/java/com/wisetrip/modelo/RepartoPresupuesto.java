package com.wisetrip.modelo;

/**
 * HU#69: cómo el viajero reparte su presupuesto entre las categorías
 * de gasto del viaje. Los valores son porcentajes enteros que deben
 * sumar 100.
 */
public class RepartoPresupuesto {

    private int hospedaje = 35;
    private int alimentacion = 25;
    private int transporte = 20;
    private int actividades = 15;
    private int imprevistos = 5;

    public RepartoPresupuesto() {
    }

    public int getHospedaje() { return hospedaje; }
    public void setHospedaje(int hospedaje) { this.hospedaje = hospedaje; }

    public int getAlimentacion() { return alimentacion; }
    public void setAlimentacion(int alimentacion) { this.alimentacion = alimentacion; }

    public int getTransporte() { return transporte; }
    public void setTransporte(int transporte) { this.transporte = transporte; }

    public int getActividades() { return actividades; }
    public void setActividades(int actividades) { this.actividades = actividades; }

    public int getImprevistos() { return imprevistos; }
    public void setImprevistos(int imprevistos) { this.imprevistos = imprevistos; }

    /** Suma de todos los porcentajes. Debe dar 100. */
    public int getTotal() {
        return hospedaje + alimentacion + transporte + actividades + imprevistos;
    }
}
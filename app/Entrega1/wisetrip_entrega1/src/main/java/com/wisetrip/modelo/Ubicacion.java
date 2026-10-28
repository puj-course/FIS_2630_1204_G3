package com.wisetrip.modelo;

/**
 * Punto de origen del viaje: país, ciudad y un detalle opcional
 * (barrio, aeropuerto o punto de partida).
 *
 * La validación contra el catálogo de países y ciudades la hace
 * ViajeServicio, que es quien conoce ese catálogo.
 *
 * Los getters y setters se conservan porque Spring los usa para llenar
 * el formulario de Origen y las vistas los leen.
 */
public class Ubicacion {

    private String pais;
    private String ciudad;
    private String detalle;   // barrio, aeropuerto o punto de partida

    public Ubicacion() {
    }

    /** Quita los espacios sobrantes al inicio y al final del detalle. */
    public void recortarDetalle() {
        if (detalle != null) {
            detalle = detalle.trim();
        }
    }

    // ===== Getters y setters (formulario y vistas) =====

    public String getPais() { return pais; }
    public void setPais(String pais) { this.pais = pais; }

    public String getCiudad() { return ciudad; }
    public void setCiudad(String ciudad) { this.ciudad = ciudad; }

    public String getDetalle() { return detalle; }
    public void setDetalle(String detalle) { this.detalle = detalle; }
}

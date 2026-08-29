package com.wisetrip.modelo;

public class Ubicacion {

    private String pais;
    private String ciudad;
    private String detalle;   // barrio, aeropuerto o punto de partida, opcional

    public Ubicacion() {
    }

    public String getPais() { return pais; }
    public void setPais(String pais) { this.pais = pais; }

    public String getCiudad() { return ciudad; }
    public void setCiudad(String ciudad) { this.ciudad = ciudad; }

    public String getDetalle() { return detalle; }
    public void setDetalle(String detalle) { this.detalle = detalle; }

    // Texto listo para mostrar: "Bogota, Colombia"
    public String getDescripcion() {
        String texto = ciudad + ", " + pais;
        if (detalle != null && !detalle.isBlank()) {
            texto = detalle + " - " + texto;
        }
        return texto;
    }
}
package com.wisetrip.modelo;

import java.util.LinkedHashMap;
import java.util.Map;

import com.wisetrip.negocio.IUbicable;

/**
 * Punto de origen del viaje: país, ciudad y un detalle opcional
 * (barrio, aeropuerto o punto de partida).
 *
 * Implementa la interfaz de negocio IUbicable (que extiende IValidable).
 * como tiene los datos de la ubicación,
 * es la clase que sabe si está completa, cómo se describe, cómo se
 * limpia y cómo se valida.
 *
 * Los getters y setters se conservan porque Spring los usa para llenar
 * el formulario de Origen y las vistas los leen.
 */
public class Ubicacion implements IUbicable {

    /** Largo máximo del punto de partida (igual al maxlength del formulario). */
    public static final int MAX_DETALLE = 60;

    private String pais;
    private String ciudad;
    private String detalle;   // barrio, aeropuerto o punto de partida

    public Ubicacion() {
    }

    public Ubicacion(String pais, String ciudad, String detalle) {
        this.pais = pais;
        this.ciudad = ciudad;
        this.detalle = detalle;
    }

    // ===== Comportamiento de negocio =====

    @Override
    public boolean estaCompleta() {
        return tieneTexto(pais) && tieneTexto(ciudad);
    }

    @Override
    public boolean tieneDetalle() {
        return tieneTexto(detalle);
    }

    /** Texto listo para mostrar: "Aeropuerto principal - Bogotá, Colombia". */
    @Override
    public String getDescripcion() {
        StringBuilder texto = new StringBuilder();
        if (tieneTexto(ciudad)) {
            texto.append(ciudad);
        }
        if (tieneTexto(pais)) {
            if (texto.length() > 0) texto.append(", ");
            texto.append(pais);
        }
        if (tieneDetalle()) {
            texto.insert(0, detalle + " - ");
        }
        return texto.toString();
    }

    /**
     * Cambia el país. Si es distinto al actual, la ciudad se borra,
     * porque ya no pertenece al nuevo país.
     */
    public void cambiarPais(String nuevoPais) {
        if (pais == null || !pais.equals(nuevoPais)) {
            this.pais = nuevoPais;
            this.ciudad = null;
        }
    }

    /** Quita espacios sobrantes y deja en null los campos vacíos. */
    public void normalizar() {
        pais = limpiar(pais);
        ciudad = limpiar(ciudad);
        detalle = limpiar(detalle);
    }

    @Override
    public Map<String, String> validar() {
        Map<String, String> errores = new LinkedHashMap<>();
        if (!tieneTexto(pais)) {
            errores.put("pais", "Selecciona tu país de origen.");
        }
        if (!tieneTexto(ciudad)) {
            errores.put("ciudad", "Selecciona tu ciudad de origen.");
        }
        if (tieneDetalle() && detalle.trim().length() > MAX_DETALLE) {
            errores.put("detalle", "El punto de partida no puede tener más de "
                    + MAX_DETALLE + " caracteres.");
        }
        return errores;
    }

    private static boolean tieneTexto(String valor) {
        return valor != null && !valor.isBlank();
    }

    private static String limpiar(String valor) {
        if (valor == null) return null;
        String limpio = valor.trim().replaceAll("\\s+", " ");
        return limpio.isEmpty() ? null : limpio;
    }

    // ===== Getters y setters (formulario y vistas) =====

    @Override
    public String getPais() { return pais; }
    public void setPais(String pais) { this.pais = pais; }

    @Override
    public String getCiudad() { return ciudad; }
    public void setCiudad(String ciudad) { this.ciudad = ciudad; }

    @Override
    public String getDetalle() { return detalle; }
    public void setDetalle(String detalle) { this.detalle = detalle; }
}
package com.wisetrip.modelo;

import com.wisetrip.negocio.IDistribuible;

/**
 * HU#69 y HU#79: cómo el viajero reparte su presupuesto entre las
 * categorías de gasto del viaje. Los valores son porcentajes enteros.
 *
 * Implementa la interfaz de negocio IDistribuible.
 * GRASP Experto en información: como tiene los porcentajes, es la clase
 * que sabe cuánto suma, cuánto queda, si se pasa y cuánto dinero le toca
 * a cada categoría.
 *
 * Los getters y setters se conservan porque Spring los usa para llenar
 * el formulario del plan y las vistas los leen con ${reparto.hospedaje}.
 */
public class RepartoPresupuesto implements IDistribuible {

    /** Reparto que sugiere WiseTrip. */
    public static final int SUGERIDO_HOSPEDAJE = 35;
    public static final int SUGERIDO_ALIMENTACION = 25;
    public static final int SUGERIDO_TRANSPORTE = 20;
    public static final int SUGERIDO_ACTIVIDADES = 15;
    public static final int SUGERIDO_IMPREVISTOS = 5;

    private int hospedaje = SUGERIDO_HOSPEDAJE;
    private int alimentacion = SUGERIDO_ALIMENTACION;
    private int transporte = SUGERIDO_TRANSPORTE;
    private int actividades = SUGERIDO_ACTIVIDADES;
    private int imprevistos = SUGERIDO_IMPREVISTOS;

    public RepartoPresupuesto() {
    }

    // ===== Comportamiento de negocio =====

    @Override
    public int porcentajeDe(String categoria) {
        return switch (categoria) {
            case "hospedaje" -> hospedaje;
            case "alimentacion" -> alimentacion;
            case "transporte" -> transporte;
            case "actividades" -> actividades;
            case "imprevistos" -> imprevistos;
            default -> throw new IllegalArgumentException("Categoría desconocida: " + categoria);
        };
    }

    /** Suma de todos los porcentajes. */
    @Override
    public int getTotal() {
        return hospedaje + alimentacion + transporte + actividades + imprevistos;
    }

    // ===== Getters y setters (formulario y vistas) =====

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
}
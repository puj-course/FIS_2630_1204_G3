// Coincidencia de preferencias por categorias y estado de la informacion.

package com.wisetrip.modelo;

import java.io.Serializable;

// HU143: NPM explicito = 6; excluye constructores, heredados y generados.
// NOC = 0: sin hijas directas en src/main/java al 2026-11-11. Recalcular ante cambios.
public class ResultadoRecomendacion implements Serializable {
    private static final long serialVersionUID = 1L;


    private final Ciudad ciudad;
    private final double puntajeTotal;
    private final double puntajePresupuesto;
    private final double puntajePreferencias;
    private final boolean preferenciasPuntuables;
    private final boolean coincidenciaProvisional;

    public ResultadoRecomendacion(Ciudad ciudad, double puntajeTotal,
                                  double puntajePresupuesto, double puntajePreferencias,
                                  boolean preferenciasPuntuables, boolean coincidenciaProvisional) {
        this.ciudad = ciudad;
        this.puntajeTotal = puntajeTotal;
        this.puntajePresupuesto = puntajePresupuesto;
        this.puntajePreferencias = puntajePreferencias;
        this.preferenciasPuntuables = preferenciasPuntuables;
        this.coincidenciaProvisional = coincidenciaProvisional;
    }

    public Ciudad getCiudad() { return ciudad; }
    public double getPuntajeTotal() { return puntajeTotal; }
    public double getPuntajePresupuesto() { return puntajePresupuesto; }
    public double getPuntajePreferencias() { return puntajePreferencias; }
    public boolean isPreferenciasPuntuables() { return preferenciasPuntuables; }
    public boolean isCoincidenciaProvisional() { return coincidenciaProvisional; }
}

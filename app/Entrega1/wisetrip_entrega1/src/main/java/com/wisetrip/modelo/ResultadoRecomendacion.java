package com.wisetrip.modelo;

public class ResultadoRecomendacion {

    private final Ciudad ciudad;
    private final double puntajeTotal;          // 0.0 a 1.0
    private final double puntajePresupuesto;
    private final double puntajePreferencias;

    public ResultadoRecomendacion(Ciudad ciudad, double puntajeTotal,
                                  double puntajePresupuesto, double puntajePreferencias) {
        this.ciudad = ciudad;
        this.puntajeTotal = puntajeTotal;
        this.puntajePresupuesto = puntajePresupuesto;
        this.puntajePreferencias = puntajePreferencias;
    }

    public Ciudad getCiudad() { return ciudad; }
    public double getPuntajeTotal() { return puntajeTotal; }
    public double getPuntajePresupuesto() { return puntajePresupuesto; }
    public double getPuntajePreferencias() { return puntajePreferencias; }

    /** Puntaje como porcentaje entero, para mostrar en la vista. */
    public int getPorcentaje() { return (int) Math.round(puntajeTotal * 100); }
    public int getPorcentajePresupuesto() { return (int) Math.round(puntajePresupuesto * 100); }
    public int getPorcentajePreferencias() { return (int) Math.round(puntajePreferencias * 100); }
}
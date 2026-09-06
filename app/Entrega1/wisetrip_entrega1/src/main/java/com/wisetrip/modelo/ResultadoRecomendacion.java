package com.wisetrip.modelo;

public class ResultadoRecomendacion {

    private final Ciudad ciudad;
    private final double puntajeTotal;
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
}
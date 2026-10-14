package com.wisetrip.negocio;

import java.util.Map;
import com.wisetrip.modelo.RepartoPresupuesto;

public interface IRepartoServicio {
    Map<String, String> listarCategorias();
    Map<String, String> validarReparto(RepartoPresupuesto reparto);
    Map<String, Double> calcularMontos(
            RepartoPresupuesto reparto, double presupuestoTotal);
    double porDia(double monto, long dias);
}

package com.wisetrip.negocio;

import com.wisetrip.modelo.Presupuesto;
import java.util.Map;

public interface GestorPresupuesto {

    Map<String, String> validarPresupuesto(Presupuesto presupuesto, String paisDestino);

    double convertirAUsd(Presupuesto presupuesto);

    Map<String, String> monedasDisponibles(String paisDestino);

    String nombreMoneda(String codigo);
}

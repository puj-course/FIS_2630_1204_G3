package com.wisetrip.negocio;

import java.util.Map;

/**
 * validación de un reparto del presupuesto.
 * quien solo valida no depende de los cálculos.
 */
public interface IValidadorReparto {

    /**
     * Devuelve los errores encontrados, con su clave ("general", "basicos").
     * Si el mapa está vacío, el reparto es válido.
     */
    Map<String, String> validarReparto(IDistribuible reparto);
}
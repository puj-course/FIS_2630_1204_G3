package com.wisetrip.negocio;

import java.util.Map;

/**
 * Interfaz de negocio: validación del reparto del presupuesto (HU#69 y HU#79).
 */
public interface IGestionReparto {

    /**
     * Devuelve los errores encontrados, con su clave ("general", "basicos").
     * Si el mapa está vacío, el reparto es válido.
     */
    Map<String, String> validarReparto(IDistribuible reparto);
}

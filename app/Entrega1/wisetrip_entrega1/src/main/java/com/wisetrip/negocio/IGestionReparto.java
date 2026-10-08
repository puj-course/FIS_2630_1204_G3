package com.wisetrip.negocio;

import java.util.Map;

/*
 * Usa extends para heredar los contratos de IValidadorReparto e
 * ICalculadoraReparto, y agrega el listado de categorías.
 */
public interface IGestionReparto extends IValidadorReparto, ICalculadoraReparto {

    /** Clave y nombre visible de cada categoría, en el orden en que se muestran. */
    Map<String, String> listarCategorias();
}
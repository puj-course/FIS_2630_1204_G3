package com.wisetrip.negocio;

import java.util.Map;

/**
 *carga las ciudades del catálogo en la base de datos.
 * otra forma de cargar ciudades (por ejemplo,
 * desde un archivo) solo tiene que implementar esta interfaz.
 */
public interface ICargadorCiudades {

    /**
     * Inserta las ciudades que tengan un costo diario válido.
     * La clave del mapa es "Ciudad|País".
     * Devuelve cuántas ciudades se insertaron.
     */
    int llenar(Map<String, Double> costosDiariosPorCiudadYPais);
}
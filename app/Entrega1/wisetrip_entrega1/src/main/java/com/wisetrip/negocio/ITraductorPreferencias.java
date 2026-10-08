package com.wisetrip.negocio;

import java.util.Map;
import java.util.Set;

/**
 * traduce las respuestas del cuestionario a los
 * atributos con los que se comparan las ciudades.
 * separa el cuestionario de la base de datos, para
 * que un cambio en uno no obligue a cambiar el otro.
 */
public interface ITraductorPreferencias {

    /** Convierte las respuestas del cuestionario en atributos de destino. */
    Map<String, Boolean> traducir(Map<String, Boolean> respuestasCuestionario);

    /** Indica si una clave del cuestionario tiene equivalencia en los destinos. */
    boolean reconoce(String claveCuestionario);

    /** Atributo de la base de datos que corresponde a una clave, o null si no tiene. */
    String equivalenteDe(String claveCuestionario);

    /** Todos los atributos de destino que se pueden comparar. */
    Set<String> atributosDisponibles();
}
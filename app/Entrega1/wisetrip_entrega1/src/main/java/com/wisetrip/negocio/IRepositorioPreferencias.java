package com.wisetrip.negocio;

import java.util.Map;

/**
 * Interfaz de negocio para guardar las preferencias de un viaje.
 * quien guarda preferencias depende de
 * este contrato, no de PostgreSQL.
 */
public interface IRepositorioPreferencias {

    /**
     * Guarda solo las preferencias marcadas como verdaderas para el viaje.
     * Si una ya existía, no la repite.
     */
    void guardarActivas(int idViaje, Map<String, Boolean> atributosSeleccionados);
}
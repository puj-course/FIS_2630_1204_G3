package com.wisetrip.negocio;

import java.util.Map;

/**
 * Interfaz de negocio para guardar las preferencias de un viaje.
 * quien guarda preferencias depende de
 * este contrato, no de PostgreSQL.
 */
public interface IRepositorioPreferencias {

    /**
     * Reemplaza las preferencias del viaje por las marcadas como verdaderas.
     * Un mapa vacio o sin valores verdaderos elimina las preferencias anteriores.
     * El reemplazo es atomico: si falla, se revierten sus cambios.
     * El ID debe ser positivo y el mapa no puede ser nulo.
     */
    void guardarActivas(int idViaje, Map<String, Boolean> atributosSeleccionados);
}

package com.wisetrip.servicio;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.wisetrip.modelo.Ubicacion;

@Service
public class ViajeServicio {

    // Paises disponibles con sus ciudades
    private static final Map<String, List<String>> CIUDADES_POR_PAIS = new LinkedHashMap<>();

    static {
        CIUDADES_POR_PAIS.put("Colombia", List.of("Bogota", "Medellin", "Cali", "Cartagena", "Barranquilla", "Bucaramanga", "Pereira", "Santa Marta"));
        CIUDADES_POR_PAIS.put("Mexico", List.of("Ciudad de Mexico", "Guadalajara", "Monterrey", "Cancun"));
        CIUDADES_POR_PAIS.put("Peru", List.of("Lima", "Cusco", "Arequipa", "Trujillo"));
        CIUDADES_POR_PAIS.put("Chile", List.of("Santiago", "Valparaiso", "Concepcion"));
        CIUDADES_POR_PAIS.put("Argentina", List.of("Buenos Aires", "Cordoba", "Mendoza", "Rosario"));
        CIUDADES_POR_PAIS.put("Ecuador", List.of("Quito", "Guayaquil", "Cuenca"));
        CIUDADES_POR_PAIS.put("Costa Rica", List.of("San Jose", "Liberia", "Puntarenas"));
        CIUDADES_POR_PAIS.put("Espana", List.of("Madrid", "Barcelona", "Sevilla", "Valencia"));
    }

    public List<String> listarPaises() {
        return new ArrayList<>(CIUDADES_POR_PAIS.keySet());
    }

    public List<String> listarCiudades(String pais) {
        if (pais == null || !CIUDADES_POR_PAIS.containsKey(pais)) {
            return new ArrayList<>();
        }
        return CIUDADES_POR_PAIS.get(pais);
    }

    /**
     * Valida la ubicacion de origen.
     * Devuelve un mapa vacio si todo esta bien.
     */
    public Map<String, String> validarUbicacion(Ubicacion ubicacion) {
        Map<String, String> errores = new LinkedHashMap<>();

        if (ubicacion.getPais() == null || ubicacion.getPais().isBlank()) {
            errores.put("pais", "Selecciona tu pais de origen.");
        } else if (!CIUDADES_POR_PAIS.containsKey(ubicacion.getPais())) {
            errores.put("pais", "Selecciona un pais valido de la lista.");
        }

        if (ubicacion.getCiudad() == null || ubicacion.getCiudad().isBlank()) {
            errores.put("ciudad", "Selecciona tu ciudad de origen.");
        } else if (ubicacion.getPais() != null
                && CIUDADES_POR_PAIS.containsKey(ubicacion.getPais())
                && !CIUDADES_POR_PAIS.get(ubicacion.getPais()).contains(ubicacion.getCiudad())) {
            errores.put("ciudad", "Esa ciudad no pertenece al pais seleccionado.");
        }

        if (ubicacion.getDetalle() != null && ubicacion.getDetalle().trim().length() > 60) {
            errores.put("detalle", "El detalle no puede superar los 60 caracteres.");
        }

        return errores;
    }
}
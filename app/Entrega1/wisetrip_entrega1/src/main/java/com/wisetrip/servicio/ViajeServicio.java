package com.wisetrip.servicio;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.wisetrip.modelo.Ubicacion;

@Service
public class ViajeServicio {

    // Paises disponibles con ciudades recomendadas 
    private static final Map<String, List<String>> CIUDADES_POR_PAIS = new LinkedHashMap<>();

    static {
       CIUDADES_POR_PAIS.put("México", List.of("Ciudad de México", "Guadalajara", "Monterrey", "Cancún", "Puebla", "Tijuana"));
        CIUDADES_POR_PAIS.put("Guatemala", List.of("Ciudad de Guatemala", "Antigua Guatemala", "Quetzaltenango"));
        CIUDADES_POR_PAIS.put("Honduras", List.of("Tegucigalpa", "San Pedro Sula", "La Ceiba"));
        CIUDADES_POR_PAIS.put("El Salvador", List.of("San Salvador", "Santa Ana", "San Miguel"));
        CIUDADES_POR_PAIS.put("Nicaragua", List.of("Managua", "León", "Granada"));
        CIUDADES_POR_PAIS.put("Costa Rica", List.of("San José", "Liberia", "Puntarenas", "Alajuela"));
        CIUDADES_POR_PAIS.put("Panamá", List.of("Ciudad de Panamá", "Colón", "David"));
        CIUDADES_POR_PAIS.put("Belice", List.of("Ciudad de Belice", "San Ignacio", "Belmopán"));
        CIUDADES_POR_PAIS.put("Colombia", List.of("Bogotá", "Medellín", "Cali", "Cartagena", "Barranquilla", "Bucaramanga", "Pereira", "Santa Marta"));
        CIUDADES_POR_PAIS.put("Venezuela", List.of("Caracas", "Maracaibo", "Valencia", "Mérida"));
        CIUDADES_POR_PAIS.put("Cuba", List.of("La Habana", "Santiago de Cuba", "Varadero"));
        CIUDADES_POR_PAIS.put("República Dominicana", List.of("Santo Domingo", "Punta Cana", "Santiago de los Caballeros"));
        CIUDADES_POR_PAIS.put("Ecuador", List.of("Quito", "Guayaquil", "Cuenca", "Manta"));
        CIUDADES_POR_PAIS.put("Perú", List.of("Lima", "Cusco", "Arequipa", "Trujillo", "Iquitos"));
        CIUDADES_POR_PAIS.put("Bolivia", List.of("La Paz", "Santa Cruz de la Sierra", "Cochabamba", "Sucre"));
        CIUDADES_POR_PAIS.put("Brasil", List.of("São Paulo", "Río de Janeiro", "Brasilia", "Salvador", "Florianópolis"));
        CIUDADES_POR_PAIS.put("Chile", List.of("Santiago", "Valparaíso", "Concepción", "Puerto Montt"));
        CIUDADES_POR_PAIS.put("Argentina", List.of("Buenos Aires", "Córdoba", "Mendoza", "Rosario", "Bariloche"));
        CIUDADES_POR_PAIS.put("Uruguay", List.of("Montevideo", "Punta del Este", "Colonia del Sacramento"));
        CIUDADES_POR_PAIS.put("Paraguay", List.of("Asunción", "Ciudad del Este", "Encarnación"));
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

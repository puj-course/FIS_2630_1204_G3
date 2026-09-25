package com.wisetrip.servicio;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.wisetrip.datos.CiudadSemilla;
import com.wisetrip.datos.DatosCiudades;
import com.wisetrip.modelo.Ubicacion;

@Service
public class ViajeServicio {

    private static final Map<String, List<String>> CIUDADES_POR_PAIS = construirCiudadesPorPais();

    static {
        agregarCiudades("México", "Ciudad de México", "Guadalajara", "Monterrey", "Cancún", "Puebla", "Tijuana");
        agregarCiudades("Guatemala", "Ciudad de Guatemala", "Antigua Guatemala", "Quetzaltenango");
        agregarCiudades("Honduras", "Tegucigalpa", "San Pedro Sula", "La Ceiba");
        agregarCiudades("El Salvador", "San Salvador", "Santa Ana", "San Miguel");
        agregarCiudades("Nicaragua", "Managua", "León", "Granada");
        agregarCiudades("Costa Rica", "San José", "Liberia", "Puntarenas", "Alajuela");
        agregarCiudades("Panamá", "Ciudad de Panamá", "Colón", "David");
        agregarCiudades("Belice", "Ciudad de Belice", "San Ignacio", "Belmopán");
        agregarCiudades("Colombia", "Bogotá", "Medellín", "Cali", "Cartagena", "Barranquilla", "Bucaramanga", "Pereira", "Santa Marta");
        agregarCiudades("Venezuela", "Caracas", "Maracaibo", "Valencia", "Mérida");
        agregarCiudades("Cuba", "La Habana", "Santiago de Cuba", "Varadero");
        agregarCiudades("República Dominicana", "Santo Domingo", "Punta Cana", "Santiago de los Caballeros");
        agregarCiudades("Ecuador", "Quito", "Guayaquil", "Cuenca", "Manta");
        agregarCiudades("Perú", "Lima", "Cusco", "Arequipa", "Trujillo", "Iquitos");
        agregarCiudades("Bolivia", "La Paz", "Santa Cruz de la Sierra", "Cochabamba", "Sucre");
        agregarCiudades("Brasil", "São Paulo", "Río de Janeiro", "Brasilia", "Salvador", "Florianópolis");
        agregarCiudades("Chile", "Santiago", "Valparaíso", "Concepción", "Puerto Montt");
        agregarCiudades("Argentina", "Buenos Aires", "Córdoba", "Mendoza", "Rosario", "Bariloche");
        agregarCiudades("Uruguay", "Montevideo", "Punta del Este", "Colonia del Sacramento");
        agregarCiudades("Paraguay", "Asunción", "Ciudad del Este", "Encarnación");
    }

    private static Map<String, List<String>> construirCiudadesPorPais() {
        List<CiudadSemilla> ciudades = DatosCiudades.ciudades();
        Map<String, List<String>> map = new LinkedHashMap<>();

        for (CiudadSemilla semilla : ciudades) {
            String pais = semilla.pais();
            String nombreCiudad = semilla.nombre();
            if (pais != null && nombreCiudad != null) {
                map.computeIfAbsent(pais, k -> new ArrayList<>()).add(nombreCiudad);
            }
        }

        return map;
    }

    private static void agregarCiudades(String pais, String... ciudades) {
        if (pais == null || ciudades == null) {
            return;
        }

        CIUDADES_POR_PAIS.computeIfAbsent(pais, k -> new ArrayList<>());
        for (String ciudad : ciudades) {
            if (ciudad != null && !CIUDADES_POR_PAIS.get(pais).contains(ciudad)) {
                CIUDADES_POR_PAIS.get(pais).add(ciudad);
            }
        }
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

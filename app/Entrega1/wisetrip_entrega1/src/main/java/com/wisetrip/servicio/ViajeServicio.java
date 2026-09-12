package com.wisetrip.servicio;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;
import com.wisetrip.modelo.Ubicacion;
import com.wisetrip.datos.CiudadSemilla;
import com.wisetrip.datos.DatosCiudades;
import java.util.ArrayList;

@Service
public class ViajeServicio {

    // Paises recomendados ya arreglado
    private static final Map<String, List<String>> CIUDADES_POR_PAIS = construirCiudadesPorPais();

    private static Map<String, List<String>> construirCiudadesPorPais() {
        List<CiudadSemilla> ciudades = DatosCiudades.ciudades();
        Map<String, List<String>> map = new LinkedHashMap<>();
        for (int i = 0; i < ciudades.size(); i++) {
            CiudadSemilla semilla = ciudades.get(i);
            String pais = semilla.pais();
            String nombreCiudad = semilla.nombre();

            if(!map.containsKey(pais)){
                map.put(pais, new ArrayList<>());
            }
            map.get(pais).add(nombreCiudad);
        }
        return map;
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

package com.wisetrip.servicio;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.wisetrip.modelo.Ciudad;

@Service
public class CatalogoCiudades {

    /**
     * Traduce las claves del cuestionario (HU#25) a los nombres de atributo
     * que usa la base de datos.
     *
     * IMPORTANTE: cuando el grupo confirme los nombres reales de la tabla
     * "atributo", solo hay que cambiar el valor de la derecha en este mapa.
     * Nada mas en todo el proyecto necesita cambiar.
     */
    private static final Map<String, String> EQUIVALENCIAS = new LinkedHashMap<>();

    static {
        EQUIVALENCIAS.put("destino_playa",        "playa");
        EQUIVALENCIAS.put("destino_montana",      "montana");
        EQUIVALENCIAS.put("destino_naturaleza",   "naturaleza");
        EQUIVALENCIAS.put("clima_calido",         "clima_calido");
        EQUIVALENCIAS.put("clima_frio",           "clima_frio");
        EQUIVALENCIAS.put("clima_nieve",          "nieve");
        EQUIVALENCIAS.put("aventura_actividades", "aventura");
        EQUIVALENCIAS.put("aventura_extremos",    "deportes_extremos");
        EQUIVALENCIAS.put("gastro_tipica",        "comida_tipica");
        EQUIVALENCIAS.put("gastro_gourmet",       "gourmet");
        EQUIVALENCIAS.put("ritmo_nocturna",       "vida_nocturna");
        EQUIVALENCIAS.put("ritmo_urbano",         "urbano");
        EQUIVALENCIAS.put("ritmo_tranquilo",      "tranquilo");
        EQUIVALENCIAS.put("ritmo_compras",        "compras");
        EQUIVALENCIAS.put("cultura_local",        "cultura_historia");
        EQUIVALENCIAS.put("cultura_museos",       "museos");
        EQUIVALENCIAS.put("cultura_religioso",    "sitios_religiosos");
        EQUIVALENCIAS.put("estilo_lujo",          "lujo");
        EQUIVALENCIAS.put("estilo_mochilero",     "mochilero");
    }

    /**
     * Convierte las respuestas del cuestionario en atributos que el
     * algoritmo puede comparar contra las ciudades.
     * Las claves sin equivalencia (ritmo_descanso, ritmo_improvisar,
     * ritmo_actividades, gastro_restricciones) se ignoran porque describen
     * al viajero, no al destino.
     */
    public Map<String, Boolean> traducir(Map<String, Boolean> respuestasCuestionario) {
        Map<String, Boolean> traducidos = new LinkedHashMap<>();
        if (respuestasCuestionario == null) return traducidos;

        for (Map.Entry<String, Boolean> entrada : respuestasCuestionario.entrySet()) {
            String claveBD = EQUIVALENCIAS.get(entrada.getKey());
            if (claveBD != null) {
                traducidos.put(claveBD, entrada.getValue());
            }
        }
        return traducidos;
    }

    /**
     * Ciudades disponibles.
     * Cuando se conecte la base de datos, este metodo se reemplaza por:
     *     return ciudadDAO.obtenerTodas();
     * Costos en USD para todo el viaje, aproximados.
     */
    public List<Ciudad> listarCiudades() {
        List<Ciudad> ciudades = new ArrayList<>();

        ciudades.add(crear(1, "Cartagena", "Colombia", 700,
                "playa", "cultura", "vida_nocturna", "comida_tipica", "clima_calido", "museos"));
        ciudades.add(crear(2, "San Andrés", "Colombia", 850,
                "playa", "clima_calido", "comida_tipica", "pet_friendly"));
        ciudades.add(crear(3, "Medellín", "Colombia", 550,
                "urbano", "vida_nocturna", "cultura", "museos", "clima_calido", "compras", "apto_ninos"));
        ciudades.add(crear(4, "Bogotá", "Colombia", 500,
                "urbano", "cultura", "museos", "clima_frio", "compras", "alta_cocina", "sitios_religiosos"));
        ciudades.add(crear(5, "Cusco", "Perú", 900,
                "montana", "cultura", "naturaleza", "aventura", "clima_frio", "sitios_religiosos"));
        ciudades.add(crear(6, "Ciudad de México", "México", 800,
                "urbano", "cultura", "museos", "comida_tipica", "alta_cocina", "vida_nocturna", "compras"));
        ciudades.add(crear(7, "San José", "Costa Rica", 1100,
                "naturaleza", "aventura", "deportes_extremos", "playa", "clima_calido"));
        ciudades.add(crear(8, "Bariloche", "Argentina", 1300,
                "montana", "nieve", "clima_frio", "naturaleza", "deportes_extremos", "lujo"));
        ciudades.add(crear(9, "Río de Janeiro", "Brasil", 1000,
                "playa", "vida_nocturna", "urbano", "clima_calido", "cultura"));
        ciudades.add(crear(10, "Montañita", "Ecuador", 400,
                "playa", "economico", "vida_nocturna", "clima_calido", "deportes_extremos"));

        return ciudades;
    }

    /** Crea una ciudad marcando como true los atributos que cumple. */
    private Ciudad crear(int id, String nombre, String pais, double costo, String... atributos) {
        Map<String, Boolean> mapa = new LinkedHashMap<>();
        for (String atributo : atributos) {
            mapa.put(atributo, true);
        }
        Ciudad ciudad = new Ciudad();
        ciudad.setId(id);
        ciudad.setNombre(nombre);
        ciudad.setPais(pais);
        ciudad.setCostoPromedio(costo);
        ciudad.setAtributos(mapa);
        return ciudad;
    }
}

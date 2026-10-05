package com.wisetrip.servicio;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.stereotype.Service;


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

}

package com.wisetrip.servicio;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.wisetrip.negocio.ITraductorPreferencias;

/**
 * Traduce las respuestas del cuestionario (HU#25) a los atributos con
 * los que se comparan las ciudades.
 *
 * Implementa la interfaz de negocio ITraductorPreferencias.
 * GRASP Indirección y Fabricación pura: une el cuestionario con la base
 * de datos sin que ninguno de los dos conozca al otro.
 * SOLID Responsabilidad única: solo traduce claves.
 */
@Service
public class CatalogoCiudades implements ITraductorPreferencias {

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
        EQUIVALENCIAS.put("playa",                  "playa");
        EQUIVALENCIAS.put("montana",                "montana");
        EQUIVALENCIAS.put("naturaleza",             "naturaleza");
        EQUIVALENCIAS.put("nieve",                  "nieve");
        EQUIVALENCIAS.put("aventura",               "aventura");
        EQUIVALENCIAS.put("deportes_extremos",      "deportes_extremos");
        EQUIVALENCIAS.put("gastronomico_destacado", "gastronomico_destacado");
        EQUIVALENCIAS.put("gourmet",                "gourmet");
        EQUIVALENCIAS.put("vida_nocturna",          "vida_nocturna");
        EQUIVALENCIAS.put("urbano",                 "urbano");
        EQUIVALENCIAS.put("tranquilo",              "tranquilo");
        EQUIVALENCIAS.put("compras",                "compras");
        EQUIVALENCIAS.put("cultura_historia",       "cultura_historia");
        EQUIVALENCIAS.put("museos",                 "museos");
        EQUIVALENCIAS.put("religioso",              "religioso");
        EQUIVALENCIAS.put("lujo",                   "lujo");
        EQUIVALENCIAS.put("mochilero",              "mochilero");
        EQUIVALENCIAS.put("familiar_kids",          "familiar_kids");
        EQUIVALENCIAS.put("pet_friendly",           "pet_friendly");
    }

    /**
     * Convierte las respuestas del cuestionario en atributos que el
     * algoritmo puede comparar contra las ciudades.
     * Las claves sin equivalencia se ignoran porque describen
     * al viajero, no al destino.
     */
    @Override
    public Map<String, Boolean> traducir(Map<String, Boolean> respuestasCuestionario) {
        Map<String, Boolean> traducidos = new LinkedHashMap<>();
        if (respuestasCuestionario == null) return traducidos;

        for (Map.Entry<String, Boolean> entrada : respuestasCuestionario.entrySet()) {
            String claveBD = equivalenteDe(entrada.getKey());
            if (claveBD != null) {
                traducidos.put(claveBD, entrada.getValue());
            }
        }
        return traducidos;
    }

    @Override
    public boolean reconoce(String claveCuestionario) {
        return claveCuestionario != null && EQUIVALENCIAS.containsKey(claveCuestionario);
    }

    @Override
    public String equivalenteDe(String claveCuestionario) {
        if (claveCuestionario == null) return null;
        return EQUIVALENCIAS.get(claveCuestionario);
    }

    @Override
    public Set<String> atributosDisponibles() {
        return Collections.unmodifiableSet(new LinkedHashSet<>(EQUIVALENCIAS.values()));
    }
}
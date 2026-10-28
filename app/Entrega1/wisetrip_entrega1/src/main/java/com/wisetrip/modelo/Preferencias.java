package com.wisetrip.modelo;

import java.util.LinkedHashMap;
import java.util.Map;

public class Preferencias {

    private Map<String, String> respuestas = new LinkedHashMap<>();

    public Preferencias() {
    }

    public Map<String, String> getRespuestas() {
        return respuestas;
    }

    public void setRespuestas(Map<String, String> respuestas) {
        this.respuestas = respuestas;
    }

    /**
     * el viajero manifiesta interes por un atributo
     * (respondio "gustar", "prefiero" o "si")
     */
    public boolean tienePreferencia(String atributo) {
        String respuesta = respuestas.get(atributo);

        return "gustar".equals(respuesta)
                || "prefiero".equals(respuesta)
                || "si".equals(respuesta);
    }

    /** Cuantas respuestas manifiestan interes por su atributo. */
    public long contarAfirmativas() {
        return respuestas.keySet().stream()
                .filter(this::tienePreferencia)
                .count();
    }
}

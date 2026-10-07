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

    public int getTotalRespondidas() {
        return (int) respuestas.values().stream()
                .filter(v -> v != null && !v.isBlank())
                .count();
    }

    /**
     * el viajero manifiest interes por un atributo
     */
    public boolean tienePreferencia(String atributo) {
        String respuesta = respuestas.get(atributo);

        return "gustar".equals(respuesta)
                || "prefiero".equals(respuesta)
                || "si".equals(respuesta);
    }

    /**
     * el atributo es indispensable para el viajero?
     */
    public boolean esIndispensable(String atributo) {
        return "si".equals(respuestas.get(atributo));
    }

    /**
     * obtiene el nivel de importancia asociado a un atributo
     */
    public Importancia obtenerImportancia(String atributo) {
        String respuesta = respuestas.get(atributo);

        if (respuesta == null || respuesta.isBlank()) {
            return null;
        }

        try {
            return Importancia.valueOf(respuesta);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
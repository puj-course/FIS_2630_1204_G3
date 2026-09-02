package com.wisetrip.modelo;

import java.util.LinkedHashMap;
import java.util.Map;

public class Preferencias {

    // clave de la pregunta -> "si" / "no"
    private Map<String, String> respuestas = new LinkedHashMap<>();

    public Preferencias() {
    }

    public Map<String, String> getRespuestas() { return respuestas; }
    public void setRespuestas(Map<String, String> respuestas) { this.respuestas = respuestas; }

    public int getTotalRespondidas() {
        return (int) respuestas.values().stream()
                .filter(v -> v != null && !v.isBlank())
                .count();
    }
}
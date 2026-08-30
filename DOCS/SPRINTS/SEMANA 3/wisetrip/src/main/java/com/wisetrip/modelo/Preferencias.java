package com.wisetrip.modelo;

import java.util.LinkedHashMap;
import java.util.Map;

public class Preferencias {

    // Cada respuesta queda como clave de la pregunta -> "si" o "no"
    // Ej: "playa" -> "si", "nieve" -> "no"
    private Map<String, String> respuestas = new LinkedHashMap<>();

    public Map<String, String> getRespuestas() { return respuestas; }
    public void setRespuestas(Map<String, String> respuestas) { this.respuestas = respuestas; }
}

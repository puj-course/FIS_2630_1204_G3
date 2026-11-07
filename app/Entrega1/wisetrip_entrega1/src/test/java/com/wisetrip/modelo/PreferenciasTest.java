package com.wisetrip.modelo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Preferencias")
class PreferenciasTest {

    private Preferencias preferencias;

    @BeforeEach
    void setUp() {
        preferencias = new Preferencias();
        preferencias.getRespuestas().put("playa", "gustar");
        preferencias.getRespuestas().put("lujo", "prefiero");
        preferencias.getRespuestas().put("golf", "si");
        preferencias.getRespuestas().put("museos", "no");
    }

    @Test
    @DisplayName("tienePreferencia es true para gustar, prefiero y si")
    void tienePreferencia_afirmativas() {
        assertTrue(preferencias.tienePreferencia("playa"));
        assertTrue(preferencias.tienePreferencia("lujo"));
        assertTrue(preferencias.tienePreferencia("golf"));
    }

    @Test
    @DisplayName("tienePreferencia es false para no y para atributos sin respuesta")
    void tienePreferencia_noYVacio() {
        assertFalse(preferencias.tienePreferencia("museos"));
        assertFalse(preferencias.tienePreferencia("urbano"));
    }

    @Test
    @DisplayName("esIndispensable es true solo cuando la respuesta es si")
    void esIndispensable_soloSi() {
        assertTrue(preferencias.esIndispensable("golf"));
        assertFalse(preferencias.esIndispensable("playa"));
        assertFalse(preferencias.esIndispensable("urbano"));
    }

    @Test
    @DisplayName("setRespuestas reemplaza el mapa de respuestas")
    void setRespuestas_reemplazaElMapa() {
        Map<String, String> nuevas = new LinkedHashMap<>();
        nuevas.put("nieve", "si");

        preferencias.setRespuestas(nuevas);

        assertEquals(1, preferencias.getRespuestas().size());
        assertTrue(preferencias.esIndispensable("nieve"));
    }
}

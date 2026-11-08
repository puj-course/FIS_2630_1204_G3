package com.wisetrip.modelo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Importancia")
class ImportanciaTest {

    @Test
    @DisplayName("cada nivel tiene su peso: no=0, gustar=1, prefiero=3, si=0")
    void getPeso_segunElNivel() {
        assertEquals(0, Importancia.NO.getPeso());
        assertEquals(1, Importancia.GUSTAR.getPeso());
        assertEquals(3, Importancia.PREFIERO.getPeso());
        assertEquals(0, Importancia.SI.getPeso());
    }

    @Test
    @DisplayName("esPuntuable es true solo para gustar y prefiero")
    void esPuntuable_soloGustarYPrefiero() {
        assertTrue(Importancia.GUSTAR.esPuntuable());
        assertTrue(Importancia.PREFIERO.esPuntuable());
        assertFalse(Importancia.NO.esPuntuable());
        assertFalse(Importancia.SI.esPuntuable());
    }

    @Test
    @DisplayName("estaSeleccionada es false solo para no")
    void estaSeleccionada_todoMenosNo() {
        assertFalse(Importancia.NO.estaSeleccionada());
        assertTrue(Importancia.GUSTAR.estaSeleccionada());
        assertTrue(Importancia.PREFIERO.estaSeleccionada());
        assertTrue(Importancia.SI.estaSeleccionada());
    }

    @Test
    @DisplayName("esIndispensable es true solo para si")
    void esIndispensable_soloSi() {
        assertTrue(Importancia.SI.esIndispensable());
        assertFalse(Importancia.PREFIERO.esIndispensable());
    }

    @Test
    @DisplayName("valor devuelve el texto en minúsculas que envía el formulario")
    void valor_enMinusculas() {
        assertEquals("no", Importancia.NO.valor());
        assertEquals("gustar", Importancia.GUSTAR.valor());
        assertEquals("prefiero", Importancia.PREFIERO.valor());
        assertEquals("si", Importancia.SI.valor());
    }

    @Test
    @DisplayName("desdeValor convierte el texto del formulario en la constante")
    void desdeValor_convierteTexto() {
        assertEquals(Importancia.NO, Importancia.desdeValor("no"));
        assertEquals(Importancia.GUSTAR, Importancia.desdeValor("gustar"));
        assertEquals(Importancia.PREFIERO, Importancia.desdeValor("prefiero"));
        assertEquals(Importancia.SI, Importancia.desdeValor("si"));
    }

    @Test
    @DisplayName("desdeValor lanza IllegalArgumentException con un texto desconocido")
    void desdeValor_textoDesconocido_lanzaExcepcion() {
        assertThrows(IllegalArgumentException.class, () -> Importancia.desdeValor("quizas"));
    }
}

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
        // Verifica que la opción NO tenga un peso de 0
        assertEquals(0, Importancia.NO.getPeso());
        // Verifica que la opción GUSTAR tenga un peso de 1
        assertEquals(1, Importancia.GUSTAR.getPeso());
        // Verifica que la opción PREFIERO tenga un peso de 3
        assertEquals(3, Importancia.PREFIERO.getPeso());
        // Verifica que la opción SI tenga un peso de 0
        assertEquals(0, Importancia.SI.getPeso());
    }

    @Test
    @DisplayName("esPuntuable es true solo para gustar y prefiero")
    void esPuntuable_soloGustarYPrefiero() {
        // Comprueba que los niveles GUSTAR y PREFIERO sean puntuables, aportan puntos (true)
        // Comprueba que los niveles NO y SI no sean puntuables , no aportan puntos (false)
        assertTrue(Importancia.GUSTAR.esPuntuable());
        assertTrue(Importancia.PREFIERO.esPuntuable());
        assertFalse(Importancia.NO.esPuntuable());
        assertFalse(Importancia.SI.esPuntuable());
    }

    @Test
    @DisplayName("estaSeleccionada es false solo para no")
    void estaSeleccionada_todoMenosNo() {
        //no selecciono nada false
        assertFalse(Importancia.NO.estaSeleccionada());
        //gustar, prefiero y si seleccionan true
        assertTrue(Importancia.GUSTAR.estaSeleccionada());
        //prefiero seleccionan true
        assertTrue(Importancia.PREFIERO.estaSeleccionada());
        //si seleccionan true
        assertTrue(Importancia.SI.estaSeleccionada());
    }

    @Test
    @DisplayName("esIndispensable es true solo para si")
    void esIndispensable_soloSi() {
        // SI es una condición obligatoria/indispensable (true)
        assertTrue(Importancia.SI.esIndispensable());
        // PREFIERO no es indispensable por defecto (false)
        assertFalse(Importancia.PREFIERO.esIndispensable());
    }

    @Test
    @DisplayName("valor devuelve el texto en minúsculas que envía el formulario")
    void valor_enMinusculas() {
        // revisa cuando fueron enviadas en minusculas
        assertEquals("no", Importancia.NO.valor());
        assertEquals("gustar", Importancia.GUSTAR.valor());
        assertEquals("prefiero", Importancia.PREFIERO.valor());
        assertEquals("si", Importancia.SI.valor());
    }

    @Test
    @DisplayName("desdeValor convierte el texto del formulario en la constante")
    void desdeValor_convierteTexto() {
        // Verifica que el método desdeValor() convierta correctamente 
        // los textos en minúsculas a sus respectivas constantes
        assertEquals(Importancia.NO, Importancia.desdeValor("no"));
        assertEquals(Importancia.GUSTAR, Importancia.desdeValor("gustar"));
        assertEquals(Importancia.PREFIERO, Importancia.desdeValor("prefiero"));
        assertEquals(Importancia.SI, Importancia.desdeValor("si"));
    }

    @Test
    @DisplayName("desdeValor lanza IllegalArgumentException con un texto desconocido")
    void desdeValor_textoDesconocido_lanzaExcepcion() {
        // Verifica que al pasar un texto desconocido a desdeValor(),
        assertThrows(IllegalArgumentException.class, () -> Importancia.desdeValor("quizas"));
    }
}

package com.wisetrip.negocio;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/** Pruebas del metodo por defecto esValido() de IValidable. */
@DisplayName("IValidable")
class IValidableTest {

    @Test
    @DisplayName("sin errores de validacion el objeto es valido")
    void esValido_sinErrores_true() {
        IValidable valido = Map::of;

        assertTrue(valido.esValido());
    }

    @Test
    @DisplayName("con al menos un error el objeto no es valido")
    void esValido_conErrores_false() {
        IValidable invalido = () -> Map.of("pais", "El pais es obligatorio.");

        assertFalse(invalido.esValido());
    }
}

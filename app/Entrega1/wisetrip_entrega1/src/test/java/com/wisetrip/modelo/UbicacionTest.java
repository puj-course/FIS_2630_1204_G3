package com.wisetrip.modelo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Ubicacion")
class UbicacionTest {

    @Test
    @DisplayName("estaCompleta exige país y ciudad con texto")
    void estaCompleta_exigePaisYCiudad() {
        assertTrue(new Ubicacion("Colombia", "Bogotá", null).estaCompleta());
        assertFalse(new Ubicacion("Colombia", " ", null).estaCompleta());
        assertFalse(new Ubicacion(null, "Bogotá", null).estaCompleta());
        assertFalse(new Ubicacion().estaCompleta());
    }

    @Test
    @DisplayName("tieneDetalle es true solo si el detalle tiene texto")
    void tieneDetalle_soloConTexto() {
        assertTrue(new Ubicacion("Colombia", "Bogotá", "Aeropuerto").tieneDetalle());
        assertFalse(new Ubicacion("Colombia", "Bogotá", "  ").tieneDetalle());
        assertFalse(new Ubicacion("Colombia", "Bogotá", null).tieneDetalle());
    }

    @Test
    @DisplayName("getDescripcion arma el texto con ciudad, país y detalle")
    void getDescripcion_variantes() {
        assertEquals("Bogotá, Colombia", new Ubicacion("Colombia", "Bogotá", null).getDescripcion());
        assertEquals("Aeropuerto El Dorado - Bogotá, Colombia",
                new Ubicacion("Colombia", "Bogotá", "Aeropuerto El Dorado").getDescripcion());
        assertEquals("Colombia", new Ubicacion("Colombia", null, null).getDescripcion());
        assertEquals("", new Ubicacion().getDescripcion());
    }

    @Test
    @DisplayName("cambiarPais con otro país borra la ciudad")
    void cambiarPais_otroPais_borraLaCiudad() {
        Ubicacion ubicacion = new Ubicacion("Colombia", "Bogotá", null);

        ubicacion.cambiarPais("Perú");

        assertEquals("Perú", ubicacion.getPais());
        assertNull(ubicacion.getCiudad());
    }

    @Test
    @DisplayName("cambiarPais con el mismo país conserva la ciudad")
    void cambiarPais_mismoPais_conservaLaCiudad() {
        Ubicacion ubicacion = new Ubicacion("Colombia", "Bogotá", null);

        ubicacion.cambiarPais("Colombia");

        assertEquals("Bogotá", ubicacion.getCiudad());
    }

    @Test
    @DisplayName("cambiarPais cuando no había país lo asigna")
    void cambiarPais_sinPaisPrevio_loAsigna() {
        Ubicacion ubicacion = new Ubicacion();

        ubicacion.cambiarPais("Chile");

        assertEquals("Chile", ubicacion.getPais());
    }

    @Test
    @DisplayName("normalizar quita espacios sobrantes y deja null lo que queda vacío")
    void normalizar_limpiaEspacios() {
        Ubicacion ubicacion = new Ubicacion("  Costa   Rica ", " San  José", "   ");

        ubicacion.normalizar();

        assertEquals("Costa Rica", ubicacion.getPais());
        assertEquals("San José", ubicacion.getCiudad());
        assertNull(ubicacion.getDetalle());
    }

    @Test
    @DisplayName("normalizar respeta los valores null")
    void normalizar_respetaNull() {
        Ubicacion ubicacion = new Ubicacion();

        ubicacion.normalizar();

        assertNull(ubicacion.getPais());
        assertNull(ubicacion.getCiudad());
    }

    @Test
    @DisplayName("validar: una ubicación completa no tiene errores")
    void validar_completa_sinErrores() {
        assertTrue(new Ubicacion("Colombia", "Bogotá", "Centro").validar().isEmpty());
    }

    @Test
    @DisplayName("validar: sin país ni ciudad devuelve los dos errores")
    void validar_vacia_dosErrores() {
        Map<String, String> errores = new Ubicacion().validar();

        assertEquals(2, errores.size());
        assertTrue(errores.containsKey("pais"));
        assertTrue(errores.containsKey("ciudad"));
    }

    @Test
    @DisplayName("validar: borde, un detalle de 60 caracteres pasa y uno de 61 falla")
    void validar_detalleEnElLimite() {
        assertFalse(new Ubicacion("Colombia", "Bogotá", "a".repeat(Ubicacion.MAX_DETALLE)).validar()
                .containsKey("detalle"));

        Map<String, String> errores = new Ubicacion("Colombia", "Bogotá", "a".repeat(Ubicacion.MAX_DETALLE + 1)).validar();

        assertTrue(errores.get("detalle").contains("60"));
    }

    @Test
    @DisplayName("esMismaCiudadQue compara ciudad y país sin importar mayúsculas")
    void esMismaCiudadQue_ignoraMayusculas() {
        Ubicacion bogota = new Ubicacion("Colombia", "Bogotá", null);

        assertTrue(bogota.esMismaCiudadQue(new Ubicacion("COLOMBIA", "bogotá", "Norte")));
        assertFalse(bogota.esMismaCiudadQue(new Ubicacion("Colombia", "Cali", null)));
        assertFalse(bogota.esMismaCiudadQue(new Ubicacion("Perú", "Bogotá", null)));
    }

    @Test
    @DisplayName("esMismaCiudadQue es false con null o con datos incompletos")
    void esMismaCiudadQue_nullODatosIncompletos() {
        assertFalse(new Ubicacion("Colombia", "Bogotá", null).esMismaCiudadQue(null));
        assertFalse(new Ubicacion("Colombia", null, null).esMismaCiudadQue(new Ubicacion("Colombia", "Bogotá", null)));
        assertFalse(new Ubicacion(null, "Bogotá", null).esMismaCiudadQue(new Ubicacion("Colombia", "Bogotá", null)));
    }

    @Test
    @DisplayName("los setters actualizan los datos")
    void setters_actualizanLosDatos() {
        Ubicacion ubicacion = new Ubicacion();
        ubicacion.setPais("Chile");
        ubicacion.setCiudad("Santiago");
        ubicacion.setDetalle("Providencia");

        assertEquals("Providencia - Santiago, Chile", ubicacion.getDescripcion());
    }
}

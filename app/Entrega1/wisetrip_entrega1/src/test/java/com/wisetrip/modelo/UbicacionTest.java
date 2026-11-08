package com.wisetrip.modelo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Pruebas de Ubicacion")
class UbicacionTest {

    @Test
    @DisplayName("La descripcion separa ciudad y pais con una coma")
    void descripcionConCiudadYPais() {
        Ubicacion ubicacion = new Ubicacion("Colombia", "Bogota", null);

        String resultadoEsperado = "Bogota, Colombia";
        String resultadoObtenido = ubicacion.getDescripcion();

        assertEquals(resultadoEsperado, resultadoObtenido);
    }

    @Test
    @DisplayName("Sin ciudad, el pais aparece sin coma")
    void descripcionSoloPais() {
        Ubicacion ubicacion = new Ubicacion("Colombia", "Bogota", null);

        ubicacion.setCiudad(null);

        String resultadoEsperado = "Colombia";
        String resultadoObtenido = ubicacion.getDescripcion();

        assertEquals(resultadoEsperado, resultadoObtenido);
    }

    @Test
    @DisplayName("Una ciudad con espacios no agrega una coma")
    void descripcionCiudadEnBlanco() {
        Ubicacion ubicacion = new Ubicacion("Colombia", "Bogota", null);

        ubicacion.setCiudad("   ");

        String resultadoEsperado = "Colombia";
        String resultadoObtenido = ubicacion.getDescripcion();

        assertEquals(resultadoEsperado, resultadoObtenido);
    }

    @Test
    @DisplayName("Sin pais, aparece solo la ciudad")
    void descripcionSoloCiudad() {
        Ubicacion ubicacion = new Ubicacion("Colombia", "Bogota", null);

        ubicacion.setPais(null);

        String resultadoEsperado = "Bogota";
        String resultadoObtenido = ubicacion.getDescripcion();

        assertEquals(resultadoEsperado, resultadoObtenido);
    }

    @Test
    @DisplayName("Sin ciudad ni pais, la descripcion queda vacia")
    void descripcionSinDatos() {
        Ubicacion ubicacion = new Ubicacion("Colombia", "Bogota", null);

        ubicacion.setCiudad(null);
        ubicacion.setPais(null);

        String resultadoEsperado = "";
        String resultadoObtenido = ubicacion.getDescripcion();

        assertEquals(resultadoEsperado, resultadoObtenido);
    }

    @Test
    @DisplayName("El punto de partida aparece antes de la ciudad")
    void descripcionConDetalle() {
        Ubicacion ubicacion = new Ubicacion("Colombia", "Bogota", null);

        ubicacion.setDetalle("Aeropuerto");

        String resultadoEsperado = "Aeropuerto - Bogota, Colombia";
        String resultadoObtenido = ubicacion.getDescripcion();

        assertEquals(resultadoEsperado, resultadoObtenido);
    }

    @Test
    @DisplayName("Una ubicacion con pais y ciudad esta completa")
    void ubicacionCompleta() {
        Ubicacion ubicacion = new Ubicacion("Colombia", "Bogota", null);

        boolean resultadoEsperado = true;
        boolean resultadoObtenido = ubicacion.estaCompleta();

        assertEquals(resultadoEsperado, resultadoObtenido);
    }

    @Test
    @DisplayName("Una ubicacion sin ciudad esta incompleta")
    void ubicacionSinCiudad() {
        Ubicacion ubicacion = new Ubicacion("Colombia", "Bogota", null);

        ubicacion.setCiudad(null);

        boolean resultadoEsperado = false;
        boolean resultadoObtenido = ubicacion.estaCompleta();

        assertEquals(resultadoEsperado, resultadoObtenido);
    }

    @Test
    @DisplayName("Cambiar de pais elimina la ciudad anterior")
    void cambiarPaisBorraCiudad() {
        Ubicacion ubicacion = new Ubicacion("Colombia", "Bogota", null);

        ubicacion.cambiarPais("Peru");

        String resultadoEsperado = null;
        String resultadoObtenido = ubicacion.getCiudad();

        assertEquals(resultadoEsperado, resultadoObtenido);
    }

    @Test
    @DisplayName("Elegir el mismo pais conserva la ciudad")
    void mismoPaisConservaCiudad() {
        Ubicacion ubicacion = new Ubicacion("Colombia", "Bogota", null);

        ubicacion.cambiarPais("Colombia");

        String resultadoEsperado = "Bogota";
        String resultadoObtenido = ubicacion.getCiudad();

        assertEquals(resultadoEsperado, resultadoObtenido);
    }

    @Test
    @DisplayName("Normalizar elimina espacios sobrantes de la ciudad")
    void normalizarCiudad() {
        Ubicacion ubicacion = new Ubicacion("Colombia", "Bogota", null);

        ubicacion.setCiudad("  Santa   Marta  ");
        ubicacion.normalizar();

        String resultadoEsperado = "Santa Marta";
        String resultadoObtenido = ubicacion.getCiudad();

        assertEquals(resultadoEsperado, resultadoObtenido);
    }

    @Test
    @DisplayName("Pais y ciudad validos no generan errores")
    void validarDatosCompletos() {
        Ubicacion ubicacion = new Ubicacion("Colombia", "Bogota", null);

        int resultadoEsperado = 0;
        int resultadoObtenido = ubicacion.validar().size();

        assertEquals(resultadoEsperado, resultadoObtenido);
    }

    @Test
    @DisplayName("Sin pais aparece un mensaje de error")
    void validarSinPais() {
        Ubicacion ubicacion = new Ubicacion("Colombia", "Bogota", null);

        ubicacion.setPais(null);

        String resultadoEsperado = "Selecciona tu país de origen.";
        String resultadoObtenido = ubicacion.validar().get("pais");

        assertEquals(resultadoEsperado, resultadoObtenido);
    }

    @Test
    @DisplayName("Sin ciudad aparece un mensaje de error")
    void validarSinCiudad() {
        Ubicacion ubicacion = new Ubicacion("Colombia", "Bogota", null);

        ubicacion.setCiudad(null);

        String resultadoEsperado = "Selecciona tu ciudad de origen.";
        String resultadoObtenido = ubicacion.validar().get("ciudad");

        assertEquals(resultadoEsperado, resultadoObtenido);
    }

    @Test
    @DisplayName("Un detalle de 60 caracteres es valido")
    void detalleEnLimite() {
        Ubicacion ubicacion = new Ubicacion("Colombia", "Bogota", null);

        ubicacion.setDetalle("a".repeat(60));

        int resultadoEsperado = 0;
        int resultadoObtenido = ubicacion.validar().size();

        assertEquals(resultadoEsperado, resultadoObtenido);
    }

    @Test
    @DisplayName("Un detalle de 61 caracteres genera un error")
    void detalleMuyLargo() {
        Ubicacion ubicacion = new Ubicacion("Colombia", "Bogota", null);

        ubicacion.setDetalle("a".repeat(61));

        String resultadoEsperado = "El punto de partida no puede tener más de 60 caracteres.";
        String resultadoObtenido = ubicacion.validar().get("detalle");

        assertEquals(resultadoEsperado, resultadoObtenido);
    }
}

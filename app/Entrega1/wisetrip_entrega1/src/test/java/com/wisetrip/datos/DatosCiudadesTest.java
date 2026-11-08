package com.wisetrip.datos;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("DatosCiudades")
class DatosCiudadesTest {

    @Test
    @DisplayName("ciudades devuelve el catálogo de ciudades semilla")
    void ciudades_devuelveElCatalogo() {
        assertSame(DatosCiudades.CIUDADES, DatosCiudades.ciudades());
        assertFalse(DatosCiudades.ciudades().isEmpty());
    }

    @Test
    @DisplayName("cada ciudad tiene nombre, país y al menos un atributo")
    void ciudades_tienenDatosCompletos() {
        for (CiudadSemilla ciudad : DatosCiudades.ciudades()) {
            assertFalse(ciudad.nombre().isBlank());
            assertFalse(ciudad.pais().isBlank());
            assertFalse(ciudad.atributosManuales().isEmpty(), "Sin atributos: " + ciudad.nombre());
        }
    }

    @Test
    @DisplayName("porNombreYPais encuentra una ciudad ignorando mayúsculas")
    void porNombreYPais_ignoraMayusculas() {
        CiudadSemilla ciudad = DatosCiudades.porNombreYPais("asunción", "PARAGUAY");

        assertNotNull(ciudad);
        assertEquals("Asunción", ciudad.nombre());
    }

    @Test
    @DisplayName("porNombreYPais devuelve null si la ciudad no existe")
    void porNombreYPais_inexistente_devuelveNull() {
        assertNull(DatosCiudades.porNombreYPais("Atlantis", "Colombia"));
    }

    @Test
    @DisplayName("porNombreYPais resuelve los alias de nombre usados por el mapa")
    void porNombreYPais_resuelveAlias() {
        assertEquals("Cartagena de Indias", DatosCiudades.porNombreYPais("Cartagena", "Colombia").nombre());
        assertEquals("San Carlos de Bariloche", DatosCiudades.porNombreYPais("Bariloche", "Argentina").nombre());
        assertEquals("Brasilia", DatosCiudades.porNombreYPais("Brasília", "Brasil").nombre());
        assertTrue(DatosCiudades.porNombreYPais("Porlamar", "Venezuela").nombre().startsWith("Porlamar"));
        assertTrue(DatosCiudades.porNombreYPais("Chillán", "Chile").nombre().startsWith("Chillán"));
    }
}

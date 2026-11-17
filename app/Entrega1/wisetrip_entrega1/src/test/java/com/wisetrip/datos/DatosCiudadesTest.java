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
        // Se espera que la lista de ciudades semilla sea la misma que la constante CIUDADES
        assertSame(DatosCiudades.CIUDADES, DatosCiudades.ciudades());
        // Se espera que la lista no esté vacía
        assertFalse(DatosCiudades.ciudades().isEmpty());
    }

    @Test
    @DisplayName("cada ciudad tiene nombre, país y al menos un atributo")
    void ciudades_tienenDatosCompletos() {
        for (CiudadSemilla ciudad : DatosCiudades.ciudades()) {
            // El nombre no puede estar en blanco
            assertFalse(ciudad.nombre().isBlank());
            // El país no puede estar en blanco
            assertFalse(ciudad.pais().isBlank());
            //verifica que la ciudad tenga al menos un atributo manual 
            assertFalse(ciudad.atributosManuales().isEmpty(), "Sin atributos: " + ciudad.nombre());
        }
    }

    @Test
    @DisplayName("porNombreYPais encuentra una ciudad ignorando mayúsculas")
    void porNombreYPais_ignoraMayusculas() {
        // realiza la busqueda en min y mayus
        CiudadSemilla ciudad = DatosCiudades.porNombreYPais("asunción", "PARAGUAY");

        //revisa que no este nulo y que el nombre sea el esperado
        assertNotNull(ciudad);
        assertEquals("Asunción", ciudad.nombre());
    }

    @Test
    @DisplayName("porNombreYPais devuelve null si la ciudad no existe")
    void porNombreYPais_inexistente_devuelveNull() {
        // busca una ciudad que no existe
        assertNull(DatosCiudades.porNombreYPais("Atlantis", "Colombia"));
    }

    @Test
    @DisplayName("porNombreYPais resuelve los alias de nombre usados por el mapa")
    void porNombreYPais_resuelveAlias() {
        // busca ciudades por sus alias, entonces busca por un nombre corto como argentina y devuelve
        // el nombre completo de la ciudad
        assertEquals("Cartagena de Indias", DatosCiudades.porNombreYPais("Cartagena", "Colombia").nombre());
        assertEquals("San Carlos de Bariloche", DatosCiudades.porNombreYPais("Bariloche", "Argentina").nombre());
        assertEquals("Brasilia", DatosCiudades.porNombreYPais("Brasília", "Brasil").nombre());
        assertTrue(DatosCiudades.porNombreYPais("Porlamar", "Venezuela").nombre().startsWith("Porlamar"));
        assertTrue(DatosCiudades.porNombreYPais("Chillán", "Chile").nombre().startsWith("Chillán"));
    }
}

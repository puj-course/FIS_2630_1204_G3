package com.wisetrip.servicio;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.wisetrip.modelo.Ciudad;
import com.wisetrip.modelo.ResultadoRecomendacion;
import com.wisetrip.modelo.SeleccionDestinos;

/**
 * Pruebas unitarias de SeleccionServicio (HU#68): encontrar cuál de los
 * destinos recomendados eligió el viajero.
 */
@DisplayName("SeleccionServicio")
class SeleccionServicioTest {

    private SeleccionServicio servicio;
    private SeleccionDestinos seleccion;

    @BeforeEach
    void setUp() {
        servicio = new SeleccionServicio();
        seleccion = new SeleccionDestinos(
                List.of(recomendacion(1, "Cartagena"), recomendacion(2, "Medellin"), recomendacion(3, "Bogota")),
                "Te recomendamos estos destinos");
    }

    private static ResultadoRecomendacion recomendacion(int id, String nombre) {
        Ciudad ciudad = new Ciudad();
        ciudad.setId(id);
        ciudad.setNombre(nombre);
        return new ResultadoRecomendacion(ciudad, 80, 40, 40, true, false);
    }

    // ------------------------------------------------------------ hayDestinos

    @Test
    @DisplayName("hayDestinos es verdadero cuando hay recomendaciones")
    void hayDestinos_conRecomendaciones() {
        assertTrue(servicio.hayDestinos(seleccion));
    }

    @Test
    @DisplayName("hayDestinos es falso si la selección está vacía")
    void hayDestinos_seleccionVacia() {
        assertFalse(servicio.hayDestinos(new SeleccionDestinos(new ArrayList<>(), "Sin destinos")));
    }

    @Test
    @DisplayName("borde: hayDestinos es falso si la selección es null (sesión vencida)")
    void hayDestinos_seleccionNull() {
        assertFalse(servicio.hayDestinos(null));
    }

    // ------------------------------------------------------------ elegir

    @Test
    @DisplayName("elegir devuelve el destino con el id indicado")
    void elegir_idExistente_loDevuelve() {
        Optional<ResultadoRecomendacion> elegido = servicio.elegir(seleccion, 2);

        assertTrue(elegido.isPresent());
        assertEquals("Medellin", elegido.get().getCiudad().getNombre());
    }

    @Test
    @DisplayName("elegir con un id que no fue recomendado devuelve vacío")
    void elegir_idNoRecomendado_vacio() {
        assertTrue(servicio.elegir(seleccion, 99).isEmpty());
    }

    @Test
    @DisplayName("borde: elegir sobre una selección null devuelve vacío sin fallar")
    void elegir_seleccionNull_vacio() {
        assertTrue(servicio.elegir(null, 1).isEmpty());
    }

    @Test
    @DisplayName("borde: una recomendación sin ciudad se ignora y no rompe la búsqueda")
    void elegir_recomendacionSinCiudad_seIgnora() {
        SeleccionDestinos conHueco = new SeleccionDestinos(
                List.of(new ResultadoRecomendacion(null, 0, 0, 0, false, false), recomendacion(5, "Cali")),
                "Mensaje");

        Optional<ResultadoRecomendacion> elegido = servicio.elegir(conHueco, 5);

        assertEquals("Cali", elegido.orElseThrow().getCiudad().getNombre());
    }
}

package com.wisetrip.servicio;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.wisetrip.modelo.Ciudad;
import com.wisetrip.modelo.ResultadoRecomendacion;
import com.wisetrip.modelo.SeleccionDestinos;

/**
 * Pruebas unitarias de SelectorDestinos: muestra máximo 3 destinos, respeta
 * el orden que dejó el recomendador y explica al viajero cómo se ordenaron.
 */
@DisplayName("SelectorDestinos")
class SelectorDestinosTest {

    private SelectorDestinos selector;

    @BeforeEach
    void setUp() {
        selector = new SelectorDestinos();
    }

    private static ResultadoRecomendacion resultado(String nombre, double puntaje) {
        Ciudad ciudad = new Ciudad();
        ciudad.setNombre(nombre);
        return new ResultadoRecomendacion(ciudad, puntaje, 1.0, puntaje, true, false);
    }

    private static List<String> nombres(SeleccionDestinos seleccion) {
        return seleccion.getDestinos().stream().map(r -> r.getCiudad().getNombre()).toList();
    }

    @Test
    @DisplayName("sin resultados devuelve una selección vacía con mensaje explicativo")
    void sinResultados_seleccionVacia() {
        SeleccionDestinos seleccion = selector.seleccionarMejoresDestinos(List.of());

        assertTrue(seleccion.isVacio());
        assertTrue(seleccion.getMensaje().startsWith("No encontramos destinos"));
    }

    @Test
    @DisplayName("borde: lista null se trata igual que vacía")
    void resultadosNull_seleccionVacia() {
        assertTrue(selector.seleccionarMejoresDestinos(null).isVacio());
    }

    @Test
    @DisplayName("borde: un solo resultado avisa que es el único destino")
    void unSoloResultado_mensajeUnico() {
        SeleccionDestinos seleccion = selector.seleccionarMejoresDestinos(List.of(resultado("Cartagena", 0.9)));

        assertEquals(1, seleccion.getDestinos().size());
        assertTrue(seleccion.getMensaje().contains("único destino"));
    }

    @Test
    @DisplayName("con más de 3 resultados muestra solo los 3 primeros, en el mismo orden")
    void masDeTres_muestraLosTresPrimeros() {
        List<ResultadoRecomendacion> ranking = List.of(
                resultado("Cartagena", 0.9), resultado("Medellin", 0.8), resultado("Bogota", 0.7),
                resultado("Cali", 0.6), resultado("Pasto", 0.5));

        SeleccionDestinos seleccion = selector.seleccionarMejoresDestinos(ranking);

        assertEquals(List.of("Cartagena", "Medellin", "Bogota"), nombres(seleccion));
        assertTrue(seleccion.getMensaje().startsWith("Te mostramos 3 destinos."));
    }

    @Test
    @DisplayName("borde: con 2 resultados muestra los 2")
    void dosResultados_muestraLosDos() {
        SeleccionDestinos seleccion = selector.seleccionarMejoresDestinos(
                List.of(resultado("Cartagena", 0.9), resultado("Medellin", 0.4)));

        assertEquals(2, seleccion.getDestinos().size());
        assertTrue(seleccion.getMensaje().startsWith("Te mostramos 2 destinos."));
    }

    @Test
    @DisplayName("puntajes distintos entre los mostrados: dice que no hay empates")
    void puntajesDistintos_sinEmpates() {
        SeleccionDestinos seleccion = selector.seleccionarMejoresDestinos(List.of(
                resultado("A", 0.9), resultado("B", 0.8), resultado("C", 0.7)));

        assertTrue(seleccion.getMensaje().contains("sin empates de puntaje"));
    }

    @Test
    @DisplayName("todos con el mismo puntaje: explica que se ordenó por presupuesto")
    void todosIguales_ordenPorPresupuesto() {
        SeleccionDestinos seleccion = selector.seleccionarMejoresDestinos(List.of(
                resultado("A", 0.5), resultado("B", 0.5), resultado("C", 0.5), resultado("D", 0.5)));

        assertTrue(seleccion.getMensaje().contains("mismo puntaje"));
    }

    @Test
    @DisplayName("empate dentro de los 3 mostrados: explica el desempate por presupuesto")
    void empateEntreLosMostrados_explicaDesempate() {
        SeleccionDestinos seleccion = selector.seleccionarMejoresDestinos(List.of(
                resultado("A", 0.9), resultado("B", 0.7), resultado("C", 0.7), resultado("D", 0.2)));

        assertTrue(seleccion.getMensaje().contains("hay empates de puntaje"));
    }
}

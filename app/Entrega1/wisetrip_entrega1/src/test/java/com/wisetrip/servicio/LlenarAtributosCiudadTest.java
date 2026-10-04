package com.wisetrip.servicio;

import com.wisetrip.datos.CiudadSemilla;
import com.wisetrip.modelo.Ciudad;
import com.wisetrip.modelo.EstadoAtributo;
import com.wisetrip.negocio.CatalogoPreguntas;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class LlenarAtributosCiudadTest {
    @Test
    void sinCredencialesNoSeConfundeErrorConCeroLugares() {
        assertNull(new ServicioGeoapify().contarLugares(0, 0, "tourism", 100, 10));
    }

    @Test
    void conectaConteosConLosTresEstadosSinPerderAtributosManuales() {
        var pregunta = CatalogoPreguntas.atributosAuto().iterator().next();
        verificarConteo(null, pregunta.id, EstadoAtributo.noSabemos);
        verificarConteo(0, pregunta.id, EstadoAtributo.noCumple);
        verificarConteo(pregunta.umbral, pregunta.id, EstadoAtributo.cumple);
        if (pregunta.umbral > 1) {
            verificarConteo(pregunta.umbral - 1, pregunta.id, EstadoAtributo.noSabemos);
        }
    }

    private void verificarConteo(Integer cantidad, String atributo, EstadoAtributo esperado) {
        var geo = new ServicioGeoapify() {
            @Override
            public Integer contarLugares(double lat, double lon, String categorias, int radio, int limite) {
                return cantidad;
            }
        };
        Ciudad ciudad = new Ciudad();
        var semilla = new CiudadSemilla("Prueba", "Pais", 0, 0,
                Map.of("manual", true, atributo, true));
        new LlenarAtributosCiudad(geo).enriquecer(ciudad, semilla, Map.of(atributo, true));
        assertEquals(esperado, ciudad.estadoAtributo(atributo));
        assertEquals(EstadoAtributo.cumple, ciudad.estadoAtributo("manual"));
    }
}

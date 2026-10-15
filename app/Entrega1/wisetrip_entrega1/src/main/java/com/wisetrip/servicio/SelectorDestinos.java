//Aca ya le aparecen al usuario los destinos recomendados y que solo salgan 3. 
//Seleccion de los mejores destinos recomendados

package com.wisetrip.servicio;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.wisetrip.modelo.ResultadoRecomendacion;
import com.wisetrip.modelo.SeleccionDestinos;
import com.wisetrip.negocio.SeleRecomendaciones;

@Service
public class SelectorDestinos implements SeleRecomendaciones  {

    private static final int CANTIDAD_DESTINOS = 3;

    @Override
    public SeleccionDestinos seleccionarMejoresDestinos(
            List<ResultadoRecomendacion> resultados) {

        if (resultados == null || resultados.isEmpty()) {
            return new SeleccionDestinos(
                    new ArrayList<>(),
                    "No encontramos destinos con todos tus requisitos obligatorios verificados."
            );
        }

        // Conserva el orden de RecomendadorDestinos
        // (puntaje + desempate por presupuesto).
        List<ResultadoRecomendacion> ordenados = new ArrayList<>(resultados);

        int limite = Math.min(CANTIDAD_DESTINOS, ordenados.size());

        List<ResultadoRecomendacion> seleccionados =
                new ArrayList<>(ordenados.subList(0, limite));

        if (resultados.size() == 1) {
            return new SeleccionDestinos(seleccionados,
                    "Encontramos un único destino con tus requisitos obligatorios verificados.");
        }

        // El criterio principal se deduce del ranking completo.
        long puntajesDistintos = resultados.stream()
                .map(ResultadoRecomendacion::getPuntajeTotal).distinct().count();
        long puntajesSeleccionados = seleccionados.stream()
                .map(ResultadoRecomendacion::getPuntajeTotal).distinct().count();

        String detalle;
        if (puntajesDistintos == 1) {
            detalle = "Todos los destinos evaluados tienen el mismo puntaje; "
                    + "ordenamos por cercanía a tu presupuesto.";
        } else if (puntajesSeleccionados < seleccionados.size()) {
            // Solo informa empates entre los destinos mostrados.
            detalle = "Ordenamos por coincidencia con tus preferencias; entre los destinos mostrados "
                    + "hay empates de puntaje y usamos la cercanía a tu presupuesto como criterio de desempate.";
        } else {
            detalle = "Ordenamos por coincidencia con tus preferencias, sin empates de puntaje entre los destinos mostrados.";
        }
        String mensaje = "Te mostramos " + seleccionados.size() + " destinos. " + detalle;

        return new SeleccionDestinos(seleccionados, mensaje);
    }
}

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
            List<ResultadoRecomendacion> resultados,
            boolean soloIndispensables) {

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

        String mensaje;

        // Si solo seleccionó requisitos indispensables
        if (soloIndispensables) {

            mensaje = "Destinos que cumplen tus requisitos, ordenados por cercanía a tu presupuesto.";

        } else if (seleccionados.size() < CANTIDAD_DESTINOS) {

            mensaje = "Encontramos " + seleccionados.size()
                    + " destino(s) que se ajustan a lo que buscas.";

        } else {

            mensaje = "Estos son los 3 destinos que mejor coinciden contigo.";
        }

        return new SeleccionDestinos(seleccionados, mensaje);
    }
}
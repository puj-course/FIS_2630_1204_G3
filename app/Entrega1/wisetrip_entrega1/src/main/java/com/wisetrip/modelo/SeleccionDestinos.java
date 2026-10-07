//Ya mostrar los destinos 

package com.wisetrip.modelo;

import java.util.List;
import java.util.ArrayList;
import org.springframework.stereotype.Service;
import com.wisetrip.modelo.ResultadoRecomendacion;
import com.wisetrip.modelo.SeleccionDestinos;
import com.wisetrip.negocio.SeleRecomendaciones;

@Service
public class SelectorDestinos implements SeleRecomendaciones {

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

        // Conserva el orden recibido desde RecomendadorDestinos
        List<ResultadoRecomendacion> ordenados = new ArrayList<>(resultados);

        int limite = Math.min(CANTIDAD_DESTINOS, ordenados.size());

        List<ResultadoRecomendacion> seleccionados =
                new ArrayList<>(ordenados.subList(0, limite));

        String mensaje;

        if (seleccionados.size() < CANTIDAD_DESTINOS) {
            mensaje = "Encontramos " + seleccionados.size()
                    + " destino(s) que se ajustan a lo que buscas.";
        } else {
            mensaje = "Estos son los 3 destinos que mejor coinciden contigo.";
        }

        return new SeleccionDestinos(seleccionados, mensaje);
    }
}
package com.wisetrip.servicio;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.springframework.stereotype.Service;

import com.wisetrip.modelo.ResultadoRecomendacion;
import com.wisetrip.modelo.SeleccionDestinos;

@Service
public class SelectorDestinos {

    private static final int CANTIDAD_DESTINOS = 3;

    public SeleccionDestinos seleccionarMejoresDestinos(List<ResultadoRecomendacion> resultados) {

        if (resultados == null || resultados.isEmpty()) {
            return new SeleccionDestinos(new ArrayList<>(),
                    "No encontramos destinos dentro de tu presupuesto.");
        }

        List<ResultadoRecomendacion> ordenados = new ArrayList<>(resultados);
        ordenados.sort(Comparator.comparingDouble(ResultadoRecomendacion::getPuntajeTotal).reversed());

        int limite = Math.min(CANTIDAD_DESTINOS, ordenados.size());
        List<ResultadoRecomendacion> seleccionados = new ArrayList<>(ordenados.subList(0, limite));

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
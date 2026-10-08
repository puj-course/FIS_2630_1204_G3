package com.wisetrip.negocio;

import java.util.List;

import com.wisetrip.modelo.ResultadoRecomendacion;
import com.wisetrip.modelo.SeleccionDestinos;

public interface SeleRecomendaciones {
    SeleccionDestinos seleccionarMejoresDestinos(
            List<ResultadoRecomendacion> resultados,
            boolean soloIndispensables);
}
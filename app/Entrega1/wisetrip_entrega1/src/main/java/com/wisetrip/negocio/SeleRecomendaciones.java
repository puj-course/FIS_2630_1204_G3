package com.wisetrip.negocio;

import com.wisetrip.modelo.ResultadoRecomendacion;
import com.wisetrip.modelo.SeleccionDestinos;

import java.util.List;

public interface SeleRecomendaciones {
    SeleccionDestinos seleccionarMejoresDestinos(
            List<ResultadoRecomendacion> resultados
    );
}



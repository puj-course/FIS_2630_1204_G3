package com.wisetrip.negocio;

import java.util.List;

import com.wisetrip.modelo.Ciudad;
import com.wisetrip.modelo.PreferenciasUsuario;
import com.wisetrip.modelo.ResultadoRecomendacion;
import com.wisetrip.modelo.ValidacionCiudades;

public interface Recomendador {
    List<ResultadoRecomendacion> recomendarDestinos(
            List<Ciudad> ciudades,
            PreferenciasUsuario preferencias
    );

    ValidacionCiudades validarCiudades(
            List<Ciudad> ciudades,
            PreferenciasUsuario preferencias
    );

    List<ResultadoRecomendacion> puntuarCiudadesValidas(
            ValidacionCiudades validacion,
            PreferenciasUsuario preferencias
    );

    ResultadoRecomendacion calcularPuntajeTotal(
            Ciudad ciudad,
            PreferenciasUsuario preferencias
    );
}

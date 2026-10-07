package com.wisetrip.negocio;

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

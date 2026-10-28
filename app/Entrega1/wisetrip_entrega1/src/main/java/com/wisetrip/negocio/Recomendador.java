package com.wisetrip.negocio;

import java.util.List;

import com.wisetrip.modelo.Ciudad;
import com.wisetrip.modelo.PreferenciasUsuario;
import com.wisetrip.modelo.ResultadoRecomendacion;
import com.wisetrip.modelo.ValidacionCiudades;

public interface Recomendador {

    /** Separa las ciudades en válidas, descartadas y pendientes de verificar. */
    ValidacionCiudades validarCiudades(
            List<Ciudad> ciudades,
            PreferenciasUsuario preferencias
    );

    /** Puntúa y ordena las ciudades válidas de la mejor a la peor. */
    List<ResultadoRecomendacion> puntuarCiudadesValidas(
            ValidacionCiudades validacion,
            PreferenciasUsuario preferencias
    );
}

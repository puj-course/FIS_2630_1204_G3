package com.wisetrip.metricas;

import com.wisetrip.controlador.RecomendacionControlador;
import com.wisetrip.modelo.ResultadoRecomendacion;
import com.wisetrip.modelo.SeleccionDestinos;
import com.wisetrip.servicio.LlenarAtributosCiudad;
import com.wisetrip.servicio.ProxyLugares;
import com.wisetrip.servicio.RecomendadorDestinos;
import com.wisetrip.servicio.SelectorDestinos;
import com.wisetrip.servicio.ServicioGeoapify;
import java.util.List;

public class MetricasRecomendaciones {

    // Class<?> representa la informacion de una clase, sin crear un objeto de ella.
    private static final List<Class<?>> CLASES_SELECCIONADAS = List.of(
            RecomendacionControlador.class,
            RecomendadorDestinos.class,
            SelectorDestinos.class,
            LlenarAtributosCiudad.class,
            ProxyLugares.class,
            ServicioGeoapify.class,
            ResultadoRecomendacion.class,
            SeleccionDestinos.class
    );

    // ValidacionCiudades queda fuera: es un record con metodos generados por Java.
    // Este programa se ejecuta por separado, sin iniciar Spring ni consultar servicios.
    public static void main(String[] args) {
        System.out.println("HU143 - Alcance del analisis de recomendaciones");
        System.out.println("Clases seleccionadas: " + CLASES_SELECCIONADAS.size());

        for (Class<?> clase : CLASES_SELECCIONADAS) {
            System.out.println("- " + clase.getSimpleName());
        }
    }
}

package com.wisetrip.metricas;

import com.wisetrip.controlador.RecomendacionControlador;
import com.wisetrip.modelo.ResultadoRecomendacion;
import com.wisetrip.modelo.SeleccionDestinos;
import com.wisetrip.servicio.LlenarAtributosCiudad;
import com.wisetrip.servicio.ProxyLugares;
import com.wisetrip.servicio.RecomendadorDestinos;
import com.wisetrip.servicio.SelectorDestinos;
import com.wisetrip.servicio.ServicioGeoapify;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.List;

public class Metricas {

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

    // NPM: numero de metodos publicos declarados en la propia clase.
    public static int calcularNPM(Class<?> clase) {
        int cantidad = 0;

        // getDeclaredMethods no incluye constructores ni metodos heredados.
        for (Method metodo : clase.getDeclaredMethods()) {
            // Los metodos sinteticos y puente son detalles generados por el compilador.
            if (metodo.isSynthetic() || metodo.isBridge()) {
                continue;
            }

            if (Modifier.isPublic(metodo.getModifiers())) {
                cantidad++;
            }
        }

        return cantidad;
    }

    // NOC: numero de hijas directas dentro de las clases recibidas como alcance.
    public static int calcularNOC(Class<?> clase, List<Class<?>> clasesDelAlcance) {
        int cantidad = 0;

        for (Class<?> candidata : clasesDelAlcance) {
            // getSuperclass devuelve solo el padre directo; no incluye interfaces ni abuelos.
            if (candidata.getSuperclass() == clase) {
                cantidad++;
            }
        }

        return cantidad;
    }

    // NOA: numero de atributos declarados directamente, de cualquier visibilidad.
    public static int calcularNOA(Class<?> clase) {
        int cantidad = 0;

        // Incluye constantes y campos static; no incluye heredados ni variables locales.
        for (Field atributo : clase.getDeclaredFields()) {
            // Excluye campos auxiliares generados por el compilador.
            if (!atributo.isSynthetic()) {
                cantidad++;
            }
        }

        return cantidad;
    }

    // ValidacionCiudades queda fuera: es un record con metodos generados por Java.
    // Este programa se ejecuta por separado, sin iniciar Spring ni consultar servicios.
    public static void main(String[] args) {
        System.out.println("HU143 - Alcance del analisis de recomendaciones");
        System.out.println("Clases seleccionadas: " + CLASES_SELECCIONADAS.size());
        System.out.printf("%-30s %5s %5s%n", "Clase", "NPM", "NOC");

        for (Class<?> clase : CLASES_SELECCIONADAS) {
            int npm = calcularNPM(clase);
            int noc = calcularNOC(clase, CLASES_SELECCIONADAS);
            System.out.printf("%-30s %5d %5d%n", clase.getSimpleName(), npm, noc);
        }
    }
}

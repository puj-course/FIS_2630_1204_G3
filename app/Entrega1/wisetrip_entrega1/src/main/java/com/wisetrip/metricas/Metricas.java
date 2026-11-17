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

public class Metricas {

    //arreglo de clases que se eligieron
    private static final Class<?>[] CLASES_SELECCIONADAS = {
            RecomendacionControlador.class,
            RecomendadorDestinos.class,
            SelectorDestinos.class,
            LlenarAtributosCiudad.class,
            ProxyLugares.class,
            ServicioGeoapify.class,
            ResultadoRecomendacion.class,
            SeleccionDestinos.class
    };

    //NPM number of public methods
    public static int NPM(Class<?> clase) {
        int cantidadMetodosPublicos = 0;

        Method[] metodos = clase.getDeclaredMethods();
        for (int i = 0; i < metodos.length; i++) {
            Method metodo = metodos[i];

            if (Modifier.isPublic(metodo.getModifiers())) {
                cantidadMetodosPublicos++;
            }
        }

        return cantidadMetodosPublicos;
    }

    // NOC: numero de hijas directas dentro de las clases recibidas como alcance.
    public static int NOC(Class<?> clase, Class<?>[] clasesDelAlcance) {
        int cantidadHijasDirectas = 0;

        for (int i = 0; i < clasesDelAlcance.length; i++) {
            Class<?> candidata = clasesDelAlcance[i];
            // getSuperclass devuelve solo el padre directo; no incluye interfaces ni abuelos.
            Class<?> padre = candidata.getSuperclass();
            if (padre == clase) {
                cantidadHijasDirectas++;
            }
        }

        return cantidadHijasDirectas;
    }

    // NOA: numero de atributos declarados directamente, de cualquier visibilidad.
    public static int NOA(Class<?> clase) {
        int cantidadAtributos = 0;

        // Incluye constantes y campos static; no incluye heredados ni variables locales.
        Field[] atributos = clase.getDeclaredFields();
        for (int i = 0; i < atributos.length; i++) {
            Field atributo = atributos[i];
            // Excluye campos auxiliares generados por el compilador.
            if (!atributo.isSynthetic()) {
                cantidadAtributos++;
            }
        }

        return cantidadAtributos;
    }

    // ValidacionCiudades queda fuera: es un record con metodos generados por Java.
    // Este programa se ejecuta por separado, sin iniciar Spring ni consultar servicios.
    public static void main(String[] args) {
        System.out.println("HU143 - Alcance del analisis de recomendaciones");
        System.out.println("Clases seleccionadas: " + CLASES_SELECCIONADAS.length);
        System.out.println("NPM: metodos publicos propios; sin constructores ni heredados.");
        System.out.println("     No se excluyen metodos sinteticos o puente en esta version.");
        System.out.println("NOC: hijas directas SOLO entre las clases seleccionadas; implements no cuenta.");
        System.out.println("NOA: atributos propios de cualquier visibilidad, incluidos static y constantes;");
        System.out.println("     sin heredados, variables locales ni campos sinteticos.");
        System.out.println("Se excluyen interfaces, records y clases internas del alcance seleccionado.");
        System.out.println();

        for (int i = 0; i < CLASES_SELECCIONADAS.length; i++) {
            Class<?> clase = CLASES_SELECCIONADAS[i];
            int npm = NPM(clase);
            int noc = NOC(clase, CLASES_SELECCIONADAS);
            int noa = NOA(clase);
            System.out.println("Clase: " + clase.getSimpleName());
            System.out.println("NPM: " + npm);
            System.out.println("NOC: " + noc);
            System.out.println("NOA: " + noa);
            System.out.println("--------------------------------");
        }
    }
}

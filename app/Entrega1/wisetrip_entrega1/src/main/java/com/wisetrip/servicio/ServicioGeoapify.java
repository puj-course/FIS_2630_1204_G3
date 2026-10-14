//hicimos que en la terminal mostrara el numero de request que se le hacia a la api
//para saber cuantas API calls se hacian en tiempo real
//controlar los 3000 requests gratis y ver cuanto se demora

package com.wisetrip.servicio;

import com.wisetrip.negocio.ProovedorLugares;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.google.gson.Gson;

//coordenadas: dada una ciudad, devuelve su latitud y longitud.
//contarLugares: cuenta cuántos lugares de ciertas categorías hay cerca de un punto.

@Service
public class ServicioGeoapify implements ProovedorLugares {

    // Escribe mensajes en la consola (así vemos cuántas llamadas se hacen)
    private static final Logger REGISTRO = Logger.getLogger(ServicioGeoapify.class.getName());

    // Tiempo max 8 creo que esta bien
    private static final Duration TIEMPO_MAXIMO_ESPERA = Duration.ofSeconds(8);

    // application properties
    @Value("${geoapify.api-key:}")
    private String claveApi;

    // Envia las peticiones por internet. Se crea una vez y se reutiliza.
    private final HttpClient clienteHttp = HttpClient.newHttpClient();
    //con long no funciono
    private final AtomicLong contadorLlamadas = new AtomicLong();

    @Override
    public double[] coordenadas(String ciudad, String pais) {
        try {
            //texto a buscar en formato válido para la URL
            String textoBuscado = URLEncoder.encode(ciudad + ", " + pais, StandardCharsets.UTF_8);
            String direccionUrl = "https://api.geoapify.com/v1/geocode/search?text=" + textoBuscado + "&apiKey=" + claveApi;

            // 2. Preparamos y enviamos la peticion
            HttpRequest peticion = HttpRequest.newBuilder()
                    //a donde va
                    .uri(URI.create(direccionUrl))
                    .timeout(TIEMPO_MAXIMO_ESPERA)
                    .GET()
                    .build();
            HttpResponse<String> respuesta = clienteHttp.send(peticion, HttpResponse.BodyHandlers.ofString());

            //convertimos el texto JSON de la respuesta en objetos Java
            Gson convertidorJson = new Gson();
            RespuestaGeoapify datos = convertidorJson.fromJson(respuesta.body(), RespuestaGeoapify.class);

            if (datos.features == null || datos.features.isEmpty())
                return null;

            //Geoapify entrega longitud, latitud
            List<Double> longitudYLatitud = datos.features.get(0).geometry.coordinates;

            return new double[]{longitudYLatitud.get(1), longitudYLatitud.get(0)};
        } catch (Exception e) {
            System.err.println("Error consultando Geoapify para " + ciudad + ": " + e.getMessage());
            return null;
        }
    }

    /**
     * Cuenta cuantos lugares de las categoriaas dadas hay dentro de un circulo.
     * Devuelve null si algo fallo o no sabemos
     * maximoResultados: tope de lugares que le pedimos a Geoapify. Mientras mas bajo,
     * menos créditos gasta
     * SUPER IMPORTANTE!!!! (-1 credito por cada 20 lugares)
     */

    @Override
    public Integer contarLugares(double latitud, double longitud, String categorias, int radioMetros, int maximoResultados) {
        // Sin clave no podemos consultar
        if (claveApi == null || claveApi.isBlank()) {
            REGISTRO.warning("Geoapify sin API key no consulto bien");
            return null;
        }

        long numeroDeLlamada = contadorLlamadas.incrementAndGet();
        long momentoInicio = System.nanoTime();
        try {
            // Armamos la URL: categorias + circulo + tope de resultados
            String direccionUrl = "https://api.geoapify.com/v2/places"
                    + "?categories=" + categorias
                    + "&filter=circle:" + longitud + "," + latitud + "," + radioMetros
                    + "&limit=" + maximoResultados
                    + "&apiKey=" + claveApi;
            HttpRequest peticion = HttpRequest.newBuilder()
                    .uri(URI.create(direccionUrl))
                    .timeout(TIEMPO_MAXIMO_ESPERA)
                    .GET()
                    .build();
            HttpResponse<String> respuesta = clienteHttp.send(peticion, HttpResponse.BodyHandlers.ofString());

            // Mostramos en consola el numero de llamada, cómo salió y cuánto tardó
            long milisegundosQueTardo = Duration.ofNanos(System.nanoTime() - momentoInicio).toMillis();
            REGISTRO.info("Geoapify llamada #" + numeroDeLlamada
                    + " status=" + respuesta.statusCode()
                    + " duracionMs=" + milisegundosQueTardo
                    + " categorias=" + categorias);

            if (respuesta.statusCode() < 200 || respuesta.statusCode() >= 300) {
                return null;
            }

            // Convertimos el JSON y contamos cuántos lugares llegaron
            Gson convertidorJson = new Gson();
            RespuestaGeoapify datos = convertidorJson.fromJson(respuesta.body(), RespuestaGeoapify.class);
            if (datos == null || datos.features == null) return null;
            return datos.features.size();
        } catch (Exception e) {
            REGISTRO.log(Level.WARNING, "Error contando lugares (" + categorias + "): " + e.getMessage(), e);
            // Conserva la señal de interrupcion.
            if (e instanceof InterruptedException) {
                Thread.currentThread().interrupt();
            }
            return null;
        }
    }

    // ---- Moldes para leer la respuesta JSON de Geoapify ----
    // toco usar los nombres de los campos features, geometry, coordinates
    // porque deben ser identicos a los del JSON de Geoapify
    // porque gson los une por nombre.
    private static class RespuestaGeoapify {
        List<Elemento> features;
    }  // lista de resultados
    private static class Elemento {
        Geometria geometry;
    }  // cada resultado tiene una ubicación
    private static class Geometria {
        List<Double> coordinates;
    }  //longitud, latitud
}

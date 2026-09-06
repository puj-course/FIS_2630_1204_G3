package com.wisetrip.servicio;

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
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.google.gson.Gson;

@Service
public class ServicioGeoapify {

    private static final Logger LOGGER = Logger.getLogger(ServicioGeoapify.class.getName());
    private static final Duration TIMEOUT = Duration.ofSeconds(8);

    @Value("${geoapify.api-key:}")
    private String apiKey;

    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final Map<String, Integer> conteoLugaresCache = new ConcurrentHashMap<>();
    private final AtomicLong llamadasGeoapify = new AtomicLong();

    public double[] obtenerCoordenadas(String ciudad, String pais) {
        try {
            String texto = URLEncoder.encode(ciudad + ", " + pais, StandardCharsets.UTF_8);
            String url = "https://api.geoapify.com/v1/geocode/search?text=" + texto + "&apiKey=" + apiKey;
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .timeout(TIMEOUT)
                    .GET()
                    .build();
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            Gson gson = new Gson();
            RespuestaGeocoding resultado = gson.fromJson(response.body(), RespuestaGeocoding.class);
            if (resultado.features == null || resultado.features.isEmpty()) return null;
            List<Double> coords = resultado.features.get(0).geometry.coordinates;
            return new double[]{coords.get(1), coords.get(0)};
        } catch (Exception e) {
            System.err.println("Error consultando Geoapify para " + ciudad + ": " + e.getMessage());
            return null;
        }
    }

    public int contarLugares(double lat, double lon, String categorias, int radioMetros, int limite) {
        if (apiKey == null || apiKey.isBlank()) {
            LOGGER.warning("Geoapify sin API key configurada; se omite la consulta de lugares.");
            return 0;
        }

        String cacheKey = lat + "|" + lon + "|" + categorias + "|" + radioMetros + "|" + limite;
        Integer cacheado = conteoLugaresCache.get(cacheKey);
        if (cacheado != null) {
            LOGGER.info("Geoapify cache hit categorias=" + categorias + " cantidad=" + cacheado);
            return cacheado;
        }

        long numeroLlamada = llamadasGeoapify.incrementAndGet();
        long inicio = System.nanoTime();
        try {
            String url = "https://api.geoapify.com/v2/places"
                    + "?categories=" + categorias
                    + "&filter=circle:" + lon + "," + lat + "," + radioMetros
                    + "&limit=" + limite
                    + "&apiKey=" + apiKey;
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .timeout(TIMEOUT)
                    .GET()
                    .build();
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            long duracionMs = Duration.ofNanos(System.nanoTime() - inicio).toMillis();
            LOGGER.info("Geoapify llamada #" + numeroLlamada
                    + " status=" + response.statusCode()
                    + " duracionMs=" + duracionMs
                    + " categorias=" + categorias);

            if (response.statusCode() >= 400) {
                return 0;
            }

            Gson gson = new Gson();
            RespuestaGeocoding resultado = gson.fromJson(response.body(), RespuestaGeocoding.class);
            int cantidad = resultado.features == null ? 0 : resultado.features.size();
            conteoLugaresCache.put(cacheKey, cantidad);
            return cantidad;
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Error contando lugares (" + categorias + "): " + e.getMessage(), e);
            return 0;
        }
    }

    private static class RespuestaGeocoding { List<Feature> features; }
    private static class Feature { Geometry geometry; }
    private static class Geometry { List<Double> coordinates; }
}

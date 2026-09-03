package com.wisetrip.servicio;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.List;
import com.google.gson.Gson;

@Service
public class ServicioGeoapify {

    @Value("${geoapify.api-key:}")
    private String apiKey;

    private final HttpClient httpClient = HttpClient.newHttpClient();

    public double[] obtenerCoordenadas(String ciudad, String pais) {
        try {
            String texto = URLEncoder.encode(ciudad + ", " + pais, StandardCharsets.UTF_8);
            String url = "https://api.geoapify.com/v1/geocode/search?text=" + texto + "&apiKey=" + apiKey;
            HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).GET().build();
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
        try {
            String url = "https://api.geoapify.com/v2/places"
                    + "?categories=" + categorias
                    + "&filter=circle:" + lon + "," + lat + "," + radioMetros
                    + "&limit=" + limite
                    + "&apiKey=" + apiKey;
            HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).GET().build();
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            Gson gson = new Gson();
            RespuestaGeocoding resultado = gson.fromJson(response.body(), RespuestaGeocoding.class);
            return resultado.features == null ? 0 : resultado.features.size();
        } catch (Exception e) {
            System.err.println("Error contando lugares (" + categorias + "): " + e.getMessage());
            return 0;
        }
    }

    private static class RespuestaGeocoding { List<Feature> features; }
    private static class Feature { Geometry geometry; }
    private static class Geometry { List<Double> coordinates; }
}

package com.wisetrip.servicio;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class TelegramNotificationService {

    @Value("${telegram.bot.token}")
    private String botToken;

    private final HttpClient httpClient = HttpClient.newHttpClient();

    public void enviarMensaje(String chatId, String texto) {
        if (chatId == null || chatId.isBlank()) {
            System.err.println("No se envio mensaje de Telegram: el usuario no tiene chat_id.");
            return;
        }

        try {
            String textoCodificado = URLEncoder.encode(texto, StandardCharsets.UTF_8);
            String url = "https://api.telegram.org/bot" + botToken + "/sendMessage"
                    + "?chat_id=" + chatId
                    + "&text=" + textoCodificado;

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200) {
                System.err.println("Telegram respondio con error: " + response.body());
            }
        } catch (Exception e) {
            System.err.println("No se pudo enviar el mensaje de Telegram: " + e.getMessage());
        }
    }
}
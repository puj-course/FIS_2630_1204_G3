package com.wisetrip.servicio;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.wisetrip.modelo.Notificacion;
import com.wisetrip.negocio.INotificador;

/** Canal de Telegram (Bot API). Reemplaza a TelegramNotificationService. */
@Service("notificadorTelegram")
public class NotificadorTelegram implements INotificador {

    private static final Logger LOGGER = Logger.getLogger(NotificadorTelegram.class.getName());

    @Value("${telegram.bot.token}")
    private String botToken;

    private final HttpClient httpClient = HttpClient.newHttpClient();

    @Override
    public void enviar(String destino, Notificacion notificacion) {
        if (destino == null || destino.isBlank()) {
            LOGGER.warning("No se envio mensaje de Telegram: el usuario no tiene chat_id.");
            return;
        }

        try {
            String textoCodificado = URLEncoder.encode(notificacion.getCuerpo(), StandardCharsets.UTF_8);
            String url = "https://api.telegram.org/bot" + botToken + "/sendMessage"
                    + "?chat_id=" + destino
                    + "&text=" + textoCodificado;

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200) {
                // El texto se arma solo si el nivel WARNING esta activo (Supplier diferido)
                LOGGER.warning(() -> "Telegram respondio con error: " + response.body());
            }
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, e, () -> "No se pudo enviar el mensaje de Telegram: " + e.getMessage());
            // Conserva la señal de interrupcion.
            if (e instanceof InterruptedException) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
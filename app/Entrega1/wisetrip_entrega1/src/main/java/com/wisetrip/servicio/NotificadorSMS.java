package com.wisetrip.servicio;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.wisetrip.modelo.Notificacion;
import com.wisetrip.negocio.INotificador;

/** Canal de SMS (Twilio, API REST). */
@Service("notificadorSMS")
public class NotificadorSMS implements INotificador {

    private static final Logger log = LoggerFactory.getLogger(NotificadorSMS.class);
    private static final String URL_BASE = "https://api.twilio.com/2010-04-01/Accounts/";

    @Value("${twilio.account-sid:}")
    private String accountSid;

    @Value("${twilio.auth-token:}")
    private String authToken;

    @Value("${twilio.from:}")
    private String numeroOrigen;

    // Solo para la cuenta trial: si trae un valor, se manda esa plantilla de Twilio
    // en vez del texto real de la notificacion.
    @Value("${twilio.plantilla:}")
    private String plantilla;

    private final HttpClient httpClient = HttpClient.newHttpClient();

    @Override
    public void enviar(String destino, Notificacion notificacion) {
        if (destino == null || destino.isBlank()) {
            log.warn("SMS no enviado: el usuario no tiene celular registrado.");
            return;
        }
        if (accountSid.isBlank() || authToken.isBlank() || numeroOrigen.isBlank()) {
            log.warn("SMS no enviado: faltan las credenciales de Twilio en application.properties.");
            return;
        }

        try {
            HttpResponse<String> respuesta = httpClient.send(
                    armarPeticion(destino, notificacion), HttpResponse.BodyHandlers.ofString());
            registrarResultado(destino, respuesta);
        } catch (InterruptedException e) {
            log.warn("Envio de SMS interrumpido.", e);
            // Se restaura la señal de interrupcion del hilo.
            Thread.currentThread().interrupt();
        } catch (IOException | RuntimeException e) {
            log.warn("No se pudo enviar el SMS a {}: {}", ocultar(destino), e.getMessage());
        }
    }

    private HttpRequest armarPeticion(String destino, Notificacion notificacion) {
        String texto = plantilla.isBlank() ? notificacion.getCuerpo() : plantilla;
        String formulario = "To=" + codificar(destino)
                + "&From=" + codificar(numeroOrigen)
                + "&Body=" + codificar(texto);
        String credenciales = Base64.getEncoder()
                .encodeToString((accountSid + ":" + authToken).getBytes(StandardCharsets.UTF_8));

        return HttpRequest.newBuilder()
                .uri(URI.create(URL_BASE + accountSid + "/Messages.json"))
                .header("Authorization", "Basic " + credenciales)
                .header("Content-Type", "application/x-www-form-urlencoded")
                .POST(HttpRequest.BodyPublishers.ofString(formulario))
                .build();
    }

    // Deja evidencia del envio en los logs: numero oculto, sid del mensaje y estado.
    private void registrarResultado(String destino, HttpResponse<String> respuesta) {
        if (respuesta.statusCode() == 201) {
            JsonObject json = JsonParser.parseString(respuesta.body()).getAsJsonObject();
            log.info("SMS enviado a {} | sid={} | estado={}",
                    ocultar(destino), json.get("sid").getAsString(), json.get("status").getAsString());
        } else {
            log.warn("Twilio rechazo el SMS a {} | http={} | respuesta={}",
                    ocultar(destino), respuesta.statusCode(), respuesta.body());
        }
    }

    private static String codificar(String valor) {
        return URLEncoder.encode(valor, StandardCharsets.UTF_8);
    }

    // +573173338196 -> +57******8196
    private static String ocultar(String numero) {
        if (numero == null || numero.length() < 8) {
            return "***";
        }
        return numero.substring(0, 3) + "*".repeat(numero.length() - 7)
                + numero.substring(numero.length() - 4);
    }
}
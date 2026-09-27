package com.wisetrip.controlador;

import com.wisetrip.servicio.EmailNotificationService;
import com.wisetrip.servicio.TelegramNotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/notificaciones")
public class NotificacionController {

    @Autowired
    private EmailNotificationService emailService;

    @Autowired
    private TelegramNotificationService telegramService;

    @PostMapping("/email-test")
    public String enviarCorreoPrueba(
        @RequestParam String destinatario,
        @RequestParam String asunto,
        @RequestParam String cuerpo) {
    emailService.enviarNotificacion(destinatario, asunto, cuerpo);
    return "Correo enviado a " + destinatario;
    }

    @PostMapping("/telegram-test")
    public String enviarTelegramPrueba(
        @RequestParam String chatId,
        @RequestParam String mensaje) {
    telegramService.enviarMensaje(chatId, mensaje);
    return "Mensaje enviado al chat_id " + chatId;
    }
}
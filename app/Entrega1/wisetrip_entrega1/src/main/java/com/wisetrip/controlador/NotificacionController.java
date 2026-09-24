package com.wisetrip.controlador;

import com.wisetrip.servicio.EmailNotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/notificaciones")
public class NotificacionController {

    @Autowired
    private EmailNotificationService emailService;

    @PostMapping("/email-test")
    public String enviarCorreoPrueba(
        @RequestParam String destinatario,
        @RequestParam String asunto,
        @RequestParam String cuerpo) {
    emailService.enviarNotificacion(destinatario, asunto, cuerpo);
    return "Correo enviado a " + destinatario;
}
}
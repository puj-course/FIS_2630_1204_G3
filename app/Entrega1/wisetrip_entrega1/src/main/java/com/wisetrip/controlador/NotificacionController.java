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
    public String enviarCorreoPrueba(@RequestParam String destinatario) {
        emailService.enviarNotificacion(
            destinatario,
            "Prueba WiseTrip",
            "Este es un correo de prueba desde WiseTrip 🚀"
        );
        return "Correo enviado a " + destinatario;
    }
}
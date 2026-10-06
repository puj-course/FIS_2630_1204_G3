package com.wisetrip.controlador;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.wisetrip.modelo.Notificacion;
import com.wisetrip.negocio.INotificador;

//END POINTS PARA HACER PRUEBAS DE NOTIFICACIONES
@RestController
@RequestMapping("/api/notificaciones")
public class NotificacionController {

    private final INotificador notificadorCorreo;
    private final INotificador notificadorTelegram;

    public NotificacionController(@Qualifier("notificadorCorreo") INotificador notificadorCorreo,
                                  @Qualifier("notificadorTelegram") INotificador notificadorTelegram) {
        this.notificadorCorreo = notificadorCorreo;
        this.notificadorTelegram = notificadorTelegram;
    }

    @PostMapping("/email-test")
    public String enviarCorreoPrueba(
            @RequestParam String destinatario,
            @RequestParam String asunto,
            @RequestParam String cuerpo) {
        notificadorCorreo.enviar(destinatario, new Notificacion(asunto, cuerpo));
        return "Correo enviado a " + destinatario;
    }

    @PostMapping("/telegram-test")
    public String enviarTelegramPrueba(
            @RequestParam String chatId,
            @RequestParam String mensaje) {
        notificadorTelegram.enviar(chatId, new Notificacion("WiseTrip", mensaje));
        return "Mensaje enviado al chat_id " + chatId;
    }
}
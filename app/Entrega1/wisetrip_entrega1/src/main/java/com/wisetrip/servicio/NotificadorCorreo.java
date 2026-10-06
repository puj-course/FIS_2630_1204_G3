package com.wisetrip.servicio;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.wisetrip.modelo.Notificacion;
import com.wisetrip.negocio.INotificador;

/** Canal de correo (SMTP) */
@Service("notificadorCorreo")
public class NotificadorCorreo implements INotificador {

    private final JavaMailSender mailSender;

    public NotificadorCorreo(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    public void enviar(String destino, Notificacion notificacion) {
        SimpleMailMessage mensaje = new SimpleMailMessage();
        mensaje.setTo(destino);
        mensaje.setSubject(notificacion.getAsunto());
        mensaje.setText(notificacion.getCuerpo());
        mailSender.send(mensaje);
    }
}
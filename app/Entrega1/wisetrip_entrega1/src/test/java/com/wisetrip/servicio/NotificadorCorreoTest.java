package com.wisetrip.servicio;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.MailSendException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import com.wisetrip.modelo.Notificacion;

/**
 * Pruebas unitarias de NotificadorCorreo.
 *
 * El JavaMailSender se simula con Mockito: no se envian correos reales.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("NotificadorCorreo")
class NotificadorCorreoTest {

    private final Notificacion bienvenida =
            new Notificacion("Bienvenido a WiseTrip", "Hola Ana, tu cuenta fue creada.");

    @Mock
    private JavaMailSender mailSender;

    private NotificadorCorreo notificador;

    @BeforeEach
    void setUp() {
        notificador = new NotificadorCorreo(mailSender);
    }

    @Test
    @DisplayName("arma el correo con destinatario, asunto y cuerpo de la notificacion")
    void enviar_armaElCorreo() {
        notificador.enviar("ana@correo.com", bienvenida);

        ArgumentCaptor<SimpleMailMessage> captor = ArgumentCaptor.forClass(SimpleMailMessage.class);
        verify(mailSender).send(captor.capture());
        SimpleMailMessage correo = captor.getValue();
        assertArrayEquals(new String[] {"ana@correo.com"}, correo.getTo());
        assertEquals("Bienvenido a WiseTrip", correo.getSubject());
        assertEquals("Hola Ana, tu cuenta fue creada.", correo.getText());
    }

    @Test
    @DisplayName("si el SMTP falla, la excepcion sube (UsuarioServicio la captura para no romper el registro)")
    void enviar_errorSmtp_propagaLaExcepcion() {
        doThrow(new MailSendException("SMTP caido")).when(mailSender).send(any(SimpleMailMessage.class));

        assertThrows(MailSendException.class, () -> notificador.enviar("ana@correo.com", bienvenida));
    }
}

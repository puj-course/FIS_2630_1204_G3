# Diagrama de clases - Notificaciones Telegram

@startuml
title Diagrama de clases - Notificaciones Telegram

class TelegramControlador {
-usuarioServicio: UsuarioServicio
+TelegramControlador(usuarioServicio: UsuarioServicio)
+mostrarVinculacion(sesion: HttpSession, model: Model): String
+procesarVinculacion(chatId: String, sesion: HttpSession, flash: RedirectAttributes): String
}

class AuthControlador {
-usuarioServicio: UsuarioServicio
-emailService: EmailNotificationService
-telegramService: TelegramNotificationService
+procesarLogin(correo: String, password: String, sesion: HttpSession, model: Model): String
}

class NotificacionController {
-emailService: EmailNotificationService
-telegramService: TelegramNotificationService
+enviarTelegramPrueba(chatId: String, mensaje: String): String
}

class UsuarioServicio {
-usuarioDAO: UsuarioDAO
+autenticar(correo: String, password: String): Usuario
+vincularTelegram(idUsuario: int, chatId: String): void
}

class UsuarioDAO {
+buscarPorCorreo(correo: String): Usuario
+actualizarChatId(idUsuario: int, chatId: String): void
}

class Usuario {
-idUsuario: int
-nombreCompleto: String
-tipoDocumento: String
-numeroDocumento: String
-fechaNacimiento: String
-correo: String
-password: String
-rol: String
-chatId: String
+getIdUsuario(): int
+getNombreCompleto(): String
+getChatId(): String
+setChatId(chatId: String): void
}

class TelegramNotificationService {
-botToken: String
-httpClient: HttpClient
+enviarMensaje(chatId: String, texto: String): void
}

class TablaUsuario <<database>> {
+chat_id: String
}

class TelegramBotAPI <<external_system>> {
+sendMessage(chat_id: String, text: String)
}

TelegramControlador "1" --> "1" UsuarioServicio
AuthControlador "1" --> "1" UsuarioServicio
AuthControlador "1" --> "1" TelegramNotificationService
NotificacionController "1" --> "1" TelegramNotificationService
UsuarioServicio "1" --> "1" UsuarioDAO

TelegramControlador ..> Usuario
UsuarioDAO ..> TablaUsuario
UsuarioDAO ..> Usuario
TelegramNotificationService ..> TelegramBotAPI

@enduml


## Imagen Diagrama de clases - Notificaciones Telegram

![Diagrama de clases - Notificaciones Telegram](<img width="2241" height="1241" alt="DiagramaClasesTelegram (1)" src="https://github.com/user-attachments/assets/a8b0b954-a1c2-4b44-93be-14d8d0021030" />
)

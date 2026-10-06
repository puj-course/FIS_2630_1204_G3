package com.wisetrip.modelo;

/**
 * Clase de DOMINIO: un mensaje que WiseTrip le envia al usuario.
 * El TEXTO del mensaje es del negocio; el CANAL (correo, Telegram) es tecnologia.
 */
public class Notificacion {

    private final String asunto;
    private final String cuerpo;

    public Notificacion(String asunto, String cuerpo) {
        this.asunto = asunto;
        this.cuerpo = cuerpo;
    }

    /** Mensaje de bienvenida al crear la cuenta. */
    public static Notificacion bienvenida(Usuario usuario) {
        return new Notificacion(
                "¡Bienvenido a WiseTrip!",
                "Hola " + usuario.getNombreCompleto() + ",\n\n"
                + "Tu cuenta en WiseTrip fue creada con éxito. "
                + "Ya puedes iniciar sesión y empezar a planificar tus viajes.\n\n"
                + "¡Buen viaje!\n"
                + "El equipo de WiseTrip");
    }

    /** Aviso de seguridad cuando el usuario inicia sesion. */
    public static Notificacion inicioSesion(Usuario usuario) {
        return new Notificacion(
                "Inicio de sesión en WiseTrip",
                "Hola " + usuario.getNombreCompleto()
                + ", iniciaste sesión en WiseTrip correctamente. Si no fuiste tú, cambia tu contraseña.");
    }

    public String getAsunto() { return asunto; }
    public String getCuerpo() { return cuerpo; }
}
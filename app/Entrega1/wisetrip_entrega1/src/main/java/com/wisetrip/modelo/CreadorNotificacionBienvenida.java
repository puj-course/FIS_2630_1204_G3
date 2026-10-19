package com.wisetrip.modelo;

/** ConcreteCreator A: decide crear la Notificacion de bienvenida. */
public class CreadorNotificacionBienvenida extends CreadorNotificacion {

    @Override
    protected Notificacion crearNotificacion(Usuario usuario) {
        return new Notificacion(
                "¡Bienvenido a WiseTrip!",
                "Hola " + usuario.getNombreCompleto() + ",\n\n"
                + "Tu cuenta en WiseTrip fue creada con éxito. "
                + "Ya puedes iniciar sesión y empezar a planificar tus viajes.\n\n"
                + "¡Buen viaje!\nEl equipo de WiseTrip");
    }
}
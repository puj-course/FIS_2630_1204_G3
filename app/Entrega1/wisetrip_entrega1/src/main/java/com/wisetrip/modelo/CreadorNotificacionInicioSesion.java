package com.wisetrip.modelo;

/** ConcreteCreator B: decide crear la Notificacion de inicio de sesion. */
public class CreadorNotificacionInicioSesion extends CreadorNotificacion {

    @Override
    protected Notificacion crearNotificacion(Usuario usuario) {
        return new Notificacion(
                "Inicio de sesión en WiseTrip",
                "Hola " + usuario.getNombreCompleto()
                + ", iniciaste sesión en WiseTrip correctamente. Si no fuiste tú, cambia tu contraseña.");
    }
}
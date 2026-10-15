package com.wisetrip.modelo;

import com.wisetrip.negocio.INotificador;

/**
 * Creator (GoF Factory Method).
 * Define el algoritmo general para notificar a un usuario
 * (crear el mensaje y enviarlo), pero NO decide que tipo de
 * Notificacion se crea: eso lo decide cada subclase concreta
 * al sobreescribir crearNotificacion(...).
 */
public abstract class CreadorNotificacion {

    // --- Este es el Factory Method ---
    // Cada subclase concreta lo sobreescribe para devolver
    // un tipo distinto de Notificacion.
    protected abstract Notificacion crearNotificacion(Usuario usuario);

    // Operacion que USA el factory method, sin saber que
    // subclase concreta esta detras ni que texto trae.
    public void notificar(Usuario usuario, String destino, INotificador notificador) {
        Notificacion notificacion = crearNotificacion(usuario); // <- aqui se llama al factory method
        notificador.enviar(destino, notificacion);
    }
}
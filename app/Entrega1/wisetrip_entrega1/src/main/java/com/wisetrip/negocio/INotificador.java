package com.wisetrip.negocio;

import com.wisetrip.modelo.Notificacion;

/**
 * Contrato para enviar una notificacion por un canal (correo, Telegram, SMS...).
 * Agregar un canal nuevo = crear otra clase que implemente esta interfaz.
 */
public interface INotificador {

    /**
     * @param destino direccion propia del canal: un correo, un chat_id, un telefono...
     */
    void enviar(String destino, Notificacion notificacion);
}
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


    public String getAsunto() { return asunto; }
    public String getCuerpo() { return cuerpo; }
}
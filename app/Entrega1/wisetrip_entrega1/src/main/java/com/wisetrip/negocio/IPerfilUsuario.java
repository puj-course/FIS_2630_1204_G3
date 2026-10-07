package com.wisetrip.negocio;

import com.wisetrip.modelo.Usuario;

/** Contrato: gestionar los datos del perfil del usuario. */
public interface IPerfilUsuario {

    /** Vincula (o cambia) el chat de Telegram del usuario y lo guarda. */
    void vincularTelegram(Usuario usuario, String chatId);
}
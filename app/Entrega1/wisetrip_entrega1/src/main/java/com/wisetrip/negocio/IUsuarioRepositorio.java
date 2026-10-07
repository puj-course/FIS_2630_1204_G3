package com.wisetrip.negocio;

import com.wisetrip.modelo.Usuario;

/**
 * Contrato de persistencia de usuarios.
 * El negocio depende de ESTA interfaz, no de como se guarda (JDBC, JPA, memoria...).
 */
public interface IUsuarioRepositorio {

    Usuario registrar(Usuario usuario);

    boolean existeCorreo(String correo);

    boolean existeDocumento(String numeroDocumento);

    /** Devuelve null si no existe un usuario activo con ese correo. */
    Usuario buscarPorCorreo(String correo);

    void vincularTelegram(int idUsuario, String chatId);
}
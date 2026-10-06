package com.wisetrip.negocio;

import com.wisetrip.modelo.Usuario;

/** Contrato: iniciar sesion. */
public interface IAutenticacionUsuario {

    /** Devuelve el usuario si correo y contrasena son correctos; null en caso contrario. */
    Usuario iniciarSesion(String correo, String password);
}
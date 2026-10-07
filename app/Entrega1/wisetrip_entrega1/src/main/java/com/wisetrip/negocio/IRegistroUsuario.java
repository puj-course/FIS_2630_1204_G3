package com.wisetrip.negocio;

import java.util.Map;

import com.wisetrip.modelo.Usuario;

/** Contrato: crear una cuenta nueva. */
public interface IRegistroUsuario {

    /** Valida los datos del formulario. Devuelve campo -> mensaje; vacio si todo esta bien. */
    Map<String, String> validarRegistro(Usuario usuario, String confirmarPassword);

    /** Guarda al usuario (ya validado) y le envia la bienvenida. */
    void registrar(Usuario usuario);
}
package com.wisetrip.negocio;

import com.wisetrip.modelo.Usuario;

/**
 * PATRON BUILDER (GoF) - rol: Builder.
 *
 * Declara los pasos abstractos para construir las partes de un Usuario
 * y el metodo que entrega el producto terminado (getResult).
 * El Director solo conoce esta interfaz, nunca la clase concreta.
 */
public interface IUsuarioBuilder {

    /** Empieza un Usuario nuevo y descarta lo que se estuviera armando. */
    void reiniciar();

    /** Parte 1: identificacion interna y nombre. */
    void construirIdentidad(int idUsuario, String nombreCompleto);

    /** Parte 2: documento de identidad y fecha de nacimiento. */
    void construirDocumento(String tipoDocumento, String numeroDocumento, String fechaNacimiento);

    /** Parte 3: datos de acceso a la cuenta. */
    void construirCredenciales(String correo, String password, String rol);

    /** Parte 4: canal de contacto por Telegram (puede no tenerlo). */
    void construirContacto(String chatId);

    /** celular para recibir SMS . */
    void construirTelefono(String telefono);

    /** Devuelve el Usuario ya ensamblado. */
    Usuario getResult();
}

package com.wisetrip.modelo;

import com.wisetrip.negocio.IUsuarioBuilder;

/**
 * PATRON BUILDER (GoF) - rol: ConcreteBuilder.
 *
 * Implementa cada paso de IUsuarioBuilder y lleva el registro del
 * producto parcial (el atributo "usuario") mientras se ensambla.
 * Usa los setters de Usuario para respetar sus reglas
 * (por ejemplo, setCorreo deja el correo en minusculas).
 */
public class UsuarioBuilder implements IUsuarioBuilder {

    private static final String ROL_POR_DEFECTO = "cliente";

    /** Producto parcial que se va armando paso a paso. */
    private Usuario usuario;

    public UsuarioBuilder() {
        reiniciar();
    }

    @Override
    public void reiniciar() {
        usuario = new Usuario();
    }

    @Override
    public void construirIdentidad(int idUsuario, String nombreCompleto) {
        usuario.setIdUsuario(idUsuario);
        usuario.setNombreCompleto(nombreCompleto);
    }

    @Override
    public void construirDocumento(String tipoDocumento, String numeroDocumento, String fechaNacimiento) {
        usuario.setTipoDocumento(tipoDocumento);
        usuario.setNumeroDocumento(numeroDocumento);
        usuario.setFechaNacimiento(fechaNacimiento);
    }

    @Override
    public void construirCredenciales(String correo, String password, String rol) {
        usuario.setCorreo(correo);
        usuario.setPassword(password);
        usuario.setRol(rol != null && !rol.isBlank() ? rol : ROL_POR_DEFECTO);
    }

    @Override
    public void construirContacto(String chatId) {
        usuario.setChatId(chatId);
    }

    /** Entrega el Usuario terminado y deja el builder listo para otro. */
    @Override
    public Usuario getResult() {
        Usuario terminado = usuario;
        reiniciar();
        return terminado;
    }
}

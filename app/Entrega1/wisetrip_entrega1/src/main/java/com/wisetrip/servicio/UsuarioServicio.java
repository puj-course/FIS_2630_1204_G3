package com.wisetrip.servicio;


import java.util.Map;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.wisetrip.modelo.Notificacion;
import com.wisetrip.modelo.Usuario;
import com.wisetrip.negocio.IAutenticacionUsuario;
import com.wisetrip.negocio.INotificador;
import com.wisetrip.negocio.IPerfilUsuario;
import com.wisetrip.negocio.IRegistroUsuario;
import com.wisetrip.negocio.IUsuarioRepositorio;


// gestiona el modulo de usuario : no tiene reglas propias solo llama a 
// los dominios usuario y notificacion y los contratos repositorio y notificador

@Service
public class UsuarioServicio implements IRegistroUsuario, IPerfilUsuario, IAutenticacionUsuario {

    private static final String MSG_DOCUMENTO_EXISTE = "Ya existe una cuenta con este documento.";
    private static final String MSG_CORREO_EXISTE = "Ya existe una cuenta con este correo.";

     private final IUsuarioRepositorio repositorio;
    private final INotificador notificadorCorreo;
    private final INotificador notificadorTelegram;

    // contructor con inyeccion de dependencias, se le dice a spring 
    //que vamos a necesitar para funcionar y spring nos lo va a dar

    public UsuarioServicio(IUsuarioRepositorio repositorio, @Qualifier("notificadorCorreo") INotificador notificadorCorreo, @Qualifier("notificadorTelegram") INotificador notificadorTelegram) {
        this.repositorio = repositorio;
        this.notificadorCorreo = notificadorCorreo;
        this.notificadorTelegram = notificadorTelegram;
    }

    @Override
    public Map<String, String> validarRegistro(Usuario usuario, String confirmarPassword) {
        Map<String, String> errores = usuario.validarDatos(confirmarPassword);

        if (!errores.containsKey("numeroDocumento")
                && repositorio.existeDocumento(usuario.getNumeroDocumento())) {
            errores.put("numeroDocumento", MSG_DOCUMENTO_EXISTE);
        }
        if (!errores.containsKey("correo")
                && repositorio.existeCorreo(usuario.getCorreo())) {
            errores.put("correo", MSG_CORREO_EXISTE);
        }
        return errores;
    }

    @Override
    public void registrar(Usuario usuario) {
        usuario.normalizar();
        repositorio.registrar(usuario);
        // Si el correo falla, el registro no se rompe
        notificar(notificadorCorreo, usuario.getCorreo(), Notificacion.bienvenida(usuario));
    }

    @Override
    public Usuario iniciarSesion(String correo, String password) {
        if (correo == null || password == null) {
            return null;
        }
        Usuario usuario = repositorio.buscarPorCorreo(correo.trim());
        if (usuario == null || !usuario.verificarPassword(password)) {
            return null;
        }
        if (usuario.tieneTelegramVinculado()) {
            notificar(notificadorTelegram, usuario.getChatId(), Notificacion.inicioSesion(usuario));
        }
        return usuario;
    }

    @Override
    public void vincularTelegram(Usuario usuario, String chatId) {
        usuario.vincularTelegram(chatId);
        repositorio.vincularTelegram(usuario.getIdUsuario(), usuario.getChatId());
    }

    // Una notificacion fallida nunca debe tumbar el registro ni el login
    private void notificar(INotificador notificador, String destino, Notificacion notificacion) {
        try {
            notificador.enviar(destino, notificacion);
        } catch (Exception e) {
            System.err.println("No se pudo enviar la notificacion: " + e.getMessage());
        }
    }
}    
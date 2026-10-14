package com.wisetrip.servicio;

import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.wisetrip.modelo.Usuario;

import jakarta.servlet.http.HttpSession;

@Service
public class UsuarioSesionFacade {

    private static final String CLAVE_USUARIO = "usuarioActivo";

    /** Devuelve el usuario activo de la sesión, o null si no hay sesión iniciada. */
    public Usuario usuarioActivo(HttpSession sesion) {
        return (Usuario) sesion.getAttribute(CLAVE_USUARIO);
    }

    /**
     * Iniciales del usuario para el avatar del encabezado.
     */
    public String iniciales(Usuario usuario) {
        if (usuario == null || usuario.getNombreCompleto() == null
                || usuario.getNombreCompleto().isBlank()) {
            return "?";
        }

        String[] partes = usuario.getNombreCompleto().trim().split("\\s+");

        if (partes.length > 1) {
            return ("" + partes[0].charAt(0) + partes[1].charAt(0)).toUpperCase();
        }

        return partes[0].substring(0, 1).toUpperCase();
    }

    /**
     * Recupera el usuario activo y, si existe, carga en el modelo los
     * atributos comunes del encabezado ("usuario" e "inicialesUsuario").
     * Devuelve null si no hay usuario activo, para que el controlador
     * redirija a /login.
     */
    public Usuario prepararEncabezado(HttpSession sesion, Model model) {
        Usuario usuario = usuarioActivo(sesion);
        if (usuario != null) {
            model.addAttribute("usuario", usuario);
            model.addAttribute("inicialesUsuario", iniciales(usuario));
        }
        return usuario;
    }
}

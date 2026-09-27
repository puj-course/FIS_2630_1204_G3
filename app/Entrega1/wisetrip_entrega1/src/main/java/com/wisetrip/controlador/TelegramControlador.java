package com.wisetrip.controlador;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.wisetrip.modelo.Usuario;
import com.wisetrip.servicio.UsuarioServicio;

import jakarta.servlet.http.HttpSession;

@Controller
public class TelegramControlador {

    private final UsuarioServicio usuarioServicio;

    public TelegramControlador(UsuarioServicio usuarioServicio) {
        this.usuarioServicio = usuarioServicio;
    }

    // Muestra el formulario para vincular Telegram
    @GetMapping("/perfil/telegram")
    public String mostrarVinculacion(HttpSession sesion, Model model) {
        Usuario usuarioActivo = (Usuario) sesion.getAttribute("usuarioActivo");
        if (usuarioActivo == null) {
            return "redirect:/login";
        }
        model.addAttribute("chatIdActual", usuarioActivo.getChatId());
        return "vincular-telegram";
    }

    // Procesa el chat_id ingresado
    @PostMapping("/perfil/telegram")
    public String procesarVinculacion(@RequestParam String chatId,
                                      HttpSession sesion,
                                      RedirectAttributes flash) {
        Usuario usuarioActivo = (Usuario) sesion.getAttribute("usuarioActivo");
        if (usuarioActivo == null) {
            return "redirect:/login";
        }

        usuarioServicio.vincularTelegram(usuarioActivo.getIdUsuario(), chatId.trim());
        usuarioActivo.setChatId(chatId.trim());
        sesion.setAttribute("usuarioActivo", usuarioActivo);

        flash.addFlashAttribute("mensaje", "¡Telegram vinculado con éxito!");
        return "redirect:/perfil/telegram";
    }
}
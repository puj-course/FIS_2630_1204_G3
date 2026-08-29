package com.wisetrip.controlador;

import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.wisetrip.modelo.Usuario;
import com.wisetrip.servicio.UsuarioServicio;

import jakarta.servlet.http.HttpSession;

@Controller
public class AuthControlador {

    private final UsuarioServicio usuarioServicio;

    // Spring inyecta el servicio automaticamente al crear el controlador
    public AuthControlador(UsuarioServicio usuarioServicio) {
        this.usuarioServicio = usuarioServicio;
    }

    // Muestra el formulario vacio
    @GetMapping("/registro")
    public String mostrarRegistro(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "registro";
    }

    // Recibe los datos enviados por el formulario
    @PostMapping("/registro")
    public String procesarRegistro(@ModelAttribute("usuario") Usuario usuario,
                                   @RequestParam(name = "confirmarPassword", required = false) String confirmarPassword,
                                   Model model,
                                   RedirectAttributes flash) {

        Map<String, String> errores = usuarioServicio.validarRegistro(usuario, confirmarPassword);

        if (!errores.isEmpty()) {
            model.addAttribute("errores", errores);
            return "registro";   // vuelve al formulario mostrando los errores
        }

        usuario.setNombreCompleto(usuario.getNombreCompleto().trim());
        usuario.setCorreo(usuario.getCorreo().trim());
        usuario.setNumeroDocumento(usuario.getNumeroDocumento().trim());
        usuarioServicio.registrar(usuario);

        flash.addFlashAttribute("nombre", usuario.getNombreCompleto());
        flash.addFlashAttribute("correo", usuario.getCorreo());
        return "redirect:/registro-exitoso";
    }

    @GetMapping("/registro-exitoso")
    public String registroExitoso() {
        return "registro-exitoso";
    }
     // Muestra el formulario de login
    @GetMapping("/login")
    public String mostrarLogin() {
        return "login";
    }

    // Procesa el inicio de sesion
    @PostMapping("/login")
    public String procesarLogin(@RequestParam("correo") String correo,
                                @RequestParam("password") String password,
                                HttpSession sesion,
                                Model model) {

        if (correo == null || correo.isBlank() || password == null || password.isEmpty()) {
            model.addAttribute("error", "Ingresa tu correo y contrasena.");
            model.addAttribute("correo", correo);
            return "login";
        }

        Usuario usuario = usuarioServicio.autenticar(correo, password);

        if (usuario == null) {
            model.addAttribute("error", "Correo o contrasena incorrectos.");
            model.addAttribute("correo", correo);
            return "login";
        }

        // Guarda al usuario en la sesion: es el equivalente al useState del usuario logueado
        sesion.setAttribute("usuarioActivo", usuario);
        return "redirect:/origen";
    }

    // Cierra la sesion
    @GetMapping("/logout")
    public String cerrarSesion(HttpSession sesion) {
        sesion.invalidate();
        return "redirect:/";
    }
}
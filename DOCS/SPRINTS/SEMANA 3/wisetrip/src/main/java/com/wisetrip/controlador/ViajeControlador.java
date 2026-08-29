package com.wisetrip.controlador;

import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.wisetrip.modelo.Ubicacion;
import com.wisetrip.modelo.Usuario;
import com.wisetrip.servicio.ViajeServicio;

import jakarta.servlet.http.HttpSession;

@Controller
public class ViajeControlador {

    private final ViajeServicio viajeServicio;

    public ViajeControlador(ViajeServicio viajeServicio) {
        this.viajeServicio = viajeServicio;
    }

    // Muestra el formulario de ubicacion de origen
    @GetMapping("/origen")
    public String mostrarOrigen(HttpSession sesion, Model model) {

        Usuario usuario = (Usuario) sesion.getAttribute("usuarioActivo");
        if (usuario == null) {
            return "redirect:/login";   // no hay sesion activa
        }

        Ubicacion guardada = (Ubicacion) sesion.getAttribute("ubicacionOrigen");

        model.addAttribute("usuario", usuario);
        model.addAttribute("ubicacion", guardada != null ? guardada : new Ubicacion());
        model.addAttribute("paises", viajeServicio.listarPaises());
        model.addAttribute("ciudades",
                guardada != null ? viajeServicio.listarCiudades(guardada.getPais()) : java.util.List.of());
        return "origen";
    }

    // Procesa el formulario
    @PostMapping("/origen")
    public String guardarOrigen(@ModelAttribute("ubicacion") Ubicacion ubicacion,
                                HttpSession sesion,
                                Model model) {

        Usuario usuario = (Usuario) sesion.getAttribute("usuarioActivo");
        if (usuario == null) {
            return "redirect:/login";
        }

        Map<String, String> errores = viajeServicio.validarUbicacion(ubicacion);

        if (!errores.isEmpty()) {
            model.addAttribute("errores", errores);
            model.addAttribute("usuario", usuario);
            model.addAttribute("paises", viajeServicio.listarPaises());
            model.addAttribute("ciudades", viajeServicio.listarCiudades(ubicacion.getPais()));
            return "origen";
        }

        if (ubicacion.getDetalle() != null) {
            ubicacion.setDetalle(ubicacion.getDetalle().trim());
        }

        // Guarda la ubicacion en la sesion del usuario
        sesion.setAttribute("ubicacionOrigen", ubicacion);
        return "redirect:/resumen";
    }

    // Pantalla de confirmacion
    @GetMapping("/resumen")
    public String mostrarResumen(HttpSession sesion, Model model) {

        Usuario usuario = (Usuario) sesion.getAttribute("usuarioActivo");
        if (usuario == null) {
            return "redirect:/login";
        }

        Ubicacion ubicacion = (Ubicacion) sesion.getAttribute("ubicacionOrigen");
        if (ubicacion == null) {
            return "redirect:/origen";
        }

        model.addAttribute("usuario", usuario);
        model.addAttribute("ubicacion", ubicacion);
        return "resumen";
    }
}
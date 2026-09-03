package com.wisetrip.controlador;

import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.wisetrip.modelo.Preferencias;
import com.wisetrip.servicio.PreferenciasServicio;

import jakarta.servlet.http.HttpSession;

@Controller
public class PreferenciasControlador {

    private final PreferenciasServicio preferenciasServicio;

    // Spring inyecta el servicio automaticamente al crear el controlador
    public PreferenciasControlador(PreferenciasServicio preferenciasServicio) {
        this.preferenciasServicio = preferenciasServicio;
    }

    // Muestra el cuestionario vacio
    @GetMapping("/preferencias")
    public String mostrarPreferencias(Model model) {
        model.addAttribute("preferencias", new Preferencias());
        model.addAttribute("categorias", PreferenciasServicio.CATEGORIAS);
        return "preferencias";
    }

    // Recibe las respuestas del cuestionario
    @PostMapping("/preferencias")
    public String procesarPreferencias(@ModelAttribute("preferencias") Preferencias preferencias,
                                        Model model,
                                        HttpSession sesion) {

        Map<String, String> errores = preferenciasServicio.validarPreferencias(preferencias);

        if (!errores.isEmpty()) {
            model.addAttribute("errores", errores);
            model.addAttribute("categorias", PreferenciasServicio.CATEGORIAS);
            return "preferencias";   // vuelve al formulario mostrando los errores
        }

        // Guarda las respuestas en la sesion (memoria dinamica): quedan
        // disponibles para el algoritmo de recomendacion sin necesitar base de datos.
        sesion.setAttribute("preferenciasViaje", preferencias);
        sesion.setAttribute("atributosSeleccionados",
                preferenciasServicio.obtenerAtributosSeleccionados(preferencias));

        return "redirect:/preferencias-exitoso";
    }

    @GetMapping("/preferencias-exitoso")
    public String preferenciasExitoso() {
        return "preferencias-exitoso";
    }
}

package com.wisetrip.controlador;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class InicioControlador {

    @GetMapping("/")
    public String mostrarLanding(Model model) {
        model.addAttribute("mensaje", "Planea tu viaje sin perder el control de tu presupuesto");
        return "landing";
    }
}
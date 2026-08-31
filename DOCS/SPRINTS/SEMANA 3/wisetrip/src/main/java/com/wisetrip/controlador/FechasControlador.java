package com.wisetrip.controlador;

import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.wisetrip.modelo.FechasViaje;
import com.wisetrip.servicio.FechasServicio;

import jakarta.servlet.http.HttpSession;

@Controller
public class FechasControlador {

    private final FechasServicio fechasServicio;

    // Spring inyecta el servicio automaticamente al crear el controlador
    public FechasControlador(FechasServicio fechasServicio) {
        this.fechasServicio = fechasServicio;
    }

    // Muestra el formulario vacio
    @GetMapping("/fechas")
    public String mostrarFechas(Model model) {
        model.addAttribute("fechas", new FechasViaje());
        return "fechas";
    }

    // Recibe las fechas seleccionadas
    @PostMapping("/fechas")
    public String procesarFechas(@ModelAttribute("fechas") FechasViaje fechas,
                                  Model model,
                                  HttpSession sesion) {

        Map<String, String> errores = fechasServicio.validarFechas(fechas);

        if (!errores.isEmpty()) {
            model.addAttribute("errores", errores);
            return "fechas";   // vuelve al formulario mostrando los errores
        }

        // Guarda las fechas en la sesion (memoria dinamica) para su procesamiento posterior
        sesion.setAttribute("fechasViaje", fechas);

        return "redirect:/fechas-exitoso";
    }

    @GetMapping("/fechas-exitoso")
    public String fechasExitoso() {
        return "fechas-exitoso";
    }
}

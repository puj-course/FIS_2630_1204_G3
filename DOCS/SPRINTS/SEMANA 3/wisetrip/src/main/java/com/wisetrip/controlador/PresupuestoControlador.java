package com.wisetrip.controlador;

import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.wisetrip.modelo.Presupuesto;
import com.wisetrip.servicio.PresupuestoServicio;

import jakarta.servlet.http.HttpSession;

@Controller
public class PresupuestoControlador {

    private final PresupuestoServicio presupuestoServicio;

    // Spring inyecta el servicio automaticamente al crear el controlador
    public PresupuestoControlador(PresupuestoServicio presupuestoServicio) {
        this.presupuestoServicio = presupuestoServicio;
    }

    // Muestra el formulario vacio
    @GetMapping("/presupuesto")
    public String mostrarPresupuesto(Model model, HttpSession sesion) {
        // "paisDestino" es la clave asumida para el pais elegido en la pantalla de destino.
        // Si el equipo la nombro distinto, solo hay que cambiar este nombre aqui.
        String paisDestino = (String) sesion.getAttribute("paisDestino");

        model.addAttribute("presupuesto", new Presupuesto());
        model.addAttribute("paisDestino", paisDestino);
        model.addAttribute("monedas", presupuestoServicio.monedasDisponibles(paisDestino));
        return "presupuesto";
    }

    // Recibe el presupuesto y la moneda
    @PostMapping("/presupuesto")
    public String procesarPresupuesto(@ModelAttribute("presupuesto") Presupuesto presupuesto,
                                       Model model,
                                       HttpSession sesion) {

        String paisDestino = (String) sesion.getAttribute("paisDestino");

        Map<String, String> errores = presupuestoServicio.validarPresupuesto(presupuesto, paisDestino);

        if (!errores.isEmpty()) {
            model.addAttribute("errores", errores);
            model.addAttribute("paisDestino", paisDestino);
            model.addAttribute("monedas", presupuestoServicio.monedasDisponibles(paisDestino));
            return "presupuesto";   // vuelve al formulario mostrando los errores
        }

        // Guarda el presupuesto y la moneda, ya asociados entre si, en la sesion.
        // Tambien se guarda el equivalente en USD como referencia interna para el
        // algoritmo, que necesita comparar presupuestos sin importar la moneda original.
        sesion.setAttribute("presupuestoViaje", presupuesto);
        sesion.setAttribute("presupuestoEnUsd",
                presupuestoServicio.convertirAUsd(presupuesto.getMonto(), presupuesto.getMoneda()));

        return "redirect:/presupuesto-exitoso";
    }

    @GetMapping("/presupuesto-exitoso")
    public String presupuestoExitoso() {
        return "presupuesto-exitoso";
    }
}

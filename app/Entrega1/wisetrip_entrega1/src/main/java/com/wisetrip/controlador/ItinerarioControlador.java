package com.wisetrip.controlador;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.wisetrip.modelo.DiaItinerario;
import com.wisetrip.modelo.FechasViaje;
import com.wisetrip.modelo.Presupuesto;
import com.wisetrip.modelo.ResultadoRecomendacion;
import com.wisetrip.modelo.Usuario;
import com.wisetrip.negocio.GestorPresupuesto;

import jakarta.servlet.http.HttpSession;

/**
 * Itinerario durante el viaje: calendario donde el viajero organiza
 * sus actividades día por día.
 *
 * GRASP Controlador: recibe la petición web y prepara los datos de la vista.
 */
@Controller
public class ItinerarioControlador {

    private final GestorPresupuesto gestorPresupuesto;

    public ItinerarioControlador(GestorPresupuesto gestorPresupuesto) {
        this.gestorPresupuesto = gestorPresupuesto;
    }

    @GetMapping("/itinerario")
    public String mostrarItinerario(HttpSession sesion, Model model) {

        Usuario usuario = (Usuario) sesion.getAttribute("usuarioActivo");
        if (usuario == null) return "redirect:/login";

        ResultadoRecomendacion destino =
                (ResultadoRecomendacion) sesion.getAttribute("destinoElegido");
        if (destino == null) return "redirect:/recomendaciones";

        FechasViaje fechas = (FechasViaje) sesion.getAttribute("fechasViaje");
        if (fechas == null) return "redirect:/fechas";

        Presupuesto presupuesto = (Presupuesto) sesion.getAttribute("presupuestoViaje");
        if (presupuesto == null) return "redirect:/presupuesto";

        model.addAttribute("usuario", usuario);
        model.addAttribute("inicialesUsuario", usuario.iniciales());
        model.addAttribute("destino", destino);
        model.addAttribute("fechas", fechas);
        model.addAttribute("dias", fechas.listarDias().stream()
                .map(DiaItinerario::de)
                .toList());

        double totalUsd = gestorPresupuesto.convertirAUsd(presupuesto);

        model.addAttribute("presupuesto", presupuesto);
        model.addAttribute("montoFormateado",
                gestorPresupuesto.formatear(presupuesto.getMontoNumerico()));
        model.addAttribute("totalUsd", totalUsd);
        model.addAttribute("usdFormateado", gestorPresupuesto.formatear(totalUsd));

        return "itinerario";
    }
}

package com.wisetrip.controlador;

import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.wisetrip.modelo.FechasViaje;
import com.wisetrip.modelo.Presupuesto;
import com.wisetrip.modelo.RepartoPresupuesto;
import com.wisetrip.modelo.ResultadoRecomendacion;
import com.wisetrip.modelo.Usuario;
import com.wisetrip.servicio.PresupuestoServicio;
import com.wisetrip.servicio.RepartoServicio;

import jakarta.servlet.http.HttpSession;

/**
 * HU#69: permite al viajero repartir su presupuesto entre las categorías
 * de gasto del viaje.
 */
@Controller
public class PlanControlador {

    private final RepartoServicio repartoServicio;
    private final PresupuestoServicio presupuestoServicio;

    public PlanControlador(RepartoServicio repartoServicio,
                           PresupuestoServicio presupuestoServicio) {
        this.repartoServicio = repartoServicio;
        this.presupuestoServicio = presupuestoServicio;
    }

    @GetMapping("/plan")
    public String mostrarPlan(HttpSession sesion, Model model) {

        Usuario usuario = (Usuario) sesion.getAttribute("usuarioActivo");
        if (usuario == null) return "redirect:/login";

        ResultadoRecomendacion destino =
                (ResultadoRecomendacion) sesion.getAttribute("destinoElegido");
        if (destino == null) return "redirect:/recomendaciones";

        Presupuesto presupuesto = (Presupuesto) sesion.getAttribute("presupuestoViaje");
        if (presupuesto == null) return "redirect:/presupuesto";

        RepartoPresupuesto guardado = (RepartoPresupuesto) sesion.getAttribute("repartoPresupuesto");
        RepartoPresupuesto reparto = guardado != null ? guardado : new RepartoPresupuesto();

        cargarDatos(model, sesion, usuario, destino, presupuesto, reparto);
        return "plan";
    }

    @PostMapping("/plan")
    public String guardarPlan(@ModelAttribute("reparto") RepartoPresupuesto reparto,
                              HttpSession sesion,
                              Model model) {

        Usuario usuario = (Usuario) sesion.getAttribute("usuarioActivo");
        if (usuario == null) return "redirect:/login";

        ResultadoRecomendacion destino =
                (ResultadoRecomendacion) sesion.getAttribute("destinoElegido");
        if (destino == null) return "redirect:/recomendaciones";

        Presupuesto presupuesto = (Presupuesto) sesion.getAttribute("presupuestoViaje");
        if (presupuesto == null) return "redirect:/presupuesto";

        Map<String, String> errores = repartoServicio.validarReparto(reparto);

        if (!errores.isEmpty()) {
            model.addAttribute("errores", errores);
            cargarDatos(model, sesion, usuario, destino, presupuesto, reparto);
            return "plan";
        }

        sesion.setAttribute("repartoPresupuesto", reparto);
        model.addAttribute("guardado", true);
        cargarDatos(model, sesion, usuario, destino, presupuesto, reparto);
        return "plan";
    }

    /** Reúne todo lo que la vista necesita, para no repetirlo en cada método. */
    private void cargarDatos(Model model, HttpSession sesion, Usuario usuario,
                             ResultadoRecomendacion destino, Presupuesto presupuesto,
                             RepartoPresupuesto reparto) {

        FechasViaje fechas = (FechasViaje) sesion.getAttribute("fechasViaje");
        double total = presupuesto.getMontoNumerico();
        long dias = fechas != null ? fechas.getDuracionDias() : 0;

        model.addAttribute("usuario", usuario);
        model.addAttribute("inicialesUsuario", calcularIniciales(usuario));
        model.addAttribute("destino", destino);
        model.addAttribute("presupuesto", presupuesto);
        model.addAttribute("fechas", fechas);
        model.addAttribute("reparto", reparto);
        model.addAttribute("dias", dias);

        model.addAttribute("montos", repartoServicio.calcularMontos(reparto, total));
        model.addAttribute("montoTotalFormateado", presupuestoServicio.formatear(total));
        model.addAttribute("porDiaTotal", dias > 0
                ? presupuestoServicio.formatear(total / dias) : "—");
    }

    private String calcularIniciales(Usuario usuario) {
        if (usuario.getNombreCompleto() == null || usuario.getNombreCompleto().isBlank()) {
            return "?";
        }
        String[] partes = usuario.getNombreCompleto().trim().split("\\s+");
        if (partes.length > 1) {
            return ("" + partes[0].charAt(0) + partes[1].charAt(0)).toUpperCase();
        }
        return partes[0].substring(0, 1).toUpperCase();
    }
}

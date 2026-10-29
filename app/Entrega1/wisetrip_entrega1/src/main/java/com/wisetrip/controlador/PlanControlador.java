package com.wisetrip.controlador;

import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.wisetrip.modelo.DiaItinerario;
import com.wisetrip.modelo.FechasViaje;
import com.wisetrip.modelo.Presupuesto;
import com.wisetrip.modelo.RepartoPresupuesto;
import com.wisetrip.modelo.ResultadoRecomendacion;
import com.wisetrip.modelo.Ubicacion;
import com.wisetrip.modelo.Usuario;
import com.wisetrip.negocio.GestorPresupuesto;
import com.wisetrip.negocio.IGestionReparto;

import jakarta.servlet.http.HttpSession;

/**
 * HU#69 y HU#79: permite al viajero repartir su presupuesto entre las
 * categorías de gasto del viaje y ver la página principal del viaje
 * con su itinerario.
 *
 * GRASP Controlador: recibe las peticiones web y delega el trabajo.
 * SOLID Inversión de dependencias: depende de la interfaz de negocio
 * IGestionReparto, no de la clase RepartoServicio.
 */
@Controller
public class PlanControlador {

    private final IGestionReparto gestionReparto;
    private final GestorPresupuesto gestorPresupuesto;

    public PlanControlador(IGestionReparto gestionReparto,
                           GestorPresupuesto gestorPresupuesto) {
        this.gestionReparto = gestionReparto;
        this.gestorPresupuesto = gestorPresupuesto;
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

    @GetMapping("/viaje")
    public String mostrarViaje(HttpSession sesion, Model model) {

        Usuario usuario = (Usuario) sesion.getAttribute("usuarioActivo");
        if (usuario == null) return "redirect:/login";

        ResultadoRecomendacion destino =
                (ResultadoRecomendacion) sesion.getAttribute("destinoElegido");
        if (destino == null) return "redirect:/recomendaciones";

        Ubicacion ubicacion = (Ubicacion) sesion.getAttribute("ubicacionOrigen");
        if (ubicacion == null) return "redirect:/origen";

        FechasViaje fechas = (FechasViaje) sesion.getAttribute("fechasViaje");
        if (fechas == null) return "redirect:/fechas";

        Presupuesto presupuesto = (Presupuesto) sesion.getAttribute("presupuestoViaje");
        if (presupuesto == null) return "redirect:/presupuesto";

        RepartoPresupuesto reparto =
                (RepartoPresupuesto) sesion.getAttribute("repartoPresupuesto");
        if (reparto == null) return "redirect:/plan";

        model.addAttribute("usuario", usuario);
        model.addAttribute("inicialesUsuario", usuario.iniciales());
        model.addAttribute("ubicacionCiudad", ubicacion.getCiudad());
        model.addAttribute("destino", destino);
        model.addAttribute("fechas", fechas);
        model.addAttribute("presupuesto", presupuesto);
        model.addAttribute("reparto", reparto);
        model.addAttribute("dias", fechas.getDuracionDias());
        cargarItinerario(model, fechas, presupuesto);

        return "viaje";
    }

    /** El itinerario ahora es una sección de Mi viaje; se conserva el enlace viejo. */
    @GetMapping("/itinerario")
    public String mostrarItinerario() {
        return "redirect:/viaje#itinerario";
    }

    /** Datos del calendario y del presupuesto en USD que usa el itinerario. */
    private void cargarItinerario(Model model, FechasViaje fechas, Presupuesto presupuesto) {
        double totalUsd = gestorPresupuesto.convertirAUsd(presupuesto);

        model.addAttribute("diasCalendario", fechas.listarDias().stream()
                .map(DiaItinerario::de)
                .toList());
        model.addAttribute("montoFormateado",
                gestorPresupuesto.formatear(presupuesto.getMontoNumerico()));
        model.addAttribute("totalUsd", totalUsd);
        model.addAttribute("usdFormateado", gestorPresupuesto.formatear(totalUsd));
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

        Map<String, String> errores = gestionReparto.validarReparto(reparto);

        if (!errores.isEmpty()) {
            model.addAttribute("errores", errores);
            cargarDatos(model, sesion, usuario, destino, presupuesto, reparto);
            return "plan";
        }

        sesion.setAttribute("repartoPresupuesto", reparto);
        return "redirect:/viaje";
    }

    /** Reúne todo lo que la vista necesita, para no repetirlo en cada método. */
    private void cargarDatos(Model model, HttpSession sesion, Usuario usuario,
                             ResultadoRecomendacion destino, Presupuesto presupuesto,
                             RepartoPresupuesto reparto) {

        FechasViaje fechas = (FechasViaje) sesion.getAttribute("fechasViaje");
        Ubicacion ubicacion = (Ubicacion) sesion.getAttribute("ubicacionOrigen");
        double total = presupuesto.getMontoNumerico();
        long dias = fechas != null ? fechas.getDuracionDias() : 0;

        model.addAttribute("usuario", usuario);
        model.addAttribute("inicialesUsuario", usuario.iniciales());
        model.addAttribute("ubicacionCiudad", ubicacion != null ? ubicacion.getCiudad() : "Origen");
        model.addAttribute("destino", destino);
        model.addAttribute("presupuesto", presupuesto);
        model.addAttribute("fechas", fechas);
        model.addAttribute("reparto", reparto);
        model.addAttribute("dias", dias);

        model.addAttribute("montoTotalFormateado", gestorPresupuesto.formatear(total));
        model.addAttribute("porDiaTotal", dias > 0
                ? gestorPresupuesto.formatear(total / dias) : "—");
    }
}
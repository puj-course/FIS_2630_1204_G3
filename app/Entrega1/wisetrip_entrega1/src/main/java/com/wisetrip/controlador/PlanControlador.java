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
import com.wisetrip.modelo.Ubicacion;
import com.wisetrip.modelo.Usuario;
import com.wisetrip.negocio.IGestionReparto;
import com.wisetrip.negocio.IRepositorioViajes;
import com.wisetrip.servicio.PresupuestoServicio;
import com.wisetrip.servicio.UsuarioSesionFacade;

import jakarta.servlet.http.HttpSession;

/**
 * HU#69 y HU#79: permite al viajero repartir su presupuesto entre las
 * categorías de gasto del viaje y ver la página principal del viaje.
 *
 * GRASP Controlador: recibe las peticiones web y delega el trabajo.
 * SOLID Inversión de dependencias: depende de la interfaz de negocio
 * IGestionReparto, no de la clase RepartoServicio.
 */
@Controller
public class PlanControlador {

    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(PlanControlador.class);
    private final IRepositorioViajes viajeDAO;

    private final IGestionReparto gestionReparto;
    private final PresupuestoServicio presupuestoServicio;
    private final UsuarioSesionFacade usuarioSesionFacade;

    public PlanControlador(IGestionReparto gestionReparto,
                           PresupuestoServicio presupuestoServicio,
                           UsuarioSesionFacade usuarioSesionFacade,
                           IRepositorioViajes viajeDAO) {
        this.gestionReparto = gestionReparto;
        this.presupuestoServicio = presupuestoServicio;
        this.usuarioSesionFacade = usuarioSesionFacade;
        this.viajeDAO = viajeDAO;
    }

    @GetMapping("/plan")
    public String mostrarPlan(HttpSession sesion, Model model) {

        Usuario usuario = usuarioSesionFacade.prepararEncabezado(sesion, model);
        if (usuario == null) return "redirect:/login";

        ResultadoRecomendacion destino =
                (ResultadoRecomendacion) sesion.getAttribute("destinoElegido");
        if (destino == null) return "redirect:/recomendaciones";

        Presupuesto presupuesto = (Presupuesto) sesion.getAttribute("presupuestoViaje");
        if (presupuesto == null) return "redirect:/presupuesto";

        RepartoPresupuesto guardado = (RepartoPresupuesto) sesion.getAttribute("repartoPresupuesto");
        RepartoPresupuesto reparto = guardado != null ? guardado : new RepartoPresupuesto();

        cargarDatos(model, sesion, destino, presupuesto, reparto, usuario);
        return "plan";
    }

    @GetMapping("/viaje")
    public String mostrarViaje(HttpSession sesion, Model model) {

        Usuario usuario = usuarioSesionFacade.prepararEncabezado(sesion, model);
        if (usuario == null) return "redirect:/login";

        ResultadoRecomendacion destino =
                (ResultadoRecomendacion) sesion.getAttribute("destinoElegido");
        if (destino == null) return "redirect:/recomendaciones";

        Ubicacion ubicacion = SesionViaje.de(sesion, usuario).getOrigen();
        if (ubicacion == null) return "redirect:/origen";

        FechasViaje fechas = (FechasViaje) sesion.getAttribute("fechasViaje");
        if (fechas == null) return "redirect:/fechas";

        Presupuesto presupuesto = (Presupuesto) sesion.getAttribute("presupuestoViaje");
        if (presupuesto == null) return "redirect:/presupuesto";

        RepartoPresupuesto reparto =
                (RepartoPresupuesto) sesion.getAttribute("repartoPresupuesto");
        if (reparto == null) return "redirect:/plan";

        model.addAttribute("ubicacionCiudad", ubicacion.getCiudad());
        model.addAttribute("destino", destino);
        model.addAttribute("fechas", fechas);
        model.addAttribute("presupuesto", presupuesto);
        model.addAttribute("reparto", reparto);
        model.addAttribute("dias", fechas.getDuracionDias());

        return "viaje";
    }

    @PostMapping("/plan")
    public String guardarPlan(@ModelAttribute("reparto") RepartoPresupuesto reparto,
                            HttpSession sesion,
                            Model model) {

        Usuario usuario = usuarioSesionFacade.prepararEncabezado(sesion, model);
        if (usuario == null) return "redirect:/login";

        ResultadoRecomendacion destino =
                (ResultadoRecomendacion) sesion.getAttribute("destinoElegido");
        if (destino == null) return "redirect:/recomendaciones";

        Presupuesto presupuesto = (Presupuesto) sesion.getAttribute("presupuestoViaje");
        if (presupuesto == null) return "redirect:/presupuesto";

        Map<String, String> errores = gestionReparto.validarReparto(
                (com.wisetrip.negocio.IDistribuible) reparto);

        if (!errores.isEmpty()) {
            model.addAttribute("errores", errores);
            cargarDatos(model, sesion, destino, presupuesto, reparto, usuario);
            return "plan";
        }

        synchronized (sesion) {
            Integer idViaje = (Integer) sesion.getAttribute("idViajeGuardado");
            if (idViaje == null) return "redirect:/recomendaciones";
            try {
                viajeDAO.guardarReparto(idViaje, usuario.getIdUsuario(), reparto);
            } catch (IllegalArgumentException | IllegalStateException e) {
                log.error("No se pudo guardar el reparto del viaje", e);
                model.addAttribute("errores", Map.of("general",
                        "No pudimos guardar el reparto. Intenta nuevamente."));
                cargarDatos(model, sesion, destino, presupuesto, reparto, usuario);
                return "plan";
            }
            sesion.setAttribute("repartoPresupuesto", reparto);
        }
        return "redirect:/viaje";
    }

    /** Reúne todo lo que la vista necesita, para no repetirlo en cada método. */
    private void cargarDatos(Model model, HttpSession sesion,
                             ResultadoRecomendacion destino, Presupuesto presupuesto,
                             RepartoPresupuesto reparto, Usuario usuario) {

        FechasViaje fechas = (FechasViaje) sesion.getAttribute("fechasViaje");
        Ubicacion ubicacion = SesionViaje.de(sesion, usuario).getOrigen();
        double total = presupuesto.getMontoNumerico();
        long dias = fechas != null ? fechas.getDuracionDias() : 0;

        model.addAttribute("ubicacionCiudad", ubicacion != null ? ubicacion.getCiudad() : "Origen");
        model.addAttribute("destino", destino);
        model.addAttribute("presupuesto", presupuesto);
        model.addAttribute("fechas", fechas);
        model.addAttribute("reparto", reparto);
        model.addAttribute("dias", dias);

        model.addAttribute("montos", gestionReparto.calcularMontos((com.wisetrip.negocio.IDistribuible) reparto, total));
        model.addAttribute("montoTotalFormateado", presupuestoServicio.formatear(total));
        model.addAttribute("porDiaTotal", dias > 0
                ? presupuestoServicio.formatear(total / dias) : "—");
    }
}

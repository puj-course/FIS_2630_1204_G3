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
import com.wisetrip.negocio.IGestionReparto;
import com.wisetrip.negocio.IRepositorioViajes;
import com.wisetrip.servicio.PresupuestoServicio;
import com.wisetrip.servicio.UsuarioSesionFacade;

import org.springframework.web.util.WebUtils;
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
        if (usuario == null) return Nombres.REDIRECT_LOGIN;

        ResultadoRecomendacion destino =
                (ResultadoRecomendacion) sesion.getAttribute(Nombres.DESTINO_ELEGIDO);
        if (destino == null) return Nombres.REDIRECT_RECOMENDACIONES;

        Presupuesto presupuesto = (Presupuesto) sesion.getAttribute(Nombres.PRESUPUESTO_VIAJE);
        if (presupuesto == null) return Nombres.REDIRECT_PRESUPUESTO;

        RepartoPresupuesto guardado = (RepartoPresupuesto) sesion.getAttribute(Nombres.REPARTO_PRESUPUESTO);
        RepartoPresupuesto reparto = guardado != null ? guardado : new RepartoPresupuesto();

        cargarDatos(model, sesion, destino, presupuesto, reparto, usuario);
        return Nombres.PLAN;
    }

    @GetMapping("/viaje")
    public String mostrarViaje(HttpSession sesion, Model model) {

        Usuario usuario = usuarioSesionFacade.prepararEncabezado(sesion, model);
        if (usuario == null) return Nombres.REDIRECT_LOGIN;

        ResultadoRecomendacion destino =
                (ResultadoRecomendacion) sesion.getAttribute(Nombres.DESTINO_ELEGIDO);
        if (destino == null) return Nombres.REDIRECT_RECOMENDACIONES;

        Ubicacion ubicacion = SesionViaje.de(sesion, usuario).getOrigen();

        FechasViaje fechas = (FechasViaje) sesion.getAttribute(Nombres.FECHAS_VIAJE);
        if (fechas == null) return "redirect:/fechas";

        Presupuesto presupuesto = (Presupuesto) sesion.getAttribute(Nombres.PRESUPUESTO_VIAJE);
        if (presupuesto == null) return Nombres.REDIRECT_PRESUPUESTO;

        RepartoPresupuesto reparto =
                (RepartoPresupuesto) sesion.getAttribute(Nombres.REPARTO_PRESUPUESTO);
        if (reparto == null) return "redirect:/plan";

        model.addAttribute("ubicacionCiudad", ubicacion != null ? ubicacion.getCiudad() : "Origen no registrado");
        model.addAttribute("destino", destino);
        model.addAttribute(Nombres.FECHAS, fechas);
        model.addAttribute(Nombres.PRESUPUESTO, presupuesto);
        model.addAttribute(Nombres.REPARTO, reparto);
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
        double totalUsd = presupuestoServicio.convertirAUsd(presupuesto);

        model.addAttribute("diasCalendario", fechas.listarDias().stream()
                .map(DiaItinerario::de)
                .toList());
        model.addAttribute("montoFormateado",
                presupuestoServicio.formatear(presupuesto.getMontoNumerico()));
        model.addAttribute("totalUsd", totalUsd);
        model.addAttribute("usdFormateado", presupuestoServicio.formatear(totalUsd));
    }

    @PostMapping("/plan")
    public String guardarPlan(@ModelAttribute(Nombres.REPARTO) RepartoPresupuesto reparto,
                            HttpSession sesion,
                            Model model) {

        Usuario usuario = usuarioSesionFacade.prepararEncabezado(sesion, model);
        if (usuario == null) return Nombres.REDIRECT_LOGIN;

        ResultadoRecomendacion destino =
                (ResultadoRecomendacion) sesion.getAttribute(Nombres.DESTINO_ELEGIDO);
        if (destino == null) return Nombres.REDIRECT_RECOMENDACIONES;

        Presupuesto presupuesto = (Presupuesto) sesion.getAttribute(Nombres.PRESUPUESTO_VIAJE);
        if (presupuesto == null) return Nombres.REDIRECT_PRESUPUESTO;

        Map<String, String> errores = gestionReparto.validarReparto(reparto);

        if (!errores.isEmpty()) {
            model.addAttribute(Nombres.ERRORES, errores);
            cargarDatos(model, sesion, destino, presupuesto, reparto, usuario);
            return Nombres.PLAN;
        }

        synchronized (WebUtils.getSessionMutex(sesion)) {
            Integer idViaje = (Integer) sesion.getAttribute("idViajeGuardado");
            if (idViaje == null) return Nombres.REDIRECT_RECOMENDACIONES;
            try {
                viajeDAO.guardarReparto(idViaje, usuario.getIdUsuario(), reparto);
            } catch (IllegalArgumentException | IllegalStateException e) {
                log.error("No se pudo guardar el reparto del viaje", e);
                model.addAttribute(Nombres.ERRORES, Map.of("general",
                        "No pudimos guardar el reparto. Intenta nuevamente."));
                cargarDatos(model, sesion, destino, presupuesto, reparto, usuario);
                return Nombres.PLAN;
            }
            sesion.setAttribute(Nombres.REPARTO_PRESUPUESTO, reparto);
        }
        return "redirect:/viaje";
    }

    /** Reúne todo lo que la vista necesita, para no repetirlo en cada método. */
    private void cargarDatos(Model model, HttpSession sesion,
                             ResultadoRecomendacion destino, Presupuesto presupuesto,
                             RepartoPresupuesto reparto, Usuario usuario) {

        FechasViaje fechas = (FechasViaje) sesion.getAttribute(Nombres.FECHAS_VIAJE);
        Ubicacion ubicacion = SesionViaje.de(sesion, usuario).getOrigen();
        double total = presupuesto.getMontoNumerico();
        long dias = fechas != null ? fechas.getDuracionDias() : 0;

        model.addAttribute("ubicacionCiudad", ubicacion != null ? ubicacion.getCiudad() : "Origen");
        model.addAttribute("destino", destino);
        model.addAttribute(Nombres.PRESUPUESTO, presupuesto);
        model.addAttribute(Nombres.FECHAS, fechas);
        model.addAttribute(Nombres.REPARTO, reparto);
        model.addAttribute("dias", dias);

       
        model.addAttribute("montoTotalFormateado", presupuestoServicio.formatear(total));
        model.addAttribute("porDiaTotal", dias > 0
                ? presupuestoServicio.formatear(total / dias) : "—");
    }
}

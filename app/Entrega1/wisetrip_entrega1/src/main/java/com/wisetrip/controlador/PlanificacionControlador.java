/**
 * Preferencias, fechas, presupuesto y resumen del viaje.
 * Controlador que orquesta el flujo de planificación, conectando las vistas
 * con sus respectivos servicios.
 */

package com.wisetrip.controlador;

import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.wisetrip.modelo.FechasViaje;
import com.wisetrip.modelo.Preferencias;
import com.wisetrip.modelo.Presupuesto;
import com.wisetrip.modelo.Ubicacion;
import com.wisetrip.modelo.Usuario;
import com.wisetrip.negocio.GestorPreferencias;
import com.wisetrip.negocio.GestorPresupuesto;
import com.wisetrip.negocio.IFechasServicio;
import com.wisetrip.servicio.PresupuestoConMargenDecorador;
import com.wisetrip.servicio.UsuarioSesionFacade;

import jakarta.servlet.http.HttpSession;

@Controller
public class PlanificacionControlador {

    private final GestorPreferencias gestorPreferencias;
    private final GestorPresupuesto gestorPresupuesto;
    private final PresupuestoConMargenDecorador decoradorMargen;
    private final IFechasServicio fechasServicio;
    private final UsuarioSesionFacade usuarioSesionFacade;

    public PlanificacionControlador(GestorPreferencias gestorPreferencias,
                                    GestorPresupuesto gestorPresupuesto,
                                    PresupuestoConMargenDecorador decoradorMargen,
                                    IFechasServicio fechasServicio,
                                    UsuarioSesionFacade usuarioSesionFacade) {
        this.gestorPreferencias = gestorPreferencias;
        this.gestorPresupuesto = gestorPresupuesto;
        this.decoradorMargen = decoradorMargen;
        this.fechasServicio = fechasServicio;
        this.usuarioSesionFacade = usuarioSesionFacade;
    }

    // ---------- HU#25 Preferencias ----------

    @GetMapping("/preferencias")
    public String mostrarPreferencias(HttpSession sesion, Model model) {
        Usuario usuario = usuarioSesionFacade.prepararEncabezado(sesion, model);
        if (usuario == null) return Nombres.REDIRECT_LOGIN;
        if (!SesionViaje.de(sesion, usuario).tieneOrigen()) return "redirect:/origen";

        Preferencias guardadas = (Preferencias) sesion.getAttribute(Nombres.PREFERENCIAS_VIAJE);
        model.addAttribute(Nombres.PREFERENCIAS, guardadas != null ? guardadas : new Preferencias());
        cargarCuestionario(model);
        return Nombres.PREFERENCIAS;
    }

    @PostMapping("/preferencias")
    public String guardarPreferencias(@ModelAttribute(Nombres.PREFERENCIAS) Preferencias preferencias,
                                      HttpSession sesion,
                                      Model model) {
        Usuario usuario = usuarioSesionFacade.prepararEncabezado(sesion, model);
        if (usuario == null) return Nombres.REDIRECT_LOGIN;

        Map<String, String> errores = gestorPreferencias.validarPreferencias(preferencias);
        if (!errores.isEmpty()) {
            model.addAttribute(Nombres.ERRORES, errores);
            model.addAttribute("faltantes", errores.size());
            cargarCuestionario(model);
            return Nombres.PREFERENCIAS;
        }

        sesion.setAttribute(Nombres.PREFERENCIAS_VIAJE, preferencias);
        sesion.setAttribute("importanciasSeleccionadas",
                gestorPreferencias.obtenerImportancias(preferencias));
        sesion.setAttribute("atributosSeleccionados",
                gestorPreferencias.obtenerAtributosSeleccionados(preferencias));
        SesionViaje.invalidarSeleccion(sesion);
        return "redirect:/fechas";
    }

    // ---------- HU#26 Fechas ----------

    @GetMapping("/fechas")
    public String mostrarFechas(HttpSession sesion, Model model) {
        Usuario usuario = usuarioSesionFacade.prepararEncabezado(sesion, model);
        if (usuario == null) return Nombres.REDIRECT_LOGIN;
        if (sesion.getAttribute(Nombres.PREFERENCIAS_VIAJE) == null) return "redirect:/preferencias";

        FechasViaje guardadas = (FechasViaje) sesion.getAttribute(Nombres.FECHAS_VIAJE);
        model.addAttribute(Nombres.FECHAS, guardadas != null ? guardadas : new FechasViaje());
        return Nombres.FECHAS;
    }

    @PostMapping("/fechas")
    public String guardarFechas(@ModelAttribute(Nombres.FECHAS) FechasViaje fechas,
                                HttpSession sesion,
                                Model model) {
        Usuario usuario = usuarioSesionFacade.prepararEncabezado(sesion, model);
        if (usuario == null) return Nombres.REDIRECT_LOGIN;

        Integer idViaje = (Integer) sesion.getAttribute("idViajeGuardado");
        Map<String, String> errores;
        try {
            errores = fechasServicio.validarFechas(fechas, usuario.getIdUsuario(), idViaje != null ? idViaje : 0);
        } catch (IllegalStateException e) {
            org.slf4j.LoggerFactory.getLogger(PlanificacionControlador.class)
                    .error("No se pudo comprobar la disponibilidad de fechas", e);
            errores = Map.of("fechaFin", "No pudimos comprobar tus otros viajes. Intenta nuevamente.");
        }

        if (!errores.isEmpty()) {
            model.addAttribute(Nombres.ERRORES, errores);
            return Nombres.FECHAS;
        }

        sesion.setAttribute(Nombres.FECHAS_VIAJE, fechas);
        SesionViaje.invalidarSeleccion(sesion);
        return Nombres.REDIRECT_PRESUPUESTO;
    }

    // ---------- Presupuesto ----------

    @GetMapping("/presupuesto")
    public String mostrarPresupuesto(HttpSession sesion, Model model) {
        Usuario usuario = usuarioSesionFacade.prepararEncabezado(sesion, model);
        if (usuario == null) return Nombres.REDIRECT_LOGIN;
        if (sesion.getAttribute(Nombres.FECHAS_VIAJE) == null) return "redirect:/fechas";

        Presupuesto guardado = (Presupuesto) sesion.getAttribute(Nombres.PRESUPUESTO_VIAJE);
        model.addAttribute(Nombres.PRESUPUESTO, guardado != null ? guardado : new Presupuesto());
        cargarFormularioPresupuesto(model, sesion);
        return Nombres.PRESUPUESTO;
    }

    @PostMapping("/presupuesto")
    public String guardarPresupuesto(@ModelAttribute(Nombres.PRESUPUESTO) Presupuesto presupuesto,
                                     HttpSession sesion,
                                     Model model) {
        Usuario usuario = usuarioSesionFacade.prepararEncabezado(sesion, model);
        if (usuario == null) return Nombres.REDIRECT_LOGIN;

        String paisDestino = (String) sesion.getAttribute(Nombres.PAIS_DESTINO);
        Map<String, String> errores = gestorPresupuesto.validarPresupuesto(presupuesto, paisDestino);
        if (!errores.isEmpty()) {
            model.addAttribute(Nombres.ERRORES, errores);
            cargarFormularioPresupuesto(model, sesion);
            return Nombres.PRESUPUESTO;
        }

        sesion.setAttribute(Nombres.PRESUPUESTO_VIAJE, presupuesto);
        sesion.setAttribute("presupuestoEnUsd", gestorPresupuesto.convertirAUsd(presupuesto));
        SesionViaje.invalidarSeleccion(sesion);
        return "redirect:/resumen";
    }

    // ---------- Resumen final ----------

    @GetMapping("/resumen")
    public String mostrarResumen(HttpSession sesion, Model model) {
        Usuario usuario = usuarioSesionFacade.prepararEncabezado(sesion, model);
        if (usuario == null) return Nombres.REDIRECT_LOGIN;

        Ubicacion ubicacion = SesionViaje.de(sesion, usuario).getOrigen();
        if (ubicacion == null) return "redirect:/origen";

        Preferencias preferencias = (Preferencias) sesion.getAttribute(Nombres.PREFERENCIAS_VIAJE);
        Presupuesto presupuesto = (Presupuesto) sesion.getAttribute(Nombres.PRESUPUESTO_VIAJE);
        Double enUsd = (Double) sesion.getAttribute("presupuestoEnUsd");

        model.addAttribute("ubicacion", ubicacion);
        model.addAttribute(Nombres.FECHAS, sesion.getAttribute(Nombres.FECHAS_VIAJE));
        model.addAttribute(Nombres.PRESUPUESTO, presupuesto);
        model.addAttribute(Nombres.DESTINO_ELEGIDO, sesion.getAttribute(Nombres.DESTINO_ELEGIDO));

        if (preferencias != null) {
            model.addAttribute("resumenPreferencias",
                    gestorPreferencias.resumenPorCategoria(preferencias));
            model.addAttribute("afirmativas", gestorPreferencias.contarAfirmativas(preferencias));
        }
        if (presupuesto != null) {
            model.addAttribute("montoFormateado",
                    gestorPresupuesto.formatear(presupuesto.getMontoNumerico()));
            model.addAttribute("nombreMoneda", gestorPresupuesto.nombreMoneda(presupuesto.getMoneda()));
            model.addAttribute("margenImprevistos", decoradorMargen.calcularMargenImprevistos(presupuesto));
        }
        if (enUsd != null) {
            model.addAttribute("usdFormateado", gestorPresupuesto.formatear(enUsd));
        }
        return "resumen";
    }

    // ---------- Apoyo ----------

    private void cargarCuestionario(Model model) {
        model.addAttribute("categorias", gestorPreferencias.listarCategorias());
        model.addAttribute("totalPreguntas", gestorPreferencias.totalPreguntas());
    }

    private void cargarFormularioPresupuesto(Model model, HttpSession sesion) {
        String paisDestino = (String) sesion.getAttribute(Nombres.PAIS_DESTINO);
        model.addAttribute("monedas", gestorPresupuesto.monedasDisponibles(paisDestino));
        model.addAttribute(Nombres.PAIS_DESTINO, paisDestino);
        model.addAttribute(Nombres.FECHAS, sesion.getAttribute(Nombres.FECHAS_VIAJE));
    }
}
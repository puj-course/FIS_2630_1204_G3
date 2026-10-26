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
        if (usuario == null) return "redirect:/login";
        if (!SesionViaje.de(sesion, usuario).tieneOrigen()) return "redirect:/origen";

        Preferencias guardadas = (Preferencias) sesion.getAttribute("preferenciasViaje");
        model.addAttribute("preferencias", guardadas != null ? guardadas : new Preferencias());
        cargarCuestionario(model);
        return "preferencias";
    }

    @PostMapping("/preferencias")
    public String guardarPreferencias(@ModelAttribute("preferencias") Preferencias preferencias,
                                      HttpSession sesion,
                                      Model model) {
        Usuario usuario = usuarioSesionFacade.prepararEncabezado(sesion, model);
        if (usuario == null) return "redirect:/login";

        Map<String, String> errores = gestorPreferencias.validarPreferencias(preferencias);
        if (!errores.isEmpty()) {
            model.addAttribute("errores", errores);
            model.addAttribute("faltantes", errores.size());
            cargarCuestionario(model);
            return "preferencias";
        }

        sesion.setAttribute("preferenciasViaje", preferencias);
        sesion.setAttribute("importanciasSeleccionadas",
                gestorPreferencias.obtenerImportancias(preferencias));
        sesion.setAttribute("atributosSeleccionados",
                gestorPreferencias.obtenerAtributosSeleccionados(preferencias));
        descartarResultados(sesion);
        return "redirect:/fechas";
    }

    // ---------- HU#26 Fechas ----------

    @GetMapping("/fechas")
    public String mostrarFechas(HttpSession sesion, Model model) {
        Usuario usuario = usuarioSesionFacade.prepararEncabezado(sesion, model);
        if (usuario == null) return "redirect:/login";
        if (sesion.getAttribute("preferenciasViaje") == null) return "redirect:/preferencias";

        FechasViaje guardadas = (FechasViaje) sesion.getAttribute("fechasViaje");
        model.addAttribute("fechas", guardadas != null ? guardadas : new FechasViaje());
        return "fechas";
    }

    @PostMapping("/fechas")
    public String guardarFechas(@ModelAttribute("fechas") FechasViaje fechas,
                                HttpSession sesion,
                                Model model) {
        Usuario usuario = usuarioSesionFacade.prepararEncabezado(sesion, model);
        if (usuario == null) return "redirect:/login";

        Map<String, String> errores = fechasServicio.validarFechas(fechas);
        if (!errores.isEmpty()) {
            model.addAttribute("errores", errores);
            return "fechas";
        }

        sesion.setAttribute("fechasViaje", fechas);
        descartarResultados(sesion);
        return "redirect:/presupuesto";
    }

    // ---------- Presupuesto ----------

    @GetMapping("/presupuesto")
    public String mostrarPresupuesto(HttpSession sesion, Model model) {
        Usuario usuario = usuarioSesionFacade.prepararEncabezado(sesion, model);
        if (usuario == null) return "redirect:/login";
        if (sesion.getAttribute("fechasViaje") == null) return "redirect:/fechas";

        Presupuesto guardado = (Presupuesto) sesion.getAttribute("presupuestoViaje");
        model.addAttribute("presupuesto", guardado != null ? guardado : new Presupuesto());
        cargarFormularioPresupuesto(model, sesion);
        return "presupuesto";
    }

    @PostMapping("/presupuesto")
    public String guardarPresupuesto(@ModelAttribute("presupuesto") Presupuesto presupuesto,
                                     HttpSession sesion,
                                     Model model) {
        Usuario usuario = usuarioSesionFacade.prepararEncabezado(sesion, model);
        if (usuario == null) return "redirect:/login";

        String paisDestino = (String) sesion.getAttribute("paisDestino");
        Map<String, String> errores = gestorPresupuesto.validarPresupuesto(presupuesto, paisDestino);
        if (!errores.isEmpty()) {
            model.addAttribute("errores", errores);
            cargarFormularioPresupuesto(model, sesion);
            return "presupuesto";
        }

        sesion.setAttribute("presupuestoViaje", presupuesto);
        sesion.setAttribute("presupuestoEnUsd", gestorPresupuesto.convertirAUsd(presupuesto));
        descartarResultados(sesion);
        return "redirect:/resumen";
    }

    // ---------- Resumen final ----------

    @GetMapping("/resumen")
    public String mostrarResumen(HttpSession sesion, Model model) {
        Usuario usuario = usuarioSesionFacade.prepararEncabezado(sesion, model);
        if (usuario == null) return "redirect:/login";

        Ubicacion ubicacion = SesionViaje.de(sesion, usuario).getOrigen();
        if (ubicacion == null) return "redirect:/origen";

        Preferencias preferencias = (Preferencias) sesion.getAttribute("preferenciasViaje");
        Presupuesto presupuesto = (Presupuesto) sesion.getAttribute("presupuestoViaje");
        Double enUsd = (Double) sesion.getAttribute("presupuestoEnUsd");

        model.addAttribute("ubicacion", ubicacion);
        model.addAttribute("fechas", sesion.getAttribute("fechasViaje"));
        model.addAttribute("presupuesto", presupuesto);
        model.addAttribute("destinoElegido", sesion.getAttribute("destinoElegido"));

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
        String paisDestino = (String) sesion.getAttribute("paisDestino");
        model.addAttribute("monedas", gestorPresupuesto.monedasDisponibles(paisDestino));
        model.addAttribute("paisDestino", paisDestino);
        model.addAttribute("fechas", sesion.getAttribute("fechasViaje"));
    }

    /** Si cambian los datos del viaje, las recomendaciones y el destino elegido ya no valen. */
    private void descartarResultados(HttpSession sesion) {
        sesion.removeAttribute("idViajeGuardado");
        sesion.removeAttribute("destinoElegido");
    }
}
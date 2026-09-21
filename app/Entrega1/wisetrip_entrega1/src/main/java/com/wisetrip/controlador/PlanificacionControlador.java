/**
 * Preferencias, fechas y presupuesto de viaje.
 * Controlador que orquesta el flujo de planificación, conectando las vistas
 * de preferencias, fechas y presupuesto con sus respectivos servicios.
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
import com.wisetrip.servicio.FechasServicio;
import com.wisetrip.servicio.PreferenciasServicio;
import com.wisetrip.servicio.PresupuestoServicio;

import jakarta.servlet.http.HttpSession;

@Controller
public class PlanificacionControlador {

    private final PreferenciasServicio preferenciasServicio;
    private final FechasServicio fechasServicio;
    private final PresupuestoServicio presupuestoServicio;

    public PlanificacionControlador(PreferenciasServicio preferenciasServicio,
                                    FechasServicio fechasServicio,
                                    PresupuestoServicio presupuestoServicio) {
        this.preferenciasServicio = preferenciasServicio;
        this.fechasServicio = fechasServicio;
        this.presupuestoServicio = presupuestoServicio;
    }

    // ---------- HU#25 Preferencias ----------
    @GetMapping("/preferencias")
    public String mostrarPreferencias(HttpSession sesion, Model model) {

        Usuario usuario = (Usuario) sesion.getAttribute("usuarioActivo");
        if (usuario == null) return "redirect:/login";
        // HU-37: Flujo continuo y ordenado.
        // Bloquea el acceso directo a esta pantalla si el viajero no ha pasado
        // por el paso anterior (origen), forzando el mismo orden para todos.
        if (sesion.getAttribute("ubicacionOrigen") == null) return "redirect:/origen";

        // HU-39: Persistencia de datos en sesión.
        // Recupera las preferencias ya guardadas, si existen, para no perderlas
        // si el viajero retrocede a esta pantalla.

        Preferencias guardadas = (Preferencias) sesion.getAttribute("preferenciasViaje");

        model.addAttribute("usuario", usuario);
        model.addAttribute("preferencias", guardadas != null ? guardadas : new Preferencias());
        model.addAttribute("categorias", preferenciasServicio.listarCategorias());
        model.addAttribute("totalPreguntas", preferenciasServicio.totalPreguntas());
        return "preferencias";
    }

    @PostMapping("/preferencias")
    public String guardarPreferencias(@ModelAttribute("preferencias") Preferencias preferencias,
                                      HttpSession sesion,
                                      Model model) {

        Usuario usuario = (Usuario) sesion.getAttribute("usuarioActivo");
        if (usuario == null) return "redirect:/login";

        Map<String, String> errores = preferenciasServicio.validarPreferencias(preferencias);

        if (!errores.isEmpty()) {
            model.addAttribute("errores", errores);
            model.addAttribute("faltantes", errores.size());
            model.addAttribute("usuario", usuario);
            model.addAttribute("categorias", preferenciasServicio.listarCategorias());
            model.addAttribute("totalPreguntas", preferenciasServicio.totalPreguntas());
            return "preferencias";
        }
        
        // HU-39: Persistencia de datos en sesión.
        // Guarda las preferencias respondidas para que estén disponibles
        // mientras dure la sesión de planificación del viajero.
        sesion.setAttribute("preferenciasViaje", preferencias);
        sesion.setAttribute("atributosSeleccionados",
                preferenciasServicio.obtenerAtributosSeleccionados(preferencias));
        sesion.removeAttribute("idViajeGuardado");
        
        // HU-37: Botón "Siguiente".
        // Al guardar sin errores, avanza automáticamente al siguiente paso del flujo.
        return "redirect:/fechas";
    }

    // ---------- HU#26 Fechas ----------

    @GetMapping("/fechas")
    public String mostrarFechas(HttpSession sesion, Model model) {

        Usuario usuario = (Usuario) sesion.getAttribute("usuarioActivo");
        if (usuario == null) return "redirect:/login";
        // HU-37: Flujo continuo y ordenado.
        // Si el viajero no ha completado preferencias, lo redirige de vuelta
        // en lugar de dejarlo saltar directamente a fechas.
        if (sesion.getAttribute("preferenciasViaje") == null) return "redirect:/preferencias";
        
        // HU-39: Persistencia de datos en sesión.
        // Recupera las fechas ya guardadas, si existen, para no perderlas
        // si el viajero retrocede a esta pantalla.
        FechasViaje guardadas = (FechasViaje) sesion.getAttribute("fechasViaje");

        model.addAttribute("usuario", usuario);
        model.addAttribute("fechas", guardadas != null ? guardadas : new FechasViaje());
        model.addAttribute("hoy", fechasServicio.hoy());
        return "fechas";
    }

    @PostMapping("/fechas")
    public String guardarFechas(@ModelAttribute("fechas") FechasViaje fechas,
                                HttpSession sesion,
                                Model model) {

        Usuario usuario = (Usuario) sesion.getAttribute("usuarioActivo");
        if (usuario == null) return "redirect:/login";

        Map<String, String> errores = fechasServicio.validarFechas(fechas);

        if (!errores.isEmpty()) {
            model.addAttribute("errores", errores);
            model.addAttribute("usuario", usuario);
            model.addAttribute("hoy", fechasServicio.hoy());
            return "fechas";
        }
        
        // HU-39: Persistencia de datos en sesión.
        // Guarda las fechas para que estén disponibles mientras dure
        // la sesión de planificación del viajero.
        sesion.setAttribute("fechasViaje", fechas);
        sesion.removeAttribute("idViajeGuardado");

        // HU-37: Botón "Siguiente".
        // Avanza al paso de presupuesto una vez las fechas son válidas.
        return "redirect:/presupuesto";
    }

    // -PRESUPUESTO

    @GetMapping("/presupuesto")
    public String mostrarPresupuesto(HttpSession sesion, Model model) {

        Usuario usuario = (Usuario) sesion.getAttribute("usuarioActivo");
        if (usuario == null) return "redirect:/login";
        // HU-37: Flujo continuo y ordenado.
        // Exige que ya existan fechas guardadas en sesión antes de mostrar
        // esta pantalla, manteniendo el orden del flujo.
        if (sesion.getAttribute("fechasViaje") == null) return "redirect:/fechas";

        String paisDestino = (String) sesion.getAttribute("paisDestino");

        // HU-39: Persistencia de datos en sesión.
        // Recupera el presupuesto ya guardado, si existe, para no perderlo
        // si el viajero retrocede a esta pantalla.
        Presupuesto guardado = (Presupuesto) sesion.getAttribute("presupuestoViaje");

        model.addAttribute("usuario", usuario);
        model.addAttribute("presupuesto", guardado != null ? guardado : new Presupuesto());
        model.addAttribute("monedas", presupuestoServicio.monedasDisponibles(paisDestino));
        model.addAttribute("paisDestino", paisDestino);
        model.addAttribute("fechas", sesion.getAttribute("fechasViaje"));
        return "presupuesto";
    }

    @PostMapping("/presupuesto")
    public String guardarPresupuesto(@ModelAttribute("presupuesto") Presupuesto presupuesto,
                                     HttpSession sesion,
                                     Model model) {

        Usuario usuario = (Usuario) sesion.getAttribute("usuarioActivo");
        if (usuario == null) return "redirect:/login";

        String paisDestino = (String) sesion.getAttribute("paisDestino");
        Map<String, String> errores = presupuestoServicio.validarPresupuesto(presupuesto, paisDestino);

        if (!errores.isEmpty()) {
            model.addAttribute("errores", errores);
            model.addAttribute("usuario", usuario);
            model.addAttribute("monedas", presupuestoServicio.monedasDisponibles(paisDestino));
            model.addAttribute("paisDestino", paisDestino);
            model.addAttribute("fechas", sesion.getAttribute("fechasViaje"));
            return "presupuesto";
        }

        double enUsd = presupuestoServicio.convertirAUsd(presupuesto);

        // HU-39: Persistencia de datos en sesión.
        // Guarda el presupuesto y su equivalente en USD mientras dure
        // la sesión de planificación del viajero.
        sesion.setAttribute("presupuestoViaje", presupuesto);
        sesion.setAttribute("presupuestoEnUsd", enUsd);
        sesion.removeAttribute("idViajeGuardado");

        // HU-37: Botón "Siguiente".
        // Al completar el último paso, avanza al resumen final del viaje.
        return "redirect:/resumen";
    }

    // ---------- Resumen final ----------

    @GetMapping("/resumen")
    public String mostrarResumen(HttpSession sesion, Model model) {

        Usuario usuario = (Usuario) sesion.getAttribute("usuarioActivo");
        if (usuario == null) return "redirect:/login";

        Ubicacion ubicacion = (Ubicacion) sesion.getAttribute("ubicacionOrigen");
        if (ubicacion == null) return "redirect:/origen";

        Preferencias preferencias = (Preferencias) sesion.getAttribute("preferenciasViaje");
        FechasViaje fechas = (FechasViaje) sesion.getAttribute("fechasViaje");
        Presupuesto presupuesto = (Presupuesto) sesion.getAttribute("presupuestoViaje");
        Double enUsd = (Double) sesion.getAttribute("presupuestoEnUsd");

        model.addAttribute("usuario", usuario);
        model.addAttribute("ubicacion", ubicacion);
        model.addAttribute("preferencias", preferencias);
        model.addAttribute("fechas", fechas);
        model.addAttribute("presupuesto", presupuesto);

             if (preferencias != null) {
            model.addAttribute("categorias", preferenciasServicio.listarCategorias());
        
        }
        if (presupuesto != null) {
            model.addAttribute("montoFormateado",
                    presupuestoServicio.formatear(presupuesto.getMontoNumerico()));
            model.addAttribute("nombreMoneda",
                    presupuestoServicio.nombreMoneda(presupuesto.getMoneda()));
        }
        if (enUsd != null) {
            model.addAttribute("usdFormateado", presupuestoServicio.formatear(enUsd));
        }

        return "resumen";
    }
}

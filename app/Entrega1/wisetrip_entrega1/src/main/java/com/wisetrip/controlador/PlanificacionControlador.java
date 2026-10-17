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
import com.wisetrip.negocio.GestorPreferencias;
import com.wisetrip.negocio.GestorPresupuesto;
import com.wisetrip.servicio.FechasServicio;
import com.wisetrip.servicio.PresupuestoConMargenDecorador;

import jakarta.servlet.http.HttpSession;

@Controller
public class PlanificacionControlador {

    private final GestorPreferencias gestorPreferencias;
    private final GestorPresupuesto gestorPresupuesto;
    private final PresupuestoConMargenDecorador decoradorMargen;
    private final FechasServicio fechasServicio;

    public PlanificacionControlador(
        GestorPreferencias gestorPreferencias,
        GestorPresupuesto gestorPresupuesto,
        PresupuestoConMargenDecorador decoradorMargen,
        FechasServicio fechasServicio) {

    this.gestorPreferencias = gestorPreferencias;
    this.gestorPresupuesto = gestorPresupuesto;
    this.decoradorMargen = decoradorMargen;
    this.fechasServicio = fechasServicio;
}

    // ---------- HU#25 Preferencias ----------
    @GetMapping("/preferencias")
    public String mostrarPreferencias(HttpSession sesion, Model model) {
        Usuario usuario = (Usuario) sesion.getAttribute("usuarioActivo");
        if (usuario == null) return "redirect:/login";
        if (sesion.getAttribute("ubicacionOrigen") == null) return "redirect:/origen";

        Preferencias guardadas = (Preferencias) sesion.getAttribute("preferenciasViaje");

        model.addAttribute("usuario", usuario);
        model.addAttribute("inicialesUsuario", calcularIniciales(usuario));
        model.addAttribute("preferencias", guardadas != null ? guardadas : new Preferencias());
        model.addAttribute("categorias", gestorPreferencias.listarCategorias());
        model.addAttribute("totalPreguntas", gestorPreferencias.totalPreguntas());
        return "preferencias";
    }

    @PostMapping("/preferencias")
    public String guardarPreferencias(@ModelAttribute("preferencias") Preferencias preferencias,
                                      HttpSession sesion,
                                      Model model) {
        Usuario usuario = (Usuario) sesion.getAttribute("usuarioActivo");
        if (usuario == null) return "redirect:/login";

        Map<String, String> errores = gestorPreferencias.validarPreferencias(preferencias);

        if (!errores.isEmpty()) {
            model.addAttribute("errores", errores);
            model.addAttribute("faltantes", errores.size());
            model.addAttribute("usuario", usuario);
            model.addAttribute("inicialesUsuario", calcularIniciales(usuario));
            model.addAttribute("categorias", gestorPreferencias.listarCategorias());
            model.addAttribute("totalPreguntas", gestorPreferencias.totalPreguntas());
            return "preferencias";
        }

        sesion.setAttribute("preferenciasViaje", preferencias);
        sesion.setAttribute("importanciasSeleccionadas",
                gestorPreferencias.obtenerImportancias(preferencias));
        sesion.setAttribute("atributosSeleccionados",
                gestorPreferencias.obtenerAtributosSeleccionados(preferencias));
        sesion.removeAttribute("idViajeGuardado");
        sesion.removeAttribute("seleccionRecomendada");
        sesion.removeAttribute("destinoElegido");

        return "redirect:/fechas";
    }

    // ---------- HU#26 Fechas ----------
    @GetMapping("/fechas")
    public String mostrarFechas(HttpSession sesion, Model model) {
        Usuario usuario = (Usuario) sesion.getAttribute("usuarioActivo");
        if (usuario == null) return "redirect:/login";
        if (sesion.getAttribute("preferenciasViaje") == null) return "redirect:/preferencias";

        FechasViaje guardadas = (FechasViaje) sesion.getAttribute("fechasViaje");

        model.addAttribute("usuario", usuario);
        model.addAttribute("inicialesUsuario", calcularIniciales(usuario));
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
            model.addAttribute("inicialesUsuario", calcularIniciales(usuario));
            model.addAttribute("hoy", fechasServicio.hoy());
            return "fechas";
        }

        sesion.setAttribute("fechasViaje", fechas);
        sesion.removeAttribute("idViajeGuardado");
        sesion.removeAttribute("seleccionRecomendada");
        sesion.removeAttribute("destinoElegido");

        return "redirect:/presupuesto";
    }

    // ---------- Presupuesto ----------
    @GetMapping("/presupuesto")
    public String mostrarPresupuesto(HttpSession sesion, Model model) {
        Usuario usuario = (Usuario) sesion.getAttribute("usuarioActivo");
        if (usuario == null) return "redirect:/login";
        if (sesion.getAttribute("fechasViaje") == null) return "redirect:/fechas";

        String paisDestino = (String) sesion.getAttribute("paisDestino");
        Presupuesto guardado = (Presupuesto) sesion.getAttribute("presupuestoViaje");

        model.addAttribute("usuario", usuario);
        model.addAttribute("inicialesUsuario", calcularIniciales(usuario));
        model.addAttribute("presupuesto", guardado != null ? guardado : new Presupuesto());
        model.addAttribute("monedas", gestorPresupuesto.monedasDisponibles(paisDestino));
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
        Map<String, String> errores = gestorPresupuesto.validarPresupuesto(presupuesto, paisDestino);

        if (!errores.isEmpty()) {
            model.addAttribute("errores", errores);
            model.addAttribute("usuario", usuario);
            model.addAttribute("inicialesUsuario", calcularIniciales(usuario));
            model.addAttribute("monedas", gestorPresupuesto.monedasDisponibles(paisDestino));
            model.addAttribute("paisDestino", paisDestino);
            model.addAttribute("fechas", sesion.getAttribute("fechasViaje"));
            return "presupuesto";
        }

        double enUsd = gestorPresupuesto.convertirAUsd(presupuesto);

        sesion.setAttribute("presupuestoViaje", presupuesto);
        sesion.setAttribute("presupuestoEnUsd", enUsd);
        sesion.removeAttribute("idViajeGuardado");
        sesion.removeAttribute("seleccionRecomendada");
        sesion.removeAttribute("destinoElegido");

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
        model.addAttribute("inicialesUsuario", calcularIniciales(usuario));
        model.addAttribute("ubicacion", ubicacion);
        model.addAttribute("preferencias", preferencias);
        model.addAttribute("fechas", fechas);
        model.addAttribute("presupuesto", presupuesto);

     if (preferencias != null) {
    model.addAttribute("categorias", gestorPreferencias.listarCategorias());
    model.addAttribute("resumenPreferencias",
            gestorPreferencias.resumenPorCategoria(preferencias));
    model.addAttribute("totalPreguntas", gestorPreferencias.totalPreguntas());
}
if (presupuesto != null) {
    model.addAttribute("montoFormateado",
            gestorPresupuesto.formatear(presupuesto.getMontoNumerico()));
    model.addAttribute("nombreMoneda",
            gestorPresupuesto.nombreMoneda(presupuesto.getMoneda()));
}
if (enUsd != null) {
    model.addAttribute("usdFormateado", gestorPresupuesto.formatear(enUsd));
}

if (presupuesto != null) {
    double margen = decoradorMargen.calcularMargenImprevistos(presupuesto);
    model.addAttribute("margenImprevistos", margen);
}

model.addAttribute("destinoElegido", sesion.getAttribute("destinoElegido"));
return "resumen";

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

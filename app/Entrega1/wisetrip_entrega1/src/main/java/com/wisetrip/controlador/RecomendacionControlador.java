package com.wisetrip.controlador;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.wisetrip.datos.CiudadSemilla;
import com.wisetrip.datos.DatosCiudades;
import com.wisetrip.modelo.Ciudad;
import com.wisetrip.modelo.FechasViaje;
import com.wisetrip.modelo.Importancia;
import com.wisetrip.modelo.PreferenciasUsuario;
import com.wisetrip.modelo.ResultadoRecomendacion;
import com.wisetrip.modelo.SeleccionDestinos;
import com.wisetrip.modelo.Usuario;
import com.wisetrip.negocio.CiudadNegocio;
import com.wisetrip.negocio.IRepositorioPreferencias;
import com.wisetrip.negocio.IRepositorioViajes;
import com.wisetrip.servicio.LlenarAtributosCiudad;
import com.wisetrip.negocio.Recomendador;
import com.wisetrip.negocio.SeleRecomendaciones;

import jakarta.servlet.http.HttpSession;

/**
 * Calcula y muestra los tres destinos recomendados.
 *
 * GRASP Controlador: recibe la petición web y delega el cálculo.
 * SOLID Inversión de dependencias: el acceso a datos se recibe por sus
 * interfaces de negocio (CiudadNegocio, IRepositorioViajes,
 * IRepositorioPreferencias), no por las clases DAO.
 */
@Controller
public class RecomendacionControlador {

    private final CiudadNegocio ciudadDAO;
    private final IRepositorioViajes viajeDAO;
    private final IRepositorioPreferencias preferenciaDAO;
    private final LlenarAtributosCiudad llenarAtributosCiudad;
    private final Recomendador recomendadorDestinos;
    private final SeleRecomendaciones selectorDestinos;

    public RecomendacionControlador(CiudadNegocio ciudadDAO,
                                    IRepositorioViajes viajeDAO,
                                    IRepositorioPreferencias preferenciaDAO,
                                    LlenarAtributosCiudad llenarAtributosCiudad,
                                    Recomendador recomendadorDestinos,
                                    SeleRecomendaciones selectorDestinos) {
        this.ciudadDAO = ciudadDAO;
        this.viajeDAO = viajeDAO;
        this.preferenciaDAO = preferenciaDAO;
        this.llenarAtributosCiudad = llenarAtributosCiudad;
        this.recomendadorDestinos = recomendadorDestinos;
        this.selectorDestinos = selectorDestinos;
    }

    @SuppressWarnings("unchecked")
    @GetMapping("/recomendaciones")
    public String mostrarRecomendaciones(HttpSession sesion, Model model) {

        Usuario usuario = (Usuario) sesion.getAttribute("usuarioActivo");
        if (usuario == null) return "redirect:/login";

        Map<String, Boolean> atributosCuestionario =
                (Map<String, Boolean>) sesion.getAttribute("atributosSeleccionados");
        if (atributosCuestionario == null) return "redirect:/preferencias";
        Map<String, Importancia> importancias =
                (Map<String, Importancia>) sesion.getAttribute("importanciasSeleccionadas");
        if (importancias == null) return "redirect:/preferencias";

        Double presupuestoUsd = (Double) sesion.getAttribute("presupuestoEnUsd");
        if (presupuestoUsd == null) return "redirect:/presupuesto";

        FechasViaje fechas = (FechasViaje) sesion.getAttribute("fechasViaje");
        if (fechas == null || fechas.getDuracionDias() < 1) return "redirect:/fechas";
        PreferenciasUsuario preferencias = new PreferenciasUsuario(
                presupuestoUsd,
                importancias,
                fechas.getDuracionDias());
        List<Ciudad> ciudades = ciudadDAO.obtenerTodas();

        for (Ciudad ciudad : ciudades) {
            CiudadSemilla semilla = DatosCiudades.porNombreYPais(ciudad.getNombre(), ciudad.getPais());
            if (semilla != null) {
                llenarAtributosCiudad.enriquecer(ciudad, semilla, atributosCuestionario);
            }
        }

        var validacion = recomendadorDestinos.validarCiudades(ciudades, preferencias);
        model.addAttribute("ciudadesPendientes", validacion.pendientes());
        List<ResultadoRecomendacion> resultados =
                recomendadorDestinos.puntuarCiudadesValidas(validacion, preferencias);
        SeleccionDestinos seleccion = selectorDestinos.seleccionarMejoresDestinos(resultados, true);
        sesion.setAttribute("seleccionDestinos", seleccion);
        guardarPlanificacionSiHaceFalta(sesion, usuario, atributosCuestionario, presupuestoUsd, seleccion);

        // Iniciales para el avatar del encabezado
        String[] partes = usuario.getNombreCompleto().trim().split("\\s+");
        model.addAttribute("inicialesUsuario", partes.length > 1
                ? ("" + partes[0].charAt(0) + partes[1].charAt(0)).toUpperCase()
                : partes[0].substring(0, 1).toUpperCase());

        model.addAttribute("usuario", usuario);
        model.addAttribute("seleccion", seleccion);
        sesion.setAttribute("seleccionRecomendada", seleccion);
        model.addAttribute("fechas", sesion.getAttribute("fechasViaje"));
        model.addAttribute("presupuestoUsd", presupuestoUsd);

        // Datos de la ficha de búsqueda
        model.addAttribute("ubicacion", sesion.getAttribute("ubicacionOrigen"));
        model.addAttribute("presupuesto", sesion.getAttribute("presupuestoViaje"));

        return "recomendaciones";
    }

    private void guardarPlanificacionSiHaceFalta(HttpSession sesion,
                                                 Usuario usuario,
                                                 Map<String, Boolean> atributosCuestionario,
                                                 Double presupuestoUsd,
                                                 SeleccionDestinos seleccion) {
        if (sesion.getAttribute("idViajeGuardado") != null || seleccion.isVacio()) {
            return;
        }

        FechasViaje fechas = (FechasViaje) sesion.getAttribute("fechasViaje");
        if (fechas == null || usuario.getIdUsuario() <= 0) {
            return;
        }

        try {
            Ciudad destinoPrincipal = seleccion.getDestinos().get(0).getCiudad();
            int idViaje = viajeDAO.insertar(usuario.getIdUsuario(), destinoPrincipal.getId(), fechas, presupuestoUsd);
            preferenciaDAO.guardarActivas(idViaje, atributosCuestionario);
            sesion.setAttribute("idViajeGuardado", idViaje);
        } catch (RuntimeException e) {
            System.err.println("No se pudo persistir la planificación: " + e.getMessage());
        }
    }
}
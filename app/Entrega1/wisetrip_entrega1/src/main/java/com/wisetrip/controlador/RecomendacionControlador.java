package com.wisetrip.controlador;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.wisetrip.datos.CiudadDAO;
import com.wisetrip.datos.CiudadSemilla;
import com.wisetrip.datos.DatosCiudades;
import com.wisetrip.datos.PreferenciaDAO;
import com.wisetrip.datos.ViajeDAO;
import com.wisetrip.modelo.Ciudad;
import com.wisetrip.modelo.FechasViaje;
import com.wisetrip.modelo.PreferenciasUsuario;
import com.wisetrip.modelo.ResultadoRecomendacion;
import com.wisetrip.modelo.SeleccionDestinos;
import com.wisetrip.modelo.Usuario;
import com.wisetrip.servicio.LlenarAtributosCiudad;
import com.wisetrip.servicio.RecomendadorDestinos;
import com.wisetrip.servicio.SelectorDestinos;

import jakarta.servlet.http.HttpSession;

@Controller
public class RecomendacionControlador {

    private final CiudadDAO ciudadDAO;
    private final ViajeDAO viajeDAO;
    private final PreferenciaDAO preferenciaDAO;
    private final LlenarAtributosCiudad llenarAtributosCiudad;
    private final RecomendadorDestinos recomendadorDestinos;
    private final SelectorDestinos selectorDestinos;

    public RecomendacionControlador(CiudadDAO ciudadDAO,
                                    ViajeDAO viajeDAO,
                                    PreferenciaDAO preferenciaDAO,
                                    LlenarAtributosCiudad llenarAtributosCiudad,
                                    RecomendadorDestinos recomendadorDestinos,
                                    SelectorDestinos selectorDestinos) {
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

        Double presupuestoUsd = (Double) sesion.getAttribute("presupuestoEnUsd");
        if (presupuestoUsd == null) return "redirect:/presupuesto";

        PreferenciasUsuario preferencias = new PreferenciasUsuario(presupuestoUsd, atributosCuestionario);
        List<Ciudad> ciudades = ciudadDAO.obtenerTodas();

        for (Ciudad ciudad : ciudades) {
            CiudadSemilla semilla = DatosCiudades.porNombreYPais(ciudad.getNombre(), ciudad.getPais());
            if (semilla != null) {
                llenarAtributosCiudad.enriquecer(ciudad, semilla, atributosCuestionario);
            }
        }

        List<ResultadoRecomendacion> resultados =
                recomendadorDestinos.recomendarDestinos(ciudades, preferencias);
        SeleccionDestinos seleccion = selectorDestinos.seleccionarMejoresDestinos(resultados);
        guardarPlanificacionSiHaceFalta(sesion, usuario, atributosCuestionario, presupuestoUsd, seleccion);

        model.addAttribute("usuario", usuario);
        model.addAttribute("seleccion", seleccion);
        model.addAttribute("fechas", sesion.getAttribute("fechasViaje"));
        model.addAttribute("presupuestoUsd", presupuestoUsd);
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

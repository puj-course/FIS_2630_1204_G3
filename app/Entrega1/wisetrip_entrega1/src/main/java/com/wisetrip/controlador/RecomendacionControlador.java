package com.wisetrip.controlador;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.wisetrip.modelo.Ciudad;
import com.wisetrip.modelo.PreferenciasUsuario;
import com.wisetrip.modelo.ResultadoRecomendacion;
import com.wisetrip.modelo.SeleccionDestinos;
import com.wisetrip.modelo.Usuario;
import com.wisetrip.servicio.CatalogoCiudades;
import com.wisetrip.servicio.RecomendadorDestinos;
import com.wisetrip.servicio.SelectorDestinos;

import jakarta.servlet.http.HttpSession;

@Controller
public class RecomendacionControlador {

    private final CatalogoCiudades catalogoCiudades;
    private final RecomendadorDestinos recomendadorDestinos;
    private final SelectorDestinos selectorDestinos;

    public RecomendacionControlador(CatalogoCiudades catalogoCiudades,
                                    RecomendadorDestinos recomendadorDestinos,
                                    SelectorDestinos selectorDestinos) {
        this.catalogoCiudades = catalogoCiudades;
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

        // Traduce las claves del cuestionario a los nombres de atributo de la base
        Map<String, Boolean> atributos = catalogoCiudades.traducir(atributosCuestionario);

        PreferenciasUsuario preferencias = new PreferenciasUsuario(presupuestoUsd, atributos);
        List<Ciudad> ciudades = catalogoCiudades.listarCiudades();

        List<ResultadoRecomendacion> resultados =
                recomendadorDestinos.recomendarDestinos(ciudades, preferencias);
        SeleccionDestinos seleccion = selectorDestinos.seleccionarMejoresDestinos(resultados);

        model.addAttribute("usuario", usuario);
        model.addAttribute("seleccion", seleccion);
        model.addAttribute("fechas", sesion.getAttribute("fechasViaje"));
        model.addAttribute("presupuestoUsd", presupuestoUsd);
        return "recomendaciones";
    }
}
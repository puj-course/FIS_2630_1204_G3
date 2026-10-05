package com.wisetrip.controlador;

import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.wisetrip.modelo.ResultadoRecomendacion;
import com.wisetrip.modelo.SeleccionDestinos;
import com.wisetrip.modelo.Usuario;

import jakarta.servlet.http.HttpSession;

/**
 * HU#68: permite al viajero elegir uno de los tres destinos recomendados
 * y continuar la planificación con ese destino.
 */
@Controller
public class SeleccionControlador {

    /**
     * Guarda en sesión el destino que el viajero eligió entre los tres
     * recomendados y avanza al reparto del presupuesto.
     */
    @GetMapping("/destino/{id}")
    public String elegirDestino(@PathVariable("id") int id, HttpSession sesion) {

        Usuario usuario = (Usuario) sesion.getAttribute("usuarioActivo");
        if (usuario == null) return "redirect:/login";

        Map<String, Boolean> atributosCuestionario =
                (Map<String, Boolean>) sesion.getAttribute("atributosSeleccionados");
        if (atributosCuestionario == null) return "redirect:/preferencias";

        Double presupuestoUsd = (Double) sesion.getAttribute("presupuestoEnUsd");
        if (presupuestoUsd == null) return "redirect:/presupuesto";

        SeleccionDestinos seleccion = (SeleccionDestinos) sesion.getAttribute("seleccionRecomendada");
        if (seleccion == null) return "redirect:/recomendaciones";
        SeleccionDestinos seleccion =
            (SeleccionDestinos) sesion.getAttribute("seleccionDestinos");
        if (seleccion == null || seleccion.isVacio()) {
            return "redirect:/recomendaciones";
        }

        ResultadoRecomendacion elegido = seleccion.getDestinos().stream()
                .filter(r -> r.getCiudad().getId() == id)
                .findFirst()
                .orElse(null);

        // Si el id no corresponde a ninguno de los tres, regresa sin guardar.
        if (elegido == null) {
            return "redirect:/recomendaciones";
        }

        sesion.setAttribute("destinoElegido", elegido);
        sesion.setAttribute("paisDestino", elegido.getCiudad().getPais());

        return "redirect:/plan";
    }
}
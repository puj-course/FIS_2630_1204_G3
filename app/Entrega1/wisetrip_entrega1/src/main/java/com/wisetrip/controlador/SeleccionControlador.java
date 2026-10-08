package com.wisetrip.controlador;

import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.wisetrip.modelo.ResultadoRecomendacion;
import com.wisetrip.modelo.SeleccionDestinos;
import com.wisetrip.modelo.Usuario;
import com.wisetrip.negocio.ISeleccionDestino;

import jakarta.servlet.http.HttpSession;

/**
 * HU#68: permite al viajero elegir uno de los tres destinos recomendados
 * y continuar la planificación con ese destino.
 *
 * GRASP Controlador: recibe la petición web y delega la elección.
 * SOLID Inversión de dependencias: depende de la interfaz de negocio
 * ISeleccionDestino, no de una clase concreta.
 */
@Controller
public class SeleccionControlador {

    private final ISeleccionDestino seleccionDestino;

    public SeleccionControlador(ISeleccionDestino seleccionDestino) {
        this.seleccionDestino = seleccionDestino;
    }

    /**
     * Guarda en sesión el destino que el viajero eligió entre los tres
     * recomendados y avanza al reparto del presupuesto.
     */
    @GetMapping("/destino/{id}")
    @SuppressWarnings("unchecked")
    public String elegirDestino(@PathVariable("id") int id, HttpSession sesion) {

        Usuario usuario = (Usuario) sesion.getAttribute("usuarioActivo");
        if (usuario == null) return "redirect:/login";

        Map<String, Boolean> atributosCuestionario =
                (Map<String, Boolean>) sesion.getAttribute("atributosSeleccionados");
        if (atributosCuestionario == null) return "redirect:/preferencias";

        if (sesion.getAttribute("presupuestoEnUsd") == null) return "redirect:/presupuesto";

        SeleccionDestinos seleccionDestinos =
                (SeleccionDestinos) sesion.getAttribute("seleccionDestinos");
        if (!seleccionDestino.hayDestinos(seleccionDestinos)) {
            return "redirect:/recomendaciones";
        }

        Optional<ResultadoRecomendacion> elegido = seleccionDestino.elegir(seleccionDestinos, id);

        // Si el id no corresponde a ninguno de los tres, regresa sin guardar.
        if (elegido.isEmpty()) {
            return "redirect:/recomendaciones";
        }

        sesion.setAttribute("destinoElegido", elegido.get());
        sesion.setAttribute("paisDestino", elegido.get().getCiudad().getPais());

        return "redirect:/plan";
    }
}
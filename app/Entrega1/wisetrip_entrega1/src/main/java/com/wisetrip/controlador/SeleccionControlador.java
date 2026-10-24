package com.wisetrip.controlador;

import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.wisetrip.modelo.FechasViaje;
import com.wisetrip.modelo.ResultadoRecomendacion;
import com.wisetrip.modelo.SeleccionDestinos;
import com.wisetrip.modelo.Usuario;
import com.wisetrip.negocio.ISeleccionDestino;
import com.wisetrip.negocio.IRepositorioViajes;
import com.wisetrip.negocio.IRepositorioPreferencias;

import jakarta.servlet.http.HttpSession;

/**
 * HU#68: permite al viajero elegir uno de los tres destinos recomendados
 * y continuar la planificación con ese destino.
 *
 * GRASP Controlador: recibe la petición web y delega la elección.
 * SOLID Inversión de dependencias: depende de las interfaces de negocio
 * ISeleccionDestino, IRepositorioViajes e IRepositorioPreferencias.
 */
@Controller
public class SeleccionControlador {

    private static final Logger log = LoggerFactory.getLogger(SeleccionControlador.class);

    private final ISeleccionDestino seleccionDestino;
    private final IRepositorioViajes viajeDAO;
    private final IRepositorioPreferencias preferenciaDAO;

    public SeleccionControlador(ISeleccionDestino seleccionDestino, IRepositorioViajes viajeDAO,
                                IRepositorioPreferencias preferenciaDAO) {
        this.seleccionDestino = seleccionDestino;
        this.viajeDAO = viajeDAO;
        this.preferenciaDAO = preferenciaDAO;
    }

    /**
     * Persiste el destino elegido, lo guarda en sesión y avanza al reparto
     * del presupuesto solo cuando el viaje y sus preferencias se han guardado.
     */
    @PostMapping("/destino/{id}")
    @SuppressWarnings("unchecked")
    public String elegirDestino(@PathVariable("id") int id, HttpSession sesion,
                                RedirectAttributes redirect) {

        Usuario usuario = (Usuario) sesion.getAttribute("usuarioActivo");
        if (usuario == null) return "redirect:/login";

        Map<String, Boolean> atributosCuestionario =
                (Map<String, Boolean>) sesion.getAttribute("atributosSeleccionados");
        if (atributosCuestionario == null) return "redirect:/preferencias";

        Double presupuestoUsd = (Double) sesion.getAttribute("presupuestoEnUsd");
        if (presupuestoUsd == null || !Double.isFinite(presupuestoUsd) || presupuestoUsd <= 0) {
            return "redirect:/presupuesto";
        }

        FechasViaje fechas = (FechasViaje) sesion.getAttribute("fechasViaje");
        if (fechas == null || !fechas.tieneRangoValido()) return "redirect:/fechas";

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

        // Serializa las elecciones de esta sesion dentro de la misma instancia del servidor.
        // La segunda peticion debe leer el ID que haya guardado la primera.
        synchronized (sesion) {
            try {
                Integer idViaje = (Integer) sesion.getAttribute("idViajeGuardado");
                int idCiudad = elegido.get().getCiudad().getId();
                if (idViaje == null) {
                    idViaje = viajeDAO.insertar(usuario.getIdUsuario(), idCiudad, fechas, presupuestoUsd);
                    sesion.setAttribute("idViajeGuardado", idViaje);
                } else {
                    viajeDAO.actualizarDestino(idViaje, usuario.getIdUsuario(), idCiudad);
                }

                // El ID ya queda en sesion para reutilizar el viaje si las preferencias fallan.
                preferenciaDAO.guardarActivas(idViaje, atributosCuestionario);
            } catch (IllegalArgumentException | IllegalStateException e) {
                log.error("No se pudo completar el guardado del viaje y sus preferencias", e);
                redirect.addFlashAttribute("errorGuardado",
                        "No pudimos completar el guardado de tu viaje. Elige el destino nuevamente para reintentar.");
                return "redirect:/recomendaciones";
            }

            sesion.setAttribute("destinoElegido", elegido.get());
            sesion.setAttribute("paisDestino", elegido.get().getCiudad().getPais());
        }

        return "redirect:/plan";
    }
}

package com.wisetrip.controlador;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.wisetrip.modelo.Ubicacion;
import com.wisetrip.modelo.Usuario;
import com.wisetrip.servicio.ViajeServicio;

import jakarta.servlet.http.HttpSession;

// Indica que esta clase funciona como controlador en Spring MVC
@Controller
public class ViajeControlador {

    // Servicio que contiene la lógica relacionada con la configuración del viaje
    private final ViajeServicio viajeServicio;

    // Spring inyecta automáticamente ViajeServicio mediante el constructor
    public ViajeControlador(ViajeServicio viajeServicio) {
        this.viajeServicio = viajeServicio;
    }

    // Atiende las solicitudes GET a /origen y muestra la pantalla de origen
    @GetMapping("/origen")
    public String mostrarOrigen(HttpSession sesion, Model model) {

        // Recupera de la sesión el usuario que inició sesión
        Usuario usuario = (Usuario) sesion.getAttribute("usuarioActivo");

        // Si no existe un usuario activo, lo redirige al login
        if (usuario == null) {
            return "redirect:/login";
        }

        // Busca si el usuario ya había guardado una ubicación anteriormente
        Ubicacion guardada = (Ubicacion) sesion.getAttribute("ubicacionOrigen");

        // Envía el usuario a la vista
        model.addAttribute("usuario", usuario);

        // Si existe una ubicación guardada la muestra,
        // de lo contrario crea una ubicación vacía para el formulario
        model.addAttribute("ubicacion", guardada != null ? guardada : new Ubicacion());

        // Envía a la vista la lista de países disponibles
        model.addAttribute("paises", viajeServicio.listarPaises());

        // Si existe una ubicación previa, carga las ciudades de ese país
        model.addAttribute("ciudades",
                guardada != null ? viajeServicio.listarCiudades(guardada.getPais()) : List.of());

        // Muestra la vista origen.jsp
        return "origen";
    }

    // Atiende el envío del formulario de origen
    @PostMapping("/origen")
    public String guardarOrigen(@ModelAttribute("ubicacion") Ubicacion ubicacion,
                                HttpSession sesion,
                                Model model) {

        // Recupera el usuario activo de la sesión
        Usuario usuario = (Usuario) sesion.getAttribute("usuarioActivo");

        // Evita acceder al proceso si el usuario no ha iniciado sesión
        if (usuario == null) {
            return "redirect:/login";
        }

        // Valida los datos de ubicación mediante viajeServicio
        Map<String, String> errores = viajeServicio.validarUbicacion(ubicacion);

        // Si existen errores, vuelve a mostrar el formulario
        if (!errores.isEmpty()) {

            // Envía los errores para mostrarlos en la vista
            model.addAttribute("errores", errores);

            // Vuelve a cargar los datos necesarios para el formulario
            model.addAttribute("usuario", usuario);
            model.addAttribute("paises", viajeServicio.listarPaises());
            model.addAttribute("ciudades", viajeServicio.listarCiudades(ubicacion.getPais()));

            return "origen";
        }

        // Elimina espacios innecesarios del detalle de ubicación
        if (ubicacion.getDetalle() != null) {
            ubicacion.setDetalle(ubicacion.getDetalle().trim());
        }

        // Guarda la ubicación seleccionada en la sesión del usuario
        sesion.setAttribute("ubicacionOrigen", ubicacion);

        // Continúa con el siguiente paso del flujo
        return "redirect:/preferencias";
    }
}

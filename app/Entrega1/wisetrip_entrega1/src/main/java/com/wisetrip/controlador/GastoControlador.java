package com.wisetrip.controlador;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.wisetrip.modelo.FechasViaje;
import com.wisetrip.modelo.Gasto;
import com.wisetrip.negocio.IGestorGastos;
import com.wisetrip.servicio.UsuarioSesionFacade;

import jakarta.servlet.http.HttpSession;

/**
 * HU#89: registra los gastos reales del viaje desde la pantalla Mi viaje.
 *
 * GRASP Controlador: recibe el formulario y delega en IGestorGastos, que
 * revisa el cupo de la categoria con el patron Factory Method
 * (ControlCategoriaCreador).
 * SOLID Inversion de dependencias: depende de la interfaz IGestorGastos,
 * no de GastosServicio.
 */
@Controller
public class GastoControlador {

    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(GastoControlador.class);
    private static final String VOLVER = "redirect:/viaje#presupuesto";
    private static final String MONEDA_VIAJE = "USD";

    private final IGestorGastos gestorGastos;
    private final UsuarioSesionFacade usuarioSesionFacade;

    public GastoControlador(IGestorGastos gestorGastos, UsuarioSesionFacade usuarioSesionFacade) {
        this.gestorGastos = gestorGastos;
        this.usuarioSesionFacade = usuarioSesionFacade;
    }

    @PostMapping("/viaje/gastos")
    public String registrarGasto(@RequestParam(defaultValue = "") String categoria,
                                 @RequestParam(defaultValue = "") String descripcion,
                                 @RequestParam(defaultValue = "") String monto,
                                 @RequestParam(defaultValue = "") String fecha,
                                 HttpSession sesion,
                                 RedirectAttributes redirect) {

        if (usuarioSesionFacade.usuarioActivo(sesion) == null) return "redirect:/login";

        Integer idViaje = (Integer) sesion.getAttribute("idViajeGuardado");
        if (idViaje == null) return "redirect:/viaje";

        Map<String, String> errores = new LinkedHashMap<>();
        BigDecimal montoGasto = leerMonto(monto, errores);
        LocalDate fechaGasto = leerFecha(fecha, (FechasViaje) sesion.getAttribute("fechasViaje"), errores);

        if (errores.isEmpty()) {
            Gasto gasto = new Gasto(0, categoria, descripcion.trim(), montoGasto, MONEDA_VIAJE, fechaGasto);
            try {
                // Aqui se usa el Factory Method: validarGasto revisa el cupo de la
                // categoria con el control estricto o flexible que le corresponde.
                errores.putAll(gestorGastos.validarGasto(idViaje, gasto));
                if (errores.isEmpty()) {
                    Map<String, String> avisos = gestorGastos.advertenciasGasto(idViaje, gasto);
                    gestorGastos.registrarGasto(idViaje, gasto);
                    redirect.addFlashAttribute("gastoRegistrado", true);
                    if (!avisos.isEmpty()) {
                        redirect.addFlashAttribute("avisoGasto", avisos.values().iterator().next());
                    }
                    return VOLVER;
                }
            } catch (IllegalArgumentException | IllegalStateException | NullPointerException e) {
                log.error("No se pudo registrar el gasto del viaje {}", idViaje, e);
                errores.put("general", "No pudimos registrar el gasto. Intenta nuevamente.");
            }
        }

        redirect.addFlashAttribute("erroresGasto", errores);
        redirect.addFlashAttribute("gastoForm", Map.of(
                "categoria", categoria, "descripcion", descripcion, "monto", monto, "fecha", fecha));
        return VOLVER;
    }

    private BigDecimal leerMonto(String texto, Map<String, String> errores) {
        try {
            BigDecimal valor = new BigDecimal(texto.trim().replace(',', '.'))
                    .setScale(2, RoundingMode.HALF_UP);
            if (valor.compareTo(new BigDecimal("9999999999.99")) > 0) {
                errores.put("monto", "El monto es demasiado grande.");
            }
            return valor;
        } catch (NumberFormatException _) {
            errores.put("monto", "Ingresa un monto válido, por ejemplo 25.50.");
            return null;
        }
    }

    /** La fecha debe ser valida y caer dentro de las fechas del viaje. */
    private LocalDate leerFecha(String texto, FechasViaje fechas, Map<String, String> errores) {
        try {
            LocalDate valor = LocalDate.parse(texto.trim());
            if (fechas != null && fechas.tieneRangoValido()
                    && (valor.isBefore(LocalDate.parse(fechas.getFechaInicio()))
                        || valor.isAfter(LocalDate.parse(fechas.getFechaFin())))) {
                errores.put("fecha", "La fecha debe estar entre el " + fechas.getFechaInicio()
                        + " y el " + fechas.getFechaFin() + ".");
            }
            return valor;
        } catch (DateTimeParseException _) {
            errores.put("fecha", "Ingresa la fecha del gasto.");
            return null;
        }
    }
}

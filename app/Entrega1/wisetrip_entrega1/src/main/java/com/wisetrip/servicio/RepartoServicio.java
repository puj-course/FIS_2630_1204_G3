package com.wisetrip.servicio;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.wisetrip.negocio.IDistribuible;
import com.wisetrip.negocio.IGestionReparto;

/**
 * HU#69 y HU#79: valida el reparto del presupuesto.
 *
 * Implementa la interfaz de negocio IGestionReparto.
 * SOLID Responsabilidad única: arma los mensajes para la vista; las
 * reglas (cuánto suma, si se pasa) las decide el dominio.
 * GRASP Bajo acoplamiento: solo conoce la abstracción IDistribuible.
 */
@Service
public class RepartoServicio implements IGestionReparto {

    /** Clave del error que se muestra arriba del formulario del reparto. */
    private static final String ERROR_GENERAL = "general";

    @Override
    public Map<String, String> validarReparto(IDistribuible reparto) {
        Map<String, String> errores = new LinkedHashMap<>();

        if (reparto.tienePorcentajesNegativos()) {
            errores.put(ERROR_GENERAL, "Ningún porcentaje puede ser negativo.");
            return errores;
        }

        if (reparto.excedeElMaximo()) {
            errores.put(ERROR_GENERAL, "Te estás pasando por " + reparto.porcentajeExcedido()
                    + "%. Ajusta el reparto para que sume 100%.");
        } else if (!reparto.estaCompleto()) {
            errores.put(ERROR_GENERAL, "Te faltan " + reparto.porcentajeDisponible()
                    + "% por repartir.");
        }

        if (!reparto.cubreLosBasicos()) {
            errores.put("basicos", "Deja algo para hospedaje o alimentación.");
        }

        return errores;
    }
}

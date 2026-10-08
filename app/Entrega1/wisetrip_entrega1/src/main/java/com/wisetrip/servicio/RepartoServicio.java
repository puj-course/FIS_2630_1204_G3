package com.wisetrip.servicio;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.wisetrip.negocio.IDistribuible;
import com.wisetrip.negocio.IGestionReparto;

/**
 * HU#69 y HU#79: valida el reparto del presupuesto y calcula cuánto
 * dinero corresponde a cada categoría.
 *
 * Implementa la interfaz de negocio IGestionReparto.
 * SOLID Responsabilidad única: arma los mensajes y los nombres para la
 * vista; las reglas (cuánto suma, si se pasa) las decide el dominio.
 * GRASP Bajo acoplamiento: solo conoce la abstracción IDistribuible.
 */
@Service
public class RepartoServicio implements IGestionReparto {

    /** Nombre visible de cada categoría, en el orden en que se muestran. */
    private static final Map<String, String> CATEGORIAS = new LinkedHashMap<>();

    static {
        CATEGORIAS.put("hospedaje", "Hospedaje");
        CATEGORIAS.put("alimentacion", "Alimentación");
        CATEGORIAS.put("transporte", "Transporte");
        CATEGORIAS.put("actividades", "Actividades");
        CATEGORIAS.put("imprevistos", "Imprevistos");
    }

    @Override
    public Map<String, String> listarCategorias() {
        return CATEGORIAS;
    }

    @Override
    public Map<String, String> validarReparto(IDistribuible reparto) {
        Map<String, String> errores = new LinkedHashMap<>();

        if (reparto.tienePorcentajesNegativos()) {
            errores.put("general", "Ningún porcentaje puede ser negativo.");
            return errores;
        }

        if (reparto.excedeElMaximo()) {
            errores.put("general", "Te estás pasando por " + reparto.porcentajeExcedido()
                    + "%. Ajusta el reparto para que sume 100%.");
        } else if (!reparto.estaCompleto()) {
            errores.put("general", "Te faltan " + reparto.porcentajeDisponible()
                    + "% por repartir.");
        }

        if (!reparto.cubreLosBasicos()) {
            errores.put("basicos", "Deja algo para hospedaje o alimentación.");
        }

        return errores;
    }

    @Override
    public Map<String, Double> calcularMontos(IDistribuible reparto, double presupuestoTotal) {
        Map<String, Double> montos = new LinkedHashMap<>();
        reparto.distribuir(presupuestoTotal)
               .forEach((clave, monto) -> montos.put(CATEGORIAS.get(clave), monto));
        return montos;
    }

    @Override
    public double porDia(double monto, long dias) {
        if (dias <= 0) return 0;
        return monto / dias;
    }
}
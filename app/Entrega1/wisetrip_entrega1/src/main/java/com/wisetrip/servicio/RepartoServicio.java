package com.wisetrip.servicio;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.wisetrip.modelo.RepartoPresupuesto;

/**
 * HU#69: valida el reparto del presupuesto y calcula cuánto dinero
 * corresponde a cada categoría.
 */
@Service
public class RepartoServicio {

    /** Nombre visible de cada categoría, en el orden en que se muestran. */
    private static final Map<String, String> CATEGORIAS = new LinkedHashMap<>();

    static {
        CATEGORIAS.put("hospedaje", "Hospedaje");
        CATEGORIAS.put("alimentacion", "Alimentación");
        CATEGORIAS.put("transporte", "Transporte");
        CATEGORIAS.put("actividades", "Actividades");
        CATEGORIAS.put("imprevistos", "Imprevistos");
    }

    public Map<String, String> listarCategorias() {
        return CATEGORIAS;
    }

    /**
     * Valida que ningún porcentaje sea negativo y que la suma dé 100.
     * Devuelve un mapa vacío si todo está correcto.
     */
    public Map<String, String> validarReparto(RepartoPresupuesto reparto) {
        Map<String, String> errores = new LinkedHashMap<>();

        if (reparto.getHospedaje() < 0 || reparto.getAlimentacion() < 0
                || reparto.getTransporte() < 0 || reparto.getActividades() < 0
                || reparto.getImprevistos() < 0) {
            errores.put("general", "Ningún porcentaje puede ser negativo.");
            return errores;
        }

        int total = reparto.getTotal();

        if (total != 100) {
            errores.put("general", total > 100
                    ? "Te estás pasando por " + (total - 100) + "%. Ajusta el reparto para que sume 100%."
                    : "Te faltan " + (100 - total) + "% por repartir.");
        }

        if (reparto.getHospedaje() == 0 && reparto.getAlimentacion() == 0) {
            errores.put("basicos", "Deja algo para hospedaje o alimentación.");
        }

        return errores;
    }

    /**
     * Convierte los porcentajes en montos, según el presupuesto total.
     * Devuelve un mapa con el nombre visible de la categoría y su monto.
     */
    public Map<String, Double> calcularMontos(RepartoPresupuesto reparto, double presupuestoTotal) {
        Map<String, Double> montos = new LinkedHashMap<>();

        montos.put("Hospedaje", presupuestoTotal * reparto.getHospedaje() / 100.0);
        montos.put("Alimentación", presupuestoTotal * reparto.getAlimentacion() / 100.0);
        montos.put("Transporte", presupuestoTotal * reparto.getTransporte() / 100.0);
        montos.put("Actividades", presupuestoTotal * reparto.getActividades() / 100.0);
        montos.put("Imprevistos", presupuestoTotal * reparto.getImprevistos() / 100.0);

        return montos;
    }

    /** Cuánto queda por día en cada categoría. */
    public double porDia(double monto, long dias) {
        if (dias <= 0) return 0;
        return monto / dias;
    }
}
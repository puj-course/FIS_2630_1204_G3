package com.wisetrip.servicio;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

<<<<<<< HEAD
import com.wisetrip.modelo.RepartoPresupuesto;
import com.wisetrip.negocio.IRepartoServicio;
=======
import com.wisetrip.negocio.IDistribuible;
import com.wisetrip.negocio.IGestionReparto;
>>>>>>> origin/develop

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
<<<<<<< HEAD
public class RepartoServicio implements IRepartoServicio {
=======
public class RepartoServicio implements IGestionReparto {
>>>>>>> origin/develop

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

<<<<<<< HEAD
    /**
     * Valida que ningún porcentaje sea negativo y que la suma dé 100.
     * Devuelve un mapa vacío si todo está correcto.
     */
    @Override
    public Map<String, String> validarReparto(RepartoPresupuesto reparto) {
=======
    @Override
    public Map<String, String> validarReparto(IDistribuible reparto) {
>>>>>>> origin/develop
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

<<<<<<< HEAD
    /**
     * Convierte los porcentajes en montos, según el presupuesto total.
     * Devuelve un mapa con el nombre visible de la categoría y su monto.
     */
    @Override
    public Map<String, Double> calcularMontos(RepartoPresupuesto reparto, double presupuestoTotal) {
=======
    @Override
    public Map<String, Double> calcularMontos(IDistribuible reparto, double presupuestoTotal) {
>>>>>>> origin/develop
        Map<String, Double> montos = new LinkedHashMap<>();
        reparto.distribuir(presupuestoTotal)
               .forEach((clave, monto) -> montos.put(CATEGORIAS.get(clave), monto));
        return montos;
    }

<<<<<<< HEAD
    /** Cuánto queda por día en cada categoría. */
=======
>>>>>>> origin/develop
    @Override
    public double porDia(double monto, long dias) {
        if (dias <= 0) return 0;
        return monto / dias;
    }
}
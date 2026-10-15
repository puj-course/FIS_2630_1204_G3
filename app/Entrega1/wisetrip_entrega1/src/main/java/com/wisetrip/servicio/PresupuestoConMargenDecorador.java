package com.wisetrip.servicio;

import com.wisetrip.modelo.Presupuesto;
import com.wisetrip.negocio.GestorPresupuesto;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@Primary
public class PresupuestoConMargenDecorador implements GestorPresupuesto {

    private final GestorPresupuesto gestorBase;
    private static final double MARGEN_IMPREVISTOS = 1.05;

    public PresupuestoConMargenDecorador(PresupuestoServicio gestorBase) {
        this.gestorBase = gestorBase;
    }

    @Override
    public Map<String, String> validarPresupuesto(Presupuesto presupuesto, String paisDestino) {
        return gestorBase.validarPresupuesto(presupuesto, paisDestino);
    }

    @Override
    public double convertirAUsd(Presupuesto presupuesto) {
        double baseUsd = gestorBase.convertirAUsd(presupuesto);
        if (baseUsd <= 0) return 0;
        return Math.round((baseUsd * MARGEN_IMPREVISTOS) * 100.0) / 100.0;
    }

    @Override
    public Map<String, String> monedasDisponibles(String paisDestino) {
        return gestorBase.monedasDisponibles(paisDestino);
    }

    @Override
    public String nombreMoneda(String codigo) {
        return gestorBase.nombreMoneda(codigo);
    }

    @Override
    public String formatear(double valor) {
        return gestorBase.formatear(valor);
    }
}
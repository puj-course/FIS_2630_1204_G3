
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

    public PresupuestoConMargenDecorador(PresupuestoServicio gestorBase) {
        this.gestorBase = gestorBase;
    }

    @Override
    public Map<String, String> validarPresupuesto(Presupuesto presupuesto, String paisDestino) {
        return gestorBase.validarPresupuesto(presupuesto, paisDestino);
    }

    @Override
    public double convertirAUsd(Presupuesto presupuesto) {
        return gestorBase.convertirAUsd(presupuesto);
    }

    public double calcularMargenImprevistos(Presupuesto presupuesto) {
        double presupuestoUsd = gestorBase.convertirAUsd(presupuesto);

        if (presupuestoUsd <= 0) {
            return 0;
        }

        return Math.round((presupuestoUsd * 0.05) * 100.0) / 100.0;
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

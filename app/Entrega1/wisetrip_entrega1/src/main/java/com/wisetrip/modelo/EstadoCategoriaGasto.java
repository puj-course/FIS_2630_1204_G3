package com.wisetrip.modelo;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Cuanto le toca a una categoria del presupuesto (cupo) y cuanto se ha
 * gastado en ella. Lo usa la pantalla Mi viaje para mostrar el avance.
 *
 * @param categoria clave de la categoria (por ejemplo, "hospedaje")
 * @param cupo      dinero que el reparto le asigno a la categoria
 * @param gastado   suma de los gastos registrados en la categoria
 */
public record EstadoCategoriaGasto(String categoria, BigDecimal cupo, BigDecimal gastado) {

    /** Lo que todavia queda del cupo (negativo si ya se paso). */
    public BigDecimal getRestante() {
        return cupo.subtract(gastado);
    }

    /** Porcentaje del cupo ya usado, de 0 a 100 (para la barra). */
    public int getPorcentajeUsado() {
        if (cupo.signum() <= 0) {
            return gastado.signum() > 0 ? 100 : 0;
        }
        int porcentaje = gastado.multiply(BigDecimal.valueOf(100))
                .divide(cupo, 0, RoundingMode.HALF_UP).intValue();
        return Math.min(porcentaje, 100);
    }

    /** true si lo gastado ya supera el cupo. */
    public boolean isExcedido() {
        return gastado.compareTo(cupo) > 0;
    }

    public String getCategoria() { return categoria; }
    public BigDecimal getCupo() { return cupo; }
    public BigDecimal getGastado() { return gastado; }
}

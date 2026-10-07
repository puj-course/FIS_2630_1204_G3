//toca entenderlo mucho mas
//algoritmo de recomendacion por presupuesto y preferencias

package com.wisetrip.servicio;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.LinkedHashMap;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;

import com.wisetrip.modelo.Ciudad;
import com.wisetrip.modelo.Importancia;
import com.wisetrip.modelo.EstadoAtributo;
import com.wisetrip.modelo.ValidacionCiudades;
import com.wisetrip.modelo.PreferenciasUsuario;
import com.wisetrip.modelo.ResultadoReco;

@Service
public class RecomendadorDestinos implements Recomendador {



    private final boolean presupuestoObligatorio;

    public RecomendadorDestinos(
            @Value("${wisetrip.presupuesto.obligatorio:true}") boolean presupuestoObligatorio) {
        this.presupuestoObligatorio = presupuestoObligatorio;
    }

    /**
     * Puntaje (0 a 1) de viabilidad presupuestal. Dentro del limite todos
     * reciben 1: no se premia gastar mas ni ahorrar. El exceso se penaliza.
     */
    public double calcularPuntajePresupuesto(double costo, double presupuesto) {
        if (presupuesto <= 0) {
            return 0.0;
        }
        if (costo <= presupuesto) {
            return 1.0;
        } else {
            double exceso = (costo - presupuesto) / presupuesto;
            return Math.max(0.0, 1 - exceso);
        }
    }

    /**
     * Factor minimo del castigo por "Lo prefiero": una ciudad que no cumple ninguno
     * conserva la mitad de su coincidencia; si cumple todos, no se castiga.
     */
    static final double FACTOR_MINIMO_PREFIERO = 0.5;

    /**
     * Coincidencia (0 a 1) de las preferencias del usuario con la oferta de la ciudad.
     * Es la coincidencia ponderada de "Me gustaría" (peso 1) y "Lo prefiero" (peso 3),
     * multiplicada por el castigo por los "Lo prefiero" que la ciudad no cumple.
     * Los indispensables ({@link Importancia#si}) no suman puntos: se validan antes.
     */

    public double calcularPuntajePreferencias(Map<String, Boolean> oferta,
                                              Map<String, Importancia> gustos) {
        return calcularCoincidenciaPonderada(oferta, gustos)
                * calcularFactorPrefiero(oferta, gustos);
    }

    /**
     * Peso logrado / peso total de todas las preguntas puntuables (gustar y prefiero),
     * sin agrupar por categoria. Los datos desconocidos cuentan como no cumplidos.
     */
    public double calcularCoincidenciaPonderada(Map<String, Boolean> oferta,
                                                   Map<String, Importancia> gustos) {
        int pesoTotal = 0;
        int pesoLogrado = 0;
        for (var categoria : PreferenciasServicio.CATEGORIAS) {
            for (var pregunta : categoria.getPreguntas()) {
                Importancia importancia = gustos.get(pregunta.getClave());
                if (importancia != null && importancia.peso > 0) {
                    pesoTotal += importancia.peso;
                    if (Boolean.TRUE.equals(oferta.get(pregunta.getClave()))) {
                        pesoLogrado += importancia.peso;
                    }
                }
            }
        }
        return pesoTotal == 0 ? 0.0 : (double) pesoLogrado / pesoTotal;
    }

    /**
     * Factor de 0.5 a 1 segun el porcentaje de respuestas "Lo prefiero" que cumple la ciudad.
     * Sin ningun "Lo prefiero" el factor es 1 (no hay castigo).
     */
    public double calcularFactorPrefiero(Map<String, Boolean> oferta,
                                             Map<String, Importancia> gustos) {
        int total = 0;
        int cumplidos = 0;
        for (var categoria : PreferenciasServicio.CATEGORIAS) {
            for (var pregunta : categoria.getPreguntas()) {
                if (gustos.get(pregunta.getClave()) == Importancia.prefiero) {
                    total++;
                    if (Boolean.TRUE.equals(oferta.get(pregunta.getClave()))) {
                        cumplidos++;
                    }
                }
            }
        }
        if (total == 0) {
            return 1.0;
        }
        return FACTOR_MINIMO_PREFIERO
                + (1 - FACTOR_MINIMO_PREFIERO) * cumplidos / total;
    }

    /**
     * Coincidencia de 0 a 1 por categoria del cuestionario (desglose informativo;
     * no interviene en el puntaje total).
     * Solo participan respuestas con peso positivo en Importancia.
     * Los datos desconocidos conservan su peso en el denominador.
     * Se omiten categorias sin preferencias puntuables.
     */
    public Map<String, Double> calcularCoincidenciasPorCategoria(Map<String, Boolean> oferta,
                                                                 Map<String, Importancia> gustos) {
        Map<String, Double> coincidencias = new LinkedHashMap<>();
        for (var categoria : PreferenciasServicio.CATEGORIAS) {
            int pesoTotal = 0;
            int pesoLogrado = 0;
            for (var pregunta : categoria.getPreguntas()) {
                Importancia importancia = gustos.get(pregunta.getClave());
                if (importancia == null || importancia.peso <= 0) continue;
                pesoTotal += importancia.peso;
                if (Boolean.TRUE.equals(oferta.get(pregunta.getClave()))) {
                    pesoLogrado += importancia.peso;
                }
            }
            if (pesoTotal > 0) {
                coincidencias.put(categoria.getNombre(), (double) pesoLogrado / pesoTotal);
            }
        }
        return coincidencias;
    }

    @Override
    public ResultadoReco calcularPuntajeTotal(
            Ciudad ciudad,
            PreferenciasUsuario preferencias) {
        long dias = preferencias.getDuracionDias();
        double diario = ciudad.getCostoPromedio();
        double costo = diario * dias; // Costo de la estancia por persona.
        double presupuesto = preferencias.getPresupuesto();

        double pCosto = calcularPuntajePresupuesto(costo, presupuesto);
        double pGustos = calcularPuntajePreferencias(
                ciudad.getAtributos(), preferencias.getAtributos());
        boolean provisional = preferencias.getAtributos().entrySet().stream()
                .anyMatch(e -> e.getValue() != null && e.getValue().peso > 0
                        && ciudad.estadoAtributo(e.getKey()) == EstadoAtributo.noSabemos);

        return new ResultadoReco(ciudad, pGustos, pCosto, pGustos,
                preferencias.tienePreferenciasPuntuables(), provisional);
    }

    /**
     * Diferencia absoluta entre el costo del viaje (diario × días) y el presupuesto.
     * Menor valor = más cerca del presupuesto del usuario.
     */
    public double diferenciaPresupuesto(Ciudad ciudad, PreferenciasUsuario preferencias) {
        double costoViaje = ciudad.getCostoPromedio() * preferencias.getDuracionDias();
        return Math.abs(costoViaje - preferencias.getPresupuesto());
    }

    public List<ResultadoReco> recomendarDestinos(List<Ciudad> ciudades,
                                                           PreferenciasUsuario preferencias) {
        return puntuarCiudadesValidas(validarCiudades(ciudades, preferencias), preferencias);
    }

    public ValidacionCiudades validarCiudades(List<Ciudad> ciudades, PreferenciasUsuario preferencias) {
        List<Ciudad> validas = new ArrayList<>();
        List<Ciudad> descartadas = new ArrayList<>();
        List<Ciudad> pendientes = new ArrayList<>();
        for (Ciudad ciudad : ciudades) {
            double costo = ciudad.getCostoPromedio() * preferencias.getDuracionDias();
            boolean faltaInformacion = !Double.isFinite(costo) || costo <= 0;
            boolean incumple = !faltaInformacion && presupuestoObligatorio
                    && costo > preferencias.getPresupuesto();
            for (var requisito : preferencias.getAtributos().entrySet()) {
                if (requisito.getValue() != Importancia.si) continue;
                EstadoAtributo estado = ciudad.estadoAtributo(requisito.getKey());
                incumple |= estado == EstadoAtributo.noCumple;
                faltaInformacion |= estado == EstadoAtributo.noSabemos;
            }
            // Un incumplimiento confirmado basta para descartar, aunque falten otros datos.
            if (incumple) descartadas.add(ciudad);
            else if (faltaInformacion) pendientes.add(ciudad);
            else validas.add(ciudad);
        }
        return new ValidacionCiudades(validas, descartadas, pendientes);
    }

    public List<ResultadoReco> puntuarCiudadesValidas(ValidacionCiudades validacion,
                                                              PreferenciasUsuario preferencias) {
        List<ResultadoReco> resultados = new ArrayList<>();
        for (Ciudad ciudad : validacion.validas()) {
            resultados.add(calcularPuntajeTotal(ciudad, preferencias));
        }

        // Desempate / orden por cercanía al presupuesto cuando no hay nada que
        // puntuar (solo indispensables o ningún gustar/prefiero) o todos empatan.
        boolean ordenarPorPresupuesto = !preferencias.tienePreferenciasPuntuables()
                || todosMismoPuntaje(resultados);

        if (ordenarPorPresupuesto) {
            resultados.sort(Comparator.comparingDouble(
                    r -> diferenciaPresupuesto(r.getCiudad(), preferencias)));
        } else {
            resultados.sort(Comparator
                    .comparingDouble(ResultadoReco::getPuntajeTotal).reversed()
                    .thenComparingDouble(r -> diferenciaPresupuesto(r.getCiudad(), preferencias)));
        }

        return resultados;
    }

    private static boolean todosMismoPuntaje(List<ResultadoReco> resultados) {
        if (resultados.size() <= 1) {
            return true;
        }
        double primero = resultados.get(0).getPuntajeTotal();
        for (ResultadoReco resultado : resultados) {
            if (Double.compare(resultado.getPuntajeTotal(), primero) != 0) {
                return false;
            }
        }
        return true;
    }
}

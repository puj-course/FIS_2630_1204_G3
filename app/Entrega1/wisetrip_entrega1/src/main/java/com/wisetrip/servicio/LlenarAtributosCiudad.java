package com.wisetrip.servicio;

import com.wisetrip.datos.CiudadSemilla;
import com.wisetrip.modelo.Ciudad;
import com.wisetrip.modelo.EstadoAtributo;
import com.wisetrip.modelo.Importancia;
import com.wisetrip.modelo.TipoAtributo;
import com.wisetrip.negocio.CatalogoPreguntas;
import com.wisetrip.negocio.DefPregunta;
import com.wisetrip.negocio.ProovedorLugares;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class LlenarAtributosCiudad {

    //constante que indica 50 km
    private static final int RADIO_BUSQUEDA_METROS = 50_000;

    // Esta es la interfaz que usamos para consultar lugares.

    private final ProovedorLugares proveedorLugares;
    //complejidad manual = 1 de la dependencia.
    public LlenarAtributosCiudad(ProovedorLugares proveedorLugares) {
        this.proveedorLugares = proveedorLugares;
    }

    //omplejidad ciclomatica manual = 7 (1 base + 3 if + 1 for + 1 ternario + 1 &&).

    public void enriquecer(
            Ciudad ciudad,
            CiudadSemilla semilla,
            Map<String, Boolean> atributosUsuario,
            Map<String, Importancia> importancias) {

        Map<String, Boolean> atributos = new HashMap<>(semilla.atributosManuales());

        if (incumpleIndispensableManual(atributos, importancias)) {
            ciudad.setAtributos(atributos);
            return;
        }

        for (DefPregunta pregunta : CatalogoPreguntas.atributosAuto()) {
            // Si no hay un mapa de preferencias, revisamos todas las preguntas.
            boolean revisarPregunta = true;

            if (atributosUsuario != null) {
                Boolean atributoActivo = atributosUsuario.get(pregunta.id);
                revisarPregunta = Boolean.TRUE.equals(atributoActivo);
            }

            if (revisarPregunta) {
                Integer cantidad = proveedorLugares.contarLugares(
                        ciudad.getLatitud(),
                        ciudad.getLongitud(),
                        pregunta.categorias,
                        RADIO_BUSQUEDA_METROS,
                        pregunta.umbral
                );

                EstadoAtributo estado = EstadoAtributo.desdeConteo(
                        cantidad, pregunta.umbral
                );

                if (estado == EstadoAtributo.NO_SABEMOS) {
                    atributos.remove(pregunta.id);
                } else if (estado == EstadoAtributo.CUMPLE) {
                    atributos.put(pregunta.id, true);
                } else {
                    atributos.put(pregunta.id, false);
                }
            }
        }

        ciudad.setAtributos(atributos);
    }

    // HU142: complejidad ciclomatica manual = 6 (1 base + 1 for + 1 if + 3 &&).
    // Las decisiones internas de esIndispensable() se miden en su propio metodo.
    // Recalcular este valor si cambian las decisiones del metodo.
    private boolean incumpleIndispensableManual(Map<String, Boolean> atributos,
                                                Map<String, Importancia> importancias) {
        for (DefPregunta pregunta : CatalogoPreguntas.todasLasPreguntas()) {
            Importancia importancia = importancias.get(pregunta.id);
            // Desconocido no significa incumplido.
            if (pregunta.tipo == TipoAtributo.MANUAL
                    && importancia != null && importancia.esIndispensable()
                    && Boolean.FALSE.equals(atributos.get(pregunta.id))) {
                return true;
            }
        }
        return false;
    }
}

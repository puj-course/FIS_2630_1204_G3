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

    // Esta es la interfaz que usamos para consultar lugares.
    private final ProovedorLugares geo;
    public LlenarAtributosCiudad(ProovedorLugares geo) {
        this.geo = geo;
    }

    public void enriquecer(Ciudad ciudad, CiudadSemilla semilla) {
        enriquecer(ciudad, semilla, null);
    }

    //este es el metodo principal
    //toma una ciudad y le va agregando sus atributos.

    public void enriquecer(
            Ciudad ciudad,
            CiudadSemilla semilla,
            Map<String, Boolean> atributosUsuario) {
        enriquecer(ciudad, semilla, atributosUsuario, Map.of());
    }

    public void enriquecer(Ciudad ciudad, CiudadSemilla semilla,
                          Map<String, Boolean> atributosUsuario,
                          Map<String, Importancia> importancias) {

        //atributos manuales
        Map<String, Boolean> atributos =
                new HashMap<>(semilla.atributosManuales());
        // Conserva el incumplimiento para la validacion final.
        if (incumpleIndispensableManual(atributos, importancias)) {
            ciudad.setAtributos(atributos);
            return;
        }
        // Si no mandaron preferencias, queda vacío.
        Set<String> atributosActivos =
                atributosUsuario == null
                        ? Set.of()
                        : atributosUsuario.entrySet()
                          .stream()
                          .filter(entry ->
                                  Boolean.TRUE.equals(entry.getValue()))
                          .map(Map.Entry::getKey)
                          .collect(Collectors.toSet());

        // Recorrer las preguntas de la api las AUto
        for (DefPregunta pregunta : CatalogoPreguntas.atributosAuto()) {
            if (atributosUsuario != null
                    && !atributosActivos.contains(pregunta.id)) {
                continue;
            }
            // Le preguntamos al proveedor cuantos lugares hay
            // cerca de la ciudad para esa categoría.
            Integer cantidad = geo.contarLugares(
                    ciudad.getLatitud(),
                    ciudad.getLongitud(),
                    pregunta.categorias,
                    //coordenadas o bueno los kilometros a la redonda
                    50_000,
                    // Basta con alcanzar el umbral.
                    pregunta.umbral
            );

            EstadoAtributo estado =
                    EstadoAtributo.desdeConteo(
                            cantidad,
                            pregunta.umbral
                    );
            // Si hubo un problema y no sabemos el resultado
            // no ponemos false porque false significaria que no cumple.
            if (estado == EstadoAtributo.noSabemos) {
                atributos.remove(pregunta.id);
            } else {
                // Guardamos true si cumple y false si no cumple.
                atributos.put(
                        pregunta.id,
                        estado == EstadoAtributo.cumple
                );
            }
        }

        // Al final guardamos todos los atributos calculados
        // dentro de la ciudad.
        ciudad.setAtributos(atributos);
    }

    private boolean incumpleIndispensableManual(Map<String, Boolean> atributos,
                                                Map<String, Importancia> importancias) {
        for (DefPregunta pregunta : CatalogoPreguntas.todas()) {
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

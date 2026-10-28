package com.wisetrip.servicio;

import com.wisetrip.datos.CiudadSemilla;
import com.wisetrip.datos.DatosCiudades;
import com.wisetrip.modelo.Ciudad;
import com.wisetrip.modelo.EstadoAtributo;
import com.wisetrip.negocio.CatalogoPreguntas;
import com.wisetrip.negocio.DefPregunta;
import com.wisetrip.negocio.ProovedorLugares;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

@Service
public class LlenarAtributosCiudad {

    private final ProovedorLugares geo;

    public LlenarAtributosCiudad(ProovedorLugares geo) {
        this.geo = geo;
    }

    /**
     * Completa los atributos de cada ciudad que tenga semilla: los manuales
     * salen de la semilla y los automáticos se consultan al proveedor de
     * lugares, solo para los atributos que el usuario marcó.
     */
    public void enriquecer(List<Ciudad> ciudades, Map<String, Boolean> atributosUsuario) {
        for (Ciudad ciudad : ciudades) {
            CiudadSemilla semilla = DatosCiudades.porNombreYPais(ciudad.getNombre(), ciudad.getPais());
            if (semilla != null) {
                enriquecer(ciudad, semilla, atributosUsuario);
            }
        }
    }

    private void enriquecer(Ciudad ciudad, CiudadSemilla semilla, Map<String, Boolean> atributosUsuario) {
        Map<String, Boolean> atributos = new HashMap<>(semilla.atributosManuales());
        Set<String> atributosActivos = atributosUsuario == null ? Set.of() :
                atributosUsuario.entrySet().stream()
                        .filter(entry -> Boolean.TRUE.equals(entry.getValue()))
                        .map(Map.Entry::getKey)
                        .collect(Collectors.toSet());

        for (DefPregunta pregunta : CatalogoPreguntas.atributosAuto()) {
            if (atributosUsuario != null && !atributosActivos.contains(pregunta.id)) {
                continue;
            }
            Integer cantidad = geo.contarLugares(ciudad.getLatitud(), ciudad.getLongitud(),
                    pregunta.categorias, 50_000, 500);
            EstadoAtributo estado = EstadoAtributo.desdeConteo(cantidad, pregunta.umbral);
            // Ausente significa desconocido; false queda reservado a incumplimiento.
            if (estado == EstadoAtributo.noSabemos) {
                atributos.remove(pregunta.id);
            } else {
                atributos.put(pregunta.id, estado == EstadoAtributo.cumple);
            }
        }
        ciudad.setAtributos(atributos);
    }
}

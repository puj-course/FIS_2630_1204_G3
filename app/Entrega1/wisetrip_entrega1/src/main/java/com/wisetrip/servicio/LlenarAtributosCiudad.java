package com.wisetrip.servicio;

import com.wisetrip.datos.CiudadSemilla;
import com.wisetrip.modelo.Ciudad;
import com.wisetrip.negocio.CatalogoPreguntas;
import com.wisetrip.negocio.DefPregunta;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

@Service
public class LlenarAtributosCiudad {

    private final ServicioGeoapify geo;

    public LlenarAtributosCiudad(ServicioGeoapify geo) {
        this.geo = geo;
    }

    public void enriquecer(Ciudad ciudad, CiudadSemilla semilla) {
        enriquecer(ciudad, semilla, null);
    }

    public void enriquecer(Ciudad ciudad, CiudadSemilla semilla, Map<String, Boolean> atributosUsuario) {
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
            int cantidad = geo.contarLugares(ciudad.getLatitud(), ciudad.getLongitud(),
                    pregunta.categorias, 50_000, 500);
            atributos.put(pregunta.id, cantidad >= pregunta.umbral);
        }
        ciudad.setAtributos(atributos);
    }
}

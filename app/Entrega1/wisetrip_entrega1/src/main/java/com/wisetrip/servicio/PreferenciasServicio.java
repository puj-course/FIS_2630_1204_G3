package com.wisetrip.servicio;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.wisetrip.modelo.CategoriaPreferencia;
import com.wisetrip.modelo.Preferencias;
import com.wisetrip.modelo.Pregunta;

@Service
public class PreferenciasServicio {

    /**
     * Banco de preguntas del cuestionario de preferencias.
     * Para agregar, quitar o cambiar preguntas solo se edita esta lista:
     * la vista, el contador y la validacion se ajustan solos.
     */
    public static final List<CategoriaPreferencia> CATEGORIAS = List.of(

        new CategoriaPreferencia("Tipo de destino", "",
            "Qué clase de lugar quieres visitar",
            List.of(
                new Pregunta("destino_playa",      "¿Te gustaría que tu viaje incluya playa?"),
                new Pregunta("destino_montana",    "¿Te gustaría visitar zonas de montaña?"),
                new Pregunta("destino_naturaleza", "¿Te interesa estar en contacto con la naturaleza (parques, reservas, bosques)?")
            )),

        new CategoriaPreferencia("Clima", "",
            "La temperatura que prefieres durante el viaje",
            List.of(
                new Pregunta("clima_calido", "¿Prefieres un destino de clima cálido?"),
                new Pregunta("clima_frio",   "¿Prefieres un destino de clima frío?"),
                new Pregunta("clima_nieve",  "¿Te gustaría que tu destino tenga nieve?")
            )),

        new CategoriaPreferencia("Aventura", "",
            "Qué tanta actividad física y adrenalina buscas",
            List.of(
                new Pregunta("aventura_actividades", "¿Te gustaría hacer actividades de aventura (senderismo, rafting, etc.)?"),
                new Pregunta("aventura_extremos",    "¿Te interesan los deportes extremos (parapente, buceo, escalada)?")
            )),

        new CategoriaPreferencia("Gastronomía", "",
            "Cómo quieres comer durante el viaje",
            List.of(
                new Pregunta("gastro_tipica",        "¿Es importante para ti probar la comida típica del destino?"),
                new Pregunta("gastro_gourmet",       "¿Te interesan los restaurantes de alta cocina o gourmet?"),
                new Pregunta("gastro_restricciones", "¿Tienes restricciones alimentarias (vegetariano, vegano, sin gluten, alergias)?"),
                new Pregunta("gastro_internacional", "¿Prefieres tener acceso a comida internacional o conocida además de la local?")
            )),

        new CategoriaPreferencia("Ritmo de viaje", "",
            "Qué tan cargada y movida quieres la agenda",
            List.of(
                new Pregunta("ritmo_descanso",    "¿Buscas un viaje principalmente de descanso y relajación?"),
                new Pregunta("ritmo_tranquilo",   "¿Prefieres destinos tranquilos, alejados del bullicio?"),
                new Pregunta("ritmo_nocturna",    "¿Te interesa la vida nocturna (bares, fiestas, discotecas)?"),
                new Pregunta("ritmo_urbano",      "¿Prefieres destinos urbanos o ciudades grandes?"),
                new Pregunta("ritmo_compras",     "¿Te interesa hacer compras durante tu viaje (mercados, centros comerciales)?"),
                new Pregunta("ritmo_improvisar",  "¿Prefieres tener tiempo libre para improvisar durante el viaje?"),
                new Pregunta("ritmo_actividades", "¿Prefieres un viaje con muchas actividades por día?")
            )),

        new CategoriaPreferencia("Cultura", "",
            "Museos, historia y tradiciones locales",
            List.of(
                new Pregunta("cultura_local",     "¿Te interesa conocer la cultura local del destino?"),
                new Pregunta("cultura_museos",    "¿Te gustaría visitar museos o galerías de arte?"),
                new Pregunta("cultura_religioso", "¿Te interesa visitar sitios religiosos o espirituales?")
            )),

        new CategoriaPreferencia("Estilo de viaje", "",
            "El nivel de comodidad y gasto que buscas",
            List.of(
                new Pregunta("estilo_lujo",      "¿Buscas experiencias de lujo (hoteles premium, servicios exclusivos)?"),
                new Pregunta("estilo_mochilero", "¿Prefieres un viaje económico tipo mochilero?")
            )),

        new CategoriaPreferencia("Compañía", "",
            "Con quién viajas y qué necesitas por eso",
            List.of(
                new Pregunta("compania_ninos",    "¿Viajas con niños y necesitas actividades aptas para ellos?"),
                new Pregunta("compania_mascotas", "¿Viajas con mascotas y necesitas lugares pet-friendly?")
            ))
    );

    public List<CategoriaPreferencia> listarCategorias() {
        return CATEGORIAS;
    }

    /** Total de preguntas del banco. Se calcula, no se escribe a mano. */
    public int totalPreguntas() {
        return CATEGORIAS.stream().mapToInt(c -> c.getPreguntas().size()).sum();
    }

    /**
     * Valida que todas las preguntas esten respondidas.
     * Devuelve un mapa: clave de la pregunta -> mensaje de error.
     * Mapa vacio significa que todo esta correcto.
     */
    public Map<String, String> validarPreferencias(Preferencias preferencias) {
        Map<String, String> errores = new LinkedHashMap<>();
        Map<String, String> respuestas = preferencias.getRespuestas();

        for (CategoriaPreferencia categoria : CATEGORIAS) {
            for (Pregunta pregunta : categoria.getPreguntas()) {
                String valor = respuestas.get(pregunta.getClave());
                if (valor == null || valor.isBlank()) {
                    errores.put(pregunta.getClave(), "Falta responder esta pregunta.");
                } else if (!valor.equals("si") && !valor.equals("no")) {
                    errores.put(pregunta.getClave(), "Respuesta no válida.");
                }
            }
        }
        return errores;
    }

    /**
     * Convierte las respuestas en atributos booleanos.
     * Esta es la entrada que consume el algoritmo de recomendacion.
     */
    public Map<String, Boolean> obtenerAtributosSeleccionados(Preferencias preferencias) {
        Map<String, Boolean> atributos = new LinkedHashMap<>();

        for (CategoriaPreferencia categoria : CATEGORIAS) {
            for (Pregunta pregunta : categoria.getPreguntas()) {
                String valor = preferencias.getRespuestas().get(pregunta.getClave());
                atributos.put(pregunta.getClave(), "si".equals(valor));
            }
        }
        return atributos;
    }

    /** Solo las claves respondidas con SI. Util para mostrar el resumen. */
    public List<String> clavesAfirmativas(Preferencias preferencias) {
        return preferencias.getRespuestas().entrySet().stream()
                .filter(e -> "si".equals(e.getValue()))
                .map(Map.Entry::getKey)
                .toList();
    }
        /** Etiqueta corta para mostrar en el resumen, en vez de la pregunta completa. */
    private static final Map<String, String> ETIQUETAS = Map.ofEntries(
        Map.entry("destino_playa",        "Playa"),
        Map.entry("destino_montana",      "Montaña"),
        Map.entry("destino_naturaleza",   "Naturaleza"),
        Map.entry("clima_calido",         "Clima cálido"),
        Map.entry("clima_frio",           "Clima frío"),
        Map.entry("clima_nieve",          "Nieve"),
        Map.entry("aventura_actividades", "Aventura"),
        Map.entry("aventura_extremos",    "Deportes extremos"),
        Map.entry("gastro_tipica",        "Comida típica"),
        Map.entry("gastro_gourmet",       "Alta cocina"),
        Map.entry("gastro_restricciones", "Restricciones alimentarias"),
        Map.entry("gastro_internacional", "Comida internacional"),
        Map.entry("ritmo_descanso",       "Descanso"),
        Map.entry("ritmo_tranquilo",      "Destinos tranquilos"),
        Map.entry("ritmo_nocturna",       "Vida nocturna"),
        Map.entry("ritmo_urbano",         "Ciudades grandes"),
        Map.entry("ritmo_compras",        "Compras"),
        Map.entry("ritmo_improvisar",     "Tiempo libre"),
        Map.entry("ritmo_actividades",    "Agenda cargada"),
        Map.entry("cultura_local",        "Cultura local"),
        Map.entry("cultura_museos",       "Museos"),
        Map.entry("cultura_religioso",    "Sitios religiosos"),
        Map.entry("estilo_lujo",          "Lujo"),
        Map.entry("estilo_mochilero",     "Mochilero"),
        Map.entry("compania_ninos",       "Viaja con niños"),
        Map.entry("compania_mascotas",    "Viaja con mascotas")
    );

    /** Categoria -> lista de etiquetas cortas respondidas con SI. */
    public Map<String, List<String>> resumenPorCategoria(Preferencias preferencias) {
        Map<String, List<String>> resumen = new LinkedHashMap<>();

        for (CategoriaPreferencia categoria : CATEGORIAS) {
            List<String> elegidas = categoria.getPreguntas().stream()
                    .filter(p -> "si".equals(preferencias.getRespuestas().get(p.getClave())))
                    .map(p -> ETIQUETAS.getOrDefault(p.getClave(), p.getTexto()))
                    .toList();
            resumen.put(categoria.getNombre(), elegidas);
        }
        return resumen;
    }

    /** Cuantas preguntas se respondieron con SI. */
    public long contarAfirmativas(Preferencias preferencias) {
        return preferencias.getRespuestas().values().stream()
                .filter("si"::equals)
                .count();
    }
}
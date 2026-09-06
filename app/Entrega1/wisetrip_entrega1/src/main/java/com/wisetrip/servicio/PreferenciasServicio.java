package com.wisetrip.servicio;

import com.wisetrip.modelo.CategoriaPreferencia;
import com.wisetrip.modelo.Preferencias;
import com.wisetrip.modelo.Pregunta;
import com.wisetrip.negocio.CatalogoPreguntas;
import com.wisetrip.negocio.DefPregunta;

import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class PreferenciasServicio {

    public static final List<CategoriaPreferencia> CATEGORIAS = List.of(
        categoria("Tipo de destino", "Qué clase de lugar quieres visitar",
                "playa", "montana", "naturaleza"),
        categoria("Aventura", "Qué tanta actividad física y adrenalina buscas",
                "aventura", "deportes_extremos"),
        categoria("Gastronomía", "Cómo quieres comer durante el viaje",
                "gastronomico_destacado", "oferta_gastronomica", "gourmet",
                "comida_internacional", "restricciones_alimentarias"),
        categoria("Ritmo de viaje", "Qué tan cargada y movida quieres la agenda",
                "relajacion", "tranquilo", "urbano", "vida_nocturna", "compras",
                "tiempo_libre", "muchas_actividades"),
        categoria("Cultura", "Museos, historia y tradiciones locales",
                "cultura_historia", "museos", "religioso"),
        categoria("Estilo de viaje", "El nivel de comodidad y gasto que buscas",
                "lujo", "mochilero"),
        categoria("Compañía", "Con quién viajas y qué necesitas por eso",
                "familiar", "familiar_kids", "pet_friendly"),
        categoria("Características específicas", "Preferencias puntuales del destino",
                "romantico", "nieve", "desierto", "isla_caribe", "vino",
                "ruinas_arqueologicas", "festivo", "off_the_beaten_path",
                "hispanohablante"),
        categoria("Actividades y lugares", "Lugares disponibles alrededor del destino",
                "navegacion_islas", "aguas_termales", "cascadas_rios", "buceo_snorkel",
                "teatro_musica", "parque_diversiones", "golf")
    );

    private static final Map<String, String> ETIQUETAS = Map.ofEntries(
        Map.entry("playa", "Playa"),
        Map.entry("montana", "Montaña"),
        Map.entry("naturaleza", "Naturaleza"),
        Map.entry("aventura", "Aventura"),
        Map.entry("deportes_extremos", "Deportes extremos"),
        Map.entry("gastronomico_destacado", "Gastronomía destacada"),
        Map.entry("oferta_gastronomica", "Opciones gastronómicas"),
        Map.entry("gourmet", "Alta cocina"),
        Map.entry("comida_internacional", "Comida internacional"),
        Map.entry("restricciones_alimentarias", "Restricciones alimentarias"),
        Map.entry("relajacion", "Descanso"),
        Map.entry("tranquilo", "Destino tranquilo"),
        Map.entry("urbano", "Ciudad grande"),
        Map.entry("vida_nocturna", "Vida nocturna"),
        Map.entry("compras", "Compras"),
        Map.entry("tiempo_libre", "Tiempo libre"),
        Map.entry("muchas_actividades", "Agenda cargada"),
        Map.entry("cultura_historia", "Cultura e historia"),
        Map.entry("museos", "Museos"),
        Map.entry("religioso", "Sitios religiosos"),
        Map.entry("lujo", "Lujo"),
        Map.entry("mochilero", "Mochilero"),
        Map.entry("familiar", "Viaje familiar"),
        Map.entry("familiar_kids", "Actividades para niños"),
        Map.entry("pet_friendly", "Pet-friendly"),
        Map.entry("romantico", "Romántico"),
        Map.entry("nieve", "Nieve"),
        Map.entry("desierto", "Desierto"),
        Map.entry("isla_caribe", "Isla o Caribe"),
        Map.entry("vino", "Vinos"),
        Map.entry("ruinas_arqueologicas", "Ruinas arqueológicas"),
        Map.entry("festivo", "Festivales"),
        Map.entry("off_the_beaten_path", "Menos turístico"),
        Map.entry("hispanohablante", "Hispanohablante"),
        Map.entry("navegacion_islas", "Navegación"),
        Map.entry("aguas_termales", "Aguas termales"),
        Map.entry("cascadas_rios", "Cascadas o ríos"),
        Map.entry("buceo_snorkel", "Buceo o snorkel"),
        Map.entry("teatro_musica", "Teatro o música"),
        Map.entry("parque_diversiones", "Parques de diversiones"),
        Map.entry("golf", "Golf")
    );

    public List<CategoriaPreferencia> listarCategorias() {
        return CATEGORIAS;
    }

    public int totalPreguntas() {
        return CATEGORIAS.stream().mapToInt(c -> c.getPreguntas().size()).sum();
    }

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

    public List<String> clavesAfirmativas(Preferencias preferencias) {
        return preferencias.getRespuestas().entrySet().stream()
                .filter(e -> "si".equals(e.getValue()))
                .map(Map.Entry::getKey)
                .toList();
    }

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

    public long contarAfirmativas(Preferencias preferencias) {
        return preferencias.getRespuestas().values().stream()
                .filter("si"::equals)
                .count();
    }

    private static CategoriaPreferencia categoria(String nombre, String descripcion, String... ids) {
        return new CategoriaPreferencia(nombre, "", descripcion,
                java.util.Arrays.stream(ids)
                        .map(CatalogoPreguntas::porIdObligatorio)
                        .map(PreferenciasServicio::aPreguntaVista)
                        .toList());
    }

    private static Pregunta aPreguntaVista(DefPregunta pregunta) {
        return new Pregunta(pregunta.id, pregunta.texto);
    }
}

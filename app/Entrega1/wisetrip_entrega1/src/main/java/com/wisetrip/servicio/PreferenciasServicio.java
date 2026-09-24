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
    categoria("Paisaje y clima", "Qué tipo de paisaje y clima buscas",
            "playa", "montana", "naturaleza", "nieve", "desierto"),
    categoria("Tipo de destino", "El ambiente y carácter del destino",
            "tranquilo", "urbano", "romantico", "off_the_beaten_path", "festivo"),
    categoria("Ritmo de viaje", "Qué tan cargada y movida quieres la agenda",
            "vida_nocturna", "compras", "tiempo_libre", "muchas_actividades"),
    categoria("Estilo de viaje", "El nivel de comodidad y gasto que buscas",
            "lujo", "mochilero"),
    categoria("Compañía", "Con quién viajas y qué necesitas por eso",
            "familiar_kids", "pet_friendly"),
    categoria("Gastronomía", "Cómo quieres comer durante el viaje",
            "oferta_gastronomica", "gourmet", "comida_internacional", "restricciones_alimentarias"),
    categoria("Actividades", "Actividades que te gustaría hacer durante el viaje",
            "aventura", "navegacion_islas", "parque_diversiones"),
    categoria("Cultura", "Museos, historia y tradiciones locales",
            "museos", "religioso", "ruinas_arqueologicas"),
    categoria("Características específicas", "Preferencias puntuales del destino",
            "hispanohablante")
);

private static final Map<String, String> ETIQUETAS = Map.ofEntries(
    Map.entry("playa", "Playa"),
    Map.entry("montana", "Montaña"),
    Map.entry("naturaleza", "Naturaleza"),
    Map.entry("aventura", "Aventura"),
    Map.entry("oferta_gastronomica", "Opciones gastronómicas"),
    Map.entry("gourmet", "Alta cocina"),
    Map.entry("comida_internacional", "Comida internacional"),
    Map.entry("restricciones_alimentarias", "Restricciones alimentarias"),
    Map.entry("tranquilo", "Destino tranquilo"),
    Map.entry("urbano", "Ciudad grande"),
    Map.entry("vida_nocturna", "Vida nocturna"),
    Map.entry("compras", "Compras"),
    Map.entry("tiempo_libre", "Tiempo libre"),
    Map.entry("muchas_actividades", "Agenda cargada"),
    Map.entry("museos", "Museos"),
    Map.entry("religioso", "Sitios religiosos"),
    Map.entry("lujo", "Lujo"),
    Map.entry("mochilero", "Mochilero"),
    Map.entry("familiar_kids", "Actividades para niños"),
    Map.entry("pet_friendly", "Pet-friendly"),
    Map.entry("romantico", "Romántico"),
    Map.entry("nieve", "Nieve"),
    Map.entry("desierto", "Desierto"),
    Map.entry("ruinas_arqueologicas", "Ruinas arqueológicas"),
    Map.entry("festivo", "Festivales"),
    Map.entry("off_the_beaten_path", "Menos turístico"),
    Map.entry("hispanohablante", "Hispanohablante"),
    Map.entry("navegacion_islas", "Navegación"),
    Map.entry("parque_diversiones", "Parques de diversiones")
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

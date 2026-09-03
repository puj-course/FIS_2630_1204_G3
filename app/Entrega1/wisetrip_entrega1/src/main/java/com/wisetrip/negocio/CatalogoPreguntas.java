package com.wisetrip.negocio;

import com.wisetrip.modelo.Peso;
import com.wisetrip.modelo.TipoAtributo;

import java.util.List;

public class CatalogoPreguntas {

    private static final List<DefPregunta> PREGUNTAS = List.of(
            new DefPregunta("playa", "¿Te gustan los destinos de playa?", TipoAtributo.MANUAL, null, 0, Peso.FUERTE),
            new DefPregunta("montana", "¿Prefieres destinos de montaña?", TipoAtributo.MANUAL, null, 0, Peso.FUERTE),
            new DefPregunta("naturaleza", "¿Te llama la naturaleza y áreas protegidas?", TipoAtributo.AUTO, "natural,national_park,leisure.park", 5, Peso.GUSTO),
            new DefPregunta("nieve", "¿Buscas nieve o destinos de invierno?", TipoAtributo.MANUAL, null, 0, Peso.EXCLUYENTE),
            new DefPregunta("aventura", "¿Te gustan las actividades de aventura?", TipoAtributo.AUTO, "sport", 3, Peso.GUSTO),
            new DefPregunta("deportes_extremos", "¿Buscas deportes extremos?", TipoAtributo.MANUAL, null, 0, Peso.FUERTE),
            new DefPregunta("gourmet", "¿Te interesa la gastronomía gourmet?", TipoAtributo.MANUAL, null, 0, Peso.GUSTO),
            new DefPregunta("comida_tipica", "¿Quieres probar comida típica local?", TipoAtributo.MANUAL, null, 0, Peso.GUSTO),
            new DefPregunta("vida_nocturna", "¿Te interesa la vida nocturna?", TipoAtributo.MANUAL, null, 0, Peso.GUSTO),
            new DefPregunta("urbano", "¿Prefieres ciudades grandes y urbanas?", TipoAtributo.MANUAL, null, 0, Peso.GUSTO),
            new DefPregunta("tranquilo", "¿Buscas un destino tranquilo?", TipoAtributo.MANUAL, null, 0, Peso.GUSTO),
            new DefPregunta("compras", "¿Te gusta ir de compras?", TipoAtributo.AUTO, "commercial", 10, Peso.GUSTO),
            new DefPregunta("cultura_historia", "¿Te interesa la cultura e historia?", TipoAtributo.AUTO, "tourism", 5, Peso.GUSTO),
            new DefPregunta("museos", "¿Te gusta visitar museos?", TipoAtributo.AUTO, "entertainment.museum", 3, Peso.GUSTO),
            new DefPregunta("sitios_religiosos", "¿Te interesan sitios religiosos?", TipoAtributo.AUTO, "religion", 3, Peso.GUSTO),
            new DefPregunta("lujo", "¿Buscas un destino de lujo?", TipoAtributo.MANUAL, null, 0, Peso.FUERTE),
            new DefPregunta("mochilero", "¿Viajas con presupuesto de mochilero?", TipoAtributo.MANUAL, null, 0, Peso.EXCLUYENTE),
            new DefPregunta("clima_calido", "¿Prefieres clima cálido?", TipoAtributo.MANUAL, null, 0, Peso.FUERTE),
            new DefPregunta("clima_frio", "¿Prefieres clima frío?", TipoAtributo.MANUAL, null, 0, Peso.FUERTE)
    );

    public static List<DefPregunta> todas() {
        return PREGUNTAS;
    }

    public static List<DefPregunta> atributosAuto() {
        return PREGUNTAS.stream().filter(p -> p.tipo == TipoAtributo.AUTO).toList();
    }

    public static DefPregunta porIdObligatorio(String id) {
        return PREGUNTAS.stream()
                .filter(p -> p.id.equals(id))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No existe la pregunta: " + id));
    }
}

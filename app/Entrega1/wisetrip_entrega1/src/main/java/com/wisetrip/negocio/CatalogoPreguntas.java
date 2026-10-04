//esto toca definirlo bien porque hay unos atributos y unas ciudades que deberian tener mas peso 
//que otras, ademas que no todos los atributos que tenemos puestas coinciden con una de las preguntas

//Catalogo de preguntas del cuestionario de preferencias


package com.wisetrip.negocio;

import com.wisetrip.modelo.TipoAtributo;

import java.util.Arrays;
import java.util.List;

// HU-71 (#239): se depuraron 12 preguntas redundantes
// en el caso de  "aventura", esta fusiono con las categorias de las extintas "deportes_extremos" y
// "buceo_snorkel" 
public final class CatalogoPreguntas {

    private CatalogoPreguntas() {
    }

    public static final List<DefPregunta> TODAS = List.of(
            new DefPregunta("playa", "¿Te gustaría que tu viaje incluya playa?", TipoAtributo.AUTO, "beach", 5),
            new DefPregunta("montana", "¿Te gustaría visitar zonas de montaña?", TipoAtributo.MANUAL, "", 0),
            new DefPregunta("naturaleza", "¿Te interesa estar en contacto con la naturaleza (parques, reservas, bosques)?", TipoAtributo.AUTO, "national_park,leisure.park,natural.forest", 12),
            new DefPregunta("aventura", "¿Te gustaría hacer actividades de aventura (senderismo, rafting, etc.)?", TipoAtributo.AUTO, "leisure.park.nature_reserve,sport.sports_centre,entertainment.activity_park", 8),
            new DefPregunta("deportes_extremos", "¿Te interesan actividades como parapente, buceo o escalada?", TipoAtributo.AUTO, "sport.dive_centre,sport.climbing,activity.sport_club", 3),
            new DefPregunta("gastronomico_destacado", "¿Te interesa un destino especialmente reconocido por su gastronomía?", TipoAtributo.MANUAL, "", 0),
            new DefPregunta("oferta_gastronomica", "¿Es importante para ti tener muchas opciones gastronómicas?", TipoAtributo.AUTO, "catering.restaurant", 40),
            new DefPregunta("gourmet", "¿Te interesan los restaurantes de alta cocina o gourmet?", TipoAtributo.AUTO, "catering.restaurant.fine_dining", 3),
            new DefPregunta("comida_internacional", "¿Prefieres tener acceso a comida internacional además de la local?", TipoAtributo.AUTO, "catering.restaurant.international,catering.restaurant.italian", 10),
            new DefPregunta("restricciones_alimentarias", "¿Tienes restricciones alimentarias como vegetariano, vegano o sin gluten?", TipoAtributo.PERFIL, "", 0),
            new DefPregunta("relajacion", "¿Buscas principalmente descansar y desconectarte?", TipoAtributo.MANUAL, "", 0),
            new DefPregunta("tranquilo", "¿Prefieres un destino tranquilo y poco agitado?", TipoAtributo.MANUAL, "", 0),
            new DefPregunta("urbano", "¿Prefieres una ciudad grande y con mucho movimiento?", TipoAtributo.MANUAL, "", 0),
            new DefPregunta("vida_nocturna", "¿Te interesa la vida nocturna (bares, fiestas o discotecas)?", TipoAtributo.AUTO, "catering.bar,catering.pub,adult.nightclub", 20),
            new DefPregunta("compras", "¿Te interesa hacer compras durante tu viaje?", TipoAtributo.AUTO, "commercial.shopping_mall,commercial.marketplace", 8),
            new DefPregunta("tiempo_libre", "¿Prefieres tener tiempo libre para improvisar durante el viaje?", TipoAtributo.PERFIL, "", 0),
            new DefPregunta("muchas_actividades", "¿Prefieres un viaje con muchas actividades por día?", TipoAtributo.PERFIL, "", 0),
            new DefPregunta("cultura_historia", "¿Te interesa conocer la cultura, historia y patrimonio del destino?", TipoAtributo.MANUAL, "", 0),
            new DefPregunta("museos", "¿Te gustaría visitar museos o galerías de arte?", TipoAtributo.AUTO, "entertainment.museum,entertainment.culture.gallery", 5),
            new DefPregunta("religioso", "¿Te interesa visitar sitios religiosos o espirituales?", TipoAtributo.AUTO, "building.place_of_worship", 10),
            new DefPregunta("lujo", "¿Buscas una experiencia de lujo?", TipoAtributo.MANUAL, "", 0),
            new DefPregunta("mochilero", "¿Prefieres un viaje económico tipo mochilero?", TipoAtributo.AUTO, "accommodation.hostel", 4),
            new DefPregunta("familiar", "¿Quieres un destino especialmente recomendado para viajar en familia?", TipoAtributo.MANUAL, "", 0),
            new DefPregunta("familiar_kids", "¿Viajas con niños y necesitas actividades aptas para ellos?", TipoAtributo.AUTO, "entertainment.zoo,entertainment.aquarium,leisure.playground,entertainment.theme_park", 6),
            new DefPregunta("pet_friendly", "¿Viajas con mascotas y necesitas lugares pet-friendly?", TipoAtributo.AUTO, "leisure.dog_park", 3),
            new DefPregunta("romantico", "¿Te gustaría viajar a un destino romántico?", TipoAtributo.MANUAL, "", 0),
            new DefPregunta("nieve", "¿Te gustaría un destino de nieve?", TipoAtributo.MANUAL, "", 0),
            new DefPregunta("desierto", "¿Te gustaría visitar un destino desértico?", TipoAtributo.MANUAL, "", 0),
            new DefPregunta("isla_caribe", "¿Quieres un destino de islas o ambiente caribeño?", TipoAtributo.MANUAL, "", 0),
            new DefPregunta("vino", "¿Te interesa un destino famoso por vinos o viñedos?", TipoAtributo.MANUAL, "", 0),
            new DefPregunta("ruinas_arqueologicas", "¿Te interesan ruinas o sitios arqueológicos?", TipoAtributo.MANUAL, "", 0),
            new DefPregunta("festivo", "¿Buscas un destino con carnaval o festivales famosos?", TipoAtributo.MANUAL, "", 0),
            new DefPregunta("off_the_beaten_path", "¿Prefieres un destino menos turístico o fuera de la ruta tradicional?", TipoAtributo.MANUAL, "", 0),
            new DefPregunta("hispanohablante", "¿Te importa que se hable español en el destino?", TipoAtributo.MANUAL, "", 0),
            new DefPregunta("navegacion_islas", "¿Te gustaría realizar paseos en barco o actividades de navegación?", TipoAtributo.AUTO, "leisure.marina", 3),
            new DefPregunta("aguas_termales", "¿Te interesan aguas termales o balnearios?", TipoAtributo.AUTO, "leisure.spa.public_bath", 2),
            new DefPregunta("cascadas_rios", "¿Te gustaría visitar cascadas o ríos?", TipoAtributo.AUTO, "natural.water.waterfall,natural.water.spring", 3),
            new DefPregunta("buceo_snorkel", "¿Te gustaría hacer buceo o snorkel?", TipoAtributo.AUTO, "sport.dive_centre", 2),
            new DefPregunta("teatro_musica", "¿Buscas un destino con teatros o música en vivo?", TipoAtributo.AUTO, "entertainment.culture.theatre,entertainment.culture", 4),
            new DefPregunta("parque_diversiones", "¿Te interesa visitar parques de diversiones o acuáticos?", TipoAtributo.AUTO, "entertainment.theme_park,entertainment.water_park", 1),
            new DefPregunta("golf", "¿Te interesa jugar golf durante el viaje?", TipoAtributo.AUTO, "sport.golf", 1)
    );

    public static List<DefPregunta> todas() {
        return TODAS;
    }

    public static List<String> categoriasDe(DefPregunta pregunta) {
        if (pregunta.categorias == null || pregunta.categorias.isBlank()) {
            return List.of();
        }
        return Arrays.asList(pregunta.categorias.split(","));
    }

    public static DefPregunta porId(String id) {
        return TODAS.stream()
                .filter(pregunta -> pregunta.id.equals(id))
                .findFirst()
                .orElse(null);
    }

    public static DefPregunta porIdObligatorio(String id) {
        DefPregunta pregunta = porId(id);
        if (pregunta == null) {
            throw new IllegalArgumentException("No existe el atributo/pregunta: " + id);
        }
        return pregunta;
    }

    public static List<DefPregunta> atributosAuto() {
        return TODAS.stream()
                .filter(pregunta -> pregunta.tipo == TipoAtributo.AUTO)
                .toList();
    }

}

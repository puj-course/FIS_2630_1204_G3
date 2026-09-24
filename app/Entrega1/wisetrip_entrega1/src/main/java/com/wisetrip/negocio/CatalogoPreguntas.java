//esto toca definirlo bien porque hay unos atributos y unas ciudades que deberian tener mas peso 
//que otras, ademas que no todos los atributos que tenemos puestas coinciden con una de las preguntas

//Catalogo de preguntas del cuestionario de preferencias

package com.wisetrip.negocio;

import com.wisetrip.modelo.Peso;
import com.wisetrip.modelo.TipoAtributo;

import java.util.Arrays;
import java.util.List;

public final class CatalogoPreguntas {

    private CatalogoPreguntas() {
    }

        public static final List<DefPregunta> TODAS = List.of(
                new DefPregunta("playa","¿Te gustaría que tu viaje incluya playa?", TipoAtributo.AUTO, "beach", 5, Peso.EXCLUYENTE),
                new DefPregunta("montana","¿Te gustaría visitar zonas montañosas?", TipoAtributo.MANUAL, "", 0, Peso.FUERTE),
                new DefPregunta("naturaleza","¿Te interesa estar en contacto con la naturaleza (rios, reservas, bosques)?", TipoAtributo.AUTO, "national_park,leisure.park,natural.forest,natural.water.waterfall,natural.water.spring", 12, Peso.GUSTO),
                new DefPregunta("aventura","¿Te gustaría hacer actividades de aventura (senderismo, rafting, buceo etc.)?", TipoAtributo.AUTO, "leisure.park.nature_reserve,sport.sports_centre,entertainment.activity_park,sport.dive_centre,sport.climbing,activity.sport_club", 8, Peso.GUSTO),
                new DefPregunta("oferta_gastronomica","¿Es importante para ti tener muchas opciones gastronómicas?", TipoAtributo.AUTO, "catering.restaurant", 40, Peso.GUSTO),
                new DefPregunta("gourmet","¿Te interesan los restaurantes de alta cocina tipo gourmet?", TipoAtributo.AUTO, "catering.restaurant.fine_dining", 3, Peso.GUSTO),
                new DefPregunta("comida_internacional","¿Prefieres tener acceso a comida internacional además de la local?", TipoAtributo.AUTO, "catering.restaurant.international,catering.restaurant.italian", 10, Peso.GUSTO),
                new DefPregunta("restricciones_alimentarias","¿Tienes restricciones alimentarias como vegetariano, vegano o sin gluten?", TipoAtributo.PERFIL, "", 0, Peso.GUSTO),
                new DefPregunta("tranquilo","¿Prefieres un destino tranquilo, poco agitado?", TipoAtributo.MANUAL, "", 0, Peso.FUERTE),
                new DefPregunta("urbano","¿Prefieres una ciudad grande y con mucho movimiento?", TipoAtributo.MANUAL, "", 0, Peso.FUERTE),
                new DefPregunta("vida_nocturna","¿Te interesa la vida nocturna (bares, fiestas o discotecas)?", TipoAtributo.AUTO, "catering.bar,catering.pub,adult.nightclub", 20, Peso.FUERTE),
                new DefPregunta("compras","¿Te interesa hacer compras durante tu viaje?", TipoAtributo.AUTO, "commercial.shopping_mall,commercial.marketplace", 8, Peso.GUSTO),
                new DefPregunta("tiempo_libre","¿Prefieres tener tiempo libre para improvisar durante el viaje?", TipoAtributo.PERFIL, "", 0, Peso.GUSTO),
                new DefPregunta("muchas_actividades","¿Prefieres un viaje con muchas actividades por día?", TipoAtributo.PERFIL, "", 0, Peso.GUSTO),
                new DefPregunta("museos","¿Te gustaría visitar museos o galerías de arte?", TipoAtributo.AUTO, "entertainment.museum,entertainment.culture.gallery", 5, Peso.GUSTO),
                new DefPregunta("religioso","¿Te interesa visitar sitios religiosos o espirituales?", TipoAtributo.AUTO, "building.place_of_worship", 10, Peso.GUSTO),
                new DefPregunta("lujo","¿Buscas una experiencia de lujo?", TipoAtributo.MANUAL, "", 0, Peso.FUERTE),
                new DefPregunta("mochilero","¿Prefieres un viaje económico tipo mochilero?", TipoAtributo.AUTO, "accommodation.hostel", 4, Peso.FUERTE),
                new DefPregunta("familiar_kids","¿Viajas con niños y necesitas actividades aptas para ellos?", TipoAtributo.AUTO, "entertainment.zoo,entertainment.aquarium,leisure.playground,entertainment.theme_park", 6, Peso.FUERTE),
                new DefPregunta("pet_friendly","¿Viajas con mascotas y necesitas lugares pet-friendly?", TipoAtributo.AUTO, "leisure.dog_park", 3, Peso.GUSTO),
                new DefPregunta("romantico","¿Te gustaría viajar a un destino romántico?", TipoAtributo.MANUAL, "", 0, Peso.FUERTE),
                new DefPregunta("nieve","¿Te gustaría un destino de nieve?", TipoAtributo.MANUAL, "", 0, Peso.EXCLUYENTE),
                new DefPregunta("desierto","¿Te gustaría visitar un destino desértico?", TipoAtributo.MANUAL, "", 0, Peso.EXCLUYENTE),
                new DefPregunta("ruinas_arqueologicas","¿Te interesa visitar ruinas o sitios arqueológicos?", TipoAtributo.MANUAL, "", 0, Peso.FUERTE),
                new DefPregunta("festivo","¿Buscas un destino con carnavales y festivales famosos?", TipoAtributo.MANUAL, "", 0, Peso.GUSTO),
                new DefPregunta("off_the_beaten_path","¿Prefieres un destino menos turístico?", TipoAtributo.MANUAL, "", 0, Peso.GUSTO),
                new DefPregunta("hispanohablante","¿Te importa que se hable español en el destino?", TipoAtributo.MANUAL, "", 0, Peso.GUSTO),
                new DefPregunta("navegacion_islas","¿Te gustaría realizar paseos en barco o actividades de navegación?", TipoAtributo.AUTO, "leisure.marina", 3, Peso.GUSTO),
                new DefPregunta("parque_diversiones","¿Te interesa visitar parques de diversiones o acuáticos?", TipoAtributo.AUTO, "entertainment.theme_park,entertainment.water_park", 1, Peso.GUSTO)    
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

    public static boolean esExcluyente(String id) {
        DefPregunta pregunta = porId(id);
        return pregunta != null && pregunta.peso == Peso.EXCLUYENTE;
    }
}

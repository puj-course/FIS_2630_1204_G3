import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class CatalogoPreguntas {

    //me demore mucho gente y esta re largo el codigo.

    private CatalogoPreguntas() {
    }

    /*
     * ==========================================================
     * CLIMA
     * ==========================================================
     *
     * PENDIENTE PARA ENTREGA 2.
     *
     * con la api que investigo clavix
     * nieve permanece por Bariloche y Chillan.
     * ==========================================================
     */

    public static final List<DefPregunta> TODAS = List.of(

            // =====================================================
            // A. TIPO DE DESTINO
            // =====================================================

            new DefPregunta(
                    "playa",
                    "¿Te gustaría que tu viaje incluya playa?",
                    TipoAtributo.AUTO,
                    "beach",
                    5,
                    Peso.EXCLUYENTE
            ),

            new DefPregunta(
                    "montana",
                    "¿Te gustaría visitar zonas de montaña?",
                    TipoAtributo.MANUAL,
                    "",
                    0,
                    Peso.FUERTE
            ),

            new DefPregunta(
                    "naturaleza",
                    "¿Te interesa estar en contacto con la naturaleza (parques, reservas, bosques)?",
                    TipoAtributo.AUTO,
                    "national_park,leisure.park,natural.forest",
                    12,
                    Peso.GUSTO
            ),


            // B. AVENTURA

            new DefPregunta(
                    "aventura",
                    "¿Te gustaría hacer actividades de aventura (senderismo, rafting, etc.)?",
                    TipoAtributo.AUTO,
                    "leisure.park.nature_reserve,sport.sports_centre,entertainment.activity_park",
                    8,
                    Peso.GUSTO
            ),

            new DefPregunta(
                    "deportes_extremos",
                    "¿Te interesan actividades como parapente, buceo o escalada?",
                    TipoAtributo.AUTO,
                    "sport.dive_centre,sport.climbing,activity.sport_club",
                    3,
                    Peso.GUSTO
            ),



            // C. GASTRONOMiA


            new DefPregunta(
                    "gastronomico_destacado",
                    "¿Te interesa un destino especialmente reconocido por su gastronomía?",
                    TipoAtributo.MANUAL,
                    "",
                    0,
                    Peso.FUERTE
            ),

            new DefPregunta(
                    "oferta_gastronomica",
                    "¿Es importante para ti tener muchas opciones gastronómicas?",
                    TipoAtributo.AUTO,
                    "catering.restaurant",
                    40,
                    Peso.GUSTO
            ),

            new DefPregunta(
                    "gourmet",
                    "¿Te interesan los restaurantes de alta cocina o gourmet?",
                    TipoAtributo.AUTO,
                    "catering.restaurant.fine_dining",
                    3,
                    Peso.GUSTO
            ),

            new DefPregunta(
                    "comida_internacional",
                    "¿Prefieres tener acceso a comida internacional además de la local?",
                    TipoAtributo.AUTO,
                    "catering.restaurant.international,catering.restaurant.italian",
                    10,
                    Peso.GUSTO
            ),

            new DefPregunta(
                    "restricciones_alimentarias",
                    "¿Tienes restricciones alimentarias como vegetariano, vegano o sin gluten?",
                    TipoAtributo.PERFIL,
                    "",
                    0,
                    Peso.GUSTO
            ),


            // D. RITMO DE VIAJE


            new DefPregunta(
                    "relajacion",
                    "¿Buscas principalmente descansar y desconectarte?",
                    TipoAtributo.MANUAL,
                    "",
                    0,
                    Peso.FUERTE
            ),

            new DefPregunta(
                    "tranquilo",
                    "¿Prefieres un destino tranquilo y poco agitado?",
                    TipoAtributo.MANUAL,
                    "",
                    0,
                    Peso.FUERTE
            ),

            new DefPregunta(
                    "urbano",
                    "¿Prefieres una ciudad grande y con mucho movimiento?",
                    TipoAtributo.MANUAL,
                    "",
                    0,
                    Peso.FUERTE
            ),

            new DefPregunta(
                    "vida_nocturna",
                    "¿Te interesa la vida nocturna (bares, fiestas o discotecas)?",
                    TipoAtributo.AUTO,
                    "catering.bar,catering.pub,adult.nightclub",
                    20,
                    Peso.FUERTE
            ),

            new DefPregunta(
                    "compras",
                    "¿Te interesa hacer compras durante tu viaje?",
                    TipoAtributo.AUTO,
                    "commercial.shopping_mall,commercial.marketplace",
                    8,
                    Peso.GUSTO
            ),

            new DefPregunta(
                    "tiempo_libre",
                    "¿Prefieres tener tiempo libre para improvisar durante el viaje?",
                    TipoAtributo.PERFIL,
                    "",
                    0,
                    Peso.GUSTO
            ),

            new DefPregunta(
                    "muchas_actividades",
                    "¿Prefieres un viaje con muchas actividades por día?",
                    TipoAtributo.PERFIL,
                    "",
                    0,
                    Peso.GUSTO
            ),


            // E. CULTURA
            //el mas complejo de todos


            new DefPregunta(
                    "cultura_historia",
                    "¿Te interesa conocer la cultura, historia y patrimonio del destino?",
                    TipoAtributo.MANUAL,
                    "",
                    0,
                    Peso.FUERTE
            ),

            new DefPregunta(
                    "museos",
                    "¿Te gustaría visitar museos o galerías de arte?",
                    TipoAtributo.AUTO,
                    "entertainment.museum,entertainment.culture.gallery",
                    5,
                    Peso.GUSTO
            ),

            new DefPregunta(
                    "religioso",
                    "¿Te interesa visitar sitios religiosos o espirituales?",
                    TipoAtributo.AUTO,
                    "building.place_of_worship",
                    10,
                    Peso.GUSTO
            ),


            //meh 50/50
            // F. ESTILO


            new DefPregunta(
                    "lujo",
                    "¿Buscas una experiencia de lujo?",
                    TipoAtributo.MANUAL,
                    "",
                    0,
                    Peso.FUERTE
            ),

            new DefPregunta(
                    "mochilero",
                    "¿Prefieres un viaje económico tipo mochilero?",
                    TipoAtributo.AUTO,
                    "accommodation.hostel",
                    4,
                    Peso.FUERTE
            ),


            //family friendly
            // G. COMPAÑiA


            new DefPregunta(
                    "familiar",
                    "¿Quieres un destino especialmente recomendado para viajar en familia?",
                    TipoAtributo.MANUAL,
                    "",
                    0,
                    Peso.FUERTE
            ),

            new DefPregunta(
                    "familiar_kids",
                    "¿Viajas con niños y necesitas actividades aptas para ellos?",
                    TipoAtributo.AUTO,
                    "entertainment.zoo,entertainment.aquarium,leisure.playground,entertainment.theme_park",
                    6,
                    Peso.FUERTE
            ),

            new DefPregunta(
                    "pet_friendly",
                    "¿Viajas con mascotas y necesitas lugares pet-friendly?",
                    TipoAtributo.AUTO,
                    "leisure.dog_park",
                    3,
                    Peso.GUSTO
            ),


            // =====================================================
            // H. CARACTERiSTICAS ESPECiFICAS MANUALES
            =

            new DefPregunta(
                    "romantico",
                    "¿Te gustaría viajar a un destino romántico?",
                    TipoAtributo.MANUAL,
                    "",
                    0,
                    Peso.FUERTE
            ),

            new DefPregunta(
                    "nieve",
                    "¿Te gustaría un destino de nieve?",
                    TipoAtributo.MANUAL,
                    "",
                    0,
                    Peso.EXCLUYENTE
            ),

            new DefPregunta(
                    "desierto",
                    "¿Te gustaría visitar un destino desértico?",
                    TipoAtributo.MANUAL,
                    "",
                    0,
                    Peso.EXCLUYENTE
            ),

            new DefPregunta(
                    "isla_caribe",
                    "¿Quieres un destino de islas o ambiente caribeño?",
                    TipoAtributo.MANUAL,
                    "",
                    0,
                    Peso.EXCLUYENTE
            ),

            new DefPregunta(
                    "vino",
                    "¿Te interesa un destino famoso por vinos o viñedos?",
                    TipoAtributo.MANUAL,
                    "",
                    0,
                    Peso.GUSTO
            ),

            new DefPregunta(
                    "ruinas_arqueologicas",
                    "¿Te interesan ruinas o sitios arqueológicos?",
                    TipoAtributo.MANUAL,
                    "",
                    0,
                    Peso.FUERTE
            ),

            new DefPregunta(
                    "festivo",
                    "¿Buscas un destino con carnaval o festivales famosos?",
                    TipoAtributo.MANUAL,
                    "",
                    0,
                    Peso.GUSTO
            ),

            new DefPregunta(
                    "off_the_beaten_path",
                    "¿Prefieres un destino menos turístico o fuera de la ruta tradicional?",
                    TipoAtributo.MANUAL,
                    "",
                    0,
                    Peso.GUSTO
            ),

            new DefPregunta(
                    "hispanohablante",
                    "¿Te importa que se hable español en el destino?",
                    TipoAtributo.MANUAL,
                    "",
                    0,
                    Peso.GUSTO
            ),


            // =====================================================
            // I. CARACTERÍSTICAS ESPECiFICAS GEOAPIFY
            // =====================================================

            new DefPregunta(
                    "navegacion_islas",
                    "¿Te gustaría realizar paseos en barco o actividades de navegación?",
                    TipoAtributo.AUTO,
                    "leisure.marina",
                    3,
                    Peso.GUSTO
            ),

            new DefPregunta(
                    "aguas_termales",
                    "¿Te interesan aguas termales o balnearios?",
                    TipoAtributo.AUTO,
                    "leisure.spa.public_bath",
                    2,
                    Peso.GUSTO
            ),

            new DefPregunta(
                    "cascadas_rios",
                    "¿Te gustaría visitar cascadas o ríos?",
                    TipoAtributo.AUTO,
                    "natural.water.waterfall,natural.water.spring",
                    3,
                    Peso.GUSTO
            ),

            new DefPregunta(
                    "buceo_snorkel",
                    "¿Te gustaría hacer buceo o snorkel?",
                    TipoAtributo.AUTO,
                    "sport.dive_centre",
                    2,
                    Peso.GUSTO
            ),

            new DefPregunta(
                    "teatro_musica",
                    "¿Buscas un destino con teatros o música en vivo?",
                    TipoAtributo.AUTO,
                    "entertainment.culture.theatre,entertainment.culture",
                    4,
                    Peso.GUSTO
            ),

            new DefPregunta(
                    "parque_diversiones",
                    "¿Te interesa visitar parques de diversiones o acuáticos?",
                    TipoAtributo.AUTO,
                    "entertainment.theme_park,entertainment.water_park",
                    1,
                    Peso.GUSTO
            ),

            new DefPregunta(
                    "golf",
                    "¿Te interesa jugar golf durante el viaje?",
                    TipoAtributo.AUTO,
                    "sport.golf",
                    1,
                    Peso.GUSTO
            )
    );


    public static List<String> categoriasDe(DefPregunta pregunta) {

        if (
                pregunta.categorias == null
                        || pregunta.categorias.isBlank()
        ) {
            return List.of();
        }

        return Arrays.asList(
                pregunta.categorias.split(",")
        );
    }


    public static DefPregunta porId(String id) {

        for (DefPregunta pregunta : TODAS) {

            if (pregunta.id.equals(id)) {
                return pregunta;
            }
        }

        return null;
    }


    public static DefPregunta porIdObligatorio(String id) {

        DefPregunta pregunta = porId(id);

        if (pregunta == null) {

            throw new IllegalArgumentException(
                    "No existe el atributo/pregunta: "
                            + id
            );
        }

        return pregunta;
    }


    public static List<DefPregunta> atributosAuto() {

        List<DefPregunta> resultado =
                new ArrayList<>();

        for (DefPregunta pregunta : TODAS) {

            if (pregunta.tipo == TipoAtributo.AUTO) {
                resultado.add(pregunta);
            }
        }

        return resultado;
    }


    public static boolean esExcluyente(String id) {

        DefPregunta pregunta = porId(id);

        return pregunta != null
                && pregunta.peso == Peso.EXCLUYENTE;
    }
}
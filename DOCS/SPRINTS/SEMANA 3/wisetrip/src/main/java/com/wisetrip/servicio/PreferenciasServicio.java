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
     * Banco de preguntas, tomado tal cual del documento "Preguntas de Preferencia"
     * (Gabriela Melo, Fundamentos de Ingenieria de Software).
     * Para agregar/quitar una pregunta, solo hay que editar esta lista:
     * no hace falta tocar el modelo, el controlador ni la vista.
     */
    public static final List<CategoriaPreferencia> CATEGORIAS = List.of(

            new CategoriaPreferencia("Tipo de destino", List.of(
                    new Pregunta("playa", "Te gustaria que tu viaje incluya playa?"),
                    new Pregunta("montana", "Te gustaria visitar zonas de montana?"),
                    new Pregunta("naturaleza", "Te interesa estar en contacto con la naturaleza (parques, reservas, bosques)?")
            )),

            new CategoriaPreferencia("Clima", List.of(
                    new Pregunta("climaCalido", "Prefieres un destino de clima calido?"),
                    new Pregunta("climaFrio", "Prefieres un destino de clima frio?"),
                    new Pregunta("nieve", "Te gustaria que tu destino tenga nieve?")
            )),

            new CategoriaPreferencia("Aventura", List.of(
                    new Pregunta("actividadesAventura", "Te gustaria hacer actividades de aventura (senderismo, rafting, etc.)?"),
                    new Pregunta("deportesExtremos", "Te interesan los deportes extremos (parapente, buceo, escalada)?")
            )),

            new CategoriaPreferencia("Gastronomia", List.of(
                    new Pregunta("comidaTipica", "Es importante para ti probar la comida tipica del destino?"),
                    new Pregunta("altaCocina", "Te interesan los restaurantes de alta cocina o gourmet?"),
                    new Pregunta("restriccionesAlimentarias", "Tienes restricciones alimentarias (vegetariano, vegano, sin gluten, alergias)?"),
                    new Pregunta("comidaInternacional", "Prefieres tener acceso a comida internacional/conocida ademas de la local?")
            )),

            new CategoriaPreferencia("Ritmo de viaje", List.of(
                    new Pregunta("descansoRelajacion", "Buscas un viaje principalmente de descanso y relajacion?"),
                    new Pregunta("destinosTranquilos", "Prefieres destinos tranquilos, alejados del bullicio?"),
                    new Pregunta("vidaNocturna", "Te interesa la vida nocturna (bares, fiestas, discotecas)?"),
                    new Pregunta("destinosUrbanos", "Prefieres destinos urbanos/ciudades grandes?"),
                    new Pregunta("comprasEnViaje", "Te interesa hacer compras durante tu viaje (mercados, centros comerciales)?"),
                    new Pregunta("tiempoLibreImprovisar", "Prefieres tener tiempo libre para improvisar durante el viaje?"),
                    new Pregunta("muchasActividadesPorDia", "Prefieres un viaje con muchas actividades por dia?")
            )),

            new CategoriaPreferencia("Cultura", List.of(
                    new Pregunta("culturaLocal", "Te interesa conocer la cultura local del destino?"),
                    new Pregunta("museos", "Te gustaria visitar museos o galerias de arte?"),
                    new Pregunta("sitiosReligiosos", "Te interesa visitar sitios religiosos o espirituales?")
            )),

            new CategoriaPreferencia("Estilo de viaje", List.of(
                    new Pregunta("lujo", "Buscas experiencias de lujo (hoteles premium, servicios exclusivos)?"),
                    new Pregunta("mochilero", "Prefieres un viaje economico tipo mochilero?")
            )),

            new CategoriaPreferencia("Compania", List.of(
                    new Pregunta("viajaConNinos", "Viajas con ninos y necesitas actividades aptas para ellos?"),
                    new Pregunta("viajaConMascotas", "Viajas con mascotas y necesitas lugares pet-friendly?")
            ))
    );

    /**
     * Valida que TODAS las preguntas del banco tengan una respuesta.
     * Devuelve un mapa vacio si todo esta bien, o clave de pregunta -> mensaje de error.
     */
    public Map<String, String> validarPreferencias(Preferencias preferencias) {
        Map<String, String> errores = new LinkedHashMap<>();
        Map<String, String> respuestas = preferencias.getRespuestas();

        for (CategoriaPreferencia categoria : CATEGORIAS) {
            for (Pregunta pregunta : categoria.getPreguntas()) {
                String valor = respuestas.get(pregunta.getClave());
                if (valor == null || valor.isBlank()) {
                    errores.put(pregunta.getClave(), "Responde esta pregunta para continuar.");
                }
            }
        }

        return errores;
    }

    /**
     * Convierte las respuestas Si/No en un mapa clave -> boolean,
     * listo para que el algoritmo de recomendacion lo consuma directamente.
     */
    public Map<String, Boolean> obtenerAtributosSeleccionados(Preferencias preferencias) {
        Map<String, Boolean> resultado = new LinkedHashMap<>();
        for (Map.Entry<String, String> entrada : preferencias.getRespuestas().entrySet()) {
            resultado.put(entrada.getKey(), "si".equalsIgnoreCase(entrada.getValue()));
        }
        return resultado;
    }
}

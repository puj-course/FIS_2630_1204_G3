//DatosCiudades: maneja costoPromedio y el filtro de presupuesto
//pero toca corregirlo
// SEMILLAS - SOLO atributos MANUALES.
// Los AUTO (playa, naturaleza, museos, vida_nocturna, mochilero,
// compras, buceo_snorkel, navegacion_islas, etc.) los añade
// PobladorCiudades contando resultados de Geoapify.

package com.wisetrip.datos;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import com.wisetrip.negocio.CatalogoPreguntas;

public class DatosCiudades {

    // Atributos de las ciudades semilla
    private static final String URBANO = "urbano";
    private static final String CULTURA_HISTORIA = "cultura_historia";
    private static final String GASTRONOMICO_DESTACADO = "gastronomico_destacado";
    private static final String HISPANOHABLANTE = "hispanohablante";
    private static final String TRANQUILO = "tranquilo";
    private static final String ROMANTICO = "romantico";
    private static final String RELAJACION = "relajacion";
    private static final String OFF_THE_BEATEN_PATH = "off_the_beaten_path";
    private static final String MONTANA = "montana";
    private static final String FAMILIAR = "familiar";
    private static final String ISLA_CARIBE = "isla_caribe";
    private static final String RUINAS_ARQUEOLOGICAS = "ruinas_arqueologicas";
    private static final String LUJO = "lujo";
    private static final String FESTIVO = "festivo";

    // Paises que se repiten
    private static final String COLOMBIA = "Colombia";
    private static final String CHILE = "Chile";
    private static final String BRASIL = "Brasil";
    private static final String ARGENTINA = "Argentina";
    private static final String VENEZUELA = "Venezuela";
    private static final String MEXICO = "M\u00e9xico";

    public static final List<CiudadSemilla> CIUDADES = List.of(
            new CiudadSemilla("Ciudad de México", MEXICO, 19.43, -99.13, attrs(URBANO, CULTURA_HISTORIA, GASTRONOMICO_DESTACADO, HISPANOHABLANTE)),
            new CiudadSemilla("Cancún", MEXICO, 21.16, -86.85, attrs(LUJO, FAMILIAR, ISLA_CARIBE, ROMANTICO)),
            new CiudadSemilla("Guadalajara", MEXICO, 20.66, -103.34, attrs(URBANO, CULTURA_HISTORIA, GASTRONOMICO_DESTACADO, TRANQUILO, HISPANOHABLANTE)),
            new CiudadSemilla("Ciudad de Guatemala", "Guatemala", 14.63, -90.51, attrs(URBANO, CULTURA_HISTORIA, HISPANOHABLANTE)),
            new CiudadSemilla("Flores", "Guatemala", 16.91, -89.89, attrs(RUINAS_ARQUEOLOGICAS, TRANQUILO, OFF_THE_BEATEN_PATH, HISPANOHABLANTE)),
            new CiudadSemilla("Tegucigalpa", "Honduras", 14.07, -87.22, attrs(URBANO, CULTURA_HISTORIA, HISPANOHABLANTE)),
            new CiudadSemilla("Roatán", "Honduras", 16.34, -86.52, attrs(ISLA_CARIBE, ROMANTICO, RELAJACION, HISPANOHABLANTE)),
            new CiudadSemilla("San Salvador", "El Salvador", 13.69, -89.18, attrs(URBANO, CULTURA_HISTORIA, GASTRONOMICO_DESTACADO, HISPANOHABLANTE)),
            new CiudadSemilla("Santa Ana", "El Salvador", 13.99, -89.56, attrs(CULTURA_HISTORIA, TRANQUILO, HISPANOHABLANTE)),
            new CiudadSemilla("Managua", "Nicaragua", 12.13, -86.24, attrs(URBANO, HISPANOHABLANTE)),
            new CiudadSemilla("Granada", "Nicaragua", 11.93, -85.96, attrs(CULTURA_HISTORIA, TRANQUILO, ROMANTICO, HISPANOHABLANTE)),
            new CiudadSemilla("San José", "Costa Rica", 9.93, -84.08, attrs(URBANO, CULTURA_HISTORIA, TRANQUILO, HISPANOHABLANTE)),
            new CiudadSemilla("La Fortuna", "Costa Rica", 10.46, -84.64, attrs(RELAJACION, TRANQUILO, OFF_THE_BEATEN_PATH, HISPANOHABLANTE)),
            new CiudadSemilla("Ciudad de Panamá", "Panamá", 8.98, -79.53, attrs(URBANO, CULTURA_HISTORIA, HISPANOHABLANTE)),
            new CiudadSemilla("Bocas del Toro", "Panamá", 9.34, -82.24, attrs(ISLA_CARIBE, TRANQUILO, OFF_THE_BEATEN_PATH, HISPANOHABLANTE)),
            new CiudadSemilla("Belize City", "Belice", 17.50, -88.20, attrs(CULTURA_HISTORIA, OFF_THE_BEATEN_PATH)),
            new CiudadSemilla("Bogotá", COLOMBIA, 4.71, -74.07, attrs(URBANO, CULTURA_HISTORIA, GASTRONOMICO_DESTACADO, HISPANOHABLANTE)),
            new CiudadSemilla("Medellín", COLOMBIA, 6.25, -75.57, attrs(URBANO, CULTURA_HISTORIA, FAMILIAR, GASTRONOMICO_DESTACADO, ROMANTICO, HISPANOHABLANTE)),
            new CiudadSemilla("Cartagena de Indias", COLOMBIA, 10.42, -75.54, attrs(ROMANTICO, FAMILIAR, GASTRONOMICO_DESTACADO, CULTURA_HISTORIA, ISLA_CARIBE, HISPANOHABLANTE)),
            new CiudadSemilla("Caracas", VENEZUELA, 10.49, -66.88, attrs(URBANO, CULTURA_HISTORIA, HISPANOHABLANTE)),
            new CiudadSemilla("Porlamar / Isla de Margarita", VENEZUELA, 10.96, -63.91, attrs(ISLA_CARIBE, RELAJACION, TRANQUILO, HISPANOHABLANTE)),
            new CiudadSemilla("La Habana", "Cuba", 23.13, -82.38, attrs(CULTURA_HISTORIA, ROMANTICO, FESTIVO)),
            new CiudadSemilla("Santiago de Cuba", "Cuba", 20.02, -76.83, attrs(CULTURA_HISTORIA, FESTIVO, TRANQUILO)),
            new CiudadSemilla("Santo Domingo", "República Dominicana", 18.47, -69.93, attrs(URBANO, CULTURA_HISTORIA, HISPANOHABLANTE)),
            new CiudadSemilla("Punta Cana", "República Dominicana", 18.46, -68.93, attrs(LUJO, FAMILIAR, RELAJACION, ISLA_CARIBE)),
            new CiudadSemilla("Quito", "Ecuador", -0.18, -78.47, attrs(CULTURA_HISTORIA, MONTANA, HISPANOHABLANTE)),
            new CiudadSemilla("Guayaquil", "Ecuador", -2.17, -79.88, attrs(URBANO, GASTRONOMICO_DESTACADO, HISPANOHABLANTE)),
            new CiudadSemilla("Lima", "Perú", -12.05, -77.04, attrs(URBANO, GASTRONOMICO_DESTACADO, CULTURA_HISTORIA, HISPANOHABLANTE)),
            new CiudadSemilla("Cusco", "Perú", -13.53, -71.97, attrs(CULTURA_HISTORIA, RUINAS_ARQUEOLOGICAS, MONTANA, OFF_THE_BEATEN_PATH, HISPANOHABLANTE)),
            new CiudadSemilla("La Paz", "Bolivia", -16.50, -68.13, attrs(MONTANA, CULTURA_HISTORIA, OFF_THE_BEATEN_PATH, HISPANOHABLANTE)),
            new CiudadSemilla("Santa Cruz de la Sierra", "Bolivia", -17.78, -63.18, attrs(URBANO, GASTRONOMICO_DESTACADO, TRANQUILO, HISPANOHABLANTE)),
            new CiudadSemilla("Río de Janeiro", BRASIL, -22.91, -43.17, attrs(URBANO, FESTIVO, ROMANTICO, FAMILIAR)),
            new CiudadSemilla("São Paulo", BRASIL, -23.55, -46.63, attrs(URBANO, CULTURA_HISTORIA, GASTRONOMICO_DESTACADO)),
            new CiudadSemilla("Brasilia", BRASIL, -15.80, -47.88, attrs(URBANO, CULTURA_HISTORIA, TRANQUILO)),
            new CiudadSemilla("Santiago de Chile", CHILE, -33.45, -70.65, attrs(URBANO, MONTANA, "vino", CULTURA_HISTORIA, GASTRONOMICO_DESTACADO, HISPANOHABLANTE)),
            new CiudadSemilla("San Pedro de Atacama", CHILE, -22.91, -68.20, attrs("desierto", OFF_THE_BEATEN_PATH, RUINAS_ARQUEOLOGICAS, TRANQUILO, HISPANOHABLANTE)),
            new CiudadSemilla("Chillán / Nevados de Chillán", CHILE, -36.61, -72.10, attrs("nieve", MONTANA, RELAJACION, FAMILIAR, HISPANOHABLANTE)),
            new CiudadSemilla("Buenos Aires", ARGENTINA, -34.60, -58.38, attrs(URBANO, CULTURA_HISTORIA, ROMANTICO, GASTRONOMICO_DESTACADO, HISPANOHABLANTE)),
            new CiudadSemilla("Mendoza", ARGENTINA, -32.89, -68.85, attrs("vino", MONTANA, RELAJACION, HISPANOHABLANTE)),
            new CiudadSemilla("San Carlos de Bariloche", ARGENTINA, -41.13, -71.31, attrs("nieve", MONTANA, FAMILIAR, RELAJACION, HISPANOHABLANTE)),
            new CiudadSemilla("Montevideo", "Uruguay", -34.90, -56.18, attrs(URBANO, TRANQUILO, CULTURA_HISTORIA, GASTRONOMICO_DESTACADO, HISPANOHABLANTE)),
            new CiudadSemilla("Punta del Este", "Uruguay", -34.95, -54.95, attrs(LUJO, ROMANTICO, RELAJACION, HISPANOHABLANTE)),
            new CiudadSemilla("Asunción", "Paraguay", -25.26, -57.58, attrs(URBANO, TRANQUILO, CULTURA_HISTORIA, HISPANOHABLANTE)),
            new CiudadSemilla("Ciudad del Este", "Paraguay", -25.51, -54.61, attrs(URBANO, HISPANOHABLANTE))
    );

    public static List<CiudadSemilla> ciudades() {
        return CIUDADES;
    }

    public static CiudadSemilla porNombreYPais(String nombre, String pais) {
        String nombreBusqueda = normalizarAlias(nombre, pais);
        return CIUDADES.stream()
                .filter(c -> c.nombre().equalsIgnoreCase(nombreBusqueda) && c.pais().equalsIgnoreCase(pais))
                .findFirst()
                .orElse(null);
    }

    private static String normalizarAlias(String nombre, String pais) {
        if (COLOMBIA.equalsIgnoreCase(pais) && "Cartagena".equalsIgnoreCase(nombre)) {
            return "Cartagena de Indias";
        }
        if (ARGENTINA.equalsIgnoreCase(pais) && "Bariloche".equalsIgnoreCase(nombre)) {
            return "San Carlos de Bariloche";
        }
        if (VENEZUELA.equalsIgnoreCase(pais) && "Porlamar".equalsIgnoreCase(nombre)) {
            return "Porlamar / Isla de Margarita";
        }
        if (CHILE.equalsIgnoreCase(pais) && "Chillán".equalsIgnoreCase(nombre)) {
            return "Chillán / Nevados de Chillán";
        }
        if (BRASIL.equalsIgnoreCase(pais) && "Brasília".equalsIgnoreCase(nombre)) {
            return "Brasilia";
        }
        return nombre;
    }

    private static Map<String, Boolean> attrs(String... atributos) {
        // La semilla enumera los rasgos manuales que cumple el destino.
        // Los demas manuales no cumplen; los AUTO siguen pendientes de Geoapify.
        var presentes = java.util.Set.copyOf(Arrays.asList(atributos));
        return CatalogoPreguntas.todasLasPreguntas().stream()
                .filter(pregunta -> pregunta.esManual())
                .collect(java.util.stream.Collectors.toUnmodifiableMap(
                        pregunta -> pregunta.id, pregunta -> presentes.contains(pregunta.id)));
    }
}

package com.wisetrip.datos;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class DatosCiudades {

    public static final List<CiudadSemilla> CIUDADES = List.of(
            new CiudadSemilla("Ciudad de México", "México", "medio", 19.43, -99.13, attrs("urbano", "cultura_historia", "museos", "comida_tipica", "vida_nocturna", "compras")),
            new CiudadSemilla("Cancún", "México", "alto", 21.16, -86.85, attrs("playa", "clima_calido")),
            new CiudadSemilla("Guadalajara", "México", "medio", 20.66, -103.34, attrs("urbano", "cultura_historia")),
            new CiudadSemilla("Ciudad de Guatemala", "Guatemala", "bajo", 14.63, -90.51, attrs()),
            new CiudadSemilla("Flores", "Guatemala", "bajo", 16.91, -89.89, attrs("naturaleza")),
            new CiudadSemilla("Tegucigalpa", "Honduras", "bajo", 14.07, -87.22, attrs()),
            new CiudadSemilla("Roatán", "Honduras", "medio", 16.34, -86.52, attrs("playa", "clima_calido")),
            new CiudadSemilla("San Salvador", "El Salvador", "bajo", 13.69, -89.18, attrs()),
            new CiudadSemilla("Santa Ana", "El Salvador", "bajo", 13.99, -89.56, attrs()),
            new CiudadSemilla("Managua", "Nicaragua", "bajo", 12.13, -86.24, attrs()),
            new CiudadSemilla("Granada", "Nicaragua", "bajo", 11.93, -85.96, attrs("cultura_historia")),
            new CiudadSemilla("San José", "Costa Rica", "medio", 9.93, -84.08, attrs("naturaleza", "aventura")),
            new CiudadSemilla("La Fortuna", "Costa Rica", "medio", 10.46, -84.64, attrs("naturaleza", "aventura", "deportes_extremos")),
            new CiudadSemilla("Ciudad de Panamá", "Panamá", "medio", 8.98, -79.53, attrs("urbano")),
            new CiudadSemilla("Bocas del Toro", "Panamá", "medio", 9.34, -82.24, attrs("playa", "clima_calido")),
            new CiudadSemilla("Belize City", "Belice", "medio", 17.50, -88.20, attrs("playa")),
            new CiudadSemilla("Bogotá", "Colombia", "bajo", 4.71, -74.07, attrs("urbano", "cultura_historia", "museos", "clima_frio", "compras", "sitios_religiosos")),
            new CiudadSemilla("Medellín", "Colombia", "bajo", 6.25, -75.57, attrs("urbano", "vida_nocturna", "cultura_historia", "museos", "clima_calido", "compras")),
            new CiudadSemilla("Cartagena de Indias", "Colombia", "medio", 10.42, -75.54, attrs("playa", "cultura_historia", "vida_nocturna", "comida_tipica", "clima_calido", "museos", "romantico")),
            new CiudadSemilla("Caracas", "Venezuela", "bajo", 10.49, -66.88, attrs()),
            new CiudadSemilla("Porlamar", "Venezuela", "bajo", 10.96, -63.91, attrs("playa")),
            new CiudadSemilla("La Habana", "Cuba", "medio", 23.13, -82.38, attrs("cultura_historia", "vida_nocturna")),
            new CiudadSemilla("Santiago de Cuba", "Cuba", "bajo", 20.02, -76.83, attrs("cultura_historia")),
            new CiudadSemilla("Santo Domingo", "República Dominicana", "bajo", 18.47, -69.93, attrs("cultura_historia")),
            new CiudadSemilla("Punta Cana", "República Dominicana", "alto", 18.46, -68.93, attrs("playa", "clima_calido", "lujo")),
            new CiudadSemilla("Quito", "Ecuador", "bajo", -0.18, -78.47, attrs("cultura_historia", "montana")),
            new CiudadSemilla("Guayaquil", "Ecuador", "bajo", -2.17, -79.88, attrs("urbano")),
            new CiudadSemilla("Lima", "Perú", "medio", -12.05, -77.04, attrs("urbano", "gourmet", "cultura_historia")),
            new CiudadSemilla("Cusco", "Perú", "medio", -13.53, -71.97, attrs("montana", "cultura_historia", "naturaleza", "aventura", "clima_frio", "sitios_religiosos")),
            new CiudadSemilla("La Paz", "Bolivia", "bajo", -16.50, -68.13, attrs("montana")),
            new CiudadSemilla("Santa Cruz de la Sierra", "Bolivia", "bajo", -17.78, -63.18, attrs()),
            new CiudadSemilla("Río de Janeiro", "Brasil", "alto", -22.91, -43.17, attrs("playa", "vida_nocturna", "urbano", "clima_calido", "cultura_historia")),
            new CiudadSemilla("São Paulo", "Brasil", "medio", -23.55, -46.63, attrs("urbano", "vida_nocturna", "compras")),
            new CiudadSemilla("Brasília", "Brasil", "medio", -15.80, -47.88, attrs("urbano")),
            new CiudadSemilla("Santiago de Chile", "Chile", "medio", -33.45, -70.65, attrs("urbano", "montana")),
            new CiudadSemilla("San Pedro de Atacama", "Chile", "alto", -22.91, -68.20, attrs("naturaleza", "aventura")),
            new CiudadSemilla("Chillán", "Chile", "medio", -36.61, -72.10, attrs("montana", "nieve")),
            new CiudadSemilla("Buenos Aires", "Argentina", "medio", -34.60, -58.38, attrs("urbano", "gourmet", "vida_nocturna", "cultura_historia")),
            new CiudadSemilla("Mendoza", "Argentina", "medio", -32.89, -68.85, attrs("gourmet", "montana")),
            new CiudadSemilla("San Carlos de Bariloche", "Argentina", "alto", -41.13, -71.31, attrs("montana", "nieve", "clima_frio", "naturaleza", "deportes_extremos", "lujo")),
            new CiudadSemilla("Montevideo", "Uruguay", "medio", -34.90, -56.18, attrs("urbano")),
            new CiudadSemilla("Punta del Este", "Uruguay", "alto", -34.95, -54.95, attrs("playa", "lujo")),
            new CiudadSemilla("Asunción", "Paraguay", "bajo", -25.26, -57.58, attrs()),
            new CiudadSemilla("Ciudad del Este", "Paraguay", "bajo", -25.51, -54.61, attrs("compras"))
    );

    public static List<CiudadSemilla> ciudades() {
        return CIUDADES;
    }

    public static CiudadSemilla porNombreYPais(String nombre, String pais) {
        String nombreBusqueda = normalizarAlias(nombre, pais);
        Optional<CiudadSemilla> semilla = CIUDADES.stream()
                .filter(c -> c.nombre().equalsIgnoreCase(nombreBusqueda) && c.pais().equalsIgnoreCase(pais))
                .findFirst();
        return semilla.orElse(null);
    }

    private static String normalizarAlias(String nombre, String pais) {
        if ("Colombia".equalsIgnoreCase(pais) && "Cartagena".equalsIgnoreCase(nombre)) {
            return "Cartagena de Indias";
        }
        if ("Argentina".equalsIgnoreCase(pais) && "Bariloche".equalsIgnoreCase(nombre)) {
            return "San Carlos de Bariloche";
        }
        return nombre;
    }

    private static Map<String, Boolean> attrs(String... atributos) {
        return java.util.Arrays.stream(atributos)
                .collect(java.util.stream.Collectors.toUnmodifiableMap(a -> a, a -> Boolean.TRUE));
    }
}

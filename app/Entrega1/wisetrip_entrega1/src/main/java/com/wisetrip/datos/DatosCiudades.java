package com.wisetrip.datos;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class DatosCiudades {

    public static final List<CiudadSemilla> CIUDADES = List.of(
            new CiudadSemilla("Ciudad de México", "México", "bajo", 19.43, -99.13, attrs("urbano", "cultura_historia", "gastronomico_destacado", "hispanohablante")),
            new CiudadSemilla("Cancún", "México", "medio", 21.16, -86.85, attrs("lujo", "familiar", "isla_caribe", "romantico")),
            new CiudadSemilla("Guadalajara", "México", "bajo", 20.66, -103.34, attrs("urbano", "cultura_historia", "gastronomico_destacado", "tranquilo", "hispanohablante")),
            new CiudadSemilla("Ciudad de Guatemala", "Guatemala", "bajo", 14.63, -90.51, attrs("urbano", "cultura_historia", "hispanohablante")),
            new CiudadSemilla("Flores", "Guatemala", "bajo", 16.91, -89.89, attrs("ruinas_arqueologicas", "tranquilo", "off_the_beaten_path", "hispanohablante")),
            new CiudadSemilla("Tegucigalpa", "Honduras", "bajo", 14.07, -87.22, attrs("urbano", "cultura_historia", "hispanohablante")),
            new CiudadSemilla("Roatán", "Honduras", "medio", 16.34, -86.52, attrs("isla_caribe", "romantico", "relajacion", "hispanohablante")),
            new CiudadSemilla("San Salvador", "El Salvador", "bajo", 13.69, -89.18, attrs("urbano", "cultura_historia", "gastronomico_destacado", "hispanohablante")),
            new CiudadSemilla("Santa Ana", "El Salvador", "bajo", 13.99, -89.56, attrs("cultura_historia", "tranquilo", "hispanohablante")),
            new CiudadSemilla("Managua", "Nicaragua", "bajo", 12.13, -86.24, attrs("urbano", "hispanohablante")),
            new CiudadSemilla("Granada", "Nicaragua", "bajo", 11.93, -85.96, attrs("cultura_historia", "tranquilo", "romantico", "hispanohablante")),
            new CiudadSemilla("San José", "Costa Rica", "bajo", 9.93, -84.08, attrs("urbano", "cultura_historia", "tranquilo", "hispanohablante")),
            new CiudadSemilla("La Fortuna", "Costa Rica", "medio", 10.46, -84.64, attrs("relajacion", "tranquilo", "off_the_beaten_path", "hispanohablante")),
            new CiudadSemilla("Ciudad de Panamá", "Panamá", "bajo", 8.98, -79.53, attrs("urbano", "cultura_historia", "hispanohablante")),
            new CiudadSemilla("Bocas del Toro", "Panamá", "bajo", 9.34, -82.24, attrs("isla_caribe", "tranquilo", "off_the_beaten_path", "hispanohablante")),
            new CiudadSemilla("Belize City", "Belice", "bajo", 17.50, -88.20, attrs("cultura_historia", "off_the_beaten_path")),
            new CiudadSemilla("Bogotá", "Colombia", "bajo", 4.71, -74.07, attrs("urbano", "cultura_historia", "gastronomico_destacado", "hispanohablante")),
            new CiudadSemilla("Medellín", "Colombia", "medio", 6.25, -75.57, attrs("urbano", "cultura_historia", "familiar", "gastronomico_destacado", "romantico", "hispanohablante")),
            new CiudadSemilla("Cartagena de Indias", "Colombia", "medio", 10.42, -75.54, attrs("romantico", "familiar", "gastronomico_destacado", "cultura_historia", "isla_caribe", "hispanohablante")),
            new CiudadSemilla("Caracas", "Venezuela", "bajo", 10.49, -66.88, attrs("urbano", "cultura_historia", "hispanohablante")),
            new CiudadSemilla("Porlamar / Isla de Margarita", "Venezuela", "bajo", 10.96, -63.91, attrs("isla_caribe", "relajacion", "tranquilo", "hispanohablante")),
            new CiudadSemilla("La Habana", "Cuba", "bajo", 23.13, -82.38, attrs("cultura_historia", "romantico", "festivo")),
            new CiudadSemilla("Santiago de Cuba", "Cuba", "bajo", 20.02, -76.83, attrs("cultura_historia", "festivo", "tranquilo")),
            new CiudadSemilla("Santo Domingo", "República Dominicana", "bajo", 18.47, -69.93, attrs("urbano", "cultura_historia", "hispanohablante")),
            new CiudadSemilla("Punta Cana", "República Dominicana", "alto", 18.46, -68.93, attrs("lujo", "familiar", "relajacion", "isla_caribe")),
            new CiudadSemilla("Quito", "Ecuador", "bajo", -0.18, -78.47, attrs("cultura_historia", "montana", "hispanohablante")),
            new CiudadSemilla("Guayaquil", "Ecuador", "bajo", -2.17, -79.88, attrs("urbano", "gastronomico_destacado", "hispanohablante")),
            new CiudadSemilla("Lima", "Perú", "bajo", -12.05, -77.04, attrs("urbano", "gastronomico_destacado", "cultura_historia", "hispanohablante")),
            new CiudadSemilla("Cusco", "Perú", "bajo", -13.53, -71.97, attrs("cultura_historia", "ruinas_arqueologicas", "montana", "off_the_beaten_path", "hispanohablante")),
            new CiudadSemilla("La Paz", "Bolivia", "bajo", -16.50, -68.13, attrs("montana", "cultura_historia", "off_the_beaten_path", "hispanohablante")),
            new CiudadSemilla("Santa Cruz de la Sierra", "Bolivia", "bajo", -17.78, -63.18, attrs("urbano", "gastronomico_destacado", "tranquilo", "hispanohablante")),
            new CiudadSemilla("Río de Janeiro", "Brasil", "medio", -22.91, -43.17, attrs("urbano", "festivo", "romantico", "familiar")),
            new CiudadSemilla("São Paulo", "Brasil", "bajo", -23.55, -46.63, attrs("urbano", "cultura_historia", "gastronomico_destacado")),
            new CiudadSemilla("Brasilia", "Brasil", "bajo", -15.80, -47.88, attrs("urbano", "cultura_historia", "tranquilo")),
            new CiudadSemilla("Santiago de Chile", "Chile", "bajo", -33.45, -70.65, attrs("urbano", "montana", "vino", "cultura_historia", "gastronomico_destacado", "hispanohablante")),
            new CiudadSemilla("San Pedro de Atacama", "Chile", "medio", -22.91, -68.20, attrs("desierto", "off_the_beaten_path", "ruinas_arqueologicas", "tranquilo", "hispanohablante")),
            new CiudadSemilla("Chillán / Nevados de Chillán", "Chile", "medio", -36.61, -72.10, attrs("nieve", "montana", "relajacion", "familiar", "hispanohablante")),
            new CiudadSemilla("Buenos Aires", "Argentina", "bajo", -34.60, -58.38, attrs("urbano", "cultura_historia", "romantico", "gastronomico_destacado", "hispanohablante")),
            new CiudadSemilla("Mendoza", "Argentina", "medio", -32.89, -68.85, attrs("vino", "montana", "relajacion", "hispanohablante")),
            new CiudadSemilla("San Carlos de Bariloche", "Argentina", "alto", -41.13, -71.31, attrs("nieve", "montana", "familiar", "relajacion", "hispanohablante")),
            new CiudadSemilla("Montevideo", "Uruguay", "bajo", -34.90, -56.18, attrs("urbano", "tranquilo", "cultura_historia", "gastronomico_destacado", "hispanohablante")),
            new CiudadSemilla("Punta del Este", "Uruguay", "alto", -34.95, -54.95, attrs("lujo", "romantico", "relajacion", "hispanohablante")),
            new CiudadSemilla("Asunción", "Paraguay", "bajo", -25.26, -57.58, attrs("urbano", "tranquilo", "cultura_historia", "hispanohablante")),
            new CiudadSemilla("Ciudad del Este", "Paraguay", "bajo", -25.51, -54.61, attrs("urbano", "hispanohablante"))
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
        if ("Colombia".equalsIgnoreCase(pais) && "Cartagena".equalsIgnoreCase(nombre)) {
            return "Cartagena de Indias";
        }
        if ("Argentina".equalsIgnoreCase(pais) && "Bariloche".equalsIgnoreCase(nombre)) {
            return "San Carlos de Bariloche";
        }
        if ("Venezuela".equalsIgnoreCase(pais) && "Porlamar".equalsIgnoreCase(nombre)) {
            return "Porlamar / Isla de Margarita";
        }
        if ("Chile".equalsIgnoreCase(pais) && "Chillán".equalsIgnoreCase(nombre)) {
            return "Chillán / Nevados de Chillán";
        }
        if ("Brasil".equalsIgnoreCase(pais) && "Brasília".equalsIgnoreCase(nombre)) {
            return "Brasilia";
        }
        return nombre;
    }

    private static Map<String, Boolean> attrs(String... atributos) {
        return Arrays.stream(atributos)
                .collect(java.util.stream.Collectors.toUnmodifiableMap(a -> a, a -> Boolean.TRUE));
    }
}

import java.util.List;
import java.util.Map;

public class DatosCiudades {

    public static List<CiudadSemilla> ciudades() {

        return List.of(

                // =================================================
                // MÉXICO
                // =================================================

                new CiudadSemilla(
                        "Ciudad de México",
                        "México",
                        "bajo",
                        19.43,
                        -99.13,
                        Map.ofEntries(
                                Map.entry("urbano", true),
                                Map.entry("cultura_historia", true),
                                Map.entry("gastronomico_destacado", true),
                                Map.entry("hispanohablante", true)
                        )
                ),

                new CiudadSemilla(
                        "Cancún",
                        "México",
                        "medio",
                        21.16,
                        -86.85,
                        Map.ofEntries(
                                Map.entry("lujo", true),
                                Map.entry("familiar", true),
                                Map.entry("isla_caribe", true),
                                Map.entry("romantico", true)
                        )
                ),

                new CiudadSemilla(
                        "Guadalajara",
                        "México",
                        "bajo",
                        20.66,
                        -103.34,
                        Map.ofEntries(
                                Map.entry("urbano", true),
                                Map.entry("cultura_historia", true),
                                Map.entry("gastronomico_destacado", true),
                                Map.entry("tranquilo", true),
                                Map.entry("hispanohablante", true)
                        )
                ),


                // =================================================
                // GUATEMALA
                // =================================================

                new CiudadSemilla(
                        "Ciudad de Guatemala",
                        "Guatemala",
                        "bajo",
                        14.63,
                        -90.51,
                        Map.ofEntries(
                                Map.entry("urbano", true),
                                Map.entry("cultura_historia", true),
                                Map.entry("hispanohablante", true)
                        )
                ),

                new CiudadSemilla(
                        "Flores",
                        "Guatemala",
                        "bajo",
                        16.91,
                        -89.89,
                        Map.ofEntries(
                                Map.entry("ruinas_arqueologicas", true),
                                Map.entry("tranquilo", true),
                                Map.entry("off_the_beaten_path", true),
                                Map.entry("hispanohablante", true)
                        )
                ),


                // =================================================
                // HONDURAS
                // =================================================

                new CiudadSemilla(
                        "Tegucigalpa",
                        "Honduras",
                        "bajo",
                        14.07,
                        -87.22,
                        Map.ofEntries(
                                Map.entry("urbano", true),
                                Map.entry("cultura_historia", true),
                                Map.entry("hispanohablante", true)
                        )
                ),

                new CiudadSemilla(
                        "Roatán",
                        "Honduras",
                        "medio",
                        16.34,
                        -86.52,
                        Map.ofEntries(
                                Map.entry("isla_caribe", true),
                                Map.entry("romantico", true),
                                Map.entry("relajacion", true),
                                Map.entry("hispanohablante", true)
                        )
                ),


                // =================================================
                // EL SALVADOR
                // =================================================

                new CiudadSemilla(
                        "San Salvador",
                        "El Salvador",
                        "bajo",
                        13.69,
                        -89.18,
                        Map.ofEntries(
                                Map.entry("urbano", true),
                                Map.entry("cultura_historia", true),
                                Map.entry("gastronomico_destacado", true),
                                Map.entry("hispanohablante", true)
                        )
                ),

                new CiudadSemilla(
                        "Santa Ana",
                        "El Salvador",
                        "bajo",
                        13.99,
                        -89.56,
                        Map.ofEntries(
                                Map.entry("cultura_historia", true),
                                Map.entry("tranquilo", true),
                                Map.entry("hispanohablante", true)
                        )
                ),


                // =================================================
                // NICARAGUA
                // =================================================

                new CiudadSemilla(
                        "Managua",
                        "Nicaragua",
                        "bajo",
                        12.13,
                        -86.24,
                        Map.ofEntries(
                                Map.entry("urbano", true),
                                Map.entry("hispanohablante", true)
                        )
                ),

                new CiudadSemilla(
                        "Granada",
                        "Nicaragua",
                        "bajo",
                        11.93,
                        -85.96,
                        Map.ofEntries(
                                Map.entry("cultura_historia", true),
                                Map.entry("tranquilo", true),
                                Map.entry("romantico", true),
                                Map.entry("hispanohablante", true)
                        )
                ),


                // =================================================
                // COSTA RICA
                // =================================================

                new CiudadSemilla(
                        "San José",
                        "Costa Rica",
                        "bajo",
                        9.93,
                        -84.08,
                        Map.ofEntries(
                                Map.entry("urbano", true),
                                Map.entry("cultura_historia", true),
                                Map.entry("tranquilo", true),
                                Map.entry("hispanohablante", true)
                        )
                ),

                new CiudadSemilla(
                        "La Fortuna",
                        "Costa Rica",
                        "medio",
                        10.46,
                        -84.64,
                        Map.ofEntries(
                                Map.entry("relajacion", true),
                                Map.entry("tranquilo", true),
                                Map.entry("off_the_beaten_path", true),
                                Map.entry("hispanohablante", true)
                        )
                ),


                // =================================================
                // PANAMÁ
                // =================================================

                new CiudadSemilla(
                        "Ciudad de Panamá",
                        "Panamá",
                        "bajo",
                        8.98,
                        -79.53,
                        Map.ofEntries(
                                Map.entry("urbano", true),
                                Map.entry("cultura_historia", true),
                                Map.entry("hispanohablante", true)
                        )
                ),

                new CiudadSemilla(
                        "Bocas del Toro",
                        "Panamá",
                        "bajo",
                        9.34,
                        -82.24,
                        Map.ofEntries(
                                Map.entry("isla_caribe", true),
                                Map.entry("tranquilo", true),
                                Map.entry("off_the_beaten_path", true),
                                Map.entry("hispanohablante", true)
                        )
                ),


                // =================================================
                // BELICE
                // =================================================

                new CiudadSemilla(
                        "Belize City",
                        "Belice",
                        "bajo",
                        17.50,
                        -88.20,
                        Map.ofEntries(
                                Map.entry("cultura_historia", true),
                                Map.entry("off_the_beaten_path", true)
                        )
                ),


                // =================================================
                // COLOMBIA
                // =================================================

                new CiudadSemilla(
                        "Bogotá",
                        "Colombia",
                        "bajo",
                        4.71,
                        -74.07,
                        Map.ofEntries(
                                Map.entry("urbano", true),
                                Map.entry("cultura_historia", true),
                                Map.entry("gastronomico_destacado", true),
                                Map.entry("hispanohablante", true)
                        )
                ),

                new CiudadSemilla(
                        "Medellín",
                        "Colombia",
                        "medio",
                        6.25,
                        -75.57,
                        Map.ofEntries(
                                Map.entry("urbano", true),
                                Map.entry("cultura_historia", true),
                                Map.entry("familiar", true),
                                Map.entry("gastronomico_destacado", true),
                                Map.entry("romantico", true),
                                Map.entry("hispanohablante", true)
                        )
                ),

                new CiudadSemilla(
                        "Cartagena de Indias",
                        "Colombia",
                        "medio",
                        10.42,
                        -75.54,
                        Map.ofEntries(
                                Map.entry("romantico", true),
                                Map.entry("familiar", true),
                                Map.entry("gastronomico_destacado", true),
                                Map.entry("cultura_historia", true),
                                Map.entry("isla_caribe", true),
                                Map.entry("hispanohablante", true)
                        )
                ),


                // =================================================
                // VENEZUELA
                // =================================================

                new CiudadSemilla(
                        "Caracas",
                        "Venezuela",
                        "bajo",
                        10.49,
                        -66.88,
                        Map.ofEntries(
                                Map.entry("urbano", true),
                                Map.entry("cultura_historia", true),
                                Map.entry("hispanohablante", true)
                        )
                ),

                new CiudadSemilla(
                        "Porlamar / Isla de Margarita",
                        "Venezuela",
                        "bajo",
                        10.96,
                        -63.91,
                        Map.ofEntries(
                                Map.entry("isla_caribe", true),
                                Map.entry("relajacion", true),
                                Map.entry("tranquilo", true),
                                Map.entry("hispanohablante", true)
                        )
                ),


                // =================================================
                // CUBA
                // =================================================

                new CiudadSemilla(
                        "La Habana",
                        "Cuba",
                        "bajo",
                        23.13,
                        -82.38,
                        Map.ofEntries(
                                Map.entry("cultura_historia", true),
                                Map.entry("romantico", true),
                                Map.entry("festivo", true)
                        )
                ),

                new CiudadSemilla(
                        "Santiago de Cuba",
                        "Cuba",
                        "bajo",
                        20.02,
                        -76.83,
                        Map.ofEntries(
                                Map.entry("cultura_historia", true),
                                Map.entry("festivo", true),
                                Map.entry("tranquilo", true)
                        )
                ),


                // =================================================
                // REPÚBLICA DOMINICANA
                // =================================================

                new CiudadSemilla(
                        "Santo Domingo",
                        "República Dominicana",
                        "bajo",
                        18.47,
                        -69.93,
                        Map.ofEntries(
                                Map.entry("urbano", true),
                                Map.entry("cultura_historia", true),
                                Map.entry("hispanohablante", true)
                        )
                ),

                new CiudadSemilla(
                        "Punta Cana",
                        "República Dominicana",
                        "alto",
                        18.46,
                        -68.93,
                        Map.ofEntries(
                                Map.entry("lujo", true),
                                Map.entry("familiar", true),
                                Map.entry("relajacion", true),
                                Map.entry("isla_caribe", true)
                        )
                ),


                // =================================================
                // ECUADOR
                // =================================================

                new CiudadSemilla(
                        "Quito",
                        "Ecuador",
                        "bajo",
                        -0.18,
                        -78.47,
                        Map.ofEntries(
                                Map.entry("cultura_historia", true),
                                Map.entry("montana", true),
                                Map.entry("hispanohablante", true)
                        )
                ),

                new CiudadSemilla(
                        "Guayaquil",
                        "Ecuador",
                        "bajo",
                        -2.17,
                        -79.88,
                        Map.ofEntries(
                                Map.entry("urbano", true),
                                Map.entry("gastronomico_destacado", true),
                                Map.entry("hispanohablante", true)
                        )
                ),


                // =================================================
                // PERÚ
                // =================================================

                new CiudadSemilla(
                        "Lima",
                        "Perú",
                        "bajo",
                        -12.05,
                        -77.04,
                        Map.ofEntries(
                                Map.entry("urbano", true),
                                Map.entry("gastronomico_destacado", true),
                                Map.entry("cultura_historia", true),
                                Map.entry("hispanohablante", true)
                        )
                ),

                new CiudadSemilla(
                        "Cusco",
                        "Perú",
                        "bajo",
                        -13.53,
                        -71.97,
                        Map.ofEntries(
                                Map.entry("cultura_historia", true),
                                Map.entry("ruinas_arqueologicas", true),
                                Map.entry("montana", true),
                                Map.entry("off_the_beaten_path", true),
                                Map.entry("hispanohablante", true)
                        )
                ),


                // =================================================
                // BOLIVIA
                // =================================================

                new CiudadSemilla(
                        "La Paz",
                        "Bolivia",
                        "bajo",
                        -16.50,
                        -68.13,
                        Map.ofEntries(
                                Map.entry("montana", true),
                                Map.entry("cultura_historia", true),
                                Map.entry("off_the_beaten_path", true),
                                Map.entry("hispanohablante", true)
                        )
                ),

                new CiudadSemilla(
                        "Santa Cruz de la Sierra",
                        "Bolivia",
                        "bajo",
                        -17.78,
                        -63.18,
                        Map.ofEntries(
                                Map.entry("urbano", true),
                                Map.entry("gastronomico_destacado", true),
                                Map.entry("tranquilo", true),
                                Map.entry("hispanohablante", true)
                        )
                ),


                // =================================================
                // BRASIL
                // =================================================

                new CiudadSemilla(
                        "Río de Janeiro",
                        "Brasil",
                        "medio",
                        -22.91,
                        -43.17,
                        Map.ofEntries(
                                Map.entry("urbano", true),
                                Map.entry("festivo", true),
                                Map.entry("romantico", true),
                                Map.entry("familiar", true)
                        )
                ),

                new CiudadSemilla(
                        "São Paulo",
                        "Brasil",
                        "bajo",
                        -23.55,
                        -46.63,
                        Map.ofEntries(
                                Map.entry("urbano", true),
                                Map.entry("cultura_historia", true),
                                Map.entry("gastronomico_destacado", true)
                        )
                ),

                new CiudadSemilla(
                        "Brasilia",
                        "Brasil",
                        "bajo",
                        -15.80,
                        -47.88,
                        Map.ofEntries(
                                Map.entry("urbano", true),
                                Map.entry("cultura_historia", true),
                                Map.entry("tranquilo", true)
                        )
                ),


                // =================================================
                // CHILE
                // =================================================

                new CiudadSemilla(
                        "Santiago de Chile",
                        "Chile",
                        "bajo",
                        -33.45,
                        -70.65,
                        Map.ofEntries(
                                Map.entry("urbano", true),
                                Map.entry("montana", true),
                                Map.entry("vino", true),
                                Map.entry("cultura_historia", true),
                                Map.entry("gastronomico_destacado", true),
                                Map.entry("hispanohablante", true)
                        )
                ),

                new CiudadSemilla(
                        "San Pedro de Atacama",
                        "Chile",
                        "medio",
                        -22.91,
                        -68.20,
                        Map.ofEntries(
                                Map.entry("desierto", true),
                                Map.entry("off_the_beaten_path", true),
                                Map.entry("ruinas_arqueologicas", true),
                                Map.entry("tranquilo", true),
                                Map.entry("hispanohablante", true)
                        )
                ),

                new CiudadSemilla(
                        "Chillán / Nevados de Chillán",
                        "Chile",
                        "medio",
                        -36.61,
                        -72.10,
                        Map.ofEntries(
                                Map.entry("nieve", true),
                                Map.entry("montana", true),
                                Map.entry("relajacion", true),
                                Map.entry("familiar", true),
                                Map.entry("hispanohablante", true)
                        )
                ),


                // =================================================
                // ARGENTINA
                // =================================================

                new CiudadSemilla(
                        "Buenos Aires",
                        "Argentina",
                        "bajo",
                        -34.60,
                        -58.38,
                        Map.ofEntries(
                                Map.entry("urbano", true),
                                Map.entry("cultura_historia", true),
                                Map.entry("romantico", true),
                                Map.entry("gastronomico_destacado", true),
                                Map.entry("hispanohablante", true)
                        )
                ),

                new CiudadSemilla(
                        "Mendoza",
                        "Argentina",
                        "medio",
                        -32.89,
                        -68.85,
                        Map.ofEntries(
                                Map.entry("vino", true),
                                Map.entry("montana", true),
                                Map.entry("relajacion", true),
                                Map.entry("hispanohablante", true)
                        )
                ),

                new CiudadSemilla(
                        "San Carlos de Bariloche",
                        "Argentina",
                        "alto",
                        -41.13,
                        -71.31,
                        Map.ofEntries(
                                Map.entry("nieve", true),
                                Map.entry("montana", true),
                                Map.entry("familiar", true),
                                Map.entry("relajacion", true),
                                Map.entry("hispanohablante", true)
                        )
                ),


                // =================================================
                // URUGUAY
                // =================================================

                new CiudadSemilla(
                        "Montevideo",
                        "Uruguay",
                        "bajo",
                        -34.90,
                        -56.18,
                        Map.ofEntries(
                                Map.entry("urbano", true),
                                Map.entry("tranquilo", true),
                                Map.entry("cultura_historia", true),
                                Map.entry("gastronomico_destacado", true),
                                Map.entry("hispanohablante", true)
                        )
                ),

                new CiudadSemilla(
                        "Punta del Este",
                        "Uruguay",
                        "alto",
                        -34.95,
                        -54.95,
                        Map.ofEntries(
                                Map.entry("lujo", true),
                                Map.entry("romantico", true),
                                Map.entry("relajacion", true),
                                Map.entry("hispanohablante", true)
                        )
                ),


                // =================================================
                // PARAGUAY
                // =================================================

                new CiudadSemilla(
                        "Asunción",
                        "Paraguay",
                        "bajo",
                        -25.26,
                        -57.58,
                        Map.ofEntries(
                                Map.entry("urbano", true),
                                Map.entry("tranquilo", true),
                                Map.entry("cultura_historia", true),
                                Map.entry("hispanohablante", true)
                        )
                ),

                new CiudadSemilla(
                        "Ciudad del Este",
                        "Paraguay",
                        "bajo",
                        -25.51,
                        -54.61,
                        Map.ofEntries(
                                Map.entry("urbano", true),
                                Map.entry("hispanohablante", true)
                        )
                )
        );
    }
}
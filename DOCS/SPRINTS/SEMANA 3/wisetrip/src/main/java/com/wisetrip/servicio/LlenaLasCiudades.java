import com.wisetrip.datos.CiudadDAO;
import com.wisetrip.modelo.Ciudad;

import java.util.Map;

public class LlenarLasCiudades {

    private final CiudadDAO ciudadDAO;
    private final ServicioGeoapify geo;

}
    private static final int RADIO_M = 50_000;

    // Máximo de resultados que puede devolver/contar la consulta.
    private static final int LIMIT_API = 500;


    public LlenarLasCiudades(
            CiudadDAO ciudadDAO,
            ServicioGeoapify geo
    ) {
        this.ciudadDAO = ciudadDAO;
        this.geo = geo;
    }


    public void llenar() {

        for (CiudadSemilla semilla : DatosCiudadesIniciales.ciudades()) {

            Ciudad ciudad = semilla.aCiudad();

            boolean insertada = ciudadDAO.insertar(ciudad);

            if (!insertada) {
                throw new IllegalStateException(
                        "No se pudo insertar la ciudad: "
                                + ciudad.getNombre()
                );
            }



            // ATRIBUTOS MANUALES


            for (
                    Map.Entry<String, Boolean> atributo
                    : semilla.atributosManuales().entrySet()
            ) {

                if (!Boolean.TRUE.equals(atributo.getValue())) {
                    continue;
                }

                DefPregunta pregunta =
                        CatalogoPreguntas.porIdObligatorio(
                                atributo.getKey()
                        );

                // Seguridad:
                // una semilla solo debería contener
                // atributos definidos como MANUAL.
                if (pregunta.tipo != TipoAtributo.MANUAL) {
                    throw new IllegalArgumentException(
                            "El atributo '"
                                    + atributo.getKey()
                                    + "' está en DatosCiudadesIniciales "
                                    + "pero no está definido como MANUAL."
                    );
                }

                // El manual guarda directamente:
                // GUSTO      = 10
                // FUERTE     = 20
                // EXCLUYENTE = 35
                int puntajeManual =
                        pregunta.peso.puntaje;

                ciudadDAO.agregarAtributo(
                        ciudad.getId(),
                        pregunta.id,
                        puntajeManual
                );
            }

            // ATRIBUTOS AUTOMATICOS GEOAPIFY


            for (
                    DefPregunta pregunta
                    : CatalogoPreguntas.atributosAuto()
            ) {
                 */
                int cantidad = geo.contarLugares(
                        ciudad.getLatitud(),
                        ciudad.getLongitud(),
                        pregunta.categorias,
                        RADIO_M,
                        LIMIT_API
                );

                if (cantidad >= pregunta.umbral) {

                    ciudadDAO.agregarAtributo(
                            ciudad.getId(),
                            pregunta.id,
                            cantidad
                    );
                }
            }
        }
    }
}
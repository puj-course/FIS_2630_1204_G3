import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class SelectorDestinos {

    private static final int CANTIDAD_DESTINOS = 3;


    public SeleccionDestinos seleccionarMejoresDestinos(
            List<ResultadoRecomendacion> resultados
    ) {

        List<ResultadoRecomendacion> ordenados =
                new ArrayList<>(resultados);


        ordenados.sort(
                Comparator
                        .comparingInt(
                                ResultadoRecomendacion
                                        ::getPuntajeTotal
                        )
                        .reversed()
        );


        if (ordenados.isEmpty()) {

            return new SeleccionDestinos(
                    new ArrayList<>(),
                    "No encontramos destinos dentro de tu presupuesto."
            );
        }


        int limite =
                Math.min(
                        CANTIDAD_DESTINOS,
                        ordenados.size()
                );


        List<ResultadoRecomendacion> seleccionados =
                new ArrayList<>(
                        ordenados.subList(
                                0,
                                limite
                        )
                );


        String mensaje;


        if (
                seleccionados.size()
                        < CANTIDAD_DESTINOS
        ) {

            mensaje =
                    "Encontramos "
                            + seleccionados.size()
                            + " destino(s) dentro de tu presupuesto.";

        } else {

            mensaje =
                    "Estos son los 3 destinos que mejor coinciden contigo.";
        }


        return new SeleccionDestinos(
                seleccionados,
                mensaje
        );
    }
}
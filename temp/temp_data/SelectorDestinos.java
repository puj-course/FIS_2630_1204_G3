import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;


public class SelectorDestinos {

    private static final int MINIMO_DESTINOS = 3; // ajustable (criterio "Negociable")
    private static final int MAXIMO_DESTINOS = 5; // ajustable (criterio "Negociable")

    public SeleccionDestinos seleccionarMejoresDestinos(List<ResultadoRecomendacion> resultados) {
        return seleccionarMejoresDestinos(resultados, MINIMO_DESTINOS, MAXIMO_DESTINOS);
    }

    public SeleccionDestinos seleccionarMejoresDestinos(List<ResultadoRecomendacion> resultados,
                                                          int minimo, int maximo) {
        // orden de mayor a menor puntaje.
        List<ResultadoRecomendacion> ordenados = new ArrayList<>(resultados);
        ordenados.sort(Comparator.comparingDouble(ResultadoRecomendacion::getPuntajeTotal).reversed());

        if (ordenados.isEmpty()) {
            return new SeleccionDestinos(
                    new ArrayList<>(),
                    "No se encontraron destinos adecuados para tus preferencias y presupuesto."
            );
        }

        int limite = Math.min(maximo, ordenados.size());
        List<ResultadoRecomendacion> seleccionados = new ArrayList<>(ordenados.subList(0, limite));

        String mensaje;
        if (seleccionados.size() < minimo) {
            mensaje = String.format(
                    "Solo se encontraron %d destino%s que coinciden con tus preferencias y presupuesto.",
                    seleccionados.size(), seleccionados.size() > 1 ? "s" : ""
            );
        } else {
            mensaje = String.format("Se encontraron %d destinos recomendados para ti.", seleccionados.size());
        }

        return new SeleccionDestinos(seleccionados, mensaje);
    }
}

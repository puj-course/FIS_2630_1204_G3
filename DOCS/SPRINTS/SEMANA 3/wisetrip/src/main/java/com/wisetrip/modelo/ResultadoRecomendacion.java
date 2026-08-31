import com.wisetrip.modelo.Ciudad;

public class ResultadoRecomendacion {

    private final Ciudad ciudad;

    private final int puntajeTotal;


    public ResultadoRecomendacion(
            Ciudad ciudad,
            int puntajeTotal
    ) {

        this.ciudad = ciudad;
        this.puntajeTotal = puntajeTotal;
    }


    public Ciudad getCiudad() {
        return ciudad;
    }


    public int getPuntajeTotal() {
        return puntajeTotal;
    }
}
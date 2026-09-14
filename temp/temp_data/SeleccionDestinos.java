import java.util.List;


public class SeleccionDestinos {
    private final List<ResultadoRecomendacion> destinos;
    private final String mensaje;

    public SeleccionDestinos(List<ResultadoRecomendacion> destinos, String mensaje) {
        this.destinos = destinos;
        this.mensaje = mensaje;
    }

    public List<ResultadoRecomendacion> getDestinos() { return destinos; }
    public String getMensaje() { return mensaje; }
}

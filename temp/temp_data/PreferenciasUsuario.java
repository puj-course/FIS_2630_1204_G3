import java.util.HashMap;
import java.util.Map;

/**
 * Preferencias y presupuesto ingresados por el usuario.
 * atributos: mismos nombres de intereses usados en Ciudad; true = le interesa al usuario.
 */
public class PreferenciasUsuario {
    private final double presupuesto;
    private final Map<String, Boolean> atributos;

    public PreferenciasUsuario(double presupuesto, Map<String, Boolean> atributos) {
        this.presupuesto = presupuesto;
        this.atributos = new HashMap<>(atributos);
    }

    public double getPresupuesto() { return presupuesto; }
    public Map<String, Boolean> getAtributos() { return atributos; }
}

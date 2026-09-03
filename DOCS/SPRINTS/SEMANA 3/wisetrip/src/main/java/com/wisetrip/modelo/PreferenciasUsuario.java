import java.util.HashMap;
import java.util.Map;

public class PreferenciasUsuario {

    private double presupuesto;

    private Map<String, Boolean> atributos;

    public PreferenciasUsuario() {
        atributos = new HashMap<>();
    }

    public double getPresupuesto() {
        return presupuesto;
    }

    public void setPresupuesto(double presupuesto) {
        this.presupuesto = presupuesto;
    }

    public Map<String, Boolean> getAtributos() {
        return atributos;
    }

    public void setAtributos(
            Map<String, Boolean> atributos
    ) {

        this.atributos =
                atributos != null
                        ? atributos
                        : new HashMap<>();
    }
}
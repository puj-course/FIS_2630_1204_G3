import java.util.HashMap;
import java.util.Map;

/**
 * Representa una ciudad/destino disponible en la base de datos.
 * atributos: mapa de intereses que cumple la ciudad, p.ej.
 *   {"playa": true, "vidaNocturna": true, "naturaleza": false, "cultura": true}
 */
public class Ciudad {
    private final int id;
    private final String nombre;
    private final String pais;
    private final double costoPromedio;
    private final Map<String, Boolean> atributos;
    private final double latitud;
    private final double longitud;

    public Ciudad(int id, String nombre, String pais, double costoPromedio, Map<String, Boolean> atributos) {
        this.id = id;
        this.nombre = nombre;
        this.pais = pais;
        this.costoPromedio = costoPromedio;
        this.atributos = new HashMap<>(atributos);
        this.latitud = latitud;
        this.longitud = longitud;
    }

    public int getId() { return id; }
    public String getNombre() { return nombre; }
    public String getPais() { return pais; }
    public double getCostoPromedio() { return costoPromedio; }
    public Map<String, Boolean> getAtributos() { return atributos; }
    public double getLatitud() { return latitud; }
    public double getLongitud() { return longitud; }
}

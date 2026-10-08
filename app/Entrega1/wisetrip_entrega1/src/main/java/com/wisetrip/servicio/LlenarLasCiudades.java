//llena las ciudades en la base de datos usando CiudadSemilla

package com.wisetrip.servicio;

import java.util.Map;

import com.wisetrip.datos.CiudadSemilla;
import com.wisetrip.datos.DatosCiudades;
import com.wisetrip.modelo.Ciudad;
import com.wisetrip.negocio.CiudadNegocio;
import com.wisetrip.negocio.ICargadorCiudades;

/**
 * Carga en la base de datos las ciudades de DatosCiudades que tengan un
 * costo diario válido. No modifica ni borra ningún destino del catálogo.
 *
 * Implementa la interfaz de negocio ICargadorCiudades.
 * SOLID Inversión de dependencias: depende de la interfaz CiudadNegocio,
 * no de la clase CiudadDAO.
 * GRASP Creador: la semilla, que tiene los datos, crea la Ciudad
 * (semilla.aCiudad); esta clase solo coordina.
 * SOLID Responsabilidad única: cada paso (armar la clave, revisar el
 * costo, insertar) está en su propio método.
 */
public class LlenarLasCiudades implements ICargadorCiudades {

    private final CiudadNegocio repositorioCiudades;

    public LlenarLasCiudades(CiudadNegocio repositorioCiudades) {
        this.repositorioCiudades = repositorioCiudades;
    }

    @Override
    public int llenar(Map<String, Double> costosDiariosPorCiudadYPais) {
        int insertadas = 0;
        if (costosDiariosPorCiudadYPais == null) return insertadas;

        for (CiudadSemilla semilla : DatosCiudades.ciudades()) {
            Double costo = costosDiariosPorCiudadYPais.get(claveDe(semilla));
            if (!esCostoValido(costo)) continue;

            Ciudad ciudad = semilla.aCiudad(costo);
            if (insertar(ciudad)) {
                insertadas++;
            }
        }
        return insertadas;
    }

    /** Clave con la que vienen los costos: "Ciudad|País". */
    private String claveDe(CiudadSemilla semilla) {
        return semilla.nombre() + "|" + semilla.pais();
    }

    /** Un costo diario sirve si existe, es un número real y es mayor que cero. */
    private boolean esCostoValido(Double costo) {
        return costo != null && Double.isFinite(costo) && costo > 0;
    }

    private boolean insertar(Ciudad ciudad) {
        boolean insertada = repositorioCiudades.insertar(ciudad);
        if (!insertada) {
            System.err.println("No se pudo insertar: " + ciudad.getNombre());
        } else {
            System.out.println("Insertada: " + ciudad.getNombre());
        }
        return insertada;
    }
}
//llena las ciudades en la base de datos usando CiudadSemilla

package com.wisetrip.servicio;

import com.wisetrip.datos.CiudadDAO;
import com.wisetrip.datos.CiudadSemilla;
import com.wisetrip.datos.DatosCiudades;
import com.wisetrip.modelo.Ciudad;

public class LlenarLasCiudades {

    private final CiudadDAO ciudadDAO;

    public LlenarLasCiudades(CiudadDAO ciudadDAO) {
        this.ciudadDAO = ciudadDAO;
    }

    public void llenar(java.util.Map<String, Double> costosDiariosPorCiudadYPais) {
        for (CiudadSemilla semilla : DatosCiudades.ciudades()) {
            Double costo = costosDiariosPorCiudadYPais.get(semilla.nombre() + "|" + semilla.pais());
            if (costo == null || !Double.isFinite(costo) || costo <= 0) continue;
            Ciudad ciudad = semilla.aCiudad(costo);
            boolean insertada = ciudadDAO.insertar(ciudad);
            if (!insertada) {
                System.err.println("No se pudo insertar: " + ciudad.getNombre());
            } else {
                System.out.println("Insertada: " + ciudad.getNombre());
            }
        }
    }
}

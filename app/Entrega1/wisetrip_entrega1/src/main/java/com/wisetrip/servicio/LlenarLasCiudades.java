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

    public void llenar() {
        for (CiudadSemilla semilla : DatosCiudades.ciudades()) {
            Ciudad ciudad = semilla.aCiudad();
            boolean insertada = ciudadDAO.insertar(ciudad);
            if (!insertada) {
                System.err.println("No se pudo insertar: " + ciudad.getNombre());
            } else {
                System.out.println("Insertada: " + ciudad.getNombre());
            }
        }
    }
}
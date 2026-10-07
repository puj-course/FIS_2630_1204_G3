package com.wisetrip.negocio;

import com.wisetrip.modelo.Ciudad;
import java.util.List;

public interface CiudadNegocio {

    List<Ciudad> obtenerTodas();
    boolean insertar(Ciudad ciudad);
}
package com.wisetrip.negocio;

import java.util.List;
import java.util.Map;
import com.wisetrip.modelo.Ubicacion;

public interface IViajeServicio {
    List<String> listarPaises();
    List<String> listarCiudades(String pais);
    Map<String, String> validarUbicacion(Ubicacion ubicacion);
}
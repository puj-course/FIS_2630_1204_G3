package com.wisetrip.servicio;

import com.wisetrip.negocio.ProovedorLugares;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

/** Proxy que reutiliza los conteos de lugares durante la vida de la aplicacion. */
@Service
@Primary
public class ProxyLugares implements ProovedorLugares {
    private static final Logger LOGGER = Logger.getLogger(ProxyLugares.class.getName());

    private final ProovedorLugares servicioReal;
    private final Map<String, Integer> conteoLugaresCache = new ConcurrentHashMap<>();

    public ProxyLugares(ServicioGeoapify servicioReal) {
        this.servicioReal = servicioReal;
    }

    @Override
    public double[] coordenadas(String ciudad, String pais) {
        return servicioReal.coordenadas(ciudad, pais);
    }

    @Override
    public Integer contarLugares(double lat, double lon, String categorias, int radioMetros, int limite) {
        String cacheKey = lat + "|" + lon + "|" + categorias + "|" + radioMetros + "|" + limite;
        Integer cacheado = conteoLugaresCache.get(cacheKey);
        if (cacheado != null) {
            LOGGER.info("Geoapify cache hit categorias=" + categorias + " cantidad=" + cacheado);
            return cacheado;
        }

        Integer cantidad = servicioReal.contarLugares(lat, lon, categorias, radioMetros, limite);
        // Cero es un conteo valido; null indica que no se pudo obtener informacion.
        if (cantidad != null) {
            conteoLugaresCache.put(cacheKey, cantidad);
        }
        return cantidad;
    }
}

package com.wisetrip.servicio;

import com.wisetrip.negocio.ProovedorLugares;
import com.wisetrip.negocio.CacheRepositorio;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.logging.Logger;

// Proxy que reutiliza los conteos guardados en la base de datos
@Service
@Primary

public class ProxyLugares implements ProovedorLugares {
    private static final Logger LOGGER = Logger.getLogger(ProxyLugares.class.getName());

    private final ProovedorLugares servicioReal;
    private final CacheRepositorio cache;

    public ProxyLugares(ServicioGeoapify servicioReal, CacheRepositorio cache) {
        this.servicioReal = servicioReal;
        this.cache = cache;
    }

    @Override
    public double[] coordenadas(String ciudad, String pais) {
        return servicioReal.coordenadas(ciudad, pais);
    }

    @Override
    public Integer contarLugares(double lat, double lon, String categorias, int radioMetros, int limite) {
        // Solo cache en BD; la consulta externa se delega al servicio real.
        Optional<Integer> cacheado = cache.buscar(lat, lon, categorias, radioMetros, limite);
        if (cacheado.isPresent()) {
            LOGGER.info("Geoapify cache BD categorias=" + categorias + " cantidad=" + cacheado.get());
            return cacheado.get();
        }

        Integer cantidad = servicioReal.contarLugares(lat, lon, categorias, radioMetros, limite);
        // Cero es valido; los fallos no se guardan.
        if (cantidad != null) {
            cache.guardar(lat, lon, categorias, radioMetros, limite, cantidad);
        }
        return cantidad;
    }
}

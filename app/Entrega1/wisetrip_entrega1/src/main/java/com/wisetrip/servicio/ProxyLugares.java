package com.wisetrip.servicio;

import com.wisetrip.negocio.ProovedorLugares;
import com.wisetrip.negocio.CacheRepositorio;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

// Proxy que reutiliza los conteos guardados en la base de datos
@Service
@Primary

// HU143: NPM explicito = 1; excluye constructores, heredados y generados.
// NOC = 0: sin hijas directas en src/main/java al 2026-11-11. Recalcular ante cambios.
public class ProxyLugares implements ProovedorLugares {
    private static final Logger LOGGER = Logger.getLogger(ProxyLugares.class.getName());

    private final ProovedorLugares servicioReal;
    private final CacheRepositorio cache;

    // Recibe el proveedor real y el repositorio de cache.
    public ProxyLugares(@Qualifier("servicioGeoapify") ProovedorLugares servicioReal,
                        CacheRepositorio cache) {
        this.servicioReal = servicioReal;
        this.cache = cache;
    }

    @Override
    public Integer contarLugares(double lat, double lon, String categorias, int radioMetros, int limite) {
        // Solo cache en BD; la consulta externa se delega al servicio real.
        Optional<Integer> cacheado = cache.buscar(lat, lon, categorias, radioMetros, limite);
        if (cacheado.isPresent()) {
            Integer guardado = cacheado.get();
            // Formato propio del logger: el texto se arma solo si INFO esta activo
            LOGGER.log(Level.INFO, "Geoapify cache BD categorias={0} cantidad={1,number,#}",
                    new Object[] {categorias, guardado});
            return guardado;
        }

        Integer cantidad = servicioReal.contarLugares(lat, lon, categorias, radioMetros, limite);
        // Cero es valido; los fallos no se guardan.
        if (cantidad != null) {
            cache.guardar(lat, lon, categorias, radioMetros, limite, cantidad);
        }
        return cantidad;
    }
}

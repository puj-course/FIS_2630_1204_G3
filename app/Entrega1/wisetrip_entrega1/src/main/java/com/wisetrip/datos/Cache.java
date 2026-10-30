package com.wisetrip.datos;

import com.wisetrip.negocio.CacheRepositorio;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.logging.Logger;

@Repository
public class Cache implements CacheRepositorio {
    private static final Logger REGISTRO = Logger.getLogger(Cache.class.getName());

    private final DataSource dataSource;

    public Cache(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Optional<Integer> buscar(double latitud, double longitud, String categoria,
                                    int radioMetros, int limite) {
        String sql = """
                SELECT respuesta FROM public.cache
                WHERE latitud = ? AND longitud = ? AND categoria = ?
                  AND radio_metros = ? AND limite = ?
                """;

        try (Connection conexion = dataSource.getConnection();
             PreparedStatement consulta = conexion.prepareStatement(sql)) {
            asignarClave(consulta, latitud, longitud, categoria, radioMetros, limite);
            try (ResultSet resultado = consulta.executeQuery()) {
                if (!resultado.next()) return Optional.empty();
                // Cero es un resultado guardado, no una ausencia.
                return Optional.of(resultado.getInt("respuesta"));
            }
        } catch (SQLException e) {
            // Un fallo de cache permite continuar con Geoapify.
            REGISTRO.warning("No se pudo leer la cache. SQLState=" + e.getSQLState());
            return Optional.empty();
        }
    }

    @Override
    public void guardar(double latitud, double longitud, String categoria,
                        int radioMetros, int limite, int respuesta) {
        String sql = """
                INSERT INTO public.cache
                    (latitud, longitud, categoria, radio_metros, limite, respuesta, consultado_en)
                VALUES (?, ?, ?, ?, ?, ?, now())
                ON CONFLICT (latitud, longitud, categoria, radio_metros, limite)
                DO UPDATE SET respuesta = EXCLUDED.respuesta, consultado_en = now()
                """;

        try (Connection conexion = dataSource.getConnection();
             PreparedStatement consulta = conexion.prepareStatement(sql)) {
            asignarClave(consulta, latitud, longitud, categoria, radioMetros, limite);
            consulta.setInt(6, respuesta);
            consulta.executeUpdate();
        } catch (SQLException e) {
            // El resultado sigue siendo util aunque no se guarde.
            REGISTRO.warning("No se pudo guardar la cache. SQLState=" + e.getSQLState());
        }
    }

    private void asignarClave(PreparedStatement consulta, double latitud, double longitud,
                             String categoria, int radioMetros, int limite) throws SQLException {
        consulta.setDouble(1, latitud);
        consulta.setDouble(2, longitud);
        consulta.setString(3, categoria);
        consulta.setInt(4, radioMetros);
        consulta.setInt(5, limite);
    }
}

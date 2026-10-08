package com.wisetrip.datos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.wisetrip.negocio.IRepositorioPreferencias;

/**
 * Guarda en PostgreSQL las preferencias activas de un viaje.
 * Implementa la interfaz de negocio IRepositorioPreferencias.
 * GRASP Fabricación pura: separa el acceso a datos del dominio.
 */
@Repository
public class PreferenciaDAO implements IRepositorioPreferencias {

    @Override
    public void guardarActivas(int idViaje, Map<String, Boolean> atributosSeleccionados) {
        if (atributosSeleccionados == null || atributosSeleccionados.isEmpty()) {
            return;
        }

        String sql = """
                INSERT INTO preferencias (id_viaje, tipo_preferencia)
                VALUES (?, ?)
                ON CONFLICT (id_viaje, tipo_preferencia) DO NOTHING
                """;

        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            boolean hayActivas = false;

            for (Map.Entry<String, Boolean> entry : atributosSeleccionados.entrySet()) {
                if (!Boolean.TRUE.equals(entry.getValue())) {
                    continue;
                }
                stmt.setInt(1, idViaje);
                stmt.setString(2, entry.getKey());
                stmt.addBatch();
                hayActivas = true;
            }

            if (hayActivas) {
                stmt.executeBatch();
            }
        } catch (SQLException e) {
            throw new IllegalStateException("No se pudieron guardar las preferencias en PostgreSQL.", e);
        }
    }
}
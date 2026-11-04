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
        if (idViaje <= 0) {
            throw new IllegalArgumentException("El ID del viaje debe ser positivo.");
        }
        if (atributosSeleccionados == null) {
            throw new IllegalArgumentException("Las preferencias del viaje no pueden ser nulas.");
        }

        String sql = """
                INSERT INTO preferencias (id_viaje, tipo_preferencia)
                VALUES (?, ?)
                ON CONFLICT (id_viaje, tipo_preferencia) DO NOTHING
                """;

        try (Connection conn = ConexionBD.obtenerConexion()) {
            conn.setAutoCommit(false);
            try (PreparedStatement bloqueo = conn.prepareStatement(
                        "SELECT id_viaje FROM viajes WHERE id_viaje = ? FOR UPDATE");
                 PreparedStatement borrar = conn.prepareStatement(
                        "DELETE FROM preferencias WHERE id_viaje = ?");
                 PreparedStatement stmt = conn.prepareStatement(sql)) {
                // Serializa la sincronizacion de preferencias del mismo viaje.
                bloqueo.setInt(1, idViaje);
                try (var resultado = bloqueo.executeQuery()) {
                    if (!resultado.next()) {
                        throw new IllegalStateException("No existe el viaje para guardar sus preferencias.");
                    }
                }

                borrar.setInt(1, idViaje);
                borrar.executeUpdate();

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
                conn.commit();
            } catch (SQLException | RuntimeException e) {
                try {
                    conn.rollback();
                } catch (SQLException rollbackError) {
                    e.addSuppressed(rollbackError);
                }
                throw e;
            }
        } catch (SQLException e) {
            throw new IllegalStateException("No se pudieron guardar las preferencias en PostgreSQL.", e);
        }
    }
}

package com.wisetrip.datos;

import com.wisetrip.modelo.FechasViaje;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

@Repository
public class ViajeDAO {

    public int insertar(int idUsuario, int idCiudad, FechasViaje fechas, double presupuestoUsd) {
        String sql = """
                INSERT INTO viajes (id_usuario, id_ciudad, fecha_inicio, fecha_fin, presupuesto)
                VALUES (?, ?, ?, ?, ?)
                RETURNING id_viaje
                """;

        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idUsuario);
            stmt.setInt(2, idCiudad);
            stmt.setDate(3, Date.valueOf(LocalDate.parse(fechas.getFechaInicio())));
            stmt.setDate(4, Date.valueOf(LocalDate.parse(fechas.getFechaFin())));
            stmt.setDouble(5, presupuestoUsd);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id_viaje");
                }
            }
            throw new IllegalStateException("PostgreSQL no devolvió id_viaje.");
        } catch (SQLException e) {
            throw new IllegalStateException("No se pudo guardar el viaje en PostgreSQL.", e);
        }
    }
}

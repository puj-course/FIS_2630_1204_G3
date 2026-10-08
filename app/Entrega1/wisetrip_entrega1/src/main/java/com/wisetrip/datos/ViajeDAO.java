package com.wisetrip.datos;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

import org.springframework.stereotype.Repository;

import com.wisetrip.modelo.FechasViaje;
import com.wisetrip.negocio.IRepositorioViajes;

/**
 * Guarda los viajes en PostgreSQL.
 * Implementa la interfaz de negocio IRepositorioViajes.
 * se separa el acceso a datos del dominio.
 */
//mejoras
//validar fecha_inicio >= fecha_actual (regla de negocio)
//Validar fecha_fin >= fecha_inicio (regla de negocio)
//Validar presupuesto > 0 (regla de negocio)
//Manejo de SQLException (unique, foreign key, check)
//Verificar que idUsuario e idCiudad existan en BD
//Agregar validación de duplicados (evitar viajes idénticos)
//Agregar logs de auditoría

@Repository
public class ViajeDAO implements IRepositorioViajes {

    @Override
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
package com.wisetrip.datos;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.wisetrip.modelo.Gasto;
import com.wisetrip.negocio.IRepositorioGastos;

/**
 * Guarda y consulta los gastos de un viaje en la tabla "gastos" de PostgreSQL.
 * Implementa la interfaz de negocio IRepositorioGastos.
 *
 * La tabla no guarda la moneda: todos los montos del viaje se manejan en
 * dolares, igual que el presupuesto de la tabla "viajes".
 */
@Repository
public class GastoDAO implements IRepositorioGastos {

    static final String MONEDA_VIAJE = "USD";

    @Override
    public Gasto guardar(int idViaje, Gasto gasto) {
        String sql = """
                INSERT INTO gastos (id_viaje, descripcion, monto, fecha_gasto, categoria)
                VALUES (?, ?, ?, ?, ?)
                RETURNING id_gasto
                """;
        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idViaje);
            stmt.setString(2, gasto.descripcion().trim());
            stmt.setBigDecimal(3, gasto.monto());
            stmt.setDate(4, Date.valueOf(gasto.fecha()));
            stmt.setString(5, gasto.categoria());
            try (ResultSet rs = stmt.executeQuery()) {
                rs.next();
                return new Gasto(rs.getInt("id_gasto"), gasto.categoria(), gasto.descripcion().trim(),
                        gasto.monto(), MONEDA_VIAJE, gasto.fecha());
            }
        } catch (SQLException e) {
            throw new IllegalStateException("No se pudo guardar el gasto en la base de datos.", e);
        }
    }

    @Override
    public List<Gasto> listarPorViaje(int idViaje) {
        String sql = """
                SELECT id_gasto, categoria, descripcion, monto, fecha_gasto
                FROM gastos
                WHERE id_viaje = ?
                ORDER BY fecha_gasto DESC, id_gasto DESC
                """;
        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idViaje);
            List<Gasto> gastos = new ArrayList<>();
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    gastos.add(new Gasto(rs.getInt("id_gasto"), rs.getString("categoria"),
                            rs.getString("descripcion"), rs.getBigDecimal("monto"),
                            MONEDA_VIAJE, rs.getDate("fecha_gasto").toLocalDate()));
                }
            }
            return gastos;
        } catch (SQLException e) {
            throw new IllegalStateException("No se pudieron consultar los gastos del viaje.", e);
        }
    }

    @Override
    public BigDecimal totalGastadoPorViaje(int idViaje) {
        return sumar("SELECT COALESCE(SUM(monto), 0) FROM gastos WHERE id_viaje = ?", idViaje, null);
    }

    @Override
    public BigDecimal totalGastadoPorCategoria(int idViaje, String categoria) {
        return sumar("SELECT COALESCE(SUM(monto), 0) FROM gastos WHERE id_viaje = ? AND categoria = ?",
                idViaje, categoria);
    }

    private BigDecimal sumar(String sql, int idViaje, String categoria) {
        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idViaje);
            if (categoria != null) {
                stmt.setString(2, categoria);
            }
            try (ResultSet rs = stmt.executeQuery()) {
                rs.next();
                return rs.getBigDecimal(1);
            }
        } catch (SQLException e) {
            throw new IllegalStateException("No se pudo calcular el total gastado del viaje.", e);
        }
    }
}

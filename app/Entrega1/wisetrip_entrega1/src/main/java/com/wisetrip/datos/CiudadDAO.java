package com.wisetrip.datos;

import com.wisetrip.modelo.Ciudad;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.*;

@Repository
public class CiudadDAO {

    public List<Ciudad> obtenerTodas() {
        List<Ciudad> ciudades = new ArrayList<>();
        String sql = """
        SELECT id_ciudad, nombre, pais, latitud, longitud, costo_promedio
        FROM ciudad
        ORDER BY nombre
        """;
        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Ciudad ciudad = new Ciudad();
                ciudad.setId(rs.getInt("id_ciudad"));
                ciudad.setNombre(rs.getString("nombre"));
                ciudad.setPais(rs.getString("pais"));
                ciudad.setLatitud(rs.getDouble("latitud"));
                ciudad.setLongitud(rs.getDouble("longitud"));
                ciudad.setCostoPromedio(rs.getDouble("costo_promedio"));
                ciudades.add(ciudad);
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener ciudades: " + e.getMessage());
        }
        return ciudades;
    }

    public boolean insertar(Ciudad ciudad) {
        String sql = "INSERT INTO ciudad (nombre, pais, latitud, longitud, costo_promedio) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, ciudad.getNombre());
            stmt.setString(2, ciudad.getPais());
            stmt.setDouble(3, ciudad.getLatitud());
            stmt.setDouble(4, ciudad.getLongitud());
            stmt.setDouble(5, ciudad.getCostoPromedio());
            stmt.executeUpdate();
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) ciudad.setId(rs.getInt(1));
            }
            return true;
        } catch (SQLException e) {
            System.err.println("Error al insertar ciudad " + ciudad.getNombre() + ": " + e.getMessage());
            return false;
        }
    }
}

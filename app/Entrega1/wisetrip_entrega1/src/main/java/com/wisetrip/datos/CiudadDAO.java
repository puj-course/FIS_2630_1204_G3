package com.wisetrip.datos;

import com.wisetrip.modelo.Ciudad;
import com.wisetrip.negocio.CiudadNegocio;

import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.*;

@Repository
public class CiudadDAO implements CiudadNegocio {

    @Override
    public List<Ciudad> obtenerTodas() {

        List<Ciudad> ciudades = new ArrayList<>();

        String sql = """
                SELECT id_ciudad, nombre, pais, latitud, longitud, costo_promedio
                FROM ciudad
                WHERE costo_promedio > 0
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

            System.err.println(
                    "Error al obtener ciudades: " + e.getMessage()
            );
        }

        return ciudades;
    }

}
package com.wisetrip.datos;

import com.wisetrip.modelo.Ciudad;

import java.sql.*;
import java.util.*;

public class CiudadDAO {

    public List<Ciudad> obtenerTodas() {

        Map<Integer, Ciudad> ciudadesMap = new LinkedHashMap<>();

        String sql = """
            SELECT
                c.id_ciudad,
                c.nombre,
                c.pais,
                c.latitud,
                c.longitud,
                c.costo_promedio,
                c.id_nivel,
                a.nombre AS nombre_atributo,
                ca.valor AS valor_atributo
            FROM ciudad c
            LEFT JOIN ciudad_atributo ca
                ON c.id_ciudad = ca.id_ciudad
            LEFT JOIN atributo a
                ON ca.id_atributo = a.id_atributo
            ORDER BY c.id_ciudad
            """;

        try (
                Connection conn = ConexionBD.obtenerConexion();
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()
        ) {

            while (rs.next()) {

                int id = rs.getInt("id_ciudad");

                Ciudad ciudad = ciudadesMap.get(id);

                if (ciudad == null) {

                    ciudad = new Ciudad();

                    ciudad.setId(id);
                    ciudad.setNombre(rs.getString("nombre"));
                    ciudad.setPais(rs.getString("pais"));
                    ciudad.setLatitud(rs.getDouble("latitud"));
                    ciudad.setLongitud(rs.getDouble("longitud"));
                    ciudad.setCostoPromedio(
                            rs.getDouble("costo_promedio")
                    );
                    ciudad.setIdNivel(rs.getInt("id_nivel"));

                    ciudadesMap.put(id, ciudad);
                }

                String nombreAtributo =
                        rs.getString("nombre_atributo");

                if (nombreAtributo != null) {

                    int valor =
                            rs.getInt("valor_atributo");

                    ciudad.getAtributos()
                            .put(nombreAtributo, valor);
                }
            }

        } catch (SQLException e) {
            System.err.println(
                    "Error al obtener ciudades: "
                            + e.getMessage()
            );
        }

        return new ArrayList<>(ciudadesMap.values());
    }

    /**
     * Alias para usarlo desde el recomendador si quieres
     * conservar ese nombre.
     */
    public List<Ciudad> listarConAtributos() {
        return obtenerTodas();
    }

    public boolean insertar(Ciudad ciudad) {

        String sql = """
            INSERT INTO ciudad
            (
                nombre,
                pais,
                latitud,
                longitud,
                costo_promedio,
                id_nivel
            )
            VALUES (?, ?, ?, ?, ?, ?)
            """;

        try (
                Connection conn = ConexionBD.obtenerConexion();
                PreparedStatement stmt = conn.prepareStatement(
                        sql,
                        Statement.RETURN_GENERATED_KEYS
                )
        ) {

            stmt.setString(1, ciudad.getNombre());
            stmt.setString(2, ciudad.getPais());
            stmt.setDouble(3, ciudad.getLatitud());
            stmt.setDouble(4, ciudad.getLongitud());
            stmt.setDouble(5, ciudad.getCostoPromedio());
            stmt.setInt(6, ciudad.getIdNivel());

            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {

                if (rs.next()) {
                    ciudad.setId(rs.getInt(1));
                } else {
                    throw new SQLException(
                            "No se obtuvo ID generado para "
                                    + ciudad.getNombre()
                    );
                }
            }

            return true;

        } catch (SQLException e) {

            System.err.println(
                    "Error al insertar ciudad "
                            + ciudad.getNombre()
                            + ": "
                            + e.getMessage()
            );

            return false;
        }
    }

    /**
     * Agrega un atributo usando el NOMBRE.
     *
     * valor:
     * MANUAL -> puntaje
     * AUTO   -> cantidad Geoapify
     */
    public boolean agregarAtributo(
            int idCiudad,
            String nombreAtributo,
            int valor
    ) {

        try (Connection conn = ConexionBD.obtenerConexion()) {

            int idAtributo =
                    obtenerOCrearIdAtributo(
                            conn,
                            nombreAtributo
                    );

            String sql = """
                INSERT INTO ciudad_atributo
                    (id_ciudad, id_atributo, valor)
                VALUES (?, ?, ?)
                ON CONFLICT (id_ciudad, id_atributo)
                DO UPDATE SET valor = EXCLUDED.valor
                """;

            try (PreparedStatement stmt =
                         conn.prepareStatement(sql)) {

                stmt.setInt(1, idCiudad);
                stmt.setInt(2, idAtributo);
                stmt.setInt(3, valor);

                stmt.executeUpdate();
            }

            return true;

        } catch (SQLException e) {

            System.err.println(
                    "Error agregando atributo "
                            + nombreAtributo
                            + " a ciudad "
                            + idCiudad
                            + ": "
                            + e.getMessage()
            );

            return false;
        }
    }

    private int obtenerOCrearIdAtributo(
            Connection conn,
            String nombre
    ) throws SQLException {

        String buscar = """
            SELECT id_atributo
            FROM atributo
            WHERE nombre = ?
            """;

        try (PreparedStatement stmt =
                     conn.prepareStatement(buscar)) {

            stmt.setString(1, nombre);

            try (ResultSet rs = stmt.executeQuery()) {

                if (rs.next()) {
                    return rs.getInt("id_atributo");
                }
            }
        }

        String insertar = """
            INSERT INTO atributo(nombre)
            VALUES (?)
            """;

        try (
                PreparedStatement stmt =
                        conn.prepareStatement(
                                insertar,
                                Statement.RETURN_GENERATED_KEYS
                        )
        ) {

            stmt.setString(1, nombre);
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {

                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }

        throw new SQLException(
                "No se pudo crear el atributo: " + nombre
        );
    }
}
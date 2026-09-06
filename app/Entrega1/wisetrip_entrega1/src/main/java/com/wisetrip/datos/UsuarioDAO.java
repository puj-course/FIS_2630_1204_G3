package com.wisetrip.datos;

import com.wisetrip.modelo.Usuario;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

@Repository
public class UsuarioDAO {

    public Usuario registrar(Usuario usuario) {
        String sql = """
                INSERT INTO usuario
                    (nombre, correo, contraseña, rol, estado, tipo_documento, numero_documento, fecha_nacimiento)
                VALUES (?, ?, ?, ?, TRUE, ?, ?, ?)
                RETURNING id_usuario
                """;

        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, usuario.getNombreCompleto());
            stmt.setString(2, usuario.getCorreo());
            stmt.setString(3, usuario.getPassword());
            stmt.setString(4, usuario.getRol() != null ? usuario.getRol() : "cliente");
            stmt.setString(5, usuario.getTipoDocumento());
            stmt.setString(6, usuario.getNumeroDocumento());
            stmt.setDate(7, parseFecha(usuario.getFechaNacimiento()));

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    usuario.setIdUsuario(rs.getInt("id_usuario"));
                }
            }
            return usuario;
        } catch (SQLException e) {
            throw new IllegalStateException("No se pudo registrar el usuario en PostgreSQL.", e);
        }
    }

    public boolean existeCorreo(String correo) {
        return existe("SELECT 1 FROM usuario WHERE LOWER(correo) = LOWER(?)", correo);
    }

    public boolean existeDocumento(String numeroDocumento) {
        return existe("SELECT 1 FROM usuario WHERE numero_documento = ?", numeroDocumento);
    }

    public Usuario buscarPorCorreo(String correo) {
        String sql = """
                SELECT id_usuario, nombre, correo, contraseña, rol,
                       tipo_documento, numero_documento, fecha_nacimiento
                FROM usuario
                WHERE LOWER(correo) = LOWER(?) AND estado = TRUE
                """;

        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, correo);

            try (ResultSet rs = stmt.executeQuery()) {
                if (!rs.next()) {
                    return null;
                }

                Usuario usuario = new Usuario();
                usuario.setIdUsuario(rs.getInt("id_usuario"));
                usuario.setNombreCompleto(rs.getString("nombre"));
                usuario.setCorreo(rs.getString("correo"));
                usuario.setPassword(rs.getString("contraseña"));
                usuario.setRol(rs.getString("rol"));
                usuario.setTipoDocumento(rs.getString("tipo_documento"));
                usuario.setNumeroDocumento(rs.getString("numero_documento"));
                Date fechaNacimiento = rs.getDate("fecha_nacimiento");
                if (fechaNacimiento != null) {
                    usuario.setFechaNacimiento(fechaNacimiento.toLocalDate().toString());
                }
                return usuario;
            }
        } catch (SQLException e) {
            throw new IllegalStateException("No se pudo consultar el usuario en PostgreSQL.", e);
        }
    }

    private boolean existe(String sql, String valor) {
        if (valor == null || valor.isBlank()) {
            return false;
        }

        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, valor.trim());
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            throw new IllegalStateException("No se pudo validar el usuario en PostgreSQL.", e);
        }
    }

    private Date parseFecha(String fecha) {
        if (fecha == null || fecha.isBlank()) {
            return null;
        }
        return Date.valueOf(LocalDate.parse(fecha));
    }
}

package com.wisetrip.datos;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConexionBD {

    private static final Properties PROPIEDADES = cargarPropiedades();

    public static Connection obtenerConexion() throws SQLException {
        try {
            return DriverManager.getConnection(
                    PROPIEDADES.getProperty("spring.datasource.url"),
                    PROPIEDADES.getProperty("spring.datasource.username"),
                    PROPIEDADES.getProperty("spring.datasource.password")
            );
        } catch (SQLException e) {
            System.err.println("Error al conectar a la base de datos: " + e.getMessage());
            throw e;
        }
    }

    private static Properties cargarPropiedades() {
        Properties propiedades = new Properties();
        try (InputStream input = ConexionBD.class.getClassLoader().getResourceAsStream("application.properties")) {
            if (input == null) {
                throw new IllegalStateException("No se encontró application.properties.");
            }
            propiedades.load(input);
        } catch (IOException e) {
            throw new IllegalStateException("No se pudo cargar application.properties.", e);
        }
        return propiedades;
    }
}

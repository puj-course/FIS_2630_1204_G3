package com.wisetrip.datos;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import com.wisetrip.modelo.Ciudad;

/**
 * Pruebas unitarias de CiudadDAO
 * La base de datos no se toca: la conexión estática (ConexionBD) se simula con
 * mockStatic y Connection, PreparedStatement y ResultSet se simulan con Mockito
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("CiudadDAO")
class CiudadDAOTest {

    @Mock
    private Connection conexion;

    @Mock
    private PreparedStatement sentencia;

    @Mock
    private ResultSet resultado;

    private CiudadDAO dao;

    @BeforeEach
    void setUp() {
        dao = new CiudadDAO();
    }

   
    @Test
    @DisplayName("obtenerTodas: las filas de la consulta se convierten en ciudades")
    void obtenerTodas_filasValidas_devuelveCiudades() throws SQLException {
        // Arrange
        when(conexion.prepareStatement(anyString())).thenReturn(sentencia);
        when(sentencia.executeQuery()).thenReturn(resultado);
        when(resultado.next()).thenReturn(true, true, false);
        when(resultado.getInt("id_ciudad")).thenReturn(1, 2);
        when(resultado.getString("nombre")).thenReturn("Bogotá", "Lima");
        when(resultado.getString("pais")).thenReturn("Colombia", "Perú");
        when(resultado.getDouble("latitud")).thenReturn(4.71, -12.04);
        when(resultado.getDouble("longitud")).thenReturn(-74.07, -77.04);
        when(resultado.getDouble("costo_promedio")).thenReturn(50.0, 45.5);

        try (MockedStatic<ConexionBD> bd = mockStatic(ConexionBD.class)) {
            bd.when(ConexionBD::obtenerConexion).thenReturn(conexion);

            // Act
            List<Ciudad> ciudades = dao.obtenerTodas();

            // Assert
            assertEquals(2, ciudades.size());
            assertEquals(1, ciudades.get(0).getId());
            assertEquals("Bogotá", ciudades.get(0).getNombre());
            assertEquals("Colombia", ciudades.get(0).getPais());
            assertEquals(4.71, ciudades.get(0).getLatitud(), 0.0001);
            assertEquals(-74.07, ciudades.get(0).getLongitud(), 0.0001);
            assertEquals(50.0, ciudades.get(0).getCostoPromedio(), 0.0001);
            assertEquals("Lima", ciudades.get(1).getNombre());
            assertEquals(45.5, ciudades.get(1).getCostoPromedio(), 0.0001);
        }
    }

    @Test
    @DisplayName("obtenerTodas: borde, sin filas devuelve una lista vacía")
    void obtenerTodas_sinFilas_listaVacia() throws SQLException {
        // Arrange
        when(conexion.prepareStatement(anyString())).thenReturn(sentencia);
        when(sentencia.executeQuery()).thenReturn(resultado);
        when(resultado.next()).thenReturn(false);

        try (MockedStatic<ConexionBD> bd = mockStatic(ConexionBD.class)) {
            bd.when(ConexionBD::obtenerConexion).thenReturn(conexion);

            // Act
            List<Ciudad> ciudades = dao.obtenerTodas();

            // Assert
            assertTrue(ciudades.isEmpty());
        }
    }

    @Test
    @DisplayName("obtenerTodas: si falla al preparar la consulta devuelve lista vacía sin lanzar excepción")
    void obtenerTodas_falloAlPreparar_listaVacia() throws SQLException {
        // Arrange
        when(conexion.prepareStatement(anyString())).thenThrow(new SQLException("fallo simulado"));

        try (MockedStatic<ConexionBD> bd = mockStatic(ConexionBD.class)) {
            bd.when(ConexionBD::obtenerConexion).thenReturn(conexion);

            // Act
            List<Ciudad> ciudades = dao.obtenerTodas();

            // Assert
            assertTrue(ciudades.isEmpty());
        }
    }

    @Test
    @DisplayName("obtenerTodas: si falla al ejecutar la consulta devuelve lista vacía sin lanzar excepción")
    void obtenerTodas_falloAlEjecutar_listaVacia() throws SQLException {
        // Arrange
        when(conexion.prepareStatement(anyString())).thenReturn(sentencia);
        when(sentencia.executeQuery()).thenThrow(new SQLException("fallo simulado"));

        try (MockedStatic<ConexionBD> bd = mockStatic(ConexionBD.class)) {
            bd.when(ConexionBD::obtenerConexion).thenReturn(conexion);

            // Act
            List<Ciudad> ciudades = dao.obtenerTodas();

            // Assert
            assertTrue(ciudades.isEmpty());
        }
    }

    @Test
    @DisplayName("obtenerTodas: la consulta solo trae ciudades con costo promedio mayor a 0, ordenadas por nombre")
    void obtenerTodas_consulta_filtraCostoYOrdenaPorNombre() throws SQLException {
        // Arrange
        when(conexion.prepareStatement(anyString())).thenReturn(sentencia);
        when(sentencia.executeQuery()).thenReturn(resultado);
        when(resultado.next()).thenReturn(false);
        ArgumentCaptor<String> sqlCaptor = ArgumentCaptor.forClass(String.class);

        try (MockedStatic<ConexionBD> bd = mockStatic(ConexionBD.class)) {
            bd.when(ConexionBD::obtenerConexion).thenReturn(conexion);

            // Act
            dao.obtenerTodas();

            // Assert
            verify(conexion).prepareStatement(sqlCaptor.capture());
            String sql = sqlCaptor.getValue();
            assertTrue(sql.contains("costo_promedio > 0"));
            assertTrue(sql.contains("ORDER BY nombre"));
        }
    }

    @Test
    @DisplayName("obtenerTodas: cierra la conexión, la sentencia y el resultado al terminar")
    void obtenerTodas_cierraLosRecursos() throws SQLException {
        // Arrange
        when(conexion.prepareStatement(anyString())).thenReturn(sentencia);
        when(sentencia.executeQuery()).thenReturn(resultado);
        when(resultado.next()).thenReturn(false);

        try (MockedStatic<ConexionBD> bd = mockStatic(ConexionBD.class)) {
            bd.when(ConexionBD::obtenerConexion).thenReturn(conexion);

            // Act
            dao.obtenerTodas();

            // Assert
            verify(resultado).close();
            verify(sentencia).close();
            verify(conexion).close();
        }
    }
}

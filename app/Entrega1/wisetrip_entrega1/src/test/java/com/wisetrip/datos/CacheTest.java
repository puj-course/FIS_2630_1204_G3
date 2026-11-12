package com.wisetrip.datos;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

import javax.sql.DataSource;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

/**
 * Pruebas unitarias de Cache (repositorio JDBC de la cache de Geoapify).
 *
 * El DataSource, la conexion, la consulta y el resultado se simulan con
 * Mockito: no se usa la base de datos real. Se revisa que los parametros se
 * asignen en orden y que un fallo de la base nunca rompa la recomendacion.
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@DisplayName("Cache")
class CacheTest {

    @Mock
    private DataSource dataSource;
    @Mock
    private Connection conexion;
    @Mock
    private PreparedStatement consulta;
    @Mock
    private ResultSet resultado;

    private Cache cache;

    @BeforeEach
    void setUp() throws SQLException {
        cache = new Cache(dataSource);
        when(dataSource.getConnection()).thenReturn(conexion);
        when(conexion.prepareStatement(anyString())).thenReturn(consulta);
        when(consulta.executeQuery()).thenReturn(resultado);
    }

    private void verificarClave() throws SQLException {
        verify(consulta).setDouble(1, 10.4);
        verify(consulta).setDouble(2, -75.5);
        verify(consulta).setString(3, "beach");
        verify(consulta).setInt(4, 5000);
        verify(consulta).setInt(5, 20);
    }

    // ------------------------------------------------------------ buscar

    @Test
    @DisplayName("buscar devuelve la respuesta guardada para esa clave")
    void buscar_encontrado_devuelveRespuesta() throws SQLException {
        when(resultado.next()).thenReturn(true);
        when(resultado.getInt("respuesta")).thenReturn(7);

        Optional<Integer> respuesta = cache.buscar(10.4, -75.5, "beach", 5000, 20);

        assertEquals(Optional.of(7), respuesta);
        verificarClave();
    }

    @Test
    @DisplayName("borde: un cero guardado es un resultado valido, no ausencia")
    void buscar_ceroGuardado_devuelveCero() throws SQLException {
        when(resultado.next()).thenReturn(true);
        when(resultado.getInt("respuesta")).thenReturn(0);

        assertEquals(Optional.of(0), cache.buscar(10.4, -75.5, "beach", 5000, 20));
    }

    @Test
    @DisplayName("si la clave no esta en la cache devuelve vacio")
    void buscar_noEncontrado_vacio() throws SQLException {
        when(resultado.next()).thenReturn(false);

        assertTrue(cache.buscar(10.4, -75.5, "beach", 5000, 20).isEmpty());
        verify(resultado, never()).getInt(anyString());
    }

    @Test
    @DisplayName("si la base falla devuelve vacio para que se consulte Geoapify")
    void buscar_errorDeBase_vacio() throws SQLException {
        when(dataSource.getConnection()).thenThrow(new SQLException("sin conexion", "08001"));

        assertTrue(cache.buscar(10.4, -75.5, "beach", 5000, 20).isEmpty());
    }

    @Test
    @DisplayName("si la consulta falla con la conexion abierta, devuelve vacio y cierra la conexion")
    void buscar_errorEnLaConsulta_cierraRecursos() throws SQLException {
        when(consulta.executeQuery()).thenThrow(new SQLException("tabla no existe", "42P01"));

        assertTrue(cache.buscar(10.4, -75.5, "beach", 5000, 20).isEmpty());
        verify(consulta).close();
        verify(conexion).close();
    }

    @Test
    @DisplayName("si falla la lectura del resultado, devuelve vacio y cierra el ResultSet")
    void buscar_errorLeyendoResultado_cierraElResultado() throws SQLException {
        when(resultado.next()).thenThrow(new SQLException("conexion cortada", "08006"));

        assertTrue(cache.buscar(10.4, -75.5, "beach", 5000, 20).isEmpty());
        verify(resultado).close();
        verify(conexion).close();
    }

    @Test
    @DisplayName("borde: si falla la consulta y tambien el cierre, devuelve vacio sin lanzar excepcion")
    void buscar_errorEnConsultaYEnCierre_vacio() throws SQLException {
        when(resultado.next()).thenThrow(new SQLException("conexion cortada", "08006"));
        doThrow(new SQLException("no se pudo cerrar", "08003")).when(resultado).close();

        assertTrue(cache.buscar(10.4, -75.5, "beach", 5000, 20).isEmpty());
    }

    // ------------------------------------------------------------ guardar

    @Test
    @DisplayName("guardar asigna la clave y la respuesta y ejecuta el insert")
    void guardar_ok_ejecutaInsert() throws SQLException {
        cache.guardar(10.4, -75.5, "beach", 5000, 20, 12);

        verificarClave();
        verify(consulta).setInt(6, 12);
        verify(consulta).executeUpdate();
    }

    @Test
    @DisplayName("si la base falla al guardar no lanza excepcion")
    void guardar_errorDeBase_noLanza() throws SQLException {
        when(consulta.executeUpdate()).thenThrow(new SQLException("tabla bloqueada", "55P03"));

        assertDoesNotThrow(() -> cache.guardar(10.4, -75.5, "beach", 5000, 20, 12));
    }
}

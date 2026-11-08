package com.wisetrip.datos;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import com.wisetrip.modelo.Usuario;
import com.wisetrip.modelo.UsuarioBuilder;
import com.wisetrip.negocio.IUsuarioBuilder;

/**
 * Pruebas unitarias de DirectorUsuario (patrón Builder, rol Director).
 *
 * La fila de la base de datos (ResultSet) se simula con Mockito: no se
 * conecta a PostgreSQL.
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@DisplayName("DirectorUsuario")
class DirectorUsuarioTest {

    @Mock
    private ResultSet fila;

    @BeforeEach
    void setUp() throws SQLException {
        when(fila.getInt("id_usuario")).thenReturn(4);
        when(fila.getString("nombre")).thenReturn("Ana Rodriguez");
        when(fila.getString("tipo_documento")).thenReturn("CC");
        when(fila.getString("numero_documento")).thenReturn("1012345678");
        when(fila.getDate("fecha_nacimiento")).thenReturn(Date.valueOf(LocalDate.of(2000, 5, 10)));
        when(fila.getString("correo")).thenReturn("ana@wisetrip.com");
        when(fila.getString("contraseña")).thenReturn("secreta123");
        when(fila.getString("rol")).thenReturn("cliente");
        when(fila.getString("chat_id")).thenReturn("555");
    }

    @Test
    @DisplayName("con el UsuarioBuilder real arma el Usuario de la fila")
    void construirDesdeConsulta_armaElUsuario() throws SQLException {
        UsuarioBuilder builder = new UsuarioBuilder();

        new DirectorUsuario(builder).construirDesdeConsulta(fila);
        Usuario usuario = builder.getResult();

        assertEquals(4, usuario.getIdUsuario());
        assertEquals("Ana Rodriguez", usuario.getNombreCompleto());
        assertEquals("1012345678", usuario.getNumeroDocumento());
        assertEquals("2000-05-10", usuario.getFechaNacimiento());
        assertEquals("ana@wisetrip.com", usuario.getCorreo());
        assertEquals("555", usuario.getChatId());
    }

    @Test
    @DisplayName("borde: sin fecha de nacimiento en la base queda en null")
    void sinFechaNacimiento_quedaNull() throws SQLException {
        when(fila.getDate("fecha_nacimiento")).thenReturn(null);
        UsuarioBuilder builder = new UsuarioBuilder();

        new DirectorUsuario(builder).construirDesdeConsulta(fila);

        assertNull(builder.getResult().getFechaNacimiento());
    }

    @Test
    @DisplayName("llama los pasos del Builder en el orden definido")
    void construirDesdeConsulta_respetaElOrden() throws SQLException {
        IUsuarioBuilder builder = mock(IUsuarioBuilder.class);

        new DirectorUsuario(builder).construirDesdeConsulta(fila);

        InOrder orden = inOrder(builder);
        orden.verify(builder).reiniciar();
        orden.verify(builder).construirIdentidad(4, "Ana Rodriguez");
        orden.verify(builder).construirDocumento("CC", "1012345678", "2000-05-10");
        orden.verify(builder).construirCredenciales("ana@wisetrip.com", "secreta123", "cliente");
        orden.verify(builder).construirContacto("555");
    }
}

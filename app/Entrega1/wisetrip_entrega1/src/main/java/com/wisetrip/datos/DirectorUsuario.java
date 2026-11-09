package com.wisetrip.datos;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.wisetrip.negocio.IUsuarioBuilder;

/**
 * PATRON BUILDER (GoF) - rol: Director.
 *
 * Orquesta el orden de construccion de un Usuario a partir de una fila
 * de la tabla "usuario". Trabaja solo con la interfaz IUsuarioBuilder:
 * no sabe que clase concreta lo arma ni como se ensambla por dentro.
 */
public class DirectorUsuario {

    private final IUsuarioBuilder builder;

    public DirectorUsuario(IUsuarioBuilder builder) {
        this.builder = builder;
    }

    /** Llama los pasos del Builder, en orden, con los datos de la consulta. */
    public void construirDesdeConsulta(ResultSet fila) throws SQLException {
        Date fechaNacimiento = fila.getDate("fecha_nacimiento");

        builder.reiniciar();
        builder.construirIdentidad(
                fila.getInt("id_usuario"),
                fila.getString("nombre"));
        builder.construirDocumento(
                fila.getString("tipo_documento"),
                fila.getString("numero_documento"),
                fechaNacimiento != null ? fechaNacimiento.toLocalDate().toString() : null);
        builder.construirCredenciales(
                fila.getString("correo"),
                fila.getString("contraseña"),
                fila.getString("rol"));
        builder.construirContacto(fila.getString("chat_id"));
    }
}

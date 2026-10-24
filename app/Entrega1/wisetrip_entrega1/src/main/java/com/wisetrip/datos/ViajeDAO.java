package com.wisetrip.datos;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.wisetrip.modelo.FechasViaje;
import com.wisetrip.modelo.RepartoPresupuesto;
import com.wisetrip.modelo.Ciudad;
import com.wisetrip.modelo.Presupuesto;
import com.wisetrip.modelo.ResultadoRecomendacion;
import com.wisetrip.modelo.Ubicacion;
import com.wisetrip.modelo.Usuario;
import com.wisetrip.modelo.Viaje;
import com.wisetrip.negocio.IRepositorioViajes;

/**
 * Guarda los viajes en PostgreSQL.
 * Implementa la interfaz de negocio IRepositorioViajes.
 * se separa el acceso a datos del dominio.
 */
//mejoras
//validar fecha_inicio >= fecha_actual (regla de negocio)
//Validar fecha_fin >= fecha_inicio (regla de negocio)
//Manejo de SQLException (unique, foreign key, check)
//Verificar que idUsuario e idCiudad existan en BD
//Agregar validación de duplicados (evitar viajes idénticos)
//Agregar logs de auditoría

@Repository
public class ViajeDAO implements IRepositorioViajes {

    private static final String CONSULTA_VIAJES = """
            SELECT v.id_viaje, v.destino, v.origen, v.fecha_inicio, v.fecha_fin, v.presupuesto,
                   c.id_ciudad, c.pais, c.latitud, c.longitud,
                   r.id_viaje AS id_reparto, r.hospedaje, r.alimentacion,
                   r.transporte, r.actividades, r.imprevistos
            FROM viajes v
            LEFT JOIN reparto_presupuesto r ON r.id_viaje = v.id_viaje
            LEFT JOIN ciudad c ON c.id_ciudad = (
                SELECT MIN(c2.id_ciudad) FROM ciudad c2
                WHERE c2.nombre = v.destino HAVING COUNT(*) = 1
            )
            WHERE v.id_usuario = ?
            """;

    @Override
    public List<Viaje> listarVigentes(Usuario usuario, LocalDate hoy) {
        validarUsuarioConsulta(usuario);
        if (hoy == null) throw new IllegalArgumentException("La fecha de consulta es obligatoria.");
        String sql = CONSULTA_VIAJES + " AND v.fecha_fin >= ? ORDER BY v.fecha_inicio, v.id_viaje";
        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, usuario.getIdUsuario());
            stmt.setDate(2, Date.valueOf(hoy));
            List<Viaje> viajes = new ArrayList<>();
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) viajes.add(leerViaje(rs, usuario));
            }
            return viajes;
        } catch (SQLException e) {
            throw new IllegalStateException("No se pudieron consultar los viajes del usuario.", e);
        }
    }

    @Override
    public Optional<Viaje> buscarPorId(int idViaje, Usuario usuario) {
        validarUsuarioConsulta(usuario);
        if (idViaje <= 0) throw new IllegalArgumentException("El ID del viaje debe ser positivo.");
        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(CONSULTA_VIAJES + " AND v.id_viaje = ?")) {
            stmt.setInt(1, usuario.getIdUsuario());
            stmt.setInt(2, idViaje);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() ? Optional.of(leerViaje(rs, usuario)) : Optional.empty();
            }
        } catch (SQLException e) {
            throw new IllegalStateException("No se pudo recuperar el viaje del usuario.", e);
        }
    }

    private void validarUsuarioConsulta(Usuario usuario) {
        if (usuario == null || usuario.getIdUsuario() <= 0) {
            throw new IllegalArgumentException("Se necesita un usuario registrado para consultar viajes.");
        }
    }

    private Viaje leerViaje(ResultSet rs, Usuario usuario) throws SQLException {
        Viaje viaje = new Viaje(usuario);
        FechasViaje fechas = new FechasViaje();
        fechas.setFechaInicio(rs.getDate("fecha_inicio").toLocalDate().toString());
        fechas.setFechaFin(rs.getDate("fecha_fin").toLocalDate().toString());
        viaje.setFechas(fechas);

        // La base guarda USD. Presupuesto espera coma como separador decimal.
        var monto = rs.getBigDecimal("presupuesto");
        viaje.definirPresupuesto(new Presupuesto(monto.toPlainString().replace('.', ','), "USD"),
                monto.doubleValue());

        String origen = rs.getString("origen");
        if (origen != null && !origen.isBlank()) {
            int separador = origen.lastIndexOf(", ");
            viaje.setOrigen(separador > 0
                    ? new Ubicacion(origen.substring(separador + 2), origen.substring(0, separador), null)
                    : new Ubicacion(null, origen, null));
        }

        Ciudad ciudad = new Ciudad();
        ciudad.setNombre(rs.getString("destino"));
        // No atribuye un pais o ID arbitrarios si el nombre corresponde a varias ciudades.
        if (rs.getObject("id_ciudad") != null) {
            ciudad.setId(rs.getInt("id_ciudad"));
            ciudad.setPais(rs.getString("pais"));
            ciudad.setLatitud(rs.getDouble("latitud"));
            ciudad.setLongitud(rs.getDouble("longitud"));
        }
        viaje.elegirDestino(new ResultadoRecomendacion(ciudad, 0, 0, 0, false, false));

        if (rs.getObject("id_reparto") != null) {
            RepartoPresupuesto reparto = new RepartoPresupuesto();
            reparto.setHospedaje(rs.getInt("hospedaje"));
            reparto.setAlimentacion(rs.getInt("alimentacion"));
            reparto.setTransporte(rs.getInt("transporte"));
            reparto.setActividades(rs.getInt("actividades"));
            reparto.setImprevistos(rs.getInt("imprevistos"));
            viaje.setReparto(reparto);
        }
        // definirPresupuesto reinicia el ID; restaurarlo al terminar la lectura.
        viaje.setIdViaje(rs.getInt("id_viaje"));
        return viaje;
    }

    @Override
    public void guardarReparto(int idViaje, int idUsuario, RepartoPresupuesto reparto) {
        if (idViaje <= 0 || idUsuario <= 0 || reparto == null || !reparto.esValido()) {
            throw new IllegalArgumentException("El viaje, el usuario y el reparto deben ser validos.");
        }
        String sql = """
                INSERT INTO reparto_presupuesto
                    (id_viaje, hospedaje, alimentacion, transporte, actividades, imprevistos)
                SELECT id_viaje, ?, ?, ?, ?, ? FROM viajes
                WHERE id_viaje = ? AND id_usuario = ?
                ON CONFLICT (id_viaje) DO UPDATE SET
                    hospedaje = EXCLUDED.hospedaje,
                    alimentacion = EXCLUDED.alimentacion,
                    transporte = EXCLUDED.transporte,
                    actividades = EXCLUDED.actividades,
                    imprevistos = EXCLUDED.imprevistos
                """;
        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, reparto.getHospedaje());
            stmt.setInt(2, reparto.getAlimentacion());
            stmt.setInt(3, reparto.getTransporte());
            stmt.setInt(4, reparto.getActividades());
            stmt.setInt(5, reparto.getImprevistos());
            stmt.setInt(6, idViaje);
            stmt.setInt(7, idUsuario);
            if (stmt.executeUpdate() != 1) {
                throw new IllegalStateException("No se encontro el viaje del usuario para guardar el reparto.");
            }
        } catch (SQLException e) {
            throw new IllegalStateException("No se pudo guardar el reparto en PostgreSQL.", e);
        }
    }

    @Override
    public int insertar(int idUsuario, int idCiudad, FechasViaje fechas, double presupuestoUsd, String origen) {
        validarDatos(idUsuario, idCiudad, fechas, presupuestoUsd);
        validarOrigen(origen);

        String sql = """
                INSERT INTO viajes (id_usuario, destino, fecha_inicio, fecha_fin, presupuesto, origen)
                SELECT ?, nombre, ?, ?, ?, ? FROM ciudad WHERE id_ciudad = ?
                RETURNING id_viaje
                """;

        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idUsuario);
            stmt.setDate(2, Date.valueOf(LocalDate.parse(fechas.getFechaInicio())));
            stmt.setDate(3, Date.valueOf(LocalDate.parse(fechas.getFechaFin())));
            stmt.setDouble(4, presupuestoUsd);
            stmt.setString(5, origen.trim());
            stmt.setInt(6, idCiudad);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id_viaje");
                }
            }
            throw new IllegalStateException("PostgreSQL no devolvió id_viaje.");
        } catch (SQLException e) {
            throw new IllegalStateException("No se pudo guardar el viaje en PostgreSQL.", e);
        }
    }
    @Override
    public void actualizar(int idViaje, int idUsuario, int idCiudad,
                           FechasViaje fechas, double presupuestoUsd, String origen) {
        if (idViaje <= 0) {
            throw new IllegalArgumentException("El ID del viaje debe ser positivo.");
        }
        validarDatos(idUsuario, idCiudad, fechas, presupuestoUsd);
        validarOrigen(origen);

        String sql = """
                UPDATE viajes
                SET destino = ciudad.nombre, fecha_inicio = ?, fecha_fin = ?, presupuesto = ?, origen = ?
                FROM ciudad
                WHERE viajes.id_viaje = ?
                  AND viajes.id_usuario = ?
                  AND ciudad.id_ciudad = ?
                """;

        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDate(1, Date.valueOf(LocalDate.parse(fechas.getFechaInicio())));
            stmt.setDate(2, Date.valueOf(LocalDate.parse(fechas.getFechaFin())));
            stmt.setDouble(3, presupuestoUsd);
            stmt.setString(4, origen.trim());
            stmt.setInt(5, idViaje);
            stmt.setInt(6, idUsuario);
            stmt.setInt(7, idCiudad);

            if (stmt.executeUpdate() != 1) {
                throw new IllegalStateException(
                        "No se encontro el viaje del usuario o la ciudad seleccionada.");
            }
        } catch (SQLException e) {
            throw new IllegalStateException("No se pudo actualizar el viaje en PostgreSQL.", e);
        }
    }

    private void validarOrigen(String origen) {
        if (origen == null || origen.isBlank() || origen.trim().length() > 200) {
            throw new IllegalArgumentException("El origen es obligatorio y debe tener como maximo 200 caracteres.");
        }
    }

    private void validarDatos(int idUsuario, int idCiudad, FechasViaje fechas, double presupuestoUsd) {
        if (idUsuario <= 0) {
            throw new IllegalArgumentException("El viaje debe estar asociado a un usuario con ID positivo.");
        }
        if (idCiudad <= 0) {
            throw new IllegalArgumentException("El destino debe tener un ID de ciudad positivo.");
        }
        if (fechas == null || !fechas.tieneRangoValido()) {
            throw new IllegalArgumentException("El viaje debe tener fechas validas y la fecha final debe ser posterior a la inicial.");
        }
        if (!Double.isFinite(presupuestoUsd) || presupuestoUsd <= 0) {
            throw new IllegalArgumentException("El presupuesto del viaje debe ser un numero finito mayor que cero.");
        }
    }
}

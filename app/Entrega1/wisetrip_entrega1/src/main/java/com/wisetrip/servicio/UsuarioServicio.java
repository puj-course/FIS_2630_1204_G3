package com.wisetrip.servicio;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.wisetrip.datos.UsuarioDAO;
import com.wisetrip.modelo.Usuario;

@Service
public class UsuarioServicio {

    private final UsuarioDAO usuarioDAO;

    public UsuarioServicio(UsuarioDAO usuarioDAO) {
        this.usuarioDAO = usuarioDAO;
    }

    public List<Usuario> listarUsuarios() {
        return new ArrayList<>();
    }

    public void registrar(Usuario usuario) {
        usuarioDAO.registrar(usuario);
    }

    public boolean existeCorreo(String correo) {
        return usuarioDAO.existeCorreo(correo);
    }

    public boolean existeDocumento(String numeroDocumento) {
        return usuarioDAO.existeDocumento(numeroDocumento);
    }

    public int calcularEdad(LocalDate fechaNacimiento) {
        return Period.between(fechaNacimiento, LocalDate.now()).getYears();
    }

    /**
     * Valida los datos del registro.
     * Devuelve un mapa vacio si todo esta bien, o con los mensajes de error.
     * Es la version en Java del objeto "errs" que tenias en RegisterScreen.
     */
    public Map<String, String> validarRegistro(Usuario u, String confirmarPassword) {
        Map<String, String> errores = new LinkedHashMap<>();

        // Nombre completo
        if (u.getNombreCompleto() == null || u.getNombreCompleto().isBlank()) {
            errores.put("nombreCompleto", "Ingresa tu nombre completo.");
        } else if (u.getNombreCompleto().trim().length() < 3) {
            errores.put("nombreCompleto", "El nombre es demasiado corto.");
        }

        // Tipo de documento
        if (u.getTipoDocumento() == null || u.getTipoDocumento().isBlank()) {
            errores.put("tipoDocumento", "Selecciona un tipo de documento.");
        }

        // Numero de documento
        if (u.getNumeroDocumento() == null || u.getNumeroDocumento().isBlank()) {
            errores.put("numeroDocumento", "Ingresa tu numero de documento.");
        } else if (!u.getNumeroDocumento().trim().matches("\\d{5,15}")) {
            errores.put("numeroDocumento", "El documento debe tener entre 5 y 15 digitos.");
        } else if (existeDocumento(u.getNumeroDocumento())) {
            errores.put("numeroDocumento", "Ya existe una cuenta con este documento.");
        }

        // Fecha de nacimiento
        if (u.getFechaNacimiento() == null || u.getFechaNacimiento().isBlank()) {
            errores.put("fechaNacimiento", "Ingresa tu fecha de nacimiento.");
        } else {
            try {
                LocalDate nacimiento = LocalDate.parse(u.getFechaNacimiento());
                int edad = calcularEdad(nacimiento);
                if (nacimiento.isAfter(LocalDate.now())) {
                    errores.put("fechaNacimiento", "La fecha de nacimiento no puede ser futura.");
                } else if (edad < 18) {
                    errores.put("fechaNacimiento", "Debes ser mayor de 18 anios para crear una cuenta en WiseTrip.");
                } else if (edad > 120) {
                    errores.put("fechaNacimiento", "Ingresa una fecha de nacimiento valida.");
                }
            } catch (Exception e) {
                errores.put("fechaNacimiento", "Ingresa una fecha valida.");
            }
        }

        // Correo
        if (u.getCorreo() == null || u.getCorreo().isBlank()) {
            errores.put("correo", "Ingresa tu correo electronico.");
        } else if (!u.getCorreo().trim().matches("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$")) {
            errores.put("correo", "El formato del correo no es valido.");
        } else if (existeCorreo(u.getCorreo())) {
            errores.put("correo", "Ya existe una cuenta con este correo.");
        }

        // Contrasena
        if (u.getPassword() == null || u.getPassword().isEmpty()) {
            errores.put("password", "Ingresa una contrasena.");
        } else if (u.getPassword().length() < 6) {
            errores.put("password", "La contrasena debe tener al menos 6 caracteres.");
        }

        // Confirmacion
        if (confirmarPassword == null || confirmarPassword.isEmpty()) {
            errores.put("confirmarPassword", "Confirma tu contrasena.");
        } else if (u.getPassword() != null && !u.getPassword().equals(confirmarPassword)) {
            errores.put("confirmarPassword", "Las contrasenas no coinciden.");
        }

        return errores;
    }
        /**
     * Busca un usuario con ese correo y contrasena.
     * Devuelve null si no existe o si la contrasena no coincide.
     */
    public Usuario autenticar(String correo, String password) {
        if (correo == null || password == null) return null;
        Usuario usuario = usuarioDAO.buscarPorCorreo(correo.trim());
        if (usuario == null || !usuario.getPassword().equals(password)) {
            return null;
        }
        return usuario;
    }
}

package com.wisetrip.modelo;

import java.time.LocalDate;
import java.time.Period;
import java.util.LinkedHashMap;
import java.util.Map;


//mejoras pendientes

//agregar validacion con not null, email y size 
//no exponer la contraseña en tostring()
//agregar metodo normalizarcorreo para que todo se guarde en minusculas
//agregar campos: estado, fecha registro, ultimo acceso

// Modelo que representa la información de un usuario de WiseTrip
public class Usuario {

    public static final int EDAD_MINIMA = 18;
    public static final int EDAD_MAXIMA = 120;
    public static final int LARGO_MINIMO_PASSWORD = 6;

    private int idUsuario;
    private String nombreCompleto;
    private String tipoDocumento;
    private String numeroDocumento;
    private String fechaNacimiento;
    private String correo;
    private String password;
    private String rol = "cliente";
    private String chatId;

    // Constructor vacío necesario para que Spring pueda
    // crear y llenar el objeto con los datos del formulario
    public Usuario() {
    }

    //GETTERS Y SETTERS
    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public String getTipoDocumento() {
        return tipoDocumento;
    }


    // Modifica el tipo de documento

    public void setTipoDocumento(String tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    public String getNumeroDocumento() {
        return numeroDocumento;
    }

    public void setNumeroDocumento(String numeroDocumento) {
        this.numeroDocumento = numeroDocumento;
    }

    public String getFechaNacimiento() {
        return fechaNacimiento;
    }

    
    // Modifica la fecha de nacimiento
    public void setFechaNacimiento(String fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public String getCorreo() {
        return correo;
    }
    public void setCorreo(String correo) {
        this.correo = correo != null ? correo.trim().toLowerCase() : null;
    }


    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public String getChatId() {
        return chatId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

    //METODOS 

    //limpia los espacios y deja el correo en minusculas
    public void normalizar(){
        if (nombreCompleto != null) {
            nombreCompleto = nombreCompleto.trim();
        }
        if (numeroDocumento != null) {
            numeroDocumento = numeroDocumento.trim();
        }
        normalizarCorreo();
    }


    public void normalizarCorreo() {
        if (this.correo != null) {
            this.correo = this.correo.trim().toLowerCase();
        }
    }

    public Map<String, String> validarDatos(String confirmarPassword) {
        Map<String, String> errores = new LinkedHashMap<>();
        // Validación de campos obligatorios

        //Nombre completo
        if (nombreCompleto == null || nombreCompleto.isBlank()) {
            errores.put("nombreCompleto", "Ingresa tu nombre completo.");
        } else if (nombreCompleto.trim().length() < 3) {
            errores.put("nombreCompleto", "El nombre es demasiado corto.");
        }

        //Tipo de documento
        if (tipoDocumento == null || tipoDocumento.isBlank()) {
            errores.put("tipoDocumento", "Selecciona un tipo de documento.");
        }

        //Número de documento
        if (numeroDocumento == null || numeroDocumento.isBlank()) {
            errores.put("numeroDocumento", "Ingresa tu numero de documento.");
        } else if (!numeroDocumento.trim().matches("\\d{5,15}")) {
            errores.put("numeroDocumento", "El documento debe tener entre 5 y 15 digitos.");
        }

        //Fecha de nacimiento
        if (fechaNacimiento == null || fechaNacimiento.isBlank()) {
            errores.put("fechaNacimiento", "Ingresa tu fecha de nacimiento.");
        } else {
            try {
                LocalDate nacimiento = LocalDate.parse(fechaNacimiento);
                int edad = calcularEdad();
                if (nacimiento.isAfter(LocalDate.now())) {
                    errores.put("fechaNacimiento", "La fecha de nacimiento no puede ser futura.");
                } else if (edad < EDAD_MINIMA) {
                    errores.put("fechaNacimiento", "Debes ser mayor de 18 anios para crear una cuenta en WiseTrip.");
                } else if (edad > EDAD_MAXIMA) {
                    errores.put("fechaNacimiento", "Ingresa una fecha de nacimiento valida.");
                }
            } catch (Exception e) {
                errores.put("fechaNacimiento", "Ingresa una fecha valida.");
            }
        }

        //Correo
        if (correo == null || correo.isBlank()) {
            errores.put("correo", "Ingresa tu correo electronico.");
        } else if (!correo.trim().matches("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$")) {
            errores.put("correo", "El formato del correo no es valido.");
        }

        //Password
        if (password == null || password.isEmpty()) {
            errores.put("password", "Ingresa una contrasena.");
        } else if (password.length() < LARGO_MINIMO_PASSWORD) {
            errores.put("password", "La contrasena debe tener al menos 6 caracteres.");
        }

        //Confirmar password
        if (confirmarPassword == null || confirmarPassword.isEmpty()) {
            errores.put("confirmarPassword", "Confirma tu contrasena.");
        } else if (password != null && !password.equals(confirmarPassword)) {
            errores.put("confirmarPassword", "Las contrasenas no coinciden.");
        }

        return errores;
    }
    

    //calcular edad actual
    public int calcularEdad() {
        return Period.between(LocalDate.parse(fechaNacimiento), LocalDate.now()).getYears();
    }

    // verifica la contraseña, si es true coincide con la dada por el usuario
    public boolean verificarPassword(String passwordIngresada) {
        return password != null && password.equals(passwordIngresada);
    }

    //asocia el chat de telegram con el usuario
    public void vincularTelegram(String nuevoChatId) {
        this.chatId = nuevoChatId != null ? nuevoChatId.trim() : null;
    }

    // retorna true si tiene el telegram vinculado, false si no

    public boolean tieneTelegramVinculado() {
        return chatId != null && !chatId.isBlank();
    }

    // saca solo el primer nombre del usuario
    public String primerNombre() {
        return nombreCompleto.trim().split("\\s+")[0];
    }


}

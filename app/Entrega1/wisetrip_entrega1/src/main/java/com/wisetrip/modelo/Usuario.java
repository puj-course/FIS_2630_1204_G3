package com.wisetrip.modelo;

// Modelo que representa la información de un usuario de WiseTrip
public class Usuario {

    // Datos personales y de acceso del usuario
    private String nombreCompleto;
    private String tipoDocumento;
    private String numeroDocumento;
    private String fechaNacimiento;
    private String correo;
    private String password;

    // Constructor vacío necesario para que Spring pueda
    // crear y llenar el objeto con los datos del formulario
    public Usuario() {
    }

    // Obtiene el nombre completo
    public String getNombreCompleto() {
        return nombreCompleto;
    }

    // Modifica el nombre completo
    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    // Obtiene el tipo de documento
    public String getTipoDocumento() {
        return tipoDocumento;
    }

    // Modifica el tipo de documento
    public void setTipoDocumento(String tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    // Obtiene el número de documento
    public String getNumeroDocumento() {
        return numeroDocumento;
    }

    // Modifica el número de documento
    public void setNumeroDocumento(String numeroDocumento) {
        this.numeroDocumento = numeroDocumento;
    }

    // Obtiene la fecha de nacimiento
    public String getFechaNacimiento() {
        return fechaNacimiento;
    }

    // Modifica la fecha de nacimiento
    public void setFechaNacimiento(String fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    // Obtiene el correo electrónico
    public String getCorreo() {
        return correo;
    }

    // Modifica el correo electrónico
    public void setCorreo(String correo) {
        this.correo = correo;
    }

    // Obtiene la contraseña
    public String getPassword() {
        return password;
    }

    // Modifica la contraseña
    public void setPassword(String password) {
        this.password = password;
    }
}

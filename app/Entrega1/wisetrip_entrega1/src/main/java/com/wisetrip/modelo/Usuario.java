package com.wisetrip.modelo;

public class Usuario {

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
    public int getIdUsuario() { return idUsuario; }
    public void setIdUsuario(int idUsuario) { this.idUsuario = idUsuario; }

    public String getNombreCompleto() { return nombreCompleto; }
    public void setNombreCompleto(String nombreCompleto) { this.nombreCompleto = nombreCompleto; }

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

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getRol() { return rol; }
    public void setRol(String rol) { this.rol = rol; }
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

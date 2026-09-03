package com.wisetrip.datos;

public class pruebaconexion {
    public static void main(String[] args) {
        try {
            var conexion = ConexionBD.obtenerConexion();
            System.out.println("Conexión exitosa: " + conexion);
            conexion.close();
        } catch (Exception e) {
            System.out.println("Falló la conexión: " + e.getMessage());
        }
    }
}
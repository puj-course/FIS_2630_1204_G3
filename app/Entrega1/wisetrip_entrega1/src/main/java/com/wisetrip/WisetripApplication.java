package com.wisetrip;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// Indica que esta es la clase principal de la aplicación Spring Boot.
// También permite que Spring configure y detecte automáticamente
// los controladores, servicios y demás componentes del proyecto.
@SpringBootApplication
public class WisetripApplication {

    // Método principal: es el punto de inicio de la aplicación.
    public static void main(String[] args) {

        // Inicia Spring Boot y levanta la aplicación junto con
        // el servidor web embebido.
        SpringApplication.run(WisetripApplication.class, args);
    }

}

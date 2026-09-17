package com.wisetrip;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Propuesta de HU: checklist de equipaje.
 * Un solo archivo: no usa JSP ni vistas separadas, la pantalla se
 * devuelve directo como HTML desde el controlador. Es solo para
 * mostrar cómo se vería la pantalla, no hay persistencia real
 * (el marcar/desmarcar es visual, con JS del propio navegador).
 */

@SpringBootApplication
public class ChecklistMockupApplication {

    public static void main(String[] args) {
        SpringApplication.run(ChecklistMockupApplication.class, args);
    }

    @Controller
    static class ChecklistControlador {

        @GetMapping("/")
        @ResponseBody
        String mostrarChecklist() {
            return PAGINA;
        }

        private static final String PAGINA = """
<!--
    Checklist de equipaje — solo pantalla (mockup en Spring Boot).
    El marcar/desmarcar es solo visual (JS del propio navegador),
    no hay persistencia real. Usa la misma paleta y tipografía
    que el resto de WiseTrip.
-->
<!DOCTYPE html>

package com.wisetrip.controlador;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.ui.ConcurrentModel;
import org.springframework.ui.Model;

/** Pruebas unitarias de InicioControlador (pagina de inicio / landing). */
@DisplayName("InicioControlador")
class InicioControladorTest {

    @Test
    @DisplayName("GET / muestra la landing con el mensaje de bienvenida")
    void mostrarLanding_devuelveLaVistaConElMensaje() {
        Model model = new ConcurrentModel();

        String vista = new InicioControlador().mostrarLanding(model);

        assertEquals("landing", vista);
        assertEquals("Planea tu viaje sin perder el control de tu presupuesto",
                model.getAttribute("mensaje"));
    }
}

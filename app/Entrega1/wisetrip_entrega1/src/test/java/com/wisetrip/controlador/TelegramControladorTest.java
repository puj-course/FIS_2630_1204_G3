package com.wisetrip.controlador;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.ui.ConcurrentModel;
import org.springframework.web.servlet.mvc.support.RedirectAttributesModelMap;

import com.wisetrip.modelo.Usuario;
import com.wisetrip.negocio.IPerfilUsuario;

@ExtendWith(MockitoExtension.class)
 // LENIENT: Mockito no marca error por when sin usar.
@MockitoSettings(strictness = Strictness.LENIENT)
@DisplayName("TelegramControlador")
class TelegramControladorTest {

    @Mock
    private IPerfilUsuario perfil;

    private TelegramControlador controlador;
    private MockHttpSession sesion;
    private ConcurrentModel model;
    private RedirectAttributesModelMap flash;

    // antes de cada test se crea un controlador nuevo y una sesión nueva

    @BeforeEach
    void setUp() {
        controlador = new TelegramControlador(perfil);
        sesion = new MockHttpSession();
        model = new ConcurrentModel();
        flash = new RedirectAttributesModelMap();
    }

    @Test
    @DisplayName("mostrarVinculacion sin usuario activo redirige al login")
    void mostrarVinculacion_sinUsuario_redirigeAlLogin() {
        // La sesión está vacía: nadie ha iniciado sesión
        assertEquals("redirect:/login", controlador.mostrarVinculacion(sesion, model));
    }

    @Test
    @DisplayName("mostrarVinculacion con usuario muestra la vista con su chat actual")
    void mostrarVinculacion_conUsuario_muestraLaVista() {
         // Usuario que ya tenía un chat de Telegram vinculado
        Usuario usuario = new Usuario();
        usuario.setChatId("12345");
        sesion.setAttribute("usuarioActivo", usuario);

        String vista = controlador.mostrarVinculacion(sesion, model);

        // Devuelve el nombre de la vista (la página) que se va a mostrar
        assertEquals("vincular-telegram", vista);
         // El chat actual queda en el modelo para que la página lo muestre
        assertEquals("12345", model.getAttribute("chatIdActual"));
    }

    @Test
    @DisplayName("procesarVinculacion sin usuario activo redirige al login y no vincula")
    void procesarVinculacion_sinUsuario_redirigeAlLogin() {
        String vista = controlador.procesarVinculacion("12345", sesion, flash);

        assertEquals("redirect:/login", vista);
        // Sin usuario no se debe tocar el servicio: no hay a quién vincularle el chat
        verifyNoInteractions(perfil);
    }

    @Test
    @DisplayName("procesarVinculacion con usuario vincula el chat, avisa y redirige")
    void procesarVinculacion_conUsuario_vinculaElChat() {
        Usuario usuario = new Usuario();
        sesion.setAttribute("usuarioActivo", usuario);

        String vista = controlador.procesarVinculacion("12345", sesion, flash);

        // Redirige a la página de perfil de Telegram, con el chat vinculado y un mensaje de éxito
        assertEquals("redirect:/perfil/telegram", vista);
        verify(perfil).vincularTelegram(usuario, "12345");
        assertSame(usuario, sesion.getAttribute("usuarioActivo"));
        assertEquals("¡Telegram vinculado con éxito!", flash.getFlashAttributes().get("mensaje"));
    }
}

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
@MockitoSettings(strictness = Strictness.LENIENT)
@DisplayName("TelegramControlador")
class TelegramControladorTest {

    @Mock
    private IPerfilUsuario perfil;

    private TelegramControlador controlador;
    private MockHttpSession sesion;
    private ConcurrentModel model;
    private RedirectAttributesModelMap flash;

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
        assertEquals("redirect:/login", controlador.mostrarVinculacion(sesion, model));
    }

    @Test
    @DisplayName("mostrarVinculacion con usuario muestra la vista con su chat actual")
    void mostrarVinculacion_conUsuario_muestraLaVista() {
        Usuario usuario = new Usuario();
        usuario.setChatId("12345");
        sesion.setAttribute("usuarioActivo", usuario);

        String vista = controlador.mostrarVinculacion(sesion, model);

        assertEquals("vincular-telegram", vista);
        assertEquals("12345", model.getAttribute("chatIdActual"));
    }

    @Test
    @DisplayName("procesarVinculacion sin usuario activo redirige al login y no vincula")
    void procesarVinculacion_sinUsuario_redirigeAlLogin() {
        String vista = controlador.procesarVinculacion("12345", sesion, flash);

        assertEquals("redirect:/login", vista);
        verifyNoInteractions(perfil);
    }

    @Test
    @DisplayName("procesarVinculacion con usuario vincula el chat, avisa y redirige")
    void procesarVinculacion_conUsuario_vinculaElChat() {
        Usuario usuario = new Usuario();
        sesion.setAttribute("usuarioActivo", usuario);

        String vista = controlador.procesarVinculacion("12345", sesion, flash);

        assertEquals("redirect:/perfil/telegram", vista);
        verify(perfil).vincularTelegram(usuario, "12345");
        assertSame(usuario, sesion.getAttribute("usuarioActivo"));
        assertEquals("¡Telegram vinculado con éxito!", flash.getFlashAttributes().get("mensaje"));
    }
}

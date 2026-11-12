package com.wisetrip.servicio;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import com.wisetrip.datos.CiudadSemilla;
import com.wisetrip.modelo.Ciudad;
import com.wisetrip.modelo.Importancia;
import com.wisetrip.negocio.CatalogoPreguntas;
import com.wisetrip.negocio.ProovedorLugares;

/**
 * Pruebas unitarias de LlenarAtributosCiudad.
 *
 * El proveedor de lugares (Geoapify) se simula con Mockito: las pruebas no
 * salen a internet. Se revisa que la ciudad quede con los atributos manuales
 * de la semilla mas los atributos automaticos calculados con el conteo.
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@DisplayName("LlenarAtributosCiudad")
class LlenarAtributosCiudadTest {

    private static final int RADIO_METROS = 50_000;

    @Mock
    private ProovedorLugares geo;

    private LlenarAtributosCiudad llenador;
    private Ciudad ciudad;

    @BeforeEach
    void setUp() {
        llenador = new LlenarAtributosCiudad(geo);
        ciudad = new Ciudad();
        ciudad.setLatitud(10.4);
        ciudad.setLongitud(-75.5);
    }

    private static CiudadSemilla semilla(Map<String, Boolean> manuales) {
        return new CiudadSemilla("Cartagena", "Colombia", 10.4, -75.5, manuales);
    }

    @Test
    @DisplayName("si un atributo manual indispensable no se cumple, no consulta Geoapify")
    void enriquecer_indispensableManualIncumplido_noConsulta() {
        Map<String, Boolean> manuales = Map.of("montana", false, "romantico", true);

        llenador.enriquecer(ciudad, semilla(manuales), null, Map.of("montana", Importancia.SI));

        verifyNoInteractions(geo);
        assertEquals(manuales, ciudad.getAtributos());
    }

    @Test
    @DisplayName("sin preferencias del usuario consulta todos los atributos automaticos")
    void enriquecer_sinPreferencias_consultaTodosLosAuto() {
        when(geo.contarLugares(anyDouble(), anyDouble(), anyString(), anyInt(), anyInt())).thenReturn(1_000);

        llenador.enriquecer(ciudad, semilla(Map.of("montana", true)), null, Map.of());

        int totalAuto = CatalogoPreguntas.atributosAuto().size();
        verify(geo, times(totalAuto)).contarLugares(anyDouble(), anyDouble(), anyString(), anyInt(), anyInt());
        Map<String, Boolean> atributos = ciudad.getAtributos();
        assertTrue(atributos.get("montana"), "conserva el atributo manual de la semilla");
        assertTrue(atributos.get("playa"));
        assertTrue(atributos.get("museos"));
        assertEquals(totalAuto + 1, atributos.size());
    }

    @Test
    @DisplayName("solo consulta los atributos automaticos que el usuario marco")
    void enriquecer_conPreferencias_soloConsultaLosMarcados() {
        when(geo.contarLugares(anyDouble(), anyDouble(), anyString(), anyInt(), anyInt())).thenReturn(9);
        Map<String, Boolean> elegidos = new HashMap<>();
        elegidos.put("playa", true);
        elegidos.put("museos", false);

        llenador.enriquecer(ciudad, semilla(Map.of()), elegidos, Map.of());

        // playa: categoria "beach" y umbral 5 en el catalogo
        verify(geo).contarLugares(10.4, -75.5, "beach", RADIO_METROS, 5);
        verify(geo, times(1)).contarLugares(anyDouble(), anyDouble(), anyString(), anyInt(), anyInt());
        assertEquals(Map.of("playa", true), ciudad.getAtributos());
    }

    @Test
    @DisplayName("si el conteo no alcanza el umbral el atributo queda en false")
    void enriquecer_conteoBajoUmbral_false() {
        when(geo.contarLugares(anyDouble(), anyDouble(), eq("beach"), anyInt(), anyInt())).thenReturn(4);

        llenador.enriquecer(ciudad, semilla(Map.of()), Map.of("playa", true), Map.of());

        assertFalse(ciudad.getAtributos().get("playa"));
    }

    @Test
    @DisplayName("borde: el conteo exacto del umbral cuenta como cumplido")
    void enriquecer_conteoIgualAlUmbral_true() {
        when(geo.contarLugares(anyDouble(), anyDouble(), eq("beach"), anyInt(), anyInt())).thenReturn(5);

        llenador.enriquecer(ciudad, semilla(Map.of()), Map.of("playa", true), Map.of());

        assertTrue(ciudad.getAtributos().get("playa"));
    }

    @Test
    @DisplayName("si Geoapify no responde, el atributo se quita (no sabemos) en vez de quedar en false")
    void enriquecer_sinRespuesta_quitaElAtributo() {
        when(geo.contarLugares(anyDouble(), anyDouble(), anyString(), anyInt(), anyInt())).thenReturn(null);

        llenador.enriquecer(ciudad, semilla(Map.of("playa", true, "montana", true)),
                Map.of("playa", true), Map.of());

        assertFalse(ciudad.getAtributos().containsKey("playa"));
        assertTrue(ciudad.getAtributos().get("montana"));
    }

    @Test
    @DisplayName("un indispensable manual que si se cumple o que no se conoce no detiene el calculo")
    void enriquecer_indispensableCumplidoODesconocido_sigue() {
        when(geo.contarLugares(anyDouble(), anyDouble(), anyString(), anyInt(), anyInt())).thenReturn(100);

        llenador.enriquecer(ciudad, semilla(Map.of("montana", true)), Map.of("playa", true),
                Map.of("montana", Importancia.SI, "nieve", Importancia.SI));

        verify(geo).contarLugares(anyDouble(), anyDouble(), eq("beach"), anyInt(), anyInt());
        assertTrue(ciudad.getAtributos().get("playa"));
    }

    @Test
    @DisplayName("un indispensable automatico en false no detiene el calculo (solo aplica a manuales)")
    void enriquecer_indispensableAutomatico_noDetiene() {
        when(geo.contarLugares(anyDouble(), anyDouble(), anyString(), anyInt(), anyInt())).thenReturn(100);

        llenador.enriquecer(ciudad, semilla(Map.of("playa", false)), Map.of("playa", true),
                Map.of("playa", Importancia.SI));

        verify(geo).contarLugares(anyDouble(), anyDouble(), eq("beach"), anyInt(), anyInt());
        assertTrue(ciudad.getAtributos().get("playa"));
    }

    @Test
    @DisplayName("un manual en false que no es indispensable no detiene el calculo")
    void enriquecer_manualFalsoNoIndispensable_sigue() {
        when(geo.contarLugares(anyDouble(), anyDouble(), anyString(), anyInt(), anyInt())).thenReturn(100);

        llenador.enriquecer(ciudad, semilla(Map.of("montana", false)), Map.of("playa", true),
                Map.of("montana", Importancia.PREFIERO));

        verify(geo).contarLugares(anyDouble(), anyDouble(), eq("beach"), anyInt(), anyInt());
        assertTrue(ciudad.getAtributos().get("playa"));
        assertFalse(ciudad.getAtributos().get("montana"));
    }
}

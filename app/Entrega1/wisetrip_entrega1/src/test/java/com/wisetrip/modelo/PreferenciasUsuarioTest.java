package com.wisetrip.modelo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("PreferenciasUsuario")
class PreferenciasUsuarioTest {

    @Test
    @DisplayName("el constructor vacío no tiene atributos, presupuesto ni duración")
    void constructorVacio() {
        PreferenciasUsuario preferencias = new PreferenciasUsuario();

        assertTrue(preferencias.getAtributos().isEmpty());
        assertNull(preferencias.getPresupuestoUsd());
        assertEquals(0L, preferencias.getDuracionDias());
    }

    @Test
    @DisplayName("el constructor completo guarda presupuesto, duración y atributos")
    void constructorCompleto() {
        PreferenciasUsuario preferencias = new PreferenciasUsuario(
                800.0, Map.of("playa", Importancia.PREFIERO), 5);

        assertEquals(800.0, preferencias.getPresupuestoUsd(), 0.0001);
        assertEquals(5L, preferencias.getDuracionDias());
        assertEquals(Importancia.PREFIERO, preferencias.getAtributos().get("playa"));
    }

    @Test
    @DisplayName("el constructor con atributos null deja el mapa vacío")
    void constructorConAtributosNull() {
        PreferenciasUsuario preferencias = new PreferenciasUsuario((Map<String, Importancia>) null);

        assertTrue(preferencias.getAtributos().isEmpty());
    }

    @Test
    @DisplayName("setAtributos descarta las preguntas de perfil y las desconocidas")
    void setAtributos_filtraPerfilYDesconocidas() {
        Map<String, Importancia> origen = new LinkedHashMap<>();
        origen.put("playa", Importancia.GUSTAR);          // pregunta de atributo: se conserva
        origen.put("tiempo_libre", Importancia.SI);       // pregunta de perfil: se descarta
        origen.put("no_existe", Importancia.SI);          // no está en el catálogo: se descarta
        PreferenciasUsuario preferencias = new PreferenciasUsuario();

        preferencias.setAtributos(origen);

        assertEquals(1, preferencias.getAtributos().size());
        assertTrue(preferencias.getAtributos().containsKey("playa"));
    }

    @Test
    @DisplayName("setAtributos con null limpia los atributos")
    void setAtributos_null_limpia() {
        PreferenciasUsuario preferencias = new PreferenciasUsuario(Map.of("playa", Importancia.GUSTAR));

        preferencias.setAtributos(null);

        assertTrue(preferencias.getAtributos().isEmpty());
    }

    @Test
    @DisplayName("tienePreferenciasPuntuables es true solo con gustar o prefiero")
    void tienePreferenciasPuntuables() {
        assertTrue(new PreferenciasUsuario(Map.of("playa", Importancia.GUSTAR)).tienePreferenciasPuntuables());
        assertTrue(new PreferenciasUsuario(Map.of("playa", Importancia.PREFIERO)).tienePreferenciasPuntuables());
        assertFalse(new PreferenciasUsuario(Map.of("playa", Importancia.SI)).tienePreferenciasPuntuables());
        assertFalse(new PreferenciasUsuario(Map.of("playa", Importancia.NO)).tienePreferenciasPuntuables());
        assertFalse(new PreferenciasUsuario().tienePreferenciasPuntuables());
    }

    @Test
    @DisplayName("borde: una pregunta sin respuesta (importancia null) no puntúa ni cuenta como preferencia")
    void importanciaNula_noPuntuaNiCuenta() {
        Map<String, Importancia> origen = new LinkedHashMap<>();
        origen.put("playa", null);
        PreferenciasUsuario soloNula = new PreferenciasUsuario(origen);

        assertFalse(soloNula.tienePreferenciasPuntuables());
        assertFalse(soloNula.tienePreferencia("playa"));
        assertFalse(soloNula.esIndispensable("playa"));

        origen.put("museos", Importancia.GUSTAR);
        assertTrue(new PreferenciasUsuario(origen).tienePreferenciasPuntuables(),
                "una respuesta null no impide que otra sí puntúe");
    }

    @Test
    @DisplayName("tienePreferencia es true para todo menos no y lo que no se respondió")
    void tienePreferencia() {
        PreferenciasUsuario preferencias = new PreferenciasUsuario(
                Map.of("playa", Importancia.GUSTAR, "lujo", Importancia.NO));

        assertTrue(preferencias.tienePreferencia("playa"));
        assertFalse(preferencias.tienePreferencia("lujo"));
        assertFalse(preferencias.tienePreferencia("museos"));
    }

    @Test
    @DisplayName("esIndispensable es true solo para el nivel si")
    void esIndispensable() {
        PreferenciasUsuario preferencias = new PreferenciasUsuario(
                Map.of("playa", Importancia.SI, "lujo", Importancia.PREFIERO));

        assertTrue(preferencias.esIndispensable("playa"));
        assertFalse(preferencias.esIndispensable("lujo"));
        assertFalse(preferencias.esIndispensable("museos"));
    }
}

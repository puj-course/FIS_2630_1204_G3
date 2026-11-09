package com.wisetrip.negocio;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.wisetrip.modelo.TipoAtributo;

@DisplayName("CatalogoPreguntas")
class CatalogoPreguntasTest {

    @Test
    @DisplayName("todasLasPreguntas devuelve el catálogo completo")
    void todasLasPreguntas_devuelveElCatalogo() {
        assertSame(CatalogoPreguntas.TODAS, CatalogoPreguntas.todasLasPreguntas());
        assertFalse(CatalogoPreguntas.todasLasPreguntas().isEmpty());
    }

    @Test
    @DisplayName("los identificadores de las preguntas no se repiten")
    void identificadores_sonUnicos() {
        Set<String> ids = new HashSet<>();
        for (DefPregunta pregunta : CatalogoPreguntas.TODAS) {
            assertTrue(ids.add(pregunta.id), "Id repetido: " + pregunta.id);
        }
    }

    @Test
    @DisplayName("porId encuentra una pregunta existente")
    void porId_existente_laDevuelve() {
        DefPregunta playa = CatalogoPreguntas.porId("playa");

        assertEquals("playa", playa.id);
        assertEquals(TipoAtributo.AUTO, playa.tipo);
    }

    @Test
    @DisplayName("porId devuelve null si la pregunta no existe")
    void porId_inexistente_devuelveNull() {
        assertNull(CatalogoPreguntas.porId("no_existe"));
    }

    @Test
    @DisplayName("porIdObligatorio lanza IllegalArgumentException si no existe")
    void porIdObligatorio_inexistente_lanzaExcepcion() {
        IllegalArgumentException error = assertThrows(IllegalArgumentException.class,
                () -> CatalogoPreguntas.porIdObligatorio("no_existe"));

        assertTrue(error.getMessage().contains("no_existe"));
    }

    @Test
    @DisplayName("porIdObligatorio devuelve la pregunta si existe")
    void porIdObligatorio_existente_laDevuelve() {
        assertEquals("golf", CatalogoPreguntas.porIdObligatorio("golf").id);
    }

    @Test
    @DisplayName("atributosAuto devuelve solo las preguntas de tipo AUTO")
    void atributosAuto_soloTipoAuto() {
        List<DefPregunta> auto = CatalogoPreguntas.atributosAuto();

        assertFalse(auto.isEmpty());
        assertTrue(auto.stream().allMatch(p -> p.tipo == TipoAtributo.AUTO));
    }

    @Test
    @DisplayName("esManual y esDePerfil distinguen el tipo de la pregunta")
    void esManualYEsDePerfil_segunElTipo() {
        DefPregunta manual = CatalogoPreguntas.porIdObligatorio("montana");
        DefPregunta perfil = CatalogoPreguntas.porIdObligatorio("tiempo_libre");
        DefPregunta auto = CatalogoPreguntas.porIdObligatorio("playa");

        assertTrue(manual.esManual());
        assertFalse(manual.esDePerfil());
        assertTrue(perfil.esDePerfil());
        assertFalse(perfil.esManual());
        assertFalse(auto.esManual());
        assertFalse(auto.esDePerfil());
    }
}

package com.wisetrip.servicio;

import com.wisetrip.datos.DatosCiudades;
import com.wisetrip.modelo.Ciudad;
import com.wisetrip.modelo.Importancia;
import com.wisetrip.modelo.PreferenciasUsuario;
import com.wisetrip.negocio.CatalogoPreguntas;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import com.wisetrip.negocio.ProovedorLugares;

class RequisitosDestinoTest {
    private final RecomendadorDestinos recomendador =
            new RecomendadorDestinos(true, new PreferenciasServicio());

    @Test
    void perfilIndispensableNoBloqueaNiPuntuaDestinos() {
        var preferencias = new PreferenciasUsuario(100.0,
                Map.of("tiempo_libre", Importancia.si,
                       "restricciones_alimentarias", Importancia.prefiero), 1);
        Ciudad ciudad = new Ciudad();
        ciudad.setCostoPromedio(50);
        assertFalse(preferencias.tienePreferenciasPuntuables());
        assertEquals(List.of(ciudad), recomendador.validarCiudades(List.of(ciudad), preferencias).validas());
    }

    @Test
    void manualAusenteDescartaEnLugarDeQuedarPendiente() {
        var semilla = DatosCiudades.porNombreYPais("Asunción", "Paraguay");
        var ciudad = semilla.aCiudad(50);
        var geo = mock(ProovedorLugares.class);
        new LlenarAtributosCiudad(geo).enriquecer(ciudad, semilla,
                Map.of("nieve", true), Map.of("nieve", Importancia.si));
        var validacion = recomendador.validarCiudades(List.of(ciudad),
                new PreferenciasUsuario(100.0, Map.of("nieve", Importancia.si), 1));
        assertEquals(List.of(ciudad), validacion.descartadas());
        assertTrue(validacion.pendientes().isEmpty());
        verifyNoInteractions(geo);
    }

    @Test
    void automaticoSinRespuestaSiguePendienteYUsaProxy() {
        var real = mock(ServicioGeoapify.class);
        var playa = CatalogoPreguntas.porIdObligatorio("playa");
        var semilla = DatosCiudades.ciudades().getFirst();
        var ciudad = semilla.aCiudad(50);
        when(real.contarLugares(anyDouble(), anyDouble(), anyString(), anyInt(), anyInt()))
                .thenReturn(null);
        new LlenarAtributosCiudad(new ProxyLugares(real)).enriquecer(ciudad, semilla,
                Map.of("playa", true), Map.of("playa", Importancia.si));
        var validacion = recomendador.validarCiudades(List.of(ciudad),
                new PreferenciasUsuario(100.0, Map.of("playa", Importancia.si), 1));
        assertEquals(List.of(ciudad), validacion.pendientes());
        verify(real).contarLugares(ciudad.getLatitud(), ciudad.getLongitud(),
                playa.categorias, 50_000, playa.umbral);
    }
}

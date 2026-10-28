package com.wisetrip.servicio;

import com.wisetrip.modelo.CategoriaPreferencia;
import com.wisetrip.modelo.Importancia;
import com.wisetrip.modelo.Preferencias;
import com.wisetrip.negocio.GestorPreferencias;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@Primary
public class PreferenciasConAuditoriaDecorador implements GestorPreferencias {

    private final GestorPreferencias gestorBase;

    public PreferenciasConAuditoriaDecorador(PreferenciasServicio gestorBase) {
        this.gestorBase = gestorBase;
    }

    @Override
    public List<CategoriaPreferencia> listarCategorias() {
        return gestorBase.listarCategorias();
    }

    @Override
    public int totalPreguntas() {
        return gestorBase.totalPreguntas();
    }

    @Override
    public Map<String, String> validarPreferencias(Preferencias preferencias) {
        return gestorBase.validarPreferencias(preferencias);
    }

    @Override
    public Map<String, Importancia> obtenerImportancias(Preferencias preferencias) {
        return gestorBase.obtenerImportancias(preferencias);
    }

    @Override
    public Map<String, Boolean> obtenerAtributosSeleccionados(Preferencias preferencias) {
        return new LinkedHashMap<>(gestorBase.obtenerAtributosSeleccionados(preferencias));
    }

    @Override
    public Map<String, List<String>> resumenPorCategoria(Preferencias preferencias) {
        return gestorBase.resumenPorCategoria(preferencias);
    }

    @Override
    public long contarAfirmativas(Preferencias preferencias) {
        long total = gestorBase.contarAfirmativas(preferencias);
        System.out.println("[AUDITORÍA] El usuario seleccionó " + total + " preferencias afirmativas.");
        return total;
    }
}
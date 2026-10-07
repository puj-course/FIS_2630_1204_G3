package com.wisetrip.negocio;

import java.util.List;
import java.util.Map;

import com.wisetrip.modelo.CategoriaPreferencia;
import com.wisetrip.modelo.Importancia;
import com.wisetrip.modelo.Preferencias;

public interface GestorPreferencias {
    List<CategoriaPreferencia> listarCategorias();
    int totalPreguntas();
    Map<String, String> validarPreferencias(Preferencias preferencias);
    Map<String, Importancia> obtenerImportancias(Preferencias preferencias);
    Map<String, Boolean> obtenerAtributosSeleccionados(Preferencias preferencias);
    Map<String, List<String>> resumenPorCategoria(Preferencias preferencias);
    long contarAfirmativas(Preferencias preferencias);
}

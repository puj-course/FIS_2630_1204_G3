## Diagrama de Interfaces de Negocio - Recomendación Personalizada de Destinos - Modulo 2

implementacion en VPasCode

```java
@startuml
title M2 - Interfaces de Negocio - Recomendación Personalizada de Destinos

left to right direction
skinparam classAttributeIconSize 0

package "Negocio" {

    interface GestorPresupuesto {
        +validarPresupuesto(presupuesto: Presupuesto, paisDestino: String): Map<String, String>
        +convertirAUsd(presupuesto: Presupuesto): double
        +monedasDisponibles(paisDestino: String): Map<String, String>
        +nombreMoneda(codigo: String): String
        +formatear(valor: double): String
    }

    interface GestorPreferencias {
        +listarCategorias(): List<CategoriaPreferencia>
        +totalPreguntas(): int
        +validarPreferencias(preferencias: Preferencias): Map<String, String>
        +obtenerImportancias(preferencias: Preferencias): Map<String, Importancia>
        +obtenerAtributosSeleccionados(preferencias: Preferencias): Map<String, Boolean>
        +resumenPorCategoria(preferencias: Preferencias): Map<String, List<String>>
        +contarAfirmativas(preferencias: Preferencias): long
    }

    interface Recomendador {
        +recomendarDestinos(ciudades: List<Ciudad>, preferencias: PreferenciasUsuario): List<ResultadoRecomendacion>
        +validarCiudades(ciudades: List<Ciudad>, preferencias: PreferenciasUsuario): ValidacionCiudades
        +puntuarCiudadesValidas(validacion: ValidacionCiudades, preferencias: PreferenciasUsuario): List<ResultadoRecomendacion>
        +calcularPuntajeTotal(ciudad: Ciudad, preferencias: PreferenciasUsuario): ResultadoRecomendacion
    }

    interface SeleRecomendaciones {
        +seleccionarMejoresDestinos(resultados: List<ResultadoRecomendacion>): SeleccionDestinos
    }

    interface ProovedorLugares {
        +coordenadas(ciudad: String, pais: String): double[]
        +contarLugares(lat: double, lon: double, categorias: String, radioMetros: int, limite: int): Integer
    }

    interface CiudadNegocio {
        +obtenerTodas(): List<Ciudad>
        +insertar(ciudad: Ciudad): boolean
    }
}


package "Servicios" {

    class PresupuestoServicio

    class PreferenciasServicio

    class RecomendadorDestinos

    class SelectorDestinos

    class ServicioGeoapify
}


package "Datos" {

    class CiudadDAO
}


package "Modelos" {

    class Presupuesto

    class Preferencias

    class PreferenciasUsuario

    class CategoriaPreferencia

    class Importancia

    class Ciudad

    class ResultadoRecomendacion

    class SeleccionDestinos

    class ValidacionCiudades
}

PresupuestoServicio ..|> GestorPresupuesto

PreferenciasServicio ..|> GestorPreferencias

RecomendadorDestinos ..|> Recomendador

SelectorDestinos ..|> SeleRecomendaciones

ServicioGeoapify ..|> ProovedorLugares

CiudadDAO ..|> CiudadNegocio

GestorPresupuesto ..> Presupuesto

GestorPreferencias ..> Preferencias
GestorPreferencias ..> CategoriaPreferencia
GestorPreferencias ..> Importancia

Recomendador ..> Ciudad
Recomendador ..> PreferenciasUsuario
Recomendador ..> ResultadoRecomendacion
Recomendador ..> ValidacionCiudades

SeleRecomendaciones ..> ResultadoRecomendacion
SeleRecomendaciones ..> SeleccionDestinos

CiudadNegocio ..> Ciudad

@enduml

```

## Diagrama Visual 
<img width="1476" height="933" alt="m2-diagrama-de-interfaces-de-negocio" src="https://github.com/user-attachments/assets/8e465b9d-3986-497c-98ce-b7f46a0aa73d" />

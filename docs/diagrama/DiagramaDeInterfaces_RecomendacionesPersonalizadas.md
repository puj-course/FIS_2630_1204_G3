## Diagrama de Interfaces de Negocio - Recomendación Personalizada de Destinos - Modulo 2

implementacion en VPasCode

```java
@startuml

WiseTrip_M2_Interfaces

title
Interfaces de Negocio - 
Recomendación Personalizada de Destinos del Modulo 2
end title

top to bottom direction

skinparam shadowing false
skinparam packageStyle circle
skinparam linetype polyline
skinparam nodesep 40
skinparam ranksep 50
skinparam packageFontName Times New Roman
skinparam backgroundColor transparent
hide empty members


package "Controlador" #F1ECFA {

    class RecomendacionControlador <<@Controller>>
}


package "Contratos de negocio" #E8F4FF {

    interface CiudadNegocio {
        +obtenerTodas(): List<Ciudad>
        +insertar(ciudad: Ciudad): boolean
    }

    interface GestorPreferencias {
        +listarCategorias(): List<CategoriaPreferencia>
        +totalPreguntas(): int
        +validarPreferencias(preferencias: Preferencias): Map<String, String>
        +obtenerImportancias(preferencias: Preferencias): Map<String, Importancia>
        +obtenerAtributosSeleccionados(preferencias: Preferencias): Map<String, Boolean>
        +clavesAfirmativas(preferencias: Preferencias): List<String>
        +resumenPorCategoria(preferencias: Preferencias): Map<String, List<String>>
        +contarAfirmativas(preferencias: Preferencias): long
    }

    interface GestorPresupuesto {
        +validarPresupuesto(presupuesto: Presupuesto, paisDestino: String): Map<String, String>
        +convertirAUsd(presupuesto: Presupuesto): double
        +monedasDisponibles(paisDestino: String): Map<String, String>
        +nombreMoneda(codigo: String): String
        +formatear(valor: double): String
    }

    interface Recomendador {
        +excedePresupuesto(ciudad: Ciudad, preferencias: PreferenciasUsuario): boolean
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

    interface CacheRepositorio {
        +buscar(latitud: double, longitud: double, categoria: String, radioMetros: int, limite: int): Optional<Integer>
        +guardar(latitud: double, longitud: double, categoria: String, radioMetros: int, limite: int, respuesta: int): void
    }

    ' ---- Cuadrícula de 2 columnas ----
    ' Fila 1
    CiudadNegocio -[hidden]right-> GestorPreferencias
    ' Fila 2
    GestorPresupuesto -[hidden]right-> Recomendador
    ' Fila 3
    SeleRecomendaciones -[hidden]right-> ProovedorLugares

    ' Separación vertical entre filas (una por columna)
    CiudadNegocio -[hidden]down-> GestorPresupuesto
    GestorPreferencias -[hidden]down-> Recomendador
    GestorPresupuesto -[hidden]down-> SeleRecomendaciones
    Recomendador -[hidden]down-> ProovedorLugares
    SeleRecomendaciones -[hidden]down-> CacheRepositorio
}


package "Servicios" #EAF7EA {

    class PreferenciasServicio <<@Service>>
    class PresupuestoServicio <<@Service>>
    class RecomendadorDestinos <<@Service>>
    class SelectorDestinos <<@Service>>
    class LlenarAtributosCiudad <<@Service>>
    class ProxyLugares <<@Service>>
    class ServicioGeoapify <<@Service>>

    ' ---- Cuadrícula de 2 columnas ----
    PreferenciasServicio -[hidden]right-> PresupuestoServicio
    RecomendadorDestinos -[hidden]right-> SelectorDestinos
    LlenarAtributosCiudad -[hidden]right-> ProxyLugares

    PreferenciasServicio -[hidden]down-> RecomendadorDestinos
    PresupuestoServicio -[hidden]down-> SelectorDestinos
    RecomendadorDestinos -[hidden]down-> LlenarAtributosCiudad
    SelectorDestinos -[hidden]down-> ProxyLugares
    LlenarAtributosCiudad -[hidden]down-> ServicioGeoapify
}


package "Acceso a datos" #FDECEC {

    class CiudadDAO <<@Repository>>
    class Cache <<@Repository>>

    CiudadDAO -[hidden]right-> Cache
}

PreferenciasServicio ..|> GestorPreferencias
PresupuestoServicio ..|> GestorPresupuesto
RecomendadorDestinos ..|> Recomendador
SelectorDestinos ..|> SeleRecomendaciones
ProxyLugares ..|> ProovedorLugares
ServicioGeoapify ..|> ProovedorLugares
CiudadDAO ..|> CiudadNegocio
Cache ..|> CacheRepositorio

RecomendacionControlador --> CiudadNegocio
RecomendacionControlador --> Recomendador
RecomendacionControlador --> SeleRecomendaciones

RecomendadorDestinos --> GestorPreferencias

LlenarAtributosCiudad --> ProovedorLugares : usa

ProxyLugares --> CacheRepositorio : consulta / guarda

ProxyLugares --> ServicioGeoapify : delega si no hay cache

RecomendacionControlador -[hidden]down-> CiudadNegocio
CacheRepositorio -[hidden]down-> PreferenciasServicio
ServicioGeoapify -[hidden]down-> CiudadDAO


note right of ProxyLugares
  El Proxy consulta primero
  la cache.

  Si no encuentra el resultado,
  delega la consulta a
  ServicioGeoapify.
end note

@enduml
```

## Diagrama Visual 
<img width="2716" height="999" alt="interfaces-de-negocio-modulo-2" src="https://github.com/user-attachments/assets/3422abcf-504a-424f-bc80-a8391c40b383" />


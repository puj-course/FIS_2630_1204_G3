# Diagrama de interfaces — Planificación de preferencias y fechas (M3)

Este diagrama muestra las interfaces de negocio creadas para el módulo de Planificación e Itinerario del Viaje (M3), en respuesta a la observación de que el proyecto no tenía interfaces reales de negocio (solo las anotaciones propias de Spring Boot).

Se definieron dos interfaces: `IFechasServicio` y `GestorPreferencias`, cada una implementada por su respectivo servicio (`FechasServicio` y `PreferenciasServicio`). El controlador (`PlanificacionControlador`) depende de estas interfaces y no de las clases concretas, siguiendo el principio de inversión de dependencias: si el equipo cambia cómo se valida una fecha o cómo se procesan las preferencias por dentro, el controlador no se entera ni se rompe, mientras el contrato de la interfaz se mantenga.

El diagrama también incluye las clases de dominio relacionadas (`CategoriaPreferencia`, `Pregunta`, `FechasViaje`, `Preferencias`), que ya no son solo atributos: tienen comportamiento propio como `tieneRangoValido()`, `getDuracionDias()`, `tienePreferencia()` y `esIndispensable()`. Por último se muestran las vistas (`fechas.jsp`, `preferencias.jsp`) que inician el flujo desde el lado del usuario.

## Código PlantUML

```plantuml
@startuml
title WiseTrip - M3: Planificación de preferencias y fechas
left to right direction
skinparam packageStyle rectangle
skinparam classAttributeIconSize 0
skinparam linetype ortho

package "Vistas" {
  class "preferencias.jsp" as VistaPreferencias <<JSP>> {
    + mostrar cuestionario
    + enviar respuestas
  }

  class "fechas.jsp" as VistaFechas <<JSP>> {
    + seleccionar fecha de inicio y fin
    + enviar fechas
  }
}

package "Controlador" {
  class PlanificacionControlador <<Controller>> {
    - gestorPreferencias: GestorPreferencias
    - fechasServicio: IFechasServicio
    --
    + mostrarPreferencias()
    + guardarPreferencias()
    + mostrarFechas()
    + guardarFechas()
  }
}

package "Interfaces de negocio" {
  interface GestorPreferencias {
    + listarCategorias(): List<CategoriaPreferencia>
    + totalPreguntas(): int
    + validarPreferencias(preferencias): Map
    + obtenerImportancias(preferencias): Map
    + obtenerAtributosSeleccionados(preferencias): Map
    + resumenPorCategoria(preferencias): Map
    + contarAfirmativas(preferencias): long
  }

  interface IFechasServicio {
    + validarFechas(fechas): Map
    + calcularDuracion(fechas): long
    + hoy(): String
  }
}

package "Servicios" {
  class PreferenciasServicio <<Service>> {
    + listarCategorias(): List<CategoriaPreferencia>
    + totalPreguntas(): int
    + validarPreferencias(preferencias): Map
    + obtenerImportancias(preferencias): Map
    + obtenerAtributosSeleccionados(preferencias): Map
    + resumenPorCategoria(preferencias): Map
    + contarAfirmativas(preferencias): long
  }

  class FechasServicio <<Service>> {
    - MAX_DIAS: int
    + validarFechas(fechas): Map
    + calcularDuracion(fechas): long
    + hoy(): String
  }
}

package "Modelos de planificación" {
  class Preferencias {
    - respuestas: Map<String, String>
    + getTotalRespondidas(): int
    + tienePreferencia(atributo): boolean
    + esIndispensable(atributo): boolean
    + obtenerImportancia(atributo): Importancia
  }

  class FechasViaje {
    - fechaInicio: String
    - fechaFin: String
    + tieneRangoValido(): boolean
    + getDuracionDias(): long
  }

  class CategoriaPreferencia {
    - nombre: String
    - icono: String
    - descripcion: String
    - preguntas: List<Pregunta>
  }

  class Pregunta {
    - clave: String
    - texto: String
  }
}

' Cada servicio implementa su interfaz de negocio.
PreferenciasServicio ..|> GestorPreferencias
FechasServicio ..|> IFechasServicio

' El controlador usa los contratos y modelos del flujo.
PlanificacionControlador ..> GestorPreferencias
PlanificacionControlador ..> IFechasServicio
PlanificacionControlador ..> Preferencias
PlanificacionControlador ..> FechasViaje

' El servicio de preferencias organiza el cuestionario por categorías y preguntas.
PreferenciasServicio ..> CategoriaPreferencia
CategoriaPreferencia "1" o-- "*" Pregunta

' Vistas atendidas por el controlador.
VistaPreferencias ..> PlanificacionControlador
VistaFechas ..> PlanificacionControlador

@enduml
```

## Imagen
<img width="1354" height="1093" alt="DiagramaInterfaces_PlaneaciónM3" src="https://github.com/user-attachments/assets/a9ea11d4-8de5-406e-95b6-930c035e8f37" />



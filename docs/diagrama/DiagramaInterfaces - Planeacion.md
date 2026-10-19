# Diagrama de interfaces — Planificación de preferencias y fechas (M3)

Este diagrama muestra las interfaces de negocio creadas para el módulo de Planificación e Itinerario del Viaje (M3), en respuesta a la observación de que el proyecto no tenía interfaces reales de negocio (solo las anotaciones propias de Spring Boot).

Se definieron dos interfaces: `IFechasServicio` y `GestorPreferencias`, cada una implementada por su respectivo servicio (`FechasServicio` y `PreferenciasServicio`). El controlador (`PlanificacionControlador`) depende de estas interfaces y no de las clases concretas, siguiendo el principio de inversión de dependencias: si el equipo cambia cómo se valida una fecha o cómo se procesan las preferencias por dentro, el controlador no se entera ni se rompe, mientras el contrato de la interfaz se mantenga.

El diagrama también incluye las clases de dominio relacionadas (`CategoriaPreferencia`, `Pregunta`, `FechasViaje`, `Preferencias`), que ya no son solo atributos: tienen comportamiento propio como `tieneRangoValido()`, `getDuracionDias()`, `tienePreferencia()` y `esIndispensable()`. Por último se muestran las vistas (`fechas.jsp`, `preferencias.jsp`) que inician el flujo desde el lado del usuario.

## Código PlantUML

```plantuml
@startuml
title WiseTrip - M3: Planificación de preferencias, fechas y presupuesto
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

  class "presupuesto.jsp" as VistaPresupuesto <<JSP>> {
    + ingresar monto y moneda
    + enviar presupuesto
  }

  class "resumen.jsp" as VistaResumen <<JSP>> {
    + mostrar resumen del viaje
  }
}

package "Controlador" {
  class PlanificacionControlador <<Controller>> {
    - gestorPreferencias: GestorPreferencias
    - gestorPresupuesto: GestorPresupuesto
    - fechasServicio: IFechasServicio
    - usuarioSesionFacade: UsuarioSesionFacade
    --
    + mostrarPreferencias()
    + guardarPreferencias()
    + mostrarFechas()
    + guardarFechas()
    + mostrarPresupuesto()
    + guardarPresupuesto()
    + mostrarResumen()
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

  interface GestorPresupuesto {
    + monedasDisponibles(paisDestino): List
    + validarPresupuesto(presupuesto, paisDestino): Map
    + convertirAUsd(presupuesto): double
    + formatear(monto): String
    + nombreMoneda(moneda): String
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
    - cadenaValidacion: ReglaValidacionFechas
    --
    + validarFechas(fechas): Map
    + calcularDuracion(fechas): long
    + hoy(): String
  }

  class PresupuestoServicio <<Service>> {
    + monedasDisponibles(paisDestino): List
    + validarPresupuesto(presupuesto, paisDestino): Map
    + convertirAUsd(presupuesto): double
    + formatear(monto): String
    + nombreMoneda(moneda): String
  }

  class UsuarioSesionFacade <<Service>> {
    + usuarioActivo(sesion): Usuario
    + iniciales(usuario): String
    + prepararEncabezado(sesion, model): Usuario
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

  class Presupuesto {
    - monto: String
    - moneda: String
    + getMontoNumerico(): double
    + esValido(): boolean
    + esSuficientePara(costo): boolean
    + tieneMoneda(): boolean
  }
}

' Cada servicio implementa su interfaz de negocio.
PreferenciasServicio ..|> GestorPreferencias
FechasServicio ..|> IFechasServicio
PresupuestoServicio ..|> GestorPresupuesto

' El controlador usa los contratos, la fachada y los modelos del flujo.
PlanificacionControlador ..> GestorPreferencias
PlanificacionControlador ..> IFechasServicio
PlanificacionControlador ..> GestorPresupuesto
PlanificacionControlador ..> UsuarioSesionFacade
PlanificacionControlador ..> Preferencias
PlanificacionControlador ..> FechasViaje
PlanificacionControlador ..> Presupuesto

' El servicio de preferencias organiza el cuestionario por categorías y preguntas.
PreferenciasServicio ..> CategoriaPreferencia
CategoriaPreferencia "1" o-- "*" Pregunta

' Vistas atendidas por el controlador.
VistaPreferencias ..> PlanificacionControlador
VistaFechas ..> PlanificacionControlador
VistaPresupuesto ..> PlanificacionControlador
VistaResumen ..> PlanificacionControlador

@enduml
```

## Imagen
<img width="1740" height="1576" alt="DiagramaInterfacePlanning" src="https://github.com/user-attachments/assets/21f7e884-3426-4a24-8f8a-22d7ecb6762b" />




## Diagrama de componentes - Planificación e itinerario del viaje

### Componentes

| Componente | Qué hace |
|---|---|
| `origen.jsp`, `preferencias.jsp`, `fechas.jsp` | Formularios donde el viajero llena su origen, sus preferencias y sus fechas |
| `ViajeControlador` | Recibe el origen, lo valida con el servicio y lo guarda en el `Viaje` de la sesión |
| `PlanificacionControlador` | Recibe las preferencias y las fechas, las valida y las guarda en la sesión HTTP |
| `UsuarioSesionFacade` | Patrón Facade: da el usuario activo de la sesión y sus iniciales para el encabezado |
| `ViajeServicio` | Lista países y ciudades y valida la ubicación de origen |
| `PreferenciasServicio` | Arma el cuestionario de 9 categorías y valida las respuestas |
| `FechasServicio` | Valida las fechas y calcula la duración del viaje |
| Reglas de validación de fechas | Patrón Chain of Responsibility: `ReglaFechaInicio` → `ReglaFechaFin` → `ReglaRelacionFechas` (máximo 60 días) |
| Dominio (modelo) | `Viaje`, `Ubicacion`, `Preferencias` y `FechasViaje`: los datos del viaje |
| Sesión HTTP | Guarda el `Viaje` y las respuestas mientras el viajero sigue el flujo |

### Interfaces

| Interfaz | Entre | Operaciones |
|---|---|---|
| HTTP origen | Usuario → `ViajeControlador` | `GET /origen`, `POST /origen` |
| HTTP planificación | Usuario → `PlanificacionControlador` | `GET /preferencias`, `POST /preferencias`, `GET /fechas`, `POST /fechas` |
| `IViajeServicio` | `ViajeControlador` → `ViajeServicio` | `listarPaises()`, `listarCiudades()`, `validarUbicacion()` |
| `GestorPreferencias` | `PlanificacionControlador` → `PreferenciasServicio` | `listarCategorias()`, `validarPreferencias()`, `resumenPorCategoria()` |
| `IFechasServicio` | `PlanificacionControlador` → `FechasServicio` | `validarFechas()`, `calcularDuracion()`, `hoy()` |
| `ReglaValidacionFechas` | `FechasServicio` → Reglas de validación | `validar()` |
| `UsuarioSesionFacade` | Los dos controladores → fachada | `usuarioActivo()`, `iniciales()`, `prepararEncabezado()` |
| `HttpSession` | Controladores y fachada → sesión HTTP | `getAttribute()`, `setAttribute()` |

### Flujo

Usuario → `origen.jsp` → `ViajeControlador` → `ViajeServicio` (valida la ubicación) → el origen queda en el `Viaje` de la sesión. Después, Usuario → `preferencias.jsp` → `PlanificacionControlador` → `PreferenciasServicio`, y Usuario → `fechas.jsp` → `PlanificacionControlador` → `FechasServicio` → cadena de reglas. En todas las pantallas, el controlador pide a `UsuarioSesionFacade` el usuario y sus iniciales. Este módulo no usa base de datos: todo queda en la sesión HTTP hasta que otros módulos lo usen.

## Imagen del diagrama de componentes

(aquí va la imagen)

## Codigo del diagrama

```plantuml
@startuml Componentes_Planificacion
title WiseTrip — Diagrama de componentes: Planificación e itinerario del viaje

skinparam componentStyle uml2
skinparam shadowing false
skinparam nodesep 40
skinparam ranksep 70
skinparam component {
  BorderColor #333333
  BorderThickness 1.3
}
skinparam interface {
  BackgroundColor #FFE699
  BorderColor #333333
  FontSize 11
}
skinparam arrow {
  Color #333333
}
left to right direction

actor "Usuario" as User

component "origen.jsp" as Origen #FFD9A0
component "preferencias.jsp\nfechas.jsp" as Vistas #FFD9A0
component "ViajeControlador" as CtrlV #FFD9A0
component "PlanificacionControlador" as CtrlP #FFD9A0
component "Sesión HTTP\n(contenedor servlet)" as Sesion #F5F5F5

component "UsuarioSesionFacade" as Fachada #FFE699
component "ViajeServicio" as SrvV #FFE699
component "PreferenciasServicio" as SrvP #FFE699
component "FechasServicio" as SrvF #FFE699
component "Reglas de validación de fechas\n(Inicio → Fin → Relación)" as Reglas #FFE699

component "Dominio (modelo)" as Dominio #FFFBE6 {
  component "Viaje\nUbicacion" as DomViaje #FFFBE6
  component "Preferencias\nFechasViaje" as DomPlan #FFFBE6
}

interface "GET /origen\nPOST /origen" as iWebV
interface "GET /preferencias\nPOST /preferencias\nGET /fechas\nPOST /fechas" as iWebP
interface "IViajeServicio\nvalidarUbicacion()" as iViaje
interface "GestorPreferencias\nvalidarPreferencias()" as iPref
interface "IFechasServicio\nvalidarFechas()" as iFechas
interface "ReglaValidacionFechas\nvalidar()" as iRegla
interface "UsuarioSesionFacade\nusuarioActivo()\niniciales()" as iFachada
interface "HttpSession\ngetAttribute()\nsetAttribute()" as iSesion

User --( iWebV
iWebV -- CtrlV
User --( iWebP
iWebP -- CtrlP

CtrlV ..> Origen : devuelve vista
CtrlP ..> Vistas : devuelve vista

CtrlV --( iViaje
iViaje -- SrvV

CtrlP --( iPref
iPref -- SrvP

CtrlP --( iFechas
iFechas -- SrvF

SrvF --( iRegla
iRegla -- Reglas

CtrlV --( iFachada
CtrlP --( iFachada
iFachada -- Fachada

Fachada --( iSesion
CtrlV --( iSesion
CtrlP --( iSesion
iSesion -- Sesion

SrvV ..> DomViaje : valida
SrvP ..> DomPlan : valida
Reglas ..> DomPlan : revisa fechas
Sesion ..> Dominio : guarda el Viaje

@enduml
```

## Diagrama

<img width="1999" height="922" alt="DiagramaComponentesPlanning" src="https://github.com/user-attachments/assets/f973e716-ec38-437b-b950-c0ec893bcbf0" />

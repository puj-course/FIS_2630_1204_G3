# Diagrama de componentes — Control de presupuesto y gastos (M4)

Este diagrama muestra las piezas que forman el módulo de Control de Presupuesto y Gastos (M4) y la interfaz por la que se comunica cada una con las demás. No muestra atributos ni métodos: lo importante aquí es quién ofrece un servicio, quién lo necesita y cómo se conectan, desde las vistas JSP hasta la base de datos PostgreSQL.

Cada interfaz se dibuja entre dos componentes. La bolita (○) va unida al componente que la provee, es decir, el que la implementa. El semicírculo va unido al componente que la requiere, es decir, el que la usa. Cuando la bolita queda dentro del semicírculo, los dos componentes están conectados. Por ejemplo, `PlanificacionControlador` requiere `GestorPresupuesto` y `PresupuestoConMargenDecorador` la provee; a la vez, el decorador requiere otro `GestorPresupuesto` (el `gestorBase`), que provee `PresupuestoServicio`. Esa cadena es el patrón Decorator: el decorador ofrece la misma interfaz que el objeto que envuelve y le agrega un 5% de margen de imprevistos al convertir el presupuesto a USD.

El flujo que hoy funciona en la aplicación es este. El viajero envía su presupuesto desde `presupuesto.jsp` y su reparto por categorías desde `plan.jsp`. `PlanificacionControlador` y `PlanControlador` los validan mediante `GestorPresupuesto` e `IGestionReparto` y los guardan en la sesión. Después, `RecomendacionControlador` usa `IRepositorioViajes` para guardar el viaje con su presupuesto en USD, pasando por `ViajeDAO`, `ConexionBD` y JDBC hasta PostgreSQL.

La parte de gastos aparece con el estereotipo «pendiente de conexión». `GastosServicio` provee `IGestorGastos`, pero ningún controlador la requiere todavía (bolita sola). También requiere tres repositorios que aún no tienen DAO (semicírculo sin bolita). Para decidir si un gasto que se pasa del cupo se bloquea o solo se advierte, usa el patrón Factory Method a través de `IControlCategoriaGasto`. Hoy esta parte solo se ejecuta en `GastosServicioTest`.

## Código PlantUML

```plantuml
@startuml DiagramaComponentes_PresupuestoGastos
skinparam componentStyle uml2
skinparam shadowing false
skinparam defaultFontName Arial
skinparam nodesep 60
skinparam ranksep 70
skinparam ArrowColor #0E7C86
skinparam ArrowThickness 1.5
skinparam component {
  BackgroundColor #FFFFFF
  BorderColor #1B2A41
  FontSize 13
}
skinparam interface {
  BackgroundColor #FFFFFF
  BorderColor #0E7C86
  FontSize 12
}
skinparam database {
  BackgroundColor #FFFFFF
  BorderColor #1B2A41
}
skinparam note {
  BackgroundColor #FFF9C4
  BorderColor #FBC02D
  FontSize 11
}

title WiseTrip - M4: Diagrama de componentes - Presupuesto y gastos

' ==================== COMPONENTES ====================
component "Vistas JSP\n(presupuesto, resumen,\nplan, viaje)" as Vistas
component "PlanificacionControlador" as CPlanif
component "PlanControlador" as CPlan
component "RecomendacionControlador" as CReco
component "PresupuestoConMargenDecorador" as Deco
component "PresupuestoServicio" as PServ
component "RepartoServicio" as RServ
component "GastosServicio" as GServ <<pendiente de conexión>>
component "ControlCategoria\n(Estricto / Flexible)" as Ctrl <<pendiente de conexión>>
component "ViajeDAO" as VDAO
component "ConexionBD" as Conn
database "PostgreSQL" as DB

' ==================== INTERFACES ====================
interface "/presupuesto" as HPres
interface "/plan" as HPlan
interface "GestorPresupuesto" as IGP
interface "GestorPresupuesto\n(gestorBase)" as IGPB
interface "IGestionReparto" as IGR
interface "IGestorGastos" as IGG
interface "IControlCategoriaGasto" as ICC
interface "IRepositorioGastos\nIRepositorioPresupuestoViaje\nIRepositorioRepartoPresupuesto\n(sin implementar)" as IRG
interface "IRepositorioViajes" as IRV
interface "getConnection()" as IConn
interface "JDBC" as IJdbc

' Regla:  Consumidor --( Interfaz   -> semicírculo (requiere)
'         Interfaz -- Proveedor     -> bolita (provee)

' Las vistas envían los formularios a los controladores
Vistas --( HPres
HPres -- CPlanif
Vistas --( HPlan
HPlan -- CPlan

' Presupuesto: el controlador recibe el Decorator (@Primary)
CPlanif --( IGP
IGP -- Deco

' Decorator: envuelve al servicio original
Deco --( IGPB
IGPB -- PServ

' Reparto del presupuesto
CPlan --( IGR
IGR -- RServ

' PlanControlador también usa PresupuestoServicio directamente (formatear)
CPlan ..> PServ : formatear()

' El presupuesto en USD se guarda con el viaje
CReco --( IRV
IRV -- VDAO
VDAO --( IConn
IConn -- Conn
Conn --( IJdbc
IJdbc -- DB

' Gastos + Factory Method (pendiente de conexión)
IGG -- GServ
GServ --( ICC
ICC -- Ctrl
GServ --( IRG

note bottom of GServ
  Provee IGestorGastos, pero ningún
  controlador la requiere todavía.
  Requiere 3 repositorios que aún
  no tienen DAO.
end note

note bottom of Deco
  Patrón Decorator: provee
  GestorPresupuesto y por dentro
  requiere otro GestorPresupuesto
  (PresupuestoServicio).
end note

note bottom of Ctrl
  Patrón Factory Method:
  crea el control Estricto o
  Flexible según la categoría.
end note

legend bottom
  **—○  bolita** = el componente PROVEE la interfaz
  **—(  semicírculo** = el componente REQUIERE la interfaz
  Bolita sola = se provee pero nadie la usa todavía
  Semicírculo solo = se requiere pero nadie la implementa todavía
end legend

@enduml
```

## Diagrama
<img width="1735" height="1118" alt="Untitled (7)" src="https://github.com/user-attachments/assets/2904ed34-7fbe-4f7f-82e0-84d5d9f1e9d3" />



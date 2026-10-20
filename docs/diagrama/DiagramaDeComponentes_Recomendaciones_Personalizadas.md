### Diagrama de componentes del modulo 2 de wisetrip
(Recomendacione y personalizacion del viaje)
## Diagrama de Componentes - WiseTrip M2

```text
@startuml
title Diagrama de Componentes - WiseTrip M2
left to right direction

skinparam componentStyle uml2
skinparam componentBackgroundColor #BFE8F5
skinparam componentBorderColor #26728E

actor Usuario

component "preferencias.jsp" as VistaPreferencias <<component>>
component "PlanificacionControlador" as PlanificacionControlador <<component>>
component "fechas.jsp" as VistaFechas <<component>>
component "presupuesto.jsp" as VistaPresupuesto <<component>>
component "resumen.jsp" as VistaResumen <<component>>

component "PreferenciasServicio\n(GestorPreferencias)" as PreferenciasServicio <<component>>
component "PresupuestoConMargenDecorador\n(GestorPresupuesto)" as PresupuestoDecorador <<component>>
component "PresupuestoServicio" as PresupuestoServicio <<component>>

component "recomendaciones.jsp" as VistaRecomendaciones <<component>>
component "RecomendacionControlador" as RecomendacionControlador <<component>>
component "RecomendadorDestinos\n(Recomendador)" as RecomendadorDestinos <<component>>
component "SelectorDestinos\n(SeleRecomendaciones)" as SelectorDestinos <<component>>
component "LlenarAtributosCiudad" as LlenarAtributosCiudad <<component>>

component "CiudadDAO\n(CiudadNegocio)" as CiudadDAO <<component>>
component "ViajeDAO\n(IRepositorioViajes)" as ViajeDAO <<component>>
component "PreferenciaDAO\n(IRepositorioPreferencias)" as PreferenciaDAO <<component>>

component "ProxyLugares\n(ProovedorLugares)" as ProxyLugares <<component>>
component "ServicioGeoapify\n(ProovedorLugares)" as ServicioGeoapify <<component>>
component "Cache\n(CacheRepositorio)" as Cache <<component>>
component "ConexionBD" as ConexionBD <<component>>
component "DataSource" as DataSource <<component>>

database "PostgreSQL\nWiseTrip" as BaseDatos
cloud "Geoapify API" as GeoapifyAPI

Usuario --> VistaPreferencias
VistaPreferencias ..> PlanificacionControlador : GET/POST /preferencias
PlanificacionControlador ..> PreferenciasServicio : valida y obtiene preferencias
PlanificacionControlador ..> VistaFechas : muestra fechas

Usuario --> VistaFechas
VistaFechas ..> PlanificacionControlador : GET/POST /fechas
PlanificacionControlador ..> VistaPresupuesto : muestra presupuesto

Usuario --> VistaPresupuesto
VistaPresupuesto ..> PlanificacionControlador : GET/POST /presupuesto
PlanificacionControlador ..> PresupuestoDecorador : valida y convierte
PresupuestoDecorador ..> PresupuestoServicio : delega operaciones
PlanificacionControlador ..> VistaResumen : muestra resumen

Usuario --> VistaResumen
VistaResumen ..> RecomendacionControlador : GET /recomendaciones

RecomendacionControlador ..> CiudadDAO : CiudadNegocio / obtenerTodas()
RecomendacionControlador ..> RecomendadorDestinos : valida y puntúa
RecomendacionControlador ..> SelectorDestinos : selecciona destinos
RecomendacionControlador ..> LlenarAtributosCiudad : enriquece ciudades
RecomendacionControlador ..> ViajeDAO : insertar()
RecomendacionControlador ..> PreferenciaDAO : guardarActivas()
RecomendacionControlador ..> VistaRecomendaciones : muestra resultados

RecomendadorDestinos ..> PreferenciasServicio : consulta categorías
LlenarAtributosCiudad ..> ProxyLugares : contarLugares()

ProxyLugares ..> Cache : CacheRepositorio / buscar y guardar
ProxyLugares ..> ServicioGeoapify : contarLugares() si no hay caché
ProxyLugares ..> ServicioGeoapify : coordenadas() delegación directa
ServicioGeoapify ..> GeoapifyAPI : HTTPS / consulta de lugares

CiudadDAO ..> ConexionBD : obtenerConexion()
ViajeDAO ..> ConexionBD : obtenerConexion()
PreferenciaDAO ..> ConexionBD : obtenerConexion()
ConexionBD ..> BaseDatos : JDBC

Cache ..> DataSource : getConnection()
DataSource ..> BaseDatos : JDBC

@enduml
```

---
<img width="1600" height="382" alt="image" src="https://github.com/user-attachments/assets/dfcf52e0-74a2-4c03-b94b-da1683e6fef1" />


---

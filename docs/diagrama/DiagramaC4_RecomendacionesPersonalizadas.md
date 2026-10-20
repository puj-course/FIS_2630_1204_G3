## Diagrama C4

codigo 

´´´
@startuml
!include <C4/C4_Component>

LAYOUT_WITH_LEGEND()

title Diagrama C4 - Componentes del módulo de recomendaciones

Person(usuario, "Usuario", "Viajero que registra preferencias, fechas y presupuesto para recibir recomendaciones.")

System_Boundary(wisetrip, "WiseTrip") {

    Container_Boundary(app, "Aplicación Web WiseTrip") {
        Component(vista, "Interfaz Web", "JSP / HTML / CSS", "Presenta los formularios y los destinos recomendados.")
        Component(controlador, "Controlador de Recomendaciones", "Java / Spring MVC", "Consulta ciudades y coordina el enriquecimiento, la evaluación y la selección de destinos.")
        Component(recomendacion, "Recomendador de Destinos", "Java / Spring", "Valida presupuesto y requisitos, calcula puntajes y ordena los resultados.")
        Component(selector, "Selector de Destinos", "Java / Spring", "Selecciona los mejores destinos a partir de los resultados evaluados.")
        Component(enriquecimiento, "Enriquecimiento de Ciudades", "Java / Spring", "Completa los atributos de las ciudades según los lugares disponibles.")
        Component(proxy, "Proxy de Lugares", "Java / Spring", "Reutiliza conteos en caché y delega las consultas faltantes al servicio Geoapify.")
        Component(servicioGeoapify, "Servicio Geoapify", "Java / Spring", "Realiza las consultas de coordenadas y lugares a la API externa.")
        Component(persistencia, "Acceso a Datos", "Java / JDBC", "Consulta ciudades y gestiona la caché, los viajes y las preferencias.")
    }

    ContainerDb(neon, "Base de Datos", "PostgreSQL / Neon", "Almacena ciudades, viajes, preferencias y conteos de lugares en caché.")
}

System_Ext(geoapify, "API Geoapify", "Proporciona coordenadas y lugares por categoría y ubicación.")

Rel(usuario, vista, "Ingresa datos del viaje y visualiza recomendaciones")
Rel(vista, controlador, "Solicita recomendaciones", "HTTP/HTTPS")
Rel(controlador, vista, "Entrega los resultados para su presentación")
Rel(controlador, persistencia, "Consulta ciudades y guarda viajes y preferencias")
Rel(controlador, enriquecimiento, "Solicita enriquecer ciudades dentro del presupuesto")
Rel(controlador, recomendacion, "Entrega ciudades y preferencias para validar y puntuar")
Rel(controlador, selector, "Entrega resultados para seleccionar los mejores destinos")
Rel(enriquecimiento, proxy, "Solicita información de lugares")
Rel(proxy, persistencia, "Consulta y guarda conteos en caché")
Rel(proxy, servicioGeoapify, "Delega conteos sin caché y consultas de coordenadas")
Rel(servicioGeoapify, geoapify, "Consulta coordenadas y lugares", "HTTPS / JSON")
Rel(persistencia, neon, "Lee y escribe", "JDBC / PostgreSQL")

@enduml
´´´

## Diagrama Visual 
---
<img width="898" height="1600" alt="image" src="https://github.com/user-attachments/assets/391c1bb4-f7ff-446f-99f7-fd0ba482f512" />

---


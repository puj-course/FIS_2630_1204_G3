# WiseTrip

## Descripción
WiseTrip es una plataforma web de planificación personalizada de viajes que permite a los usuarios organizar sus viajes de acuerdo con su presupuesto, fechas e intereses. La plataforma busca reunir en un solo lugar elementos que normalmente requieren varias aplicaciones, como itinerarios, reservas, gastos, transporte, alojamiento y actividades.

El sistema generará un itinerario personalizado según las preferencias y el presupuesto del usuario, permitiéndole modificar su planificación y recibir alertas relacionadas con clima, cambios en el viaje, presupuesto y gastos. Además, busca ofrecer recomendaciones relacionadas con intereses como aventura, cultura o gastronomía.

El objetivo principal de WiseTrip es simplificar la planificación de viajes, reducir el tiempo y el estrés asociados a organizar un viaje y ayudar al usuario a mantener un mejor control financiero, centralizando la información y facilitando la toma de decisiones antes y durante el viaje.

---

## Equipo del Proyecto
| Nombre        | Rol                   | GitHub / Perfil |
|--------------|-----------------------|-----------------|
| Maria Alejandra Rodriguez | Scrum Master,fronted         | https://github.com/MalejaRodri |
| Gabriela Melo Gualteros | Product Owner, Backend Developer         | https://github.com/GabrielaMeloG|
| Valeria Cortes Rendon | Sprint Planner , Database enginner        | https://github.com/valeriacortess|
| Isabella Posada | Configuration Manager | https://github.com/isaposada |
| Santiago Clavijo | QA Lead, DevOps Engineer               | https://github.com/Santiago-Clavijo |

---

## Tecnologías Utilizadas
- Java 21
- Spring Boot 4.1.1 (Spring MVC + JSP/JSTL)
- Maven Wrapper (no hace falta instalar Maven)
- Docker (opcional)

La persistencia sigue en memoria en este milestone. El esquema previsto está en `docs/database.md`. No hay módulo Python ni PostgreSQL conectado todavía.

## Requisitos

- JDK 21 o superior

## Cómo ejecutar

En la raíz del repositorio:

Windows:

```bat
mvnw.cmd spring-boot:run
```

Linux / macOS:

```bash
./mvnw spring-boot:run
```

Abrir http://localhost:8090

El puerto se cambia con `SERVER_PORT` o en `src/main/resources/application.properties`.

### Docker

```bash
docker compose up --build
```

## Configuración

Variables opcionales (ver `.env.example`):

| Variable | Descripción | Valor por defecto |
| -------- | ----------- | ----------------- |
| `SERVER_PORT` | Puerto HTTP | `8090` |

## Tests

```bash
./mvnw test
```

Windows: `mvnw.cmd test`

También: `scripts/test.sh` o `make test`.

---

## Estructura del Proyecto
```text
FIS_2630_1204_G3/
├── .github/
│   ├── ISSUE_TEMPLATE/
│   │   ├── bug_report.md
│   │   │   └── Plantilla para el reporte estandarizado de errores y bugs.
│   │   └── feature_request.md
│   │       └── Plantilla para propuestas de nuevas características.
│   └── PULL_REQUEST_TEMPLATE.md
│       └── Plantilla base para revisión y apertura de Pull Requests.
│
├── app/
│   ├── index.js
│   │   └── Archivo JavaScript base del proyecto.
│   ├── package.json
│   │   └── Define scripts y dependencias del entorno Node auxiliar.
│   └── Entrega1/
│       └── wisetrip_entrega1/
│           ├── pom.xml
│           │   └── Configuración de dependencias y plugins Maven (Spring Boot).
│           ├── mvnw / mvnw.cmd
│           │   └── Wrappers para ejecutar Maven sin instalación global previa.
│           ├── .mvn/
│           │   └── wrapper/
│           │       └── Configuración interna del Maven Wrapper.
│           └── src/
│               ├── main/
│               │   ├── java/com/wisetrip/
│               │   │   ├── WisetripApplication.java
│               │   │   │   └── Clase principal y punto de entrada de la aplicación Spring Boot.
│               │   │   ├── ServletInitializer.java
│               │   │   │   └── Soporte para despliegue en contenedores servlet externos.
│               │   │   ├── controlador/
│               │   │   │   └── Controladores MVC (Auth, Plan, Recomendaciones, Telegram, etc.).
│               │   │   ├── datos/
│               │   │   │   └── Acceso a datos (DAOs, conexión JDBC y datos semilla de ciudades).
│               │   │   ├── modelo/
│               │   │   │   └── Clases de dominio (Usuario, Viaje, Presupuesto, Preferencias, Ciudad).
│               │   │   ├── negocio/
│               │   │   │   └── Lógica de negocio y catálogo de preguntas dinámicas.
│               │   │   └── servicio/
│               │   │       └── Servicios de recomendación, APIs externas, notificaciones y reglas de negocio.
│               │   ├── resources/
│               │   │   ├── application.properties
│               │   │   │   └── Configuración de base de datos, puertos y credenciales de APIs.
│               │   │   ├── sql/
│               │   │   │   └── Scripts DDL y DML para inicialización y parches de base de datos.
│               │   │   └── static/
│               │   │       ├── css/
│               │   │       │   └── Hojas de estilo organizadas por vista y componentes.
│               │   │       └── img/
│               │   │           └── Recursos gráficos (banderas SVG/PNG, fotos de portada, iconos de preferencias y resumen).
│               │   └── webapp/WEB-INF/vistas/
│               │       └── Vistas JSP (login, registro, orígenes, fechas, presupuesto, recomendaciones, resumen, Telegram).
│               └── test/
│                   └── java/com/wisetrip/
│                       └── WisetripApplicationTests.java
│                           └── Pruebas unitarias y de integración de la aplicación.
│
├── conf/
│   ├── config.yaml
│   │   └── Archivo general de configuración del entorno y servicios.
│   └── settings.json
│       └── Parámetros y ajustes específicos de ejecución.
│
├── database/
│   ├── Base_de_datos_Compartida.pdf
│   │   └── Documento de diseño y acuerdos de la base de datos compartida.
│   ├── BasesNegocio.md / DiccionarioDatos.md
│   │   └── Reglas del negocio de datos y descripción detallada de tablas/campos.
│   ├── DDL.md / database.md
│   │   └── Definición de esquemas de datos y documentación relacional.
│   └── Diagrama entidad-relacion bdd 2.svg
│       └── Esquema gráfico del modelo Entidad-Relación.
│
├── docs/
│   ├── 1raEntrega_FIS_G3.pdf
│   │   └── Informe formal correspondiente a la primera entrega del proyecto.
│   ├── DefinicionProyecto.md
│   │   └── Alcance, objetivos y contexto general de WiseTrip.
│   ├── RequerimientosFuncionales.md / RequerimientosNoFuncionales.md
│   │   └── Especificación completa de requisitos del sistema.
│   ├── WiseTrip.mp4
│   │   └── Video demostrativo de funcionalidades del sistema.
│   ├── api/
│   │   └── Documentación técnica de APIs integradas (Geoapify, Clima, Pasarela de Pagos).
│   ├── architecture/
│   │   └── Diagramas de arquitectura (.svg), bocetos iniciales y documentación técnica del sitio.
│   └── user_guide/
│       └── README.md
│           └── Manual de usuario e instrucciones de uso para el cliente final.
│
├── scripts/
│   ├── deploy.sh
│   │   └── Script de automatización de despliegue en servidor/ambiente objetivo.
│   ├── setup.sh
│   │   └── Script de inicialización de dependencias y entorno local.
│   └── test.sh
│       └── Script para ejecución centralizada del conjunto de pruebas.
│
├── temp/
│   ├── checklist/
│   │   └── Prototipo Spring Boot de pruebas para el módulo de checklist.
│   └── temp_data/
│       └── Algoritmos experimentales preliminares de recomendación y selección.
│
├── BOILERPLATE_template.md
│   └── Guía explicativa sobre la plantilla base y convenciones del repositorio.
│
├── CHANGELOG.md
│   └── Historial cronológico de cambios, mejoras y correcciones.
│
├── CONTRIBUTING.md
│   └── Guía de contribución, flujo de ramas y normas de código para colaboradores.
│
├── docker-compose.yml
│   └── Orquestación de contenedores Docker para levantar servicios locales.
│
├── Dockerfile
│   └── Instrucciones de construcción de la imagen de la aplicación.
│
├── LICENSE
│   └── Licencia legal de distribución del proyecto.
│
├── Makefile
│   └── Accesos directos para compilación, ejecución de scripts y tareas comunes.
│
├── README.md
│   └── Documentación general de bienvenida, instalación y puesta en marcha del proyecto.
│
└── .gitignore
    └── Reglas de exclusión para evitar subir binarios, entornos de IDEs y temporales.

```

## Contexto Académico
- **Asignatura:** Fundamentos de Ingeniería de Software
- **Docente:** Luis Gabriel Moreno Sandoval, PhD
- **Contacto:** morenoluis@javeriana.edu.co

---

## Contacto

**Equipo de desarrollo:**

**Valeria Cortes Rendon**  
Estudiante de Ingeniería en Sistemas, Pontificia Universidad Javeriana  
📧 cortesvaleria@javeriana.edu.co  

**Maria Alejandra Rodriguez Betancourt**  
Estudiante de Ingeniería en Sistemas, Pontificia Universidad Javeriana  
📧 rodriguez_malejandra@javeriana.edu.co  

**Gabriela Melo Gualteros**  
Estudiante de Ingeniería en Sistemas, Pontificia Universidad Javeriana  
📧 g.melog@javeriana.edu.co 

**Santiago Andrés Clavijo Suárez**  
Estudiante de Ingeniería en Sistemas, Pontificia Universidad Javeriana  
📧 clavijoss@javeriana.edu.co  

**Isabella Rodrguez Posada**  
Estudiante de Ingeniería en Sistemas, Pontificia Universidad Javeriana  
📧 isabellarodriguez@javeriana.edu.co 

--- 

## 


## Licencia
Proyecto desarrollado con fines académicos.

## Taller Git
- [Valeria Cortés](https://github.com/valeriacortess)
- [Maria Alejandra Rodriguez](https://github.com/MalejaRodri)
- [Santiago Clavijo](https://github.com/Santiago-Clavijo)


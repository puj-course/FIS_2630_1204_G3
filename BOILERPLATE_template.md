# WiseTrip

## Estructura del proyecto

```
FIS_2630_1204_G3/
├── app/
│   ├── index.js
│   ├── package.json
│   └── Entrega1/
│       └── wisetrip_entrega1/
│           ├── pom.xml
│           ├── mvnw
│           ├── mvnw.cmd
│           ├── src/
│           │   ├── main/
│           │   │   ├── java/com/wisetrip/
│           │   │   │   ├── WisetripApplication.java
│           │   │   │   ├── ServletInitializer.java
│           │   │   │   ├── controlador/
│           │   │   │   ├── datos/
│           │   │   │   ├── modelo/
│           │   │   │   ├── negocio/
│           │   │   │   └── servicio/
│           │   │   ├── resources/
│           │   │   │   ├── application.properties
│           │   │   │   ├── sql/
│           │   │   │   └── static/css/
│           │   │   └── webapp/WEB-INF/vistas/
│           │   └── test/java/com/wisetrip/
│           └── target/
├── conf/
│   └── .gitkeep
├── DOCS/
│   ├── database.md
│   ├── SPRINTS/
│   └── TALLERES/
├── .gitignore
├── README.md
├── LICENSE
├── CHANGELOG.md
└── BOILERPLATE_template.md
```

## Descripción de directorios y archivos

**app/**
Contiene el código principal de la aplicación.
- index.js: archivo base heredado del boilerplate inicial
- package.json: archivo de configuración de Node.js heredado del boilerplate
- Entrega1/wisetrip_entrega1/: proyecto principal de WiseTrip desarrollado en Java con Spring Boot

**app/Entrega1/wisetrip_entrega1/**
Contiene la aplicación web principal de WiseTrip.
- pom.xml: configuración del proyecto Maven, dependencias y versión de Java
- mvnw y mvnw.cmd: ejecutables de Maven Wrapper que permiten correr el proyecto sin instalar Maven globalmente
- src/main/java/: código fuente principal desarrollado en Java
- src/main/resources/: recursos de configuración, scripts SQL y archivos estáticos
- src/main/webapp/WEB-INF/vistas/: vistas JSP de la aplicación
- src/test/java/: pruebas del proyecto
- target/: archivos generados automáticamente por Maven durante la compilación y ejecución

**src/main/java/com/wisetrip/**
Contiene la lógica principal de WiseTrip.
- WisetripApplication.java: clase principal encargada de iniciar la aplicación Spring Boot
- ServletInitializer.java: configuración utilizada para desplegar la aplicación como archivo WAR
- controlador/: contiene los controladores Spring MVC encargados de recibir las solicitudes web y retornar las vistas JSP
- datos/: contiene las clases DAO encargadas de acceder a la base de datos PostgreSQL mediante JDBC
- modelo/: contiene las clases que representan las entidades y objetos utilizados por la aplicación
- negocio/: contiene el catálogo y las definiciones de preguntas utilizadas para registrar las preferencias del usuario
- servicio/: contiene la lógica de negocio, validaciones, recomendación de destinos y conexión con servicios externos

**src/main/resources/**
Contiene los recursos utilizados por la aplicación.
- application.properties: configuración de Spring Boot, puerto de ejecución, vistas JSP, conexión con Geoapify y conexión con PostgreSQL
- sql/: scripts SQL utilizados para modificaciones de la base de datos e inserción de ciudades iniciales
- static/css/: archivos CSS utilizados para los estilos de la aplicación

**src/main/webapp/WEB-INF/vistas/**
Contiene las páginas JSP que se muestran al usuario.
- landing.jsp: página inicial de WiseTrip
- registro.jsp: formulario de registro de usuarios
- login.jsp: formulario de inicio de sesión
- origen.jsp: selección de ciudad y país de origen
- preferencias.jsp: cuestionario utilizado para conocer las preferencias del viajero
- fechas.jsp: selección de las fechas del viaje
- presupuesto.jsp: registro del presupuesto disponible
- resumen.jsp: resumen de la planificación realizada por el usuario
- recomendaciones.jsp: muestra los destinos recomendados por WiseTrip
- registro-exitoso.jsp: página de confirmación después de completar correctamente el registro

**DOCS/**
Contiene la documentación del proyecto.
- database.md: documentación y scripts relacionados con la estructura de la base de datos
- SPRINTS/: evidencias, diagramas y documentación correspondiente a cada semana de sprint
- TALLERES/: documentos relacionados con talleres académicos realizados durante el curso

**conf/**
Carpeta destinada a configuraciones generales del proyecto. Actualmente conserva su estructura mediante el archivo .gitkeep

**Archivos en la raíz**
- .gitignore: define los archivos y carpetas que Git debe ignorar
- README.md: documento principal del proyecto
- LICENSE: información relacionada con la licencia del proyecto
- CHANGELOG.md: registro de cambios realizados durante el desarrollo
- BOILERPLATE_template.md: plantilla base original proporcionada para la estructura del proyecto

## Tecnologías utilizadas

- Backend: Java, Spring Boot y Spring MVC
- Vistas: JSP
- Base de datos: PostgreSQL
- Acceso a datos: JDBC
- Build Tool: Maven
- API externa: Geoapify
- Frontend: HTML, CSS y JSP

PostgreSQL

Las vistas JSP permiten la interacción con el usuario. Los controladores reciben las solicitudes y coordinan el flujo de la aplicación. La capa de servicios contiene la lógica de negocio y el proceso de recomendación de destinos. Las clases DAO realizan las consultas y operaciones sobre PostgreSQL mediante JDBC.

Geoapify es utilizado como servicio externo para consultar información geográfica y características relacionadas con los destinos turísticos.

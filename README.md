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
- **Frontend:** HTML/CC/JS
- **Backend:** Java – Spring Boot
- **Base de Datos:** PostgresSQL
- **IA / Data Science:** Python, Pandas, Scikit-learn
- **DevOps:** GitHub Actions, Docker, SonarQube
- **Control de versiones:** Git

---

## Estructura del Proyecto
```text
FIS_2630_1204_G3/
├── app/
│   ├── index.js
│   │   └── Archivo JavaScript base del proyecto.
│   ├── package.json
│   │   └── Define scripts y configuración del módulo Node inicial.
│   └── Entrega1/
│       └── wisetrip_entrega1/
│           ├── pom.xml
│           │   └── Configuración Maven del proyecto Spring Boot.
│           ├── mvnw / mvnw.cmd
│           │   └── Wrappers para ejecutar Maven en Linux/Mac o Windows.
│           ├── .mvn/
│           │   └── Configuración del Maven Wrapper.
│           ├── src/
│           │   ├── main/
│           │   │   ├── java/com/wisetrip/
│           │   │   │   ├── WisetripApplication.java
│           │   │   │   │   └── Clase principal de la aplicación Spring Boot.
│           │   │   │   ├── ServletInitializer.java
│           │   │   │   │   └── Inicializador para despliegue como aplicación web.
│           │   │   │   ├── controlador/
│           │   │   │   │   └── Controladores MVC de autenticación, inicio, viajes, planificación y recomendaciones.
│           │   │   │   ├── datos/
│           │   │   │   │   └── Clases DAO, conexión a base de datos y datos semilla.
│           │   │   │   ├── modelo/
│           │   │   │   │   └── Entidades y objetos del dominio de WiseTrip.
│           │   │   │   ├── negocio/
│           │   │   │   │   └── Catálogo y reglas de preguntas/preferencias.
│           │   │   │   └── servicio/
│           │   │   │       └── Servicios de usuarios, viajes, presupuesto, preferencias, ciudades y recomendaciones.
│           │   │   ├── resources/
│           │   │   │   ├── application.properties
│           │   │   │   │   └── Configuración de la aplicación.
│           │   │   │   ├── sql/
│           │   │   │   │   └── Scripts SQL de usuarios y ciudades.
│           │   │   │   └── static/css/
│           │   │   │       └── Hojas de estilo de la interfaz.
│           │   │   └── webapp/WEB-INF/vistas/
│           │   │       └── Vistas JSP de login, registro, planificación, recomendaciones y resumen.
│           │   └── test/
│           │       └── java/com/wisetrip/
│           │           └── Pruebas automatizadas de la aplicación.
│           └── target/
│               └── Carpeta generada por Maven con clases compiladas y reportes de pruebas.
│
├── conf/
│   └── .gitkeep
│       └── Mantiene la carpeta de configuración dentro del repositorio.
│
├── DOCS/
│   ├── database.md
│   │   └── Documentación de la base de datos.
│   ├── prueba.txt
│   │   └── Archivo de prueba.
│   ├── SPRINTS/
│   │   ├── SEMANA 1/
│   │   │   └── Documentos y diagrama de arquitectura inicial.
│   │   ├── SEMANA 2/
│   │   │   └── Documentos sobre APIs, base de datos, preferencias y bocetos.
│   │   └── SEMANA 3/
│   │       └── Documentos y diagramas de base de datos.
│   └── TALLERES/
│       └── Documentos de talleres de Git y GitHub.
│
├── BOILERPLATE_template.md
│   └── Documento que explica la estructura base y cómo utilizar este boilerplate.
│
├── CHANGELOG.md
│   └── Registro de cambios del proyecto.
│
├── LICENSE
│   └── Especifica la licencia bajo la cual se distribuye el proyecto.
│
├── README.md
│   └── Documento principal con la descripción, instalación, uso y características del proyecto.
│
└── .gitignore
    └── Define los archivos y carpetas que Git no debe versionar.

```

## Ejecución de pruebas
```text
docker-compose run backend mvn test
docker-compose run ai-model pytest
```

---

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

## Licencia
Proyecto desarrollado con fines académicos.

## Taller Git
- [Valeria Cortés](https://github.com/valeriacortess)
- [Maria Alejandra Rodriguez](https://github.com/MalejaRodri)
- [Santiago Clavijo](https://github.com/Santiago-Clavijo)



# fis_boilerplate
## Descripción de cada directorio y archivos
```bash

```


### .github/
Contiene las configuraciones comunitarias y plantillas estándar para la gestión del repositorio en GitHub.

- `ISSUE_TEMPLATE/`: Plantillas para estandarizar el reporte de errores (`bug_report.md`) y la propuesta de nuevas funcionalidades (`feature_request.md`).
- `PULL_REQUEST_TEMPLATE.md`: Guía y lista de verificación requerida para abrir y revisar Pull Requests.

### app/
Contiene la aplicación principal del proyecto, incluyendo el entorno base y los entregables modulares.

- `index.js` y `package.json`: Configuración y scripts del entorno Node base.
- `Entrega1/wisetrip_entrega1/`: Núcleo de la aplicación web desarrollada con Spring Boot y Maven.
  - `pom.xml`: Definición del proyecto, dependencias y plugins de Maven.
  - `mvnw` / `mvnw.cmd` / `.mvn/`: Maven Wrapper para compilar y ejecutar el proyecto sin requerir instalación global de Maven.
  - `src/main/java/com/wisetrip/`: Código fuente Java organizado bajo arquitectura MVC:
    - `controlador/`: Controladores web (autenticación, viajes, notificaciones, Telegram, recomendaciones).
    - `datos/`: Capa de acceso a datos (DAOs, conexión JDBC, semillas iniciales de ciudades e implementación JDBC del caché de consultas a Geoapify).
    - `modelo/`: Entidades de dominio (Usuario, Viaje, Presupuesto, Preferencias, Ubicaciones).
    - `negocio/`: Lógica de reglas de negocio y catálogo de preguntas de perfilamiento.
    - `servicio/`: Servicios de negocio, recomendador de destinos y clientes de APIs externas (Geoapify, Telegram, Correo).
    - `WisetripApplication.java` y `ServletInitializer.java`: Puntos de entrada para ejecución standalone y despliegue en servidor web.
  - `src/main/resources/`:
    - `application.properties`: Configuración de puertos, base de datos y credenciales.
    - `sql/`: Scripts DDL y DML para esquemas de tablas e inserción masiva de ciudades.
    - `static/`: Recursos estáticos que incluyen hojas de estilo CSS (`css/`) y assets visuales como banderas, imágenes de portada y preferencias (`img/`).
  - `src/main/webapp/WEB-INF/vistas/`: Plantillas JSP dinámicas de la interfaz de usuario (login, registro, cuestionario de preferencias, recomendaciones, resumen y vinculación de servicios).
  - `src/test/java/com/wisetrip/`: Pruebas de integración y unitarias para validar el contexto de Spring Boot.

### conf/
Archivos de configuración globales y de parametrización del entorno.

- `config.yaml`: Variables de configuración generales en formato YAML.
- `settings.json`: Ajustes operativos y perfiles en formato JSON.

### database/
Diseño, especificación y modelado de la persistencia de datos del sistema.

- `Diagrama entidad-relacion bdd 2.svg`: Diagrama conceptual y relacional de entidades y tablas.
- `DiccionarioDatos.md`: Descripción detallada de campos, tipos de datos, llaves foráneas y restricciones.
- `BasesNegocio.md` y `database.md`: Justificación, reglas de persistencia y descripción de flujos de datos.
- `DDL.md`: Sentencias SQL para la creación de esquemas y tablas.
- `Base_de_datos_Compartida.pdf`: Documento formal de acuerdos sobre la estructura compartida de la base de datos.

### docs/
Documentación técnica, funcional y de gestión del proyecto.

- `1raEntrega_FIS_G3.pdf`: Documento formal consolidado de la primera entrega académica.
- `DefinicionProyecto.md`: Contexto, problema, justificación y alcance general de WiseTrip.
- `RequerimientosFuncionales.md` y `RequerimientosNoFuncionales.md`: Especificación detallada de requerimientos del sistema.
- `WiseTrip.mp4`: Demostración visual y funcional del software.
- `api/`: Documentación y especificaciones de servicios de terceros (Geoapify, APIs meteorológicas y pasarelas de pago).
- `architecture/`: Arquitectura de software, diagramas de componentes (`.svg`), notas técnicas y bocetos de interfaz.
- `user_guide/`: Manuales de usuario y guías de uso paso a paso de la aplicación.

### scripts/
Scripts ejecutables en bash para automatizar el ciclo de desarrollo y operaciones.

- `setup.sh`: Script para preparar dependencias y configurar el entorno de trabajo.
- `deploy.sh`: Script de automatización para empaquetado y despliegue del aplicativo.
- `test.sh`: Script para la ejecución automática de la suite de pruebas.

### temp/
Directorio de trabajo auxiliar, pruebas de concepto y archivos temporales.

- `checklist/`: Prototipo aislado de Spring Boot para pruebas del módulo de checklist de viaje.
- `temp_data/`: Pruebas tempranas de lógica en Java (algoritmos iniciales de recomendación y selección) e imágenes temporales.
- `Preguntas_de_Preferencia_FIS.pdf` y `temp_file.txt`: Insumos y notas preliminares del equipo.

### Archivos en la raíz del proyecto

- `.gitignore`: Reglas para excluir del control de versiones archivos compilados, temporales y configuraciones locales.
- `BOILERPLATE_template.md`: Plantilla y guía de referencia sobre el diseño de la estructura del repositorio.
- `CHANGELOG.md`: Registro cronológico de versiones, adiciones y correcciones del proyecto.
- `CONTRIBUTING.md`: Lineamientos sobre estándares de código, flujo de ramas y convenciones para colaborar.
- `Dockerfile`: Instrucciones de construcción de la imagen contenedora para WiseTrip.
- `docker-compose.yml`: Orquestación multicontenedor para levantar los servicios del proyecto de forma reproducible.
- `estructura.txt`: Snapshot o volcado en texto plano de la estructura del árbol de archivos.
- `LICENSE`: Términos de la licencia de software bajo la cual se distribuye el código.
- `Makefile`: Atajos de comandos de terminal para compilar, probar y ejecutar tareas repetitivas.
- `README.md`: Documento principal de presentación con instrucciones de instalación, ejecución y visión general del proyecto.

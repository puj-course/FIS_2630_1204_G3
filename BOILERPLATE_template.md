# fis_boilerplate
## Descripción de cada directorio y archivos
```bash
FIS_2630_1204_G3/
├── .idea/
│   ├── compiler.xml
│   ├── encodings.xml
│   ├── FIS_2630_1204_G3.iml
│   ├── inspectionProfiles/
│   │   └── Project_Default.xml
│   ├── jarRepositories.xml
│   ├── misc.xml
│   ├── modules.xml
│   ├── vcs.xml
│   └── workspace.xml
├── app/
│   ├── Entrega1/
│   │   └── wisetrip_entrega1/
│   │       ├── .mvn/
│   │       │   └── wrapper/
│   │       │       └── maven-wrapper.properties
│   │       ├── mvnw
│   │       ├── mvnw.cmd
│   │       ├── pom.xml
│   │       └── src/
│   │           ├── main/
│   │           │   ├── java/com/wisetrip/
│   │           │   │   ├── controlador/
│   │           │   │   │   ├── AuthControlador.java
│   │           │   │   │   ├── InicioControlador.java
│   │           │   │   │   ├── PlanificacionControlador.java
│   │           │   │   │   ├── RecomendacionControlador.java
│   │           │   │   │   └── ViajeControlador.java
│   │           │   │   ├── datos/
│   │           │   │   │   ├── CiudadDAO.java
│   │           │   │   │   ├── CiudadSemilla.java
│   │           │   │   │   ├── ConexionBD.java
│   │           │   │   │   ├── DatosCiudades.java
│   │           │   │   │   ├── PreferenciaDAO.java
│   │           │   │   │   ├── UsuarioDAO.java
│   │           │   │   │   └── ViajeDAO.java
│   │           │   │   ├── modelo/
│   │           │   │   │   ├── CategoriaPreferencia.java
│   │           │   │   │   ├── Ciudad.java
│   │           │   │   │   ├── FechasViaje.java
│   │           │   │   │   ├── Peso.java
│   │           │   │   │   ├── Preferencias.java
│   │           │   │   │   ├── PreferenciasUsuario.java
│   │           │   │   │   ├── Pregunta.java
│   │           │   │   │   ├── Presupuesto.java
│   │           │   │   │   ├── ResultadoRecomendacion.java
│   │           │   │   │   ├── SeleccionDestinos.java
│   │           │   │   │   ├── TipoAtributo.java
│   │           │   │   │   ├── Ubicacion.java
│   │           │   │   │   └── Usuario.java
│   │           │   │   ├── negocio/
│   │           │   │   │   ├── CatalogoPreguntas.java
│   │           │   │   │   └── DefPregunta.java
│   │           │   │   ├── servicio/
│   │           │   │   │   ├── CatalogoCiudades.java
│   │           │   │   │   ├── FechasServicio.java
│   │           │   │   │   ├── LlenarAtributosCiudad.java
│   │           │   │   │   ├── LlenarLasCiudades.java
│   │           │   │   │   ├── PreferenciasServicio.java
│   │           │   │   │   ├── PresupuestoServicio.java
│   │           │   │   │   ├── RecomendadorDestinos.java
│   │           │   │   │   ├── SelectorDestinos.java
│   │           │   │   │   ├── ServicioGeoapify.java
│   │           │   │   │   ├── UsuarioServicio.java
│   │           │   │   │   └── ViajeServicio.java
│   │           │   │   ├── ServletInitializer.java
│   │           │   │   └── WisetripApplication.java
│   │           │   └── resources/
│   │           │       ├── application.properties
│   │           │       ├── sql/
│   │           │       │   ├── alter_usuario_registro.sql
│   │           │       │   └── insert_ciudades.sql
│   │           │       └── static/css/
│   │           │           ├── estilos.css
│   │           │           └── landing.css
│   │           ├── webapp/WEB-INF/vistas/
│   │           │   ├── fechas.jsp
│   │           │   ├── landing.jsp
│   │           │   ├── login.jsp
│   │           │   ├── origen.jsp
│   │           │   ├── preferencias.jsp
│   │           │   ├── presupuesto.jsp
│   │           │   ├── recomendaciones.jsp
│   │           │   ├── registro-exitoso.jsp
│   │           │   ├── registro.jsp
│   │           │   └── resumen.jsp
│   │           ├── README.md
│   │           └── test/java/com/wisetrip/
│   │               └── WisetripApplicationTests.java
│   ├── index.js
│   └── package.json
├── conf/
│   ├── config.yaml
│   └── settings.json
├── database/
│   ├── BasesNegocio.md
│   ├── database.md
│   ├── DDL.md
│   ├── Diagrama entidad-relacion bdd 2.svg
│   └── DiccionarioDatos.md
├── docs/
│   ├── api/
│   │   ├── APIs de clima wisetrip (3).pdf
│   │   ├── API_Geoapify_WiseTrip.pdf
│   │   └── F.I.S Implementación API Pasarela de Pagos.pdf
│   ├── architecture/
│   │   ├── Arquitectura WiseTrip.svg
│   │   ├── ArquitecturaInicial.md
│   │   ├── CodigoInicialBoceto.jsx
│   │   ├── DocumentacionTecnicaPaginaWeb.md
│   │   └── mockupInicial.md
│   ├── user_guide/
│   │   └── README.md
│   ├── DefinicionProyecto.md
│   ├── RequerimientosFuncionales.md
│   └── RequerimientosNoFuncionales.md
├── jupyter/
│   ├── datasets/
│   └── notebooks/
├── scripts/
│   ├── deploy.sh
│   ├── setup.sh
│   └── test.sh
├── src/
│   ├── main/
│   └── test/
│       ├── java/
│       └── resources/
├── temp/
│   ├── Preguntas_de_Preferencia_FIS.pdf
│   ├── temp_data/
│   │   ├── Captura de pantalla 2026-08-31 221922.png
│   │   ├── Ciudad.java
│   │   ├── PreferenciasUsuario.java
│   │   ├── RecomendadorDestinos.java
│   │   ├── ResultadoRecomendacion.java
│   │   ├── SeleccionDestinos.java
│   │   ├── SelectorDestinos.java
│   │   ├── temp1.tmp
│   │   └── temp2.tmp
│   └── temp_file.txt
├── BOILERPLATE_template.md
├── CHANGELOG.md
├── CONTRIBUTING.md
├── docker-compose.yml
├── Dockerfile
├── estructura.txt
├── LICENSE
├── Makefile
├── README.md
```


### .github/
Contiene configuraciones específicas para GitHub, como plantillas para problemas (issues) y solicitudes de extracción (pull requests), y flujos de trabajo de GitHub Actions para integración continua (CI) y despliegue continuo (CD).

- `ISSUE_TEMPLATE/`: Plantillas para reportar bugs y solicitar nuevas características.
- `workflows/`: Archivos YAML para definir los flujos de trabajo de CI/CD.

### docs/
Documentación del proyecto.

- `api/`: Documentación de la API.
- `architecture/`: Diagramas y documentación de la arquitectura.
- `user_guide/`: Guías para usuarios.

### src/
Código fuente del proyecto.

- `main/`: Código fuente principal.
  - `java/` (o `python/`, etc.): Código fuente del proyecto según el lenguaje utilizado.
  - `resources/`: Archivos de recursos como configuraciones y otros archivos necesarios.
- `test/`: Código de pruebas.
  - `java/` (o `python/`, etc.): Código de pruebas unitarias y de integración.
  - `resources/`: Archivos de recursos para las pruebas.

### scripts/
Scripts útiles para tareas comunes como configuración, despliegue y pruebas.

- `setup.sh`: Script para configurar el entorno de desarrollo.
- `deploy.sh`: Script para despliegue.
- `test.sh`: Script para ejecutar pruebas.

### conf/
Carpeta para archivos de configuración.

- `config.yaml`: Archivo de configuración en formato YAML.
- `settings.json`: Archivo de configuración en formato JSON.

### jupyter/
Carpeta para los notebooks de Jupyter y datasets utilizados.

- `notebooks/`: Carpeta para los notebooks de Jupyter.
  - `exploration.ipynb`: Notebook para la exploración de datos.
  - `analysis.ipynb`: Notebook para el análisis de datos.
- `datasets/`: Carpeta para los datasets utilizados en los notebooks.
  - `data1.csv`: Ejemplo de dataset en formato CSV.
  - `data2.csv`: Otro ejemplo de dataset en formato CSV.

### temp/
Carpeta para archivos temporales.

- `temp_file.txt`: Archivo temporal de ejemplo.
- `temp_data/`: Subcarpeta para datos temporales.
  - `temp1.tmp`: Archivo temporal de ejemplo.
  - `temp2.tmp`: Otro archivo temporal de ejemplo.

### Archivos en la raíz del proyecto

- `.gitignore`: Archivo para especificar qué archivos y directorios deben ser ignorados por Git.
- `README.md`: Descripción general del proyecto, instrucciones de instalación, uso, contribución, etc.
- `LICENSE`: Información sobre la licencia del proyecto.
- `CHANGELOG.md`: Registro de cambios en el proyecto.
- `CONTRIBUTING.md`: Guía para contribuir al proyecto.
- `Dockerfile`: Archivo para construir la imagen Docker del proyecto.
- `docker-compose.yml`: Archivo de configuración para Docker Compose.
- `Makefile`: Archivo para automatizar tareas mediante comandos `make`.

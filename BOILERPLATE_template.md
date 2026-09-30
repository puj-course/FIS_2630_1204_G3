
# fis_boilerplate
## Descripción de cada directorio y archivos
```bash
FIS_2630_1204_G3/
├── .github/
│   ├── ISSUE_TEMPLATE/
│   │   ├── bug_report.md
│   │   └── feature_request.md
│   └── PULL_REQUEST_TEMPLATE.md
├── .idea/
│   ├── inspectionProfiles/
│   │   └── Project_Default.xml
│   ├── .gitignore
│   ├── compiler.xml
│   ├── encodings.xml
│   ├── FIS_2630_1204_G3.iml
│   ├── jarRepositories.xml
│   ├── misc.xml
│   ├── modules.xml
│   ├── vcs.xml
│   └── workspace.xml
├── .vscode/
│   └── launch.json
├── app/
│   ├── Entrega1/
│   │   └── wisetrip_entrega1/
│   │       ├── .mvn/
│   │       │   └── wrapper/
│   │       │       └── maven-wrapper.properties
│   │       ├── src/
│   │       │   ├── main/
│   │       │   │   ├── java/com/wisetrip/
│   │       │   │   │   ├── controlador/
│   │       │   │   │   │   ├── AuthControlador.java
│   │       │   │   │   │   ├── InicioControlador.java
│   │       │   │   │   │   ├── NotificacionController.java
│   │       │   │   │   │   ├── PlanControlador.java
│   │       │   │   │   │   ├── PlanificacionControlador.java
│   │       │   │   │   │   ├── RecomendacionControlador.java
│   │       │   │   │   │   ├── SeleccionControlador.java
│   │       │   │   │   │   ├── TelegramControlador.java
│   │       │   │   │   │   └── ViajeControlador.java
│   │       │   │   │   ├── datos/
│   │       │   │   │   │   ├── CiudadDAO.java
│   │       │   │   │   │   ├── CiudadSemilla.java
│   │       │   │   │   │   ├── ConexionBD.java
│   │       │   │   │   │   ├── DatosCiudades.java
│   │       │   │   │   │   ├── PreferenciaDAO.java
│   │       │   │   │   │   ├── UsuarioDAO.java
│   │       │   │   │   │   └── ViajeDAO.java
│   │       │   │   │   ├── modelo/
│   │       │   │   │   │   ├── CategoriaPreferencia.java
│   │       │   │   │   │   ├── Ciudad.java
│   │       │   │   │   │   ├── FechasViaje.java
│   │       │   │   │   │   ├── Peso.java
│   │       │   │   │   │   ├── Preferencias.java
│   │       │   │   │   │   ├── PreferenciasUsuario.java
│   │       │   │   │   │   ├── Pregunta.java
│   │       │   │   │   │   ├── Presupuesto.java
│   │       │   │   │   │   ├── RepartoPresupuesto.java
│   │       │   │   │   │   ├── ResultadoRecomendacion.java
│   │       │   │   │   │   ├── SeleccionDestinos.java
│   │       │   │   │   │   ├── TipoAtributo.java
│   │       │   │   │   │   ├── Ubicacion.java
│   │       │   │   │   │   └── Usuario.java
│   │       │   │   │   ├── negocio/
│   │       │   │   │   │   ├── CatalogoPreguntas.java
│   │       │   │   │   │   └── DefPregunta.java
│   │       │   │   │   ├── servicio/
│   │       │   │   │   │   ├── CatalogoCiudades.java
│   │       │   │   │   │   ├── EmailNotificationService.java
│   │       │   │   │   │   ├── FechasServicio.java
│   │       │   │   │   │   ├── LlenarAtributosCiudad.java
│   │       │   │   │   │   ├── LlenarLasCiudades.java
│   │       │   │   │   │   ├── PreferenciasServicio.java
│   │       │   │   │   │   ├── PresupuestoServicio.java
│   │       │   │   │   │   ├── RecomendadorDestinos.java
│   │       │   │   │   │   ├── RepartoServicio.java
│   │       │   │   │   │   ├── SelectorDestinos.java
│   │       │   │   │   │   ├── ServicioGeoapify.java
│   │       │   │   │   │   ├── TelegramNotificationService.java
│   │       │   │   │   │   ├── UsuarioServicio.java
│   │       │   │   │   │   └── ViajeServicio.java
│   │       │   │   │   ├── ServletInitializer.java
│   │       │   │   │   └── WisetripApplication.java
│   │       │   │   ├── resources/
│   │       │   │   │   ├── sql/
│   │       │   │   │   │   ├── alter_usuario_registro.sql
│   │       │   │   │   │   └── insert_ciudades.sql
│   │       │   │   │   ├── static/
│   │       │   │   │   │   ├── css/
│   │       │   │   │   │   │   ├── img/
│   │       │   │   │   │   │   │   └── banderas/
│   │       │   │   │   │   │   │       ├── ARS.svg
│   │       │   │   │   │   │   │       ├── BOB.svg
│   │       │   │   │   │   │   │       ├── BRL.svg
│   │       │   │   │   │   │   │       ├── BZD.svg
│   │       │   │   │   │   │   │       ├── CLP.svg
│   │       │   │   │   │   │   │       ├── COP.svg
│   │       │   │   │   │   │   │       ├── CRC.svg
│   │       │   │   │   │   │   │       ├── CUP.svg
│   │       │   │   │   │   │   │       ├── DOP.svg
│   │       │   │   │   │   │   │       ├── GTQ.svg
│   │       │   │   │   │   │   │       ├── HNL.svg
│   │       │   │   │   │   │   │       ├── MXN.svg
│   │       │   │   │   │   │   │       ├── NIO.svg
│   │       │   │   │   │   │   │       ├── PAB.svg
│   │       │   │   │   │   │   │       ├── PEN.svg
│   │       │   │   │   │   │   │       ├── PYG.svg
│   │       │   │   │   │   │   │       ├── USD.svg
│   │       │   │   │   │   │   │       ├── UYU.svg
│   │       │   │   │   │   │   │       └── VES.svg
│   │       │   │   │   │   │   ├── estilos.css
│   │       │   │   │   │   │   ├── fechas.css
│   │       │   │   │   │   │   ├── landing.css
│   │       │   │   │   │   │   ├── login.css
│   │       │   │   │   │   │   ├── origen.css
│   │       │   │   │   │   │   ├── plan.css
│   │       │   │   │   │   │   ├── presupuesto.css
│   │       │   │   │   │   │   ├── recomendaciones.css
│   │       │   │   │   │   │   ├── registro.css
│   │       │   │   │   │   │   └── resumen.css
│   │       │   │   │   │   └── img/
│   │       │   │   │   │       ├── banderas/
│   │       │   │   │   │       │   ├── ARS.png
│   │       │   │   │   │       │   ├── BOB.png
│   │       │   │   │   │       │   ├── BRL.png
│   │       │   │   │   │       │   ├── BZD.png
│   │       │   │   │   │       │   ├── CLP.png
│   │       │   │   │   │       │   ├── COP.png
│   │       │   │   │   │       │   ├── CRC.png
│   │       │   │   │   │       │   ├── CUP.png
│   │       │   │   │   │       │   ├── DOP.png
│   │       │   │   │   │       │   ├── GTQ.png
│   │       │   │   │   │       │   ├── HNL.png
│   │       │   │   │   │       │   ├── MXN.png
│   │       │   │   │   │       │   ├── NIO.png
│   │       │   │   │   │       │   ├── PAB.png
│   │       │   │   │   │       │   ├── PEN.png
│   │       │   │   │   │       │   ├── PYG.png
│   │       │   │   │   │       │   ├── USD.png
│   │       │   │   │   │       │   ├── UYU.png
│   │       │   │   │   │       │   └── VES.png
│   │       │   │   │   │       ├── portada/
│   │       │   │   │   │       │   ├── Amazonas.jpg
│   │       │   │   │   │       │   ├── Argentina.jpg
│   │       │   │   │   │       │   ├── Cartegena.jpg
│   │       │   │   │   │       │   ├── desierto.jpg
│   │       │   │   │   │       │   ├── logo.png
│   │       │   │   │   │       │   ├── Peru.jpg
│   │       │   │   │   │       │   └── Rio.jpg
│   │       │   │   │   │       ├── preferencias/
│   │       │   │   │   │       │   ├── aventura.png
│   │       │   │   │   │       │   ├── comida_internacional.png
│   │       │   │   │   │       │   ├── compras.png
│   │       │   │   │   │       │   ├── desierto.png
│   │       │   │   │   │       │   ├── festivo.png
│   │       │   │   │   │       │   ├── gourmet.png
│   │       │   │   │   │       │   ├── lujo.png
│   │       │   │   │   │       │   ├── mochilero.png
│   │       │   │   │   │       │   ├── montana.png
│   │       │   │   │   │       │   ├── muchas_actividades.png
│   │       │   │   │   │       │   ├── naturaleza.png
│   │       │   │   │   │       │   ├── nieve.png
│   │       │   │   │   │       │   ├── off_the_beaten_path.png
│   │       │   │   │   │       │   ├── playa.png
│   │       │   │   │   │       │   ├── restricciones_alimentarias.png
│   │       │   │   │   │       │   ├── romantico.png
│   │       │   │   │   │       │   ├── tiempo_libre.png
│   │       │   │   │   │       │   ├── tranquilo.png
│   │       │   │   │   │       │   ├── urbano.png
│   │       │   │   │   │       │   └── vida_nocturna.png
│   │       │   │   │   │       └── resumen/
│   │       │   │   │   │           ├── monteverde.jpg
│   │       │   │   │   │           ├── pines.jpg
│   │       │   │   │   │           ├── salar.jpg
│   │       │   │   │   │           └── tulum.jpg
│   │       │   │   │   └── application.properties
│   │       │   │   └── webapp/WEB-INF/vistas/
│   │       │   │       ├── encabezado.jsp
│   │       │   │       ├── fechas.jsp
│   │       │   │       ├── landing.jsp
│   │       │   │       ├── login.jsp
│   │       │   │       ├── origen.jsp
│   │       │   │       ├── plan.jsp
│   │       │   │       ├── preferencias.jsp
│   │       │   │       ├── presupuesto.jsp
│   │       │   │       ├── recomendaciones.jsp
│   │       │   │       ├── registro-exitoso.jsp
│   │       │   │       ├── registro.jsp
│   │       │   │       ├── resumen.jsp
│   │       │   │       └── vincular-telegram.jsp
│   │       │   ├── test/
│   │       │   │   └── java/com/wisetrip/
│   │       │   │       └── WisetripApplicationTests.java
│   │       │   ├── .gitignore
│   │       │   └── README.md
│   │       ├── .gitattributes
│   │       ├── .gitignore
│   │       ├── mvnw
│   │       ├── mvnw.cmd
│   │       └── pom.xml
│   ├── index.js
│   └── package.json
├── conf/
│   ├── config.yaml
│   └── settings.json
├── database/
│   ├── .gitkeep
│   ├── Base_de_datos_Compartida.pdf
│   ├── BasesNegocio.md
│   ├── database.md
│   ├── DDL.md
│   ├── Diagrama entidad-relacion bdd 2.svg
│   └── DiccionarioDatos.md
├── docs/
│   ├── api/
│   │   ├── .gitkeep
│   │   ├── API_Geoapify_WiseTrip.pdf
│   │   ├── APIs de clima wisetrip (3).pdf
│   │   └── F.I.S Implementación API Pasarela de Pagos.pdf
│   ├── architecture/
│   │   ├── .gitkeep
│   │   ├── Arquitectura WiseTrip.svg
│   │   ├── ArquitecturaInicial.md
│   │   ├── CodigoInicialBoceto.jsx
│   │   ├── DocumentacionTecnicaPaginaWeb.md
│   │   └── mockupInicial.md
│   ├── user_guide/
│   │   ├── .gitkeep
│   │   └── README.md
│   ├── 1raEntrega_FIS_G3.pdf
│   ├── DefinicionProyecto.md
│   ├── RequerimientosFuncionales.md
│   ├── RequerimientosNoFuncionales.md
│   └── WiseTrip.mp4
├── scripts/
│   ├── deploy.sh
│   ├── setup.sh
│   └── test.sh
├── temp/
│   ├── checklist/
│   │   ├── src/main/java/com/wisetrip/
│   │   │   └── ChecklistMockupApplication.java
│   │   ├── target/classes/com/wisetrip/
│   │   │   ├── ChecklistMockupApplication$ChecklistControlador.class
│   │   │   └── ChecklistMockupApplication.class
│   │   └── pom.xml
│   ├── temp_data/
│   │   ├── Captura de pantalla 2026-08-31 221922.png
│   │   ├── Ciudad.java
│   │   ├── PreferenciasUsuario.java
│   │   ├── RecomendadorDestinos.java
│   │   ├── ResultadoRecomendacion.java
│   │   ├── SeleccionDestinos.java
│   │   ├── SelectorDestinos.java
│   │   ├── temp1.tmp
│   │   ├── temp2.tmp
│   │   └── VistaDestinosFX.java
│   ├── .gitkeep
│   ├── Preguntas_de_Preferencia_FIS.pdf
│   └── temp_file.txt
├── .gitignore
├── BOILERPLATE_template.md
├── CHANGELOG.md
├── CONTRIBUTING.md
├── docker-compose.yml
├── Dockerfile
├── estructura.txt
├── FIS_2630_1204_G3
├── FIS_2630_1204_G3-1
├── LICENSE
├── Makefile
└── README.md
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

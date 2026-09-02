# WiseTrip

Aplicación web para planificación de viajes con control de presupuesto.

**Curso:** Fundamentos de Ingeniería de Software  
**Grupo:** FIS_2630_1204_G3  
**Sprint:** 3

---

## Tecnologías

| Componente | Versión |
|---|---|
| Java | 21 |
| Spring Boot (Spring MVC) | 4.1.1 |
| Vistas | JSP + JSTL |
| Gestor de dependencias | Maven |
| Servidor | Tomcat embebido |
| Persistencia | En memoria (sin base de datos en este sprint) |

Arquitectura por capas siguiendo el patrón Modelo–Vista–Controlador.

---

## Requisitos

- JDK 21 o superior
- No requiere instalar Maven ni Tomcat: el proyecto los incluye

---

## Cómo ejecutar

### Opción 1 — Desde el código fuente

Windows:
```
mvnw.cmd spring-boot:run
```

Linux / Mac:
```
./mvnw spring-boot:run
```

### Opción 2 — Desde el archivo empaquetado

```
java -jar wisetrip-0.0.1-SNAPSHOT.war
```

En ambos casos, abrir en el navegador:

```
http://localhost:8090
```

Si el puerto 8090 está ocupado, se puede cambiar en
`src/main/resources/application.properties`, en la línea `server.port`.

---

## Historias de usuario implementadas

| Historia | Descripción | Ruta |
|---|---|---|
| HU#22 | Registro de usuario con correo y contraseña | `/registro` |
| HU#23 | Inicio de sesión | `/login` |
| HU#24 | Indicar ubicación de origen | `/origen` |

### Flujo de navegación

```
/  (portada)
   └── /registro  →  /registro-exitoso
                          └── /login  →  /origen  →  /resumen
```

Las rutas `/origen` y `/resumen` están protegidas: si no hay una sesión
activa, redirigen automáticamente a `/login`.

---

## Validaciones implementadas

**Registro (HU#22)**
- Nombre completo obligatorio, mínimo 3 caracteres
- Tipo de documento obligatorio (CC, CE, TI, PA)
- Número de documento entre 5 y 15 dígitos, sin duplicados
- Fecha de nacimiento obligatoria, no futura, mayor de 18 años
- Correo con formato válido y sin duplicados
- Contraseña de mínimo 6 caracteres, con confirmación

**Inicio de sesión (HU#23)**
- Campos obligatorios
- Verificación de credenciales contra los usuarios registrados
- Mensaje de error genérico por seguridad (no revela cuál de los dos campos falló)

**Ubicación de origen (HU#24)**
- País obligatorio, validado contra la lista permitida
- Ciudad obligatoria, validada como perteneciente al país seleccionado
- Punto de partida opcional, máximo 60 caracteres

---

## Estructura del proyecto

```
src/main/
├── java/com/wisetrip/
│   ├── WisetripApplication.java     Clase principal
│   ├── modelo/
│   │   ├── Usuario.java
│   │   └── Ubicacion.java
│   ├── servicio/
│   │   ├── UsuarioServicio.java     Registro, autenticación, validaciones
│   │   └── ViajeServicio.java       Países, ciudades y validaciones
│   └── controlador/
│       ├── InicioControlador.java   Portada
│       ├── AuthControlador.java     Registro y login
│       └── ViajeControlador.java    Ubicación de origen
├── resources/
│   ├── static/css/                  Hojas de estilo
│   └── application.properties       Configuración
└── webapp/WEB-INF/vistas/           Páginas JSP
```

---

## Consideraciones

Los datos se almacenan en memoria mediante un `ArrayList` gestionado por
`UsuarioServicio`, anotado con `@Service` para que Spring mantenga una
única instancia compartida. Por lo tanto, los usuarios registrados se
pierden al detener la aplicación.

La integración con base de datos y el hasheo de contraseñas con BCrypt
están previstos para el siguiente sprint.
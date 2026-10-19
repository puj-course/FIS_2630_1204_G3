## Cómo funciona el módulo de inicio de sesión

El dominio (lo que el negocio sabe hacer) está separado de la tecnología que lo ejecuta (Spring, base de datos, Telegram). Las capas se comunican por **interfaces**, así que ninguna depende de cómo está hecha la otra.

### Qué hace cada pieza

| Capa | Pieza | Qué hace |
|---|---|---|
| **Dominio** | `Usuario` | Conoce sus reglas: comprueba si la contraseña coincide y si tiene Telegram vinculado |
| | `Notificacion` | Guarda el asunto y el cuerpo de un mensaje |
| | `CreadorNotificacion` / `CreadorNotificacionInicioSesion` | Deciden qué mensaje se arma: el aviso de inicio de sesión (Factory Method) |
| **Contratos** | `IAutenticacionUsuario` | Iniciar sesión |
| | `IUsuarioRepositorio` | Buscar un usuario por correo |
| | `INotificador` | Enviar un mensaje por un canal |
| **Servicio** | `UsuarioServicio` | Coordina; no tiene reglas propias |
| | `NotificadorTelegram` | Envía el mensaje por la API de Telegram |
| **Datos** | `UsuarioDAO`, `ConexionBD` | Consultan la base de datos y abren la conexión |
| **Controlador** | `AuthControlador` | Recibe la petición web y llama al contrato, sin lógica propia |

### Flujo (`POST /login`)

1. `AuthControlador` revisa que el correo y la contraseña no vengan vacíos y llama a `iniciarSesion`.
2. `UsuarioServicio` busca al usuario por correo en el repositorio y le pregunta al propio `Usuario` si la contraseña coincide.
3. Si el usuario no existe o la contraseña no coincide, devuelve `null` y el controlador vuelve al formulario con el error.
4. Si tiene Telegram vinculado, `NotificadorTelegram` envía el aviso de inicio de sesión. Si el envío falla, el login no se cancela.
5. El controlador guarda al usuario en la sesión y redirige a `/origen`.

Para cerrar sesión (`GET /logout`), el controlador invalida la sesión y redirige a `/`.

### Decisiones de diseño

- **Dominio sin tecnología:** `Usuario` no sabe de base de datos ni de Telegram.
- **Servicio que solo coordina:** la verificación de la contraseña vive en `Usuario`, no en `UsuarioServicio`.
- **Interfaces como contratos:** el controlador depende de `IAutenticacionUsuario`, no de `UsuarioServicio`, y el servicio depende de `IUsuarioRepositorio`, no de JDBC. Se puede cambiar una implementación sin tocar quien la usa.
- **Canal nuevo, clase nueva:** para avisar por otro medio basta con otra clase que implemente `INotificador`.
- **Qué se dice vs. por dónde se envía:** el contenido del mensaje es del negocio (`Creador…` y `Notificacion`); el canal es tecnología (`NotificadorTelegram`).

## Diagrama de interfaces 

<img width="2431" height="1383" alt="Untitled (6)" src="https://github.com/user-attachments/assets/2e9feef2-d2d7-43d4-8dbd-838e96427fc3" />


```plantuml
@startuml WiseTrip_InicioSesion
title WiseTrip — Diagrama de interfaces: módulo de Inicio de sesión

skinparam classAttributeIconSize 0
skinparam shadowing false
skinparam packageStyle rectangle
skinparam nodesep 50
skinparam ranksep 70
skinparam class {
  BackgroundColor #FFFFFF
  BorderColor #444444
  ArrowColor #555555
}
set separator none
hide empty members

package "Externas (JDK)" #F5F5F5 {
  class HttpClient <<java.net.http>>
}

package "com.wisetrip.modelo" #FFFBE6 {
  class Usuario {
    + {static} EDAD_MINIMA: int = 18
    + {static} EDAD_MAXIMA: int = 120
    + {static} LARGO_MINIMO_PASSWORD: int = 6
    - idUsuario: int
    - nombreCompleto: String
    - tipoDocumento: String
    - numeroDocumento: String
    - fechaNacimiento: String
    - correo: String
    - password: String
    - rol: String = "cliente"
    - chatId: String
    --
    + verificarPassword(passwordIngresada: String): boolean
    + tieneTelegramVinculado(): boolean
    + getChatId(): String
  }

  class Notificacion {
    - asunto: String
    - cuerpo: String
    --
    + Notificacion(asunto: String, cuerpo: String)
    + getAsunto(): String
    + getCuerpo(): String
  }

  abstract class CreadorNotificacion {
    # {abstract} crearNotificacion(usuario: Usuario): Notificacion
    + notificar(usuario: Usuario, destino: String, notificador: INotificador): void
  }

  class CreadorNotificacionInicioSesion {
    # crearNotificacion(usuario: Usuario): Notificacion
  }
}

package "com.wisetrip.negocio" #E8F4FF {
  interface IAutenticacionUsuario {
    + iniciarSesion(correo: String, password: String): Usuario
  }
  interface IUsuarioRepositorio {
    + buscarPorCorreo(correo: String): Usuario
  }
  interface INotificador {
    + enviar(destino: String, notificacion: Notificacion): void
  }
}

package "com.wisetrip.servicio" #EAF7EA {
  class UsuarioServicio <<@Service>> {
    - repositorio: IUsuarioRepositorio
    - notificadorTelegram: INotificador
    --
    + iniciarSesion(correo: String, password: String): Usuario
    - notificar(creador: CreadorNotificacion, usuario: Usuario, destino: String, notificador: INotificador): void
  }

  class NotificadorTelegram <<@Service("notificadorTelegram")>> {
    - botToken: String
    - httpClient: HttpClient
    --
    + enviar(destino: String, notificacion: Notificacion): void
  }
}

package "com.wisetrip.datos" #FDECEC {
  class UsuarioDAO <<@Repository>> {
    + buscarPorCorreo(correo: String): Usuario
  }
  class ConexionBD {
    - {static} PROPIEDADES: Properties
    --
    + {static} obtenerConexion(): Connection
    - {static} cargarPropiedades(): Properties
  }
}

package "com.wisetrip.controlador" #F1ECFA {
  class AuthControlador <<@Controller>> {
    - autenticacion: IAutenticacionUsuario
    --
    + mostrarLogin(): String  «GET /login»
    + procesarLogin(correo: String, password: String, sesion: HttpSession, model: Model): String  «POST /login»
    + cerrarSesion(sesion: HttpSession): String  «GET /logout»
  }
}

' ===== Herencia / realización =====
UsuarioServicio ..|> IAutenticacionUsuario
NotificadorTelegram ..|> INotificador
UsuarioDAO ..|> IUsuarioRepositorio
CreadorNotificacionInicioSesion --|> CreadorNotificacion

' ===== Asociaciones (atributos) =====
AuthControlador --> IAutenticacionUsuario : autenticacion
UsuarioServicio --> IUsuarioRepositorio : repositorio
UsuarioServicio --> INotificador : notificadorTelegram
NotificadorTelegram --> HttpClient : httpClient

' ===== Dependencias esenciales =====
UsuarioServicio ..> CreadorNotificacionInicioSesion : «create»
CreadorNotificacion ..> Notificacion : «create»
INotificador ..> Notificacion
IAutenticacionUsuario ..> Usuario
IUsuarioRepositorio ..> Usuario
UsuarioDAO ..> ConexionBD

@enduml

## Diagrama de Clases - inicio de sesion 

### Clases

| Capa | Clase | Responsabilidad |
|---|---|---|
| **Controlador** | `AuthControlador` | Recibe la petición web, llama al servicio y guarda o invalida la sesión; sin lógica propia |
| **Servicio** | `UsuarioServicio` | Coordina el login: busca al usuario en el DAO, pide al `Usuario` verificar la contraseña y envía el aviso; no tiene reglas propias |
| | `NotificadorTelegram` | Envía el mensaje por la API de Telegram usando `HttpClient` |
| **Datos** | `UsuarioDAO` | Busca al usuario por correo |
| | `ConexionBD` | Abre la conexión JDBC a PostgreSQL |
| **Dominio** | `Usuario` | Verifica la contraseña y sabe si tiene Telegram vinculado |
| | `Notificacion` | Guarda el asunto y el cuerpo de un mensaje |
| | `CreadorNotificacion` / `CreadorNotificacionInicioSesion` | Deciden qué mensaje se arma (Factory Method) |

### Relaciones

- **Asociaciones:** `AuthControlador` tiene un `UsuarioServicio`; `UsuarioServicio` tiene un `UsuarioDAO` y un `NotificadorTelegram`.
- **Herencia:** `CreadorNotificacionInicioSesion` extiende `CreadorNotificacion`.
- **Creación:** `UsuarioServicio` crea `CreadorNotificacionInicioSesion`; `CreadorNotificacion` crea la `Notificacion`.
- **Dependencias:** el controlador, el servicio, el DAO y `CreadorNotificacion` usan a `Usuario`; `NotificadorTelegram` usa `Notificacion`; `UsuarioDAO` usa `ConexionBD`.

### Flujo

`POST /login`: `AuthControlador` llama a `iniciarSesion`; `UsuarioServicio` busca al usuario por correo en el DAO y le pregunta al `Usuario` si la contraseña coincide. Si no, devuelve `null` y vuelve al formulario. Si coincide y tiene Telegram vinculado, `NotificadorTelegram` envía el aviso (si falla, el login no se cancela). Luego el controlador guarda la sesión y redirige a `/origen`. `GET /logout` invalida la sesión.

> En el código, el controlador y el servicio no se conectan a las clases directamente sino a interfaces (`IAutenticacionUsuario`, `IUsuarioRepositorio`, `INotificador`), que aquí no se dibujan; por eso los atributos muestran el tipo de la interfaz y las flechas apuntan a la clase que la implementa. `UsuarioServicio` tiene más miembros (registro y vinculación de Telegram) que pertenecen a otros módulos.

## imagen del Diagrama 

<img width="826" height="857" alt="Untitled (11)" src="https://github.com/user-attachments/assets/f54c8e0a-2a0b-4f51-acf0-be8036195102" />


## Código del diagrama

```plantuml
@startuml Clases_InicioSesion
title WiseTrip — Diagrama de clases: Inicio de sesión

skinparam classAttributeIconSize 0
skinparam shadowing false
skinparam nodesep 60
skinparam ranksep 80
skinparam class {
  BorderColor #444444
  ArrowColor #555555
}
hide empty members

class AuthControlador <<@Controller>> #F1ECFA {
  - autenticacion: IAutenticacionUsuario
  --
  + mostrarLogin(): String
  + procesarLogin(correo: String, password: String, sesion: HttpSession, model: Model): String
  + cerrarSesion(sesion: HttpSession): String
}
class UsuarioServicio <<@Service>> #EAF7EA {
  - repositorio: IUsuarioRepositorio
  - notificadorTelegram: INotificador
  --
  + iniciarSesion(correo: String, password: String): Usuario
  - notificar(creador: CreadorNotificacion, usuario: Usuario, destino: String, notificador: INotificador): void
}
class NotificadorTelegram <<@Service("notificadorTelegram")>> #EAF7EA {
  - botToken: String
  - httpClient: HttpClient
  --
  + enviar(destino: String, notificacion: Notificacion): void
}
class UsuarioDAO <<@Repository>> #FDECEC {
  + buscarPorCorreo(correo: String): Usuario
}

class ConexionBD #FDECEC {
  - {static} PROPIEDADES: Properties
  --
  + {static} obtenerConexion(): Connection
  - {static} cargarPropiedades(): Properties
}

class Usuario #FFFBE6 {
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
  + getNombreCompleto(): String
  + getChatId(): String
  + verificarPassword(passwordIngresada: String): boolean
  + tieneTelegramVinculado(): boolean
}

class Notificacion #FFFBE6 {
  - asunto: String
  - cuerpo: String
  --
  + Notificacion(asunto: String, cuerpo: String)
  + getAsunto(): String
  + getCuerpo(): String
}

abstract class CreadorNotificacion #FFFBE6 {
  # {abstract} crearNotificacion(usuario: Usuario): Notificacion
  + notificar(usuario: Usuario, destino: String, notificador: INotificador): void
}

class CreadorNotificacionInicioSesion #FFFBE6 {
  # crearNotificacion(usuario: Usuario): Notificacion
}

AuthControlador "1" --> "1" UsuarioServicio : autenticacion
AuthControlador ..> Usuario
UsuarioServicio "1" --> "1" UsuarioDAO : repositorio
UsuarioServicio "1" --> "1" NotificadorTelegram : notificadorTelegram
UsuarioServicio ..> Usuario
UsuarioServicio ..> CreadorNotificacionInicioSesion : «create»
CreadorNotificacionInicioSesion --|> CreadorNotificacion
CreadorNotificacion ..> Usuario
CreadorNotificacion ..> Notificacion : «create»
NotificadorTelegram ..> Notificacion
UsuarioDAO ..> Usuario
UsuarioDAO ..> ConexionBD

@enduml
```

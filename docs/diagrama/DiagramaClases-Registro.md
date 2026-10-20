# Diagrama de Clases de Registro

### Clases

| Capa | Clase | Responsabilidad |
|---|---|---|
| **Controlador** | `AuthControlador` | Recibe la petición web, llama al servicio y guarda al usuario en la sesión; sin lógica propia |
| **Servicio** | `UsuarioServicio` | Coordina el registro: valida con `Usuario`, consulta duplicados al DAO y envía la bienvenida; no tiene reglas propias |
| | `NotificadorCorreo` | Envía el mensaje por SMTP usando `JavaMailSender` |
| **Datos** | `UsuarioDAO` | Inserta el usuario y consulta si el correo o el documento ya existen |
| | `ConexionBD` | Abre la conexión JDBC a PostgreSQL |
| **Dominio** | `Usuario` | Se normaliza y se valida (formato, edad mínima de 18, contraseñas) |
| | `Notificacion` | Guarda el asunto y el cuerpo de un mensaje |
| | `CreadorNotificacion` / `CreadorNotificacionBienvenida` | Deciden qué mensaje se arma (Factory Method) |

### Relaciones

- **Asociaciones:** `AuthControlador` tiene un `UsuarioServicio`; `UsuarioServicio` tiene un `UsuarioDAO` y un `NotificadorCorreo`.
- **Herencia:** `CreadorNotificacionBienvenida` extiende `CreadorNotificacion`.
- **Creación:** `UsuarioServicio` crea `CreadorNotificacionBienvenida`; `CreadorNotificacion` crea la `Notificacion`.
- **Dependencias:** el controlador, el servicio, el DAO y `CreadorNotificacion` usan a `Usuario`; `NotificadorCorreo` usa `Notificacion`; `UsuarioDAO` usa `ConexionBD`.

### Flujo

`POST /registro`: `AuthControlador` llama a `validarRegistro`; `UsuarioServicio` valida con `Usuario` y consulta duplicados al DAO. Si hay errores, vuelve al formulario. Si no, `registrar` normaliza al `Usuario`, el DAO lo guarda y `NotificadorCorreo` envía la bienvenida (si falla, el registro no se cancela). Luego el controlador guarda la sesión y redirige a `/registro-exitoso`.

> En el código, el controlador y el servicio no se conectan a las clases directamente sino a interfaces (`IRegistroUsuario`, `IUsuarioRepositorio`, `INotificador`), que aquí no se dibujan; por eso los atributos muestran el tipo de la interfaz y las flechas apuntan a la clase que la implementa. `UsuarioServicio` tiene más miembros (login y Telegram) que pertenecen a otros módulos.


## Imagen del diagrama 

<img width="1703" height="1533" alt="Untitled (12)" src="https://github.com/user-attachments/assets/1cd81a1e-1ecb-4492-9f9c-bfd9928cec2e" />


## Codigo del diagrama 

```plantuml
@startuml Clases_Registro
title WiseTrip — Diagrama de clases: Registro

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
  - registro: IRegistroUsuario
  --
  + mostrarRegistro(model: Model): String
  + procesarRegistro(usuario: Usuario, confirmarPassword: String, sesion: HttpSession, model: Model, flash: RedirectAttributes): String
  + registroExitoso(): String
}
class UsuarioServicio <<@Service>> #EAF7EA {
  - {static} MSG_DOCUMENTO_EXISTE: String
  - {static} MSG_CORREO_EXISTE: String
  - repositorio: IUsuarioRepositorio
  - notificadorCorreo: INotificador
  --
  + validarRegistro(usuario: Usuario, confirmarPassword: String): Map<String, String>
  + registrar(usuario: Usuario): void
  - notificar(creador: CreadorNotificacion, usuario: Usuario, destino: String, notificador: INotificador): void
}
class NotificadorCorreo <<@Service("notificadorCorreo")>> #EAF7EA {
  - mailSender: JavaMailSender
  --
  + NotificadorCorreo(mailSender: JavaMailSender)
  + enviar(destino: String, notificacion: Notificacion): void
}
class UsuarioDAO <<@Repository>> #FDECEC {
  + registrar(usuario: Usuario): Usuario
  + existeCorreo(correo: String): boolean
  + existeDocumento(numeroDocumento: String): boolean
  - existe(sql: String, valor: String): boolean
  - parseFecha(fecha: String): Date
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
  + getCorreo(): String
  + getNumeroDocumento(): String
  + normalizar(): void
  + normalizarCorreo(): void
  + validarDatos(confirmarPassword: String): Map<String, String>
  + calcularEdad(): int
  + primerNombre(): String
  + iniciales(): String
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

class CreadorNotificacionBienvenida #FFFBE6 {
  # crearNotificacion(usuario: Usuario): Notificacion
}

AuthControlador "1" --> "1" UsuarioServicio : registro
AuthControlador ..> Usuario
UsuarioServicio "1" --> "1" UsuarioDAO : repositorio
UsuarioServicio "1" --> "1" NotificadorCorreo : notificadorCorreo
UsuarioServicio ..> Usuario
UsuarioServicio ..> CreadorNotificacionBienvenida : «create»
CreadorNotificacionBienvenida --|> CreadorNotificacion
CreadorNotificacion ..> Usuario
CreadorNotificacion ..> Notificacion : «create»
NotificadorCorreo ..> Notificacion
UsuarioDAO ..> Usuario
UsuarioDAO ..> ConexionBD

@enduml

```








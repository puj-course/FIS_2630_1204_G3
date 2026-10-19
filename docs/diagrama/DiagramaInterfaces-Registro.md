## Cómo funciona el módulo de registro

El dominio (lo que el negocio sabe hacer) está separado de la tecnología que lo ejecuta (Spring, base de datos, correo). Las capas se comunican por **interfaces**, así que ninguna depende de cómo está hecha la otra.

### Qué hace cada pieza

| Capa | Pieza | Qué hace |
|---|---|---|
| **Dominio** | `Usuario` | Conoce sus reglas: se normaliza y se valida (formato, edad, contraseñas) |
| | `Notificacion` | Guarda el asunto y el cuerpo de un mensaje |
| | `CreadorNotificacion` / `CreadorNotificacionBienvenida` | Deciden qué mensaje se arma (Factory Method) |
| **Contratos** | `IRegistroUsuario` | Validar y registrar un usuario |
| | `IUsuarioRepositorio` | Guardar usuarios y comprobar si ya existen |
| | `INotificador` | Enviar un mensaje por un canal |
| **Servicio** | `UsuarioServicio` | Coordina; no tiene reglas propias |
| | `NotificadorCorreo` | Envía el mensaje por SMTP |
| **Datos** | `UsuarioDAO`, `ConexionBD` | Guardan en la base de datos y abren la conexión |
| **Controlador** | `AuthControlador` | Recibe la petición web y llama al contrato, sin lógica propia |

### Flujo (`POST /registro`)

1. `AuthControlador` llama a `validarRegistro`.
2. `UsuarioServicio` le pide al `Usuario` que se valide y consulta al repositorio si el correo o el documento ya existen.
3. Si hay errores, se vuelve al formulario con los mensajes.
4. Si no hay errores, `registrar` normaliza al `Usuario`, el DAO lo guarda y `NotificadorCorreo` envía la bienvenida. Si el correo falla, el registro no se cancela.
5. El controlador guarda al usuario en la sesión y redirige a `/registro-exitoso`.

### Decisiones de diseño

- **Dominio sin tecnología:** `Usuario` no sabe de base de datos ni de correo.
- **Servicio que solo coordina:** las reglas viven en `Usuario`, no en `UsuarioServicio`.
- **Interfaces como contratos:** el controlador depende de `IRegistroUsuario`, no de `UsuarioServicio`, y el servicio depende de `IUsuarioRepositorio`, no de JDBC. Se puede cambiar una implementación sin tocar quien la usa.
- **Canal nuevo, clase nueva:** para enviar por otro medio (por ejemplo SMS) basta con otra clase que implemente `INotificador`.
- **Qué se dice vs. por dónde se envía:** el contenido del mensaje es del negocio (`Creador…` y `Notificacion`); el canal es tecnología (`NotificadorCorreo`).


##Diagrama de interfaces 

<img width="2871" height="1444" alt="Untitled (5)" src="https://github.com/user-attachments/assets/16bbbcfc-d17c-403f-85f5-64528f37f08c" />


```plantuml
@startuml WiseTrip_Registro
title WiseTrip — Diagrama de interfaces: módulo de Registro

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

' ================= EXTERNAS =================
package "Externas (framework)" #F5F5F5 {
  interface JavaMailSender <<org.springframework.mail.javamail>> {
    + send(mensaje: SimpleMailMessage): void
  }
}

' ================= MODELO =================
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
    + normalizar(): void
    + normalizarCorreo(): void
    + validarDatos(confirmarPassword: String): Map<String, String>
    + calcularEdad(): int
    + primerNombre(): String
    + iniciales(): String
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

  class CreadorNotificacionBienvenida {
    # crearNotificacion(usuario: Usuario): Notificacion
  }
}

' ================= NEGOCIO =================
package "com.wisetrip.negocio" #E8F4FF {
  interface IRegistroUsuario {
    + validarRegistro(usuario: Usuario, confirmarPassword: String): Map<String, String>
    + registrar(usuario: Usuario): void
  }
  interface IUsuarioRepositorio {
    + registrar(usuario: Usuario): Usuario
    + existeCorreo(correo: String): boolean
    + existeDocumento(numeroDocumento: String): boolean
  }
  interface INotificador {
    + enviar(destino: String, notificacion: Notificacion): void
  }
}

' ================= SERVICIO =================
package "com.wisetrip.servicio" #EAF7EA {
  class UsuarioServicio <<@Service>> {
    - {static} MSG_DOCUMENTO_EXISTE: String
    - {static} MSG_CORREO_EXISTE: String
    - repositorio: IUsuarioRepositorio
    - notificadorCorreo: INotificador
    --
    + validarRegistro(usuario: Usuario, confirmarPassword: String): Map<String, String>
    + registrar(usuario: Usuario): void
    - notificar(creador: CreadorNotificacion, usuario: Usuario, destino: String, notificador: INotificador): void
  }

  class NotificadorCorreo <<@Service("notificadorCorreo")>> {
    - mailSender: JavaMailSender
    --
    + NotificadorCorreo(mailSender: JavaMailSender)
    + enviar(destino: String, notificacion: Notificacion): void
  }
}

' ================= DATOS =================
package "com.wisetrip.datos" #FDECEC {
  class UsuarioDAO <<@Repository>> {
    + registrar(usuario: Usuario): Usuario
    + existeCorreo(correo: String): boolean
    + existeDocumento(numeroDocumento: String): boolean
    - existe(sql: String, valor: String): boolean
    - parseFecha(fecha: String): Date
  }
  class ConexionBD {
    - {static} PROPIEDADES: Properties
    --
    + {static} obtenerConexion(): Connection
    - {static} cargarPropiedades(): Properties
  }
}

' ================= CONTROLADOR =================
package "com.wisetrip.controlador" #F1ECFA {
  class AuthControlador <<@Controller>> {
    - registro: IRegistroUsuario
    --
    + mostrarRegistro(model: Model): String  «GET /registro»
    + procesarRegistro(usuario: Usuario, confirmarPassword: String, sesion: HttpSession, model: Model, flash: RedirectAttributes): String  «POST /registro»
    + registroExitoso(): String  «GET /registro-exitoso»
  }
}

' ================= HERENCIA / REALIZACION =================
UsuarioServicio ..|> IRegistroUsuario
NotificadorCorreo ..|> INotificador
UsuarioDAO ..|> IUsuarioRepositorio
CreadorNotificacionBienvenida --|> CreadorNotificacion

' ================= ASOCIACIONES (atributos) =================
AuthControlador --> IRegistroUsuario : registro
UsuarioServicio --> IUsuarioRepositorio : repositorio
UsuarioServicio --> INotificador : notificadorCorreo
NotificadorCorreo --> JavaMailSender : mailSender

' ================= DEPENDENCIAS ESENCIALES =================
UsuarioServicio ..> CreadorNotificacionBienvenida : «create»
CreadorNotificacion ..> Notificacion : «create»
INotificador ..> Notificacion
IRegistroUsuario ..> Usuario
IUsuarioRepositorio ..> Usuario
UsuarioDAO ..> ConexionBD

@enduml

```


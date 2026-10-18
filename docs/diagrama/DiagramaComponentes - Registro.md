
## Diagrama de componentes — Registro

### Componentes

| Componente | Qué hace |
|---|---|
| `registro.jsp`, `registro-exitoso.jsp` | Vistas del formulario y de la confirmación |
| `AuthControlador` | Recibe la petición web, llama al contrato de registro y guarda al usuario en la sesión HTTP |
| `UsuarioServicio` | Coordina el registro: valida con `Usuario`, consulta al repositorio y envía la bienvenida |
| Dominio (modelo) | `Usuario` (reglas y validaciones) y `CreadorNotificacionBienvenida` + `Notificacion` (el mensaje) |
| `UsuarioDAO` | Guarda el usuario y comprueba si el correo o el documento ya existen |
| `ConexionBD` | Abre la conexión JDBC a PostgreSQL |
| `NotificadorCorreo` | Envía la bienvenida usando Spring Mail y el servidor SMTP |

### Interfaces

| Interfaz | Entre | Operaciones |
|---|---|---|
| HTTP | Usuario → `AuthControlador` | `GET /registro`, `POST /registro`, `GET /registro-exitoso` |
| `IRegistroUsuario` | `AuthControlador` → `UsuarioServicio` | `validarRegistro()`, `registrar()` |
| `IUsuarioRepositorio` | `UsuarioServicio` → `UsuarioDAO` | `registrar()`, `existeCorreo()`, `existeDocumento()` |
| `INotificador` | `UsuarioServicio` → `NotificadorCorreo` | `enviar()` |
| `JavaMailSender` | `NotificadorCorreo` → Spring Mail | `send()` |
| `HttpSession` | `AuthControlador` → sesión HTTP | `setAttribute()` |

### Flujo

Usuario → `registro.jsp` → `AuthControlador` → `UsuarioServicio` → `UsuarioDAO` → `ConexionBD` → PostgreSQL. Después del guardado, `UsuarioServicio` → `NotificadorCorreo` → Spring Mail → servidor SMTP, y el controlador redirige a `registro-exitoso.jsp`.


# Foto del diagrama 
<img width="2432" height="1077" alt="Untitled (7)" src="https://github.com/user-attachments/assets/f41a8b6c-596a-4039-b030-76c89f58e95b" />

## Codigo del diagrama
```plantuml
@startuml Componentes_Registro
title WiseTrip — Diagrama de componentes: Registro

skinparam componentStyle uml2
skinparam shadowing false
skinparam nodesep 40
skinparam ranksep 70
skinparam component {
  BorderColor #333333
  BorderThickness 1.3
}
skinparam interface {
  BackgroundColor #FFE699
  BorderColor #333333
  FontSize 11
}
skinparam database {
  BackgroundColor #A5D6A7
  BorderColor #333333
}
skinparam cloud {
  BackgroundColor #E3F2FD
  BorderColor #333333
}
skinparam arrow {
  Color #333333
}
left to right direction

actor "Usuario" as User

component "Vistas de registro" as Vistas #FFD9A0 {
  component "registro.jsp" as Registro #FFD9A0
  component "registro-exitoso.jsp" as Exito #FFD9A0
}
component "AuthControlador" as Ctrl #FFD9A0
component "Sesión HTTP\n(contenedor servlet)" as Sesion #F5F5F5

component "UsuarioServicio" as Srv #FFE699
component "Dominio (modelo)" as Dominio #FFFBE6 {
  component "Usuario" as Usr #FFFBE6
  component "CreadorNotificacionBienvenida\n+ Notificacion" as Notif #FFFBE6
}

component "NotificadorCorreo" as Correo #EAF7EA
component "Spring Mail" as SpringMail #F5F5F5

component "UsuarioDAO" as DAO #C8E6C9
component "ConexionBD" as Conn #C8E6C9

database "PostgreSQL\ntabla usuario" as DB
cloud "Servidor SMTP" as SMTP

interface "GET /registro\nPOST /registro\nGET /registro-exitoso" as iWeb
interface "HttpSession\nsetAttribute()" as iSesion
interface "IRegistroUsuario\nvalidarRegistro()\nregistrar()" as iReg
interface "IUsuarioRepositorio\nregistrar()\nexisteCorreo()\nexisteDocumento()" as iRepo
interface "INotificador\nenviar()" as iNot
interface "JavaMailSender\nsend()" as iMail
interface "obtenerConexion()" as iConn
interface "JDBC" as iJdbc
interface "SMTP" as iSmtp

User --( iWeb
iWeb -- Ctrl

Ctrl ..> Registro : devuelve vista
Ctrl ..> Exito : redirect

Ctrl --( iSesion
iSesion -- Sesion

Ctrl --( iReg
iReg -- Srv

Srv ..> Usr : validarDatos()\nnormalizar()
Srv ..> Notif : crea la bienvenida

Srv --( iRepo
iRepo -- DAO

Srv --( iNot
iNot -- Correo

Correo --( iMail
iMail -- SpringMail

SpringMail --( iSmtp
iSmtp -- SMTP

DAO --( iConn
iConn -- Conn

Conn --( iJdbc
iJdbc -- DB

DAO ..> Usr : lee / guarda

@enduml

```



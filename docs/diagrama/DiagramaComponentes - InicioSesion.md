## Diagrama de componentes - Inicio de Sesion 

### Componentes

| Componente | Qué hace |
|---|---|
| `login.jsp`, `origen.jsp` | Formulario de acceso y pantalla a la que se redirige tras iniciar sesión |
| `AuthControlador` | Recibe la petición web, llama al contrato de autenticación y guarda o invalida la sesión HTTP |
| `UsuarioServicio` | Coordina el login: busca al usuario, pide al `Usuario` verificar la contraseña y envía el aviso |
| Dominio (modelo) | `Usuario` (verifica contraseña, sabe si tiene Telegram vinculado) y `CreadorNotificacionInicioSesion` + `Notificacion` (el mensaje) |
| `UsuarioDAO` | Busca al usuario por correo y construye el `Usuario` |
| `ConexionBD` | Abre la conexión JDBC a PostgreSQL |
| `NotificadorTelegram` | Envía el aviso por la API de Telegram |

### Interfaces

| Interfaz | Entre | Operaciones |
|---|---|---|
| HTTP | Usuario → `AuthControlador` | `GET /login`, `POST /login`, `GET /logout` |
| `IAutenticacionUsuario` | `AuthControlador` → `UsuarioServicio` | `iniciarSesion()` |
| `IUsuarioRepositorio` | `UsuarioServicio` → `UsuarioDAO` | `buscarPorCorreo()` |
| `INotificador` | `UsuarioServicio` → `NotificadorTelegram` | `enviar()` (solo si hay Telegram vinculado) |
| HTTP `/sendMessage` | `NotificadorTelegram` → Telegram Bot API | Envío del mensaje |
| `HttpSession` | `AuthControlador` → sesión HTTP | `setAttribute()`, `invalidate()` |

### Flujo

Usuario → `login.jsp` → `AuthControlador` → `UsuarioServicio` → `UsuarioDAO` → `ConexionBD` → PostgreSQL. Si la contraseña coincide y el usuario tiene Telegram vinculado, `UsuarioServicio` → `NotificadorTelegram` → Telegram Bot API. El controlador guarda la sesión y redirige a `origen.jsp`. Si el aviso falla, el login no se cancela.

## Imagen del diagrama de componentes 

<img width="2431" height="717" alt="Untitled (8)" src="https://github.com/user-attachments/assets/6fe1e99b-8634-4048-badc-ecc89b48a5f1" />

## Codigo del diagrama 

```plantuml
@startuml Componentes_InicioSesion
title WiseTrip — Diagrama de componentes: Inicio de sesión

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

component "login.jsp" as Login #FFD9A0
component "AuthControlador" as Ctrl #FFD9A0
component "origen.jsp" as Origen #FFD9A0
component "Sesión HTTP\n(contenedor servlet)" as Sesion #F5F5F5

component "UsuarioServicio" as Srv #FFE699
component "Dominio (modelo)" as Dominio #FFFBE6 {
  component "Usuario" as Usr #FFFBE6
  component "CreadorNotificacionInicioSesion\n+ Notificacion" as Notif #FFFBE6
}

component "NotificadorTelegram" as Tg #EAF7EA

component "UsuarioDAO" as DAO #C8E6C9
component "ConexionBD" as Conn #C8E6C9

database "PostgreSQL\ntabla usuario" as DB
cloud "Telegram Bot API" as TgApi

interface "GET /login\nPOST /login\nGET /logout" as iWeb
interface "HttpSession\nsetAttribute()\ninvalidate()" as iSesion
interface "IAutenticacionUsuario\niniciarSesion()" as iAut
interface "IUsuarioRepositorio\nbuscarPorCorreo()" as iRepo
interface "INotificador\nenviar()" as iNot
interface "HTTP\n/sendMessage" as iHttp
interface "obtenerConexion()" as iConn
interface "JDBC" as iJdbc

User --( iWeb
iWeb -- Ctrl

Ctrl ..> Login : devuelve vista
Ctrl ..> Origen : redirect:/origen

Ctrl --( iSesion
iSesion -- Sesion

Ctrl --( iAut
iAut -- Srv

Srv ..> Usr : verificarPassword()\ntieneTelegramVinculado()
Srv ..> Notif : crea el aviso

Srv --( iRepo
iRepo -- DAO

Srv --( iNot : solo si hay\nTelegram vinculado
iNot -- Tg

Tg --( iHttp
iHttp -- TgApi

DAO --( iConn
iConn -- Conn

Conn --( iJdbc
iJdbc -- DB

DAO ..> Usr : construye

@enduml


  ```
 

 

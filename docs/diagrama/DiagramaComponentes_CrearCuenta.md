# Codigo del diagrama en VPasCode

```plantuml
@startuml

!include https://static.visual-paradigm.com/web/resources/plantuml-stdlib/themes/vp.puml

skinparam vpDiagramType ComponentDiagram
skinparam componentStyle uml2
skinparam nodesep 35
skinparam ranksep 5

left to right direction

title HU #22 - Crear Cuenta - Diagrama de Componentes

component "localhost:8090" as Browser <<component>>

component "AuthControlador" as Auth <<component>>
component "UsuarioServicio" as Usuarios <<component>>
component "UsuarioDAO" as DAO <<component>>
component "ConexionBD" as Conexion <<component>>

component "Vistas de registro" as Vistas <<component>> {
    component "registro.jsp" as Registro <<component>>
    component "registro-exitoso.jsp" as Exito <<component>>
}

component "EmailNotificationService" as Email <<component>>
component "Spring Mail" as SpringMail <<component>>
component "Servidor SMTP" as SMTP <<component>>
component "Contenedor Servlet\nSesión HTTP" as Sesion <<component>>

database "PostgreSQL\nTabla usuario" as BD

interface "GET /registro - GET /registro-exitoso" as iRegistro
interface "validarRegistro()\nregistrar()" as iUsuarios
interface "existeCorreo() - existeDocumento() - registrar()" as iDAO
interface "obtenerConexion()" as iConexion
interface "enviarNotificacion()" as iEmail
interface "JavaMailSender\nsend()" as iMail
interface "HttpSession\nsetAttribute()" as iSesion
interface "Neon SQL" as iBD
interface "SMTP" as iSMTP


Browser --( iRegistro
iRegistro -- Auth

Auth --( iUsuarios
iUsuarios -- Usuarios

Usuarios --( iDAO
iDAO -- DAO

DAO --( iConexion
iConexion -- Conexion

Conexion --( iBD
iBD -- BD


Auth --( iEmail
iEmail -- Email

Email --( iMail
iMail -- SpringMail

SpringMail --( iSMTP
iSMTP -- SMTP

'estas son las vistas
Auth --( iSesion
iSesion -- Sesion

Auth ..> Registro
Auth ..> Exito

@enduml
```

# Foto del diagrama 
<img width="1866" height="577" alt="hu-22-crear-cuenta-diagrama-de-componentes" src="https://github.com/user-attachments/assets/d075a0de-0a04-444e-bd5b-8eddbb904e55" />

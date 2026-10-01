## Diagrama de componentes - Inicio de Sesion 

```plantuml
@startuml DiagramaComponentes_Login_WiseTrip
skinparam componentStyle uml2
skinparam shadowing false
skinparam nodesep 30
skinparam ranksep 60

' ==================== ESTILOS ====================
skinparam component {
  BorderColor #333333
  FontSize 13
  BorderThickness 1.5
}
skinparam actor {
  BackgroundColor #E8B4D8
  BorderColor #7B2D5F
  BorderThickness 2
}
skinparam interface {
  BackgroundColor #FFE699
  BorderColor #333333
  FontSize 11
}
skinparam database {
  BackgroundColor #C8E6C9
  BorderColor #333333
  FontSize 13
}
skinparam arrow {
  Color #333333
  Thickness 1.3
}
skinparam note {
  BackgroundColor #FFF9C4
  BorderColor #FBC02D
  FontSize 11
}
left to right direction

' ==================== ACTOR ====================
actor "Usuario" as User

' ==================== COMPONENTES ====================
component "«component»\n**login.jsp**" as JSP #FFD9A0
component "«component»\n**AuthControlador**" as Ctrl #FFD9A0
component "«component»\n**UsuarioServicio**" as Srv #FFE699
component "«component»\n**UsuarioDAO**" as DAO #C8E6C9
component "«component»\n**Usuario**" as Usr #C8E6C9
component "«component»\n**ConexionBD**" as Conn #C8E6C9
component "«component»\n**origen.jsp**" as Next #FFD9A0
database "**MySQL**\nTabla: usuario" as DB #A5D6A7

' ==================== INTERFACES ====================
() "formularioLogin" as IF1
() "peticionHTTP" as IF2
() "IUsuarioServicio" as IF3
() "IUsuarioDAO" as IF4
() "getConnection()" as IF5
() "JDBC" as IF6
() "entidadUsuario" as IF7
() "vistaOrigen" as IF8

' ==================== BALL-AND-SOCKET ====================
' Consumidor tiene socket "--(" ; Proveedor tiene bola "--()"

' IF1: User consume, JSP provee
User --( IF1
IF1 --() JSP

' IF2: JSP consume, Ctrl provee
JSP --( IF2
IF2 --() Ctrl

' IF3: Ctrl consume, Srv provee
Ctrl --( IF3
IF3 --() Srv

' IF4: Srv consume, DAO provee
Srv --( IF4
IF4 --() DAO

' IF5: DAO consume, Conn provee
DAO --( IF5
IF5 --() Conn

' IF6: Conn consume, DB provee
Conn --( IF6
IF6 --() DB

' IF7: DAO provee entidadUsuario (constructor)
IF7 --() DAO
Usr --( IF7

' IF8: Ctrl consume, origen.jsp provee
Ctrl --( IF8
IF8 --() Next

' ==================== NOTAS ====================
note bottom of Srv
  Coordina la autenticación.
  Valida credenciales contra
  el DAO.
end note

note bottom of Usr
  Entidad del dominio.
  El DAO la construye tras
  el SELECT en MySQL.
end note

note bottom of Next
  Pantalla post-login.
  Se muestra tras autenticación
  exitosa (redirect:/origen).
end note

note bottom of DB
  SELECT * FROM usuario
  WHERE usuario = ?
end note

' ==================== LEYENDA ====================
legend bottom
  **Flujo del Login**
  ---
  Usuario → login.jsp → AuthControlador
          → UsuarioServicio → UsuarioDAO
          → ConexionBD → MySQL
          → Usuario (entidad)
          → origen.jsp
  ---
  **—(**  socket → componente que requiere
  **—()** bola   → componente que provee
end legend

@enduml

  ```
  ## Imagen del diagrama de componentes 

  <img width="2234" height="924" alt="DiagramaComponentes - InicioSesion (1)" src="https://github.com/user-attachments/assets/7334d74d-92c8-4d62-90c1-72ec8e9cdf99" />

 

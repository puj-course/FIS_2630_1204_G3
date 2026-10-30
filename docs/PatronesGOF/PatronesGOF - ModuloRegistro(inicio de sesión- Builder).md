# Patrones GoF en WiseTrip - Módulo de registro e inicio de sesión

## Builder — "armar un usuario paso a paso" - Creacional

**Problema de diseño:** Cuando una persona inicia sesión, WiseTrip busca su cuenta en la base de datos (`UsuarioDAO.buscarPorCorreo`) y tiene que convertir la fila que devuelve PostgreSQL en un objeto `Usuario`. El problema es que `Usuario` tiene **9 datos**: id, nombre, tipo de documento, número de documento, fecha de nacimiento, correo, contraseña, rol y chat de Telegram. Algunos son opcionales (el `chatId` solo existe si el usuario vinculó Telegram) y otro tiene un valor por defecto (`rol = "cliente"`).

Antes, el objeto se armaba así:

```java
Usuario usuario = new Usuario();
usuario.setIdUsuario(rs.getInt("id_usuario"));
usuario.setNombreCompleto(rs.getString("nombre"));
usuario.setCorreo(rs.getString("correo"));
usuario.setPassword(rs.getString("contraseña"));
usuario.setRol(rs.getString("rol"));
usuario.setTipoDocumento(rs.getString("tipo_documento"));
usuario.setNumeroDocumento(rs.getString("numero_documento"));
Date fechaNacimiento = rs.getDate("fecha_nacimiento");
if (fechaNacimiento != null) {
    usuario.setFechaNacimiento(fechaNacimiento.toLocalDate().toString());
}
usuario.setChatId(rs.getString("chat_id"));
return usuario;
```

Esto trae varios problemas:
- **Objeto a medias:** entre `new Usuario()` y el último `set...` el objeto existe pero está incompleto. Si alguien olvida una línea, el usuario queda sin ese dato y nadie se da cuenta.
- **Difícil de leer:** son 9 líneas que repiten `usuario.set...`, y no se nota cuándo "termina" la construcción.
- **La otra opción tampoco sirve:** un constructor con 9 parámetros (`new Usuario(1, "Ana", "CC", "123", ...)`) es peor, porque es muy fácil cambiar el orden de dos `String` (por ejemplo, correo y contraseña) sin que el compilador se queje.
- **No se puede quitar el constructor vacío:** Spring lo necesita para llenar el formulario de registro (`new Usuario()` en `AuthControlador`), así que tampoco podemos obligar a usar un constructor grande.

**Solución directa:** un constructor con todos los parámetros, o seguir llamando setters uno por uno.

El patrón **Builder** separa **la construcción** de un objeto complejo de **su representación**. En vez de crear el objeto y luego irlo llenando, se usa un **"constructor ayudante"** (el *Builder*) que va guardando los datos paso a paso, con un método por dato y con nombre claro. Al final, un solo método, `build()`, entrega el objeto **ya completo**.

**Ejemplo de la vida real:** es como pedir un sándwich en un restaurante. No te entregan el pan y luego te van pasando los ingredientes uno por uno. Le dices al que lo prepara "con pollo, con queso, sin cebolla" y al final te entrega **el sándwich terminado**. Ese que lo prepara es el Builder.

### 1: el producto, el objeto que queremos construir

`modelo/Usuario.java`

```java
public class Usuario {
    private int idUsuario;
    private String nombreCompleto;
    private String tipoDocumento;
    private String numeroDocumento;
    private String fechaNacimiento;
    private String correo;
    private String password;
    private String rol = "cliente";
    private String chatId;

    // Se conserva: Spring lo usa para llenar el formulario de registro
    public Usuario() {
    }

    // ... getters, setters y metodos de negocio (validarDatos, verificarPassword, etc.)
}
```

Nota: `Usuario` **no cambió**. Mantiene su constructor vacío, sus setters y toda su lógica (validaciones, `verificarPassword`, `tieneTelegramVinculado`...). El Builder se **agrega** al lado, sin quitar nada.

### 2: el Builder, el que arma el usuario paso a paso

Es una **clase estática interna** de `Usuario`, por eso se escribe `Usuario.Builder`. Va dentro de `Usuario` porque solo sirve para construir usuarios.

`modelo/Usuario.java`

```java
public static class Builder {

    // Guarda los mismos datos que Usuario, con los mismos valores por defecto
    private int idUsuario;
    private String nombreCompleto;
    private String tipoDocumento;
    private String numeroDocumento;
    private String fechaNacimiento;
    private String correo;
    private String password;
    private String rol = "cliente";
    private String chatId;

    public Builder idUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
        return this;          // <- devuelve el mismo Builder
    }

    public Builder nombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
        return this;
    }

    public Builder correo(String correo) {
        this.correo = correo;
        return this;
    }

    // ... un metodo igual para cada dato: tipoDocumento, numeroDocumento,
    //     fechaNacimiento, password, rol, chatId
}
```

**¿Por qué cada método hace `return this`?** Porque así cada método devuelve **el mismo Builder**, y se puede llamar al siguiente método en la misma línea. Eso se llama **encadenar métodos** (*method chaining*):

```java
new Usuario.Builder().nombreCompleto("Ana").correo("ana@mail.com").rol("cliente")
```

Cada método se llama como el dato que guarda (`correo(...)`, `password(...)`), así que **no se puede confundir el orden**: no importa en qué orden se llamen, cada valor va a su lugar.

### 3: `build()`, el paso que entrega el objeto terminado

`modelo/Usuario.java` (dentro de `Usuario.Builder`)

```java
public Usuario build() {
    Usuario usuario = new Usuario();
    usuario.setIdUsuario(idUsuario);
    usuario.setNombreCompleto(nombreCompleto);
    usuario.setTipoDocumento(tipoDocumento);
    usuario.setNumeroDocumento(numeroDocumento);
    usuario.setFechaNacimiento(fechaNacimiento);
    usuario.setCorreo(correo);
    usuario.setPassword(password);
    usuario.setRol(rol);
    usuario.setChatId(chatId);
    return usuario;
}
```

Este es el **único lugar** donde se crea el `Usuario`. Lo que sale de `build()` ya tiene todos sus datos: quien lo recibe nunca ve un usuario a medias.

**¿Por qué `build()` usa los setters en vez de asignar directamente?** Porque algunos setters tienen reglas. Por ejemplo, `setCorreo` deja el correo sin espacios y en minúsculas:

```java
public void setCorreo(String correo) {
    this.correo = correo != null ? correo.trim().toLowerCase() : null;
}
```

Si el Builder se saltara los setters, esa regla se perdería. Usándolos, el usuario construido con Builder es **exactamente igual** al que se construía antes.

### 4: quién usa el Builder

`datos/UsuarioDAO.java`, método `buscarPorCorreo`

```java
Date fechaNacimiento = rs.getDate("fecha_nacimiento");

// Patron Builder: arma el Usuario con los datos de la consulta
return new Usuario.Builder()
        .idUsuario(rs.getInt("id_usuario"))
        .nombreCompleto(rs.getString("nombre"))
        .correo(rs.getString("correo"))
        .password(rs.getString("contraseña"))
        .rol(rs.getString("rol"))
        .tipoDocumento(rs.getString("tipo_documento"))
        .numeroDocumento(rs.getString("numero_documento"))
        .fechaNacimiento(fechaNacimiento != null ? fechaNacimiento.toLocalDate().toString() : null)
        .chatId(rs.getString("chat_id"))
        .build();
```

Se lee casi como una frase: *"constrúyeme un usuario con este id, este nombre, este correo... y entrégamelo"*. Cada línea dice qué columna de la base de datos va a qué dato del usuario.

**Lo que NO cambió en la parte de base de datos:** la consulta SQL, la conexión (`ConexionBD.obtenerConexion()`), el `try-with-resources`, el manejo de errores y las columnas que se leen siguen exactamente igual. Solo cambió **la forma de armar el objeto** con los datos que ya se leían.

### ¿Cómo se conecta con el resto del módulo?

Este es el recorrido completo cuando alguien inicia sesión:

1. El usuario escribe su correo y contraseña en el formulario → `AuthControlador`.
2. `AuthControlador` le pide a `UsuarioServicio.iniciarSesion(correo, password)` que lo autentique.
3. `UsuarioServicio` le pide al repositorio el usuario: `repositorio.buscarPorCorreo(correo)`.
4. `UsuarioDAO` hace la consulta SQL y **usa el Builder** para convertir la fila en un `Usuario` completo.
5. `UsuarioServicio` recibe ese usuario, revisa la contraseña con `verificarPassword(...)` y, si es correcta, envía el aviso por Telegram (Strategy + Factory Method).

`UsuarioServicio` no sabe que existe el Builder. Solo recibe un `Usuario` listo para usar. El Builder queda escondido dentro de la capa de datos, que es justo donde se arma el objeto.

### ¿Por qué se puso aquí y no en otra parte?

| Pregunta | Respuesta |
|---|---|
| ¿Por qué en `Usuario`? | Es la clase del modelo con más datos (9) y mezcla datos obligatorios, opcionales y con valor por defecto. Es justo el caso para el que existe el Builder. |
| ¿Por qué en `UsuarioDAO`? | Es el lugar donde se arma un `Usuario` con **todos** sus datos a la vez (al leerlo de la base de datos). En el formulario no se usa, porque ahí Spring llena el objeto solo. |
| ¿Por qué no un constructor con 9 parámetros? | Hay 8 parámetros `String` seguidos: cambiar dos de lugar compila sin error y guarda datos equivocados. Con el Builder cada valor tiene nombre. |
| ¿Por qué no se quitaron los setters? | Spring los necesita para llenar el formulario de registro (`@ModelAttribute("usuario") Usuario usuario` en `AuthControlador`). Quitarlos rompería el registro. |
| ¿Por qué no Singleton u otro creacional? | En Spring todos los `@Service` ya son únicos (singleton) por defecto, así que no aporta nada nuevo. Y Factory Method ya está usado en el proyecto. |

### Ventajas que se obtienen

- **Legibilidad:** cada dato tiene nombre (`.correo(...)`, `.rol(...)`), se entiende sin abrir la clase `Usuario`.
- **Seguridad al construir:** el objeto se entrega completo en `build()`, nunca a medias.
- **Sin errores de orden:** no hay que recordar en qué posición va cada parámetro.
- **Datos opcionales fáciles:** si un dato no se llama (por ejemplo `chatId`), queda con su valor por defecto.
- **Fácil de extender:** si mañana `Usuario` tiene un dato nuevo (por ejemplo `fechaRegistro`), se agrega un método al Builder y una línea en `build()`, sin romper a quien ya lo usa.
- **No rompe nada:** el constructor vacío y los setters siguen ahí, así que el formulario de registro y el resto del código funcionan igual.

### Roles del patrón en WiseTrip

| Rol GoF | Clase en WiseTrip | Qué hace |
|---|---|---|
| **Product** | `Usuario` | El objeto complejo que se quiere construir. |
| **Builder** | `Usuario.Builder` | Guarda los datos paso a paso y sabe cómo armar el `Usuario` en `build()`. |
| **Director / Cliente** | `UsuarioDAO` | Decide qué datos poner (los que vienen de la base de datos) y en qué orden llamar al Builder. |

## Diseño - Diagrama UML

<img width="808" height="1160" alt="Untitled (1)" src="https://github.com/user-attachments/assets/2d79ed9e-24d3-4357-9641-fad2814f7cc3" />


**Cómo leer el diagrama:**
- `UsuarioServicio` solo conoce la interfaz `IUsuarioRepositorio`, no sabe del Builder.
- `UsuarioDAO` implementa esa interfaz y **usa** `Usuario.Builder` para armar el objeto.
- `Usuario.Builder` **crea** el `Usuario` en `build()`.
- La línea entre `Usuario` y `Usuario.Builder` indica que `Builder` es una **clase interna** de `Usuario` (vive dentro de su archivo).

---

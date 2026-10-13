# Patrones GoF en WiseTrip - Módulo de registro e inicio de sesión

## 1. Strategy — "diferentes formas de avisarle a alguien"

El Patrón Strategy consiste en programar contra una interfaz (INotificador) en lugar de contra clases concretas, permitiendo intercambiar algoritmos o implementaciones (como enviar por correo o por Telegram) en tiempo de ejecución sin alterar la lógica principal de la clase que los utiliza (UsuarioServicio).

### 1: el molde que deben cumplir todas las formas de avisar

`negocio/INotificador.java`

```java
public interface INotificador {
    void enviar(String destino, Notificacion notificacion);
}
```

Nota: "cualquier cosa que quiera ser un notificador debe tener un método `enviar`". No hace nada por sí sola.

### 2: la forma concreta de avisar por correo

Sobreescribe el metodo de enviar, esta vez para el correo. Usa @override para llamar ese método si es necesario. 

`servicio/NotificadorCorreo.java`

```java
@Service("notificadorCorreo")
public class NotificadorCorreo implements INotificador {

    @Override
    public void enviar(String destino, Notificacion notificacion) {
        SimpleMailMessage mensaje = new SimpleMailMessage();
        mensaje.setTo(destino);
        mensaje.setSubject(notificacion.getAsunto());
        mensaje.setText(notificacion.getCuerpo());
        mailSender.send(mensaje);
    }
}
```

### 3: la forma concreta de avisar por Telegram

Sobreescribe el metodo de enviar, esta vez para el Telegram . Usa @override para llamar ese método si es necesario.

`servicio/NotificadorTelegram.java`

```java
@Service("notificadorTelegram")
public class NotificadorTelegram implements INotificador {

    @Override
    public void enviar(String destino, Notificacion notificacion) {
        // arma la URL y llama a la API de Telegram
    }
}
```

### 4: quién usa las dos formas, sin saber cuál es cuál

En este fragmento, UsuarioServicio recibe ambas dependencias declaradas únicamente a través de su interfaz común (INotificador). Esto significa que el servicio utiliza las dos formas de notificación para cumplir con los flujos del sistema, sin necesidad de conocer ni acoplarse a los detalles técnicos de cómo funciona internamente NotificadorCorreo o NotificadorTelegram.

`servicio/UsuarioServicio.java`

```java
public UsuarioServicio(IUsuarioRepositorio repositorio,
                       @Qualifier("notificadorCorreo") INotificador notificadorCorreo,
                       @Qualifier("notificadorTelegram") INotificador notificadorTelegram) {
    this.repositorio = repositorio;
    this.notificadorCorreo = notificadorCorreo;
    this.notificadorTelegram = notificadorTelegram;
}
```

El tipo de ambas variables es `INotificador` (el molde), no `NotificadorCorreo` ni `NotificadorTelegram`. `UsuarioServicio` guarda "algo que sabe avisar", sin preocuparse de si por dentro es correo o Telegram.

### Dónde se usa cada una

```java
// Al registrarse -> usa el canal de correo
public void registrar(Usuario usuario) {
    ...
    notificar(notificadorCorreo, usuario.getCorreo(), Notificacion.bienvenida(usuario));
}

// Al iniciar sesión -> usa el canal de Telegram
public Usuario iniciarSesion(String correo, String password) {
    ...
    notificar(notificadorTelegram, usuario.getChatId(), Notificacion.inicioSesion(usuario));
}
```

### ¿Cómo sabe INotificador cuál usar?

**No lo sabe. `INotificador` nunca decide nada**, es solo un molde vacío. La decisión ocurre en dos momentos distintos:

**Momento 1 — cuando arranca la app (Spring arma las piezas):**
Spring lee el `@Qualifier("notificadorCorreo")` y busca qué clase tiene `@Service("notificadorCorreo")`. Encuentra `NotificadorCorreo` y pone **ese objeto real** en la variable. Lo mismo para `notificadorTelegram`. Esto pasa una sola vez, al arrancar, no cada vez que alguien se registra.

**Momento 2 — cuando pasa algo (tú decides cuál variable usar):**
Tú, al escribir el método `registrar`, decidiste a mano usar `notificadorCorreo`. Al escribir `iniciarSesion`, decidiste usar `notificadorTelegram`. No hay inteligencia artificial decidiendo esto — es una elección fija que programaste.

Lo imporatnte aqui es que `UsuarioServicio` nunca tiene que saber los detalles técnicos de SMTP ni de la API de Telegram — esa complejidad queda escondida dentro de cada `Notificador`.

---

## 2. Factory Method — El sistema arma el mensaje. 

El Patrón Factory Method consiste en encapsular la lógica de instanciación de objetos dentro de un método o clase especializada (la "fábrica"), evitando que el código cliente utilice la palabra new directamente y logrando que la creación de objetos sea independiente de quién los utiliza.

Ejemplo: en vez de escribir a mano "Hola Juan, bienvenido a WiseTrip..." cada vez que alguien se registra, tienes una función que arma el mensaje automáticamente.

`modelo/Notificacion.java`

```java
public class Notificacion {
    private final String asunto;
    private final String cuerpo;

    public Notificacion(String asunto, String cuerpo) {
        this.asunto = asunto;
        this.cuerpo = cuerpo;
    }

    public static Notificacion bienvenida(Usuario usuario) {
        return new Notificacion(
                "¡Bienvenido a WiseTrip!",
                "Hola " + usuario.getNombreCompleto() + ",\n\n"
                + "Tu cuenta en WiseTrip fue creada con éxito. "
                + "Ya puedes iniciar sesión y empezar a planificar tus viajes.\n\n"
                + "¡Buen viaje!\nEl equipo de WiseTrip");
    }

    public static Notificacion inicioSesion(Usuario usuario) {
        return new Notificacion(
                "Inicio de sesión en WiseTrip",
                "Hola " + usuario.getNombreCompleto()
                + ", iniciaste sesión en WiseTrip correctamente. Si no fuiste tú, cambia tu contraseña.");
    }
}
```

Se usa así:

```java
Notificacion.bienvenida(usuario)      // dentro de registrar(...)
Notificacion.inicioSesion(usuario)    // dentro de iniciarSesion(...)
```

Le pasas el usuario y la función devuelve el mensaje completo, con el nombre correcto adentro. Si un día cambia el texto del correo de bienvenida, se edita un solo lugar (`Notificacion.bienvenida`) y se actualiza en toda la app.

---

## 3. Facade — UsuarioServicio como fachada

Si usaramos AuthControlador como quien registra, valida, guarda en base de datos, coordina los DAO, notifica y realiza la logica de validacion tendria mucho acoplamiento por eso aplicamos el patrón Facade a través de una clase de servicio intermediaria que encapsula y unifica toda esa complejidad en un solo lugar, permitiendo que el controlador solo tenga que hacer una llamada limpia sin enterarse de los detalles internos.

`controlador/AuthControlador.java`

```java
private final IRegistroUsuario registro;   // <- la fachada

public String procesarRegistro(...) {
    Map<String, String> errores = registro.validarRegistro(usuario, confirmarPassword);
    if (!errores.isEmpty()) { ... }
    registro.registrar(usuario);   // <- una sola orden
    ...
}
```

Detrás de esa única llamada `registro.registrar(usuario)`:

`servicio/UsuarioServicio.java`

```java
@Override
public void registrar(Usuario usuario) {
    usuario.normalizar();                                       // 1. limpia datos
    repositorio.registrar(usuario);                             // 2. guarda en PostgreSQL
    notificar(notificadorCorreo, usuario.getCorreo(),            // 3. arma y envía
              Notificacion.bienvenida(usuario));                 //    el correo
}
```

El controlador (`AuthControlador`) solo dice "registrar", sin enterarse de que por dentro pasan tres cosas distintas: normalizar, guardar y notificar.

---

# Patrones GoF en WiseTrip - Módulo de registro e inicio de sesión

## 1. Strategy — "diferentes formas de avisarle a alguien"

**Problema de diseño :**  Al registrarse o iniciar sesion, nuestra aplicación debe avisarle al usuario de estos por diferentes canales si es la primera vez en ingresar - registrarse le llegara un mensaje a su correo mientras que si ya se encontraba registrado anteriormente solo le llegara un mensaje al Telegram. Mas adelante deberian entrar otros canales como SMS. 
Sin un patron la clase UsuarioServicio tendria que conocer la informacion de cada canal teniendo de esta forma: 
- Alto Acoplamiento : el servicio de los usuarios dependeria de JavaMailSender y de la API de telegram. 
- Codigo rigido: cada canal obligatoria a modificar la clase UsuarioServicio
- Baja cohesion: la clase de Usuario tendria que armar las URLs de telegram y los mensajes SMTP 
- Dificultad en pruebas: No se puede probar el registro sin correo ni Telegram reales.

**Solucion directa:** if/else para para cada canal

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

Lo importante aqui es que `UsuarioServicio` nunca tiene que saber los detalles técnicos de SMTP ni de la API de Telegram — esa complejidad queda escondida dentro de cada `Notificador`.

## Diseño - Diagrama UML 

<img width="1143" height="529" alt="Untitled (2)" src="https://github.com/user-attachments/assets/dd388ee8-1732-4199-870d-d970b7db7a3c" />

---

## 2. Factory Method — El sistema arma el mensaje (y decide qué tipo de mensaje armar)

El patrón **Factory Method** consiste en declarar un método de creación en una clase base (el **Creator**), sin que esa clase sepa qué objeto concreto va a crear. Son las **subclases** (los **ConcreteCreator**) las que, al heredar de esa clase base y sobreescribir el método, deciden qué objeto específico se construye. Así, el código cliente nunca escribe `new Notificacion(...)` directamente — en vez de eso, crea el "fabricante" correcto y le delega la construcción.

**Ejemplo:** en vez de escribir a mano "Hola Juan, bienvenido a WiseTrip..." cada vez que alguien se registra, tienes una clase especializada que arma ese mensaje por ti. Y si mañana necesitas un tercer tipo de mensaje (por ejemplo, "recuperar contraseña"), solo creas **otra clase** que herede de la misma base — no tocas nada de lo que ya existe.

**`modelo/Notificacion.java`** (el *Product*: el objeto que se crea)

```java
public class Notificacion {
    private final String asunto;
    private final String cuerpo;

    public Notificacion(String asunto, String cuerpo) {
        this.asunto = asunto;
        this.cuerpo = cuerpo;
    }

    public String getAsunto() { return asunto; }
    public String getCuerpo() { return cuerpo; }
}
```

**`modelo/CreadorNotificacion.java`** (el *Creator*: no sabe qué va a crear)

```java
public abstract class CreadorNotificacion {

    // Este es el Factory Method: no tiene cuerpo, obliga a que
    // cada subclase decida que Notificacion concreta construir.
    protected abstract Notificacion crearNotificacion(Usuario usuario);

    // Usa el factory method sin saber que subclase hay detras.
    public void notificar(Usuario usuario, String destino, INotificador notificador) {
        Notificacion notificacion = crearNotificacion(usuario);
        notificador.enviar(destino, notificacion);
    }
}
```

**`modelo/CreadorNotificacionBienvenida.java`** (un *ConcreteCreator*: sí sabe qué crear)

```java
public class CreadorNotificacionBienvenida extends CreadorNotificacion {

    @Override
    protected Notificacion crearNotificacion(Usuario usuario) {
        return new Notificacion(
                "¡Bienvenido a WiseTrip!",
                "Hola " + usuario.getNombreCompleto() + ",\n\n"
                + "Tu cuenta en WiseTrip fue creada con éxito. "
                + "Ya puedes iniciar sesión y empezar a planificar tus viajes.\n\n"
                + "¡Buen viaje!\nEl equipo de WiseTrip");
    }
}
```

**`modelo/CreadorNotificacionInicioSesion.java`** (otro *ConcreteCreator*)

```java
public class CreadorNotificacionInicioSesion extends CreadorNotificacion {

    @Override
    protected Notificacion crearNotificacion(Usuario usuario) {
        return new Notificacion(
                "Inicio de sesión en WiseTrip",
                "Hola " + usuario.getNombreCompleto()
                + ", iniciaste sesión en WiseTrip correctamente. Si no fuiste tú, cambia tu contraseña.");
    }
}
```

**Se usa así**, dentro de `UsuarioServicio`:

```java
// dentro de registrar(...)
new CreadorNotificacionBienvenida().notificar(usuario, usuario.getCorreo(), notificadorCorreo);

// dentro de iniciarSesion(...)
new CreadorNotificacionInicioSesion().notificar(usuario, usuario.getChatId(), notificadorTelegram);
```

`UsuarioServicio` nunca escribe `new Notificacion(...)` directamente — crea el "fabricante" que corresponde (`CreadorNotificacionBienvenida` o `CreadorNotificacionInicioSesion`) y le pide a él que arme y envíe el mensaje. Si un día el texto del correo de bienvenida cambia, se edita un solo lugar (`CreadorNotificacionBienvenida.crearNotificacion`), y se actualiza en toda la app.

**La diferencia clave con la versión anterior:** antes, `Notificacion.bienvenida(usuario)` era una función estática — no había herencia, ni una clase que "decidiera" nada por polimorfismo. Ahora, el que decide es `CreadorNotificacionBienvenida` **al heredar** de `CreadorNotificacion` y **sobreescribir** `crearNotificacion(...)`. Esa herencia + sobreescritura es justo lo que GoF exige para llamarlo Factory Method de verdad.

<img width="1532" height="664" alt="Untitled (2)" src="https://github.com/user-attachments/assets/827c75f6-5c02-48b2-aedf-45b990f526df" />

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

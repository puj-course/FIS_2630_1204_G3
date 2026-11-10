# Patrones GoF en WiseTrip - Módulo de Planificación e Itinerario del Viaje

## 1. Facade — UsuarioSesionFacade como fachada de la sesión

**Problema de diseño:** En el flujo de planificación preferencias, fechas, presupuesto, recomendaciones, reparto y el resumen del viaje) hay cuatro controladores distintos — PlanificacionControlador, PlanControlador, ViajeControlador y RecomendacionControlador — y los cuatro necesitan, en casi todos sus métodos, lo mismo: saber quién es el usuario que tiene la sesión abierta, y mostrar sus iniciales en el encabezado de cada pantalla. Antes de aplicar el patrón, cada controlador resolvía esto por su cuenta:

- Alto acoplamiento: los cuatro controladores dependían directamente de cómo
  se guarda el usuario en `HttpSession` (la clave `"usuarioActivo"` estaba
  escrita a mano en cada uno).
- Duplicación de lógica: el método que calcula las iniciales
  (`calcularIniciales`) estaba copiado, idéntico, en los cuatro
  controladores.
- Código rígido: si cambiaba la clave de sesión o la forma de calcular las
  iniciales, había que acordarse de tocar los cuatro archivos a la vez.

**Solución directa:** seguir copiando el mismo bloque de código en cada
controlador nuevo que se agregue al flujo.

El patrón Facade consiste en poner una sola clase entre los controladores
(los clientes) y el subsistema que usan (HttpSession, Model y Usuario), de
forma que los controladores le hablen únicamente a esa clase y no conozcan
los detalles de cómo resuelve el trabajo por dentro.

### 1: el problema, antes de la fachada

Así se veía cada método de controlador antes del refactor (fragmento real,
tomado de `PlanControlador` antes de aplicar el patrón):

```java
@GetMapping("/viaje")
public String mostrarViaje(HttpSession sesion, Model model) {

    Usuario usuario = (Usuario) sesion.getAttribute("usuarioActivo");
    if (usuario == null) return "redirect:/login";

    ...

    model.addAttribute("usuario", usuario);
    model.addAttribute("inicialesUsuario", calcularIniciales(usuario));
    ...
}

private String calcularIniciales(Usuario usuario) {
    if (usuario.getNombreCompleto() == null || usuario.getNombreCompleto().isBlank()) {
        return "?";
    }
    String[] partes = usuario.getNombreCompleto().trim().split("\\s+");
    if (partes.length > 1) {
        return ("" + partes[0].charAt(0) + partes[1].charAt(0)).toUpperCase();
    }
    return partes[0].substring(0, 1).toUpperCase();
}
```

Este mismo par de bloques — la línea de `getAttribute` + el método
`calcularIniciales` completo — ase repetía en los demás controladores del flujo, letra por letra.

### 2: la fachada — un solo lugar que sabe resolver todo esto

`servicio/UsuarioSesionFacade.java`

<img width="735" height="499" alt="Screenshot 2026-10-13 at 11 14 30 PM" src="https://github.com/user-attachments/assets/06bbdd22-4529-4fc0-b717-9a05837dba79" />
<img width="655" height="173" alt="Screenshot 2026-10-13 at 11 15 18 PM" src="https://github.com/user-attachments/assets/4592c332-e905-4757-88fe-691a06eebc2e" />


Nota: `prepararEncabezado` es el método que de verdad usan los
controladores, es la "puerta de entrada" simplificada de la fachada. Por
dentro llama a `usuarioActivo` y a `iniciales`, que también quedan
disponibles por separado por si algún controlador solo necesita una de las
dos cosas. La clave de sesión quedó guardada en una sola constante,
`CLAVE_USUARIO`, así que ningún controlador tiene que conocerla.

### 3: quién usa la fachada, sin saber cómo resuelve nada por dentro

La fachada la usan cuatro controladores: `PlanificacionControlador`,
`PlanControlador`, `ViajeControlador` y `RecomendacionControlador`. Por
ejemplo, así quedó el mismo método `mostrarViaje` de `PlanControlador`:

<img width="579" height="103" alt="Screenshot 2026-10-14 at 12 09 58 AM" src="https://github.com/user-attachments/assets/4a446638-4f2e-4c7d-960e-c797b12574fd" />



`PlanControlador` ya no sabe que el usuario vive bajo la clave
`"usuarioActivo"` en la sesión, ni cómo se arman las iniciales. Solo sabe
que `usuarioSesionFacade.prepararEncabezado(...)` le devuelve el usuario (y
de paso ya deja todo listo en el `model`), o `null` si no hay nadie con
sesión iniciada.

Y así quedó inyectada en los cuatro controladores, por ejemplo en
`PlanControlador`:
```java
private final UsuarioSesionFacade usuarioSesionFacade;

public PlanControlador(IGestionReparto gestionReparto,
                       PresupuestoServicio presupuestoServicio,
                       UsuarioSesionFacade usuarioSesionFacade,
                       IRepositorioViajes viajeDAO) {
    this.gestionReparto = gestionReparto;
    this.presupuestoServicio = presupuestoServicio;
    this.usuarioSesionFacade = usuarioSesionFacade;
    this.viajeDAO = viajeDAO;
}
```


Spring arma automáticamente esta pieza al arrancar la aplicación, igual que
arma cualquier otro `@Service`: ve que `UsuarioSesionFacade` está anotada
con `@Service`, crea una sola instancia de ella, y se la entrega a
cualquier controlador que la pida en su constructor.

### ¿Qué pasa si no hay sesión activa?

`prepararEncabezado` devuelve `null` en ese caso, **sin tocar el `model`**
— no agrega ni `"usuario"` ni `"inicialesUsuario"`, porque no tiene nada
que mostrar. El controlador es quien decide qué hacer con ese `null`: en
todos los casos de WiseTrip, la decisión es la misma, redirigir a
`/login`:

<img width="565" height="57" alt="Screenshot 2026-10-13 at 11 16 58 PM" src="https://github.com/user-attachments/assets/b29c45ec-e8c4-47dc-9cd4-3cee308e9c38" />


La fachada no decide a dónde redirigir — eso no es su responsabilidad,
porque cada controlador podría, en teoría, querer hacer algo distinto con
un usuario sin sesión. Lo único que centraliza es el **cómo se obtiene** el
usuario y **cómo se calculan** sus iniciales, no qué hacer si no hay
usuario.

### ¿Qué gana el equipo con esto?

Un cambio en la regla de iniciales (por ejemplo, para manejar un nombre
compuesto como "María José") significa editar un solo método, `iniciales()`,
dentro de `UsuarioSesionFacade`, y ese cambio queda aplicado en todas las
pantallas que usan los cuatro controladores. Lo mismo pasa si se agrega
otro controlador al flujo: no hay que copiar nada, solo inyectar la
fachada.


## Diseño — Diagrama UML

<img width="732" height="751" alt="PatrónFacade drawio" src="https://github.com/user-attachments/assets/c6b9c997-efee-44ae-afea-8d7e98816e94" />

---
En el diagrama se ven tres partes:

- **Clientes:** los cuatro controladores. Cada uno guarda la fachada como atributo, por eso la relación es una asociación (línea continua).
- **Facade:** `UsuarioSesionFacade`, con la constante `CLAVE_USUARIO` (subrayada porque es `static`) y sus tres métodos.
- **Subsistema:** `HttpSession` y `Model`, que son interfaces de Jakarta y de Spring, y `Usuario`. La fachada no los guarda como atributos, solo los recibe como parámetros o los usa dentro de sus métodos, por eso la relación es una dependencia (línea punteada).

---

## 2. Chain of Responsibility — validación de fechas

### Problema de diseño

- `FechasServicio.validarFechas()` tenía tres reglas distintas (fecha de inicio válida, fecha de fin válida, relación entre ambas + duración máxima) metidas dentro del mismo método, con ifs anidados.
- Las tres reglas compartían las mismas variables locales (`inicio`, `fin`), así que no se podían leer ni probar por separado.
- Agregar una regla nueva significaba editar ese método completo y arriesgarse a romper una regla que ya funcionaba.

### Solución directa

Cada regla se convirtió en su propia clase, y esas clases se conectan entre sí formando una cadena: cada una valida lo suyo y le pasa el turno a la siguiente. `FechasServicio` ya no valida nada directamente — solo arma la cadena y la dispara.

### 1: el problema, antes de la cadena

Así se veía `validarFechas()` antes del refactor:

```java
@Override
public Map<String, String> validarFechas(FechasViaje fechas) {
    Map<String, String> errores = new LinkedHashMap<>();
    LocalDate inicio = null;
    LocalDate fin = null;

    // Fecha de inicio
    if (fechas.getFechaInicio() == null || fechas.getFechaInicio().isBlank()) {
        errores.put("fechaInicio", "Selecciona la fecha de inicio de tu viaje.");
    } else {
        try {
            inicio = LocalDate.parse(fechas.getFechaInicio());
            if (inicio.isBefore(LocalDate.now())) {
                errores.put("fechaInicio", "La fecha de inicio no puede ser anterior a hoy.");
                inicio = null;
            }
        } catch (Exception e) {
            errores.put("fechaInicio", "Ingresa una fecha válida.");
        }
    }

    // Fecha de fin
    if (fechas.getFechaFin() == null || fechas.getFechaFin().isBlank()) {
        errores.put("fechaFin", "Selecciona la fecha de regreso.");
    } else {
        try {
            fin = LocalDate.parse(fechas.getFechaFin());
        } catch (Exception e) {
            errores.put("fechaFin", "Ingresa una fecha válida.");
        }
    }

    // Relacion entre ambas
    if (inicio != null && fin != null) {
        if (!fin.isAfter(inicio)) {
            errores.put("fechaFin", "La fecha de regreso debe ser posterior a la de inicio.");
        } else if (ChronoUnit.DAYS.between(inicio, fin) + 1 > MAX_DIAS) {
            errores.put("fechaFin", "Por ahora WiseTrip planea viajes de máximo " + MAX_DIAS + " días.");
        }
    }

    return errores;
}
```

Todo en un solo método: difícil de leer de corrido, y cualquier regla nueva tendría que meterse ahí adentro.

### 2: el contexto que viaja por la cadena

Antes de ver las reglas, hace falta algo que lleve la información de una a
otra. Ese es `ContextoValidacionFechas`: guarda las fechas originales, va
acumulando los errores y guarda las fechas ya convertidas a `LocalDate`
para que la última regla no tenga que volver a interpretarlas. También
guarda, en `consultaCruces`, la forma de preguntar si las fechas se cruzan
con otro viaje del usuario.

<img width="699" height="760" alt="Screenshot 2026-11-04 at 9 21 28 AM" src="https://github.com/user-attachments/assets/f940410e-246e-4ba7-a807-2b698c072c96" />


Si nadie le asigna `consultaCruces`, `tieneCruceConOtroViaje()` devuelve
`false`, así que la cadena también se puede usar sin consultar la base de
datos.

### 3: el eslabón base de la cadena

`ReglaValidacionFechas` es la clase abstracta de la que salen todas las reglas. Define cómo se encadena una regla con la siguiente (`siguiente()`) y el método que dispara la validación (`validar()`), que es `final` porque ninguna regla concreta debe poder cambiar el mecanismo de la cadena, solo su propia lógica.

<img width="685" height="656" alt="Screenshot 2026-11-09 at 7 10 04 PM" src="https://github.com/user-attachments/assets/6f1b27eb-7368-4bf5-b85b-af70c7df0d46" />



`validarPropio` es `protected`: las reglas concretas lo implementan, pero
desde afuera nadie lo llama directamente. La única entrada a la cadena es
`validar()`, que siempre ejecuta la regla actual y después llama a la
siguiente, hasta llegar a la última (la que tiene `siguiente` en `null`).


### 4: las tres reglas concretas

Las tres reglas se ejecutan siempre, una detrás de otra, y cada una va
dejando sus errores en el contexto. Así el usuario ve todos sus errores de
una vez y no tiene que corregirlos de a uno.

`ReglaFechaInicio` revisa que la fecha de inicio exista, que tenga un
formato válido y que no sea anterior a hoy. Si todo está bien, la guarda en
el contexto con `setInicio()`:

<img width="881" height="557" alt="Screenshot 2026-11-09 at 7 11 17 PM" src="https://github.com/user-attachments/assets/e14d87ec-cdb7-4df7-b164-22744e8b023d" />


`ReglaFechaFin` hace lo mismo con la fecha de regreso y la guarda con
`setFin()`:

<img width="724" height="416" alt="Screenshot 2026-11-09 at 7 12 00 PM" src="https://github.com/user-attachments/assets/a4374748-2852-4868-9f06-b84731d182d3" />


`ReglaRelacionFechas` es la última. Si `inicio` o `fin` quedaron en `null`
(porque alguna de las reglas anteriores encontró un error), no hace nada,
porque comparar fechas inválidas no tendría sentido. Si las dos fechas
están bien, revisa que el regreso sea después del inicio, que el viaje no
pase de `MAX_DIAS` y que las fechas no se crucen con otro viaje del
usuario:

<img width="985" height="565" alt="Screenshot 2026-11-09 at 7 12 22 PM" src="https://github.com/user-attachments/assets/61f8fc56-5902-4ab5-bf92-73a06b6edb96" />



### 5: quién arma y dispara la cadena

`FechasServicio` arma la cadena una sola vez, en el constructor, y la
reutiliza en cada validación. Tiene dos versiones de `validarFechas`: la
primera solo revisa las fechas, y la segunda además le dice al contexto
cómo consultar en la base de datos (a través de `IRepositorioViajes`) si
las fechas se cruzan con otro viaje del mismo usuario.

<img width="753" height="800" alt="Screenshot 2026-11-04 at 9 29 47 AM" src="https://github.com/user-attachments/assets/0cc27f5f-19d5-4e9d-882d-d9709cd94467" />

`FechasServicio` solo guarda la primera regla (`cadenaValidacion`): el
resto de la cadena se recorre sola gracias a `siguiente`. Al final devuelve
el mapa de errores del contexto, y si está vacío las fechas son válidas.

La cadena la usan `PlanificacionControlador` (cuando el usuario guarda las
fechas del viaje) y `SeleccionControlador` (cuando el usuario elige un
destino), los dos a través de la interfaz `IFechasServicio`.

### ¿Cómo se reparte la responsabilidad?

- **`FechasServicio`** arma la cadena, crea el contexto y la dispara. No
  revisa ninguna fecha por su cuenta.
- **`ReglaValidacionFechas`** no valida nada: se encarga del recorrido,
  ejecutando la regla actual y pasándole el turno a la siguiente.
- **`ReglaFechaInicio`** y **`ReglaFechaFin`** revisan un solo campo cada
  una y preparan las fechas convertidas para la regla siguiente.
- **`ReglaRelacionFechas`** es la que más trabajo tiene: compara las dos
  fechas, revisa la duración máxima y consulta si hay cruce con otro viaje.
- **`ContextoValidacionFechas`** es lo que comparten todas: lleva las
  fechas, guarda los resultados intermedios y junta los errores.

### ¿Por qué Chain of Responsibility y no Strategy?

Strategy sirve para elegir entre varias formas distintas de resolver el mismo problema (por ejemplo, distintos algoritmos para calcular un descuento). Acá no hay varias formas alternativas de validar una fecha — hay varias validaciones distintas que tienen que aplicarse una detrás de otra, y donde una depende de que la anterior haya funcionado. Eso es justo el caso de uso de Chain of Responsibility.

### ¿Qué gana el equipo con esto?

Cada regla se puede probar sola, armando un `ContextoValidacionFechas` a mano, sin tener que disparar las tres validaciones juntas. Si en el futuro se necesita una regla nueva — por ejemplo, una validación extra para cierto tipo de destino — se crea una clase que extienda `ReglaValidacionFechas` y se agrega a la cadena del constructor, sin tocar ni una línea de las reglas que ya existen.

## Diseño — Diagrama UML

<img width="1100" height="709" alt="PatrónFacade-ChainResponsability drawio (1)" src="https://github.com/user-attachments/assets/078a004b-7c90-4fa7-a341-0007381ad7c1" />


En el diagrama se ven los participantes del patrón:

- **`FechasServicio`** guarda la primera regla de la cadena en
  `cadenaValidacion` (asociación) y crea el contexto en cada validación
  (dependencia "crea").
- **`ReglaValidacionFechas`** es abstracta (por eso su nombre y
  `validarPropio` van en cursiva) y tiene una asociación consigo misma,
  `siguiente`, con multiplicidad `0..1`, porque la última regla no tiene
  siguiente.
- **Las tres reglas concretas** heredan de `ReglaValidacionFechas` y
  sobrescriben `validarPropio`.
- **`ContextoValidacionFechas`** es lo que viaja por la cadena: las reglas
  lo leen y lo modifican.

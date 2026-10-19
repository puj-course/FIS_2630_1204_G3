# Patrones GoF en WiseTrip - Módulo de Planificación e Itinerario del Viaje

## 1. Facade — UsuarioSesionFacade como fachada de la sesión

**Problema de diseño:** En el flujo de planificación (preferencias, fechas,
presupuesto, reparto y el resumen del viaje) hay tres controladores
distintos — `PlanificacionControlador`, `PlanControlador` y
`ViajeControlador` — y los tres necesitan, en casi todos sus métodos, lo
mismo: saber quién es el usuario que tiene la sesión abierta, y mostrar sus
iniciales en el encabezado de cada pantalla. Antes de aplicar el patrón,
cada controlador resolvía esto por su cuenta:

- Alto acoplamiento: los tres controladores dependían directamente de cómo
  se guarda el usuario en `HttpSession` (la clave `"usuarioActivo"` estaba
  escrita a mano en cada uno).
- Duplicación de lógica: el método que calcula las iniciales
  (`calcularIniciales`) estaba copiado, idéntico, en los tres
  controladores.
- Código rígido: si cambiaba la clave de sesión o la forma de calcular las
  iniciales, había que acordarse de tocar los tres archivos a la vez.

**Solución directa:** seguir copiando el mismo bloque de código en cada
controlador nuevo que se agregue al flujo.

El patrón Facade consiste en poner una sola clase entre los controladores
(los clientes) y el subsistema que usan (`HttpSession` + `Usuario`), de
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
`calcularIniciales` completo — aparecía también en
`PlanificacionControlador` y en `ViajeControlador`, letra por letra.

### 2: la fachada — un solo lugar que sabe resolver todo esto

`servicio/UsuarioSesionFacade.java`

<img width="735" height="499" alt="Screenshot 2026-10-13 at 11 14 30 PM" src="https://github.com/user-attachments/assets/06bbdd22-4529-4fc0-b717-9a05837dba79" />
<img width="655" height="173" alt="Screenshot 2026-10-13 at 11 15 18 PM" src="https://github.com/user-attachments/assets/4592c332-e905-4757-88fe-691a06eebc2e" />


Nota: `prepararEncabezado` es el método que de verdad usan los
controladores — es la "puerta de entrada" simplificada de la fachada. Por
dentro llama a `usuarioActivo` y a `iniciales`, que también quedan
disponibles por separado por si algún controlador solo necesita una de las
dos cosas.

### 3: quién usa la fachada, sin saber cómo resuelve nada por dentro

Así quedó el mismo método `mostrarViaje` después:

<img width="579" height="103" alt="Screenshot 2026-10-14 at 12 09 58 AM" src="https://github.com/user-attachments/assets/4a446638-4f2e-4c7d-960e-c797b12574fd" />


`PlanControlador` ya no sabe que el usuario vive bajo la clave
`"usuarioActivo"` en la sesión, ni cómo se arman las iniciales. Solo sabe
que `usuarioSesionFacade.prepararEncabezado(...)` le devuelve el usuario (y
de paso ya deja todo listo en el `model`), o `null` si no hay nadie con
sesión iniciada.

Y así quedó inyectada en los tres controladores, por ejemplo en
`PlanControlador`:

<img width="513" height="129" alt="Screenshot 2026-10-14 at 12 01 00 AM" src="https://github.com/user-attachments/assets/8676845d-b2e2-4712-9f52-c52b0c7febc9" />


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

Antes, un cambio en la regla de iniciales (por ejemplo, para manejar un
nombre compuesto como "María José") significaba editar tres archivos.
Ahora significa editar un solo método, `iniciales()`, dentro de
`UsuarioSesionFacade`, y ese cambio queda aplicado automáticamente en las
tres pantallas del flujo de planificación la próxima vez que se usen. Lo
mismo pasa si mañana se agrega un cuarto controlador al flujo: no hay que
copiar nada, solo inyectar la fachada.

## Diseño — Diagrama UML

<img width="620" height="662" alt="DiagramaEstructural(F)GoF" src="https://github.com/user-attachments/assets/f7d35571-45d7-4ab7-8d41-2bcafbba81f4" />

---


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

Antes de ver las reglas, hace falta algo que lleve la información de una a otra. Ese es `ContextoValidacionFechas`: guarda las fechas originales, va acumulando los errores, y guarda las fechas ya parseadas para que la última regla no tenga que volver a interpretarlas.

<img width="684" height="602" alt="Screenshot 2026-10-14 at 12 58 02 PM" src="https://github.com/user-attachments/assets/2c3faade-3812-4ea9-9daf-403eeeccdac4" />


### 3: el eslabón base de la cadena

`ReglaValidacionFechas` es la clase abstracta de la que salen todas las reglas. Define cómo se encadena una regla con la siguiente (`siguiente()`) y el método que dispara la validación (`validar()`), que es `final` porque ninguna regla concreta debe poder cambiar el mecanismo de la cadena, solo su propia lógica.

<img width="693" height="506" alt="Screenshot 2026-10-14 at 12 59 11 PM" src="https://github.com/user-attachments/assets/a42e2eb5-05c8-4923-9730-cf81c398dffa" />


### 4: las tres reglas concretas

`ReglaFechaInicio` y `ReglaFechaFin` siempre devuelven `true` (la cadena sigue pase lo que pase), porque son validaciones independientes y el usuario debería ver los dos errores a la vez si los comete los dos. `ReglaRelacionFechas` es la última: si `inicio` o `fin` quedaron en `null`, corta la cadena sin agregar un error nuevo, porque comparar fechas inválidas no tendría sentido.

<img width="890" height="548" alt="Screenshot 2026-10-14 at 1 00 23 PM" src="https://github.com/user-attachments/assets/5342ec26-bcef-43a1-a6e1-e37065c673fe" />


<img width="730" height="461" alt="Screenshot 2026-10-14 at 1 01 56 PM" src="https://github.com/user-attachments/assets/77077c32-41d5-4721-a666-4070b442e9c4" />


<img width="910" height="510" alt="Screenshot 2026-10-14 at 1 02 40 PM" src="https://github.com/user-attachments/assets/3c6b3e9d-9776-4d8e-ac72-0a95e9824dec" />


### 5: quién arma y dispara la cadena

`FechasServicio` arma la cadena una sola vez, en el constructor, y la reutiliza en cada validación:

<img width="639" height="800" alt="Screenshot 2026-10-14 at 1 03 39 PM" src="https://github.com/user-attachments/assets/60ecb0aa-7007-4760-9284-87868a77f797" />


### ¿Por qué Chain of Responsibility y no Strategy?

Strategy sirve para elegir entre varias formas distintas de resolver el mismo problema (por ejemplo, distintos algoritmos para calcular un descuento). Acá no hay varias formas alternativas de validar una fecha — hay varias validaciones distintas que tienen que aplicarse una detrás de otra, y donde una depende de que la anterior haya funcionado. Eso es justo el caso de uso de Chain of Responsibility.

### ¿Qué gana el equipo con esto?

Cada regla se puede probar sola, armando un `ContextoValidacionFechas` a mano, sin tener que disparar las tres validaciones juntas. Si en el futuro se necesita una regla nueva — por ejemplo, una validación extra para cierto tipo de destino — se crea una clase que extienda `ReglaValidacionFechas` y se agrega a la cadena del constructor, sin tocar ni una línea de las reglas que ya existen.

## Diseño — Diagrama UML

<img width="814" height="656" alt="DiagramaComportamental(CR)GoF" src="https://github.com/user-attachments/assets/474e302a-5dfd-4182-bdf2-8a95dab967a2" />



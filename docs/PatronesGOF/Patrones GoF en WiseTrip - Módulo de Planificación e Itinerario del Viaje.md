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

## 2. Chain of Responsibility — validación de fechas

*(pendiente, se agrega en el siguiente paso del taller)*

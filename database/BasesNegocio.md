# Supuestos Adoptados por el Equipo

Durante el diseño de la base de datos y la definición de las reglas del sistema, el equipo adoptó los siguientes supuestos:

1. **Unicidad del correo electrónico:** cada correo electrónico pertenece a un único usuario. No pueden existir dos cuentas registradas con el mismo correo electrónico dentro de la plataforma.

2. **Un único rol por usuario:** un usuario solo puede tener un rol a la vez, el cual puede ser `administrador` o `cliente`. Este campo define los privilegios dentro del sistema. Por ejemplo, un administrador podrá acceder a funciones de gestión de usuarios y consulta de información.

3. **Propiedad de los viajes:** cada viaje planificado pertenece a un único usuario. Un viaje no puede estar asociado a más de una cuenta ni puede ser compartido entre diferentes usuarios.

4. **Múltiples viajes por usuario:** un usuario podrá planificar y gestionar múltiples viajes.

5. **Coherencia de las fechas:** las fechas de un viaje deben ser coherentes. La fecha de finalización debe ser posterior o igual a la fecha de inicio, según las reglas definidas para el sistema.

6. **Presupuesto positivo:** el presupuesto de un viaje siempre debe ser un valor mayor que cero. No se permiten presupuestos negativos ni iguales a cero.

7. **Un único estado por viaje:** un viaje solo podrá tener un estado a la vez. Por ejemplo, no podrá estar simultáneamente en estado `guardado` y `planificando`.

8. **Preferencias asociadas al viaje:** las preferencias estarán asociadas directamente a cada viaje y no de forma permanente a la cuenta del usuario. Como equipo, se asume que un usuario puede tener intereses diferentes dependiendo del viaje que esté planificando.

9. **Múltiples preferencias por viaje:** un viaje podrá tener diferentes preferencias asociadas. Por ejemplo, un usuario podrá seleccionar varias categorías de interés para un mismo viaje.

10. **No duplicación de preferencias:** no se podrá registrar una misma categoría más de una vez dentro del mismo viaje. Por ejemplo, no será posible seleccionar la categoría `aventura` dos veces para un mismo viaje.

11. **Administradores y clientes en una misma entidad:** no se separarán los administradores y clientes en tablas diferentes. Como equipo, se asume que ambos comparten los mismos atributos básicos y que la diferencia principal se encuentra en los privilegios definidos por el rol.

12. **Almacenamiento de contraseñas:** inicialmente, la contraseña podrá almacenarse durante la fase de desarrollo de forma simple. Sin embargo, para una implementación real y a largo plazo, las contraseñas deberán almacenarse utilizando mecanismos seguros de hash.

13. **Canales de comunicación activos:** se asume que el usuario contará con canales activos de comunicación, como correo electrónico o servicios de mensajería compatibles con la plataforma.

14. **Roles limitados:** los únicos roles disponibles dentro del sistema serán `cliente` y `administrador`. No existirá personalización ni creación dinámica de nuevos roles.

15. **Presupuesto restante:** el presupuesto restante de un viaje se calcula a partir del presupuesto total menos la suma de los gastos registrados; no se almacena como un campo aparte.

16. **Alertas:** las alertas se asocian tanto al viaje que las origina como al canal por el que se envían, permitiendo saber qué se comunicó y por dónde.

---

# Reglas de Negocio

## Gestión de Usuarios

- El correo electrónico de cada usuario debe ser único dentro de la plataforma.
- Los campos `nombre`, `correo` y `contraseña` no pueden ser nulos.
- Un usuario no podrá iniciar sesión si su cuenta está marcada como inactiva.
- El campo `correo` debe contener una dirección de correo electrónico válida.
- La contraseña debe cumplir con una longitud mínima antes de ser almacenada.
- Los únicos roles permitidos para un usuario serán:
  - `cliente`
  - `administrador`
- Un usuario solo podrá tener un rol asignado a la vez.
- Si se elimina un usuario, sus canales de comunicación asociados también se eliminan en cascada.

---

## Gestión de Viajes

- La fecha de finalización debe ser mayor o igual a la fecha de inicio.
- La fecha de inicio de un viaje no podrá ser anterior a la fecha actual.
- El presupuesto de un viaje debe ser mayor que cero (`presupuesto > 0`).
- No se podrá crear un viaje sin un usuario asociado.
- El identificador del usuario será una clave foránea obligatoria (`FK NOT NULL`) dentro de la entidad de viajes.
- Si se elimina un usuario, todos sus viajes asociados también deberán eliminarse mediante la regla `ON DELETE CASCADE`.
- **Presupuesto restante:** se calcula a partir del presupuesto total menos la suma de los gastos registrados; no se almacena como un campo aparte.
- Si se elimina un viaje, también se eliminan en cascada sus actividades de itinerario, gastos y alertas asociadas.

---

## Gestión de Preferencias

- Las preferencias de viaje están asociadas únicamente a un viaje específico.
- Un viaje puede tener múltiples preferencias asociadas.
- No se puede repetir la misma preferencia dentro del mismo viaje.
- No se podrán crear preferencias sin un viaje asociado (`FK NOT NULL`).
- Si se elimina un viaje, sus preferencias también deberán eliminarse en cascada.

---

## Gestión de Gastos

- El monto de un gasto debe ser mayor a `0`.
- No se puede crear un gasto sin su respectiva clave foránea (`FK NOT NULL`).

---

## Gestión de Itinerario

- No se puede crear una actividad de itinerario sin su respectiva clave foránea (`FK NOT NULL`).

---

## Gestión de Reservas

- El estado de una reserva únicamente puede ser:
  - `pendiente`
  - `confirmada`
  - `cancelada`
- No se puede crear una reserva sin su respectiva clave foránea (`FK NOT NULL`).

---

## Gestión de Canales de Notificación

- El tipo de canal de notificación únicamente puede ser:
  - `correo`
  - `telegram`
- Si se elimina un usuario, sus canales de comunicación asociados también se eliminan en cascada.

---

## Gestión de Alertas

- Las alertas se asocian tanto al viaje que las origina como al canal por el que se envían, permitiendo saber qué se comunicó y por dónde.
- El tipo de alerta únicamente puede ser:
  - `clima`
  - `presupuesto`
  - `recomendacion`
- No se puede crear una alerta sin su respectiva clave foránea (`FK NOT NULL`).
- Si se elimina un viaje, las alertas asociadas también se eliminan en cascada.

---

## Seguridad y Acceso

- Un usuario no podrá acceder, eliminar ni modificar los viajes de otro usuario.
- La contraseña se almacena inicialmente como texto plano, pero a largo plazo se implementará como un hash.

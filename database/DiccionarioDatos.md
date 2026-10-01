# Diccionario de Datos

## Entidad: Usuario

| Campo | Tipo de dato | Tamaño | Clave | Restricciones | Descripción |
|---|---|---:|---|---|---|
| `id_usuario` | `integer` | - | PK | - | Identificador único del usuario. |
| `nombre` | `character varying` | - | - | - | Nombre del usuario. |
| `correo` | `USER-DEFINED` | - | - | - | Correo electrónico del usuario. |
| `contraseña` | `character varying` | - | - | - | Contraseña del usuario. |
| `rol` | `character varying` | - | - | - | Rol asignado al usuario dentro del sistema. |
| `fecha_registro` | `timestamp without time zone` | - | - | - | Fecha y hora de registro del usuario. |
| `estado` | `boolean` | - | - | - | Indica el estado del usuario. |
| `tipo_documento` | `character varying` | - | - | - | Tipo de documento de identificación del usuario. |
| `numero_documento` | `character varying` | - | - | - | Número de documento de identificación del usuario. |
| `fecha_nacimiento` | `date` | - | - | - | Fecha de nacimiento del usuario. |
| `chat_id` | `character varying` | - | - | - | Identificador del chat utilizado para enviar información mediante Telegram. |

---

## Entidad: Viajes

| Campo | Tipo de dato | Tamaño | Clave | Restricciones | Descripción |
|---|---|---:|---|---|---|
| `id_viaje` | `integer` | - | PK | - | Identificador único del viaje. |
| `id_usuario` | `integer` | - | FK | Referencia a `usuario(id_usuario)` | Usuario al que pertenece el viaje. |
| `destino` | `character varying` | - | - | - | Destino seleccionado para el viaje. |
| `fecha_inicio` | `date` | - | - | - | Fecha de inicio del viaje. |
| `fecha_fin` | `date` | - | - | - | Fecha de finalización del viaje. |
| `presupuesto` | `numeric` | - | - | - | Presupuesto disponible para el viaje. |
| `fecha_creacion` | `timestamp without time zone` | - | - | - | Fecha y hora de creación del viaje. |

---

## Entidad: Preferencias

| Campo | Tipo de dato | Tamaño | Clave | Restricciones | Descripción |
|---|---|---:|---|---|---|
| `id_preferencia` | `integer` | - | PK | - | Identificador único de la preferencia. |
| `id_viaje` | `integer` | - | FK | Referencia a `viajes(id_viaje)` | Viaje al que pertenece la preferencia. |
| `tipo_preferencia` | `character varying` | - | - | - | Tipo de interés seleccionado para el viaje, como aventura, cultura o gastronomía. |

---

## Entidad: Itinerario

| Campo | Tipo de dato | Tamaño | Clave | Restricciones | Descripción |
|---|---|---:|---|---|---|
| `id_itinerario` | `integer` | - | PK | - | Identificador único de la actividad del itinerario. |
| `id_viaje` | `integer` | - | FK | Referencia a `viajes(id_viaje)` | Viaje al que pertenece la actividad. |
| `nombre` | `character varying` | - | - | - | Nombre o descripción de la actividad. |
| `fecha_actividad` | `date` | - | - | - | Fecha en la que se realizará la actividad. |
| `hora_actividad` | `time without time zone` | - | - | - | Hora programada para la actividad. |
| `tipo` | `character varying` | - | - | - | Tipo de actividad del itinerario. |
| `costo_estimado` | `numeric` | - | - | - | Costo estimado de la actividad. |

---

## Entidad: Gastos

| Campo | Tipo de dato | Tamaño | Clave | Restricciones | Descripción |
|---|---|---:|---|---|---|
| `id_gasto` | `integer` | - | PK | - | Identificador único del gasto. |
| `id_viaje` | `integer` | - | FK | Referencia a `viajes(id_viaje)` | Viaje al que pertenece el gasto. |
| `descripcion` | `character varying` | - | - | - | Descripción del gasto realizado. |
| `monto` | `numeric` | - | - | - | Valor monetario del gasto. |
| `fecha_gasto` | `date` | - | - | - | Fecha en la que se realizó el gasto. |
| `categoria` | `character varying` | - | - | - | Categoría a la que pertenece el gasto. |

---

## Entidad: Reservas

| Campo | Tipo de dato | Tamaño | Clave | Restricciones | Descripción |
|---|---|---:|---|---|---|
| `id_reserva` | `integer` | - | PK | - | Identificador único de la reserva. |
| `id_viaje` | `integer` | - | FK | Referencia a `viajes(id_viaje)` | Viaje al que pertenece la reserva. |
| `tipo` | `character varying` | - | - | - | Tipo de reserva realizada. |
| `descripcion` | `character varying` | - | - | - | Descripción de la reserva. |
| `fecha_reserva` | `date` | - | - | - | Fecha de la reserva. |
| `estado` | `character varying` | - | - | - | Estado actual de la reserva. |

---

## Entidad: Canales de Notificación

| Campo | Tipo de dato | Tamaño | Clave | Restricciones | Descripción |
|---|---|---:|---|---|---|
| `id_canal` | `integer` | - | PK | - | Identificador único del canal de notificación. |
| `id_usuario` | `integer` | - | FK | Referencia a `usuario(id_usuario)` | Usuario propietario del canal de notificación. |
| `tipo_canal` | `character varying` | - | - | - | Tipo de canal utilizado para las notificaciones. |
| `identificador` | `character varying` | - | - | - | Identificador necesario para utilizar el canal de notificación. |
| `activo` | `boolean` | - | - | - | Indica si el canal de notificación está activo. |

---

## Entidad: Alertas

| Campo | Tipo de dato | Tamaño | Clave | Restricciones | Descripción |
|---|---|---:|---|---|---|
| `id_alerta` | `integer` | - | PK | - | Identificador único de la alerta. |
| `id_viaje` | `integer` | - | FK | Referencia a `viajes(id_viaje)` | Viaje relacionado con la alerta. |
| `id_canal` | `integer` | - | FK | Referencia a `canales_notificacion(id_canal)` | Canal mediante el cual se gestiona la alerta. |
| `tipo_alerta` | `character varying` | - | - | - | Tipo de alerta generada por el sistema. |
| `mensaje` | `character varying` | - | - | - | Contenido del mensaje de la alerta. |
| `fecha_envio` | `timestamp without time zone` | - | - | - | Fecha y hora asociada al envío de la alerta. |
| `estado` | `character varying` | - | - | - | Estado actual de la alerta. |

---

## Entidad: Ciudad

| Campo | Tipo de dato | Tamaño | Clave | Restricciones | Descripción |
|---|---|---:|---|---|---|
| `id_ciudad` | `integer` | - | PK | - | Identificador único de la ciudad. |
| `nombre` | `character varying` | - | - | - | Nombre de la ciudad. |
| `pais` | `character varying` | - | - | - | País al que pertenece la ciudad. |
| `latitud` | `numeric` | - | - | - | Coordenada geográfica de latitud de la ciudad. |
| `longitud` | `numeric` | - | - | - | Coordenada geográfica de longitud de la ciudad. |
| `costo_promedio` | `numeric` | - | - | - | Costo promedio asociado a la ciudad. |

---

## Entidad: Atributo

| Campo | Tipo de dato | Tamaño | Clave | Restricciones | Descripción |
|---|---|---:|---|---|---|
| `id_atributo` | `integer` | - | PK | - | Identificador único del atributo. |
| `nombre` | `character varying` | - | - | - | Nombre del atributo o característica asociada a un destino. |

---

## Entidad: Ciudad_Atributo

| Campo | Tipo de dato | Tamaño | Clave | Restricciones | Descripción |
|---|---|---:|---|---|---|
| `id_ciudad` | `integer` | - | PK, FK | Referencia a `ciudad(id_ciudad)` | Identificador de la ciudad relacionada. |
| `id_atributo` | `integer` | - | PK, FK | Referencia a `atributo(id_atributo)` | Identificador del atributo relacionado. |

---

## Entidad: Nivel_Costo

| Campo | Tipo de dato | Tamaño | Clave | Restricciones | Descripción |
|---|---|---:|---|---|---|
| `id_nivel` | `integer` | - | PK | - | Identificador único del nivel de costo. |
| `nombre` | `character varying` | - | - | - | Nombre del nivel de costo asociado a un destino. |

---

# Relaciones entre Entidades

- Un **usuario** puede tener múltiples **viajes**.
- Cada **viaje** pertenece a un único **usuario**.
- Un **usuario** puede tener múltiples **canales de notificación**.
- Cada **canal de notificación** pertenece a un único **usuario**.
- Un **viaje** puede tener múltiples **preferencias**.
- Cada **preferencia** pertenece a un único **viaje**.
- Un **viaje** puede tener múltiples actividades en el **itinerario**.
- Cada actividad del **itinerario** pertenece a un único **viaje**.
- Un **viaje** puede tener múltiples **gastos**.
- Cada **gasto** pertenece a un único **viaje**.
- Un **viaje** puede tener múltiples **reservas**.
- Cada **reserva** pertenece a un único **viaje**.
- Un **viaje** puede tener múltiples **alertas**.
- Cada **alerta** pertenece a un único **viaje**.
- Un **canal de notificación** puede estar relacionado con múltiples **alertas**.
- Cada **alerta** utiliza un único **canal de notificación**.
- Una **ciudad** puede estar relacionada con múltiples **atributos**.
- Un **atributo** puede estar relacionado con múltiples **ciudades**.
- La relación entre **ciudad** y **atributo** se establece mediante la tabla intermedia `ciudad_atributo`.
- La tabla **nivel_costo** no presenta relaciones con otras tablas en el JSON actual.

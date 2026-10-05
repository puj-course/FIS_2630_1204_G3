# Diccionario de Datos

## Entidad: Usuario

| Campo              | Tipo de dato                | Tamaño | Clave | Restricciones       | Descripción                                                             |
| ------------------ | --------------------------- | -----: | ----- | ------------------- | ----------------------------------------------------------------------- |
| `id_usuario`       | integer                     |      — | PK    | Identificador único | Identificador único del usuario.                                        |
| `nombre`           | varchar                     |    100 | —     | —                   | Nombre completo del usuario.                                            |
| `correo`           | USER-DEFINED                |      — | —     | —                   | Correo electrónico utilizado por el usuario.                            |
| `contraseña`       | varchar                     |    255 | —     | —                   | Contraseña asociada a la cuenta del usuario.                            |
| `rol`              | varchar                     |     20 | —     | —                   | Rol que tiene el usuario dentro del sistema.                            |
| `fecha_registro`   | timestamp without time zone |      — | —     | —                   | Fecha y hora en la que se registra el usuario.                          |
| `estado`           | boolean                     |      — | —     | —                   | Indica el estado activo o inactivo del usuario.                         |
| `tipo_documento`   | varchar                     |     30 | —     | —                   | Tipo de documento de identificación del usuario.                        |
| `numero_documento` | varchar                     |     30 | —     | —                   | Número de documento de identificación del usuario.                      |
| `fecha_nacimiento` | date                        |      — | —     | —                   | Fecha de nacimiento del usuario.                                        |
| `chat_id`          | varchar                     |     50 | —     | —                   | Identificador utilizado para la comunicación mediante un canal de chat. |

---

## Entidad: Viajes

| Campo            | Tipo de dato                | Tamaño | Clave | Restricciones                      | Descripción                                  |
| ---------------- | --------------------------- | -----: | ----- | ---------------------------------- | -------------------------------------------- |
| `id_viaje`       | integer                     |      — | PK    | Identificador único                | Identificador único del viaje.               |
| `id_usuario`     | integer                     |      — | FK    | Referencia a `usuario(id_usuario)` | Identifica al usuario propietario del viaje. |
| `destino`        | varchar                     |    150 | —     | —                                  | Destino seleccionado para el viaje.          |
| `fecha_inicio`   | date                        |      — | —     | —                                  | Fecha de inicio del viaje.                   |
| `fecha_fin`      | date                        |      — | —     | —                                  | Fecha de finalización del viaje.             |
| `presupuesto`    | numeric                     |   12,2 | —     | —                                  | Presupuesto establecido para el viaje.       |
| `fecha_creacion` | timestamp without time zone |      — | —     | —                                  | Fecha y hora en que se crea el viaje.        |

---

## Entidad: Preferencias

| Campo              | Tipo de dato | Tamaño | Clave | Restricciones                   | Descripción                                          |
| ------------------ | ------------ | -----: | ----- | ------------------------------- | ---------------------------------------------------- |
| `id_preferencia`   | integer      |      — | PK    | Identificador único             | Identificador único de la preferencia.               |
| `id_viaje`         | integer      |      — | FK    | Referencia a `viajes(id_viaje)` | Identifica el viaje al que pertenece la preferencia. |
| `tipo_preferencia` | varchar      |     50 | —     | —                               | Tipo de preferencia seleccionada para el viaje.      |

---

## Entidad: Itinerario

| Campo             | Tipo de dato           | Tamaño | Clave | Restricciones                   | Descripción                                         |
| ----------------- | ---------------------- | -----: | ----- | ------------------------------- | --------------------------------------------------- |
| `id_itinerario`   | integer                |      — | PK    | Identificador único             | Identificador único de la actividad del itinerario. |
| `id_viaje`        | integer                |      — | FK    | Referencia a `viajes(id_viaje)` | Identifica el viaje al que pertenece la actividad.  |
| `nombre`          | varchar                |    150 | —     | —                               | Nombre de la actividad programada.                  |
| `fecha_actividad` | date                   |      — | —     | —                               | Fecha en la que se realizará la actividad.          |
| `hora_actividad`  | time without time zone |      — | —     | —                               | Hora programada para realizar la actividad.         |
| `tipo`            | varchar                |     50 | —     | —                               | Tipo o categoría de la actividad.                   |
| `costo_estimado`  | numeric                |   12,2 | —     | —                               | Costo estimado de la actividad.                     |

---

## Entidad: Gastos

| Campo         | Tipo de dato | Tamaño | Clave | Restricciones                   | Descripción                                    |
| ------------- | ------------ | -----: | ----- | ------------------------------- | ---------------------------------------------- |
| `id_gasto`    | integer      |      — | PK    | Identificador único             | Identificador único del gasto.                 |
| `id_viaje`    | integer      |      — | FK    | Referencia a `viajes(id_viaje)` | Identifica el viaje al que pertenece el gasto. |
| `descripcion` | varchar      |    200 | —     | —                               | Descripción del gasto realizado.               |
| `monto`       | numeric      |   12,2 | —     | —                               | Valor monetario del gasto.                     |
| `fecha_gasto` | date         |      — | —     | —                               | Fecha en la que se realizó el gasto.           |
| `categoria`   | varchar      |     50 | —     | —                               | Categoría a la que pertenece el gasto.         |

---

## Entidad: Reservas

| Campo           | Tipo de dato | Tamaño | Clave | Restricciones                   | Descripción                                        |
| --------------- | ------------ | -----: | ----- | ------------------------------- | -------------------------------------------------- |
| `id_reserva`    | integer      |      — | PK    | Identificador único             | Identificador único de la reserva.                 |
| `id_viaje`      | integer      |      — | FK    | Referencia a `viajes(id_viaje)` | Identifica el viaje asociado a la reserva.         |
| `tipo`          | varchar      |     30 | —     | —                               | Tipo de reserva realizada.                         |
| `descripcion`   | varchar      |    250 | —     | —                               | Descripción o información adicional de la reserva. |
| `fecha_reserva` | date         |      — | —     | —                               | Fecha de la reserva.                               |
| `estado`        | varchar      |     20 | —     | —                               | Estado actual de la reserva.                       |

---

## Entidad: Canales de Notificación

| Campo           | Tipo de dato | Tamaño | Clave | Restricciones                      | Descripción                                                                 |
| --------------- | ------------ | -----: | ----- | ---------------------------------- | --------------------------------------------------------------------------- |
| `id_canal`      | integer      |      — | PK    | Identificador único                | Identificador único del canal de notificación.                              |
| `id_usuario`    | integer      |      — | FK    | Referencia a `usuario(id_usuario)` | Identifica al usuario propietario del canal.                                |
| `tipo_canal`    | varchar      |     20 | —     | —                                  | Tipo de canal utilizado para las notificaciones.                            |
| `identificador` | varchar      |    150 | —     | —                                  | Identificador o dato utilizado para contactar al usuario mediante el canal. |
| `activo`        | boolean      |      — | —     | —                                  | Indica si el canal de notificación se encuentra activo.                     |

---

## Entidad: Alertas

| Campo         | Tipo de dato                | Tamaño | Clave | Restricciones                                 | Descripción                                                 |
| ------------- | --------------------------- | -----: | ----- | --------------------------------------------- | ----------------------------------------------------------- |
| `id_alerta`   | integer                     |      — | PK    | Identificador único                           | Identificador único de la alerta.                           |
| `id_viaje`    | integer                     |      — | FK    | Referencia a `viajes(id_viaje)`               | Identifica el viaje relacionado con la alerta.              |
| `id_canal`    | integer                     |      — | FK    | Referencia a `canales_notificacion(id_canal)` | Identifica el canal mediante el cual se gestiona la alerta. |
| `tipo_alerta` | varchar                     |     30 | —     | —                                             | Tipo de alerta generada.                                    |
| `mensaje`     | varchar                     |    500 | —     | —                                             | Contenido del mensaje de la alerta.                         |
| `fecha_envio` | timestamp without time zone |      — | —     | —                                             | Fecha y hora asociada al envío de la alerta.                |
| `estado`      | varchar                     |     20 | —     | —                                             | Estado actual de la alerta.                                 |

---

## Entidad: Ciudad

| Campo            | Tipo de dato | Tamaño | Clave | Restricciones       | Descripción                                     |
| ---------------- | ------------ | -----: | ----- | ------------------- | ----------------------------------------------- |
| `id_ciudad`      | integer      |      — | PK    | Identificador único | Identificador único de la ciudad.               |
| `nombre`         | varchar      |    100 | —     | —                   | Nombre de la ciudad.                            |
| `pais`           | varchar      |    100 | —     | —                   | País en el que se encuentra la ciudad.          |
| `latitud`        | numeric      |    9,6 | —     | —                   | Coordenada geográfica de latitud de la ciudad.  |
| `longitud`       | numeric      |    9,6 | —     | —                   | Coordenada geográfica de longitud de la ciudad. |
| `costo_promedio` | numeric      |   12,2 | —     | —                   | Costo diario estimado en USD por persona (alojamiento, comida y transporte local).            |

---

## Entidad: Atributo

| Campo         | Tipo de dato | Tamaño | Clave | Restricciones       | Descripción                                |
| ------------- | ------------ | -----: | ----- | ------------------- | ------------------------------------------ |
| `id_atributo` | integer      |      — | PK    | Identificador único | Identificador único del atributo.          |
| `nombre`      | varchar      |     50 | —     | —                   | Nombre del atributo asociado a una ciudad. |

---

## Entidad: Ciudad_Atributo

| Campo         | Tipo de dato | Tamaño | Clave  | Restricciones                        | Descripción                                       |
| ------------- | ------------ | -----: | ------ | ------------------------------------ | ------------------------------------------------- |
| `id_ciudad`   | integer      |      — | PK, FK | Referencia a `ciudad(id_ciudad)`     | Identifica la ciudad relacionada con el atributo. |
| `id_atributo` | integer      |      — | PK, FK | Referencia a `atributo(id_atributo)` | Identifica el atributo relacionado con la ciudad. |

> La combinación `id_ciudad` + `id_atributo` conforma la clave primaria de esta entidad y permite relacionar ciudades con sus atributos.

---

# Relaciones entre Entidades

| Entidad origen         | Campo         | Tipo de relación | Entidad destino        | Campo relacionado |
| ---------------------- | ------------- | ---------------- | ---------------------- | ----------------- |
| `viajes`               | `id_usuario`  | FK               | `usuario`              | `id_usuario`      |
| `preferencias`         | `id_viaje`    | FK               | `viajes`               | `id_viaje`        |
| `itinerario`           | `id_viaje`    | FK               | `viajes`               | `id_viaje`        |
| `gastos`               | `id_viaje`    | FK               | `viajes`               | `id_viaje`        |
| `reservas`             | `id_viaje`    | FK               | `viajes`               | `id_viaje`        |
| `canales_notificacion` | `id_usuario`  | FK               | `usuario`              | `id_usuario`      |
| `alertas`              | `id_viaje`    | FK               | `viajes`               | `id_viaje`        |
| `alertas`              | `id_canal`    | FK               | `canales_notificacion` | `id_canal`        |
| `ciudad_atributo`      | `id_ciudad`   | PK, FK           | `ciudad`               | `id_ciudad`       |
| `ciudad_atributo`      | `id_atributo` | PK, FK           | `atributo`             | `id_atributo`     |

---

# Resumen de Entidades

| Entidad                | Propósito                                                                       |
| ---------------------- | ------------------------------------------------------------------------------- |
| `usuario`              | Almacena la información de los usuarios del sistema.                            |
| `viajes`               | Almacena los viajes creados por los usuarios.                                   |
| `preferencias`         | Registra las preferencias asociadas a cada viaje.                               |
| `itinerario`           | Almacena las actividades programadas para cada viaje.                           |
| `gastos`               | Registra los gastos realizados durante los viajes.                              |
| `reservas`             | Almacena las reservas asociadas a los viajes.                                   |
| `canales_notificacion` | Gestiona los canales utilizados para enviar notificaciones a los usuarios.      |
| `alertas`              | Registra las alertas relacionadas con los viajes y sus canales de notificación. |
| `ciudad`               | Almacena información de las ciudades disponibles como destinos.                 |
| `atributo`             | Almacena los atributos que pueden asociarse a las ciudades.                     |
| `ciudad_atributo`      | Relaciona las ciudades con sus atributos.                                       |

---

# Convenciones utilizadas

* **PK (Primary Key):** identifica de manera única cada registro de una entidad.
* **FK (Foreign Key):** establece una referencia entre una entidad y otra.
* **PK, FK:** el campo forma parte de la clave primaria y, al mismo tiempo, referencia otra entidad.
* **varchar(n):** cadena de caracteres con una longitud máxima de `n`.
* **numeric(p,s):** número decimal donde `p` corresponde a la precisión total y `s` al número de posiciones decimales.
* **integer:** número entero.
* **boolean:** valor lógico verdadero o falso.
* **date:** fecha.
* **timestamp without time zone:** fecha y hora sin información de zona horaria.
* **time without time zone:** hora sin información de zona horaria.

> **Nota:** Las longitudes y tipos de datos utilizados en este diccionario corresponden al JSON proporcionado. Por ejemplo, `chat_id` es `varchar(50)`, `mensaje` es `varchar(500)`, `destino` es `varchar(150)` y `costo_promedio` es `numeric(12,2)`. Para `correo` se conserva `USER-DEFINED` porque así aparece definido en la información proporcionada.

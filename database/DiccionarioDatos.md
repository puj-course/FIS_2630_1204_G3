# Diccionario de Datos

## Entidad: Usuario

| Campo | Tipo de dato | Tamaño | Clave | Restricciones | Descripción |
|---|---|---:|---|---|---|
| `id_usuario` | `INT` | - | PK | - | Identificador único del usuario. |
| `nombre` | `VARCHAR` | 100 | - | `NOT NULL` | Nombre del usuario. |
| `correo` | `VARCHAR` | 150 | UK | `NOT NULL`, `UNIQUE` | Correo electrónico único del usuario. |
| `contraseña` | `VARCHAR` | 255 | - | `NOT NULL` | Contraseña del usuario. |
| `rol` | `VARCHAR` | 20 | - | `NOT NULL` | Define el rol del usuario dentro de la plataforma, por ejemplo, cliente o administrador. |
| `fecha_registro` | `TIMESTAMP` | - | - | `NOT NULL`, `DEFAULT CURRENT_TIMESTAMP` | Fecha y hora de creación de la cuenta. |
| `estado` | `BOOLEAN` | - | - | `NOT NULL` | Indica si la cuenta del usuario se encuentra activa o inactiva. |

---

## Entidad: Viaje

| Campo | Tipo de dato | Tamaño | Clave | Restricciones | Descripción |
|---|---|---:|---|---|---|
| `id_viaje` | `INT` | - | PK | - | Identificador único del viaje. |
| `id_usuario` | `INT` | - | FK | `REFERENCES usuario(id_usuario)`, `NOT NULL` | Usuario que planificó el viaje. |
| `destino` | `VARCHAR` | 150 | - | `NOT NULL` | Lugar de destino del viaje. |
| `fecha_inicio` | `DATE` | - | - | `NOT NULL` | Fecha de inicio del viaje. |
| `fecha_fin` | `DATE` | - | - | `NOT NULL` | Fecha de finalización del viaje. |
| `presupuesto` | `DECIMAL` | - | - | `NOT NULL` | Presupuesto asignado para el viaje. |
| `fecha_creacion` | `TIMESTAMP` | - | - | `NOT NULL`, `DEFAULT CURRENT_TIMESTAMP` | Fecha y hora en la que se registró la planificación del viaje. |

---

## Entidad: Preferencias

| Campo | Tipo de dato | Tamaño | Clave | Restricciones | Descripción |
|---|---|---:|---|---|---|
| `id_preferencia` | `INT` | - | PK | - | Identificador único del registro de preferencia. |
| `id_viaje` | `INT` | - | FK | `REFERENCES viaje(id_viaje)`, `NOT NULL` | Viaje al cual se encuentran asociadas las preferencias. |
| `tipo_preferencia` | `VARCHAR` | 50 | - | `NOT NULL` | Categoría de interés seleccionada para el viaje, por ejemplo: aventura, cultura o gastronomía. |

---

## Relaciones entre Entidades

- Un **usuario** puede planificar múltiples **viajes**.
- Cada **viaje** pertenece a un único **usuario**.
- Un **viaje** puede tener múltiples **preferencias**.
- Cada **preferencia** pertenece a un único **viaje**.

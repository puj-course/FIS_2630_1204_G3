## Funcionamiento general

Cada usuario planifica viajes con sus preferencias, itinerarios y gastos. Las ciudades aportan el costo diario estimado en USD por persona. El presupuesto se interpreta como total por persona para la estancia.


## Relaciones principales

El modelo puede entenderse mediante las siguientes relaciones:

- **`usuario` → `viajes`:** un usuario puede tener múltiples viajes, pero cada viaje pertenece a un único usuario.
- **`usuario` → `canales_notificacion`:** un usuario puede tener uno o varios canales de notificación.
- **`viajes` → `preferencias`:** un viaje puede tener múltiples preferencias.
- **`viajes` → `itinerario`:** un viaje puede contener múltiples actividades.
- **`viajes` → `gastos`:** un viaje puede tener múltiples gastos.
- **`viajes` → `reservas`:** un viaje puede tener múltiples reservas.
- **`viajes` → `alertas`:** un viaje puede generar múltiples alertas.
- **`canales_notificacion` → `alertas`:** un canal puede ser utilizado para gestionar múltiples alertas.
- **`ciudad` ↔ `atributo`:** una ciudad puede tener múltiples atributos y un atributo puede pertenecer a múltiples ciudades. Esta relación se implementa mediante `ciudad_atributo`.

Las relaciones dependientes de `viajes` utilizan eliminación en cascada (`ON DELETE CASCADE`), por lo que al eliminar un viaje también se eliminan sus preferencias, actividades del itinerario, gastos, reservas y alertas asociadas.


Son catálogos pensados para una futura función de recomendación de destinos:

- `ciudad` guarda información de lugares que pueden ser recomendados, incluyendo nombre, país, ubicación geográfica y costo promedio.
- `atributo` guarda características que pueden describir una ciudad, como playa, cultura, aventura o gastronomía.
- `ciudad_atributo` establece la relación entre ciudades y atributos.

Actualmente estas tablas no participan directamente en el flujo principal de planificación de viajes.


---

# 1. USUARIO

La tabla `usuario` guarda la información de las personas que utilizan la aplicación. Contiene los datos básicos de identificación y acceso, como nombre, correo y contraseña, además del tipo de cuenta, que puede ser `cliente` o `administrador`.

También almacena información adicional de identificación (`tipo_documento` y `numero_documento`), fecha de nacimiento, estado de la cuenta y fecha de registro.

El campo `chat_id` permite almacenar el identificador necesario para relacionar al usuario con un servicio de mensajería, principalmente Telegram, y facilitar posteriormente el envío de notificaciones.

El correo electrónico es único dentro del sistema y el número de documento también se maneja como único, evitando registros duplicados.

### Campos

| Campo | Tipo | Clave | Descripción |
|---|---|---|---|
| `id_usuario` | integer | PK | Identificador único del usuario. |
| `nombre` | character varying | | Nombre del usuario. |
| `correo` | USER-DEFINED | | Correo electrónico utilizado para la cuenta. |
| `contraseña` | character varying | | Contraseña de acceso. |
| `rol` | character varying | | Define si el usuario es `cliente` o `administrador`. |
| `fecha_registro` | timestamp without time zone | | Fecha y hora de registro de la cuenta. |
| `estado` | boolean | | Indica si la cuenta se encuentra activa. |
| `tipo_documento` | character varying | | Tipo de documento de identificación. |
| `numero_documento` | character varying | | Número del documento de identificación. |
| `fecha_nacimiento` | date | | Fecha de nacimiento del usuario. |
| `chat_id` | character varying | | Identificador utilizado para la comunicación mediante Telegram. |

```sql
CREATE TABLE usuario (

    id_usuario      SERIAL PRIMARY KEY,
    nombre         VARCHAR(100) NOT NULL,
    correo         VARCHAR(150) NOT NULL,
    contraseña     VARCHAR(255) NOT NULL,
    rol            VARCHAR(20) NOT NULL,
    fecha_registro TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    estado         BOOLEAN NOT NULL DEFAULT TRUE,
    chat_id        VARCHAR(50),

    tipo_documento VARCHAR(30),
    numero_documento VARCHAR(30),
    fecha_nacimiento DATE,

    CONSTRAINT uk_usuario_correo
        UNIQUE (correo),

    CONSTRAINT chk_usuario_rol
        CHECK (rol IN ('cliente', 'administrador'))
);

CREATE UNIQUE INDEX IF NOT EXISTS ux_usuario_numero_documento
ON usuario (numero_documento);

```
--- 

## 2. CIUDAD

La tabla `ciudad` guarda información de los lugares que el usuario podría visitar.

Además del nombre y país, almacena las coordenadas geográficas mediante `latitud` y `longitud`, lo que permite utilizar posteriormente la información para mapas o servicios relacionados con localización.

El campo `costo_promedio` representa una estimación del costo promedio asociado a una ciudad y puede utilizarse como uno de los criterios para la futura recomendación de destinos.

Una ciudad se identifica de manera única mediante la combinación de `nombre` y `pais`, evitando tener dos registros para la misma ciudad dentro del mismo país.

### Campos

| Campo | Tipo | Clave | Descripción |
|---|---|---|---|
| `id_ciudad` | integer | PK | Identificador único de la ciudad. |
| `nombre` | character varying | | Nombre de la ciudad. |
| `pais` | character varying | | País al que pertenece la ciudad. |
| `latitud` | numeric | | Coordenada geográfica de latitud. |
| `longitud` | numeric | | Coordenada geográfica de longitud. |
| `costo_promedio` | numeric | | Costo diario estimado en USD por persona (alojamiento, comida y transporte local). |

### SQL

```sql
CREATE TABLE ciudad (
    id_ciudad       SERIAL PRIMARY KEY,
    nombre          VARCHAR(100) NOT NULL,
    pais            VARCHAR(100) NOT NULL,
    latitud         DECIMAL(9,6) NOT NULL,
    longitud        DECIMAL(9,6) NOT NULL,
    costo_promedio  DECIMAL(12,2),
    CONSTRAINT chk_ciudad_costo CHECK (costo_promedio > 0),
    CONSTRAINT uk_ciudad_nombre_pais UNIQUE (nombre, pais)
);
```
---

# 3. VIAJES

Es una de las tablas más importantes porque representa el núcleo de la planificación de un viaje.
Cada fila representa un viaje planeado por un usuario: a dónde quiere ir, cuándo empieza y termina y cuánto presupuesto tiene disponible.
Cada viaje está obligatoriamente ligado a un usuario mediante `id_usuario`, por lo que nunca debería existir un viaje sin propietario.
La base de datos valida que la fecha de finalización no sea anterior a la fecha de inicio y que el presupuesto sea mayor que cero.
Si se elimina el usuario, todos sus viajes se eliminan automáticamente mediante `ON DELETE CASCADE`. De igual manera, al eliminar un viaje se eliminan los registros dependientes asociados a este.

### Campos

| Campo            | Tipo                        | Clave | Descripción                           |
| ---------------- | --------------------------- | ----- | ------------------------------------- |
| `id_viaje`       | integer                     | PK    | Identificador único del viaje.        |
| `id_usuario`     | integer                     | FK    | Usuario propietario del viaje.        |
| `destino`        | character varying           |       | Destino seleccionado para el viaje.   |
| `fecha_inicio`   | date                        |       | Fecha de inicio del viaje.            |
| `fecha_fin`      | date                        |       | Fecha de finalización del viaje.      |
| `presupuesto`    | numeric                     |       | Presupuesto disponible para el viaje. |
| `fecha_creacion` | timestamp without time zone |       | Fecha y hora de creación del viaje.   |

```sql
CREATE TABLE viajes (

    id_viaje        SERIAL PRIMARY KEY,
    id_usuario      INT NOT NULL,
    destino         VARCHAR(150) NOT NULL,
    fecha_inicio    DATE NOT NULL,
    fecha_fin       DATE NOT NULL,
    presupuesto     DECIMAL(12,2) NOT NULL,
    fecha_creacion  TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_viajes_usuario
        FOREIGN KEY (id_usuario)
        REFERENCES usuario(id_usuario)
        ON DELETE CASCADE,

    CONSTRAINT chk_viajes_fechas
        CHECK (fecha_fin >= fecha_inicio),

    CONSTRAINT chk_viajes_presupuesto
        CHECK (presupuesto > 0)
);
```
---

## 4. PREFERENCIAS

La tabla `preferencias` guarda los intereses que el usuario definió para un viaje específico, por ejemplo, aventura, cultura o gastronomía.

No está ligada directamente al usuario, sino al viaje, porque una misma persona puede querer diferentes tipos de experiencias dependiendo del viaje que esté planeando.

La combinación de `id_viaje` y `tipo_preferencia` es única, por lo que no se permite registrar dos veces la misma preferencia dentro de un mismo viaje.

### Campos

| Campo | Tipo | Clave | Descripción |
|---|---|---|---|
| `id_preferencia` | integer | PK | Identificador único de la preferencia. |
| `id_viaje` | integer | FK | Viaje al que pertenece la preferencia. |
| `tipo_preferencia` | character varying | | Tipo de interés seleccionado por el usuario. |

### Script SQL

```sql
CREATE TABLE preferencias (

    id_preferencia   SERIAL PRIMARY KEY,
    id_viaje         INT NOT NULL,
    tipo_preferencia VARCHAR(50) NOT NULL,

    CONSTRAINT fk_preferencia_viaje
        FOREIGN KEY (id_viaje)
        REFERENCES viajes(id_viaje)
        ON DELETE CASCADE,

    CONSTRAINT uk_preferencias_viaje_tipo
        UNIQUE (id_viaje, tipo_preferencia)
);
```
---
## 5. ITINERARIO

La tabla `itinerario` organiza las actividades día por día dentro de un viaje.

Cada registro representa una actividad e indica qué se va a hacer, en qué fecha, a qué hora, qué tipo de actividad es y cuál es su costo estimado.

Cada actividad pertenece a un único viaje mediante `id_viaje`.

El campo `costo_estimado` permite diferenciar entre el costo que se había previsto para una actividad y los gastos reales registrados posteriormente en `gastos`.

### Campos

| Campo | Tipo | Clave | Descripción |
|---|---|---|---|
| `id_itinerario` | integer | PK | Identificador único de la actividad. |
| `id_viaje` | integer | FK | Viaje al que pertenece la actividad. |
| `nombre` | character varying | | Nombre o descripción de la actividad. |
| `fecha_actividad` | date | | Fecha en la que se realizará la actividad. |
| `hora_actividad` | time without time zone | | Hora programada para la actividad. |
| `tipo` | character varying | | Tipo de actividad. |
| `costo_estimado` | numeric | | Costo estimado de la actividad. |

### Script SQL

```sql
CREATE TABLE itinerario (

    id_itinerario   SERIAL PRIMARY KEY,
    id_viaje        INT NOT NULL,
    nombre          VARCHAR(150) NOT NULL,
    fecha_actividad DATE NOT NULL,
    hora_actividad  TIME NOT NULL,
    tipo            VARCHAR(50) NOT NULL,
    costo_estimado  DECIMAL(12,2) NOT NULL,

    CONSTRAINT fk_itinerario_viaje
        FOREIGN KEY (id_viaje)
        REFERENCES viajes(id_viaje)
        ON DELETE CASCADE,

    CONSTRAINT chk_itinerario_costo
        CHECK (costo_estimado >= 0)
);
```
---
## 6. GASTOS

La tabla `gastos` registra el dinero que realmente se ha gastado durante el viaje, a diferencia del presupuesto, que representa el valor disponible o planeado inicialmente.

Cada gasto contiene una descripción, monto, fecha y categoría. Esta información permite llevar control del consumo del presupuesto y comparar el presupuesto inicial con los gastos realizados.

El presupuesto restante puede obtenerse mediante:

```
Presupuesto restante = presupuesto del viaje − suma de los gastos registrados
```

No es necesario almacenar el presupuesto restante como una columna independiente porque puede calcularse a partir de los datos existentes.

### Campos

| Campo | Tipo | Clave | Descripción |
|---|---|---|---|
| `id_gasto` | integer | PK | Identificador único del gasto. |
| `id_viaje` | integer | FK | Viaje al que pertenece el gasto. |
| `descripcion` | character varying | | Descripción del gasto realizado. |
| `monto` | numeric | | Valor monetario del gasto. |
| `fecha_gasto` | date | | Fecha en que se realizó el gasto. |
| `categoria` | character varying | | Categoría a la que pertenece el gasto. |

### Script SQL

```sql
CREATE TABLE gastos (

    id_gasto      SERIAL PRIMARY KEY,
    id_viaje      INT NOT NULL,
    descripcion   VARCHAR(200) NOT NULL,
    monto         DECIMAL(12,2) NOT NULL,
    fecha_gasto   DATE NOT NULL,
    categoria     VARCHAR(50) NOT NULL,

    CONSTRAINT fk_gastos_viaje
        FOREIGN KEY (id_viaje)
        REFERENCES viajes(id_viaje)
        ON DELETE CASCADE,

    CONSTRAINT chk_gastos_monto
        CHECK (monto > 0)
);
```
---
## 7. RESERVAS

La tabla `reservas` centraliza las reservas realizadas para un viaje, como hospedaje, transporte o actividades.

Cada reserva contiene su tipo, descripción, fecha y estado.

El campo `estado` permite representar si la reserva está:

- confirmada
- pendiente
- cancelada

Cada reserva pertenece a un único viaje mediante `id_viaje`.

### Campos

| Campo | Tipo | Clave | Descripción |
|---|---|---|---|
| `id_reserva` | integer | PK | Identificador único de la reserva. |
| `id_viaje` | integer | FK | Viaje al que pertenece la reserva. |
| `tipo` | character varying | | Tipo de reserva, por ejemplo hospedaje o transporte. |
| `descripcion` | character varying | | Descripción de la reserva. |
| `fecha_reserva` | date | | Fecha de la reserva. |
| `estado` | character varying | | Estado actual de la reserva. |

### Script SQL

```sql
CREATE TABLE reservas (

    id_reserva     SERIAL PRIMARY KEY,
    id_viaje       INT NOT NULL,
    tipo           VARCHAR(30) NOT NULL,
    descripcion    VARCHAR(250) NOT NULL,
    fecha_reserva  DATE NOT NULL,
    estado         VARCHAR(20) NOT NULL,

    CONSTRAINT fk_reservas_viaje
        FOREIGN KEY (id_viaje)
        REFERENCES viajes(id_viaje)
        ON DELETE CASCADE,

    CONSTRAINT chk_reservas_estado
        CHECK (estado IN ('confirmada', 'pendiente', 'cancelada'))
);
```
---
## 8. CANALES DE NOTIFICACIÓN

La tabla `canales_notificacion` define los medios mediante los cuales un usuario puede recibir notificaciones de WiseTrip.

Actualmente se contemplan dos tipos de canal:

- correo
- telegram

El campo `identificador` almacena el dato necesario para utilizar el canal. Por ejemplo, puede almacenar una dirección de correo electrónico o el identificador correspondiente a Telegram.

El campo `activo` permite determinar si el canal está habilitado para recibir notificaciones.

Un usuario puede tener varios canales de notificación y cada canal pertenece a un único usuario mediante `id_usuario`.

### Campos

| Campo | Tipo | Clave | Descripción |
|---|---|---|---|
| `id_canal` | integer | PK | Identificador único del canal. |
| `id_usuario` | integer | FK | Usuario propietario del canal. |
| `tipo_canal` | character varying | | Tipo de canal: correo o telegram. |
| `identificador` | character varying | | Dato necesario para utilizar el canal. |
| `activo` | boolean | | Indica si el canal está habilitado. |

### Script SQL

```sql
CREATE TABLE canales_notificacion (

    id_canal       SERIAL PRIMARY KEY,
    id_usuario     INT NOT NULL,
    tipo_canal     VARCHAR(20) NOT NULL,
    identificador  VARCHAR(150) NOT NULL,
    activo         BOOLEAN NOT NULL,

    CONSTRAINT fk_canal_usuario
        FOREIGN KEY (id_usuario)
        REFERENCES usuario(id_usuario)
        ON DELETE CASCADE,

    CONSTRAINT chk_canal_tipo
        CHECK (tipo_canal IN ('correo', 'telegram'))
);
```
---
## 9. ALERTAS

La tabla `alertas` almacena las notificaciones que el sistema genera para informar al usuario sobre diferentes eventos relacionados con su viaje.

Actualmente se contemplan tres tipos de alerta:

- clima
- presupuesto
- recomendacion

Cada alerta está relacionada con un viaje mediante `id_viaje` y con un canal específico mediante `id_canal`.

De esta forma es posible identificar qué alerta se generó, para qué viaje, mediante qué canal fue gestionada y cuál es su estado.

El campo `estado` permite distinguir entre una alerta pendiente y una alerta enviada.

### Campos

| Campo | Tipo | Clave | Descripción |
|---|---|---|---|
| `id_alerta` | integer | PK | Identificador único de la alerta. |
| `id_viaje` | integer | FK | Viaje al que pertenece la alerta. |
| `id_canal` | integer | FK | Canal mediante el cual se gestiona la alerta. |
| `tipo_alerta` | character varying | | Tipo de alerta: clima, presupuesto o recomendacion. |
| `mensaje` | character varying | | Contenido del mensaje de la alerta. |
| `fecha_envio` | timestamp without time zone | | Fecha y hora asociada al envío de la alerta. |
| `estado` | character varying | | Estado de la alerta: pendiente o enviada. |

### Relaciones de alertas

La tabla `alertas` tiene dos relaciones principales:

- `alertas` → `viajes`
- `alertas` → `canales_notificacion`

La primera permite identificar a qué viaje pertenece la alerta.

La segunda permite identificar mediante qué canal se gestiona la notificación.

El uso de `ON DELETE RESTRICT` en `id_canal` evita eliminar un canal que todavía está siendo referenciado por alertas existentes.

### Script SQL

```sql
CREATE TABLE alertas (

    id_alerta     SERIAL PRIMARY KEY,
    id_viaje      INT NOT NULL,
    id_canal      INT NOT NULL,
    tipo_alerta   VARCHAR(30) NOT NULL,
    mensaje       VARCHAR(500) NOT NULL,
    fecha_envio   TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    estado        VARCHAR(20) NOT NULL,

    CONSTRAINT fk_alerta_viaje
        FOREIGN KEY (id_viaje)
        REFERENCES viajes(id_viaje)
        ON DELETE CASCADE,

    CONSTRAINT fk_alerta_canal
        FOREIGN KEY (id_canal)
        REFERENCES canales_notificacion(id_canal)
        ON DELETE RESTRICT,

    CONSTRAINT chk_alerta_tipo
        CHECK (tipo_alerta IN ('clima', 'presupuesto', 'recomendacion')),

    CONSTRAINT chk_alerta_estado
        CHECK (estado IN ('pendiente', 'enviada'))
);
```
---
## 10. CIUDAD_ATRIBUTO

La tabla `ciudad_atributo` conecta una ciudad con uno o varios atributos.

Esta tabla es necesaria porque la relación entre ciudades y atributos es de muchos a muchos:

- Una ciudad puede tener múltiples atributos.
- Un mismo atributo puede estar asociado a múltiples ciudades.

Por ejemplo, una ciudad podría estar asociada con cultura, gastronomía y aventura, mientras que el atributo cultura podría estar asociado con múltiples ciudades.

Por esta razón, la clave primaria está compuesta por:

```
(id_ciudad, id_atributo)
```

Esto evita que la misma combinación de ciudad y atributo se registre más de una vez.

### Campos

| Campo | Tipo | Clave | Apunta a |
|---|---|---|---|
| `id_ciudad` | integer | PK, FK | `ciudad.id_ciudad` |
| `id_atributo` | integer | PK, FK | `atributo.id_atributo` |

### Script SQL

```sql
CREATE TABLE IF NOT EXISTS ciudad_atributo (

    id_ciudad INT NOT NULL,
    id_atributo INT NOT NULL,

    CONSTRAINT pk_ciudad_atributo
        PRIMARY KEY (id_ciudad, id_atributo),

    CONSTRAINT fk_ciudad_atributo_ciudad
        FOREIGN KEY (id_ciudad)
        REFERENCES ciudad(id_ciudad)
        ON DELETE CASCADE,

    CONSTRAINT fk_ciudad_atributo_atributo
        FOREIGN KEY (id_atributo)
        REFERENCES atributo(id_atributo)
        ON DELETE CASCADE
);
```
---

## 11. ATRIBUTO

La tabla `atributo` guarda las características o intereses que pueden utilizarse para describir un destino.

Algunos ejemplos son:

- playa
- cultura
- aventura
- gastronomía
- descanso

Actualmente funciona como catálogo para la futura función de recomendación de destinos.

El nombre de cada atributo es único, evitando registrar dos veces la misma característica.

### Campos

| Campo | Tipo | Clave | Descripción |
|---|---|---|---|
| `id_atributo` | integer | PK | Identificador único del atributo. |
| `nombre` | character varying | | Nombre de la característica del destino. |

### Script SQL

```sql
CREATE TABLE IF NOT EXISTS atributo (

    id_atributo SERIAL PRIMARY KEY,

    nombre VARCHAR(50) NOT NULL,

    CONSTRAINT uk_atributo_nombre
        UNIQUE (nombre)
);
```
---
## Resumen de las tablas

| Tabla | Propósito | Relación principal |
|---|---|---|
| `usuario` | Almacena los usuarios del sistema. | Tiene muchos viajes y canales de notificación. |
| `viajes` | Representa cada viaje planificado. | Pertenece a un usuario. |
| `preferencias` | Almacena los intereses seleccionados para un viaje. | Pertenece a un viaje. |
| `itinerario` | Almacena las actividades planificadas. | Pertenece a un viaje. |
| `gastos` | Registra los gastos realizados. | Pertenece a un viaje. |
| `reservas` | Almacena las reservas del viaje. | Pertenece a un viaje. |
| `canales_notificacion` | Define los medios para recibir notificaciones. | Pertenece a un usuario. |
| `alertas` | Registra las notificaciones generadas. | Pertenece a un viaje y a un canal. |
| `ciudad` | Catálogo de destinos disponibles. | Se relaciona con atributos. |
| `atributo` | Catálogo de características de destinos. | Se relaciona con ciudades. |
| `ciudad_atributo` | Relaciona ciudades y atributos. | Tabla intermedia N:M. |

## Relaciones y dependencias

La estructura general de dependencias de la base de datos puede resumirse de la siguiente manera:

```
usuario
├── viajes
│   ├── preferencias
│   ├── itinerario
│   ├── gastos
│   ├── reservas
│   └── alertas
│
└── canales_notificacion
    └── alertas

ciudad
└── ciudad_atributo
    └── atributo

```


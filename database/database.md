## Funcionamiento general

En conjunto, la base de datos de WiseTrip está diseñada alrededor de una idea simple: un usuario se registra, planifica un viaje, y todo lo demás que hace dentro de esa planificación —sus preferencias, su itinerario de actividades, sus gastos reales, sus reservas y las alertas que recibe— queda organizado y conectado a ese viaje en particular, de modo que en cualquier momento se puede reconstruir el panorama completo de un viaje consultando únicamente su identificador; adicionalmente, existe un pequeño grupo de tablas de catálogo (`ciudad`, `atributo`, `ciudad_atributo` y `nivel_costo`) que por ahora funcionan aparte y que en el futuro permitirían que el sistema recomiende destinos automáticamente según los intereses y el nivel de costo que el usuario haya seleccionado.

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

## Nota: `ciudad`, `atributo`, `ciudad_atributo` y `nivel_costo`

Son catálogos pensados para una futura función de recomendación de destinos:

- `ciudad` guarda información de lugares que pueden ser recomendados, incluyendo nombre, país, ubicación geográfica y costo promedio.
- `atributo` guarda características que pueden describir una ciudad, como playa, cultura, aventura o gastronomía.
- `ciudad_atributo` establece la relación entre ciudades y atributos.
- `nivel_costo` almacena categorías generales de costo, como económico, medio o alto.

Actualmente estas tablas no participan directamente en el flujo principal de planificación de viajes.

En particular, `nivel_costo` se encuentra aislada porque ninguna de las tablas actuales la referencia mediante una clave foránea.

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
| `costo_promedio` | numeric | | Costo promedio estimado de la ciudad. |

### SQL

```sql
CREATE TABLE ciudad (
    id_ciudad       SERIAL PRIMARY KEY,
    nombre          VARCHAR(100) NOT NULL,
    pais            VARCHAR(100) NOT NULL,
    latitud         DECIMAL(9,6) NOT NULL,
    longitud        DECIMAL(9,6) NOT NULL,
    costo_promedio  DECIMAL(12,2) NOT NULL,
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

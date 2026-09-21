## Funcionamiento general

En conjunto, la base de datos de WiseTrip está diseñada alrededor de una idea simple: un usuario se registra, planifica un viaje, y todo lo demás que hace dentro de esa planificación —sus preferencias, su itinerario de actividades, sus gastos reales, sus reservas y las alertas que recibe— queda organizado y conectado a ese viaje en particular, de modo que en cualquier momento se puede reconstruir el panorama completo de un viaje consultando únicamente su identificador; adicionalmente, existe un pequeño grupo de tablas de catálogo (`ciudad`, `atributo`, `ciudad_atributo` y `nivel_costo`) que por ahora funcionan aparte y que en el futuro permitirían que el sistema recomiende destinos automáticamente según los intereses y el nivel de costo que el usuario haya seleccionado.




-- NOTA: ciudad, atributo, ciudad_atributo
Son catálogos pensados para una futura función de recomendación de destinos: ciudad guarda información de lugares (ubicación, costo promedio), atributo guarda características (playa, cultura, aventura, etc.), y ciudad_atributo los conecta entre sí. Actualmente estas tablas existen de forma independiente y no están conectadas con viajes ni preferencias, por lo que todavía no participan en el funcionamiento activo de la aplicación.



## 1 USUARIO
La tabla usuario guarda quien usa la aplicacion: su nombre, correo, contraseña y tipo de cuenta si es cliente o administrador. Tambien guarda su documento de identidad y fecha de nacimiento, para verificar que es una persona real y unica en el sistema, no pueden haber dos personas con el mismo correo ni numero de documento.


    CREATE TABLE usuario (

    id_usuario      SERIAL PRIMARY KEY,
    nombre         VARCHAR(100) NOT NULL,
    correo         VARCHAR(150) NOT NULL,
    contraseña     VARCHAR(255) NOT NULL,
    rol            VARCHAR(20) NOT NULL,
    fecha_registro TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    estado         BOOLEAN NOT NULL DEFAULT TRUE,

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


## 2 CIUDAD
Guarda informacion de lugares que el usuario podria visitar

    CREATE TABLE ciudad (

    id_ciudad       SERIAL PRIMARY KEY,
    nombre          VARCHAR(100) NOT NULL,
    pais            VARCHAR(100) NOT NULL,
    latitud         DECIMAL(9,6) NOT NULL,
    longitud        DECIMAL(9,6) NOT NULL,
    costo_promedio  DECIMAL(12,2) NOT NULL,

    CONSTRAINT chk_ciudad_costo
        CHECK (costo_promedio > 0),

    CONSTRAINT uk_ciudad_nombre_pais
        UNIQUE (nombre, pais)
    );



## 3 VIAJES
Es una de las tablas mas importantes por que casi todas las demas dependen de ella. Cada fila representa un viaje planeado por un usuario: a dónde quiere ir (destino), cuándo empieza y termina, y cuánto presupuesto tiene disponible. Cada viaje está obligatoriamente ligado a un usuario (mediante id_usuario), así que nunca existe un viaje sin dueño. Si se elimina el usuario, todos sus viajes se eliminan automáticamente junto con él.

    CREATE TABLE viajes (

    id_viaje        SERIAL PRIMARY KEY,
    id_usuario      INT NOT NULL,
    destino VARCHAR(150) NOT NULL,
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

## 4 PREFERENCIAS
Guarda los intereses que el usuario definió para un viaje específico, por ejemplo, aventura, cultura o gastronomía. No está ligada al usuario directamente, sino al viaje, porque una misma persona puede querer cosas distintas dependiendo del viaje que esté planeando. No se permite repetir la misma preferencia dos veces dentro de un mismo viaje.

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

## 5 ITINERARIO
Organiza las actividades día por día dentro de un viaje: qué se va a hacer, en qué fecha, a qué hora y cuánto cuesta aproximadamente. Cada actividad pertenece a un único viaje.

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

## 6 GASTOS
Registra el dinero que realmente se ha gastado durante el viaje, a diferencia del presupuesto (que es solo una estimación inicial). Cada gasto tiene una descripción, un monto, una fecha y una categoría, y sirve para que el usuario pueda comparar cuánto había planeado gastar contra lo que efectivamente gastó.

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

## 7 RESERVAS
Centraliza las reservas hechas para un viaje —hospedaje, transporte, actividades, etc. Junto con su estado actual pendiente, confirmada o cancelada. Permite tener en un solo lugar todo lo que el usuario ya aseguró para su viaje.

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


## 8 CANALES DE NOTIFICACIÓN
Define por qué medio quiere un usuario recibir sus avisos: correo electrónico o Telegram. Guarda el dato de contacto necesario para ese canal el correo o el usuario de Telegram.

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

## 9 ALERTAS
Almacena las notificaciones que el sistema genera automáticamente, como avisos de clima, alertas de presupuesto o recomendaciones. Cada alerta sabe a qué viaje pertenece y por qué canal fue enviada, lo que permite rastrear qué se comunicó y cómo llegó al usuario.

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

## 10. CIUDAD ATRIBUTO 
Conecta una ciudad y sus atributos entre si. 

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

## 11. ATRIBUTO 
Guarda caracteristicas (playa, cultura, aventura, etc)

    CREATE TABLE IF NOT EXISTS atributo (
    id_atributo SERIAL PRIMARY KEY,

    nombre VARCHAR(50) NOT NULL,

    CONSTRAINT uk_atributo_nombre
        UNIQUE (nombre)
    );


## 12. NIVEL COSTO
Es un catálogo simple que clasifica los niveles de gasto que puede tener un destino — por ejemplo, económico, medio o alto. Cada fila representa una categoría de costo con su nombre, y el nombre no se puede repetir. Al igual que ciudad, atributo y ciudad_atributo, está pensada para la futura función de recomendación de destinos (permitiría filtrar ciudades según qué tan costoso es viajar ahí), pero actualmente no está conectada con ninguna otra tabla del modelo — ni ciudad la referencia, así que por ahora existe de forma aislada, sin participar en el funcionamiento activo de la aplicación.

    CREATE TABLE IF NOT EXISTS nivel_costo (
    id_nivel SERIAL PRIMARY KEY,
    nombre VARCHAR(20) NOT NULL,

    CONSTRAINT uk_nivel_costo_nombre
        UNIQUE (nombre)
    );

--1 USUARIO

CREATE TABLE usuario (

    id_usuario      SERIAL PRIMARY KEY,
    nombre         VARCHAR(100) NOT NULL,
    correo         VARCHAR(150) NOT NULL,
    contraseña     VARCHAR(255) NOT NULL,
    rol            VARCHAR(20) NOT NULL,
    fecha_registro TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    estado         BOOLEAN NOT NULL DEFAULT TRUE,

    CONSTRAINT uk_usuario_correo
        UNIQUE (correo),

    CONSTRAINT chk_usuario_rol
        CHECK (rol IN ('cliente', 'administrador'))
);
-- 2 CIUDAD

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



-- 3 VIAJES
CREATE TABLE viajes (

    id_viaje        SERIAL PRIMARY KEY,
    id_usuario      INT NOT NULL,
    id_ciudad       INT NOT NULL,
    fecha_inicio    DATE NOT NULL,
    fecha_fin       DATE NOT NULL,
    presupuesto     DECIMAL(12,2) NOT NULL,
    fecha_creacion  TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_viajes_usuario
        FOREIGN KEY (id_usuario)
        REFERENCES usuario(id_usuario)
        ON DELETE CASCADE,

    CONSTRAINT fk_viajes_ciudad
        FOREIGN KEY (id_ciudad)
        REFERENCES ciudad(id_ciudad)
        ON DELETE RESTRICT,

    CONSTRAINT chk_viajes_fechas
        CHECK (fecha_fin >= fecha_inicio),

    CONSTRAINT chk_viajes_presupuesto
        CHECK (presupuesto > 0)
);

-- 4 PREFERENCIAS

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

-- 5 ITINERARIO
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

-- 6 GASTOS


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

-- 7 RESERVAS
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


-- 8 CANALES DE NOTIFICACIÓN
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

-- 9 ALERTAS
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
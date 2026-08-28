CREATE TABLE usuario (
    id_usuario INT,
    nombre VARCHAR(100) NOT NULL,
    correo VARCHAR(150) NOT NULL,
    contraseña VARCHAR(255) NOT NULL,
    rol VARCHAR(20) NOT NULL,
    fecha_registro TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    estado BOOLEAN NOT NULL,

    CONSTRAINT pk_usuario PRIMARY KEY (id_usuario),
    CONSTRAINT uk_usuario_correo UNIQUE (correo),

);

CREATE TABLE viajes (
    id_viaje INT,
    id_usuario INT NOT NULL,
    destino VARCHAR(150) NOT NULL,
    fecha_inicio DATE NOT NULL,
    fecha_fin DATE NOT NULL,
    presupuesto DECIMAL(12,2) NOT NULL,
    fecha_creacion TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT pk_viajes PRIMARY KEY (id_viaje),

    CONSTRAINT fk_viajes_usuario
        FOREIGN KEY (id_usuario)
        REFERENCES usuario(id_usuario)
        ON DELETE CASCADE,

);

CREATE TABLE preferencias (
    id_preferencia INT,
    id_viaje INT NOT NULL,
    tipo_preferencia VARCHAR(50) NOT NULL,

    CONSTRAINT pk_preferencias PRIMARY KEY (id_preferencia),

    CONSTRAINT fk_preferencia_viaje
        FOREIGN KEY (id_viaje)
        REFERENCES viajes(id_viaje)
        ON DELETE CASCADE,

    CONSTRAINT uk_preferencias_viaje_tipo
        UNIQUE (id_viaje, tipo_preferencia)
);

CREATE TABLE itinerario (
    id_itinerario INT,
    id_viaje INT NOT NULL,
    nombre VARCHAR(150) NOT NULL,
    fecha_actividad DATE NOT NULL,
    hora_actividad VARCHAR(5) NOT NULL,
    tipo VARCHAR(50) NOT NULL,
    costo_estimado DECIMAL(12,2) NOT NULL,

    CONSTRAINT pk_itinerario PRIMARY KEY (id_itinerario),

    CONSTRAINT fk_itinerario_viaje
        FOREIGN KEY (id_viaje)
        REFERENCES viajes(id_viaje)
        ON DELETE CASCADE,


);

CREATE TABLE gastos (
    id_gasto INT,
    id_viaje INT NOT NULL,
    descripcion VARCHAR(200) NOT NULL,
    monto DECIMAL(12,2) NOT NULL,
    fecha_gasto DATE NOT NULL,
    categoria VARCHAR(50) NOT NULL,

    CONSTRAINT pk_gastos PRIMARY KEY (id_gasto),

    CONSTRAINT fk_gastos_viaje
        FOREIGN KEY (id_viaje)
        REFERENCES viajes(id_viaje)
        ON DELETE CASCADE,


);

CREATE TABLE reservas (
    id_reserva INT,
    id_viaje INT NOT NULL,
    tipo VARCHAR(30) NOT NULL,
    descripcion VARCHAR(250) NOT NULL,
    fecha_reserva DATE NOT NULL,
    estado VARCHAR(20) NOT NULL,

    CONSTRAINT pk_reservas PRIMARY KEY (id_reserva),

    CONSTRAINT fk_reservas_viaje
        FOREIGN KEY (id_viaje)
        REFERENCES viajes(id_viaje)
        ON DELETE CASCADE,

);

CREATE TABLE canales_notificacion (
    id_canal INT,
    id_usuario INT NOT NULL,
    tipo_canal VARCHAR(20) NOT NULL,
    identificador VARCHAR(150) NOT NULL,
    activo BOOLEAN NOT NULL,

    CONSTRAINT pk_canales_notificacion PRIMARY KEY (id_canal),

    CONSTRAINT fk_canal_usuario
        FOREIGN KEY (id_usuario)
        REFERENCES usuario(id_usuario)
        ON DELETE CASCADE,

);

CREATE TABLE alertas (
    id_alerta INT,
    id_viaje INT NOT NULL,
    id_canal INT NOT NULL,
    tipo_alerta VARCHAR(30) NOT NULL,
    mensaje VARCHAR(500) NOT NULL,
    fecha_envio TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    estado VARCHAR(20) NOT NULL,

    CONSTRAINT pk_alertas PRIMARY KEY (id_alerta),

    CONSTRAINT fk_alerta_viaje
        FOREIGN KEY (id_viaje)
        REFERENCES viajes(id_viaje)
        ON DELETE CASCADE,

    CONSTRAINT fk_alerta_canal
        FOREIGN KEY (id_canal)
        REFERENCES canales_notificacion(id_canal)
        ON DELETE RESTRICT,

);
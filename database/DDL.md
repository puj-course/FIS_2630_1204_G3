# Script de Creación de la Base de Datos

```sql

-- 1. USUARIO
CREATE TABLE IF NOT EXISTS usuario (
    id_usuario SERIAL PRIMARY KEY,

    nombre VARCHAR(100) NOT NULL,
    correo VARCHAR(150) NOT NULL,
    contraseña VARCHAR(255) NOT NULL,
    rol VARCHAR(20) NOT NULL,

    fecha_registro TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    estado BOOLEAN NOT NULL DEFAULT TRUE,

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


--dice cuanto vale una ciudad 
-- 2. NIVEL COSTO
CREATE TABLE IF NOT EXISTS nivel_costo (
    id_nivel SERIAL PRIMARY KEY,
    nombre VARCHAR(20) NOT NULL,

    CONSTRAINT uk_nivel_costo_nombre
        UNIQUE (nombre)
);



-- 3. CIUDAD
CREATE TABLE IF NOT EXISTS ciudad (
    id_ciudad SERIAL PRIMARY KEY,

    nombre VARCHAR(100) NOT NULL,
    pais VARCHAR(100) NOT NULL,

    latitud NUMERIC(9,6) NOT NULL,
    longitud NUMERIC(9,6) NOT NULL,

    costo_promedio NUMERIC(12,2) NOT NULL,

    CONSTRAINT uk_ciudad_nombre_pais
        UNIQUE (nombre, pais),

    CONSTRAINT chk_ciudad_costo
        CHECK (costo_promedio > 0)
);



-- 4. ATRIBUTO
CREATE TABLE IF NOT EXISTS atributo (
    id_atributo SERIAL PRIMARY KEY,

    nombre VARCHAR(50) NOT NULL,

    CONSTRAINT uk_atributo_nombre
        UNIQUE (nombre)
);




-- 5. CIUDAD ATRIBUTO
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



-- 6. VIAJES
CREATE TABLE IF NOT EXISTS viajes (
    id_viaje SERIAL PRIMARY KEY,

    id_usuario INT NOT NULL,

    destino VARCHAR(150) NOT NULL,

    fecha_inicio DATE NOT NULL,
    fecha_fin DATE NOT NULL,

    presupuesto NUMERIC(12,2) NOT NULL,

    fecha_creacion TIMESTAMP NOT NULL
        DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_viajes_usuario
        FOREIGN KEY (id_usuario)
        REFERENCES usuario(id_usuario)
        ON DELETE CASCADE,

    CONSTRAINT chk_viajes_fechas
        CHECK (fecha_fin >= fecha_inicio),

    CONSTRAINT chk_viajes_presupuesto
        CHECK (presupuesto > 0)
);



-- 7. PREFERENCIAS
CREATE TABLE IF NOT EXISTS preferencias (
    id_preferencia SERIAL PRIMARY KEY,

    id_viaje INT NOT NULL,

    tipo_preferencia VARCHAR(50) NOT NULL,

    CONSTRAINT fk_preferencia_viaje
        FOREIGN KEY (id_viaje)
        REFERENCES viajes(id_viaje)
        ON DELETE CASCADE,

    CONSTRAINT uk_preferencias_viaje_tipo
        UNIQUE (id_viaje, tipo_preferencia)
);



-- 8. ITINERARIO
CREATE TABLE IF NOT EXISTS itinerario (
    id_itinerario SERIAL PRIMARY KEY,

    id_viaje INT NOT NULL,

    nombre VARCHAR(150) NOT NULL,

    fecha_actividad DATE NOT NULL,

    hora_actividad TIME NOT NULL,

    tipo VARCHAR(50) NOT NULL,

    costo_estimado NUMERIC(12,2) NOT NULL,

    CONSTRAINT fk_itinerario_viaje
        FOREIGN KEY (id_viaje)
        REFERENCES viajes(id_viaje)
        ON DELETE CASCADE,

    CONSTRAINT chk_itinerario_costo
        CHECK (costo_estimado >= 0)
);



-- 9. GASTOS
CREATE TABLE IF NOT EXISTS gastos (
    id_gasto SERIAL PRIMARY KEY,

    id_viaje INT NOT NULL,

    descripcion VARCHAR(200) NOT NULL,

    monto NUMERIC(12,2) NOT NULL,

    fecha_gasto DATE NOT NULL,

    categoria VARCHAR(50) NOT NULL,

    CONSTRAINT fk_gastos_viaje
        FOREIGN KEY (id_viaje)
        REFERENCES viajes(id_viaje)
        ON DELETE CASCADE,

    CONSTRAINT chk_gastos_monto
        CHECK (monto > 0)
);



-- 10. RESERVAS
CREATE TABLE IF NOT EXISTS reservas (
    id_reserva SERIAL PRIMARY KEY,

    id_viaje INT NOT NULL,

    tipo VARCHAR(30) NOT NULL,

    descripcion VARCHAR(250) NOT NULL,

    fecha_reserva DATE NOT NULL,

    estado VARCHAR(20) NOT NULL,

    CONSTRAINT fk_reservas_viaje
        FOREIGN KEY (id_viaje)
        REFERENCES viajes(id_viaje)
        ON DELETE CASCADE,

    CONSTRAINT chk_reservas_estado
        CHECK (
            estado IN (
                'confirmada',
                'pendiente',
                'cancelada'
            )
        )
);


-- 11. CANALES DE NOTIFICACION
CREATE TABLE IF NOT EXISTS canales_notificacion (
    id_canal SERIAL PRIMARY KEY,

    id_usuario INT NOT NULL,

    tipo_canal VARCHAR(20) NOT NULL,

    identificador VARCHAR(150) NOT NULL,

    activo BOOLEAN NOT NULL,

    CONSTRAINT fk_canal_usuario
        FOREIGN KEY (id_usuario)
        REFERENCES usuario(id_usuario)
        ON DELETE CASCADE,

    CONSTRAINT chk_canal_tipo
        CHECK (
            tipo_canal IN (
                'correo',
                'telegram'
            )
        )
);




-- 12. ALERTAS
CREATE TABLE IF NOT EXISTS alertas (
    id_alerta SERIAL PRIMARY KEY,

    id_viaje INT NOT NULL,
    id_canal INT NOT NULL,

    tipo_alerta VARCHAR(30) NOT NULL,

    mensaje VARCHAR(500) NOT NULL,

    fecha_envio TIMESTAMP NOT NULL
        DEFAULT CURRENT_TIMESTAMP,

    estado VARCHAR(20) NOT NULL,

    CONSTRAINT fk_alerta_viaje
        FOREIGN KEY (id_viaje)
        REFERENCES viajes(id_viaje)
        ON DELETE CASCADE,

    CONSTRAINT fk_alerta_canal
        FOREIGN KEY (id_canal)
        REFERENCES canales_notificacion(id_canal)
        ON DELETE RESTRICT,

    CONSTRAINT chk_alerta_tipo
        CHECK (
            tipo_alerta IN (
                'clima',
                'presupuesto',
                'recomendacion'
            )
        ),

    CONSTRAINT chk_alerta_estado
        CHECK (
            estado IN (
                'pendiente',
                'enviada'
            )
        )
);




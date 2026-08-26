CREATE TABLE usuario (
	id_usuario INT, 
	nombre VARCHAR(100) NOT NULL
	correo VARCHAR(150) NOT NULL, 
	contraseña VARCHAR (255) NOT NULL, 
	rol VARCHAR(20) NOT NULL, 
	fecha_registro TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
estado BOOLEAN NOT NULL, 

CONSTRAINT pk_usuario PRIMARY KEY (id_usuario),
CONSTRAINT uk_usuario_correo UNIQUE (correo)
);


CREATE TABLE viajes(
	id_viaje INT,
	id_usuario INT NOT NULL, 
	destino VARCHAR((150) NOT NULL, 
	fecha_inicio DATE NOT NULL, 
	fecha_fin DATE NOT NULL, 
	presupuesto DOUBLE NOT NULL, 
	fecha_creacion TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

	CONSTRAINT pk_viajes PRIMARY KEY (id_viaje),
	CONSTRAINT fk_viajes FOREING KEY (id_usuario)
);


CREATE TABLE preferencias (
	id_preferencia INT, 
	id_viaje INT NOT NULL, 
	tipo_preferencia VARCHAR(50) NOT NULL, 

	CONSTRAINT pk_preferencias PRIMARY KEY (id_preferencia), 
	CONSTRAINT fk_preferencia_viaje FOREING KEY (id_viaje)

	references viajes (id_viaje) ON DELETE CASCADE, 
	CONSTRAINT uk_preferencias_viaje_tipo UNIQUE (id_viaje, tipo_preferencia)
);

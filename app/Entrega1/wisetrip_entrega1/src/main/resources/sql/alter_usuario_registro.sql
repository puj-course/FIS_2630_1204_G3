ALTER TABLE usuario
ADD COLUMN IF NOT EXISTS tipo_documento VARCHAR(30),
ADD COLUMN IF NOT EXISTS numero_documento VARCHAR(30),
ADD COLUMN IF NOT EXISTS fecha_nacimiento DATE;

CREATE UNIQUE INDEX IF NOT EXISTS ux_usuario_numero_documento
ON usuario (numero_documento);

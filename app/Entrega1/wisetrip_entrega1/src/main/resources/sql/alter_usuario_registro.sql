ALTER TABLE usuario
ADD COLUMN IF NOT EXISTS tipo_documento VARCHAR(30),
ADD COLUMN IF NOT EXISTS numero_documento VARCHAR(30),
ADD COLUMN IF NOT EXISTS fecha_nacimiento DATE,
ADD COLUMN IF NOT EXISTS chat_id VARCHAR(100);

CREATE UNIQUE INDEX IF NOT EXISTS ux_usuario_numero_documento
ON usuario (numero_documento);

CREATE UNIQUE INDEX IF NOT EXISTS ux_usuario_chat_id
ON usuario (chat_id)
WHERE chat_id IS NOT NULL;

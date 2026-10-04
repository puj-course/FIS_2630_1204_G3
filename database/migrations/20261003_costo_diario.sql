-- Ejecutar manualmente antes del nuevo insert_ciudades.sql.
-- Primero cargar/revisar TODOS los costos existentes: los antiguos 500/800/1300
-- eran valores ficticios por categoria, NO costos diarios. Para una ciudad
-- sin costo investigado, establecer costo_promedio = NULL por su id_ciudad.
-- No se convierten automaticamente valores existentes ni se inventan precios.
BEGIN;
ALTER TABLE ciudad ALTER COLUMN costo_promedio DROP NOT NULL;
COMMENT ON COLUMN ciudad.costo_promedio IS
    'USD por persona por dia: alojamiento, comida y transporte local. NULL = pendiente. Excluye traslado hasta el destino.';
DROP TABLE IF EXISTS nivel_costo;
COMMIT;

-- Ejemplo comunicado por el usuario (revisar nombre antes de ejecutar):
-- UPDATE ciudad SET costo_promedio = 77.00
-- WHERE nombre = 'Bogotá' AND pais = 'Colombia';

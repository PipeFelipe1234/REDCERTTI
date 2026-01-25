-- Migración 2: Agregar campos GPS y picture
-- Version: 2
-- Descripción: Agrega campos de GPS para checkin/checkout y campo picture
-- Esta migración es idempotente - puede ejecutarse múltiples veces sin errores

-- Agregar columnas solo si no existen
-- Nota: MySQL no soporta ADD COLUMN IF NOT EXISTS en versiones antiguas,
-- por lo que usamos IGNORE para evitar errores si las columnas ya existen

ALTER TABLE registros ADD COLUMN latitude_checkin DOUBLE DEFAULT NULL;
ALTER TABLE registros ADD COLUMN longitude_checkin DOUBLE DEFAULT NULL;
ALTER TABLE registros ADD COLUMN precision_metros_checkin DOUBLE DEFAULT NULL;

ALTER TABLE registros ADD COLUMN latitude_checkout DOUBLE DEFAULT NULL;
ALTER TABLE registros ADD COLUMN longitude_checkout DOUBLE DEFAULT NULL;
ALTER TABLE registros ADD COLUMN precision_metros_checkout DOUBLE DEFAULT NULL;

ALTER TABLE registros ADD COLUMN picture VARCHAR(500) DEFAULT NULL;


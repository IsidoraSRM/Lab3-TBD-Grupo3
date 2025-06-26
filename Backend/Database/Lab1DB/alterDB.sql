-- Activar extensión PostGIS
CREATE EXTENSION IF NOT EXISTS postgis;

-- Agregar campos de ubicación geográfica
ALTER TABLE cliente ADD COLUMN ubicacion GEOMETRY(Point, 4326);
ALTER TABLE repartidor ADD COLUMN ubicacion_actual GEOMETRY(Point, 4326);
ALTER TABLE EmpresaAsociada ADD COLUMN ubicacion GEOMETRY(Point, 4326);
ALTER TABLE OrderEntity ADD COLUMN ruta_estimada GEOMETRY(LineString, 4326);
ALTER TABLE usuarios  ADD COLUMN ubicacion geometry(Point,4326);
ALTER TABLE detallepedido  ADD COLUMN ubicacionInicio geometry(Point,4326);
ALTER TABLE detallepedido  ADD COLUMN ubicacionDestino geometry(Point,4326);



-- Crear tabla de zonas de cobertura
CREATE TABLE zonas_cobertura (
    zona_id SERIAL PRIMARY KEY,
    nombre VARCHAR(100),
    geom GEOMETRY(MultiPolygon, 4326) -- Polygon
);

-- Insertar ubicaciones simuladas (coordenadas reales aproximadas)
UPDATE cliente SET ubicacion = ST_SetSRID(ST_MakePoint(-70.6483, -33.4569), 4326) WHERE cliente_id = 1;
UPDATE cliente SET ubicacion = ST_SetSRID(ST_MakePoint(-70.6500, -33.4400), 4326) WHERE cliente_id = 2;

UPDATE repartidor SET ubicacion_actual = ST_SetSRID(ST_MakePoint(-70.6470, -33.4450), 4326) WHERE repartidor_id = 1;
UPDATE repartidor SET ubicacion_actual = ST_SetSRID(ST_MakePoint(-70.6600, -33.4500), 4326) WHERE repartidor_id = 2;

UPDATE EmpresaAsociada SET ubicacion = ST_SetSRID(ST_MakePoint(-70.6450, -33.4489), 4326) WHERE idEmpresaAsociada = 1;
UPDATE EmpresaAsociada SET ubicacion = ST_SetSRID(ST_MakePoint(-70.6400, -33.4420), 4326) WHERE idEmpresaAsociada = 2;

-- Insertar ruta estimada para pedido
UPDATE OrderEntity SET ruta_estimada = ST_MakeLine(
    ARRAY[
        ST_SetSRID(ST_MakePoint(-70.6450, -33.4489), 4326), -- empresa
        ST_SetSRID(ST_MakePoint(-70.6483, -33.4569), 4326)  -- cliente
    ]
) WHERE idPedido = 1;

-- Insertar zona de cobertura de ejemplo (polígono simple)
INSERT INTO zonas_cobertura (nombre, geom) VALUES (
    'Zona Central',
    ST_SetSRID(ST_GeomFromText('POLYGON((
        -70.6600 -33.4400,
        -70.6600 -33.4600,
        -70.6400 -33.4600,
        -70.6400 -33.4400,
        -70.6600 -33.4400
    ))'), 4326)
);
--EXTRAS: Implementar una función que calcule automáticamente la zona a la que pertenece un cliente.
CREATE OR REPLACE FUNCTION calcular_zona_cliente(p_cliente_id INTEGER)
RETURNS VARCHAR AS $$
DECLARE
    v_cliente_geom GEOMETRY;
    v_zona_nombre VARCHAR;
BEGIN
    -- Obter la ubicación del cliente
    SELECT ubicacion INTO v_cliente_geom
    FROM cliente
    WHERE cliente_id = p_cliente_id;
    
    -- Si no existe ubicación, retorna NULL.
    IF v_cliente_geom IS NULL THEN
        RETURN NULL;
    END IF;
    
    -- Busca la zona que contenga al cliente.
    SELECT nombre INTO v_zona_nombre
    FROM zonas_cobertura
    WHERE ST_Within(v_cliente_geom, geom)
    LIMIT 1;
    
    RETURN v_zona_nombre ;
EXCEPTION
    WHEN NO_DATA_FOUND THEN
        RETURN NULL;
END;
$$ LANGUAGE plpgsql;



-- SELECT calcular_zona_cliente(7);


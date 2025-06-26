-- Consulta 1 para obtener los 5 puntos de entrega más cercanos a la empresa "Express Chile"
SELECT 
    dp.idDetallePedido,
    ea.nombreEmpresa,
    dp.direccionDestino,
    ROUND(ST_Distance(ea.ubicacion::geography, dp.ubicacionDestino::geography)) AS distancia_metros,
    ST_MakeLine(ea.ubicacion, dp.ubicacionDestino) AS ruta_estimada
FROM 
    detallepedido dp
JOIN 
    servicios s ON dp.idServicio = s.idServicio
JOIN 
    empresaasociada ea ON s.idEmpresaAsociada = ea.idEmpresaAsociada
WHERE 
    ea.nombreEmpresa ILIKE '%Express Chile%'  
ORDER BY 
    distancia_metros
LIMIT 5;



-- Consulta 2 para verificar si un cliente está dentro de una zona de cobertura usando ST_Buffer
SELECT 
    c.cliente_id,
    c.nombre,
    z.zona_id,
    z.nombre AS nombre_zona
FROM 
    cliente c
JOIN 
    zonas_cobertura z
ON 
    ST_Intersects(
        ST_Buffer(c.ubicacion::geography, 1000)::geometry,
        z.geom
    )
WHERE 
    c.cliente_id = ?;




-- consulta numero 3
SELECT
    r.repartidor_id,
    r.nombre,
    SUM(ST_Distance(dp.ubicacionInicio::geography, dp.ubicacionDestino::geography)) AS distancia_total_metros
FROM
    repartidor r
        JOIN
    orderentity o ON o.repartidor_id = r.repartidor_id
        JOIN
    detallepedido dp ON dp.idpedido = o.idpedido
WHERE
    o.fechaentrega >= NOW() - INTERVAL '1 month'
  AND dp.ubicacionInicio IS NOT NULL
  AND dp.ubicacionDestino IS NOT NULL
GROUP BY
    r.repartidor_id, r.nombre;


-- consulta numero 4
SELECT
    e.nombreEmpresa,
    d.direccionDestino,
    ST_Distance(
            ST_Transform(e.ubicacion, 3857),
            ST_Transform(d.ubicacionDestino, 3857)
    ) / 1000 AS distancia_km
FROM DetallePedido d
         JOIN OrderEntity o ON d.idPedido = o.idPedido
         JOIN EmpresaAsociada e ON o.idEmpresaAsociada = e.idEmpresaAsociada
WHERE ST_Distance(
              ST_Transform(e.ubicacion, 3857),
              ST_Transform(d.ubicacionDestino, 3857)
      ) = (
          SELECT MAX(ST_Distance(
                  ST_Transform(e2.ubicacion, 3857),
                  ST_Transform(d2.ubicacionDestino, 3857)
                     ))
          FROM DetallePedido d2
                   JOIN OrderEntity o2 ON d2.idPedido = o2.idPedido
                   JOIN EmpresaAsociada e2 ON o2.idEmpresaAsociada = e2.idEmpresaAsociada
          WHERE e2.idEmpresaAsociada = e.idEmpresaAsociada
      )
ORDER BY distancia_km DESC;


-- consulta 5
SELECT
    o.idPedido,
    e.nombreEmpresa,
    COUNT(z.zona_id) AS zonas_cruzadas
FROM OrderEntity o
    JOIN EmpresaAsociada e ON o.idEmpresaAsociada = e.idEmpresaAsociada
    JOIN zonas_cobertura z ON ST_Intersects(o.ruta_estimada, z.geom)
GROUP BY o.idPedido, e.nombreEmpresa
HAVING COUNT(z.zona_id) > 2;


-- consulta 6
SELECT
    c.cliente_id,
    c.nombre,
    MIN(ST_Distance(
        ST_Transform(c.ubicacion, 3857),
        ST_Transform(e.ubicacion, 3857)
            )) AS distancia_minima
FROM cliente c
    JOIN EmpresaAsociada e ON TRUE
GROUP BY c.cliente_id, c.nombre
HAVING MIN(ST_Distance(ST_Transform(c.ubicacion, 3857), ST_Transform(e.ubicacion, 3857))) > 5000;

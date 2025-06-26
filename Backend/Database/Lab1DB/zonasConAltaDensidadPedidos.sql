-- 1) Genera clusters DBSCAN y sus estadísticas
WITH order_points AS (
  SELECT c.ubicacion AS punto
  FROM cliente c
  JOIN OrderEntity o ON c.cliente_id = o.cliente_id
),
clusters AS (
  SELECT
    ST_ClusterDBSCAN(punto, 0.005, 2) OVER () AS cluster_id,
    punto
  FROM order_points
),
cluster_stats AS (
  SELECT
    cluster_id,
    COUNT(*)                        AS cantidad_pedidos,
    ST_Centroid(ST_Collect(punto))  AS centro_cluster,
    ST_Collect(punto)               AS geom_cluster
  FROM clusters
  WHERE cluster_id >= 0            -- descartar ruido
  GROUP BY cluster_id
)
-- 2) Etiqueta cada cluster con el nombre de la zona de cobertura
SELECT
  cs.cluster_id,
  z.nombre        AS zona_cobertura,
  cs.cantidad_pedidos,
  cs.centro_cluster,
  cs.geom_cluster
FROM cluster_stats cs
LEFT JOIN public.zonas_cobertura z
  ON ST_Within(cs.centro_cluster, z.geom)
ORDER BY cs.cantidad_pedidos DESC;





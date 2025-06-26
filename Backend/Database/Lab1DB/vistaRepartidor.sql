CREATE VIEW VistaDesempenoRepartidores AS
SELECT r.nombre AS repartidor,
       COUNT(o.idPedido) AS total_entregas,
       AVG(EXTRACT(EPOCH FROM (o.fechaEntrega - o.fechaPedido)) / 3600) AS tiempo_promedio_horas,
       COALESCE(AVG(rt.score), 0) AS calificacion_promedio
FROM repartidor r
JOIN OrderEntity o ON r.repartidor_id = o.repartidor_id
LEFT JOIN Rating rt ON o.idPedido = rt.idPedido
WHERE o.estadoPedido = 'ENTREGADO'
GROUP BY r.nombre;
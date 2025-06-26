-- Vista detalle pedido
CREATE VIEW order_summary AS
    SELECT
        c.cliente_id,
        c.nombre AS nombre_cliente,
        c.email AS email_cliente,
        COUNT(o.idPedido) AS total_pedidos,
        COALESCE(SUM(p.monto), 0) AS monto_total_gastado
    FROM
        cliente c
            LEFT JOIN
        OrderEntity o ON c.cliente_id = o.cliente_id
            LEFT JOIN
        Pago p ON o.idPedido = p.idPedido
            LEFT JOIN
        Rating r ON o.idPedido = r.idPedido
    GROUP BY
        c.cliente_id, c.nombre, c.email
    ORDER BY
        monto_total_gastado DESC;
		
-- Vista empresas
CREATE OR REPLACE VIEW VistaEmpresasTopServicios AS
SELECT e.nombreEmpresa AS empresa,
       COUNT(dp.idDetallePedido) AS total_servicios_entregados
FROM EmpresaAsociada e
JOIN Servicios s ON e.idEmpresaAsociada = s.idEmpresaAsociada
JOIN DetallePedido dp ON s.idServicio = dp.idServicio
JOIN OrderEntity o ON dp.idPedido = o.idPedido
WHERE UPPER(o.estadoPedido) = 'ENTREGADO'
GROUP BY e.nombreEmpresa
ORDER BY total_servicios_entregados DESC;

-- Vista repartidor
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
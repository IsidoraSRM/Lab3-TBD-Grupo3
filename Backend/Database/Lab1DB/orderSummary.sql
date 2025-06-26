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
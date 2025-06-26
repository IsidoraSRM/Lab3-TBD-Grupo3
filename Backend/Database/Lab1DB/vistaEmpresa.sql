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
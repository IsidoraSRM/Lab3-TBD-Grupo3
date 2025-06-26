CREATE OR REPLACE PROCEDURE register_order(
    IN p_cliente_id INT,
    IN p_prioridadPedido VARCHAR(50),
    IN p_nombre_metodo VARCHAR(100),
    IN p_monto DECIMAL(10,2),
    IN p_nombre_servicio VARCHAR(255),
    IN p_descripcion TEXT,
    IN p_categoria VARCHAR(100),
    IN p_direccion_inicio VARCHAR(255),
    IN p_direccion_destino VARCHAR(255),
    OUT p_id_pedido INT
)
    LANGUAGE plpgsql
AS $$
DECLARE
    v_repartidor_id INT;
    v_id_servicio INT;
    v_id_metodo_pago INT;
    v_fecha_entrega TIMESTAMP;
    v_tiempo_preparacion INT;
    v_tiempo_envio INT;
    v_id_empresa_docdelivery INT;
BEGIN
    -- 1. Obtener o crear la empresa DocDelivery
    SELECT idEmpresaAsociada INTO v_id_empresa_docdelivery
    FROM EmpresaAsociada
    WHERE nombreEmpresa = 'DocDelivery'
    LIMIT 1;

    IF v_id_empresa_docdelivery IS NULL THEN
        INSERT INTO EmpresaAsociada (nombreEmpresa, tipoServicio)
        VALUES ('DocDelivery', 'Delivery de Documentos')
        RETURNING idEmpresaAsociada INTO v_id_empresa_docdelivery;
    END IF;

    -- 2. Asignar repartidor disponible (el que tenga menos pedidos pendientes)
    SELECT r.repartidor_id INTO v_repartidor_id
    FROM Repartidor r
    ORDER BY (
                 SELECT COUNT(*)
                 FROM OrderEntity o
                 WHERE o.repartidor_id = r.repartidor_id
                   AND o.estadoPedido IN ('PENDIENTE_CON', 'EN_CAMINO')
             ) ASC
    LIMIT 1;

    -- 3. Registrar método de pago si no existe
    SELECT idMetodoDePago INTO v_id_metodo_pago
    FROM MedioDePago
    WHERE nombreMetodoDePago = p_nombre_metodo;

    IF v_id_metodo_pago IS NULL THEN
        INSERT INTO MedioDePago (nombreMetodoDePago)
        VALUES (p_nombre_metodo)
        RETURNING idMetodoDePago INTO v_id_metodo_pago;
    END IF;

    -- 4. Registrar el servicio (con verificación de stock si es necesario)
    INSERT INTO Servicios (
        idEmpresaAsociada,
        nombreServicio,
        descripcionServicio,
        categoriaServicio,
        precioServicio,
        stock
    ) VALUES (
                 v_id_empresa_docdelivery,
                 p_nombre_servicio,
                 p_descripcion,
                 p_categoria,
                 p_monto,
                 1 -- Stock por defecto (ajustable según necesidades)
             )
    RETURNING idServicio INTO v_id_servicio;

    -- 5. Calcular tiempo estimado de entrega basado en prioridad
    CASE p_prioridadPedido
        WHEN 'ALTA' THEN
            v_tiempo_preparacion := 30; -- 30 minutos
            v_tiempo_envio := 20;      -- 20 minutos
        WHEN 'MEDIA' THEN
            v_tiempo_preparacion := 45;
            v_tiempo_envio := 30;
        ELSE -- 'BAJA'
        v_tiempo_preparacion := 60;
        v_tiempo_envio := 45;
        END CASE;

    -- Calcular fecha estimada de entrega
    v_fecha_entrega := NOW() +
                       (v_tiempo_preparacion * INTERVAL '1 minute') +
                       (v_tiempo_envio * INTERVAL '1 minute');

    -- 6. Registrar el pedido principal
    INSERT INTO OrderEntity (
        cliente_id,
        idEmpresaAsociada,
        repartidor_id,
        fechaPedido,
        fechaEntrega,
        estadoPedido,
        prioridadPedido
    ) VALUES (
                 p_cliente_id,
                 v_id_empresa_docdelivery,
                 v_repartidor_id,
                 NOW(),
                 v_fecha_entrega,
                 'PENDIENTE_CON', -- Pendiente de confirmación
                 p_prioridadPedido
             )
    RETURNING idPedido INTO p_id_pedido;

    -- 7. Registrar detalle del pedido con direcciones
    INSERT INTO DetallePedido (
        idPedido,
        idServicio,
        cantidad,
        direccionInicio,
        direccionDestino
    ) VALUES (
                 p_id_pedido,
                 v_id_servicio,
                 1, -- Cantidad por defecto
                 p_direccion_inicio,
                 p_direccion_destino
             );

    -- 8. Registrar el pago
    INSERT INTO Pago (
        idPedido,
        idMedioDePago,
        monto
    ) VALUES (
                 p_id_pedido,
                 v_id_metodo_pago,
                 p_monto
             );

    -- 9. Actualizar disponibilidad del repartidor si fue asignado
    IF v_repartidor_id IS NOT NULL THEN
        UPDATE Repartidor
        SET disponible = FALSE
        WHERE repartidor_id = v_repartidor_id;
    END IF;


END;
$$;
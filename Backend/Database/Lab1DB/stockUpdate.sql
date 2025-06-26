CREATE OR REPLACE PROCEDURE confirmar_pedido(
    IN p_id_pedido INT
)
LANGUAGE plpgsql
AS $$
DECLARE
    v_id_servicio INT;
    v_cantidad INT;
    v_estado_actual VARCHAR(50);
BEGIN
    -- 1. Verificar el estado actual del pedido
    SELECT estadoPedido INTO v_estado_actual
    FROM OrderEntity
    WHERE idPedido = p_id_pedido;

    -- 2. Si el pedido no existe o ya está confirmado, salir
    IF v_estado_actual IS NULL THEN
        RAISE EXCEPTION 'El pedido con ID % no existe', p_id_pedido;
    ELSIF v_estado_actual = 'CONFIRMADO' THEN
        RAISE NOTICE 'El pedido ya está confirmado';
        RETURN;
    END IF;

    -- 3. Obtener el servicio y cantidad del pedido
    SELECT idServicio, cantidad INTO v_id_servicio, v_cantidad
    FROM DetallePedido
    WHERE idPedido = p_id_pedido;

    -- 4. Verificar que existe el servicio
    IF v_id_servicio IS NULL THEN
        RAISE EXCEPTION 'El pedido no tiene un servicio asociado';
    END IF;

    -- 5. Verificar stock disponible
    IF (SELECT stock FROM Servicios WHERE idServicio = v_id_servicio) < v_cantidad THEN
        RAISE EXCEPTION 'Stock insuficiente para el servicio ID %', v_id_servicio;
    END IF;

    -- 6. Actualizar el estado del pedido
    UPDATE OrderEntity
    SET estadoPedido = 'CONFIRMADO'
    WHERE idPedido = p_id_pedido;

    -- 7. Actualizar el stock del servicio
    UPDATE Servicios
    SET stock = stock - v_cantidad
    WHERE idServicio = v_id_servicio;

    -- 8. Registrar la operación (opcional)
    RAISE NOTICE 'Pedido % confirmado. Stock del servicio % actualizado', p_id_pedido, v_id_servicio;
END;
$$;


-- procedimiento almacenado 8 
CREATE OR REPLACE PROCEDURE cambiar_estado_pedido(
    pedido_id INT,
    nuevo_estado VARCHAR
)
LANGUAGE plpgsql
AS $$
BEGIN
      --  validación no cambiar si ya  está en estado final
    IF EXISTS (
        SELECT 1 FROM OrderEntity
        WHERE idPedido = pedido_id
        AND estadoPedido NOT IN ('CANCELADO', 'ENTREGADO')
    ) THEN

        	-- si  el nueva estado es 'Entregado', también actualiza la fecha de entrega
        IF nuevo_estado = 'ENTREGADO' THEN
            UPDATE OrderEntity
            SET estadoPedido = nuevo_estado,
                fechaEntrega = NOW()
            WHERE idPedido = pedido_id;
        ELSE
            -- Solo cambia el estado
            UPDATE OrderEntity
            SET estadoPedido = nuevo_estado
            WHERE idPedido = pedido_id;
        END IF;

    ELSE
        RAISE NOTICE 'El pedido no existe o ya fue Cancelado o Entregado.';
    END IF;
END;
$$;

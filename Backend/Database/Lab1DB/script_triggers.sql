--10
CREATE OR REPLACE FUNCTION set_fecha_entrega()
RETURNS TRIGGER AS $$
BEGIN
    IF NEW.estadoPedido = 'Entregado' AND OLD.estadoPedido IS DISTINCT FROM 'Entregado' THEN
        NEW.fechaEntrega := NOW();
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- 11

CREATE OR REPLACE FUNCTION notificar_problema_critico()
RETURNS TRIGGER AS $$
BEGIN
  IF NEW.estadopedido ILIKE 'FALLIDO'
     OR NEW.estadopedido ILIKE 'CANCELADO'
     OR NEW.estadopedido ILIKE 'ERROR' THEN
     
    RAISE NOTICE 'Problema crítico detectado en el pedido %: Estado = %', NEW.idpedido, NEW.estadopedido;
  END IF;
  
  RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trigger_problema_critico
AFTER INSERT OR UPDATE ON orderentity
FOR EACH ROW
EXECUTE FUNCTION notificar_problema_critico();



-- 12

CREATE OR REPLACE FUNCTION insertar_calificacion_automatica()
RETURNS TRIGGER AS $$
BEGIN
  -- Solo actuar si la fecha de entrega fue hace más de 48 horas
  IF NEW.fechapedido IS NOT NULL AND NEW.fechapedido <= NOW() - INTERVAL '48 hours' THEN

    -- Verificar si ya existe una calificación para este pedido
    IF NOT EXISTS (
      SELECT 1 FROM rating WHERE idpedido = NEW.idpedido
    ) THEN
      INSERT INTO rating (idpedido, puntuacion, comentario)
      VALUES (NEW.idpedido, 1, 'Calificación automática por sistema');
      
      RAISE NOTICE 'Calificación automática insertada para el pedido %', NEW.idpedido;
    END IF;

  END IF;

  RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trigger_calificacion_automatica
AFTER UPDATE ON orderentity
FOR EACH ROW
WHEN (NEW.fechapedido IS NOT NULL)
EXECUTE FUNCTION insertar_calificacion_automatica();

-- INSERTAR DATOS EN LA TABLA "EmpresaAsociada"
INSERT INTO empresaasociada (nombreEmpresa, direccionEmpresa, tipoServicio, ubicacion)
VALUES
    ('Express Chile', 'Av. Central 100, Santiago', 'Logística', ST_SetSRID(ST_MakePoint(-70.6450, -33.4489),4326)),
    ('Transporte Rápido', 'Calle Rápida 200, Santiago', 'Transporte', ST_SetSRID(ST_MakePoint(-70.6420, -33.4440),4326));

-- INSERTAR DATOS EN LA TABLA "OrderEntity"
-- Se crea una ruta estimada entre la ubicación de la empresa y la del cliente
INSERT INTO orderentity (cliente_id, idEmpresaAsociada, repartidor_id, fechaPedido, fechaEntrega, estadoPedido, prioridadPedido, ruta_estimada)
VALUES
    (
        7,                   -- Referencia al cliente Luis Perez
        1,                   -- Referencia a Express Chile
        1,                   -- Referencia al repartidor Marcela Gómez
        '2025-06-14 14:00:00',
        '2025-06-14 15:00:00',
        'En Proceso',
        'Alta',
        ST_MakeLine(
                ARRAY[
                    ST_SetSRID(ST_MakePoint(-70.6450, -33.4489),4326),
                    ST_SetSRID(ST_MakePoint(-70.6500, -33.4560),4326)
                    ]
        )
    ),
    (
        7,                   -- Referencia a Ana Gomez
        2,                   -- Referencia a Transporte Rápido
        1,                   -- Referencia al repartidor Jorge López
        '2025-06-14 14:30:00',
        '2025-06-14 15:30:00',
        'Pendiente',
        'Media',
        ST_MakeLine(
                ARRAY[
                    ST_SetSRID(ST_MakePoint(-70.6420, -33.4440),4326),
                    ST_SetSRID(ST_MakePoint(-70.6480, -33.4600),4326)
                    ]
        )
    );

-- INSERTAR DATOS EN LA TABLA "Servicios"
INSERT INTO servicios (idEmpresaAsociada, nombreServicio, descripcionServicio, precioServicio, categoriaServicio, stock)
VALUES
    (1, 'Entrega Rápida', 'Servicio de entrega en tiempo récord', 5000, 'Express', 10),
    (2, 'Transporte Refrigerado', 'Transporte para productos perecederos', 8000, 'Refrigerado', 5);








-- INSERTAR DATOS EN LA TABLA "DetallePedido"
-- Se asumen direcciones y las ubicaciones corresponden a la ruta de cada pedido
INSERT INTO detallepedido (idPedido, idServicio, cantidad, direccionDestino, direccionInicio, ubicacionInicio, ubicacionDestino)
VALUES
    (
        1,
        1,
        2,
        'Calle Destino 789, Santiago',
        'Av. Central 100, Santiago',
        ST_SetSRID(ST_MakePoint(-70.6450, -33.4489),4326),
        ST_SetSRID(ST_MakePoint(-70.6500, -33.4560),4326)
    ),
    (
        2,
        2,
        1,
        'Calle Destino 123, Santiago',
        'Calle Rápida 200, Santiago',
        ST_SetSRID(ST_MakePoint(-70.6420, -33.4440),4326),
        ST_SetSRID(ST_MakePoint(-70.6480, -33.4600),4326)
    );

-- INSERTAR DATOS EN LA TABLA "MedioDePago"
INSERT INTO mediodepago (nombreMetodoDePago)
VALUES
    ('Tarjeta de Crédito'),
    ('Transferencia Bancaria'),
    ('Efectivo');

-- INSERTAR DATOS EN LA TABLA "Pago"
INSERT INTO pago (idPedido, idMedioDePago, monto)
VALUES
    (1, 1, 5000.00),
    (2, 2, 8000.00);


-- INSERTAR DATOS EN LA TABLA "Rating"
INSERT INTO rating (idPedido, score, comment)
VALUES
    (1, 5, 'Excelente servicio en la entrega'),
    (2, 4, 'Buen servicio, pero podría mejorar en rapidez');

-- INSERTAR DATOS EN LA TABLA "zonas_cobertura"
-- Se define un polígono que delimita una zona en Santiago (Zona Central)
INSERT INTO zonas_cobertura (nombre, geom)
VALUES (
           'Zona Central',
           ST_SetSRID(ST_GeomFromText('POLYGON((-70.6600 -33.4400, -70.6600 -33.4600, -70.6400 -33.4600, -70.6400 -33.4400, -70.6600 -33.4400))'), 4326)
       );


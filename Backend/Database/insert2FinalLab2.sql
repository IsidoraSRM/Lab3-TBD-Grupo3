


-------------------------------
-- Insertar registros en la tabla OrderEntity (incluye ruta_estimada como LineString)
-------------------------------
INSERT INTO orderentity
(cliente_id, idEmpresaAsociada, repartidor_id, fechaPedido, fechaEntrega, estadoPedido, prioridadPedido, ruta_estimada)
VALUES
    -- Registro modificado: la ruta incluye tres puntos para simular atravesar 3 zonas
    (1, 3, 1,
     '2025-06-01 08:00:00', '2025-06-05 13:00:00',
     'ENTREGADO', 'URGENTE',
     ST_SetSRID(ST_MakeLine(ARRAY[
         ST_MakePoint(-70.6400, -33.4400),  -- Punto en zona 1
         ST_MakePoint(-70.6700, -33.4450),  -- Punto intermedio en zona 2
         ST_MakePoint(-70.6450, -33.4450)   -- Punto final en zona 3
         ]), 4326)),

    -- Registro modificado: ruta con tres puntos
    (7, 5, 2,
     '2025-06-03 10:15:00', '2025-06-07 12:00:00',
     'PENDIENTE', 'URGENTE',
     ST_SetSRID(ST_MakeLine(ARRAY[
         ST_MakePoint(-70.6600, -33.4600), -- Inicio en zona 1
         ST_MakePoint(-70.6900, -33.4650), -- Intermedio en zona 2
         ST_MakePoint(-70.6650, -33.4650)  -- Final en zona 3
         ]), 4326)),

    -- Registro modificado: la ruta incluye tres puntos
    (3, 1, 5,
     '2025-06-04 11:45:00', '2025-06-08 16:30:00',
     'ENTREGADO', 'URGENTE',
     ST_SetSRID(ST_MakeLine(ARRAY[
         ST_MakePoint(-70.6700, -33.4700), -- Inicio en zona 1
         ST_MakePoint(-70.7000, -33.4750), -- Intermedio en zona 2
         ST_MakePoint(-70.6750, -33.4750)  -- Final en zona 3
         ]), 4326)),

    -- Registros con rutas de dos puntos:
    (10, 4, 4,
     '2025-06-05 14:00:00', '2025-06-09 09:00:00',
     'PENDIENTE', 'URGENTE',
     ST_SetSRID(ST_MakeLine(
                        ST_MakePoint(-70.6800, -33.4800),
                        ST_MakePoint(-70.6850, -33.4850)
                ), 4326)),

    (2, 4, 6,
     '2025-06-06 08:20:00', '2025-06-10 14:15:00',
     'ENTREGADO', 'ALTA',
     ST_SetSRID(ST_MakeLine(
                        ST_MakePoint(-70.6900, -33.4900),
                        ST_MakePoint(-70.6950, -33.4950)
                ), 4326)),

    (8, 6, 7,
     '2025-06-07 10:00:00', '2025-06-11 11:00:00',
     'PENDIENTE', 'ALTA',
     ST_SetSRID(ST_MakeLine(
                        ST_MakePoint(-70.7000, -33.5000),
                        ST_MakePoint(-70.7050, -33.5050)
                ), 4326)),

    (4, 8, 8,
     '2025-06-08 12:30:00', '2025-06-12 10:00:00',
     'CANCELADO', 'ALTA',
     ST_SetSRID(ST_MakeLine(
                        ST_MakePoint(-70.7100, -33.5100),
                        ST_MakePoint(-70.7150, -33.5150)
                ), 4326)),

    (6, 7, 9,
     '2025-06-09 15:45:00', '2025-06-13 16:00:00',
     'ENTREGADO', 'ALTA',
     ST_SetSRID(ST_MakeLine(
                        ST_MakePoint(-70.7200, -33.5200),
                        ST_MakePoint(-70.7250, -33.5250)
                ), 4326)),

    (9, 10, 10,
     '2025-06-10 17:00:00', '2025-06-14 12:30:00',
     'PENDIENTE', 'ALTA',
     ST_SetSRID(ST_MakeLine(
                        ST_MakePoint(-70.7300, -33.5300),
                        ST_MakePoint(-70.7350, -33.5350)
                ), 4326)),

    (1, 5, 1,
     '2025-06-11 07:30:00', '2025-06-14 12:45:00',
     'ENTREGADO', 'URGENTE',
     ST_SetSRID(ST_MakeLine(
                        ST_MakePoint(-70.6400, -33.4400),
                        ST_MakePoint(-70.6450, -33.4450)
                ), 4326)),

    (3, 2, 3,
     '2025-06-12 09:00:00', '2025-06-15 15:20:00',
     'CANCELADO', 'URGENTE',
     ST_SetSRID(ST_MakeLine(
                        ST_MakePoint(-70.6500, -33.4500),
                        ST_MakePoint(-70.6550, -33.4550)
                ), 4326)),

    (7, 3, 2,
     '2025-06-13 11:00:00', '2025-06-16 10:00:00',
     'PENDIENTE', 'URGENTE',
     ST_SetSRID(ST_MakeLine(
                        ST_MakePoint(-70.6600, -33.4600),
                        ST_MakePoint(-70.6650, -33.4650)
                ), 4326)),

    (5, 9, 5,
     '2025-06-14 13:15:00', '2025-06-17 09:30:00',
     'ENTREGADO', 'URGENTE',
     ST_SetSRID(ST_MakeLine(
                        ST_MakePoint(-70.6700, -33.4700),
                        ST_MakePoint(-70.6750, -33.4750)
                ), 4326)),

    (10, 5, 4,
     '2025-06-15 16:00:00', '2025-06-18 11:00:00',
     'PENDIENTE', 'URGENTE',
     ST_SetSRID(ST_MakeLine(
                        ST_MakePoint(-70.6800, -33.4800),
                        ST_MakePoint(-70.6850, -33.4850)
                ), 4326)),

    (2, 8, 6,
     '2025-06-16 08:00:00', '2025-06-19 17:00:00',
     'PENDIENTE', 'ALTA',
     ST_SetSRID(ST_MakeLine(
                        ST_MakePoint(-70.6900, -33.4900),
                        ST_MakePoint(-70.6950, -33.4950)
                ), 4326)),

    (4, 1, 7,
     '2025-06-18 10:30:00', '2025-06-20 14:00:00',
     'ENTREGADO', 'ALTA',
     ST_SetSRID(ST_MakeLine(
                        ST_MakePoint(-70.7000, -33.5000),
                        ST_MakePoint(-70.7050, -33.5050)
                ), 4326)),

    (1, 3, 1,
     '2025-06-01 08:00:00', '2025-06-21 13:00:00',
     'PENDIENTE', 'ALTA',
     ST_SetSRID(ST_MakeLine(ARRAY[
         ST_MakePoint(-70.7400, -33.4200), -- Ubicado en Zona Central Noroeste
         ST_MakePoint(-70.6800, -33.4200), -- Ubicado en Zona Central Nordeste
         ST_MakePoint(-70.6800, -33.4500)  -- Ubicado en Zona Central Sureste
         ]), 4326)
    ),

    (6, 8, 8,
     '2025-06-19 12:45:00', '2025-06-22 10:00:00',
     'PENDIENTE', 'ALTA',
     ST_SetSRID(ST_MakeLine(
                        ST_MakePoint(-70.7100, -33.5100),
                        ST_MakePoint(-70.7150, -33.5150)
                ), 4326))
;

-------------------------------
-- Insertar registros en la tabla Servicios
-------------------------------
INSERT INTO Servicios (idEmpresaAsociada, nombreServicio, descripcionServicio, precioServicio, categoriaServicio, stock) VALUES
-- Documentos de Identidad
(1, 'Pasaporte Chileno', 'Documento biométrico válido para viajes internacionales', 12990, 'Identidad', 100),
(2, 'Cédula de Identidad', 'Renovación de documento de identificación nacional', 5990, 'Identidad', 200),
(3, 'Licencia de Conducir Clase B', 'Plástico con chip RFID vigente por 4 años', 10990, 'Identidad', 120),
-- Documentos Legales
(4, 'Escritura de Propiedad', 'Copia legalizada con firma notarial y timbre', 17990, 'Legal', 50),
(5, 'Contrato de Arrendamiento', 'Formato estándar SII impreso en papel notarial', 9990, 'Legal', 80),
(6, 'Poder Notarial Simple', 'Documento con firma legalizada ante notario', 14990, 'Legal', 35),
(7, 'Certificado de Antecedentes', 'Versión actualizada con código QR de validación', 6990, 'Legal', 150),
(8, 'Acta de Matrimonio', 'Copia legalizada del Registro Civil', 8990, 'Legal', 60),
-- Documentos Vehiculares
(9, 'Permiso de Circulación 2024', 'Incluye sticker de validación para parabrisas', 14990, 'Vehicular', 75),
(10, 'Certificado de Multas', 'Estado actualizado de infracciones de tránsito', 4990, 'Vehicular', 90),
(1, 'Informe de Dominio Vehicular', 'Historial de propietarios del vehículo', 7990, 'Vehicular', 40),
-- Documentos Médicos
(2, 'Historial Clínico Completo', 'Expediente físico con sellos hospitalarios', 8990, 'Médico', 30),
(3, 'Receta Médica Controlada', 'Formulario especial para medicamentos restringidos', 3990, 'Médico', 200),
(4, 'Certificado de Discapacidad', 'Documento CONADI con validez nacional', 11990, 'Médico', 25),
-- Documentos Académicos
(5, 'Título Profesional Legalizado', 'Copia autenticada por la universidad', 11990, 'Académico', 25),
(6, 'Certificado de Alumno Regular', 'Con sello institucional y código de validación', 4990, 'Académico', 150),
(7, 'Concentración de Notas', 'Historial académico oficial en papel seguridad', 6990, 'Académico', 80),
-- Documentos Tributarios
(8, 'Factura Original Timbrada', 'Con código SII y folio autorizado', 2990, 'Tributario', 500),
(9, 'Declaración Jurada de IVA', 'Formulario físico para presentación manual', 1990, 'Tributario', 300),
(10, 'Certificado de Deuda Tributaria', 'Estado actualizado de obligaciones con SII', 5990, 'Tributario', 120);



INSERT INTO DetallePedido
(idPedido, idServicio, cantidad, direccionDestino, direccionInicio, ubicacionInicio, ubicacionDestino)
VALUES
    (1, 1, 2,
     'Calle Destino 101, Santiago',
     'Av. Inicio 123, Santiago',
     ST_SetSRID(ST_MakePoint(-70.6410, -33.4410), 4326),
     ST_SetSRID(ST_MakePoint(-70.6450, -33.4450), 4326)),

    (5, 2, 1,
     'Calle Destino 202, Valparaíso',
     'Av. Inicio 456, Valparaíso',
     ST_SetSRID(ST_MakePoint(-70.6610, -33.4610), 4326),
     ST_SetSRID(ST_MakePoint(-70.6650, -33.4650), 4326)),

    (7, 3, 3,
     'Calle Destino 303, Providencia',
     'Av. Inicio 789, Providencia',
     ST_SetSRID(ST_MakePoint(-70.6710, -33.4710), 4326),
     ST_SetSRID(ST_MakePoint(-70.6750, -33.4750), 4326)),

    (3, 4, 1,
     'Calle Destino 404, Concepción',
     'Av. Inicio 101, Concepción',
     ST_SetSRID(ST_MakePoint(-70.6810, -33.4610), 4326),
     ST_SetSRID(ST_MakePoint(-70.6850, -33.4650), 4326)),

    (10, 5, 2,
     'Calle Destino 505, La Serena',
     'Av. Inicio 202, La Serena',
     ST_SetSRID(ST_MakePoint(-70.6910, -33.4410), 4326),
     ST_SetSRID(ST_MakePoint(-70.6950, -33.4450), 4326)),

    (2, 6, 1,
     'Calle Destino 606, Iquique',
     'Av. Inicio 303, Iquique',
     ST_SetSRID(ST_MakePoint(-70.7010, -33.4310), 4326),
     ST_SetSRID(ST_MakePoint(-70.7050, -33.4350), 4326)),

    (8, 7, 2,
     'Calle Destino 707, Temuco',
     'Av. Inicio 404, Temuco',
     ST_SetSRID(ST_MakePoint(-70.7110, -33.4210), 4326),
     ST_SetSRID(ST_MakePoint(-70.7150, -33.4250), 4326)),

    (4, 8, 3,
     'Calle Destino 808, Punta Arenas',
     'Av. Inicio 505, Punta Arenas',
     ST_SetSRID(ST_MakePoint(-70.7210, -33.4110), 4326),
     ST_SetSRID(ST_MakePoint(-70.7250, -33.4150), 4326)),

    (6, 9, 2,
     'Calle Destino 909, Viña del Mar',
     'Av. Inicio 606, Viña del Mar',
     ST_SetSRID(ST_MakePoint(-70.7310, -33.4010), 4326),
     ST_SetSRID(ST_MakePoint(-70.7350, -33.4050), 4326)),

    (9, 10, 1,
     'Calle Destino 1010, Puerto Montt',
     'Av. Inicio 707, Puerto Montt',
     ST_SetSRID(ST_MakePoint(-70.7410, -33.3910), 4326),
     ST_SetSRID(ST_MakePoint(-70.7450, -33.3950), 4326)),

    (2, 1, 1,
     'Calle Destino 102, Santiago',
     'Av. Inicio 124, Santiago',
     ST_SetSRID(ST_MakePoint(-70.6420, -33.4420), 4326),
     ST_SetSRID(ST_MakePoint(-70.6460, -33.4460), 4326)),

    (3, 1, 3,
     'Calle Destino 103, Santiago',
     'Av. Inicio 125, Santiago',
     ST_SetSRID(ST_MakePoint(-70.6430, -33.4430), 4326),
     ST_SetSRID(ST_MakePoint(-70.6470, -33.4470), 4326)),

    (1, 1, 2,
     'Calle Destino 104, Santiago',
     'Av. Inicio 126, Santiago',
     ST_SetSRID(ST_MakePoint(-70.6440, -33.4440), 4326),
     ST_SetSRID(ST_MakePoint(-70.6480, -33.4480), 4326));

-------------------------------
-- Insertar registros en la tabla MedioDePago
-------------------------------
INSERT INTO MedioDePago (nombreMetodoDePago) VALUES
                                                 ('Efectivo'),
                                                 ('Tarjeta de Débito'),
                                                 ('Tarjeta de Crédito'),
                                                 ('Transferencia Bancaria');

-------------------------------
-- Insertar registros en la tabla Rating
-------------------------------
INSERT INTO Rating (idPedido, score, comment) VALUES
                                                  (1, 5, 'Entrega súper rápida, el repartidor fue muy amable incluso llegó antes de lo esperado'),
                                                  (3, 5, 'Documento llegó en perfecto estado y bien protegido. Excelente servicio!'),
                                                  (6, 5, 'Empresa muy profesional, el notario vino con todo lo necesario para la firma'),
                                                  (4, 5, 'El historial clínico llegó sellado y confidencial como se prometió'),
                                                  (3, 5, 'Urgente y lo resolvieron en 2 horas! Increíble eficiencia'),
                                                  (2, 5, 'Envío internacional con seguimiento en tiempo real, muy confiable'),
                                                  (1, 5, 'A pesar de ser documento legal, explicaron todo en lenguaje sencillo'),
                                                  (4, 4, 'Buen servicio pero el repartidor no llamó antes de llegar'),
                                                  (5, 4, 'Documentos correctos aunque el embalaje pudo ser más resistente'),
                                                  (9, 4, 'Cumplieron con el plazo aunque con poco margen de tiempo'),
                                                  (5, 4, 'Buen trato pero el proceso de pago fue un poco engorroso'),
                                                  (5, 4, 'Receta médica llegó bien pero el horario de entrega no fue exacto'),
                                                  (6, 4, 'El certificado tenía un pequeño error pero lo corrigieron al instante'),
                                                  (7, 3, 'Demoró más de lo prometido y no dieron explicaciones'),
                                                  (8, 3, 'El documento venía con un doblez en la esquina, regular'),
                                                  (9, 3, 'Servicio aceptable pero el repartidor parecía apurado'),
                                                  (1, 3, 'No hubo seguimiento del pedido como ofrecían'),
                                                  (2, 2, 'Pedido cancelado pero no me devolvieron el dinero inmediatamente'),
                                                  (8, 1, 'Documentos llegaron mojados por la lluvia, pésimo embalaje');

-------------------------------
-- Insertar registros en la tabla Zonas_cobertura
-------------------------------

-- Zona Central Noroeste
INSERT INTO zonas_cobertura (nombre, geom)
VALUES (
           'Zona Central Noroeste',
           ST_GeomFromText(
                   'MULTIPOLYGON(((-70.7500 -33.4100, -70.6950 -33.4100, -70.6950 -33.4400, -70.7500 -33.4400, -70.7500 -33.4100)))',
                   4326
           )
       );

-- Zona Central Nordeste
INSERT INTO zonas_cobertura (nombre, geom)
VALUES (
           'Zona Central Nordeste',
           ST_GeomFromText(
                   'MULTIPOLYGON(((-70.6950 -33.4100, -70.6400 -33.4100, -70.6400 -33.4400, -70.6950 -33.4400, -70.6950 -33.4100)))',
                   4326
           )
       );

-- Zona Central Suroeste
INSERT INTO zonas_cobertura (nombre, geom)
VALUES (
           'Zona Central Suroeste',
           ST_GeomFromText(
                   'MULTIPOLYGON(((-70.7500 -33.4400, -70.6950 -33.4400, -70.6950 -33.4700, -70.7500 -33.4700, -70.7500 -33.4400)))',
                   4326
           )
       );

-- Zona Central Sureste
INSERT INTO zonas_cobertura (nombre, geom)
VALUES (
           'Zona Central Sureste',
           ST_GeomFromText(
                   'MULTIPOLYGON(((-70.6950 -33.4400, -70.6400 -33.4400, -70.6400 -33.4700, -70.6950 -33.4700, -70.6950 -33.4400)))',
                   4326
           )
       );

-- Región 1: Arica y Parinacota
INSERT INTO zonas_cobertura (nombre, geom)
VALUES
    ('Arica y Parinacota',
     ST_GeomFromText(
             'MULTIPOLYGON(((-70.8 -17.8, -69.5 -17.8, -69.5 -19.0, -70.8 -19.0, -70.8 -17.8)))',
             4326
     )
    );

-- Región 2: Tarapacá
INSERT INTO zonas_cobertura (nombre, geom)
VALUES
    ('Tarapacá',
     ST_GeomFromText(
             'MULTIPOLYGON(((-70.0 -19.0, -68.0 -19.0, -68.0 -20.5, -70.0 -20.5, -70.0 -19.0)))',
             4326
     )
    );

-- Región 3: Antofagasta
INSERT INTO zonas_cobertura (nombre, geom)
VALUES
    ('Antofagasta',
     ST_GeomFromText(
             'MULTIPOLYGON(((-71.0 -21.0, -69.5 -21.0, -69.5 -23.0, -71.0 -23.0, -71.0 -21.0)))',
             4326
     )
    );

-- Región 4: Atacama
INSERT INTO zonas_cobertura (nombre, geom)
VALUES
    ('Atacama',
     ST_GeomFromText(
             'MULTIPOLYGON(((-70.5 -25.0, -69.0 -25.0, -69.0 -26.5, -70.5 -26.5, -70.5 -25.0)))',
             4326
     )
    );

-- Región 5: Coquimbo
INSERT INTO zonas_cobertura (nombre, geom)
VALUES
    ('Coquimbo',
     ST_GeomFromText(
             'MULTIPOLYGON(((-71.0 -26.5, -70.0 -26.5, -70.0 -28.0, -71.0 -28.0, -71.0 -26.5)))',
             4326
     )
    );

-- Región 6: Valparaíso
INSERT INTO zonas_cobertura (nombre, geom)
VALUES
    (
        'Valparaíso',
        ST_GeomFromText(
                'MULTIPOLYGON(((-71.2 -33.0, -70.9 -33.0, -70.9 -33.3, -71.2 -33.3, -71.2 -33.0)))',
                4326
        )
    );



-- Región 8: O’Higgins
INSERT INTO zonas_cobertura (nombre, geom)
VALUES
    ('O’Higgins',
     ST_GeomFromText(
             'MULTIPOLYGON(((-71.0 -33.7, -70.5 -33.7, -70.5 -35.0, -71.0 -35.0, -71.0 -33.7)))',
             4326
     )
    );

-- Región 9: Maule
INSERT INTO zonas_cobertura (nombre, geom)
VALUES
    ('Maule',
     ST_GeomFromText(
             'MULTIPOLYGON(((-71.0 -34.0, -70.0 -34.0, -70.0 -35.5, -71.0 -35.5, -71.0 -34.0)))',
             4326
     )
    );

-- Región 10: Ñuble
INSERT INTO zonas_cobertura (nombre, geom)
VALUES
    ('Ñuble',
     ST_GeomFromText(
             'MULTIPOLYGON(((-71.5 -35.5, -70.5 -35.5, -70.5 -36.2, -71.5 -36.2, -71.5 -35.5)))',
             4326
     )
    );

-- Región 11: Biobío
INSERT INTO zonas_cobertura (nombre, geom)
VALUES
    ('Biobío',
     ST_GeomFromText(
             'MULTIPOLYGON(((-73.0 -36.0, -71.5 -36.0, -71.5 -38.0, -73.0 -38.0, -73.0 -36.0)))',
             4326
     )
    );

-- Región 12: La Araucanía
INSERT INTO zonas_cobertura (nombre, geom)
VALUES
    ('La Araucanía',
     ST_GeomFromText(
             'MULTIPOLYGON(((-73.0 -38.0, -71.5 -38.0, -71.5 -40.0, -73.0 -40.0, -73.0 -38.0)))',
             4326
     )
    );

-- Región 13: Los Ríos
INSERT INTO zonas_cobertura (nombre, geom)
VALUES
    ('Los Ríos',
     ST_GeomFromText(
             'MULTIPOLYGON(((-73.0 -40.0, -71.5 -40.0, -71.5 -41.5, -73.0 -41.5, -73.0 -40.0)))',
             4326
     )
    );

-- Región 14: Los Lagos
INSERT INTO zonas_cobertura (nombre, geom)
VALUES
    ('Los Lagos',
     ST_GeomFromText(
             'MULTIPOLYGON(((-75.0 -41.0, -72.0 -41.0, -72.0 -43.0, -75.0 -43.0, -75.0 -41.0)))',
             4326
     )
    );

-- Región 15: Aysén
INSERT INTO zonas_cobertura (nombre, geom)
VALUES
    ('Aysén',
     ST_GeomFromText(
             'MULTIPOLYGON(((-74.5 -44.0, -70.5 -44.0, -70.5 -47.0, -74.5 -47.0, -74.5 -44.0)))',
             4326
     )
    );

-- Región 16: Magallanes y de la Antártica Chilena
INSERT INTO zonas_cobertura (nombre, geom)
VALUES
    ('Magallanes y de la Antártica Chilena',
     ST_GeomFromText(
             'MULTIPOLYGON(((-75.0 -49.0, -70.0 -49.0, -70.0 -56.0, -75.0 -56.0, -75.0 -49.0)))',
             4326
     )
    );
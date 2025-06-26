-- Insertar administrador contraseña: admin1
INSERT INTO usuarios (rut, name_param, email, phone, birthdate, password, role) VALUES
    ('11.111.111-1', 'Admin Principal', 'admin@empresa.com', '+569 1111 1111', '1980-01-01', '$2y$10$dPDS.V6zJNYTeDyzZoXi9.gnJqYZInE2OfeTEyj/kzupc1FkwNtYa', 'ADMIN');

-- Insertar clientes como usuarios
INSERT INTO usuarios (rut, name_param, email, phone, birthdate, password, role) VALUES
-- Clientes (rut ficticio, birthdate estimado) contraseña: cliente1
('12.345.678-9', 'María González', 'maria.gonzalez@email.com', '555-100-2001', '1985-05-15', '$2y$10$bZM2F0RgXSxZCHa8fuTNq.dvH6gcT8Jr5H560GudHbYpCCL6YwPGG', 'CLIENTE'),
('23.456.789-0', 'Carlos Rodríguez', 'carlos.rodriguez@email.com', '555-100-2002', '1990-08-22', '$2y$10$bZM2F0RgXSxZCHa8fuTNq.dvH6gcT8Jr5H560GudHbYpCCL6YwPGG', 'CLIENTE'),
('34.567.890-1', 'Laura Martínez', 'laura.martinez@email.com', '555-100-2003', '1982-11-30', '$2y$10$bZM2F0RgXSxZCHa8fuTNq.dvH6gcT8Jr5H560GudHbYpCCL6YwPGG', 'CLIENTE'),
('45.678.901-2', 'Jorge Sánchez', 'jorge.sanchez@email.com', '555-100-2004', '1978-03-10', '$2y$10$bZM2F0RgXSxZCHa8fuTNq.dvH6gcT8Jr5H560GudHbYpCCL6YwPGG$2y$10$bZM2F0RgXSxZCHa8fuTNq.dvH6gcT8Jr5H560GudHbYpCCL6YwPGG', 'CLIENTE'),
('56.789.012-3', 'Ana López', 'ana.lopez@email.com', '555-100-2005', '1995-07-25', '$2y$10$bZM2F0RgXSxZCHa8fuTNq.dvH6gcT8Jr5H560GudHbYpCCL6YwPGG', 'CLIENTE'),
('67.890.123-4', 'Pedro Ramírez', 'pedro.ramirez@email.com', '555-100-2006', '1989-09-18', '$2y$10$bZM2F0RgXSxZCHa8fuTNq.dvH6gcT8Jr5H560GudHbYpCCL6YwPGG', 'CLIENTE'),
('78.901.234-5', 'Sofía Herrera', 'sofia.herrera@email.com', '555-100-2007', '1992-12-05', '$2y$10$bZM2F0RgXSxZCHa8fuTNq.dvH6gcT8Jr5H560GudHbYpCCL6YwPGG', 'CLIENTE'),
('89.012.345-6', 'Diego Jiménez', 'diego.jimenez@email.com', '555-100-2008', '1987-04-20', '$2y$10$bZM2F0RgXSxZCHa8fuTNq.dvH6gcT8Jr5H560GudHbYpCCL6YwPGG', 'CLIENTE'),
('90.123.456-7', 'Elena Castro', 'elena.castro@email.com', '555-100-2009', '1983-06-15', '$2y$10$bZM2F0RgXSxZCHa8fuTNq.dvH6gcT8Jr5H560GudHbYpCCL6YwPGG', 'CLIENTE'),
('01.234.567-8', 'Ricardo Mora', 'ricardo.mora@email.com', '555-100-2010', '1991-02-28', '$2y$10$bZM2F0RgXSxZCHa8fuTNq.dvH6gcT8Jr5H560GudHbYpCCL6YwPGG', 'CLIENTE');

-- Insertar repartidores como usuarios (TRABAJADORES)
INSERT INTO usuarios (rut, name_param, email, phone, birthdate, password, role) VALUES
-- Repartidores (rut ficticio, birthdate estimado) contraseña: trabajador1
('98.765.432-1', 'Fernando Silva', 'fernando.silva@empresa.com', '+569 1234 5678', '1986-01-10', '$2y$10$oKcmafHC3wneIvbMLIQUGugTuDAx6HCa7MEFyT3pEw.Gq5AusU6X6', 'TRABAJADOR'),
('87.654.321-0', 'Camila Rojas', 'camila.rojas@empresa.com', '+569 2345 6789', '1990-05-20', '$2y$10$oKcmafHC3wneIvbMLIQUGugTuDAx6HCa7MEFyT3pEw.Gq5AusU6X6', 'TRABAJADOR'),
('76.543.210-9', 'Pablo Mendoza', 'pablo.mendoza@empresa.com', '+569 3456 7890', '1984-08-15', '$2y$10$oKcmafHC3wneIvbMLIQUGugTuDAx6HCa7MEFyT3pEw.Gq5AusU6X6', 'TRABAJADOR'),
('65.432.109-8', 'Valentina Díaz', 'valentina.diaz@empresa.com', '+569 4567 8901', '1988-11-25', '$2y$10$oKcmafHC3wneIvbMLIQUGugTuDAx6HCa7MEFyT3pEw.Gq5AusU6X6', 'TRABAJADOR'),
('54.321.098-7', 'Sebastián Torres', 'sebastian.torres@empresa.com', '+569 5678 9012', '1982-03-30', '$2y$10$oKcmafHC3wneIvbMLIQUGugTuDAx6HCa7MEFyT3pEw.Gq5AusU6X6', 'TRABAJADOR'),
('43.210.987-6', 'Daniela Vargas', 'daniela.vargas@empresa.com', '+569 6789 0123', '1993-07-12', '$2y$10$oKcmafHC3wneIvbMLIQUGugTuDAx6HCa7MEFyT3pEw.Gq5AusU6X6', 'TRABAJADOR'),
('32.109.876-5', 'Alejandro Soto', 'alejandro.soto@empresa.com', '+569 7890 1234', '1987-09-18', '$2y$10$oKcmafHC3wneIvbMLIQUGugTuDAx6HCa7MEFyT3pEw.Gq5AusU6X6', 'TRABAJADOR'),
('21.098.765-4', 'Javiera Núñez', 'javiera.nunez@empresa.com', '+569 8901 2345', '1991-04-22', '$2y$10$oKcmafHC3wneIvbMLIQUGugTuDAx6HCa7MEFyT3pEw.Gq5AusU6X6', 'TRABAJADOR'),
('10.987.654-3', 'Mauricio Lagos', 'mauricio.lagos@empresa.com', '+569 9012 3456', '1985-12-08', '$2y$10$oKcmafHC3wneIvbMLIQUGugTuDAx6HCa7MEFyT3pEw.Gq5AusU6X6', 'TRABAJADOR'),
('09.876.543-2', 'Antonella Peña', 'antonella.pena@empresa.com', '+569 0123 4567', '1989-06-14', '$2y$10$oKcmafHC3wneIvbMLIQUGugTuDAx6HCa7MEFyT3pEw.Gq5AusU6X6', 'TRABAJADOR');

-- El resto de tus inserciones (clientes, repartidores, empresas, servicios, pedidos, etc.) pueden permanecer igual
-- ya que son consistentes con esta estructura

-- Insertar clientes (tabla cliente)
INSERT INTO cliente (nombre, direccion, email, telefono) VALUES
('María González', 'Av. Libertador Bernardo O''Higgins 123, Santiago', 'maria.gonzalez@email.com', '555-100-2001'),
('Carlos Rodríguez', 'Calle Prat 456, Valparaíso', 'carlos.rodriguez@email.com', '555-100-2002'),
('Laura Martínez', 'Los Leones 789, Providencia', 'laura.martinez@email.com', '555-100-2003'),
('Jorge Sánchez', 'Av. Argentina 101, Concepción', 'jorge.sanchez@email.com', '555-100-2004'),
('Ana López', 'San Martín 202, La Serena', 'ana.lopez@email.com', '555-100-2005'),
('Pedro Ramírez', 'Arturo Prat 303, Iquique', 'pedro.ramirez@email.com', '555-100-2006'),
('Sofía Herrera', 'Los Carrera 404, Temuco', 'sofia.herrera@email.com', '555-100-2007'),
('Diego Jiménez', 'Baquedano 505, Punta Arenas', 'diego.jimenez@email.com', '555-100-2008'),
('Elena Castro', 'Portales 606, Viña del Mar', 'elena.castro@email.com', '555-100-2009'),
('Ricardo Mora', 'Aníbal Pinto 707, Puerto Montt', 'ricardo.mora@email.com', '555-100-2010');

-- Insertar repartidores (tabla repartidor)
INSERT INTO repartidor (nombre, telefono, disponible) VALUES
('Fernando Silva', '+569 1234 5678', TRUE),
('Camila Rojas', '+569 2345 6789', TRUE),
('Pablo Mendoza', '+569 3456 7890', TRUE),
('Valentina Díaz', '+569 4567 8901', TRUE),
('Sebastián Torres', '+569 5678 9012', TRUE),
('Daniela Vargas', '+569 6789 0123', TRUE),
('Alejandro Soto', '+569 7890 1234', TRUE),
('Javiera Núñez', '+569 8901 2345', TRUE),
('Mauricio Lagos', '+569 9012 3456', TRUE),
('Antonella Peña', '+569 0123 4567', TRUE);

--empresas asociadas

INSERT INTO EmpresaAsociada (nombreEmpresa, direccionEmpresa, tipoServicio) VALUES
('Logística Chile S.A.', 'Av. Andrés Bello 2235, Providencia, Santiago', 'Transporte de mercancías'),
('DelivExpress', 'Cerro El Plomo 5630, Las Condes, Santiago', 'Mensajería urgente'),
('PuroCampo Distribuciones', 'Camino La Pólvora 1420, Valparaíso', 'Productos agrícolas'),
('TecnoFast', 'Av. Nueva Tajamar 481, Torre Sur, Santiago', 'Reparto tecnológico'),
('FarmaDelivery Chile', 'San Pascual 735, Concepción', 'Medicamentos y productos médicos'),
('Sabores del Sur', 'O''Higgins 1023, Temuco', 'Alimentos gourmet'),
('PescaRápida', 'Chacabuco 1240, Coquimbo', 'Productos del mar'),
('Andina Logistic', 'Los Militares 5890, Las Condes, Santiago', 'Carga internacional'),
('EcoEnvíos', 'Manuel Montt 1440, Puerto Montt', 'Paquetería sustentable'),
('VinoExpress', 'Av. Vitacura 2932, Vitacura, Santiago', 'Distribución de vinos premium');


-- Servicios --> Documentos

INSERT INTO Servicios (idEmpresaAsociada, nombreServicio, descripcionServicio, precioServicio, categoriaServicio, stock) VALUES
-- Documentos de Identidad (1-3)
(1, 'Pasaporte Chileno', 'Documento biométrico válido para viajes internacionales', 12990, 'Identidad', 100),
(2, 'Cédula de Identidad', 'Renovación de documento de identificación nacional', 5990, 'Identidad', 200),
(3, 'Licencia de Conducir Clase B', 'Plástico con chip RFID vigente por 4 años', 10990, 'Identidad', 120),

-- Documentos Legales (4-8)
(4, 'Escritura de Propiedad', 'Copia legalizada con firma notarial y timbre', 17990, 'Legal', 50),
(5, 'Contrato de Arrendamiento', 'Formato estándar SII impreso en papel notarial', 9990, 'Legal', 80),
(6, 'Poder Notarial Simple', 'Documento con firma legalizada ante notario', 14990, 'Legal', 35),
(7, 'Certificado de Antecedentes', 'Versión actualizada con código QR de validación', 6990, 'Legal', 150),
(8, 'Acta de Matrimonio', 'Copia legalizada del Registro Civil', 8990, 'Legal', 60),

-- Documentos Vehiculares (9-11)
(9, 'Permiso de Circulación 2024', 'Incluye sticker de validación para parabrisas', 14990, 'Vehicular', 75),
(10, 'Certificado de Multas', 'Estado actualizado de infracciones de tránsito', 4990, 'Vehicular', 90),
(1, 'Informe de Dominio Vehicular', 'Historial de propietarios del vehículo', 7990, 'Vehicular', 40),

-- Documentos Médicos (12-14)
(2, 'Historial Clínico Completo', 'Expediente físico con sellos hospitalarios', 8990, 'Médico', 30),
(3, 'Receta Médica Controlada', 'Formulario especial para medicamentos restringidos', 3990, 'Médico', 200),
(4, 'Certificado de Discapacidad', 'Documento CONADI con validez nacional', 11990, 'Médico', 25),

-- Documentos Académicos (15-17)
(5, 'Título Profesional Legalizado', 'Copia autenticada por la universidad', 11990, 'Académico', 25),
(6, 'Certificado de Alumno Regular', 'Con sello institucional y código de validación', 4990, 'Académico', 150),
(7, 'Concentración de Notas', 'Historial académico oficial en papel seguridad', 6990, 'Académico', 80),

-- Documentos Tributarios (18-20)
(8, 'Factura Original Timbrada', 'Con código SII y folio autorizado', 2990, 'Tributario', 500),
(9, 'Declaración Jurada de IVA', 'Formulario físico para presentación manual', 1990, 'Tributario', 300),
(10, 'Certificado de Deuda Tributaria', 'Estado actualizado de obligaciones con SII', 5990, 'Tributario', 120);


--pedidos

INSERT INTO OrderEntity (
    cliente_id,
    idEmpresaAsociada,
    repartidor_id,
    fechaPedido,
    fechaEntrega,
    estadoPedido,
    prioridadPedido
) VALUES

      (1, 3, 1, '2024-06-01 08:00:00', '2024-06-01 13:00:00', 'ENTREGADO', 'URGENTE'),  -- Documento médico entregado
      (5, 2, 3, '2024-06-01 09:30:00', '2024-06-01 17:00:00', 'CANCELADO', 'URGENTE'),  -- Cliente canceló
      (7, 5, 2, '2024-06-01 10:15:00', '2024-06-02 12:00:00', 'PENDIENTE', 'URGENTE'),   -- Receta controlada
      (3, 1, 5, '2024-06-01 11:45:00', '2024-06-01 16:30:00', 'ENTREGADO', 'URGENTE'),  -- Pasaporte express
      (10, 4, 4, '2024-06-01 14:00:00', '2024-06-02 09:00:00', 'PENDIENTE', 'URGENTE'),  -- Contrato legal

      (2, 4, 6, '2024-06-02 08:20:00', '2024-06-02 14:15:00', 'ENTREGADO', 'ALTA'),     -- Escritura notarial
      (8, 6, 7, '2024-06-02 10:00:00', '2024-06-03 11:00:00', 'PENDIENTE', 'ALTA'),     -- Certificado agrícola
      (4, 8, 8, '2024-06-02 12:30:00', '2024-06-04 10:00:00', 'CANCELADO', 'ALTA'),      -- Envío internacional cancelado
      (6, 7, 9, '2024-06-02 15:45:00', '2024-06-03 16:00:00', 'ENTREGADO', 'ALTA'),     -- Permiso de circulación
      (9, 10, 10, '2024-06-02 17:00:00', '2024-06-03 12:30:00', 'PENDIENTE', 'ALTA'),   -- Certificado de origen

      (1, 5, 1, '2024-06-03 07:30:00', '2024-06-03 12:45:00', 'ENTREGADO', 'URGENTE'),   -- Historial clínico
      (3, 2, 3, '2024-06-03 09:00:00', '2024-06-03 15:20:00', 'CANCELADO', 'URGENTE'),    -- Cancelado por cliente
      (7, 3, 2, '2024-06-03 11:00:00', '2024-06-05 10:00:00', 'PENDIENTE', 'URGENTE'),   -- Discapacidad CONADI
      (5, 9, 5, '2024-06-03 13:15:00', '2024-06-04 09:30:00', 'ENTREGADO', 'URGENTE'),    -- Factura SII urgente
      (10, 5, 4, '2024-06-03 16:00:00', '2024-06-04 11:00:00', 'PENDIENTE', 'URGENTE'),   -- Receta controlada

      (2, 8, 6, '2024-06-04 08:00:00', '2024-06-06 17:00:00', 'PENDIENTE', 'ALTA'),       -- DHL documentos
      (4, 1, 7, '2024-06-04 10:30:00', '2024-06-05 14:00:00', 'ENTREGADO', 'ALTA'),       -- Título universitario
      (6, 8, 8, '2024-06-04 12:45:00', '2024-06-07 10:00:00', 'PENDIENTE', 'ALTA'),       -- Certificado de multas
      (8, 4, 9, '2024-06-04 15:00:00', '2024-06-05 16:30:00', 'CANCELADO', 'ALTA'),        -- Cancelado por empresa
      (9, 8, 10, '2024-06-04 17:20:00', '2024-06-06 13:15:00', 'ENTREGADO', 'ALTA');       -- Permiso de pesca


--medios de pago

INSERT INTO MedioDePago (nombreMetodoDePago) VALUES
-- Métodos tradicionales
('Efectivo'),
('Tarjeta de Débito'),
('Tarjeta de Crédito'),
('Transferencia Bancaria');

-- calificaciones

INSERT INTO Rating (idPedido, score, comment) VALUES
                                                  (1, 5, 'Entrega súper rápida, el repartidor fue muy amable incluso llegó antes de lo esperado'),
                                                  (3, 5, 'Documento llegó en perfecto estado y bien protegido. Excelente servicio!'),
                                                  (6, 5, 'Empresa muy profesional, el notario vino con todo lo necesario para la firma'),
                                                  (11, 5, 'El historial clínico llegó sellado y confidencial como se prometió'),
                                                  (14, 5, 'Urgente y lo resolvieron en 2 horas! Increíble eficiencia'),
                                                  (16, 5, 'Envío internacional con seguimiento en tiempo real, muy confiable'),
                                                  (19, 5, 'A pesar de ser documento legal, explicaron todo en lenguaje sencillo'),
                                                  (4, 4, 'Buen servicio pero el repartidor no llamó antes de llegar'),
                                                  (5, 4, 'Documentos correctos aunque el embalaje pudo ser más resistente'),
                                                  (9, 4, 'Cumplieron con el plazo aunque con poco margen de tiempo'),
                                                  (12, 4, 'Buen trato pero el proceso de pago fue un poco engorroso'),
                                                  (15, 4, 'Receta médica llegó bien pero el horario de entrega no fue exacto'),
                                                  (17, 4, 'El certificado tenía un pequeño error pero lo corrigieron al instante'),
                                                  (7, 3, 'Demoró más de lo prometido y no dieron explicaciones'),
                                                  (10, 3, 'El documento venía con un doblez en la esquina, regular'),
                                                  (13, 3, 'Servicio aceptable pero el repartidor parecía apurado'),
                                                  (18, 3, 'No hubo seguimiento del pedido como ofrecían'),
                                                  (2, 2, 'Pedido cancelado pero no me devolvieron el dinero inmediatamente'),
                                                  (8, 1, 'Documentos llegaron mojados por la lluvia, pésimo embalaje');




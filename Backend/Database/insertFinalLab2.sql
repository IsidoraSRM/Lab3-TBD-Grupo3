-------------------------------
-- Insetar registros en tabla usuarios (incluye ubicacion)
-------------------------------
-- Insertar administrador (contraseña: admin1)
INSERT INTO usuarios (rut, name_param, email, phone, birthdate, password, role, ubicacion) VALUES
('11.111.111-1', 'Admin Principal', 'admin@empresa.com', '+569 1111 1111', '1980-01-01',
 '$2y$10$dPDS.V6zJNYTeDyzZoXi9.gnJqYZInE2OfeTEyj/kzupc1FkwNtYa', 'ADMIN',
 ST_SetSRID(ST_MakePoint(-70.6500, -33.4500), 4326));

-- Insertar clientes como usuarios (contraseña: cliente1)
INSERT INTO usuarios (rut, name_param, email, phone, birthdate, password, role, ubicacion) VALUES
('12.345.678-9', 'María González', 'maria.gonzalez@email.com',
 '555-100-2001', '1985-05-15',
 '$2y$10$bZM2F0RgXSxZCHa8fuTNq.dvH6gcT8Jr5H560GudHbYpCCL6YwPGG', 'CLIENTE',
 ST_SetSRID(ST_MakePoint(-70.6450, -33.4520), 4326)),
('23.456.789-0', 'Carlos Rodríguez', 'carlos.rodriguez@email.com',
 '555-100-2002', '1990-08-22',
 '$2y$10$bZM2F0RgXSxZCHa8fuTNq.dvH6gcT8Jr5H560GudHbYpCCL6YwPGG', 'CLIENTE',
 ST_SetSRID(ST_MakePoint(-70.6600, -33.4600), 4326)),
('34.567.890-1', 'Laura Martínez', 'laura.martinez@email.com',
 '555-100-2003', '1982-11-30',
 '$2y$10$bZM2F0RgXSxZCHa8fuTNq.dvH6gcT8Jr5H560GudHbYpCCL6YwPGG', 'CLIENTE',
 ST_SetSRID(ST_MakePoint(-70.6700, -33.4650), 4326)),
('45.678.901-2', 'Jorge Sánchez', 'jorge.sanchez@email.com',
 '555-100-2004', '1978-03-10',
 '$2y$10$bZM2F0RgXSxZCHa8fuTNq.dvH6gcT8Jr5H560GudHbYpCCL6YwPGG', 'CLIENTE',
 ST_SetSRID(ST_MakePoint(-70.6800, -33.4550), 4326)),
('56.789.012-3', 'Ana López', 'ana.lopez@email.com',
 '555-100-2005', '1995-07-25',
 '$2y$10$bZM2F0RgXSxZCHa8fuTNq.dvH6gcT8Jr5H560GudHbYpCCL6YwPGG', 'CLIENTE',
 ST_SetSRID(ST_MakePoint(-70.6900, -33.4400), 4326)),
('67.890.123-4', 'Pedro Ramírez', 'pedro.ramirez@email.com',
 '555-100-2006', '1989-09-18',
 '$2y$10$bZM2F0RgXSxZCHa8fuTNq.dvH6gcT8Jr5H560GudHbYpCCL6YwPGG', 'CLIENTE',
 ST_SetSRID(ST_MakePoint(-70.7050, -33.4350), 4326)),
('78.901.234-5', 'Sofía Herrera', 'sofia.herrera@email.com',
 '555-100-2007', '1992-12-05',
 '$2y$10$bZM2F0RgXSxZCHa8fuTNq.dvH6gcT8Jr5H560GudHbYpCCL6YwPGG', 'CLIENTE',
 ST_SetSRID(ST_MakePoint(-70.7150, -33.4300), 4326)),
('89.012.345-6', 'Diego Jiménez', 'diego.jimenez@email.com',
 '555-100-2008', '1987-04-20',
 '$2y$10$bZM2F0RgXSxZCHa8fuTNq.dvH6gcT8Jr5H560GudHbYpCCL6YwPGG', 'CLIENTE',
 ST_SetSRID(ST_MakePoint(-70.7200, -33.4250), 4326)),
('90.123.456-7', 'Elena Castro', 'elena.castro@email.com',
 '555-100-2009', '1983-06-15',
 '$2y$10$bZM2F0RgXSxZCHa8fuTNq.dvH6gcT8Jr5H560GudHbYpCCL6YwPGG', 'CLIENTE',
 ST_SetSRID(ST_MakePoint(-70.7300, -33.4200), 4326)),
('01.234.567-8', 'Ricardo Mora', 'ricardo.mora@email.com',
 '555-100-2010', '1991-02-28',
 '$2y$10$bZM2F0RgXSxZCHa8fuTNq.dvH6gcT8Jr5H560GudHbYpCCL6YwPGG', 'CLIENTE',
 ST_SetSRID(ST_MakePoint(-70.7400, -33.4150), 4326)),
('12.345.678-9','Pepito Perez','pepito.perez@email.com','555-100-2011','1985-03-15','$2y$10$bZM2F0RgXSxZCHa8fuTNq.dvH6gcT8Jr5H560GudHbYpCCL6YwPGG',
'CLIENTE',ST_SetSRID(ST_MakePoint(-71.2486, -34.9828), 4326));


-- Insertar repartidores como usuarios (TRABAJADORES) (contraseña: trabajador1)
INSERT INTO usuarios (rut, name_param, email, phone, birthdate, password, role, ubicacion) VALUES
('98.765.432-1', 'Fernando Silva', 'fernando.silva@empresa.com',
 '+569 1234 5678', '1986-01-10',
 '$2y$10$oKcmafHC3wneIvbMLIQUGugTuDAx6HCa7MEFyT3pEw.Gq5AusU6X6', 'TRABAJADOR',
 ST_SetSRID(ST_MakePoint(-70.6500, -33.4450), 4326)),
('87.654.321-0', 'Camila Rojas', 'camila.rojas@empresa.com',
 '+569 2345 6789', '1990-05-20',
 '$2y$10$oKcmafHC3wneIvbMLIQUGugTuDAx6HCa7MEFyT3pEw.Gq5AusU6X6', 'TRABAJADOR',
 ST_SetSRID(ST_MakePoint(-70.6550, -33.4500), 4326)),
('76.543.210-9', 'Pablo Mendoza', 'pablo.mendoza@empresa.com',
 '+569 3456 7890', '1984-08-15',
 '$2y$10$oKcmafHC3wneIvbMLIQUGugTuDAx6HCa7MEFyT3pEw.Gq5AusU6X6', 'TRABAJADOR',
 ST_SetSRID(ST_MakePoint(-70.6600, -33.4550), 4326)),
('65.432.109-8', 'Valentina Díaz', 'valentina.diaz@empresa.com',
 '+569 4567 8901', '1988-11-25',
 '$2y$10$oKcmafHC3wneIvbMLIQUGugTuDAx6HCa7MEFyT3pEw.Gq5AusU6X6', 'TRABAJADOR',
 ST_SetSRID(ST_MakePoint(-70.6650, -33.4600), 4326)),
('54.321.098-7', 'Sebastián Torres', 'sebastian.torres@empresa.com',
 '+569 5678 9012', '1982-03-30',
 '$2y$10$oKcmafHC3wneIvbMLIQUGugTuDAx6HCa7MEFyT3pEw.Gq5AusU6X6', 'TRABAJADOR',
 ST_SetSRID(ST_MakePoint(-70.6700, -33.4650), 4326)),
('43.210.987-6', 'Daniela Vargas', 'daniela.vargas@empresa.com',
 '+569 6789 0123', '1993-07-12',
 '$2y$10$oKcmafHC3wneIvbMLIQUGugTuDAx6HCa7MEFyT3pEw.Gq5AusU6X6', 'TRABAJADOR',
 ST_SetSRID(ST_MakePoint(-70.6750, -33.4700), 4326)),
('32.109.876-5', 'Alejandro Soto', 'alejandro.soto@empresa.com',
 '+569 7890 1234', '1987-09-18',
 '$2y$10$oKcmafHC3wneIvbMLIQUGugTuDAx6HCa7MEFyT3pEw.Gq5AusU6X6', 'TRABAJADOR',
 ST_SetSRID(ST_MakePoint(-70.6800, -33.4750), 4326)),
('21.098.765-4', 'Javiera Núñez', 'javiera.nunez@empresa.com',
 '+569 8901 2345', '1991-04-22',
 '$2y$10$oKcmafHC3wneIvbMLIQUGugTuDAx6HCa7MEFyT3pEw.Gq5AusU6X6', 'TRABAJADOR',
 ST_SetSRID(ST_MakePoint(-70.6850, -33.4800), 4326)),
('10.987.654-3', 'Mauricio Lagos', 'mauricio.lagos@empresa.com',
 '+569 9012 3456', '1985-12-08',
 '$2y$10$oKcmafHC3wneIvbMLIQUGugTuDAx6HCa7MEFyT3pEw.Gq5AusU6X6', 'TRABAJADOR',
 ST_SetSRID(ST_MakePoint(-70.6900, -33.4850), 4326)),
('09.876.543-2', 'Antonella Peña', 'antonella.pena@empresa.com',
 '+569 0123 4567', '1989-06-14',
 '$2y$10$oKcmafHC3wneIvbMLIQUGugTuDAx6HCa7MEFyT3pEw.Gq5AusU6X6', 'TRABAJADOR',
 ST_SetSRID(ST_MakePoint(-70.6950, -33.4900), 4326));

-------------------------------
-- Insertar registros en la tabla cliente
-------------------------------
INSERT INTO cliente (nombre, direccion, email, telefono, ubicacion) VALUES
('María González', 'Av. Libertador Bernardo O''Higgins 123, Santiago', 
 'maria.gonzalez@email.com', '555-100-2001',
 ST_SetSRID(ST_MakePoint(-70.6450, -33.4520), 4326)),

('Carlos Rodríguez', 'Calle Prat 456, Valparaíso',
 'carlos.rodriguez@email.com', '555-100-2002',
 ST_SetSRID(ST_MakePoint(-70.6600, -33.4600), 4326)),

('Laura Martínez', 'Los Leones 789, Providencia',
 'laura.martinez@email.com', '555-100-2003',
 ST_SetSRID(ST_MakePoint(-70.6700, -33.4650), 4326)),

('Jorge Sánchez', 'Av. Argentina 101, Concepción',
 'jorge.sanchez@email.com', '555-100-2004',
 ST_SetSRID(ST_MakePoint(-70.6800, -33.4550), 4326)),

('Ana López', 'San Martín 202, La Serena',
 'ana.lopez@email.com', '555-100-2005',
 ST_SetSRID(ST_MakePoint(-70.6900, -33.4400), 4326)),

('Pedro Ramírez', 'Arturo Prat 303, Iquique',
 'pedro.ramirez@email.com', '555-100-2006',
 ST_SetSRID(ST_MakePoint(-70.7050, -33.4350), 4326)),

('Sofía Herrera', 'Los Carrera 404, Temuco',
 'sofia.herrera@email.com', '555-100-2007',
 ST_SetSRID(ST_MakePoint(-70.7150, -33.4300), 4326)),

('Diego Jiménez', 'Baquedano 505, Punta Arenas',
 'diego.jimenez@email.com', '555-100-2008',
 ST_SetSRID(ST_MakePoint(-70.7200, -33.4250), 4326)),

('Elena Castro', 'Portales 606, Viña del Mar',
 'elena.castro@email.com', '555-100-2009',
 ST_SetSRID(ST_MakePoint(-70.7300, -33.4200), 4326)),

('Ricardo Mora', 'Aníbal Pinto 707, Puerto Montt',
 'ricardo.mora@email.com', '555-100-2010',
 ST_SetSRID(ST_MakePoint(-70.7400, -33.4150), 4326)),

('Pepito Perez', 'Av. Manso de Velasco 456, Curicó',
 'pepito.perez@email.com', '555-100-2011',
 ST_SetSRID(ST_MakePoint(-71.2486, -34.9828), 4326));

-------------------------------
-- Insertar registros en la tabla repartidor
-------------------------------
INSERT INTO repartidor (nombre, telefono, disponible, ubicacion_actual) VALUES
('Fernando Silva', '+569 1234 5678', TRUE,
 ST_SetSRID(ST_MakePoint(-70.6500, -33.4450), 4326)),
('Camila Rojas', '+569 2345 6789', TRUE,
 ST_SetSRID(ST_MakePoint(-70.6550, -33.4500), 4326)),
('Pablo Mendoza', '+569 3456 7890', TRUE,
 ST_SetSRID(ST_MakePoint(-70.6600, -33.4550), 4326)),
('Valentina Díaz', '+569 4567 8901', TRUE,
 ST_SetSRID(ST_MakePoint(-70.6650, -33.4600), 4326)),
('Sebastián Torres', '+569 5678 9012', TRUE,
 ST_SetSRID(ST_MakePoint(-70.6700, -33.4650), 4326)),
('Daniela Vargas', '+569 6789 0123', TRUE,
 ST_SetSRID(ST_MakePoint(-70.6750, -33.4700), 4326)),
('Alejandro Soto', '+569 7890 1234', TRUE,
 ST_SetSRID(ST_MakePoint(-70.6800, -33.4750), 4326)),
('Javiera Núñez', '+569 8901 2345', TRUE,
 ST_SetSRID(ST_MakePoint(-70.6850, -33.4800), 4326)),
('Mauricio Lagos', '+569 9012 3456', TRUE,
 ST_SetSRID(ST_MakePoint(-70.6900, -33.4850), 4326)),
('Antonella Peña', '+569 0123 4567', TRUE,
 ST_SetSRID(ST_MakePoint(-70.6950, -33.4900), 4326));

-------------------------------
-- Insertar registros en la tabla EmpresaAsociada
-------------------------------
INSERT INTO empresaasociada (nombreEmpresa, direccionEmpresa, tipoServicio, ubicacion) VALUES
('Logística Chile S.A.', 'Av. Andrés Bello 2235, Providencia, Santiago',
 'Transporte de mercancías',
 ST_SetSRID(ST_MakePoint(-70.6480, -33.4560), 4326)),
('DelivExpress', 'Cerro El Plomo 5630, Las Condes, Santiago',
 'Mensajería urgente',
 ST_SetSRID(ST_MakePoint(-70.6600, -33.4600), 4326)),
('PuroCampo Distribuciones', 'Camino La Pólvora 1420, Valparaíso',
 'Productos agrícolas',
 ST_SetSRID(ST_MakePoint(-71.6000, -33.5000), 4326)),
('TecnoFast', 'Av. Nueva Tajamar 481, Torre Sur, Santiago',
 'Reparto tecnológico',
 ST_SetSRID(ST_MakePoint(-70.6700, -33.4650), 4326)),
('FarmaDelivery Chile', 'San Pascual 735, Concepción',
 'Medicamentos y productos médicos',
 ST_SetSRID(ST_MakePoint(-70.6800, -33.4550), 4326)),
('Sabores del Sur', 'O''Higgins 1023, Temuco',
 'Alimentos gourmet',
 ST_SetSRID(ST_MakePoint(-70.6900, -33.4400), 4326)),
('PescaRápida', 'Chacabuco 1240, Coquimbo',
 'Productos del mar',
 ST_SetSRID(ST_MakePoint(-70.7000, -33.4300), 4326)),
('Andina Logistic', 'Los Militares 5890, Las Condes, Santiago',
 'Carga internacional',
 ST_SetSRID(ST_MakePoint(-70.7100, -33.4250), 4326)),
('EcoEnvíos', 'Manuel Montt 1440, Puerto Montt',
 'Paquetería sustentable',
 ST_SetSRID(ST_MakePoint(-70.7200, -33.4200), 4326)),
('VinoExpress', 'Av. Vitacura 2932, Vitacura, Santiago',
 'Distribución de vinos premium',
 ST_SetSRID(ST_MakePoint(-70.7300, -33.4150), 4326));


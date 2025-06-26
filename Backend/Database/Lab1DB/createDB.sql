CREATE TABLE usuarios (
    id_usuario SERIAL PRIMARY KEY, -- ID autoincremental
    rut VARCHAR(12) NOT NULL, -- RUT del usuario
    name_param VARCHAR(100) NOT NULL, -- Nombre del usuario
    email VARCHAR(100) NOT NULL UNIQUE, -- Correo electrónico único
    phone VARCHAR(15), -- Teléfono del usuario
    birthdate DATE NOT NULL, -- Fecha de nacimiento
    password VARCHAR(255) NOT NULL, -- Contraseña (cifrada)
    role VARCHAR(50) NOT NULL -- Rol del usuario (ADMIN, TRABAJADOR, CLIENTE, etc.)
);
-- Creación de la tabla cliente
CREATE TABLE cliente (
    cliente_id SERIAL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    direccion TEXT DEFAULT 'Sin dirección',
    email VARCHAR(100) UNIQUE NOT NULL,
    telefono VARCHAR(20) NOT NULL
);

-- Creación de la tabla repartidor
CREATE TABLE repartidor (
    repartidor_id SERIAL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    telefono VARCHAR(20) NOT NULL,
    disponible BOOLEAN DEFAULT TRUE
);

CREATE TABLE EmpresaAsociada (
    idEmpresaAsociada SERIAL PRIMARY KEY,
    nombreEmpresa VARCHAR(255),
    direccionEmpresa VARCHAR(255),
    tipoServicio VARCHAR(100)
);

CREATE TABLE OrderEntity (
    idPedido SERIAL PRIMARY KEY,
    cliente_id INT REFERENCES Cliente(cliente_id) ON DELETE CASCADE,
	idEmpresaAsociada INT REFERENCES EmpresaAsociada(idEmpresaAsociada),
    repartidor_id INT REFERENCES Repartidor(repartidor_id),
    fechaPedido TIMESTAMP,
    fechaEntrega TIMESTAMP,
    estadoPedido VARCHAR(50),
    prioridadPedido VARCHAR(50)
);

CREATE TABLE DetallePedido (
    idDetallePedido SERIAL PRIMARY KEY,
    idPedido INT REFERENCES OrderEntity(idPedido) ON DELETE CASCADE,
    idServicio INT,
    cantidad INT,
    direccionDestino VARCHAR(255),
    direccionInicio VARCHAR(255)
);


CREATE TABLE Servicios (
    idServicio SERIAL PRIMARY KEY,
    idEmpresaAsociada INT REFERENCES EmpresaAsociada(idEmpresaAsociada) ON DELETE SET NULL,
    nombreServicio VARCHAR(255),
    descripcionServicio TEXT,
    precioServicio INT,
    categoriaServicio VARCHAR(100),
    stock INT
);

CREATE TABLE MedioDePago (
    idMetodoDePago SERIAL PRIMARY KEY,
    nombreMetodoDePago VARCHAR(100)
);

CREATE TABLE Pago (
    idPago SERIAL PRIMARY KEY,
    idPedido INT REFERENCES OrderEntity(idPedido) ON DELETE CASCADE,
    idMedioDePago INT REFERENCES MedioDePago(idMetodoDePago),
    monto DECIMAL(10,2)
);

CREATE TABLE Rating (
    idCalificacion SERIAL PRIMARY KEY,
    idPedido INT REFERENCES OrderEntity(idPedido) ON DELETE CASCADE,
    score INT CHECK (score BETWEEN 1 AND 5),
    comment TEXT
);
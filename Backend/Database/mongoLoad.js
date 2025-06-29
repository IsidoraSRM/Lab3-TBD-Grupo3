// ============================================
// ARCHIVO DE POBLACIÓN MONGODB - Lab3
// ============================================

// Conectar a la base de datos
use('Lab3');

// ============================================
// 1. OPINIONES DE CLIENTES
// ============================================
db.opiniones_clientes.insertMany([
    {
        cliente_id: "1",
        empresa_id: "1",
        comentario: "Excelente servicio, rápido y seguro.",
        puntuacion: 5,
        fecha: ISODate("2025-03-20T10:15:00.000Z")
    },
    {
        cliente_id: "2",
        empresa_id: "2",
        comentario: "Demora en la entrega y mal embalaje.",
        puntuacion: 2,
        fecha: ISODate("2025-03-21T14:22:00.000Z")
    },
    {
        cliente_id: "3",
        empresa_id: "1",
        comentario: "Servicio matutino excelente",
        puntuacion: 4,
        fecha: ISODate("2025-03-22T08:30:00.000Z")
    },
    {
        cliente_id: "4",
        empresa_id: "2",
        comentario: "Entrega nocturna tardía",
        puntuacion: 3,
        fecha: ISODate("2025-03-22T20:15:00.000Z")
    },
    {
        cliente_id: "5",
        empresa_id: "1",
        comentario: "Perfecto desayuno entregado",
        puntuacion: 5,
        fecha: ISODate("2025-03-23T07:45:00.000Z")
    },
    {
        cliente_id: "6",
        empresa_id: "2",
        comentario: "Almuerzo llegó frío",
        puntuacion: 2,
        fecha: ISODate("2025-03-23T13:20:00.000Z")
    },
    {
        cliente_id: "7",
        empresa_id: "1",
        comentario: "Cena perfecta",
        puntuacion: 4,
        fecha: ISODate("2025-03-23T19:30:00.000Z")
    },
    // Datos adicionales para mejores análisis
    {
        cliente_id: "8",
        empresa_id: "1",
        comentario: "Delivery muy rápido en la mañana",
        puntuacion: 5,
        fecha: ISODate("2025-03-24T09:00:00.000Z")
    },
    {
        cliente_id: "9",
        empresa_id: "2",
        comentario: "Servicio regular, puede mejorar",
        puntuacion: 3,
        fecha: ISODate("2025-03-24T15:30:00.000Z")
    },
    {
        cliente_id: "10",
        empresa_id: "1",
        comentario: "Excelente atención al cliente",
        puntuacion: 5,
        fecha: ISODate("2025-03-24T18:45:00.000Z")
    }
])

// ============================================
// 2. HISTORIAL DE REPARTIDORES
// ============================================
db.historial_repartidores.insertMany([
    {
        repartidor_id: "R001",
        rutas: [
            {
                lat: -33.4489,
                lng: -70.6693,
                timestamp: ISODate("2025-03-20T09:45:00.000Z")
            },
            {
                lat: -33.45,
                lng: -70.67,
                timestamp: ISODate("2025-03-20T10:00:00.000Z")
            },
            {
                lat: -33.4512,
                lng: -70.6705,
                timestamp: ISODate("2025-03-20T10:15:00.000Z")
            }
        ]
    },
    {
        repartidor_id: "R002",
        rutas: [
            {
                lat: -33.4372,
                lng: -70.6506,
                timestamp: ISODate("2025-03-21T11:00:00.000Z")
            },
            {
                lat: -33.4385,
                lng: -70.6520,
                timestamp: ISODate("2025-03-21T11:15:00.000Z")
            },
            {
                lat: -33.4398,
                lng: -70.6535,
                timestamp: ISODate("2025-03-21T11:30:00.000Z")
            }
        ]
    },
    {
        repartidor_id: "R003",
        rutas: [
            {
                lat: -33.4150,
                lng: -70.6083,
                timestamp: ISODate("2025-03-22T14:00:00.000Z")
            },
            {
                lat: -33.4165,
                lng: -70.6095,
                timestamp: ISODate("2025-03-22T14:20:00.000Z")
            }
        ]
    },

    {
    repartidor_id: '1',
    rutas: [
      {
        lat: -33.4489,
        lng: -70.6693,
        timestamp: new Date(Date.now() - 2 * 24 * 60 * 60 * 1000) // hace 2 días
      },
      {
        lat: -33.45,
        lng: -70.67,
        timestamp: new Date(Date.now() - 2 * 24 * 60 * 60 * 1000 + 15 * 60 * 1000) // +15 min
      },
      {
        lat: -33.4512,
        lng: -70.6705,
        timestamp: new Date(Date.now() - 2 * 24 * 60 * 60 * 1000 + 30 * 60 * 1000) // +30 min
      }
    ]
  },

  {
    repartidor_id: '4',
    rutas: [
      {
        lat: -33.4372,
        lng: -70.6506,
        timestamp: new Date(Date.now() - 1 * 24 * 60 * 60 * 1000) // hace 1 día
      },
      {
        lat: -33.4385,
        lng: -70.652,
        timestamp: new Date(Date.now() - 1 * 24 * 60 * 60 * 1000 + 15 * 60 * 1000)
      },
      {
        lat: -33.4398,
        lng: -70.6535,
        timestamp: new Date(Date.now() - 1 * 24 * 60 * 60 * 1000 + 30 * 60 * 1000)
      }
    ]
  },

  {
    repartidor_id: '5',
    rutas: [
      {
        lat: -33.415,
        lng: -70.6083,
        timestamp: new Date(Date.now() - 3 * 24 * 60 * 60 * 1000) // hace 3 días
      },
      {
        lat: -33.4165,
        lng: -70.6095,
        timestamp: new Date(Date.now() - 3 * 24 * 60 * 60 * 1000 + 20 * 60 * 1000)
      }
    ]
  }
])

// ============================================
// 3. NAVEGACIÓN DE USUARIOS
// ============================================
db.navegacion_usuarios.insertMany([
    {
        cliente_id: "1",
        eventos: [
            {
                tipo: "busqueda",
                valor: "paracetamol",
                timestamp: ISODate("2025-03-20T09:05:00.000Z")
            },
            {
                tipo: "click",
                valor: "producto_123",
                timestamp: ISODate("2025-03-20T09:06:00.000Z")
            },
            {
                tipo: "compra",
                valor: "producto_123",
                timestamp: ISODate("2025-03-20T09:10:00.000Z")
            }
        ]
    },
    {
        cliente_id: "2",
        eventos: [
            {
                tipo: "busqueda",
                valor: "aspirina",
                timestamp: ISODate("2025-03-21T10:00:00.000Z")
            },
            {
                tipo: "click",
                valor: "producto_456",
                timestamp: ISODate("2025-03-21T10:02:00.000Z")
            },
            {
                tipo: "click",
                valor: "producto_789",
                timestamp: ISODate("2025-03-21T10:05:00.000Z")
            },
            {
                tipo: "compra",
                valor: "producto_789",
                timestamp: ISODate("2025-03-21T10:08:00.000Z")
            }
        ]
    },
    {
        cliente_id: "3",
        eventos: [
            {
                tipo: "busqueda",
                valor: "vitaminas",
                timestamp: ISODate("2025-03-22T08:00:00.000Z")
            },
            {
                tipo: "filtro",
                valor: "categoria_salud",
                timestamp: ISODate("2025-03-22T08:02:00.000Z")
            },
            {
                tipo: "click",
                valor: "producto_101",
                timestamp: ISODate("2025-03-22T08:05:00.000Z")
            }
        ]
    },
    {
        cliente_id: "4",
        eventos: [
            {
                tipo: "busqueda",
                valor: "antibioticos",
                timestamp: ISODate("2025-03-22T19:00:00.000Z")
            },
            {
                tipo: "click",
                valor: "producto_202",
                timestamp: ISODate("2025-03-22T19:02:00.000Z")
            }
        ]
    }
])

// ============================================
// 4. LOGS DE PEDIDOS
// ============================================
db.logs_pedidos.insertMany([
    {
        pedido_id: "P001",
        eventos: [
            {
                estado: "creado",
                timestamp: ISODate("2025-03-20T09:10:00.000Z"),
                detalle: "Pedido creado por cliente C001"
            },
            {
                estado: "confirmado",
                timestamp: ISODate("2025-03-20T09:15:00.000Z"),
                detalle: "Pedido confirmado por farmacia"
            },
            {
                estado: "en preparacion",
                timestamp: ISODate("2025-03-20T09:30:00.000Z"),
                detalle: "Preparando medicamentos"
            },
            {
                estado: "listo",
                timestamp: ISODate("2025-03-20T09:45:00.000Z"),
                detalle: "Pedido listo para entrega"
            },
            {
                estado: "en reparto",
                timestamp: ISODate("2025-03-20T09:50:00.000Z"),
                detalle: "Asignado a repartidor R001"
            },
            {
                estado: "entregado",
                timestamp: ISODate("2025-03-20T10:15:00.000Z"),
                detalle: "Entregado satisfactoriamente"
            }
        ]
    },
    {
        pedido_id: "P002",
        eventos: [
            {
                estado: "creado",
                timestamp: ISODate("2025-03-21T13:00:00.000Z"),
                detalle: "Pedido creado por cliente C002"
            },
            {
                estado: "confirmado",
                timestamp: ISODate("2025-03-21T13:10:00.000Z"),
                detalle: "Pedido confirmado"
            },
            {
                estado: "en preparacion",
                timestamp: ISODate("2025-03-21T13:30:00.000Z"),
                detalle: "Preparando pedido"
            },
            {
                estado: "listo",
                timestamp: ISODate("2025-03-21T14:00:00.000Z"),
                detalle: "Pedido listo"
            },
            {
                estado: "en reparto",
                timestamp: ISODate("2025-03-21T14:10:00.000Z"),
                detalle: "Asignado a repartidor R002"
            },
            {
                estado: "entregado",
                timestamp: ISODate("2025-03-21T14:22:00.000Z"),
                detalle: "Entregado con retraso"
            }
        ]
    },
    {
        pedido_id: "P003",
        eventos: [
            {
                estado: "creado",
                timestamp: ISODate("2025-03-22T08:15:00.000Z"),
                detalle: "Pedido creado por cliente C003"
            },
            {
                estado: "confirmado",
                timestamp: ISODate("2025-03-22T08:20:00.000Z"),
                detalle: "Pedido confirmado"
            },
            {
                estado: "en preparacion",
                timestamp: ISODate("2025-03-22T08:25:00.000Z"),
                detalle: "Preparando pedido matutino"
            },
            {
                estado: "listo",
                timestamp: ISODate("2025-03-22T08:28:00.000Z"),
                detalle: "Pedido listo rápidamente"
            },
            {
                estado: "en reparto",
                timestamp: ISODate("2025-03-22T08:29:00.000Z"),
                detalle: "Asignado a repartidor R001"
            },
            {
                estado: "entregado",
                timestamp: ISODate("2025-03-22T08:30:00.000Z"),
                detalle: "Entrega express exitosa"
            }
        ]
    },
    {
        pedido_id: "P004",
        eventos: [
            {
                estado: "creado",
                timestamp: ISODate("2025-03-22T19:00:00.000Z"),
                detalle: "Pedido creado por cliente C004"
            },
            {
                estado: "confirmado",
                timestamp: ISODate("2025-03-22T19:05:00.000Z"),
                detalle: "Pedido nocturno confirmado"
            },
            {
                estado: "en preparacion",
                timestamp: ISODate("2025-03-22T19:30:00.000Z"),
                detalle: "Preparación nocturna lenta"
            },
            {
                estado: "listo",
                timestamp: ISODate("2025-03-22T20:00:00.000Z"),
                detalle: "Pedido listo con demora"
            },
            {
                estado: "en reparto",
                timestamp: ISODate("2025-03-22T20:10:00.000Z"),
                detalle: "Asignado a repartidor R003"
            },
            {
                estado: "entregado",
                timestamp: ISODate("2025-03-22T20:15:00.000Z"),
                detalle: "Entregado fuera del horario óptimo"
            }
        ]
    },
    {
        pedido_id: "P005",
        eventos: [
            {
                estado: "creado",
                timestamp: ISODate("2025-03-23T12:00:00.000Z"),
                detalle: "Pedido creado por cliente C006"
            },
            {
                estado: "confirmado",
                timestamp: ISODate("2025-03-23T12:05:00.000Z"),
                detalle: "Pedido confirmado"
            },
            {
                estado: "cancelado",
                timestamp: ISODate("2025-03-23T12:30:00.000Z"),
                detalle: "Producto no disponible - Pedido cancelado"
            }
        ]
    }
])

// ============================================
// VERIFICACIÓN DE DATOS INSERTADOS
// ============================================
print("=== RESUMEN DE DATOS INSERTADOS ===")
print("Opiniones de clientes: " + db.opiniones_clientes.countDocuments())
print("Historial de repartidores: " + db.historial_repartidores.countDocuments())
print("Navegación de usuarios: " + db.navegacion_usuarios.countDocuments())
print("Logs de pedidos: " + db.logs_pedidos.countDocuments())
print("=== POBLACIÓN COMPLETA ===")
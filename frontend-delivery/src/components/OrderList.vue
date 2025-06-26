<template>
  <div class="order-list-container">
    <!-- Estado de carga -->
    <div v-if="loading" class="loading">Cargando pedidos...</div>
    
    <!-- Mensaje de error -->
    <div v-else-if="error" class="error">{{ error }}</div>
    
    <!-- Tabla de pedidos -->
    <div v-else class="table-container">
      <table v-if="viewMode === 'repartidor' || viewMode === 'usuario'" class="orders-table">
        <thead>
          <tr>
            <th>ID Pedido</th>
            <th>Cliente</th>
            <th>Estado</th>
            <th>Origen</th>
            <th>Destino</th>
            <th>Fecha Pedido</th>
            <th>Fecha Entrega</th>
            <th>Acciones</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="(item, index) in filteredOrders" :key="'order-' + index" class="order-row">
            <td>{{ item.pedido?.idPedido || 'N/A' }}</td>
            <td>{{ item.cliente?.nombre || 'N/A' }}</td>
            <td>
              <span v-if="item.pedido?.estadoPedido" class="status-badge" :class="item.pedido.estadoPedido.toLowerCase()">
                {{ formatStatus(item.pedido.estadoPedido) }}
              </span>
              <span v-else>N/A</span>
            </td>
            <td>{{ item.detallePedido?.direccionInicio || 'N/A' }}</td>
            <td>{{ item.detallePedido?.direccionDestino || 'N/A' }}</td>
            <td>{{ item.pedido?.fechaPedido ? formatDate(item.pedido.fechaPedido) : 'N/A' }}</td>
            <td>{{ item.pedido?.fechaEntrega ? formatDate(item.pedido.fechaEntrega) : 'N/A' }}</td>
            <td>
              <button 
                    class="action-button confirm-button margin-right" 
                    @click="confirmarPedido(item.pedido?.idPedido)"
                    :disabled="pedidoEnProceso === item.pedido?.idPedido"
                    >
                    {{ pedidoEnProceso === item.pedido?.idPedido ? 'Confirmando...' : 'Confirmar Pedido' }}
                </button>
              <button class="action-button confirm-button" @click="delivered(item.pedido?.idPedido,'ENTREGADO')">
                Entregado                
              </button>
            </td>
          </tr>
        </tbody>
      </table>
      
      <div v-if="filteredOrders.length === 0 && !loading" class="no-orders">
        No se encontraron pedidos
      </div>
    </div>
  </div>
</template>

<script>
import orderService from '../services/orderService';

export default {
  name: 'OrderList',
  props: {
    viewMode: {
      type: String,
      default: 'normal' // 'normal' o 'repartidor' o 'usuario'
    },
    repartidorId: {
      type: Number,
      default: null
    },
    userId: {
      type: Number,
      default: null
    }
  },
  data() {
    return {
      orders: [],
      filteredOrders: [],
      loading: true,
      error: null,
      selectedStatus: '',
      statusOptions: ['PENDIENTE', 'EN_CAMINO', 'ENTREGADO', 'CANCELADO'],
      pedidoEnProceso: null
    };
  },
  created() {
    this.fetchOrders();
  },
  methods: {
    // Método mejorado para la obtención de pedidos
    fetchOrders() {
    this.loading = true;
    this.error = null;
    
    let fetchPromise;
    
    if (this.viewMode === 'repartidor' && this.repartidorId) {
        fetchPromise = orderService.getPedidosByRepartidor(this.repartidorId);
    } else if (this.viewMode === 'usuario' && this.userId) {
        fetchPromise = orderService.getPedidosByUserId(this.userId);
    } else {
        fetchPromise = orderService.getAllOrders();
    }
    
    fetchPromise
        .then(response => {
        if (!response || !response.data) {
            throw new Error('No se recibieron datos válidos de la API');
        }
        
        console.log('Datos recibidos:', response.data);
        
        // Asignar datos de forma segura
        const rawData = Array.isArray(response.data) ? response.data : [];
        
        // Transformar los datos planos en la estructura anidada que espera el componente
        this.orders = rawData.map(item => {
            // Si los datos vienen como objeto plano (como muestra el ejemplo)
            if (item.idpedido && !item.pedido) {
            return {
                pedido: {
                idPedido: item.idpedido,
                clienteId: item.cliente_id,
                repartidorId: item.repartidor_id,
                fechaPedido: item.fechapedido,
                fechaEntrega: item.fechaentrega,
                estadoPedido: item.estadopedido,
                prioridadPedido: item.prioridadpedido
                },
                cliente: {
                nombre: item.nombre || 'Cliente no disponible',
                direccion: item.direccion || 'Sin dirección',
                email: item.email,
                telefono: item.telefono
                },
                detallePedido: {
                idDetallePedido: item.iddetallepedido,
                idServicio: item.idservicio,
                cantidad: item.cantidad,
                direccionDestino: item.direcciondestino,
                direccionInicio: item.direccioninicio
                }
            };
            }
            
            // Si ya viene con la estructura esperada
            if (item.pedido && item.cliente) {
            return item;
            }
            
            // Si es un objeto plano de OrderEntity (desde getAllOrders)
            return {
            pedido: item,
            cliente: { nombre: 'Cliente no disponible' },
            detallePedido: {}
            };
        });
        
        // Aplicar filtros
        this.applyFilters();
        
        this.loading = false;
        })
        .catch(error => {
        console.error('Error fetching orders:', error);
        this.error = 'Error al cargar los pedidos. Por favor, intente nuevamente.';
        this.filteredOrders = [];
        this.loading = false;
        });
    },

    // Método separado para aplicar filtros
    applyFilters() {
    if (!this.orders || !Array.isArray(this.orders)) {
        this.filteredOrders = [];
        return;
    }
    
    if (this.selectedStatus) {
        if (this.viewMode === 'repartidor' || this.viewMode === 'usuario') {
        this.filteredOrders = this.orders.filter(item => 
            item.pedido && item.pedido.estadoPedido === this.selectedStatus
        );
        } else {
        this.filteredOrders = this.orders.filter(order => 
            order.estadoPedido === this.selectedStatus
        );
        }
    } else {
        this.filteredOrders = this.orders;
    }
    },

    // Métodos de formato que manejan valores nulos
    formatStatus(status) {
    if (!status) return 'Desconocido';
    
    const statusMap = {
        'PENDIENTE': 'Pendiente',
        'EN_CAMINO': 'En camino',
        'ENTREGADO': 'Entregado',
        'CANCELADO': 'Cancelado',
        'CONFIRMADO': 'Confirmado'
    };
    
    return statusMap[status] || status;
    },

    formatDate(dateString) {
      if (!dateString) return 'N/A';
      
      try {
        const date = new Date(dateString);
        // Usar toLocaleString para mostrar fecha Y hora
        return date.toLocaleString('es-CL', {
          year: 'numeric', 
          month: '2-digit', 
          day: '2-digit',
          hour: '2-digit',
          minute: '2-digit'
        });
      } catch (e) {
        return 'Fecha inválida';
      }
    },// Añadir este método en la sección "methods" del componente
    confirmarPedido(idPedido) {
    if (!idPedido) {
      this.error = 'No se puede confirmar: ID de pedido no válido';
      return;
    }
    
    // Mostrar confirmación antes de proceder
    if (!confirm('¿Estás seguro de que deseas confirmar este pedido?')) {
      return;
    }
    
    // Indicador de carga para este pedido específico
    this.pedidoEnProceso = idPedido;
    
    orderService.confirmarPedido(idPedido)
      .then(() => { // Quitar el parámetro 'response' no utilizado
        // Mostrar mensaje de éxito
        alert('Pedido confirmado exitosamente');
        
        // Recargar la lista de pedidos para reflejar el cambio
        this.fetchOrders();
      })
      .catch(error => {
        console.error('Error al confirmar pedido:', error);
        this.error = 'Error al confirmar el pedido. Inténtalo de nuevo.';
      })
      .finally(() => {
        this.pedidoEnProceso = null; // Limpiar el indicador de carga
      });
    },
    delivered(idPedido, estado) {
      if (!idPedido) {
        this.error = 'No se puede confirmar: ID de pedido no válido';
        return;
      }
      
      // Mostrar confirmación antes de proceder
      if (!confirm('¿Estás seguro de que deseas marcar este pedido como entregado?')) {
        return;
      }
      
      orderService.cambiarEstadoPedido(idPedido, estado)
        .then(() => { // Quitar el parámetro 'response' no utilizado
          // Mostrar mensaje de éxito
          alert('Pedido marcado como entregado exitosamente');
          
          // Recargar la lista de pedidos para reflejar el cambio
          this.fetchOrders();
        })
        .catch(error => {
          console.error('Error al marcar pedido como entregado:', error);
          this.error = 'Error al marcar el pedido como entregado. Inténtalo de nuevo.';
        });
    }
  }
}
</script>

<style scoped>
.table-container {
  overflow-x: auto;
}

.orders-table {
  width: 100%;
  border-collapse: collapse;
  margin: 20px 0;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
}

.orders-table th,
.orders-table td {
  padding: 12px 15px;
  text-align: left;
  border-bottom: 1px solid #E2DCD2;
  background-color:#f8f9fa ;
}

.orders-table th {
  background-color: #f8f9fa;
  font-weight: 600;
}

.status-badge {
  padding: 4px 8px;
  border-radius: 12px;
  font-size: 0.85em;
  font-weight: 500;
}

.status-badge.pendiente_con {
  background-color: #f8f9fa;
  color: #ef6c00;
}

.status-badge.en_camino {
  background-color: #e3f2fd;
  color: #1976d2;
}

.status-badge.entregado {
  background-color: #e8f5e9;
  color: #2e7d32;
}
.status-badge.confirmado {
  background-color: #e8f0fe;
  color: #3f51b5; /* Color púrpura/azul oscuro */
}

.priority-tag {
  padding: 4px 8px;
  border-radius: 4px;
  font-size: 0.85em;
}

.priority-tag.alta {
  background-color: #f8f9fa;
  color: #c62828;
}

.priority-tag.media {
  background-color: #f8f9fa;
  color: #ef6c00;
}

.priority-tag.baja {
  background-color: #f5f5f5;
  color: #616161;
}

.action-button {
  padding: 6px 12px;
  background-color: #2196f3;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  transition: background-color 0.3s;
}

.action-button:hover {
  background-color: #1976d2;
}

.status-filter {
  margin-bottom: 20px;
  padding: 8px 12px;
  border: 1px solid #ddd;
  border-radius: 4px;
}

.no-orders {
  text-align: center;
  padding: 20px;
  color: #666;
}

.action-button:disabled {
  background-color: #cccccc;
  cursor: not-allowed;
  opacity: 0.7;
  
}

.confirm-button {
  background-color: #4CAF50;
  width: 180px; /* Ancho fijo */
  white-space: normal; /* Permite saltos de línea */
  height: auto; /* Altura ajustable al contenido */
  line-height: 1.2; /* Espacio entre líneas */
  padding: 8px 12px;
}

.confirm-button:hover:not(:disabled) {
  background-color: #45a049;
}
.margin-right {
  margin-right: 10px; /* Ajusta este valor según necesites */
}

.order-list {
  background-color: var(--bg-secondary);
  border-radius: 8px;
  padding: 1.5rem;
  border: 1px solid var(--border-blue);
}

.order-card {
  background-color: var(--card-bg);
  border: 1px solid var(--border-blue);
  border-radius: 8px;
  padding: 1.5rem;
  margin-bottom: 1rem;
  transition: transform 0.3s ease, box-shadow 0.3s ease;
}

.order-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 6px var(--blue-glow);
}

.order-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 1rem;
  padding-bottom: 0.5rem;
  border-bottom: 1px solid var(--border-blue);
}

.order-title {
  color: var(--text-primary);
  font-size: 1.25rem;
  font-weight: bold;
}

.order-status {
  padding: 0.5rem 1rem;
  border-radius: 20px;
  font-size: 0.875rem;
}

.status-pending {
  background-color: var(--dark-gray);
  color: var(--text-secondary);
}

.status-processing {
  background-color: var(--primary-blue);
  color: var(--text-primary);
}

.status-completed {
  background-color: var(--blue-neon);
  color: var(--text-primary);
}

.order-details {
  color: var(--text-secondary);
  margin: 1rem 0;
}

.order-actions {
  display: flex;
  gap: 1rem;
  margin-top: 1rem;
}

.action-button {
  background-color: var(--primary-blue);
  color: var(--text-primary);
  padding: 0.5rem 1rem;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  transition: background-color 0.3s;
}

.action-button:hover {
  background-color: var(--primary-blue-hover);
}

.action-button.secondary {
  background-color: var(--dark-gray);
  color: var(--text-secondary);
}

.action-button.secondary:hover {
  background-color: var(--bg-secondary);
}
</style>
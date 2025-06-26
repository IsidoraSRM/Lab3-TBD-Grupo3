<template>
  <div class="client-summary">
    <div v-if="loading" class="loading-indicator">
      <i class="fas fa-spinner fa-spin"></i> Cargando datos de clientes...
    </div>
    <div v-else-if="error" class="error-message">
      {{ error }}
    </div>
    <div v-else>
      <div class="results-header">
        <h3>Resumen de Clientes</h3>
      </div>
      <div class="table-responsive">
        <table class="results-table">
          <thead>
            <tr>
              <th>Cliente</th>
              <th>Email</th>
              <th>Total Pedidos</th>
              <th>Monto Total Gastado</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="(cliente, index) in clientes" :key="index">
              <td class="centered">{{ cliente.nombre }}</td>
              <td class="centered">{{ cliente.email }}</td>
              <td class="centered">{{ cliente.totalPedidos }}</td>
              <td class="centered">{{ formatearMonto(cliente.montoTotal) }}</td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>
  </div>
</template>

<script>
import summariesService from '@/services/summariesService';

export default {
  name: 'ClientSummary',
  data() {
    return {
      clientes: [],
      loading: true,
      error: null
    };
  },
  created() {
    this.fetchClientesSummary();
  },
  methods: {
    async fetchClientesSummary() {
      this.loading = true;
      try {
        const response = await summariesService.getAllSummaries();
        console.log('Resumen Cliente:', response.data);
        
        // Mapeo de los datos de la API a la estructura que espera el componente
        this.clientes = response.data.map(item => {
          return {
            id: item.clienteId,
            nombre: item.nombreCliente,
            email: item.emailCliente,
            totalPedidos: item.totalPedidos,
            montoTotal: item.montoTotalGastado
          };
        });
        
      } catch (error) {
        console.error('Error al cargar el resumen de clientes:', error);
        this.error = 'No se pudo cargar la información de clientes. Inténtelo de nuevo.';
        
        // Datos de ejemplo como fallback
        this.clientes = [
          { nombre: 'Cliente Ejemplo', email: 'ejemplo@email.com', totalPedidos: 10, montoTotal: 150000 }
        ];
      } finally {
        this.loading = false;
      }
    },
    formatearMonto(monto) {
      if (monto === undefined || monto === null) return 'N/A';
      // Formatear como moneda CLP
      return '$' + monto.toLocaleString('es-CL');
    }
  }
};
</script>

<style scoped>
.client-summary {
  width: 100%;
}

.loading-indicator {
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 40px;
  color: #7f8c8d;
  gap: 10px;
}

.error-message {
  padding: 20px;
  background-color: #ffebee;
  color: #c62828;
  border-radius: 4px;
  text-align: center;
}

.results-table {
  width: 100%;
  border-collapse: collapse;
}

.results-table th,
.results-table td {
  padding: 12px 15px;
  border-bottom: 1px solid #eee;
}
.centered {
  text-align: center;
}

.results-table th {
  background: #f8f9fa;
  font-weight: 600;
  color: #34495e;
}

.results-table tr:hover td {
  background: #f5f7fa;
}

.results-table tr:last-child td {
  border-bottom: none;
}

/* Barra de progreso para puntualidad */
.progress-bar {
  height: 8px;
  background-color: #ecf0f1;
  border-radius: 4px;
  overflow: hidden;
  margin-bottom: 5px;
  width: 100px;
}

.progress-fill {
  height: 100%;
  transition: width 0.3s ease;
}
</style>
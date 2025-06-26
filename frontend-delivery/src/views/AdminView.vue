<template>
  <div class="admin-dashboard">
    <!-- Barra superior -->
    <div class="admin-header">
      <div class="header-left">
        <h1>Panel de Administración</h1>
        <p class="welcome-message">Bienvenido, Administrador</p>
      </div>
      <div class="header-right">
        <button class="refresh-btn" @click="fetchData">
          <i class="fas fa-sync-alt"></i> Actualizar
        </button>
        <span class="last-updated">Última actualización: {{ lastUpdated }}</span>
      </div>
    </div>
    <!-- Sección principal -->
    <div class="main-content">
      <!-- Consultas SQL -->
      <div class="card query-section">
        <div class="card-header">
          <h2>Consultas Analíticas</h2>
          <select v-model="selectedQuery" class="query-select">
            <option value="" disabled>Seleccione una consulta</option>
            <option value="1">[LAB 1] Cliente que más ha gastado en pedidos entregados.</option>
            <option value="2">[LAB 1] Productos más pedidos por categoría</option>
            <option value="3">[LAB 1] Listar las empresas asociadas con más entregas fallidas.</option>
            <option value="4">[LAB 1] Calcular el tiempo promedio entre pedido y entrega por repartidor.</option>
            <option value="5">[LAB 1] Obtener los 3 repartidores con mejor rendimiento (basado en entregas y puntuación).</option>
            <option value="6">[LAB 1] Obtener el medio de pago más usado en pedidos urgentes</option>
            <option value="7">[LAB 2] 5 puntos de entrega más cercanos a la empresa</option>
            <option value="8">[LAB 2] Punto de entrega más lejano desde cada empresa</option>
            <option value="9">[LAB 2] Pedidos cuya ruta cruza más de 2 zonas</option>
            <option value="12">[LAB 2] Clientes a más de 5km de cualquier empresa/farmacia</option>
            <option value="13">[LAB 2] ¿Cliente está en zona de cobertura? (buffer 1km)</option>
            <option value="14">[LAB 2] Distancia recorrida por repartidor en el último mes</option>
            <option value="10">[EXTRA] Zona a la que pertenece un cliente</option>
            <option value="11">[EXTRA] Zonas con alta densidad de pedidos</option>
          </select>
          <input v-if="selectedQuery === '7'" v-model="empresaInput" placeholder="Nombre de la empresa" class="empresa-input" style="margin-left: 1rem; min-width: 200px;" />
          <input v-if="selectedQuery === '10' || selectedQuery === '13'" v-model="clienteIdInput" placeholder="ID del cliente" class="empresa-input" style="margin-left: 1rem; min-width: 200px;" />
          <button class="btn-run-query" @click="runQuery" :disabled="!selectedQuery || (selectedQuery === '7' && !empresaInput) || (selectedQuery === '10' && !clienteIdInput)">
            Ejecutar
          </button>
        </div>
        <div class="card-body">
          <div v-if="queryLoading" class="loading-indicator">
            <i class="fas fa-spinner fa-spin"></i> Procesando consulta...
          </div>
          <div v-else-if="queryResults" class="query-results">
            <div class="results-header">
              <h3>{{ queryTitle }}</h3>
            </div>
            <div class="table-responsive">
              <table class="results-table">
                <thead>
                  <tr>
                    <th v-for="(header, index) in queryHeaders" :key="index">{{ header }}</th>
                  </tr>
                </thead>
                <tbody>
                  <tr v-for="(row, rowIndex) in queryResults" :key="rowIndex">
                    <td v-for="(cell, cellIndex) in row" :key="cellIndex">{{ cell }}</td>
                  </tr>
                </tbody>
              </table>
            </div>
          </div>
          <div v-else class="empty-state">
            <i class="fas fa-chart-bar"></i>
            <p>Seleccione y ejecute una consulta para ver los resultados</p>
          </div>
        </div>
      </div>

      <!-- Vistas SQL como estadísticas clave -->
      <div class="card stats-section">
    <div class="card-header">
      <h2>Vistas SQL Clave</h2>
      <select v-model="selectedView" class="query-select" @change="loadView">
        <option value="" disabled>Seleccione una vista</option>
        <option value="13">Resumen de pedidos por cliente</option>
        <option value="14">Desempeño por repartidor</option>
        <option value="15">Empresas con mayor volumen</option>
      </select>
    </div>
    <div class="card-body">
      <div v-if="viewLoading" class="loading-indicator">
        <i class="fas fa-spinner fa-spin"></i> Cargando vista...
      </div>
      
      <!-- Caso específico para Desempeño por repartidor -->
      <RepartidorPerformanceTable v-else-if="selectedView === '14'" />
      <ClientSummary v-else-if="selectedView === '13'" />
      <TopCompany v-else-if="selectedView === '15'" />
      <!-- Otras vistas -->
      <div v-else-if="viewResults" class="query-results">
        <div class="results-header">
          <h3>{{ viewTitle }}</h3>
        </div>
        <div class="table-responsive">
          <table class="results-table">
            <thead>
              <tr>
                <th v-for="(header, index) in viewHeaders" :key="index">{{ header }}</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="(row, rowIndex) in viewResults" :key="rowIndex">
                <td v-for="(cell, cellIndex) in row" :key="cellIndex">{{ cell }}</td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>
      <div v-else class="empty-state">
        <i class="fas fa-table"></i>
        <p>Seleccione una vista SQL para ver los resultados</p>
      </div>
    </div>
  </div>
    </div>
  </div>
</template>

<script>
import pagoService from '@/services/pagoService'; 
import detallePedidoService from '@/services/detallePedidoService'; 
import repartidorService from '@/services/repartidorService';
import clienteService from '@/services/clienteService';
import RepartidorPerformanceTable from '@/components/PerformanceDist.vue';
import ClientSummary from '@/components/ClientSummary.vue';
import TopCompany from '@/components/TopCompany.vue';
import orderService from '@/services/orderService';
import empresaService from '@/services/empresaService';


export default {
  name: 'AdminView',
  components: {
    RepartidorPerformanceTable,
    ClientSummary,
    TopCompany
  },
  data() {
    return {
      lastUpdated: new Date().toLocaleTimeString(),
      stats: {
        ordersToday: 42,
        ordersChange: 5.3,
        newCustomers: 8,
        pendingDeliveries: 15,
        reportedIssues: 3,
        issuesChange: -2.1
      },
      selectedQuery: '',
      queryLoading: false,
      queryResults: null,
      queryTitle: '',
      queryHeaders: [],
      selectedView: '',
      viewLoading: false,
      viewResults: null,
      viewTitle: '',
      viewHeaders: [],
      notifications: [
        {
          id: 1,
          type: 'warning',
          icon: 'fas fa-exclamation-circle',
          text: 'Pedido #1045 retrasado - Problema con el repartidor',
          time: 'Hace 15 min',
          read: false
        },
        {
          id: 2,
          type: 'success',
          icon: 'fas fa-check-circle',
          text: 'Nuevo registro de cliente premium',
          time: 'Hace 1 hora',
          read: true
        }
      ],
      empresaInput: '',
      clienteIdInput: '',
    }
  },
  methods: {
    
    fetchData() {
      this.lastUpdated = new Date().toLocaleTimeString()
    },
    async runQuery() {
      this.queryLoading = true;
      this.queryResults = null;
      this.queryError = null;

      try {
        if (this.selectedQuery === '1') { // Cliente que más ha gastado
          const response = await clienteService.getClienteQueMasHaGastado();
          console.log('Respuesta del backend:', response.data);

          this.queryTitle = 'Cliente que más ha gastado';
          this.queryHeaders = ['Nombre', 'Email', 'Total Gastado'];

          // Maneja el Optional de Java (puede ser null)
          if (response.data) {
            this.queryResults = [[
              response.data.nombre,
              response.data.email,
              `$${response.data.totalGastado.toLocaleString('es-CL')}` // Formato chileno
            ]];
          } else {
            this.queryResults = [['No se encontraron datos', '', '']];
          }
        }

        else if (this.selectedQuery === '2') {
          const response = await detallePedidoService.getMasPedidosPorCategoria();
          console.log('Respuesta del backend:', response.data);

          this.queryTitle = 'Productos más pedidos por categoría';
          this.queryHeaders = ['Servicio', 'Categoría', 'Cantidad de pedidos'];

          this.queryResults = response.data.map(item => [
            item.servicio,
            item.categoria,
            item.cantidadPedidos
          ]);
        }

        else if (this.selectedQuery === '3') { 
          const response = await empresaService.getEmpresaConMasEntregasFallidas();
          console.log('Respuesta del backend:', response.data);

          this.queryTitle = 'Empresas con más entregas fallidas';
          this.queryHeaders = ['Empresa', 'Entregas Fallidas'];

          this.queryResults = response.data.map(item => [
            item.nombreempresa || 'Empresa no identificada',
            item.entregas_fallidas || 0
          ]);
          
          this.queryResults.sort((a, b) => b[1] - a[1]);
        }




        else if (this.selectedQuery === '4') { // Tiempo promedio por repartidor
          const response = await orderService.getTiempoPromedioEntregaPorRepartidor();
          console.log('Respuesta del backend:', response.data);

          this.queryTitle = 'Tiempo promedio de entrega por repartidor';
          this.queryHeaders = ['Repartidor', 'Tiempo Promedio'];

          // Función para formatear el tiempo (maneja valores negativos)
          const formatTime = (minutes) => {
            const absMinutes = Math.abs(minutes);
            const hours = Math.floor(absMinutes / 60);
            const mins = Math.floor(absMinutes % 60);
            return `${hours}h ${mins}m` + (minutes < 0 ? ' (valor negativo)' : '');
          };

          this.queryResults = response.data.map(item => [
            item.nombre_repartidor || 'Nombre no disponible',
            formatTime(item.tiempo_promedio_minutos)
          ]);
        }

        else if (this.selectedQuery === '5') { 
          const response = await repartidorService.getTop3Repartidores();
          console.log('Respuesta del backend:', response.data);

          this.queryTitle = 'Top 3 Repartidores por Rendimiento';
          this.queryHeaders = ['Nombre', 'Entregas Completadas', 'Puntuación Promedio', 'Rendimiento'];

          this.queryResults = response.data.map(item => [
            item.nombre, 
            item.entregasCompletadas,
            item.puntuacionPromedio.toFixed(2), 
            item.rendimiento.toFixed(2) 
          ]);
        }
        else if (this.selectedQuery === '6') {
          const { data } = await pagoService.getMedioPagoMasUsadoUrgentes();
          console.log('Respuesta del backend:', data);

          this.queryTitle = 'Medio de pago más usado en pedidos urgentes';
          this.queryHeaders = ['Medio de pago', 'Cantidad de veces usado'];

          this.queryResults = Array.isArray(data) ? data : [data];
          this.queryResults = this.queryResults.map(item => [
            item.nombremetododepago,
            item.cantidad
          ]);
        }
        else if (this.selectedQuery === '7') {
          const empresa = this.empresaInput.trim();
          const response = await detallePedidoService.getEntregasCercanas(empresa);
          console.log('Respuesta del backend:', response.data);

          this.queryTitle = `5 puntos de entrega más cercanos a la empresa "${empresa}"`;
          this.queryHeaders = ['Dirección destino', 'Distancia (km)', 'Latitud', 'Longitud'];

          this.queryResults = response.data.map(item => {
            // Parsear latitud y longitud desde la WKT 'LINESTRING'
            let lat = '--', lng = '--';
            if (item.ruta_estimada) {
              // Ejemplo: "LINESTRING(-70.67 -33.465,-70.685 -33.465)"
              const match = item.ruta_estimada.match(/LINESTRING\((-?\d+(\.\d+)?)[\s,]+(-?\d+(\.\d+)?)/);
              if (match) {
                lng = match[1];
                lat = match[3];
              }
            }
            return [
              item.direcciondestino || 'No disponible',
              item.distancia_metros ? (item.distancia_metros / 1000).toFixed(2) : '--',
              lat,
              lng
            ];
          });

          if (!this.queryResults.length) {
            this.queryResults = [['No hay datos', '', '', '']];
          }
        }
        else if (this.selectedQuery === '8') {
          const response = await detallePedidoService.getPuntosEntregaMasLejano();
          console.log('Respuesta del backend:', response.data);

          this.queryTitle = 'Punto de entrega más lejano desde cada empresa';
          this.queryHeaders = ['Empresa', 'Dirección', 'Distancia (km)'];

          this.queryResults = response.data.map(item => [
            item.nombreempresa || 'No disponible',
            item.direcciondestino || 'No disponible',
            item.distancia_km ? item.distancia_km.toFixed(2) : '--'
          ]);

          if (!this.queryResults.length) {
            this.queryResults = [['No hay datos', '', '']];
          }
        }

        else if (this.selectedQuery === '9') {
          const response = await orderService.getPedidosZonasCruzadas();
          this.queryTitle = 'Pedidos cuya ruta cruza más de 2 zonas';
          this.queryHeaders = ['ID Pedido', 'Empresa', 'Zonas Cruzadas'];
          this.queryResults = (response.data || []).map(item => [
            item.idpedido || '--',
            item.nombreempresa || '--',
            item.zonas_cruzadas !== undefined ? item.zonas_cruzadas : '--'
          ]);
          if (!this.queryResults.length) {
            this.queryResults = [['No hay datos', '', '']];
          }
        }

        else if (this.selectedQuery === '10') {
          const id = this.clienteIdInput.trim();
          if (!id) {
            this.queryResults = [['Debe ingresar un ID de cliente']];
            return;
          }
          const response = await orderService.getZonaCliente(id);
          this.queryTitle = `Zona a la que pertenece el cliente #${id}`;
          this.queryHeaders = ['Zona'];
          this.queryResults = [[response.data]];
        }
        else if (this.selectedQuery === '11') {
          const response = await orderService.getHighDensityZones();
          this.queryTitle = 'Zonas con alta densidad de pedidos';
          this.queryHeaders = ['Zona', 'Cantidad de pedidos', 'Centroide (Lon, Lat)'];

          this.queryResults = (response.data || []).map(item => {
            // Extraer lon y lat del centroWkt: POINT(-70.645 -33.452)
            let centroide = '--';
            if (item.centroWkt && item.centroWkt.startsWith('POINT(')) {
              const match = item.centroWkt.match(/POINT\(([-\d.]+) ([-\d.]+)\)/);
              if (match) {
                centroide = `${match[1]}, ${match[2]}`;
              }
            }
            return [
              item.zonaCobertura || '--',
              item.cantidadPedidos || '--',
              centroide
            ];
          });

          if (!this.queryResults.length) {
            this.queryResults = [['No hay datos', '', '']];
          }
        }

        else if (this.selectedQuery === '12') {
          // NUEVA: Clientes a más de 5km
          const response = await clienteService.getClientesMasDe5Km();
          this.queryTitle = 'Clientes a más de 5km de cualquier empresa o farmacia';
          this.queryHeaders = ['ID Cliente', 'Nombre', 'Distancia mínima (km)'];
          this.queryResults = (response.data || []).map(item => [
            item.cliente_id ?? '--',
            item.nombre ?? '--',
            item.distancia_minima != null
              ? (item.distancia_minima / 1000).toLocaleString('es-CL', { minimumFractionDigits: 2, maximumFractionDigits: 2 })
              : '--'
          ]);
        }

        else if (this.selectedQuery === '13') {
          const idCliente = this.clienteIdInput?.trim();
          if (!idCliente) {
            this.queryResults = [['Debe ingresar un ID de cliente']];
            return;
          }
          const response = await clienteService.verificarClienteEnZona(idCliente);
          this.queryTitle = `¿Cliente #${idCliente} está en zona de cobertura? (buffer 1km)`;
          this.queryHeaders = [
            'ID Cliente',
            'Nombre',
            'En zona',
            'ID Zona',
            'Detalle/Zona'
          ];
          this.queryResults = (response.data || []).map(item => [
            item.cliente_id ?? '--',
            item.nombre ?? '--',
            item.zona_id ? 'Sí' : 'No',
            item.zona_id ?? '--',
            item.nombre_zona ?? '--'
          ]);
        }

        else if (this.selectedQuery === '14') {
          const response = await repartidorService.getDistanciasRecorridasUltimoMes();
          this.queryTitle = 'Distancia recorrida por repartidor (último mes)';
          this.queryHeaders = ['ID Repartidor', 'Nombre', 'Distancia recorrida (km)'];
          this.queryResults = (response.data || []).map(item => [
            item.repartidorId ?? '--',
            item.nombre ?? '--',
            // Si quieres mostrar SIEMPRE el número con 2 decimales y el sufijo "km":
            item.distanciaTotalMetros != null
              ? `${(item.distanciaTotalMetros / 1000).toLocaleString('es-CL', { minimumFractionDigits: 2, maximumFractionDigits: 2 })} km`
              : '--'
          ]);
        }

      } catch (error) {
        console.error('Error al obtener los datos:', error);
        this.queryError = 'Hubo un problema al cargar los datos. Intenta de nuevo más tarde.';
      } finally {
        this.queryLoading = false;
      }
    },

    dismissNotification(id) {
      this.notifications = this.notifications.filter(notification => notification.id !== id);
    },  
  }
}
</script>

<style scoped>
.admin-dashboard {
  background-color: var(--bg-primary);
  min-height: 100vh;
  padding: 2rem;
}

.admin-header {
  background-color: var(--card-bg);
  border: 1px solid var(--border-blue);
  border-radius: 12px;
  padding: 1.5rem;
  margin-bottom: 2rem;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.header-left h1 {
  font-size: 1.75rem;
  color: var(--text-primary);
  margin-bottom: 0.5rem;
}

.welcome-message {
  color: var(--text-secondary);
  font-size: 1rem;
}

.header-right {
  display: flex;
  align-items: center;
  gap: 1.5rem;
}

.refresh-btn {
  background-color: var(--primary-blue);
  color: var(--text-primary);
  border: none;
  padding: 0.5rem 1rem;
  border-radius: 6px;
  cursor: pointer;
  display: flex;
  align-items: center;
  gap: 0.5rem;
  transition: all 0.3s ease;
  font-size: 0.875rem;
}

.refresh-btn:hover {
  background-color: var(--primary-blue-hover);
  transform: translateY(-1px);
}

.last-updated {
  color: var(--text-secondary);
  font-size: 0.875rem;
}

.main-content {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
  gap: 1.5rem;
}

.card {
  background-color: var(--card-bg);
  border: 1px solid var(--border-blue);
  border-radius: 12px;
  transition: transform 0.3s ease;
}

.card:hover {
  transform: translateY(-2px);
}

.card-header {
  padding: 1rem 1.5rem;
  border-bottom: 1px solid var(--border-blue);
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.card-header h2 {
  font-size: 1.25rem;
  color: var(--text-primary);
  margin: 0;
}

.card-body {
  padding: 1.5rem;
}

.query-section {
  display: flex;
  flex-direction: column;
  gap: 1rem;
}

.query-select {
  background-color: var(--bg-secondary);
  border: 1px solid var(--border-blue);
  color: var(--text-primary);
  padding: 0.75rem 1rem;
  border-radius: 6px;
  width: 100%;
  font-size: 0.875rem;
  transition: all 0.3s ease;
}

.query-select:focus {
  border-color: var(--primary-blue);
  box-shadow: 0 0 0 2px var(--blue-glow);
  outline: none;
}

.query-select option {
  background-color: var(--bg-secondary);
  color: var(--text-primary);
  padding: 0.5rem;
}

.empresa-input {
  background-color: var(--bg-secondary);
  border: 1px solid var(--border-blue);
  color: var(--text-primary);
  padding: 0.75rem 1rem;
  border-radius: 6px;
  width: 100%;
  font-size: 0.875rem;
  transition: all 0.3s ease;
}

.empresa-input:focus {
  border-color: var(--primary-blue);
  box-shadow: 0 0 0 2px var(--blue-glow);
  outline: none;
}

.empresa-input::placeholder {
  color: var(--placeholder-color);
}

.btn-run-query {
  background-color: var(--primary-blue);
  color: var(--text-primary);
  border: none;
  padding: 0.75rem 1rem;
  border-radius: 6px;
  cursor: pointer;
  font-size: 0.875rem;
  transition: all 0.3s ease;
  display: flex;
  align-items: center;
  gap: 0.5rem;
}

.btn-run-query:hover {
  background-color: var(--primary-blue-hover);
  transform: translateY(-1px);
}

.btn-run-query:disabled {
  background-color: var(--dark-gray);
  cursor: not-allowed;
}

.results-section {
  margin-top: 1rem;
  padding: 1rem;
  background-color: var(--bg-secondary);
  border-radius: 6px;
  border: 1px solid var(--border-blue);
}

.results-table {
  width: 100%;
  border-collapse: separate;
  border-spacing: 0;
}

.results-table th {
  text-align: left;
  padding: 0.75rem;
  color: var(--text-secondary);
  font-weight: 500;
  border-bottom: 1px solid var(--border-blue);
}

.results-table td {
  padding: 0.75rem;
  color: var(--text-primary);
  border-bottom: 1px solid var(--border-blue);
}

.results-table tr:last-child td {
  border-bottom: none;
}

.results-table tr:hover td {
  background-color: var(--bg-secondary);
}

.no-results {
  text-align: center;
  padding: 2rem;
  color: var(--text-secondary);
}

@media (max-width: 768px) {
  .admin-dashboard {
    padding: 1rem;
  }

  .admin-header {
    flex-direction: column;
    text-align: center;
    gap: 1rem;
  }

  .header-right {
    flex-direction: column;
    width: 100%;
  }

  .refresh-btn {
    width: 100%;
    justify-content: center;
  }

  .main-content {
    grid-template-columns: 1fr;
  }

  .card-header {
    flex-direction: column;
    gap: 0.5rem;
    text-align: center;
  }
}
</style>
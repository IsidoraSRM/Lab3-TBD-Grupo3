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
          Actualizar <i class="fas fa-sync-alt"></i>
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
          
          <!-- Barra de controles en una sola fila -->
          <div class="query-controls">
            <!-- Filtro por laboratorio -->
            <div class="filter-group">

              <select v-model="selectedLabFilter" id="lab-filter" class="filter-select">
                <option value="ALL">Todas</option>
                <option value="LAB1">LAB 1</option>
                <option value="LAB2">LAB 2</option>
                <option value="LAB3">LAB 3</option>
                
              </select>
              <label for="lab-filter">
                <i class="fas fa-filter"></i>
              </label>
              
            </div>
            
            <!-- Selector de consulta -->
            <div class="query-group">
              <div class="query-select-wrapper">
                <select v-model="selectedQuery" class="query-select">
                  <option value="" disabled>Seleccione una consulta</option>
                  <option 
                    v-for="option in filteredQueryOptions" 
                    :key="option.value" 
                    :value="option.value"
                  >
                    {{ option.text }}
                  </option>
                </select>
                <i class="fas fa-chart-line query-icon"></i>
              </div>
            </div>
            
            <!-- Inputs condicionales -->
            <div class="input-group" v-if="selectedQuery === '7'">
              <input 
                v-model="empresaInput" 
                placeholder="Nombre de la empresa" 
                class="param-input"
              />
            </div>
            
            <div class="input-group" v-if="selectedQuery === '10' || selectedQuery === '13'">
              <input 
                v-model="clienteIdInput" 
                placeholder="ID del cliente" 
                class="param-input"
              />
            </div>
            
            <!-- Botón ejecutar -->
            <div class="execute-group">
              <button 
                class="btn-run-query" 
                @click="runQuery" 
                :disabled="!selectedQuery || (selectedQuery === '7' && !empresaInput) || ((selectedQuery === '10' || selectedQuery === '13') && !clienteIdInput)"
              >
                Ejecutar <i class="fas fa-play"></i>
              </button>
            </div>
          </div>
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
                    <th v-if="hasGeoData()" class="action-header">Ver en Mapa</th>
                  </tr>
                </thead>
                <tbody>
                  <tr v-for="(row, rowIndex) in queryResults" :key="rowIndex">
                    <td v-for="(cell, cellIndex) in row" :key="cellIndex">
                      {{ cell }}
                    </td>
                    <!-- Columna "Ver" para consultas georreferenciadas -->
                    <td v-if="hasGeoData()" class="action-cell">
                      <button 
                        class="view-map-btn" 
                        @click="openMapModal(row, rowIndex)"
                        :disabled="!hasValidCoordinates(row)"
                      >
                        <i class="fas fa-map-marker-alt"></i> Ver
                      </button>
                    </td>
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
    
    <!-- Modal del mapa -->
    <div v-if="showMapModal" class="map-modal-overlay" @click="closeMapModal">
      <div class="map-modal" @click.stop>
        <div class="map-modal-header">
          <h3>{{ modalMapData?.title || 'Ubicación en el mapa' }}</h3>
          <button class="close-btn" @click="closeMapModal">
            <i class="fas fa-times"></i>
          </button>
        </div>
        <div class="map-modal-body">
          <div class="map-container" ref="modalMapContainer"></div>
          <div v-if="modalMapData?.info" class="location-info">
            <p><strong>{{ modalMapData.info }}</strong></p>
            <p>Coordenadas: {{ modalMapData.lat }}, {{ modalMapData.lng }}</p>
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
import { analyticsService } from '@/services/analyticsService';
import { rutasFrecuentesService } from '@/services/rutasFrecuentesService';
import L from 'leaflet';
import 'leaflet/dist/leaflet.css';


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
      // Variables para el modal del mapa
      showMapModal: false,
      modalMapData: null,
      mapInstance: null,
      // Variable para filtros
      selectedLabFilter: 'ALL',
      // Lista completa de opciones de consulta
      allQueryOptions: [
        // LAB 1
        { value: '1', text: '[LAB 1 C 1] Cliente que más ha gastado en pedidos entregados.', lab: 'LAB1' },
        { value: '2', text: '[LAB 1 C 2] Productos más pedidos por categoría', lab: 'LAB1' },
        { value: '3', text: '[LAB 1 C 3] Listar las empresas asociadas con más entregas fallidas.', lab: 'LAB1' },
        { value: '4', text: '[LAB 1 C 4] Calcular el tiempo promedio entre pedido y entrega por repartidor.', lab: 'LAB1' },
        { value: '5', text: '[LAB 1 C 5] Obtener los 3 repartidores con mejor rendimiento (basado en entregas y puntuación).', lab: 'LAB1' },
        { value: '6', text: '[LAB 1 C 6] Obtener el medio de pago más usado en pedidos urgentes', lab: 'LAB1' },
        
        // LAB 2
        { value: '7', text: '[LAB 2 C 1] 5 puntos de entrega más cercanos a la empresa', lab: 'LAB2' },
        { value: '8', text: '[LAB 2 C 2] Punto de entrega más lejano desde cada empresa', lab: 'LAB2' },
        { value: '9', text: '[LAB 2 C 3] Pedidos cuya ruta cruza más de 2 zonas', lab: 'LAB2' },
        { value: '12', text: '[LAB 2 C 4] Clientes a más de 5km de cualquier empresa/farmacia', lab: 'LAB2' },
        { value: '13', text: '[LAB 2 C 5] ¿Cliente está en zona de cobertura? (buffer 1km)', lab: 'LAB2' },
        { value: '14', text: '[LAB 2 C 6] Distancia recorrida por repartidor en el último mes', lab: 'LAB2' },
        { value: '10', text: '[LAB 2 EXTRA 1] Zona a la que pertenece un cliente', lab: 'LAB2' },
        { value: '11', text: '[LAB 2 EXTRA 2] Zonas con alta densidad de pedidos', lab: 'LAB2' },
        
        // LAB 3
        { value: '15', text: '[LAB 3 C 1] Obtener el promedio de puntuación por empresa o farmacia', lab: 'LAB3' },
        { value: '16', text: '[LAB 3 C 2] Listar las opiniones que contengan palabras clave como \'demora\' o \'error\'.', lab: 'LAB3' },
        { value: '17', text: '[LAB 3 C 3] Contar cuántos pedidos tienen más de 3 cambios de estado en menos de 10 minutos.', lab: 'LAB3' },
        { value: '18', text: '[LAB 3 C 4] Analizar las rutas más frecuentes de repartidores en los últimos 7 días.', lab: 'LAB3' },
        { value: '19', text: '[LAB 3 C 5] Detectar clientes que realizaron búsquedas sin concretar pedidos (navegación sin compra).', lab: 'LAB3' },
        { value: '20', text: '[LAB 3 C 6] Agrupar opiniones por hora del día para analizar patrones de satisfacción.', lab: 'LAB3' },
      ],
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

        else if (this.selectedQuery === '15') {
          // LAB 3 C1: Promedio de puntuación por empresa
          const response = await analyticsService.getPromedioPorEmpresa();
          console.log('Respuesta del backend:', response);

          this.queryTitle = 'Promedio de puntuación por empresa';
          this.queryHeaders = ['ID Empresa', 'Nombre Empresa', 'Promedio Puntuación', 'Total Opiniones'];

          this.queryResults = response.map(item => [
            item.empresaId || '--',
            item.nombreEmpresa || '--',
            item.promedioPuntuacion ? item.promedioPuntuacion.toFixed(2) : '--',
            item.totalOpiniones || 0
          ]);
        }

        else if (this.selectedQuery === '16') {
          const response = await analyticsService.getOpinionesConPalabrasClave();
          console.log('Respuesta del backend:', response);

          this.queryTitle = 'Opiniones con palabras clave "demora" o "error"';
          this.queryHeaders = ['ID Cliente', 'ID Empresa', 'Comentario', 'Puntuación', 'Fecha'];

          this.queryResults = response.map(item => [
            item.clienteId || '--',
            item.empresaId || '--',
            item.comentario || '--',
            item.puntuacion || '--',
            item.fecha ? new Date(item.fecha).toLocaleString() : '--'
          ]);
        }


        else if (this.selectedQuery === '17') {
          const response = await analyticsService.getPedidosConMuchosCambios();
          console.log('Pedidos con >3 cambios <10min:', response);

          this.queryTitle = 'Pedidos con más de 3 cambios de estado en menos de 10 minutos';
          this.queryHeaders = ['ID Pedido'];

          this.queryResults = response.map(pedidoId => [pedidoId]);
        }


        else if (this.selectedQuery === '19') {
          const response = await analyticsService.getClientesSinCompra();
          console.log('Clientes que no compraron:', response);

          this.queryTitle = 'Clientes que navegaron pero no concretaron compra';
          this.queryHeaders = ['ID Cliente'];

          this.queryResults = response.map(clienteId => [clienteId]);
        }
        
        else if (this.selectedQuery === '18') {
          // LAB 3 C4: Rutas más frecuentes de repartidores (últimos 7 días)
          const response = await rutasFrecuentesService.getTopTramosFrecuentes(10);
          console.log('Respuesta del backend:', response);

          this.queryTitle = 'Top 10 tramos más frecuentes de repartidores (últimos 7 días)';
          this.queryHeaders = ['ID Repartidor', 'Nombre Repartidor', 'Desde (Lat, Lng)', 'Hasta (Lat, Lng)', 'Frecuencia'];

          this.queryResults = response.map(item => [
            item.repartidorId || '--',
            item.nombreRepartidor || '--',
            `(${item.latDesde}, ${item.lngDesde})`,
            `(${item.latHasta}, ${item.lngHasta})`,
            item.frecuencia || 0
          ]);
        }

        else if (this.selectedQuery === '20') {
          // LAB 3 C6: Patrones de opiniones por hora
          const response = await analyticsService.getPatronesPorHora();
          console.log('Respuesta del backend:', response);

          this.queryTitle = 'Patrones de satisfacción por hora del día';
          this.queryHeaders = ['Hora', 'Promedio Puntuación', 'Total Opiniones', 'Puntuación Máxima', 'Puntuación Mínima'];

          this.queryResults = response.map(item => [
            `${item.hora}:00`,
            item.promedioPuntuacion ? item.promedioPuntuacion.toFixed(2) : '--',
            item.totalOpiniones || 0,
            item.puntuacionMaxima ? item.puntuacionMaxima.toFixed(1) : '--',
            item.puntuacionMinima ? item.puntuacionMinima.toFixed(1) : '--'
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

    // Métodos para el mapa
    hasGeoData() {
      // Consultas que contienen datos georreferenciados con coordenadas válidas
      return ['7', '11', '18'].includes(this.selectedQuery);
    },

    hasValidCoordinates(row) {
      if (this.selectedQuery === '7') {
        // Query 7: [Dirección, Distancia, Latitud, Longitud]
        return row[2] && row[3] && row[2] !== '--' && row[3] !== '--';
      } else if (this.selectedQuery === '11') {
        // Query 11: [Zona, Cantidad, Centroide]
        return row[2] && row[2] !== '--' && row[2].includes(',');
      } else if (this.selectedQuery === '18') {
        // Query 18: [ID Repartidor, Nombre, Desde, Hasta, Frecuencia]
        return row[2] && row[3] && row[2] !== '--' && row[3] !== '--';
      }
      return false;
    },

    openMapModal(row, rowIndex) {
      let lat, lng, title, info;

      if (this.selectedQuery === '7') {
        // Query 7: 5 puntos de entrega más cercanos
        lat = parseFloat(row[2]);
        lng = parseFloat(row[3]);
        title = 'Punto de entrega';
        info = `${row[0]} - Distancia: ${row[1]} km`;
      } else if (this.selectedQuery === '11') {
        // Query 11: Zonas con alta densidad
        const coords = row[2].split(',');
        lng = parseFloat(coords[0].trim());
        lat = parseFloat(coords[1].trim());
        title = 'Centro de zona';
        info = `${row[0]} - Pedidos: ${row[1]}`;
      } else if (this.selectedQuery === '18') {
        // Query 18: Rutas frecuentes - mostrar ruta completa (origen y destino)
        const coordsDesde = row[2].match(/\(([-\d.]+),\s*([-\d.]+)\)/);
        const coordsHasta = row[3].match(/\(([-\d.]+),\s*([-\d.]+)\)/);
        
        if (coordsDesde && coordsHasta) {
          const latDesde = parseFloat(coordsDesde[1]);
          const lngDesde = parseFloat(coordsDesde[2]);
          const latHasta = parseFloat(coordsHasta[1]);
          const lngHasta = parseFloat(coordsHasta[2]);
          
          // Calcular el punto central para centrar el mapa
          lat = (latDesde + latHasta) / 2;
          lng = (lngDesde + lngHasta) / 2;
          
          title = 'Ruta frecuente';
          info = `${row[1]} - Frecuencia: ${row[4]}`;
          
          // Pasar las coordenadas completas para dibujar la ruta
          this.modalMapData = { 
            lat, 
            lng, 
            title, 
            info,
            routeData: {
              desde: { lat: latDesde, lng: lngDesde },
              hasta: { lat: latHasta, lng: lngHasta },
              repartidor: row[1],
              frecuencia: row[4]
            }
          };
          this.showMapModal = true;
          this.$nextTick(() => {
            this.initModalMap();
          });
          return;
        }
      }

      if (lat && lng && !isNaN(lat) && !isNaN(lng)) {
        this.modalMapData = { lat, lng, title, info };
        this.showMapModal = true;
        this.$nextTick(() => {
          this.initModalMap();
        });
      }
    },

    initModalMap() {
      if (!this.$refs.modalMapContainer || !this.modalMapData) return;

      // Fix para los iconos de Leaflet
      delete L.Icon.Default.prototype._getIconUrl;
      L.Icon.Default.mergeOptions({
        iconRetinaUrl: 'https://cdnjs.cloudflare.com/ajax/libs/leaflet/1.7.1/images/marker-icon-2x.png',
        iconUrl: 'https://cdnjs.cloudflare.com/ajax/libs/leaflet/1.7.1/images/marker-icon.png',
        shadowUrl: 'https://cdnjs.cloudflare.com/ajax/libs/leaflet/1.7.1/images/marker-shadow.png',
      });

      // Crear el mapa
      this.mapInstance = L.map(this.$refs.modalMapContainer).setView(
        [this.modalMapData.lat, this.modalMapData.lng], 
        15
      );

      // Agregar tiles del mapa
      L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
        attribution: '© OpenStreetMap contributors'
      }).addTo(this.mapInstance);

      // Si es una ruta (consulta 18), dibujar la ruta completa
      if (this.modalMapData.routeData) {
        const { desde, hasta, repartidor, frecuencia } = this.modalMapData.routeData;
        
        // Crear iconos personalizados para origen y destino
        const origenIcon = L.divIcon({
          className: 'custom-marker origen-marker',
          html: '<i class="fas fa-play-circle" style="color: #10b981; font-size: 20px;"></i>',
          iconSize: [30, 30],
          iconAnchor: [15, 15]
        });
        
        const destinoIcon = L.divIcon({
          className: 'custom-marker destino-marker',
          html: '<i class="fas fa-flag-checkered" style="color: #ef4444; font-size: 20px;"></i>',
          iconSize: [30, 30],
          iconAnchor: [15, 15]
        });
        
        // Agregar marcadores de origen y destino
        L.marker([desde.lat, desde.lng], { icon: origenIcon })
          .addTo(this.mapInstance)
          .bindPopup(`<strong>🚀 Origen</strong><br>Repartidor: ${repartidor}<br>Coordenadas: (${desde.lat}, ${desde.lng})`);
        
        L.marker([hasta.lat, hasta.lng], { icon: destinoIcon })
          .addTo(this.mapInstance)
          .bindPopup(`<strong>🏁 Destino</strong><br>Repartidor: ${repartidor}<br>Coordenadas: (${hasta.lat}, ${hasta.lng})`);
        
        // Dibujar línea entre origen y destino
        const rutaLine = L.polyline([
          [desde.lat, desde.lng],
          [hasta.lat, hasta.lng]
        ], {
          color: '#3b82f6',
          weight: 4,
          opacity: 0.8,
          dashArray: '10, 5'
        }).addTo(this.mapInstance);
        
        // Popup en el centro de la línea
        const centerLat = (desde.lat + hasta.lat) / 2;
        const centerLng = (desde.lng + hasta.lng) / 2;
        
        L.marker([centerLat, centerLng], {
          icon: L.divIcon({
            className: 'route-info-marker',
            html: `<div style="background: #3b82f6; color: white; padding: 4px 8px; border-radius: 12px; font-size: 12px; font-weight: bold; box-shadow: 0 2px 4px rgba(0,0,0,0.3);"><i class="fas fa-route"></i> ${frecuencia}</div>`,
            iconSize: [50, 25],
            iconAnchor: [25, 12]
          })
        })
        .addTo(this.mapInstance)
        .bindPopup(`<strong>📊 Ruta Frecuente</strong><br>Repartidor: ${repartidor}<br>Frecuencia: ${frecuencia} vez(es)<br>Distancia aprox: ${this.calculateDistance(desde.lat, desde.lng, hasta.lat, hasta.lng)} km`);
        
        // Ajustar la vista para mostrar toda la ruta
        const bounds = L.latLngBounds([
          [desde.lat, desde.lng],
          [hasta.lat, hasta.lng]
        ]);
        this.mapInstance.fitBounds(bounds, { padding: [30, 30] });
        
      } else {
        // Para otras consultas, mostrar un marcador simple
        L.marker([this.modalMapData.lat, this.modalMapData.lng])
          .addTo(this.mapInstance)
          .bindPopup(this.modalMapData.info || 'Ubicación')
          .openPopup();
      }
    },

    closeMapModal() {
      this.showMapModal = false;
      if (this.mapInstance) {
        this.mapInstance.remove();
        this.mapInstance = null;
      }
      this.modalMapData = null;
    },

    // Método para calcular distancia entre dos puntos (fórmula de Haversine)
    calculateDistance(lat1, lng1, lat2, lng2) {
      const R = 6371; // Radio de la Tierra en km
      const dLat = this.degreesToRadians(lat2 - lat1);
      const dLng = this.degreesToRadians(lng2 - lng1);
      const a = 
        Math.sin(dLat/2) * Math.sin(dLat/2) +
        Math.cos(this.degreesToRadians(lat1)) * Math.cos(this.degreesToRadians(lat2)) * 
        Math.sin(dLng/2) * Math.sin(dLng/2);
      const c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
      return (R * c).toFixed(2);
    },

    degreesToRadians(degrees) {
      return degrees * (Math.PI/180);
    },  
  },

  computed: {
    filteredQueryOptions() {
      if (this.selectedLabFilter === 'ALL') {
        return this.allQueryOptions;
      }
      return this.allQueryOptions.filter(option => option.lab === this.selectedLabFilter);
    }
  },

  watch: {
    selectedLabFilter() {
      // Resetear la consulta seleccionada cuando cambie el filtro
      this.selectedQuery = '';
      this.queryResults = null;
    }
  },

  beforeUnmount() {
    if (this.mapInstance) {
      this.mapInstance.remove();
    }
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
  flex-direction: column;
  gap: 1rem;
}

.card-header h2 {
  font-size: 1.25rem;
  color: var(--text-primary);
  margin: 0;
  align-self: flex-start;
}

/* Estilos para los iconos de Font Awesome */
.filter-group label i,
.btn-run-query i,
.refresh-btn i {
  margin-left: 0.5rem;
  font-size: 0.875rem;
}

.query-select-wrapper {
  position: relative;
  display: flex;
  align-items: center;
  width: 100%;
}

.query-icon {
  position: absolute;
  right: 0.75rem;
  z-index: 1;
  color: var(--text-secondary);
  pointer-events: none;
  font-size: 0.875rem;
}

.query-select-wrapper .query-select {
  padding-right: 2.5rem;
}

.filter-group label {
  display: flex;
  align-items: center;
  color: var(--text-secondary);
  font-size: 0.875rem;
  font-weight: 500;
  white-space: nowrap;
}

/* Estilos para la barra de controles horizontal */
.query-controls {
  display: flex;
  align-items: center;
  gap: 1rem;
  flex-wrap: wrap;
}

.filter-group {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  min-width: fit-content;
}

.filter-select {
  background-color: var(--bg-secondary);
  border: 1px solid var(--border-blue);
  color: var(--text-primary);
  padding: 0.5rem 0.75rem;
  border-radius: 6px;
  font-size: 0.875rem;
  transition: all 0.3s ease;
  min-width: 100px;
}

.filter-select:focus {
  border-color: var(--primary-blue);
  box-shadow: 0 0 0 2px var(--blue-glow);
  outline: none;
}

.query-group {
  flex: 1;
  min-width: 250px;
}

.input-group {
  min-width: 200px;
}

.param-input {
  background-color: var(--bg-secondary);
  border: 1px solid var(--border-blue);
  color: var(--text-primary);
  padding: 0.5rem 0.75rem;
  border-radius: 6px;
  width: 100%;
  font-size: 0.875rem;
  transition: all 0.3s ease;
}

.param-input:focus {
  border-color: var(--primary-blue);
  box-shadow: 0 0 0 2px var(--blue-glow);
  outline: none;
}

.param-input::placeholder {
  color: var(--placeholder-color);
}

.execute-group {
  min-width: fit-content;
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

.filter-select option {
  background-color: var(--bg-secondary);
  color: var(--text-primary);
  padding: 0.5rem;
}

.btn-run-query {
  background-color: var(--primary-blue);
  color: var(--text-primary);
  border: none;
  padding: 0.5rem 1rem;
  border-radius: 6px;
  cursor: pointer;
  font-size: 0.875rem;
  transition: all 0.3s ease;
  display: flex;
  align-items: center;
  gap: 0.5rem;
  white-space: nowrap;
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
    padding: 1rem;
    gap: 0.75rem;
  }

  .card-header h2 {
    align-self: center;
    text-align: center;
  }

  .query-controls {
    flex-direction: column;
    align-items: stretch;
    gap: 0.75rem;
  }

  .filter-group {
    justify-content: center;
  }

  .query-group {
    min-width: auto;
  }

  .input-group {
    min-width: auto;
  }

  .execute-group {
    display: flex;
    justify-content: center;
  }

  .btn-run-query {
    width: 100%;
    justify-content: center;
  }
}

/* Estilos para el modal del mapa */
.map-modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.7);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 1000;
}

.map-modal {
  background-color: var(--card-bg);
  border: 1px solid var(--border-blue);
  border-radius: 12px;
  width: 90%;
  max-width: 800px;
  max-height: 90vh;
  overflow: hidden;
  box-shadow: 0 20px 40px rgba(0, 0, 0, 0.5);
}

.map-modal-header {
  padding: 1rem 1.5rem;
  border-bottom: 1px solid var(--border-blue);
  display: flex;
  justify-content: space-between;
  align-items: center;
  background-color: var(--bg-secondary);
}

.map-modal-header h3 {
  color: var(--text-primary);
  margin: 0;
  font-size: 1.25rem;
}

.close-btn {
  background: none;
  border: none;
  color: var(--text-secondary);
  font-size: 1.5rem;
  cursor: pointer;
  padding: 0.25rem;
  border-radius: 4px;
  transition: all 0.3s ease;
}

.close-btn:hover {
  color: var(--text-primary);
  background-color: var(--border-blue);
}

.map-modal-body {
  padding: 0;
}

.map-container {
  height: 400px;
  width: 100%;
}

.location-info {
  padding: 1rem 1.5rem;
  background-color: var(--bg-secondary);
  border-top: 1px solid var(--border-blue);
}

.location-info p {
  margin: 0.5rem 0;
  color: var(--text-primary);
}

.location-info strong {
  color: var(--primary-blue);
}

/* Estilos para la columna de acciones */
.action-header {
  text-align: center;
  width: 120px;
}

.action-cell {
  text-align: center;
  padding: 0.5rem;
}

.view-map-btn {
  background-color: var(--primary-blue);
  color: var(--text-primary);
  border: none;
  padding: 0.5rem 0.75rem;
  border-radius: 6px;
  cursor: pointer;
  font-size: 0.875rem;
  transition: all 0.3s ease;
  display: inline-flex;
  align-items: center;
  gap: 0.25rem;
}

.view-map-btn:hover:not(:disabled) {
  background-color: var(--primary-blue-hover);
  transform: translateY(-1px);
}

.view-map-btn:disabled {
  background-color: var(--dark-gray);
  cursor: not-allowed;
  opacity: 0.6;
}

.view-map-btn i {
  font-size: 0.75rem;
}

/* Estilos para marcadores personalizados del mapa */
:deep(.custom-marker) {
  background: transparent !important;
  border: none !important;
  display: flex;
  align-items: center;
  justify-content: center;
}

:deep(.origen-marker) {
  filter: drop-shadow(2px 2px 4px rgba(0,0,0,0.3));
}

:deep(.destino-marker) {
  filter: drop-shadow(2px 2px 4px rgba(0,0,0,0.3));
}

:deep(.route-info-marker) {
  background: transparent !important;
  border: none !important;
}

/* Estilos para el loading y estados vacíos */
.loading-indicator {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 0.5rem;
  padding: 2rem;
  color: var(--text-secondary);
}

.empty-state {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  gap: 1rem;
  padding: 3rem;
  color: var(--text-secondary);
  text-align: center;
}

.empty-state i {
  font-size: 3rem;
  opacity: 0.5;
}
</style>
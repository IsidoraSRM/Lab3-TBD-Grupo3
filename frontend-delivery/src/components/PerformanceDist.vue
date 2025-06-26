<template>
  <div class="repartidor-performance">
    <div v-if="loading" class="loading-indicator">
      <i class="fas fa-spinner fa-spin"></i> Cargando datos de desempeño...
    </div>
    <div v-else-if="error" class="error-message">
      {{ error }}
    </div>
    <div v-else>
      <div class="results-header">
        <h3>Desempeño por repartidor</h3>
      </div>
      <div class="table-responsive">
        <table class="results-table">
          <thead>
            <tr>
              <th>Repartidor</th>
              <th>Pedidos entregados</th>
              <th>Tiempo Promedio (hr)</th>
              <th>Calificación</th>
              <th>Resultado</th>
            </tr>
          </thead>
          <tbody >
            <tr v-for="(repartidor, index) in repartidores" :key="index">
              <!-- centrar columas -->
              <td class="centered" >{{ repartidor.nombre }}</td>
              <td class="centered" >{{ repartidor.pedidosEntregados }}</td>
              <td class="centered" >{{ formatearTiempo(repartidor.tiempoPromedio) }}</td>
              <td class="centered" >{{ repartidor.calificacion }}</td>
              <td>
                <div class="progress-bar" >
                  <div 
                    class="progress-fill"
                    :style="{ width: calcularPuntualidad(repartidor) + '%', backgroundColor: getColorPuntualidad(repartidor) }">
                  </div>
                </div>
                {{ calcularPuntualidad(repartidor) }}%
              </td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>
  </div>
</template>

<script>
import repartidorService from '@/services/repartidorService';

export default {
  name: 'RepartidorPerformanceTable',
  data() {
    return {
      repartidores: [],
      loading: true,
      error: null
    };
  },
  created() {
    this.fetchRepartidoresDesempeno();
  },
  methods: {
    async fetchRepartidoresDesempeno() {
  this.loading = true;
  try {
    const response = await repartidorService.getDesempenoRepartidor();
    console.log('Desempeño de repartidores:', response.data);
    
    // Mapeo de los datos de la API a la estructura que espera el componente
    this.repartidores = response.data.map(item => {
      
      
      return {
        nombre: item.repartidor,
        pedidosEntregados: item.totalEntregas,
        tiempoPromedio: item.tiempoPromedioHoras,
        calificacion: item.calificacionPromedio
      };
    });
    
  } catch (error) {
    console.error('Error al cargar el desempeño de repartidores:', error);
    this.error = 'No se pudo cargar la información de desempeño. Inténtelo de nuevo.';
    
    // Datos de ejemplo como fallback
    this.repartidores = [
      { nombre: 'Juan Pérez', pedidosEntregados: 50, retrasos: 2 },
      { nombre: 'Carla Torres', pedidosEntregados: 45, retrasos: 1 },
      { nombre: 'Pedro López', pedidosEntregados: 60, retrasos: 0 }
    ];
  } finally {
    this.loading = false;
  }
},
    formatearTiempo(tiempo) {
        if (tiempo === undefined || tiempo === null) return 'N/A';
        return tiempo.toFixed(2) ; // Redondea a 2 decimales
        },
    calcularPuntualidad(repartidor) {
        if (!repartidor.calificacion && repartidor.calificacion !== 0) return 0;
        // Convertir la calificación (0-5) a un porcentaje (0-100%)
        return Math.round((repartidor.calificacion / 5) * 100);
        },

        getColorPuntualidad(repartidor) {
        const puntualidad = repartidor.calificacion;
        
        if (puntualidad >= 4) return '#27ae60'; // Verde: buena calificación
        if (puntualidad >= 2.5) return '#f39c12'; // Amarillo: calificación media
        return '#e74c3c'; // Rojo: mala calificación
        }
  }
};
</script>

<style scoped>
.repartidor-performance {
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
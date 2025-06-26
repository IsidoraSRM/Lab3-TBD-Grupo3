<template>
  <div class="empresa-ranking">
    <div v-if="loading" class="loading-indicator">
      <i class="fas fa-spinner fa-spin"></i> Cargando datos de empresas...
    </div>
    <div v-else-if="error" class="error-message">
      {{ error }}
    </div>
    <div v-else>
      <div class="results-header">
        <h3>Empresas con mayor volumen</h3>
      </div>
      <div class="table-responsive">
        <table class="results-table">
          <thead>
            <tr>
              <th>Ranking</th>
              <th>Empresa</th>
              <th>Total Servicios</th>
              <th>Rendimiento</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="(empresa, index) in empresas" :key="index">
              <td class="centered">{{ formatearRanking(empresa.ranking) }}</td>
              <td class="centered">{{ empresa.nombreEmpresa }}</td>
              <td class="centered">{{ empresa.totalServiciosEntregados }}</td>
              
              <td>
                <div class="progress-bar">
                  <div 
                    class="progress-fill"
                    :style="{ width: calcularDesempeno(empresa) + '%', backgroundColor: getColorDesempeno(empresa.ranking) }">
                  </div>
                </div>
                {{ calcularDesempeno(empresa) }}%
              </td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>
  </div>
</template>

<script>
import empresaService from '@/services/empresaService';

export default {
  name: 'TopCompany',
  data() {
    return {
      empresas: [],
      loading: true,
      error: null,
      maxServicios: 0 // Para calcular porcentajes relativos
    };
  },
  created() {
    this.fetchTopEmpresas();
  },
  methods: {
    async fetchTopEmpresas() {
      this.loading = true;
      try {
        const response = await empresaService.getCompanyRanking();
        console.log('Top empresas:', response.data);
        
        if (response.data && response.data.length > 0) {
          this.empresas = response.data;
          
          // Encontrar el máximo de servicios para calcular porcentajes relativos
          this.maxServicios = Math.max(...this.empresas.map(e => e.totalServiciosEntregados || 0));
        } else {
          this.empresas = [];
          this.error = 'No se encontraron datos de empresas';
        }
      } catch (error) {
        console.error('Error al cargar el ranking de empresas:', error);
        this.error = 'No se pudo cargar la información de empresas. Inténtelo de nuevo.';
        
        // Datos de ejemplo como fallback
        this.empresas = [
          { nombreEmpresa: 'DocExpress', totalServiciosEntregados: 42, ranking: 1 },
          { nombreEmpresa: 'RapiDoc', totalServiciosEntregados: 35, ranking: 2 },
          { nombreEmpresa: 'EntregaYa', totalServiciosEntregados: 28, ranking: 3 }
        ];
        this.maxServicios = 42;
      } finally {
        this.loading = false;
      }
    },
    
    formatearRanking(ranking) {
      switch(ranking) {
        case 1: return '🥇 1°';
        case 2: return '🥈 2°';
        case 3: return '🥉 3°';
        default: return `${ranking}°`;
      }
    },
    
    calcularDesempeno(empresa) {
      if (!empresa.totalServiciosEntregados || this.maxServicios === 0) return 0;
      // Calcular el porcentaje relativo al máximo
      return Math.round((empresa.totalServiciosEntregados / this.maxServicios) * 100);
    },
    
    getColorDesempeno(ranking) {
      switch(ranking) {
        case 1: return '#27ae60'; // Verde para el primer lugar
        case 2: return '#2ecc71'; // Verde más claro para segundo lugar
        case 3: return '#f39c12'; // Amarillo para tercer lugar
        default: return '#95a5a6'; // Gris para los demás
      }
    }
  }
};
</script>

<style scoped>
.empresa-ranking {
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

/* Barra de progreso para rendimiento */
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
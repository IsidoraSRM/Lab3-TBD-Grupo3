<script>
import orderService from '@/services/orderService';

export default {
    name: 'HomeView',
    data() {
        return {
            userName: 'Cargando...',
            activeOrders: 0,
            recentOrders: [],
            filterStatus: 'all',
            searchQuery: '',
            loading: false,
            error: null
        }
    },
    computed: {
        filteredOrders() {
            let orders = this.recentOrders;
            
            if (this.filterStatus !== 'all') {
                orders = orders.filter(order => {
                    if (this.filterStatus === 'active') {
                        return order.estadoPedido !== 'ENTREGADO' && 
                               order.estadoPedido !== 'FALLIDO' && 
                               order.estadoPedido !== 'CANCELADO';
                    } else {
                        const statusMap = {
                            'ENTREGADO': 'Entregado',
                            'EN_CAMINO': 'En camino',
                            'PROCESANDO': 'Procesando',
                            'FALLIDO': 'Fallido',
                            'CANCELADO': 'Cancelado'
                        };
                        return statusMap[order.estadoPedido] === this.filterStatus;
                    }
                });
            }
            
            if (this.searchQuery) {
                const query = this.searchQuery.toLowerCase();
                orders = orders.filter(order => 
                    order.idPedido.toString().toLowerCase().includes(query) || 
                    order.prioridadPedido.toLowerCase().includes(query)
                );
            }
            
            return orders;
        }
    },
    methods: {
        getStatusClass(status) {
            const statusMap = {
                'ENTREGADO': 'success',
                'EN_CAMINO': 'warning',
                'PENDIENTE_CON': 'info',
                'PENDIENTE': 'info',
                'CONFIRMADO': 'info',
                'PROCESANDO': 'info',
                'FALLIDO': 'danger',
                'CANCELADO': 'danger',
                // Agrega cualquier otro estado que pueda aparecer
            };
            return { [statusMap[status]]: true };
        },
        
        formatStatus(status) {
            const statusTranslations = {
                'ENTREGADO': 'Entregado',
                'EN_CAMINO': 'En camino',
                'PENDIENTE_CON': 'Pendiente de confirmación',
                'PENDIENTE': 'Pendiente',
                'CONFIRMADO': 'Confirmado',
                'PROCESANDO': 'Procesando',
                'FALLIDO': 'Fallido',
                'CANCELADO': 'Cancelado'
            };
            return statusTranslations[status] || status;
        },
        
        
        formatDate(dateString) {
            if (!dateString) return '--';
            const date = new Date(dateString);
            return date.toLocaleDateString('es-CL');
        },
        
        startAutoRefresh() {
            this.refreshInterval = setInterval(() => {
                if (!this.loading) {
                    this.loadUserData();
                }
            }, 30000); // Actualiza cada 30 segundos
        },

        // Método para extraer nombre amigable del email
        extractNameFromEmail(email) {
            if (!email) return 'Usuario';
            const namePart = email.split('@')[0];
            // Capitalizar primera letra
            return namePart.charAt(0).toUpperCase() + namePart.slice(1);
        },
                
        async loadUserData() {
            this.loading = true;
            this.error = null;
            
            try {
                // 1. Verificación de autenticación básica
                const authToken = localStorage.getItem('authToken');
                if (!authToken) {
                    throw new Error('No hay sesión activa');
                }

                // 2. Obtener información del usuario
                const userEmail = localStorage.getItem('userEmail') || '';
                this.userName = this.extractNameFromEmail(userEmail);
                
                // 3. Obtener ID del cliente con verificación
                const clienteId = localStorage.getItem('userId');
                console.log('ID de cliente obtenido:', clienteId);
                
                
                if (!clienteId) {
                    throw new Error('No se pudo identificar al cliente');
                }

                // 4. Obtener pedidos con prevención de caché
                const timestamp = new Date().getTime();
                const response = await orderService.getPedidosByClienteId(clienteId, timestamp);
                
                console.log('Datos de pedidos recibidos:', response.data);
                
                // 5. Procesar pedidos
                this.recentOrders = response.data || [];
                
                // 6. Calcular pedidos activos (considerando todos los estados no finalizados)
                this.activeOrders = this.recentOrders.filter(order => {
                    const estado = order.estadoPedido;
                    return !['ENTREGADO', 'FALLIDO', 'CANCELADO'].includes(estado);
                }).length;
                
                // 7. Verificación de datos
                if (this.recentOrders.length === 0) {
                    console.warn('Se recibió una lista vacía de pedidos');
                }
                
            } catch (error) {
                console.error('Error completo:', error);
                console.error('Error response:', error.response);
                
                this.error = error.response?.data?.message || 
                            error.message || 
                            'Error al cargar los pedidos. Intente nuevamente.';
                this.userName = 'Cliente';
                
                // Opcional: Recargar después de un error
                setTimeout(() => this.loadUserData(), 5000);
            } finally {
                this.loading = false;
            }
        }
        


    },
    created() {
        this.loadUserData();
        this.startAutoRefresh();
        console.log("Estados encontrados en los pedidos:", 
            this.recentOrders.map(order => order.estadoPedido));
    },

    activated() {
        this.loadUserData();
    }
}
</script>
<template>
    <div class="home-view">
        <!-- Mensaje de carga/error -->
        <div v-if="loading" class="loading-message">
            <div class="spinner"></div>
            <span>Cargando pedidos...</span>
        </div>
        <div v-if="error" class="error-message">{{ error }}</div>

        <!-- Hero Section -->
        <section class="hero-section">
            <div class="hero-content">
                <div class="hero-text">
                    <h1>Panel de Cliente</h1>
                    <p class="welcome-text">Bienvenido, {{ userName }}</p>
                    <div class="stats">
                        <div class="stat-item">
                            <span class="stat-number">{{ activeOrders }}</span>
                            <span class="stat-label">Pedidos Activos</span>
                        </div>
                    </div>
                    <div class="hero-actions">
                        <router-link to="/client" class="btn btn-primary">
                            <i class="fas fa-plus"></i>
                            Nuevo Pedido
                        </router-link>
                    </div>
                </div>
            </div>
        </section>

        <!-- Sección de Pedidos Recientes -->
        <section class="orders-section">
            <div class="card">
                <div class="section-header">
                    <h2>Pedidos Recientes</h2>
                    
                    <div class="filters">
                        <div class="search-box">
                            <input 
                                type="text" 
                                v-model="searchQuery" 
                                placeholder="Buscar por número o prioridad..."
                                class="search-input"
                            >
                            <i class="fas fa-search"></i>
                        </div>
                        
                        <select v-model="filterStatus" class="status-filter">
                            <option value="all">Todos los pedidos</option>
                            <option value="active">En curso</option>
                            <option value="Entregado">Entregados</option>
                            <option value="En camino">En camino</option>
                            <option value="Procesando">Procesando</option>
                            <option value="Fallido">Fallidos</option>
                        </select>
                    </div>
                </div>
                
                <div class="table-responsive">
                    <table class="orders-table">
                        <thead>
                            <tr>
                                <th># Pedido</th>
                                <th>Fecha Pedido</th>
                                <th>Fecha Entrega</th>
                                <th>Prioridad</th>
                                <th>Estado</th>
                            </tr>
                        </thead>
                        <tbody>
                            <tr v-for="order in filteredOrders" :key="order.idPedido">
                                <td>{{ order.idPedido }}</td>
                                <td>{{ formatDate(order.fechaPedido) }}</td>
                                <td>{{ formatDate(order.fechaEntrega) }}</td>
                                <td>{{ order.prioridadPedido }}</td>
                                <td>
                                    <span class="status-badge" :class="getStatusClass(order.estadoPedido)">
                                        {{ formatStatus(order.estadoPedido) }}
                                    </span>
                                </td>   
                            </tr>
                            <tr v-if="filteredOrders.length === 0 && !loading">
                                <td colspan="6" class="no-orders">No se encontraron pedidos</td>
                            </tr>
                        </tbody>
                    </table>
                </div>
            </div>
        </section>
    </div>
</template>

<style scoped>
.home-view {
    background-color: var(--bg-primary);
    min-height: 100vh;
    padding: 2rem;
}

/* Loading y Error */
.loading-message {
    display: flex;
    align-items: center;
    justify-content: center;
    gap: 1rem;
    color: var(--text-secondary);
    padding: 2rem;
}

.spinner {
    width: 24px;
    height: 24px;
    border: 3px solid var(--border-blue);
    border-top-color: var(--primary-blue);
    border-radius: 50%;
    animation: spin 1s linear infinite;
}

@keyframes spin {
    to { transform: rotate(360deg); }
}

.error-message {
    color: #ef4444;
    background-color: rgba(239, 68, 68, 0.1);
    padding: 1rem;
    border-radius: 8px;
    margin-bottom: 1rem;
}

/* Hero Section */
.hero-section {
    margin-bottom: 3rem;
    background: linear-gradient(135deg, var(--bg-secondary) 0%, var(--card-bg) 100%);
    border-radius: 16px;
    border: 1px solid var(--border-blue);
    overflow: hidden;
    position: relative;
}

.hero-section::before {
    content: '';
    position: absolute;
    top: 0;
    left: 0;
    right: 0;
    height: 1px;
    background: linear-gradient(90deg, transparent, var(--blue-neon), transparent);
}

.hero-content {
    padding: 3rem 2rem;
    position: relative;
    z-index: 1;
}

.hero-text {
    max-width: 800px;
    margin: 0 auto;
    text-align: center;
}

.hero-text h1 {
    font-size: 2.5rem;
    font-weight: 700;
    margin-bottom: 1rem;
    background: linear-gradient(45deg, var(--text-primary), var(--blue-neon));
    -webkit-background-clip: text;
    -webkit-text-fill-color: transparent;
}

.welcome-text {
    font-size: 1.25rem;
    color: var(--text-secondary);
    margin-bottom: 2rem;
}

.stats {
    display: flex;
    justify-content: center;
    gap: 3rem;
    margin-bottom: 2rem;
}

.stat-item {
    text-align: center;
}

.stat-number {
    display: block;
    font-size: 2.5rem;
    font-weight: 700;
    color: var(--primary-blue);
    margin-bottom: 0.5rem;
}

.stat-label {
    color: var(--text-secondary);
    font-size: 0.875rem;
}

.hero-actions {
    display: flex;
    justify-content: center;
    gap: 1rem;
}

/* Cards y Secciones */
.card {
    background-color: var(--card-bg);
    border-radius: 12px;
    border: 1px solid var(--border-blue);
    padding: 1.5rem;
    margin-bottom: 2rem;
}

/* Tabla y Filtros */
.section-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 2rem;
    flex-wrap: wrap;
    gap: 1rem;
}

.section-header h2 {
    font-size: 1.5rem;
    color: var(--text-primary);
}

.filters {
    display: flex;
    gap: 1rem;
    align-items: center;
}

.search-input {
    background-color: var(--bg-secondary);
    border: 1px solid var(--border-blue);
    color: var(--text-primary);
    padding: 0.75rem 1rem 0.75rem 2.5rem;
    border-radius: 8px;
    width: 250px;
    transition: all 0.3s ease;
}

.search-input:focus {
    border-color: var(--primary-blue);
    box-shadow: 0 0 0 2px var(--blue-glow);
    outline: none;
}

.search-input::placeholder {
    color: var(--text-secondary);
}

.search-box {
    position: relative;
}

.search-box i {
    position: absolute;
    left: 1rem;
    top: 50%;
    transform: translateY(-50%);
    color: var(--text-secondary);
}

.status-filter {
    background-color: var(--bg-secondary);
    border: 1px solid var(--border-blue);
    color: var(--text-primary);
    padding: 0.75rem 1rem;
    border-radius: 8px;
    min-width: 150px;
}

/* Tabla */
.table-responsive {
    overflow-x: auto;
    margin: 0 -1.5rem;
    padding: 0 1.5rem;
}

.orders-table {
    width: 100%;
    border-collapse: separate;
    border-spacing: 0;
}

.orders-table th {
    background-color: var(--bg-secondary);
    color: var(--text-secondary);
    font-weight: 500;
    text-align: left;
    padding: 1rem;
    border-bottom: 1px solid var(--border-blue);
}

.orders-table td {
    padding: 1rem;
    border-bottom: 1px solid var(--border-blue);
    color: var(--text-primary);
}

.orders-table tr:hover td {
    background-color: var(--bg-secondary);
}

/* Status Badges */
.status-badge {
    padding: 0.5rem 1rem;
    border-radius: 20px;
    font-size: 0.875rem;
    font-weight: 500;
    display: inline-block;
}

.success {
    background-color: rgba(34, 197, 94, 0.1);
    color: #22c55e;
}

.warning {
    background-color: rgba(234, 179, 8, 0.1);
    color: #eab308;
}

.info {
    background-color: rgba(59, 130, 246, 0.1);
    color: var(--primary-blue);
}

.danger {
    background-color: rgba(239, 68, 68, 0.1);
    color: #ef4444;
}

/* Botones */
.btn {
    display: inline-flex;
    align-items: center;
    gap: 0.5rem;
    padding: 0.75rem 1.5rem;
    border-radius: 8px;
    font-weight: 500;
    transition: all 0.3s ease;
    border: none;
    cursor: pointer;
}

.btn-primary {
    background-color: var(--primary-blue);
    color: var(--text-primary);
}

.btn-primary:hover {
    background-color: var(--primary-blue-hover);
    transform: translateY(-2px);
}

/* Responsive */
@media (max-width: 768px) {
    .home-view {
        padding: 1rem;
    }

    .hero-content {
        padding: 2rem 1rem;
    }

    .hero-text h1 {
        font-size: 2rem;
    }

    .section-header {
        flex-direction: column;
        align-items: stretch;
    }

    .filters {
        flex-direction: column;
        width: 100%;
    }

    .search-input,
    .status-filter {
        width: 100%;
    }

    .stats {
        flex-wrap: wrap;
        gap: 1.5rem;
    }
}
</style>
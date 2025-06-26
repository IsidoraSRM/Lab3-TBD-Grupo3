<template>
  <div class="trabajador-container">
    <div class="trabajador-header">
      <div class="header-content">
        <h1>Panel de Trabajador</h1>
        <p class="welcome-text">Bienvenido al panel de trabajador</p>
      </div>
    </div>
    
    <div class="orders-section">
      <OrderList 
        viewMode="repartidor" 
        :repartidorId="currentRepartidorId" 
      />
    </div>
  </div>
</template>

<script>
import OrderList from '../components/OrderList.vue';
export default {
  name: 'TrabajadorView',
  components: {
    OrderList
  },
  data() {
    return {
      currentRepartidorId: localStorage.getItem('userId') // Este valor debería venir de tu sistema de autenticación
    };
  }
}
</script>

<style scoped>
.trabajador-container {
  background-color: var(--bg-primary);
  min-height: 100vh;
  padding: 2rem;
}

.trabajador-header {
  background: linear-gradient(135deg, var(--bg-secondary) 0%, var(--card-bg) 100%);
  border-radius: 16px;
  border: 1px solid var(--border-blue);
  padding: 2rem;
  margin-bottom: 2rem;
  position: relative;
  overflow: hidden;
}

.trabajador-header::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  height: 1px;
  background: linear-gradient(90deg, transparent, var(--blue-neon), transparent);
}

.header-content {
  text-align: center;
}

.header-content h1 {
  font-size: 2rem;
  margin-bottom: 1rem;
  background: linear-gradient(45deg, var(--text-primary), var(--blue-neon));
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
}

.welcome-text {
  color: var(--text-secondary);
  font-size: 1.1rem;
  margin: 0;
}

.orders-section {
  background-color: var(--card-bg);
  border-radius: 12px;
  border: 1px solid var(--border-blue);
  padding: 1.5rem;
}

@media (max-width: 768px) {
  .trabajador-container {
    padding: 1rem;
  }

  .trabajador-header {
    padding: 1.5rem;
  }

  .header-content h1 {
    font-size: 1.75rem;
  }
}
</style>
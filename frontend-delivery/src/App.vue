<script>
import NavBar from './components/AppNavBar.vue'
import AppFooter from './components/AppFooter.vue'

export default {
  name: 'App',
  components: {
    NavBar,
    AppFooter
  }
}
</script>

<template>
  <v-app>
    <div id="app">
      <NavBar />
      <v-main>
        <router-view />
      </v-main>
      <AppFooter />
    </div>
  </v-app>
</template>


<style>
:root {
  /* Colores del sistema */
  --bg-primary: #0a0c14;
  --bg-secondary: #12141f;
  --text-primary: #ffffff;
  --text-secondary: #a0a3bd;
  --primary-blue: #3b82f6;
  --primary-blue-hover: #2563eb;
  --border-blue: #2a3a6d;
  --blue-neon: #60a5fa;
  --blue-glow: rgba(59, 130, 246, 0.5);
  --card-bg: #1A2238;
  --placeholder-color: #6C757D;
  --blue-gradient-start: #3A6BFF;
  --blue-gradient-end: #5A8BFF;
  --dark-gray: #2C3A58;
}

html, body {
  background-color: var(--bg-primary);
  color: var(--text-primary);
  margin: 0;
  padding: 0;
  height: 100%;
}

/* Opcional: para personalizar la barra de desplazamiento */
::-webkit-scrollbar {
  width: 8px;
}

::-webkit-scrollbar-track {
  background: var(--bg-secondary);
}

::-webkit-scrollbar-thumb {
  background: var(--border-blue);
  border-radius: 4px;
}

::-webkit-scrollbar-thumb:hover {
  background: var(--primary-blue);
}

#app {
  background-color: var(--bg-primary);
  color: var(--text-primary);
  min-height: 100vh;
  display: flex;
  flex-direction: column;
}

.v-main {
  flex: 1 0 auto;
  background-color: var(--bg-primary);
  min-height: calc(100vh - 120px);
  padding-top: 70px !important; /* Ajuste para el navbar fijo */
}

.main-content {
  flex: 1 0 auto;
  padding: 20px;
  margin-bottom: 60px; /* Espacio para el footer */
}

/* Asegurarse de que el footer siempre esté al final */
.v-application {
  display: flex;
  flex-direction: column;
  min-height: 100vh;
}

footer {
  position: fixed;
  bottom: 0;
  width: 100%;
  height: 20px; /* ajusta según la altura real de tu footer */
  background-color: #125A6C;
  color: white;
  z-index: 100;
}

/*
colores:
- Naranjo quemado (rgb(209, 118, 0))  #D17600 → Botones y CTAs
- Mostaza (rgb(245, 196, 72))   #F5C448 → Secciones destacadas
- Terracota (rgb(181, 85, 41)) #B55529  → Contrastes elegantes
- Gris cálido (rgb(226, 220, 210)) #E2DCD2  → Fondos
- Azul petróleo (rgb(18, 90, 108)) 	#125A6C  → Detalles y profundidad
*/
</style>

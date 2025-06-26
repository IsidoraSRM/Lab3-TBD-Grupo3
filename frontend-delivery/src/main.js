import { createApp } from 'vue'
import { createRouter, createWebHistory } from 'vue-router'
import App from './App.vue'
import { authService } from './services/authService'


// Import your view components
import HomeView from './views/HomeView.vue'
import HomeViewClient from './views/HomeViewClient.vue'
import LoginView from './views/LoginView.vue'

import 'vuetify/styles'
import { createVuetify } from 'vuetify'
import * as components from 'vuetify/components'
import * as directives from 'vuetify/directives'

// Define your routes
const routes = [
  {
    path: '/',
    name: 'Home',
    component: HomeView
  },
  {
    path: '/homeClient',
    name: 'Homee',
    component: HomeViewClient
  },
  {
    path: '/login',
    name: 'Login',
    component: LoginView,
    meta: { requiresGuest: true } // Esta ruta es solo para visitantes
  },
  {
    path: '/admin',
    name: 'Admin',
    component: () => import('./views/AdminView.vue'),  // Necesitarías crear este componente
    meta: { 
      requiresAuth: true,
      role: 'ADMIN'
    }
  },
  {
    path: '/client',
    name: 'Client',
    component: () => import('./views/ClienteView.vue'),  // Necesitarías crear este componente
    meta: { 
      requiresAuth: true,
      role: 'CLIENTE'
    }
  },
  {
    path: '/trabajador',
    name: 'Trabajador',
    component: () => import('./views/TrabajadorView.vue'),  // Necesitarías crear este componente
    meta: { 
      requiresAuth: true,
      role: 'TRABAJADOR'
    }
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

// Navegación global para proteger rutas
router.beforeEach((to, from, next) => {
  const isAuthenticated = authService.isAuthenticated();
  const userRole = authService.getUserRole();

  // Si la ruta requiere autenticación
  if (to.matched.some(record => record.meta.requiresAuth)) {
    if (!isAuthenticated) {
      next('/login');
    } else if (to.meta.role && to.meta.role !== userRole) {
      next('/'); // Redirigir a inicio si no tiene el rol requerido
    } else {
      next();
    }
  } 
  // Si la ruta es solo para visitantes (como login)
  else if (to.matched.some(record => record.meta.requiresGuest)) {
    if (isAuthenticated) {
      next('/');
    } else {
      next();
    }
  } else {
    next();
  }
});

const vuetify = createVuetify({
  components,
  directives,
})

// Create the app and use the router
const app = createApp(App)
app.use(router)
app.use(vuetify)
app.mount('#app')
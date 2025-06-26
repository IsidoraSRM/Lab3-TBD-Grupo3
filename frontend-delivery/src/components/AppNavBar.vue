<script>
import { authService } from '../services/authService';

export default {
    name: 'NavBar',
    data() {
        return {
            menuOpen: false,
            isLoggedIn: false,
            userRole: ''
        }
    },
    created() {
        this.checkAuthStatus();
    },
    mounted() {
        window.addEventListener('storage', this.checkAuthStatus);
        this.$router.afterEach(this.checkAuthStatus);
    },
    beforeUnmount() {
        window.removeEventListener('storage', this.checkAuthStatus);
    },
    methods: {
        toggleMenu() {
            this.menuOpen = !this.menuOpen;
        },
        checkAuthStatus() {
            this.isLoggedIn = authService.isAuthenticated();
            this.userRole = authService.getUserRole();
        },
        logout() {
            authService.logout();
            this.checkAuthStatus();
            this.$router.push('/login');
        }
    }
}
</script>

<template>
  <nav class="navbar">
    <div class="navbar-container">
      <div class="navbar-brand">
        <router-link to="/" class="brand-link">
          <img src="@/assets/DocDelivery.png" alt="DocDelivery Logo" class="brand-logo">
          <span class="brand-name">DocDelivery</span>
        </router-link>
      </div>

      <div class="navbar-menu">
        <div class="nav-links" :class="{ 'active': isMenuOpen }">
          <router-link to="/" class="nav-link" exact>Inicio</router-link>
          <template v-if="isLoggedIn">
            <router-link v-if="userRole === 'ADMIN'" to="/admin" class="nav-link">Panel Admin</router-link>
            <router-link v-else-if="userRole === 'CLIENTE'" to="/homeClient" class="nav-link">Panel Cliente</router-link>
            <router-link v-else-if="userRole === 'TRABAJADOR'" to="/trabajador" class="nav-link">Panel Trabajador</router-link>
            <button class="nav-link" @click="logout" style="background:none;border:none;cursor:pointer;">Cerrar Sesión</button>
          </template>
          <template v-else>
            <router-link to="/login" class="nav-link">Iniciar Sesión</router-link>
          </template>
        </div>
      </div>
    </div>
  </nav>
</template>

<style scoped>
.navbar {
  background-color: var(--bg-secondary);
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  z-index: 1000;
  border-bottom: 1px solid var(--border-blue);
  height: 70px;
  display: flex;
  align-items: center;
}

.navbar-container {
  width: 100%;
  max-width: 1400px;
  margin: 0 auto;
  padding: 0 2rem;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.navbar-brand {
  display: flex;
  align-items: center;
}

.brand-link {
  display: flex;
  align-items: center;
  text-decoration: none;
  gap: 1rem;
}

.brand-logo {
  height: 40px;
  width: auto;
  object-fit: contain;
}

.brand-name {
  font-size: 1.5rem;
  font-weight: 700;
  color: var(--text-primary);
  letter-spacing: -0.5px;
  background: linear-gradient(45deg, var(--text-primary), var(--blue-neon));
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
}

.navbar-menu {
  display: flex;
  align-items: center;
}

.nav-links {
  display: flex;
  gap: 2rem;
  align-items: center;
}

.nav-link {
  color: var(--text-secondary);
  text-decoration: none;
  font-size: 1rem;
  font-weight: 500;
  padding: 0.5rem 1rem;
  border-radius: 6px;
  transition: all 0.3s ease;
  position: relative;
}

.nav-link::after {
  content: '';
  position: absolute;
  bottom: 0;
  left: 50%;
  transform: translateX(-50%);
  width: 0;
  height: 2px;
  background-color: var(--primary-blue);
  transition: width 0.3s ease;
}

.nav-link:hover::after,
.nav-link.router-link-active::after {
  width: 80%;
}

.nav-link:hover,
.nav-link.router-link-active {
  color: var(--text-primary);
}

@media (max-width: 768px) {
  .navbar-container {
    padding: 0 1rem;
  }

  .brand-name {
    font-size: 1.25rem;
  }

  .brand-logo {
    height: 32px;
  }

  .nav-links {
    gap: 1rem;
  }

  .nav-link {
    font-size: 0.9rem;
    padding: 0.4rem 0.8rem;
  }
}
</style>
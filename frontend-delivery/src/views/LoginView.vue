<template>
  <div class="background-effects">
    <div class="floating-particles"></div>
    <div class="wave-animation"></div>
  </div>
  <div class="login-container">
    <div class="login-card" ref="cardRef">
      <h2>{{ isLogin ? 'Iniciar Sesión' : 'Registro' }}</h2>
      <div v-if="errorMessage" class="error-message">{{ errorMessage }}</div>
      
      <!-- Formulario de login -->
      <form @submit.prevent="loginUser" v-if="isLogin">
        <div class="input-group">
          <input 
            type="email" 
            id="email" 
            v-model="email" 
            required
          />
          <label for="email" :class="{ 'label-float': email }">Correo Electrónico</label>
        </div>

        <div class="input-group">
          <input 
            type="password" 
            id="password" 
            v-model="password" 
            required
          />
          <label for="password" :class="{ 'label-float': password }">Contraseña</label>
        </div>

        <button 
          type="submit" 
          class="login-button" 
          :class="{ loading }"
          :disabled="loading || !email || !password"
        >
          {{ loading ? '' : 'Iniciar Sesión' }}
        </button>
        
        <div class="links">
          <a href="#" @click.prevent="toggleForm">¿No tienes una cuenta? Regístrate</a>
        </div>
      </form>
      
      <!-- Formulario de registro -->
      <form @submit.prevent="registerUser" v-else>
        <div class="input-group">
          <input 
            type="text" 
            id="register-rut" 
            v-model="registerData.rut" 
            required
          />
          <label for="register-rut" :class="{ 'label-float': registerData.rut }">RUT</label>
        </div>
        
        <div class="input-group">
          <input 
            type="text" 
            id="register-name" 
            v-model="registerData.nameParam" 
            required
          />
          <label for="register-name" :class="{ 'label-float': registerData.nameParam }">Nombre Completo</label>
        </div>
        
        <div class="input-group">
          <input 
            type="email" 
            id="register-email" 
            v-model="registerData.email" 
            required
          />
          <label for="register-email" :class="{ 'label-float': registerData.email }">Correo Electrónico</label>
        </div>
        
        <div class="input-group">
          <input 
            type="tel" 
            id="register-phone" 
            v-model="registerData.phone" 
          />
          <label for="register-phone" :class="{ 'label-float': registerData.phone }">Teléfono</label>
        </div>
        
        <div class="input-group">
          <input 
            type="date" 
            id="register-birthdate" 
            v-model="registerData.birthdate" 
          />
          <label for="register-birthdate" :class="{ 'label-float': registerData.birthdate }">Fecha de nacimiento</label>
        </div>
        
        <div class="input-group">
          <input 
            type="password" 
            id="register-password" 
            v-model="registerData.password" 
            required
          />
          <label for="register-password" :class="{ 'label-float': registerData.password }">Contraseña</label>
        </div>
        
        <div class="input-group">
          <input 
            type="password" 
            id="register-confirm" 
            v-model="passwordConfirmation" 
            required
          />
          <label for="register-confirm" :class="{ 'label-float': passwordConfirmation }">Confirmar Contraseña</label>
        </div>

        <!-- Mapa para seleccionar ubicación -->
        <div class="map-section">
          <h3>Selecciona tu ubicación en el mapa</h3>
          <p class="map-instruction">Haz clic en el mapa para marcar tu ubicación</p>
          <div class="map-container" id="map" ref="mapContainer"></div>
          <div class="coordinates-display" v-if="registerData.latitud && registerData.longitud">
            <span>📍 Lat: {{ parseFloat(registerData.latitud).toFixed(6) }}, Lng: {{ parseFloat(registerData.longitud).toFixed(6) }}</span>
          </div>
        </div>


        <button 
          type="submit" 
          class="login-button" 
          :disabled="loading"
        >
          {{ loading ? '' : 'Registrarse' }}
        </button>
        
        <div class="links">
          <a href="#" @click.prevent="toggleForm">¿Ya tienes una cuenta? Inicia sesión</a>
        </div>
      </form>
    </div>
  </div>
</template>

<script>
import { ref, onMounted, onUnmounted, nextTick } from 'vue';
import { authService } from '../services/authService';
import { useRouter } from 'vue-router';
import axios from 'axios';
import L from 'leaflet';
import 'leaflet/dist/leaflet.css';

export default {
  name: 'LoginView',
  setup() {
    const router = useRouter();
    const cardRef = ref(null);
    const mapContainer = ref(null);
    const isLogin = ref(true);
    const loading = ref(false);
    const errorMessage = ref('');
    const email = ref('');
    const password = ref('');
    
    // Variables del mapa
    let map = null;
    let marker = null;

    const registerData = ref({
      rut: '',
      nameParam: '',
      email: '',
      phone: '',
      birthdate: '',
      password: '',
      latitud: '',      
      longitud: ''     
    });

    const passwordConfirmation = ref('');

    // Funciones del mapa
    const initMap = async () => {
      await nextTick();
      if (!mapContainer.value) return;
      
      // Fix para los iconos de Leaflet
      delete L.Icon.Default.prototype._getIconUrl;
      L.Icon.Default.mergeOptions({
        iconRetinaUrl: 'https://cdnjs.cloudflare.com/ajax/libs/leaflet/1.7.1/images/marker-icon-2x.png',
        iconUrl: 'https://cdnjs.cloudflare.com/ajax/libs/leaflet/1.7.1/images/marker-icon.png',
        shadowUrl: 'https://cdnjs.cloudflare.com/ajax/libs/leaflet/1.7.1/images/marker-shadow.png',
      });
      
      // Inicializar el mapa centrado en Chile
      map = L.map(mapContainer.value).setView([-33.4489, -70.6693], 10);
      
      // Agregar tiles del mapa
      L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
        attribution: '© OpenStreetMap contributors'
      }).addTo(map);
      
      // Manejar clics en el mapa
      map.on('click', (e) => {
        const { lat, lng } = e.latlng;
        
        // Actualizar las coordenadas
        registerData.value.latitud = lat.toString();
        registerData.value.longitud = lng.toString();
        
        // Remover marcador anterior si existe
        if (marker) {
          map.removeLayer(marker);
        }
        
        // Agregar nuevo marcador
        marker = L.marker([lat, lng]).addTo(map);
      });
      
      // Intentar obtener la ubicación actual del usuario
      if (navigator.geolocation) {
        navigator.geolocation.getCurrentPosition(
          (position) => {
            const { latitude, longitude } = position.coords;
            map.setView([latitude, longitude], 13);
            
            // Agregar marcador en la ubicación actual
            registerData.value.latitud = latitude.toString();
            registerData.value.longitud = longitude.toString();
            marker = L.marker([latitude, longitude]).addTo(map);
          },
          (error) => {
            console.log('No se pudo obtener la ubicación:', error);
          }
        );
      }
    };

    const destroyMap = () => {
      if (map) {
        map.remove();
        map = null;
        marker = null;
      }
    };

    const loginUser = async () => {
      if (!email.value || !password.value) {
        errorMessage.value = 'Completa todos los campos para iniciar sesión.';
        return;
      }
      errorMessage.value = '';
      loading.value = true;
      try {
        const res = await authService.login({
          email: email.value,
          password: password.value
        });
        const { token, userId, role } = res.data;
        localStorage.setItem('authToken', token);
        localStorage.setItem('userId', userId);
        localStorage.setItem('userRole', role);
        router.push('/');
      } catch (err) {
        errorMessage.value = err.response?.data || err.message;
        console.error('Error de login:', err);
      } finally {
        loading.value = false;
      }
    };

    const registerUser = async () => {
      if (registerData.value.password !== passwordConfirmation.value) {
        errorMessage.value = 'Las contraseñas no coinciden';
        return;
      }
      
      if (!registerData.value.latitud || !registerData.value.longitud) {
        errorMessage.value = 'Por favor selecciona tu ubicación en el mapa';
        return;
      }
      
      loading.value = true;
      errorMessage.value = '';
      
      try {
        const userData = {
          ...registerData.value,
          role: 'CLIENTE'
        };
        await axios.post(
          'http://localhost:8080/auth/register', // <-- cambia aquí temporalmente
          userData
        );
        
        alert('Registro exitoso. Por favor inicie sesión.');
        clearRegisterForm();
        isLogin.value = true;
        
      } catch (error) {
        console.error('Error de registro:', error);
        errorMessage.value = error.response?.data?.message || 
                         'Error al registrar. Verifique los datos ingresados.';
        
        if (error.response?.status === 409) {
          errorMessage.value = 'Este correo ya está registrado';
        }
      } finally {
        loading.value = false;
      }
    };

    const toggleForm = async () => {
      isLogin.value = !isLogin.value;
      errorMessage.value = '';
      
      if (!isLogin.value) {
        // Si cambiamos al formulario de registro, inicializar el mapa
        await nextTick();
        setTimeout(initMap, 100);
      } else {
        // Si cambiamos al formulario de login, destruir el mapa
        destroyMap();
      }
    };

    const clearRegisterForm = () => {
      registerData.value = {
        rut: '',
        nameParam: '',
        email: '',
        phone: '',
        birthdate: '',
        password: '',
        latitud: '',     // <- NUEVO
        longitud: ''     // <- NUEVO
      };
      passwordConfirmation.value = '';
      
      // Limpiar marcador del mapa
      if (marker && map) {
        map.removeLayer(marker);
        marker = null;
      }
    };

    onMounted(() => {
      const inputs = document.querySelectorAll('.input-group input');
      inputs.forEach(input => {
        input.addEventListener('focus', function() {
          this.parentElement.style.transform = 'scale(1.02)';
        });
        input.addEventListener('blur', function() {
          this.parentElement.style.transform = 'scale(1)';
        });
      });

      const handleMouseMove = (e) => {
        if (!cardRef.value) return;
        const rect = cardRef.value.getBoundingClientRect();
        const centerX = rect.left + rect.width / 2;
        const centerY = rect.top + rect.height / 2;
        const rotateX = (e.clientY - centerY) / 95;
        const rotateY = (centerX - e.clientX) / 90;
        cardRef.value.style.transform = `perspective(1000px) rotateX(${rotateX}deg) rotateY(${rotateY}deg)`;
      };

      const handleMouseLeave = () => {
        if (!cardRef.value) return;
        cardRef.value.style.transform = 'perspective(1000px) rotateX(0deg) rotateY(0deg)';
      };

      window.addEventListener('mousemove', handleMouseMove);
      window.addEventListener('mouseleave', handleMouseLeave);

      // Inicializar mapa si estamos en modo registro
      if (!isLogin.value) {
        setTimeout(initMap, 100);
      }

      onUnmounted(() => {
        window.removeEventListener('mousemove', handleMouseMove);
        window.removeEventListener('mouseleave', handleMouseLeave);
        destroyMap();
      });
    });

    return {
      cardRef,
      mapContainer,
      isLogin,
      loading,
      errorMessage,
      email,
      password,
      registerData,
      passwordConfirmation,
      loginUser,
      registerUser,
      toggleForm
    };
  }
}
</script>

<style scoped>
:root {
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

body {
  margin: 0;
  padding: 0;
  min-height: 100vh;
  background: linear-gradient(135deg, var(--bg-primary) 0%, var(--bg-secondary) 100%);
}

.background-effects {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100vh;
  z-index: 1;
  overflow: hidden;
}

.floating-particles::before,
.floating-particles::after {
  content: '';
  position: absolute;
  width: 200px;
  height: 200px;
  border-radius: 50%;
  background: radial-gradient(circle, var(--blue-glow) 0%, transparent 70%);
  animation: float 8s ease-in-out infinite;
}

.floating-particles::before {
  top: 20%;
  left: 10%;
  animation-delay: 0s;
}

.floating-particles::after {
  bottom: 20%;
  right: 10%;
  animation-delay: 4s;
}

.wave-animation {
  position: absolute;
  bottom: 0;
  left: 0;
  width: 100%;
  height: 100px;
  background: linear-gradient(90deg, transparent, var(--blue-glow), transparent);
  opacity: 0.1;
  animation: wave 6s linear infinite;
}

.login-container {
  position: relative;
  z-index: 10;
  width: 100%;
  max-width: 450px;
  padding: 20px;
  min-height: 100vh;
  margin: 0 auto;
  display: flex;
  align-items: center;
  justify-content: center;
}

.login-card {
  background: var(--card-bg);
  border: 1px solid var(--border-blue);
  border-radius: 20px;
  padding: 40px 30px;
  width: 100%;
  margin: 40px 0;
  box-shadow: 
    0 20px 40px rgba(0, 0, 0, 0.4),
    0 0 20px var(--blue-glow),
    inset 0 1px 0 rgba(255, 255, 255, 0.1);
  backdrop-filter: blur(10px);
  position: relative;
  overflow: hidden;
  animation: slideUp 0.6s ease-out;
}

.login-card::before {
  content: '';
  position: absolute;
  top: 0;
  left: -100%;
  width: 100%;
  height: 100%;
  background: linear-gradient(90deg, transparent, rgba(58, 107, 255, 0.1), transparent);
  animation: shimmer 3s infinite;
}

.login-card h2 {
  color: var(--text-primary);
  font-size: 2.2rem;
  font-weight: 300;
  text-align: center;
  margin-bottom: 30px;
  text-shadow: 0 0 20px var(--blue-glow);
}

.input-group {
  position: relative;
  margin-bottom: 25px;
  width: 100%;
}

.input-group input {
  width: 100%;
  padding: 12px 0;
  font-size: 16px;
  color: var(--text-primary);
  background: transparent;
  border: none;
  border-bottom: 2px solid var(--border-blue);
  outline: none;
  transition: all 0.3s ease;
}

.input-group input:focus {
  border-bottom-color: var(--primary-blue);
  box-shadow: 0 2px 10px var(--blue-glow);
}

.input-group input:focus + label,
.input-group label.label-float {
  top: -20px;
  font-size: 14px;
  color: var(--primary-blue);
  text-shadow: 0 0 10px var(--blue-glow);
  opacity: 0;
}

.input-group label {
  position: absolute;
  top: 15px;
  left: 0;
  font-size: 16px;
  color: var(--placeholder-color);
  pointer-events: none;
  transition: all 0.3s ease;
  opacity: 1;
}

.login-button {
  width: 50%;
  padding: 15px;
  font-size: 16px;
  font-weight: 600;
  color: var(--text-primary);
  background: linear-gradient(135deg, var(--blue-gradient-start) 0%, var(--blue-gradient-end) 100%);
  border: none;
  border-radius: 30px;
  cursor: pointer;
  transition: all 0.3s ease;
  margin: 20px auto;
  display: block;
  box-shadow: 0 4px 15px var(--blue-glow);
  position: relative;
  overflow: hidden;
}

.login-button:hover:not(:disabled) {
  transform: scale(1.02);
  box-shadow: 0 6px 20px var(--blue-glow);
  animation: pulse 2s infinite;
}

.login-button:disabled {
  opacity: 0.7;
  cursor: not-allowed;
  animation: none;
  transform: none;
  box-shadow: none;
}

.links {
  text-align: center;
  margin-top: 20px;
}

.links a {
  color: var(--text-secondary);
  text-decoration: none;
  font-size: 14px;
  transition: all 0.3s ease;
  position: relative;
}

.links a:hover {
  color: var(--primary-blue);
  text-shadow: 0 0 10px var(--blue-glow);
}

.links a::after {
  content: '';
  position: absolute;
  bottom: -2px;
  left: 0;
  width: 0;
  height: 1px;
  background: var(--primary-blue);
  transition: width 0.3s ease;
}

.links a:hover::after {
  width: 100%;
}

.error-message {
  background: rgba(239, 68, 68, 0.1);
  color: #ef4444;
  padding: 15px;
  border-radius: 10px;
  margin-bottom: 20px;
  text-align: center;
  border: 1px solid rgba(239, 68, 68, 0.2);
  backdrop-filter: blur(5px);
}

@keyframes float {
  0%, 100% { transform: translateY(0px) scale(1); opacity: 0.5; }
  50% { transform: translateY(-30px) scale(1.1); opacity: 0.8; }
}

@keyframes wave {
  0% { transform: translateX(-100%); }
  100% { transform: translateX(100%); }
}

@keyframes shimmer {
  0% { left: -100%; }
  100% { left: 100%; }
}

@keyframes slideUp {
  from {
    opacity: 0;
    transform: translateY(50px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

@keyframes pulse {
  0%, 100% { box-shadow: 0 0 20px var(--blue-glow); }
  50% { box-shadow: 0 0 30px var(--blue-glow), 0 0 40px var(--blue-glow); }
}

/* Estilos para el mapa */
.map-section {
  margin: 25px 0;
  text-align: center;
}

.map-section h3 {
  color: var(--text-primary);
  font-size: 1.2rem;
  margin-bottom: 10px;
  text-shadow: 0 0 10px var(--blue-glow);
}

.map-instruction {
  color: var(--text-secondary);
  font-size: 0.9rem;
  margin-bottom: 15px;
}

.map-container {
  height: 300px;
  width: 100%;
  border-radius: 15px;
  overflow: hidden;
  border: 2px solid var(--border-blue);
  box-shadow: 0 4px 15px rgba(0, 0, 0, 0.3);
  margin-bottom: 15px;
}

.coordinates-display {
  background: rgba(59, 130, 246, 0.1);
  color: var(--primary-blue);
  padding: 10px;
  border-radius: 10px;
  font-size: 0.85rem;
  border: 1px solid var(--border-blue);
  margin-top: 10px;
}

.coordinates-display span {
  display: inline-block;
  font-family: 'Courier New', monospace;
}

/* Estilos para los iconos de Leaflet */
.leaflet-default-icon-path {
  background-image: url('https://unpkg.com/leaflet@1.7.1/dist/images/marker-icon.png');
}
</style>
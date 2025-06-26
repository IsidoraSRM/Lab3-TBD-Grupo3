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

        <div class="input-group">
          <input
            type="number"
            step="any"
            id="register-latitud"
            v-model="registerData.latitud"
            required
          />
          <label for="register-latitud" :class="{ 'label-float': registerData.latitud }">Latitud</label>
        </div>

        <div class="input-group">
          <input
            type="number"
            step="any"
            id="register-longitud"
            v-model="registerData.longitud"
            required
          />
          <label for="register-longitud" :class="{ 'label-float': registerData.longitud }">Longitud</label>
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
import { ref, onMounted, onUnmounted } from 'vue';
import { authService } from '../services/authService';
import { useRouter } from 'vue-router';
import axios from 'axios';

export default {
  name: 'LoginView',
  setup() {
    const router = useRouter();
    const cardRef = ref(null);
    const isLogin = ref(true);
    const loading = ref(false);
    const errorMessage = ref('');
    const email = ref('');
    const password = ref('');

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

    const toggleForm = () => {
      isLogin.value = !isLogin.value;
      errorMessage.value = '';
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

      onUnmounted(() => {
        window.removeEventListener('mousemove', handleMouseMove);
        window.removeEventListener('mouseleave', handleMouseLeave);
      });
    });

    return {
      cardRef,
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
</style>
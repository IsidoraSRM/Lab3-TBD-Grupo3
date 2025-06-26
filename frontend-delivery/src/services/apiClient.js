import axios from 'axios';
import { authService } from './authService';

const apiClient = axios.create({
  baseURL: process.env.VUE_APP_API_URL
});

// Interceptor para incluir el token en las solicitudes
apiClient.interceptors.request.use(
  config => {
    const token = authService.getToken();
    if (token) {
      config.headers['Authorization'] = `Bearer ${token}`;
    }
    return config;
  },
  error => Promise.reject(error)
);

// Interceptor para manejar errores de autenticación
apiClient.interceptors.response.use(
  response => response,
  error => {
    if (error.response && error.response.status === 401) {
      // Si recibimos un 401 Unauthorized, limpiamos el almacenamiento local
      authService.logout();
      window.location.href = '/login';
    }
    return Promise.reject(error);
  }
);

export default apiClient;
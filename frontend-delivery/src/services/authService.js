import axios from 'axios';

const API_URL = 'http://localhost:8080';

export const authService = {
  login(credentials) {
    return axios.post(`${API_URL}/auth/login`, credentials);
  },
  
  register(userData) {
    return axios.post(`${API_URL}/auth/register`, userData)
  },

  logout() {
    localStorage.removeItem('authToken');
    localStorage.removeItem('userId');
    localStorage.removeItem('userRole');
  },

  getToken() {
    return localStorage.getItem('authToken');
  },

  getUserRole() {
    return localStorage.getItem('userRole');
  },

  isAuthenticated() {
    return !!this.getToken();
  }
}
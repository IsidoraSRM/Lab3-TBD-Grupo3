import axios from 'axios';

const API_URL = 'http://localhost:8080/api/customers';
export default {
  getClienteQueMasHaGastado() {
    return axios.get(`${API_URL}/moreSpent`, {
      headers: {
        'Authorization': `Bearer ${localStorage.getItem('authToken')}` // Requiere autenticación ADMIN
      }
    });
  },
  getAllSummaries() {
    return axios.get(`${API_URL}/order-summaries`, {
      headers: {
        'Authorization': `Bearer ${localStorage.getItem('authToken')}` // Requiere autenticación ADMIN
      }
    });
  },
  getclienteByUserId(userId) {
    return axios.get(`${API_URL}/${userId}`, {
      headers: {
        'Authorization': `Bearer ${localStorage.getItem('authToken')}` 
      }
    });
  },
  
  getClientesMasDe5Km() {
    return axios.get(`${API_URL}/clientesMasDe5Km`, {
      headers: { 'Authorization': `Bearer ${localStorage.getItem('authToken')}` }
    });
  },
  verificarClienteEnZona(idCliente) {
    return axios.get(`${API_URL}/cliente-en-zona/${idCliente}`, {
      headers: { 'Authorization': `Bearer ${localStorage.getItem('authToken')}` }
    });
  }

};
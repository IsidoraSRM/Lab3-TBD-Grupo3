import axios from 'axios';

const API_URL = 'http://localhost:8080/api/empresas';

const API_URL2 = 'http://localhost:8080/api/pedidos';

export default {
  getCompanyRanking() {
    return axios.get(`${API_URL}/top-ranking`, {
      headers: {
        'Authorization': `Bearer ${localStorage.getItem('authToken')}` // Requiere autenticación ADMIN
      }
    });
  },


  getEmpresaConMasEntregasFallidas() {
    return axios.get(`${API_URL2}/entregas-fallidas-por-empresa`, {
      headers: {
        'Authorization': `Bearer ${localStorage.getItem('authToken')}`,
        'Content-Type': 'application/json'
      }
    });
  }


};

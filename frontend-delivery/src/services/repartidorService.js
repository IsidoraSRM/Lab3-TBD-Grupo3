import axios from 'axios';

const API_URL = 'http://localhost:8080/api/repartidores';

export default {
  getTop3Repartidores() {
    return axios.get(`${API_URL}/top3`, {
      headers: {
        'Authorization': `Bearer ${localStorage.getItem('authToken')}`
      }
    });
  },
  getDesempenoRepartidor() {
    return axios.get(`${API_URL}/desempeno`, {
      headers: {
        'Authorization': `Bearer ${localStorage.getItem('authToken')}`
      }
    });
  },
  getDistanciasRecorridasUltimoMes() {
    return axios.get(`${API_URL}/distancia-ultimo-mes`, {
      headers: {
        'Authorization': `Bearer ${localStorage.getItem('authToken')}`
      }
    });
  }
};
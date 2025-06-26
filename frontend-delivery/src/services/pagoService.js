import axios from 'axios';

const API_BASE_URL = 'http://localhost:8080/api';

export default {
  // Método para obtener el medio de pago más usado en pedidos urgentes
  getMedioPagoMasUsadoUrgentes() {
    return axios.get(`${API_BASE_URL}/pagos/mas-usado-en-urgentes`, {
      headers: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${localStorage.getItem('authToken')}`
      }
    });
  },
};
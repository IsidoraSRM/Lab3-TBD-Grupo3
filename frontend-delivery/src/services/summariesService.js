import axios from 'axios';

const API_URL = 'http://localhost:8080/api/order-summaries';

export default {
  getAllSummaries() {
    return axios.get(`${API_URL}/all`, {
      headers: {
        'Authorization': `Bearer ${localStorage.getItem('authToken')}` // Requiere autenticación ADMIN
      }
    });
  } 
};

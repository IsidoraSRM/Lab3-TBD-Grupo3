import axios from 'axios';

const API_URL = 'http://localhost:8080/api';

export const rutasFrecuentesService = {

  async getTopTramosFrecuentes(topN = 10) {
    try {
      const response = await axios.get(`${API_URL}/historial-repartidores/top`, {
        params: { topN },
        headers: {
          'Authorization': `Bearer ${localStorage.getItem('authToken')}`,
          'Content-Type': 'application/json'
        }
      });
      return response.data;
    } catch (error) {
      console.error('Error al obtener top tramos frecuentes:', error);
      throw error;
    }
  }
};
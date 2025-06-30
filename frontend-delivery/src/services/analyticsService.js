import axios from 'axios';

const API_URL = 'http://localhost:8080/api';

export const analyticsService = {

  async getPromedioPorEmpresa() {
    try {
      const response = await axios.get(`${API_URL}/opiniones/promedio-por-empresa`, {
        headers: {
          'Authorization': `Bearer ${localStorage.getItem('authToken')}`,
          'Content-Type': 'application/json'
        }
      });
      return response.data;
    } catch (error) {
      console.error('Error al obtener promedio por empresa:', error);
      throw error;
    }
  },

  /**
   * Obtiene los patrones de opiniones por hora
   * @returns {Promise} Lista de opiniones por hora
   */
  async getPatronesPorHora() {
    try {
      const response = await axios.get(`${API_URL}/opiniones/patrones-por-hora`, {
        headers: {
          'Authorization': `Bearer ${localStorage.getItem('authToken')}`,
          'Content-Type': 'application/json'
        }
      });
      return response.data;
    } catch (error) {
      console.error('Error al obtener patrones por hora:', error);
      throw error;
    }
  },


  async getPedidosConMuchosCambios() {
  try {
    const response = await axios.get(`${API_URL}/opiniones/pedidos-cambios-rapidos`, {
      headers: {
        'Authorization': `Bearer ${localStorage.getItem('authToken')}`,
        'Content-Type': 'application/json'
      }
    });
    return response.data;
  } catch (error) {
    console.error('Error al obtener pedidos con cambios rápidos:', error);
    throw error;
  }
},

async getClientesSinCompra() {
  try {
    const response = await axios.get(`${API_URL}/opiniones/clientes-sin-compra`, {
      headers: {
        'Authorization': `Bearer ${localStorage.getItem('authToken')}`,
        'Content-Type': 'application/json'
      }
    });
    return response.data;
  } catch (error) {
    console.error('Error al obtener clientes sin compra:', error);
    throw error;
  }
},


  async getOpinionesConPalabrasClave() {
  try {
    const response = await axios.get(`${API_URL}/opiniones/palabras-clave`, {
      headers: {
        'Authorization': `Bearer ${localStorage.getItem('authToken')}`,
        'Content-Type': 'application/json'
      }
    });
    return response.data;
  } catch (error) {
    console.error('Error al obtener opiniones con palabras clave:', error);
    throw error;
  }
}



};
import axios from 'axios';

const API_URL = 'http://localhost:8080/orders';

const API_URL2 = 'http://localhost:8080/api/pedidos'

const orderService = {
  
  crearPedido(pedidoDto) {
    return axios.post(`${API_URL}/crear`, pedidoDto, {
      headers: { Authorization: `Bearer ${localStorage.getItem('authToken')}` }
    });
  },

  getTiempoPromedioEntregaPorRepartidor() {
    return axios.get(`${API_URL2}/tiempo-promedio-entrega-por-repartidor`, {
      headers: {
        'Authorization': `Bearer ${localStorage.getItem('authToken')}`,
        'Content-Type': 'application/json'
      }
    });
  },

  
  getPedidoById(id) {
    return axios.get(`${API_URL}/${id}`, {
      headers: { Authorization: `Bearer ${localStorage.getItem('authToken')}` }
    });
  },

  registerOrder(orderData) {
    const params = new URLSearchParams({
      clienteId: orderData.clienteId,
      prioridad: orderData.prioridad,
      nombreMetodo: orderData.nombreMetodo,
      monto: orderData.monto,
      nombre_servicio: orderData.nombre_servicio,
      descripcion: orderData.descripcion,
      categoria: orderData.categoria,
      direccionInicio: orderData.direccionInicio,
      direccionDestino: orderData.direccionDestino
    });

    return axios.post(`${API_URL2}/register?${params}`, null, {
      headers: { Authorization: `Bearer ${localStorage.getItem('authToken')}` }
    });
  },

  cambiarEstadoPedido(idPedido, estado) {
  return axios.put(`${API_URL2}/${idPedido}/estado?nuevoEstado=${estado}`, null, {
    headers: { Authorization: `Bearer ${localStorage.getItem('authToken')}` }
  });
  },

  confirmarPedido(idPedido) {
    return axios.post(`${API_URL2}/confirmar/${idPedido}`, null, {
      headers: { Authorization: `Bearer ${localStorage.getItem('authToken')}` }
    });
  },

  getPedidosByClienteId(clienteId, cacheBuster = null) {
      let url = `${API_URL2}/cliente/${clienteId}`;
      if (cacheBuster) {
          url += `?timestamp=${cacheBuster}`;
      }
      
      return axios.get(url, {
          headers: { 
              Authorization: `Bearer ${localStorage.getItem('authToken')}`,
              'Content-Type': 'application/json'
          },
          transformResponse: [function (data) {
              console.log("Datos crudos recibidos:", data);
              return JSON.parse(data);
          }]
      });
  },

  getAllOrders() {
    return axios.get(`${API_URL}/all`, {
      headers: { Authorization: `Bearer ${localStorage.getItem('authToken')}` }
    });
  },
  getPedidosByRepartidor(repartidorId) {
    return axios.get(`${API_URL2}/repartidor/${repartidorId}`, {
      headers: { Authorization: `Bearer ${localStorage.getItem('authToken')}` }
    });
  },
  getPedidosByUserId(repartidorId) {
    return axios.get(`${API_URL}/repartidor/${repartidorId}`, {
      headers: { Authorization: `Bearer ${localStorage.getItem('authToken')}` }
    });
  },
  getPedidosZonasCruzadas() {
    return axios.get(`${API_URL2}/zonas-cruzadas`, {
      headers: { Authorization: `Bearer ${localStorage.getItem('authToken')}` }
    });
  },
  getZonaCliente(clienteId) {
    return axios.get(`${API_URL2}/extra1/${clienteId}`, {
      headers: { Authorization: `Bearer ${localStorage.getItem('authToken')}` }
    });
  },
  getHighDensityZones() {
    return axios.get(`${API_URL2}/extra2`, {
      headers: { Authorization: `Bearer ${localStorage.getItem('authToken')}` }
    });
  }
};

export default orderService;
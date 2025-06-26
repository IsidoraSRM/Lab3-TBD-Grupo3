package com.Docdelivery.Backend.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.Docdelivery.Backend.Entity.DetallePedidoEntity;
import com.Docdelivery.Backend.Entity.OrderEntity;
import com.Docdelivery.Backend.Entity.RepartidorEntity;
import com.Docdelivery.Backend.Repository.DetallePedidoRepository;
import com.Docdelivery.Backend.Repository.OrderRepository;
import com.Docdelivery.Backend.Repository.RepartidorRepository;
import com.Docdelivery.Backend.dto.ClusterZoneDto;
import com.Docdelivery.Backend.dto.CrearPedidoDto;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private DetallePedidoRepository detallePedidoRepository;
    @Autowired
    private RepartidorRepository repartidorRepository;

    @Transactional
    public Long crearPedido(CrearPedidoDto pedidoDto) {
        // Crear la entidad de pedido
        OrderEntity order = new OrderEntity();
        order.setClienteId(pedidoDto.getClienteId());
        order.setFechaPedido(LocalDateTime.now());
        order.setEstadoPedido("PENDIENTE");
        order.setPrioridadPedido(pedidoDto.getPrioridadPedido());
        
        // Guardar el pedido primero para obtener su ID
        Long idPedido = orderRepository.save(order);
        
        // Procesar cada detalle del pedido
        
        DetallePedidoEntity detalle = new DetallePedidoEntity();
        detalle.setIdPedido(idPedido);
        detalle.setIdServicio(pedidoDto.getIdServicio());
        detalle.setCantidad(pedidoDto.getCantidad());
        detallePedidoRepository.save(detalle);
        
        
        return idPedido;
    }

    public Optional<OrderEntity> getPedidoById(Long id) {
        return orderRepository.findById(id);
    }

    public List<DetallePedidoEntity> getDetallesByPedidoId(Long idPedido) {
        return detallePedidoRepository.findByPedidoId(idPedido);
    }

    //Procedure registerOrder
    public int registerOrder(int clienteId, String prioridad, String nombreMetodo, int monto,String nombre_servicio,String descripcion, String categoria, String direccionInicio,String direccionDestino) {
        return orderRepository.registerOrder(clienteId,prioridad,nombreMetodo,monto,nombre_servicio,descripcion,categoria,direccionInicio,direccionDestino);
    }

    //Procedure-> cambiarEstadoPedido
    public boolean cambiarEstado(int idPedido, String nuevoEstado) {
        return orderRepository.cambiarEstadoPedido(idPedido, nuevoEstado);
    }
    
    //Procedure confirmarPedido
    public int confirmarPedido(int idPedido) {
        return orderRepository.confirmarPedido(idPedido);
    }


    public List<OrderEntity> getAllOrders() {
        return orderRepository.findAll();
    }


    // filepath: c:\Users\Public\Desktop\Lab1-TBD-Grupo3\Backend\src\main\java\com\Docdelivery\Backend\Service\OrderService.java
    public List<Map<String, Object>> getOrdersByRepartidorId(Long idUsuario) {
        Optional<Long> repartidorId = repartidorRepository.findRepartidorIdByUsuarioId(idUsuario);
        
        System.out.println("ID Usuario: " + idUsuario);
        System.out.println("Repartidor encontrado: " + repartidorId.isPresent());
        if (repartidorId.isPresent()) {
            System.out.println("ID Repartidor: " + repartidorId.get());
            List<Map<String, Object>> result = orderRepository.getPedidosConClienteYDetalleByRepartidorId(repartidorId.get());
            System.out.println("Número de pedidos encontrados: " + result.size());
            return result;
        } else {
            System.out.println("No se encontró un repartidor para este usuario");
            return Collections.emptyList();
        }
    }
    
    // Listar las empresas asociadas con más entregas fallidas
    public List<Map<String, Object>> getEntregasFallidasPorEmpresa() {
        return orderRepository.getEntregasFallidasPorEmpresa();
    }

    // Calcular el tiempo promedio entre pedido y entrega por repartidor
    public List<Map<String, Object>> getTiempoPromedioEntregaPorRepartidor() {
        return orderRepository.getTiempoPromedioEntregaPorRepartidor();
    }

    public List<OrderEntity> getPedidosByClienteId(Long clienteId) {
        return orderRepository.findByClienteId(clienteId);
    }

    // Si quieres incluir los detalles
    public List<Map<String, Object>> getPedidosConDetallesByClienteId(Long clienteId) {
        return orderRepository.getPedidosConDetallesByClienteId(clienteId);
    }


    // --------------------------- Lab 2 ---------------------------

    // Consulta 5: Listar todos los pedidos cuya ruta estimada cruce más de 2 zonas de reparto.
    public List<Map<String, Object>> getPedidosConClienteYDetalleByRutaEstimadaCruce2Zonas() {
        return orderRepository.getPedidosConClienteYDetalleByRutaEstimadaCruce2Zonas();
    }


    // --------------------------------EXTRAS-----------------------------

    // EXTRA 1 : Implementar una función que calcule automáticamente la zona a la que pertenece un cliente.
    public String getZoneForClient(int clienteId) {
        return orderRepository.calcularZonaCliente(clienteId);
    }
    // EXTRA 2 : Detectar zonas con alta densidad de pedidos mediante agregación de puntos.
     
    public List<ClusterZoneDto> getHighDensityZones() {
        return orderRepository.findHighDensityZones();
    }
}
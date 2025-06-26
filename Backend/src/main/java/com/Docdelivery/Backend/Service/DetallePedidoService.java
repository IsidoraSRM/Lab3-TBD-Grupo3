package com.Docdelivery.Backend.Service;

import com.Docdelivery.Backend.Entity.DetallePedidoEntity;
import com.Docdelivery.Backend.Repository.DetallePedidoRepository;
import com.Docdelivery.Backend.dto.ServiciosDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class DetallePedidoService {

    private final DetallePedidoRepository detallePedidoRepository;

    @Autowired
    public DetallePedidoService(DetallePedidoRepository detallePedidoRepository) {
        this.detallePedidoRepository = detallePedidoRepository;
    }

    // Create
    public void save(DetallePedidoEntity detalle) {
        detallePedidoRepository.save(detalle);
    }

    // Read - Get all detalles
    public List<DetallePedidoEntity> findAll() {
        return detallePedidoRepository.findAll();
    }

    // Read - Get detalles by pedido ID
    public List<DetallePedidoEntity> findByPedidoId(Long idPedido) {
        return detallePedidoRepository.findByPedidoId(idPedido);
    }

    // Update
    public boolean update(DetallePedidoEntity detalle) {
        try {
            detallePedidoRepository.update(detalle);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // Delete
    public boolean deleteById(Long id) {
        return detallePedidoRepository.deleteById(id);
    }

    // Get servicios más pedidos (existing method)
    public List<ServiciosDto> obtenerServiciosMasPedidos() {
        return detallePedidoRepository.getServiciosMasPedidosUltimoMes();
    }


    // --------------------------- Lab 2 ---------------------------

    // Consulta 4: Identificar el punto de entrega más lejano desde cada empresa asociada.
    public List<Map<String, Object>> getPuntosEntregaMasLejano() {
        return detallePedidoRepository.getPuntosEntregaMasLejano();
    }


    //Consulta 1 para obtener los 5 puntos de entrega más cercanos a la empresa "Express Chile"
    public List<Map<String, Object>> obtenerEntregasCercanasAEmpresa(String nombreEmpresa) {
        return detallePedidoRepository.obtenerEntregasCercanasAEmpresa(nombreEmpresa);
    }

}
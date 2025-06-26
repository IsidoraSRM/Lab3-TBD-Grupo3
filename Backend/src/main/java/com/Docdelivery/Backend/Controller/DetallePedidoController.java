package com.Docdelivery.Backend.Controller;

import com.Docdelivery.Backend.Entity.DetallePedidoEntity;
import com.Docdelivery.Backend.Service.DetallePedidoService;
import com.Docdelivery.Backend.dto.ServiciosDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/detallepedidos")
@CrossOrigin(origins = "*")
public class DetallePedidoController {

    private final DetallePedidoService detallePedidoService;

    @Autowired
    public DetallePedidoController(DetallePedidoService detallePedidoService) {
        this.detallePedidoService = detallePedidoService;
    }

    // Create
    @PostMapping
    @Secured({"ROLE_ADMIN", "ROLE_CLIENTE"})
    public ResponseEntity<Void> createDetallePedido(@RequestBody DetallePedidoEntity detalle) {
        try {
            detallePedidoService.save(detalle);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Read - Get all
    @GetMapping
    @Secured({"ROLE_ADMIN", "ROLE_CLIENTE"})
    public ResponseEntity<List<DetallePedidoEntity>> getAllDetalles() {
        List<DetallePedidoEntity> detalles = detallePedidoService.findAll();
        return ResponseEntity.ok(detalles);
    }

    // Read - Get by pedido ID
    @GetMapping("/pedido/{idPedido}")
    @Secured({"ROLE_ADMIN", "ROLE_CLIENTE"})
    public ResponseEntity<List<DetallePedidoEntity>> getDetallesByPedidoId(@PathVariable Long idPedido) {
        List<DetallePedidoEntity> detalles = detallePedidoService.findByPedidoId(idPedido);
        if (detalles.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(detalles);
    }

    // Update
    @PutMapping("/{id}")
    @Secured({"ROLE_ADMIN"})
    public ResponseEntity<Void> updateDetallePedido(
            @PathVariable Long id,
            @RequestBody DetallePedidoEntity detalle) {
        detalle.setIdDetallePedido(id);
        boolean updated = detallePedidoService.update(detalle);
        if (updated) {
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    // Delete
    @DeleteMapping("/{id}")
    @Secured({"ROLE_ADMIN"})
    public ResponseEntity<Void> deleteDetallePedido(@PathVariable Long id) {
        boolean deleted = detallePedidoService.deleteById(id);
        if (deleted) {
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    // Get servicios más pedidos (existing endpoint)
    @GetMapping("/masPedidos")
    @Secured({"ROLE_CLIENTE", "ROLE_ADMIN"})
    public ResponseEntity<List<ServiciosDto>> getServiciosMasPedidos() {
        return ResponseEntity.ok(detallePedidoService.obtenerServiciosMasPedidos());
    }


    // --------------------------- Lab 2 ---------------------------

    // Consulta 4: Identificar el punto de entrega más lejano desde cada empresa asociada.
    @GetMapping("/punto-lejano")
    @Secured({"ROLE_CLIENTE", "ROLE_ADMIN", "ROLE_TRABAJADOR"})
    public ResponseEntity<List<Map<String, Object>>> getPuntosEntregaMasLejano() {
        List<Map<String, Object>> puntosEntregaMasLejano = detallePedidoService.getPuntosEntregaMasLejano();
        return ResponseEntity.ok(puntosEntregaMasLejano);
    }


    //Consulta 1 para obtener los 5 puntos de entrega más cercanos a la empresa "Express Chile"
    @GetMapping("/entregasCercanas")
    @Secured({"ROLE_ADMIN", "ROLE_CLIENTE", "ROLE_TRABAJADOR"})
    public ResponseEntity<List<Map<String, Object>>> obtenerEntregasCercanas(
            @RequestParam String empresa) {
        List<Map<String, Object>> entregas = detallePedidoService.obtenerEntregasCercanasAEmpresa(empresa);
        return ResponseEntity.ok(entregas);
    }

}
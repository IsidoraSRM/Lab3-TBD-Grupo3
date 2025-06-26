package com.Docdelivery.Backend.Controller;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import com.Docdelivery.Backend.Entity.DetallePedidoEntity;
import com.Docdelivery.Backend.Entity.OrderEntity;
import com.Docdelivery.Backend.Service.OrderService;
import com.Docdelivery.Backend.dto.ClusterZoneDto;
import com.Docdelivery.Backend.dto.CrearPedidoDto;
import java.util.Collections;

@RestController
@RequestMapping("/api/pedidos")
@CrossOrigin
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/crear")
    @Secured({"ROLE_CLIENTE", "ROLE_ADMIN"})
    public ResponseEntity<?> crearPedido(@RequestBody CrearPedidoDto pedidoDto) {
        try {
        // Obtener la información de autenticación actual
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        
        // Registrar información detallada sobre la autenticación
        System.out.println("=== DIAGNÓSTICO DE AUTENTICACIÓN ===");
        System.out.println("¿Usuario autenticado? " + auth.isAuthenticated());
        System.out.println("Nombre de usuario: " + auth.getName());
        System.out.println("Tipo de principal: " + (auth.getPrincipal() != null ? auth.getPrincipal().getClass().getName() : "null"));
        System.out.println("Roles/Autoridades: " + auth.getAuthorities().stream()
                .map(a -> a.getAuthority())
                .collect(Collectors.joining(", ")));
        System.out.println("Roles requeridos: ROLE_CLIENTE, ROLE_ADMIN");
        System.out.println("¿Tiene ROLE_CLIENTE? " + auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_CLIENTE")));
        System.out.println("¿Tiene ROLE_ADMIN? " + auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN")));
        System.out.println("===================================");
        
        // Continuar con la lógica existente
        Long idPedido = orderService.crearPedido(pedidoDto);
        
        Map<String, Object> response = new HashMap<>();
        response.put("mensaje", "Pedido creado con éxito");
        response.put("idPedido", idPedido);
        
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    } catch (AccessDeniedException e) {
        // Capturar específicamente errores de acceso denegado
        System.out.println("Acceso denegado: " + e.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body("Acceso denegado: No tienes los roles necesarios (ROLE_CLIENTE o ROLE_ADMIN)");
    } catch (Exception e) {
        System.out.println("Error en crearPedido: " + e.getClass().getName() + " - " + e.getMessage());
        e.printStackTrace();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error al crear el pedido: " + e.getMessage());
    }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getPedidoById(@PathVariable Long id) {
        Optional<OrderEntity> pedido = orderService.getPedidoById(id);
        if (pedido.isPresent()) {
            OrderEntity order = pedido.get();
            List<DetallePedidoEntity> detalles = orderService.getDetallesByPedidoId(id);
            
            Map<String, Object> response = new HashMap<>();
            response.put("pedido", order);
            response.put("detalles", detalles);
            
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.notFound().build();
    }

    //Procedure registerOrder
    @PostMapping("/register")
    @Secured({"ROLE_CLIENTE", "ROLE_ADMIN"})
    public int registerOrder(
            @RequestParam int clienteId, 
            @RequestParam String prioridad, 
            @RequestParam String nombreMetodo, 
            @RequestParam int monto,
            @RequestParam String nombre_servicio,
            @RequestParam String descripcion, 
            @RequestParam String categoria, 
            @RequestParam String direccionInicio,
            @RequestParam String direccionDestino
    ) {
        return orderService.registerOrder(clienteId,prioridad, nombreMetodo, monto, nombre_servicio, descripcion, categoria, direccionInicio, direccionDestino);
    }


    //Procedimiento para cambiar el estado de un pedido
    @Secured({"ROLE_TRABAJADOR", "ROLE_ADMIN"})
    @PutMapping("/{id}/estado")
    public ResponseEntity<String> cambiarEstadoPedido(
            @PathVariable("id") int idPedido,
            @RequestParam("nuevoEstado") String nuevoEstado) {

        boolean exito = orderService.cambiarEstado(idPedido, nuevoEstado);

        if (exito) {
            return ResponseEntity.ok("Estado del pedido actualizado correctamente");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error al actualizar el estado del pedido");
        }
    }

    //Procedure confirmarPedido
    @PostMapping("/confirmar/{idPedido}")
    @Secured({"ROLE_TRABAJADOR", "ROLE_ADMIN"})
    public ResponseEntity<?> confirmarPedido(@PathVariable int idPedido) {
        try {
            int resultado = orderService.confirmarPedido(idPedido);
            if (resultado > 0) {
                return ResponseEntity.ok("Pedido confirmado con éxito");
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Error al confirmar el pedido");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al confirmar el pedido: " + e.getMessage());
        }
    }

    // llamr a todas las ordenes
    @GetMapping("/all")
    @Secured({"ROLE_CLIENTE", "ROLE_ADMIN", "ROLE_TRABAJADOR"})
    public ResponseEntity<List<OrderEntity>> getAllOrders() {
        List<OrderEntity> orders = orderService.getAllOrders();
        return ResponseEntity.ok(orders);
    }

    // Obtener todos los pedidos por repartidor
    @GetMapping("/repartidor/{repartidorId}")
    @Secured({"ROLE_CLIENTE", "ROLE_ADMIN", "ROLE_TRABAJADOR"})
    public ResponseEntity<List<Map<String, Object>>> getPedidosByRepartidor(@PathVariable Long repartidorId) {
        List<Map<String, Object>> pedidos = orderService.getOrdersByRepartidorId(repartidorId);
        return ResponseEntity.ok(pedidos);
    }

    // Listar las empresas asociadas con más entregas fallidas
    @GetMapping("/entregas-fallidas-por-empresa")
    @Secured({"ROLE_CLIENTE", "ROLE_ADMIN", "ROLE_TRABAJADOR"})
    public ResponseEntity<List<Map<String, Object>>> getEntregasFallidasPorEmpresa() {
        try {
            List<Map<String, Object>> entregasFallidas = orderService.getEntregasFallidasPorEmpresa();
            if (entregasFallidas.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(entregasFallidas);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.emptyList());
        }
    }

    // Calcular el tiempo promedio entre pedido y entrega por repartidor
    @GetMapping("/tiempo-promedio-entrega-por-repartidor")
    @Secured({"ROLE_CLIENTE", "ROLE_ADMIN", "ROLE_TRABAJADOR"})
    public ResponseEntity<List<Map<String, Object>>> getTiempoPromedioEntregaPorRepartidor() {
        try {
            List<Map<String, Object>> tiempoPromedio = orderService.getTiempoPromedioEntregaPorRepartidor();
            if (tiempoPromedio.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(tiempoPromedio);
        }
        catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.emptyList());
        }
    }

    @GetMapping("/cliente/{clienteId}")
    @Secured({"ROLE_CLIENTE", "ROLE_ADMIN"})
    public ResponseEntity<List<OrderEntity>> getPedidosSimpleByClienteId(@PathVariable Long clienteId) {
        List<OrderEntity> pedidos = orderService.getPedidosByClienteId(clienteId);
        return ResponseEntity.ok(pedidos);
    }


    // --------------------------- Lab 2 ---------------------------

    // Consulta 5: Listar todos los pedidos cuya ruta estimada cruce más de 2 zonas de reparto.
    @GetMapping("/zonas-cruzadas")
    @Secured({"ROLE_CLIENTE", "ROLE_ADMIN", "ROLE_TRABAJADOR"})
    public ResponseEntity<List<Map<String, Object>>> getPedidosConRutaCruce2Zonas() {
        List<Map<String, Object>> pedidos = orderService.getPedidosConClienteYDetalleByRutaEstimadaCruce2Zonas();
        return ResponseEntity.ok(pedidos);
    }


    // --------------------------------EXTRAS-----------------------------

    // EXTRA 1 : Implementar una función que calcule automáticamente la zona a la que pertenece un cliente.
    @GetMapping("/extra1/{id}")
    public ResponseEntity<String> obtenerZonaCliente(@PathVariable("id") int clienteId) {
        String zona = orderService.getZoneForClient(clienteId);
        return ResponseEntity.ok(zona);
    }
    // EXTRA 2 : Detectar zonas con alta densidad de pedidos mediante agregación de puntos.
    @GetMapping("/extra2")
    @Secured({"ROLE_CLIENTE", "ROLE_ADMIN", "ROLE_TRABAJADOR"})
    public List<ClusterZoneDto> highDensityZones() {
        return orderService.getHighDensityZones();
    }
}
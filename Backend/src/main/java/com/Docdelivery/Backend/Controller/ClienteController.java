package com.Docdelivery.Backend.Controller;

import com.Docdelivery.Backend.Entity.ClienteEntity;
import com.Docdelivery.Backend.Service.ClienteServices;
import com.Docdelivery.Backend.dto.ClienteConTotalGastadoDTO; // Asegúrate de importar el DTO
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/customers")
public class ClienteController {

    @Autowired
    private ClienteServices clienteServices;

    @GetMapping("/moreSpent")
    @Secured({"ROLE_ADMIN"}) // Se asume que el admin es quien puede acceder a esta consulta
    public Optional<ClienteConTotalGastadoDTO> moreSpent() {
        try {
            Optional<ClienteConTotalGastadoDTO> cliente = clienteServices.obtenerClienteMasAGastado();
            return cliente;
        } catch (Exception e) {
            return Optional.empty();
        }
    }
    @GetMapping("/{usuarioId}")
    @Secured({"ROLE_CLIENTE", "ROLE_ADMIN", "ROLE_TRABAJADOR"})
    public Object findClienteIdByUsuarioId(@PathVariable Long usuarioId) {
        return clienteServices.findClienteIdByUsuarioId(usuarioId);
    }

    @PutMapping("/update/{id}")
    @Secured({"ROLE_CLIENTE", "ROLE_ADMIN", "ROLE_TRABAJADOR"})
    public void updateCliente(@PathVariable Long id, @RequestBody ClienteEntity cliente) {
        try{
            clienteServices.actualizarCliente(id);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @DeleteMapping("/{id}")
    @Secured({"ROLE_CLIENTE", "ROLE_ADMIN", "ROLE_TRABAJADOR"})
    public void deleteCliente(@PathVariable Long id) {
        try {
            clienteServices.eliminarCliente(id);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }


    // --------------------------- Lab 2 ---------------------------

    // Consulta 6: Determinar los clientes que están a más de 5km de cualquier empresa o farmacia.
    @GetMapping("/clientesMasDe5Km")
    @Secured({"ROLE_CLIENTE", "ROLE_ADMIN", "ROLE_TRABAJADOR"})
    public ResponseEntity<List<Map<String, Object>>> obtenerClientesMasDe5Km() {
        List<Map<String, Object>> clientesMasDe5Km = clienteServices.obtenerClientesMasDe5Km();
        return ResponseEntity.ok(clientesMasDe5Km);
    }

    // Consulta 2: Verificar si los clientes están dentro de una zona de cobertura (con buffer de 1km)
    @GetMapping("/cliente-en-zona/{id}")
    @Secured({"ROLE_ADMIN", "ROLE_TRABAJADOR", "ROLE_CLIENTE"})
    public ResponseEntity<List<Map<String, Object>>> verificarClienteEnZona(@PathVariable Long id) {
        List<Map<String, Object>> resultado = clienteServices.verificarClienteEnZona(id);
        return ResponseEntity.ok(resultado);
    }


}

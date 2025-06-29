package com.Docdelivery.Backend.Controller;

import com.Docdelivery.Backend.Entity.NavegacionUsuariosEntity;
import com.Docdelivery.Backend.Repository.NavegacionUsuariosRepository;
import com.Docdelivery.Backend.Service.NavegacionUsuariosServices;
import com.Docdelivery.Backend.dto.ClienteSinCompraDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/navegacion")
@CrossOrigin(origins = "*")
public class NavegacionUsuariosController {

	@Autowired
	private NavegacionUsuariosServices navegacionService;

	/**
	 * ENDPOINT PRINCIPAL: Consulta 5 del enunciado
	 * Detectar clientes que realizaron búsquedas sin concretar pedidos
	 */
	@GetMapping("/clientes-sin-compra")
	public ResponseEntity<Map<String, Object>> getClientesSinCompra() {
		try {
			List<ClienteSinCompraDTO> clientes = navegacionService.detectarClientesSinCompra();

			Map<String, Object> response = new HashMap<>();
			response.put("success", true);
			response.put("mensaje", "Clientes con búsquedas sin compra encontrados");
			response.put("total_clientes", clientes.size());
			response.put("clientes", clientes);
			response.put("consulta", "5. Detectar clientes que realizaron búsquedas sin concretar pedidos (navegación sin compra)");

			return ResponseEntity.ok(response);

		} catch (Exception e) {
			Map<String, Object> errorResponse = new HashMap<>();
			errorResponse.put("success", false);
			errorResponse.put("error", "Error al obtener clientes sin compra: " + e.getMessage());

			return ResponseEntity.internalServerError().body(errorResponse);
		}
	}
}
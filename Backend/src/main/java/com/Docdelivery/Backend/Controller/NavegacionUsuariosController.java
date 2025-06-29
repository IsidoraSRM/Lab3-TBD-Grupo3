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
@RequestMapping("/api/navegacion-usuarios")
public class NavegacionUsuariosController {

	@Autowired
	private NavegacionUsuariosRepository navegacionUsuariosRepository;
	@Autowired
	private NavegacionUsuariosServices navegacionUsuariosServices;

	@GetMapping("/")
	@Secured({"ROLE_ADMIN", "ROLE_REPARTIDOR"})
	public List<NavegacionUsuariosEntity> findAll() {
		return navegacionUsuariosRepository.findAll();
	}

	@PostMapping("/")
	@Secured({"ROLE_ADMIN", "ROLE_REPARTIDOR"})
	public NavegacionUsuariosEntity addNavegacionUsuario(@RequestBody NavegacionUsuariosEntity navegacionUsuariosEntity) {
		return navegacionUsuariosRepository.save(navegacionUsuariosEntity);
	}
	@GetMapping("/clientes-sin-compra")
	public ResponseEntity<Map<String, Object>> getClientesSinCompra() {
		try {
			List<ClienteSinCompraDTO> clientes = navegacionUsuariosServices.detectarClientesSinCompra();

			Map<String, Object> response = new HashMap<>();
			response.put("success", true);
			response.put("message", "Consulta ejecutada exitosamente");
			response.put("total_clientes", clientes.size());
			response.put("clientes", clientes);

			return ResponseEntity.ok(response);

		} catch (Exception e) {
			Map<String, Object> errorResponse = new HashMap<>();
			errorResponse.put("success", false);
			errorResponse.put("message", "Error al ejecutar la consulta: " + e.getMessage());

			return ResponseEntity.internalServerError().body(errorResponse);
		}
	}
	@GetMapping("/clientes-sin-compra/alternativo")
	public ResponseEntity<Map<String, Object>> getClientesSinCompraAlternativo() {
		try {
			List<ClienteSinCompraDTO> clientes = navegacionUsuariosServices.detectarClientesSinCompraAlternativo();

			Map<String, Object> response = new HashMap<>();
			response.put("success", true);
			response.put("message", "Consulta alternativa ejecutada exitosamente");
			response.put("total_clientes", clientes.size());
			response.put("clientes", clientes);
			response.put("metodo", "Java Stream Processing");

			return ResponseEntity.ok(response);

		} catch (Exception e) {
			Map<String, Object> errorResponse = new HashMap<>();
			errorResponse.put("success", false);
			errorResponse.put("message", "Error al ejecutar la consulta alternativa: " + e.getMessage());

			return ResponseEntity.internalServerError().body(errorResponse);
		}
	}
	@GetMapping("/estadisticas-navegacion")
	public ResponseEntity<Map<String, Object>> getEstadisticasNavegacion() {
		try {
			List<ClienteSinCompraDTO> clientesSinCompra = navegacionUsuariosServices.detectarClientesSinCompra();

			// Calcular estadísticas
			int totalClientes = clientesSinCompra.size();
			int totalBusquedas = clientesSinCompra.stream()
					.mapToInt(ClienteSinCompraDTO::getCantidadBusquedas)
					.sum();
			int totalClicks = clientesSinCompra.stream()
					.mapToInt(ClienteSinCompraDTO::getCantidadClicks)
					.sum();

			double promedioBusquedasPorCliente = totalClientes > 0 ?
					(double) totalBusquedas / totalClientes : 0;

			Map<String, Object> estadisticas = new HashMap<>();
			estadisticas.put("total_clientes_sin_compra", totalClientes);
			estadisticas.put("total_busquedas", totalBusquedas);
			estadisticas.put("total_clicks", totalClicks);
			estadisticas.put("promedio_busquedas_por_cliente", Math.round(promedioBusquedasPorCliente * 100.0) / 100.0);
			estadisticas.put("conversion_rate", "0% (ningún cliente completó compra)");

			Map<String, Object> response = new HashMap<>();
			response.put("success", true);
			response.put("estadisticas", estadisticas);
			response.put("detalle_clientes", clientesSinCompra);

			return ResponseEntity.ok(response);

		} catch (Exception e) {
			Map<String, Object> errorResponse = new HashMap<>();
			errorResponse.put("success", false);
			errorResponse.put("message", "Error al calcular estadísticas: " + e.getMessage());

			return ResponseEntity.internalServerError().body(errorResponse);
		}
	}
}

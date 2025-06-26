package com.Docdelivery.Backend.Controller;


import com.Docdelivery.Backend.Entity.PagoEntity;
import com.Docdelivery.Backend.Service.PagoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/pagos")
@CrossOrigin
public class 	PagoController {

	@Autowired
	private PagoService pagoService;

	// Obtener todos los pagos
	@GetMapping("/")
	public ResponseEntity<List<PagoEntity>> getAllPagos() {
		try {
			List<PagoEntity> pagos = pagoService.getAllPagos();
			return ResponseEntity.ok(pagos);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(Collections.emptyList());
		}
	}

	// Obtener un pago por ID
	@GetMapping("/{id}")
	public ResponseEntity<?> getPagoById(@PathVariable Long id) {
		try {
			Optional<PagoEntity> pago = pagoService.getPagoById(id);
			if (pago.isPresent()) {
				return ResponseEntity.ok(pago.get());
			} else {
				return ResponseEntity.status(HttpStatus.NOT_FOUND)
						.body("Pago no encontrado con ID: " + id);
			}
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error al buscar el pago: " + e.getMessage());
		}
	}

	// Crear un nuevo pago
	@PostMapping("/")
	public ResponseEntity<?> createPago(@RequestBody PagoEntity pago) {
		try {
			pagoService.createPago(pago);
			return ResponseEntity.status(HttpStatus.CREATED)
					.body("Pago creado exitosamente");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error al crear el pago: " + e.getMessage());
		}
	}

	// Obtener el medio de pago más usado en pedidos urgentes
	@GetMapping("/mas-usado-en-urgentes")
	@Secured({"ROLE_CLIENTE", "ROLE_ADMIN", "ROLE_TRABAJADOR"})
	public ResponseEntity<?> getMedioDePagoMasUsadoEnUrgentes() {
		try {
			Map<String, Object> metodoMasUsado = pagoService.getMedioDePagoMasUsadoEnUrgentes();
			if (metodoMasUsado != null && !metodoMasUsado.isEmpty()) {
				return ResponseEntity.ok(metodoMasUsado);
			} else {
				return ResponseEntity.status(HttpStatus.NOT_FOUND)
						.body("No se encontró ningún método de pago para pedidos urgentes.");
			}
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error interno al consultar el método de pago: " + e.getMessage());
		}
	}
}

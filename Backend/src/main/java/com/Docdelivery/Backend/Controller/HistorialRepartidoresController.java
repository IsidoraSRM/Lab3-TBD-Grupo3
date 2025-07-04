package com.Docdelivery.Backend.Controller;

import com.Docdelivery.Backend.Entity.HistorialRepartidoresEntity;
import com.Docdelivery.Backend.Entity.LogsPedidosEntity;
import com.Docdelivery.Backend.Repository.HistorialRepartidoresRepository;
import com.Docdelivery.Backend.Repository.LogsPedidosRepository;
import com.Docdelivery.Backend.Service.RutasFrecuentesService;
import com.Docdelivery.Backend.Service.RutasFrecuentesService.TramoFrecuenteDto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/historial-repartidores")
public class HistorialRepartidoresController {

	@Autowired
	LogsPedidosRepository logsPedidosRepository;
	@Autowired
	private HistorialRepartidoresRepository historialRepartidoresRepository;
	@Autowired
	private RutasFrecuentesService rutasFrecuentesService;

	@GetMapping("/")
	@Secured({"ROLE_ADMIN", "ROLE_REPARTIDOR"})
	public List<LogsPedidosEntity> getAll() {
		return logsPedidosRepository.findAll();
	}

	@PostMapping("/")
	@Secured({"ROLE_ADMIN", "ROLE_REPARTIDOR"})
	public HistorialRepartidoresEntity addHistorialRepartidor(@RequestBody HistorialRepartidoresEntity historialRepartidoresEntity) {
		return historialRepartidoresRepository.save(historialRepartidoresEntity);
	}

	@GetMapping("/top")
    @Secured({"ROLE_ADMIN", "ROLE_REPARTIDOR"})
    public List<TramoFrecuenteDto> getTopTramosFrecuentes(
            @RequestParam(defaultValue = "10") int topN
    ) {
        return rutasFrecuentesService.obtenerTramosFrecuentesUltimos7Dias(topN);
    }
}

package com.Docdelivery.Backend.Controller;

import com.Docdelivery.Backend.Entity.NavegacionUsuariosEntity;
import com.Docdelivery.Backend.Repository.NavegacionUsuariosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/navegacion-usuarios")
public class NavegacionUsuariosController {

	@Autowired
	private NavegacionUsuariosRepository navegacionUsuariosRepository;

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
}

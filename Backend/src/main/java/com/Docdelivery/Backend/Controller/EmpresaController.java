package com.Docdelivery.Backend.Controller;

import com.Docdelivery.Backend.Entity.EmpresaAsociadaEntity;
import com.Docdelivery.Backend.Service.EmpresaService;
import com.Docdelivery.Backend.dto.VistaEmpresaDto;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/empresas")
public class EmpresaController {

    private final EmpresaService empresaService;

    public EmpresaController(EmpresaService empresaService) {
        this.empresaService = empresaService;
    }

    // 🔹 Obtener todas las empresas con paginación
    @GetMapping
    @Secured({"ROLE_CLIENTE", "ROLE_ADMIN"})
    public List<EmpresaAsociadaEntity> obtenerEmpresas(@RequestParam(defaultValue = "10") int limit,
                                                       @RequestParam(defaultValue = "0") int offset) {
        return empresaService.findAll(limit, offset);
    }

    // 🔹 Obtener una empresa por ID
    @GetMapping("/{id}")
    @Secured({"ROLE_CLIENTE", "ROLE_ADMIN"})
    public Optional<EmpresaAsociadaEntity> obtenerEmpresaPorId(@PathVariable Long id) {
        return empresaService.findById(id);
    }

    // Crear nueva empresa
    @PostMapping
    @Secured({"ROLE_ADMIN"})
    public void crearEmpresa(@RequestBody EmpresaAsociadaEntity empresa) {
        empresaService.save(empresa);
    }

    // Actualizar empresa existente
    @PutMapping("/{id}")
    @Secured({"ROLE_ADMIN"})
    public void actualizarEmpresa(@PathVariable Long id, @RequestBody EmpresaAsociadaEntity empresa) {
        empresa.setIdEmpresaAsociada(id);  // Asegurar que el ID es el correcto
        empresaService.save(empresa);
    }

    // Eliminar empresa por ID
    @DeleteMapping("/{id}")
    @Secured({"ROLE_ADMIN"})
    public void eliminarEmpresa(@PathVariable Long id) {
        empresaService.deleteById(id);
    }


    @GetMapping("/top-ranking")
    @Secured({"ROLE_CLIENTE", "ROLE_ADMIN"})
    public List<VistaEmpresaDto> obtenerEmpresasConRanking1() {
        return empresaService.obtenerEmpresasConRanking1();
    }
}

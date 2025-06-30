package com.Docdelivery.Backend.Controller;

import com.Docdelivery.Backend.Entity.OpinionClienteEntity;
import com.Docdelivery.Backend.Repository.OpinionClienteRepository;
import com.Docdelivery.Backend.dto.OpinionClienteDTO;
import com.Docdelivery.Backend.dto.OpinionesPorHoraDto;
import com.Docdelivery.Backend.dto.PromedioPuntuacionEmpresaDto;
import com.Docdelivery.Backend.Service.OpinionClienteService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/opiniones")
public class OpinionClienteController {

    @Autowired
    private OpinionClienteRepository repo;

    @Autowired
    private OpinionClienteService analyticsService;

    @GetMapping("/all")
    @Secured({"ROLE_ADMIN", "ROLE_CLIENTE"})
    public List<OpinionClienteEntity> getAll() {
        return repo.findAll();
    }

    @PostMapping("/save")
    @Secured({"ROLE_ADMIN", "ROLE_CLIENTE"})
    public OpinionClienteEntity addOpinion(@RequestBody OpinionClienteEntity opinion) {
        return repo.save(opinion);
    }

    @GetMapping("/promedio-por-empresa")
    @Secured({"ROLE_ADMIN", "ROLE_CLIENTE"})
    public List<PromedioPuntuacionEmpresaDto> getPromedioPorEmpresa() {
        return analyticsService.getPromedioPorEmpresaConNombre();
    }

    @GetMapping("/patrones-por-hora")
    @Secured({"ROLE_ADMIN", "ROLE_CLIENTE"})
    public List<OpinionesPorHoraDto> getPatronesPorHora() {
        return analyticsService.getOpinionesPorHora();
    }

    @GetMapping("/palabras-clave")
    @Secured({"ROLE_ADMIN", "ROLE_CLIENTE"})
    public List<OpinionClienteDTO> getOpinionesConPalabrasClave() {
        return analyticsService.buscarOpinionesConPalabrasClave();
    }
}
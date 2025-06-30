package com.Docdelivery.Backend.Controller;

import com.Docdelivery.Backend.Service.LogsPedidosService;
import com.Docdelivery.Backend.Entity.LogsPedidosEntity;
import com.Docdelivery.Backend.Repository.LogsPedidosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/logs-pedidos")
public class LogsPedidosController {

    @Autowired
    private LogsPedidosRepository repo;

    @Autowired
    private LogsPedidosService service;

    @GetMapping("/all")
    @Secured({"ROLE_ADMIN", "ROLE_REPARTIDOR"})
    public List<LogsPedidosEntity> getAll() {
        return repo.findAll();
    }

    @GetMapping("/cambios-rapidos")
    @Secured({"ROLE_ADMIN", "ROLE_CLIENTE"})
    public List<String> getPedidosConCambiosRapidos() {
        return service.getPedidosConCambiosRapidos();
    }



    @PostMapping("/save")
    @Secured({"ROLE_ADMIN", "ROLE_REPARTIDOR"})
    public LogsPedidosEntity addLog(@RequestBody LogsPedidosEntity log) {
        return repo.save(log);
    }
}
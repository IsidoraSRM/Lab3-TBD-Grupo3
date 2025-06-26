package com.Docdelivery.Backend.Controller;

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

    @GetMapping("/all")
    @Secured({"ROLE_ADMIN", "ROLE_REPARTIDOR"})
    public List<LogsPedidosEntity> getAll() {
        return repo.findAll();
    }



    @PostMapping("/save")
    @Secured({"ROLE_ADMIN", "ROLE_REPARTIDOR"})
    public LogsPedidosEntity addLog(@RequestBody LogsPedidosEntity log) {
        return repo.save(log);
    }
}
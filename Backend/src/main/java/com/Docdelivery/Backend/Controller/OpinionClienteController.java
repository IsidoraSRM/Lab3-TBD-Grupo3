package com.Docdelivery.Backend.Controller;

import com.Docdelivery.Backend.Entity.OpinionClienteEntity;
import com.Docdelivery.Backend.Repository.OpinionClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/opiniones")
public class OpinionClienteController {

    @Autowired
    private OpinionClienteRepository repo;

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
}
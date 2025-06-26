// src/main/java/com/Docdelivery/Backend/Controller/RepartidorController.java
package com.Docdelivery.Backend.Controller;

import com.Docdelivery.Backend.Entity.RepartidorEntity;
import com.Docdelivery.Backend.dto.RepartidorDistanciaDTO;
import com.Docdelivery.Backend.dto.TopRepartidorDto;
import com.Docdelivery.Backend.Service.RepartidorService;
import com.Docdelivery.Backend.dto.VistaRepartidorDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/repartidores")
@CrossOrigin
public class RepartidorController {

    @Autowired
    private RepartidorService repartidorService;

    @PostMapping
    @Secured("ROLE_ADMIN")
    public ResponseEntity<Void> createRepartidor(@RequestBody RepartidorEntity repartidor) {
        try {
            repartidorService.createRepartidor(repartidor);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping
    @Secured({"ROLE_ADMIN", "ROLE_TRABAJADOR"})
    public ResponseEntity<List<RepartidorEntity>> getAllRepartidores() {
        List<RepartidorEntity> repartidores = repartidorService.getAllRepartidores();
        return ResponseEntity.ok(repartidores);
    }

    @GetMapping("/{id}")
    @Secured({"ROLE_ADMIN", "ROLE_TRABAJADOR"})
    public ResponseEntity<RepartidorEntity> getRepartidorById(@PathVariable Long id) {
        return repartidorService.getRepartidorById(id)
                .map(ResponseEntity::ok)
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PutMapping("/{id}")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<Void> updateRepartidor(
            @PathVariable Long id,
            @RequestBody RepartidorEntity repartidor) {
        repartidor.setRepartidorId(id);
        boolean updated = repartidorService.updateRepartidor(repartidor);
        if (updated) {
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{id}")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<Void> deleteRepartidor(@PathVariable Long id) {
        boolean deleted = repartidorService.deleteRepartidor(id);
        if (deleted) {
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }


    @GetMapping("/top3")
    public ResponseEntity<List<TopRepartidorDto>> getTop3Repartidores() {
        List<TopRepartidorDto> topRepartidores = repartidorService.findTop3ByRendimiento();
        return ResponseEntity.ok(topRepartidores);
    }

    //Llamada a la vista de repartidores
    @GetMapping("/desempeno")
    @Secured({"ROLE_ADMIN"})
    public ResponseEntity<List<VistaRepartidorDto>> obtenerDesempeno() {
        return ResponseEntity.ok(repartidorService.obtenerDesempeno());
    }

    //comsulta 3 LAB 2
    @GetMapping("/distancia-ultimo-mes")
    @Secured({"ROLE_ADMIN", "ROLE_TRABAJADOR"})
    public ResponseEntity<List<RepartidorDistanciaDTO>> getDistanciasRecorridas() {
        List<RepartidorDistanciaDTO> distancias = repartidorService.obtenerDistanciasUltimoMes();
        return ResponseEntity.ok(distancias);
    }

}
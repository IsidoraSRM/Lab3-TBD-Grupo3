package com.Docdelivery.Backend.Controller;

import com.Docdelivery.Backend.Entity.MedioDePagoEntity;
import com.Docdelivery.Backend.Service.MedioDePagoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/mediosdepago")
@CrossOrigin(origins = "*")
public class MedioDePagoController {

    private final MedioDePagoService medioDePagoService;

    @Autowired
    public MedioDePagoController(MedioDePagoService medioDePagoService) {
        this.medioDePagoService = medioDePagoService;
    }

    // Create
    @PostMapping
    @Secured("ROLE_ADMIN")
    public ResponseEntity<Void> createMedioDePago(@RequestBody MedioDePagoEntity medioDePago) {
        try {
            medioDePagoService.save(medioDePago);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Read - Get All
    @GetMapping
    @Secured({"ROLE_ADMIN", "ROLE_CLIENTE"})
    public ResponseEntity<List<MedioDePagoEntity>> getAllMediosDePago() {
        List<MedioDePagoEntity> mediosDePago = medioDePagoService.findAll();
        return ResponseEntity.ok(mediosDePago);
    }

    // Read - Get by ID
    @GetMapping("/{id}")
    @Secured({"ROLE_ADMIN", "ROLE_CLIENTE"})
    public ResponseEntity<MedioDePagoEntity> getMedioDePagoById(@PathVariable Long id) {
        return medioDePagoService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // Update
    @PutMapping("/{id}")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<Void> updateMedioDePago(
            @PathVariable Long id,
            @RequestBody MedioDePagoEntity medioDePago) {
        medioDePago.setIdMetodoDePago(id);
        boolean updated = medioDePagoService.update(medioDePago);
        if (updated) {
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    // Delete
    @DeleteMapping("/{id}")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<Void> deleteMedioDePago(@PathVariable Long id) {
        boolean deleted = medioDePagoService.deleteById(id);
        if (deleted) {
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
package com.Docdelivery.Backend.Controller;

import com.Docdelivery.Backend.Entity.RatingEntity;
import com.Docdelivery.Backend.Service.RatingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ratings")
public class RatingController {

    @Autowired
    private RatingService ratingService;

    @PostMapping
    public ResponseEntity<Long> createRating(@RequestBody RatingEntity rating) {
        Long id = ratingService.createRating(rating);
        return ResponseEntity.status(HttpStatus.CREATED).body(id);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RatingEntity> getRatingById(@PathVariable Long id) {
        return ratingService.getRatingById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public List<RatingEntity> getAllRatings() {
        return ratingService.getAllRatings();
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateRating(@PathVariable Long id, @RequestBody RatingEntity rating) {
        rating.setIdCalificacion(id);
        boolean updated = ratingService.updateRating(rating);
        return updated ? ResponseEntity.ok("Rating actualizado")
                : ResponseEntity.status(HttpStatus.NOT_FOUND).body("Rating no encontrado");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteRating(@PathVariable Long id) {
        boolean deleted = ratingService.deleteRating(id);
        return deleted ? ResponseEntity.ok("Rating eliminado")
                : ResponseEntity.status(HttpStatus.NOT_FOUND).body("Rating no encontrado");
    }
}


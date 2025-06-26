package com.Docdelivery.Backend.Service;

import com.Docdelivery.Backend.Entity.RatingEntity;
import com.Docdelivery.Backend.Repository.RatingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RatingService {
    @Autowired
    private RatingRepository ratingRepository;

    public Long createRating(RatingEntity rating) {
        return ratingRepository.save(rating);
    }

    public Optional<RatingEntity> getRatingById(Long id) {
        return ratingRepository.findById(id);
    }

    public List<RatingEntity> getAllRatings() {
        return ratingRepository.findAll();
    }

    public boolean updateRating(RatingEntity rating) {
        return ratingRepository.update(rating);
    }

    public boolean deleteRating(Long id) {
        return ratingRepository.delete(id);
    }
}

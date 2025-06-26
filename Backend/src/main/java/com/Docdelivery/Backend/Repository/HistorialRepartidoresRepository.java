package com.Docdelivery.Backend.Repository;

import com.Docdelivery.Backend.Entity.HistorialRepartidoresEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface HistorialRepartidoresRepository extends MongoRepository<HistorialRepartidoresEntity, String> {
}

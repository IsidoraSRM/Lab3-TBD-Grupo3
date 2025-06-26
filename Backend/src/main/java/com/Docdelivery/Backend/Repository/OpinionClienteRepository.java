package com.Docdelivery.Backend.Repository;

import com.Docdelivery.Backend.Entity.OpinionClienteEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface OpinionClienteRepository extends MongoRepository<OpinionClienteEntity, String> {
}
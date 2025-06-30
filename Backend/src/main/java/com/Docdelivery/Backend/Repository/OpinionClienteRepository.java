package com.Docdelivery.Backend.Repository;

import com.Docdelivery.Backend.Entity.OpinionClienteEntity;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface OpinionClienteRepository extends MongoRepository<OpinionClienteEntity, String> {
    
    @Query("{ 'comentario': { $regex: '.*demora.*|.*error.*', $options: 'i' }}")
    List<OpinionClienteEntity> findByPalabrasClave();
}



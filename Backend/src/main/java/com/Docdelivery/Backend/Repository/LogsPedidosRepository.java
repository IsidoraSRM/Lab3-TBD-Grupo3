package com.Docdelivery.Backend.Repository;

import com.Docdelivery.Backend.Entity.LogsPedidosEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface LogsPedidosRepository extends MongoRepository<LogsPedidosEntity, String> {
}

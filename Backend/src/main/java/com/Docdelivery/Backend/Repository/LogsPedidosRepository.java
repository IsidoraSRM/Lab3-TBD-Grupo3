
package com.Docdelivery.Backend.Repository;

import com.Docdelivery.Backend.Entity.LogsPedidosEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface LogsPedidosRepository extends MongoRepository<LogsPedidosEntity, String> {
    

}
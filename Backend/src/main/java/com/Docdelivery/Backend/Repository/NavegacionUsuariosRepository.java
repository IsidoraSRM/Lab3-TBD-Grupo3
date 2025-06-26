package com.Docdelivery.Backend.Repository;

import com.Docdelivery.Backend.Entity.NavegacionUsuariosEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface NavegacionUsuariosRepository extends MongoRepository<NavegacionUsuariosEntity, String> {
}

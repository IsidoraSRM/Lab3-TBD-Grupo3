package com.Docdelivery.Backend.Repository;

import com.Docdelivery.Backend.Entity.OpinionClienteEntity;

import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface OpinionClienteRepository extends MongoRepository<OpinionClienteEntity, String> {

/*     @Aggregation(pipeline = {
            "{ $group: { _id: '$empresa_id', promedio_puntuacion: { $avg: '$puntuacion' }, total_opiniones: { $sum: 1 } } }",
            "{ $sort: { promedio_puntuacion: -1 } }"
    })
    List<Object> findPromedioPuntuacionPorEmpresa();
 */
}
package com.Docdelivery.Backend.Repository;

import com.Docdelivery.Backend.Entity.NavegacionUsuariosEntity;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NavegacionUsuariosRepository extends MongoRepository<NavegacionUsuariosEntity, String> {

    // Método para encontrar navegación por cliente
    List<NavegacionUsuariosEntity> findByClienteId(String clienteId);

    // Usando agregación personalizada (se implementará en el service)
    @Aggregation(pipeline = {
            "{ $match: { 'eventos.tipo': 'busqueda' } }",
            "{ $match: { 'eventos.tipo': { $ne: 'compra' } } }",
            "{ $project: { cliente_id: 1, eventos: 1 } }"
    })
    List<NavegacionUsuariosEntity> findClientesConBusquedaSinCompra();
}

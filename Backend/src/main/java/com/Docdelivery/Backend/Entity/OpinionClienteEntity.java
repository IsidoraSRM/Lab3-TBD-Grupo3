package com.Docdelivery.Backend.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "opiniones_clientes")
public class OpinionClienteEntity {
    @Id
    private String id;
    private String cliente_id;
    private String empresa_id;
    private String comentario;
    private int puntuacion;
    private Instant fecha;
}
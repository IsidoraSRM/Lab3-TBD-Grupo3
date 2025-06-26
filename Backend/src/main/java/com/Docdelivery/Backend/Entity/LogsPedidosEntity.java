package com.Docdelivery.Backend.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "logs_pedidos")
public class LogsPedidosEntity {
    @Id
    private String id;
    private String pedido_id;
    private List<EventoPedido> eventos;
    
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class EventoPedido {
        private String estado;
        private Instant timestamp;
    }
}
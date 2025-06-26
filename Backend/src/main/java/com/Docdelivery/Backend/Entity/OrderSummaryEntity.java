package com.Docdelivery.Backend.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderSummaryEntity {
    private Long clienteId;
    private String nombreCliente;
    private String emailCliente;
    private Integer totalPedidos;
    private Double montoTotalGastado;
    
}

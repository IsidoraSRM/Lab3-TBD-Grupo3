package com.Docdelivery.Backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;



@Data
@NoArgsConstructor
@AllArgsConstructor
public class CrearPedidoDto {
    private Long clienteId;
    private String prioridadPedido;
    private Long idServicio;
    private Integer cantidad;
    
}

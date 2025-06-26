package com.Docdelivery.Backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClienteConTotalGastadoDTO {
    private Long clienteId;
    private String nombre;
    private String email;
    private String direccion;
    private String telefono;
    private Double totalGastado;
}

package com.Docdelivery.Backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClienteSinCompraDTO {
    private Long clienteId; // Cambiado a Long para coincidir con PostgreSQL
    private String nombre; // Nuevo campo del cliente
    private String email; // Nuevo campo del cliente
    private String telefono; // Nuevo campo del cliente
    private int cantidadBusquedas;
    private int cantidadClicks;
    private LocalDateTime ultimaBusqueda;
    private List<String> terminosBuscados;
}
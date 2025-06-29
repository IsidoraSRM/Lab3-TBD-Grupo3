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
    private String clienteId;
    private int cantidadBusquedas;
    private int cantidadClicks;
    private LocalDateTime ultimaBusqueda;
    private List<String> terminosBuscados;
}
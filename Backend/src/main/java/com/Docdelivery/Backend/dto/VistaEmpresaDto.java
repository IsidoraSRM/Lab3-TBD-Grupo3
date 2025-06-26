package com.Docdelivery.Backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VistaEmpresaDto {
    private String nombreEmpresa;
    private int totalServiciosEntregados;
    private int ranking; // Nuevo atributo para manejar el ranking
}


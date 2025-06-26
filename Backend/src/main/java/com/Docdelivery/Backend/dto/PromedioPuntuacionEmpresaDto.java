package com.Docdelivery.Backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PromedioPuntuacionEmpresaDto {
    private String empresaId;
    private String nombreEmpresa;  
    private Double promedioPuntuacion;
    private Integer totalOpiniones;
}
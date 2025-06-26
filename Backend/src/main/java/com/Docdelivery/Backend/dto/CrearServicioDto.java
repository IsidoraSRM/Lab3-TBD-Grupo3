package com.Docdelivery.Backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CrearServicioDto {
    private Long idServicio;
    private Long idEmpresaAsociada;
    private String nombreServicio;
    private String descripcionServicio;
    private Integer precioServicio;
    private String catergoriaServicio;
}

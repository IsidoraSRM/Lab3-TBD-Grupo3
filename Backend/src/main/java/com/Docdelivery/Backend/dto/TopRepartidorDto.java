package com.Docdelivery.Backend.dto;

import lombok.Data;

@Data
public class TopRepartidorDto {
    private Long repartidorId;
    private String nombre;
    private String telefono;
    private Boolean disponible;
    private Long entregasCompletadas;
    private Double puntuacionPromedio;
    private Double rendimiento;
}
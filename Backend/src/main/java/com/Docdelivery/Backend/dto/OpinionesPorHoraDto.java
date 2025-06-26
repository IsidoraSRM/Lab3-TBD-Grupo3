package com.Docdelivery.Backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OpinionesPorHoraDto {
    private Integer hora;                    // Hora del día (0-23)
    private Double promedioPuntuacion;       // Promedio de satisfacción en esa hora
    private Integer totalOpiniones;          // Cantidad de opiniones en esa hora
    private Double puntuacionMaxima;         // Puntuación más alta en esa hora
    private Double puntuacionMinima;         // Puntuación más baja en esa hora
}
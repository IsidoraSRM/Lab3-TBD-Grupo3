package com.Docdelivery.Backend.Entity;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RatingEntity {
    private long idCalificacion;
    // C(R) one to one P(O)
    private long idPedido;
    private Integer score;
    private String comment;

}
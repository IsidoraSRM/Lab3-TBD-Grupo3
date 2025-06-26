package com.Docdelivery.Backend.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.locationtech.jts.geom.Point;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RepartidorEntity {
    private Long repartidorId;
    private String nombre;
    private String telefono;
    private Boolean disponible;
    private Point ubicacion_actual;
}

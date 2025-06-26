package com.Docdelivery.Backend.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.locationtech.jts.geom.Polygon;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class ZonasCoberturaEntity {
    private Long zona_id;
    private String nombre;
    private Polygon geom;



}

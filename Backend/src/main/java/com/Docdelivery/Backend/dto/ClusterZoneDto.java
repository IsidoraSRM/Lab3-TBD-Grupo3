package com.Docdelivery.Backend.dto;

import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.Point;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClusterZoneDto {
  private int clusterId;
  private String zonaCobertura;
  private int cantidadPedidos;
  private String centroWkt;
  private String geomGeoJson; 
}

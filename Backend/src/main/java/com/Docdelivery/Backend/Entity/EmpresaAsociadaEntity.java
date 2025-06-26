package com.Docdelivery.Backend.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.locationtech.jts.geom.Point;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmpresaAsociadaEntity {
    public Long idEmpresaAsociada;
    public String nombreEmpresa;
    public String direccionEmpresa;
    public String tipoServicio;
    public Point ubicacion;
}

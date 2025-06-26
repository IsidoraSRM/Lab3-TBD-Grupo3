package com.Docdelivery.Backend.dto;

import lombok.*;
import org.locationtech.jts.geom.Point;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterDto {
    private String rut;
    private String nameParam;
    private String email;
    private String phone;
    private Date birthdate;
    private String password;
    private String role;
    private Double latitud;
    private Double longitud;
}

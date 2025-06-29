package com.Docdelivery.Backend.dto;

import lombok.*;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OpinionClienteDTO {
	private String cliente_id;
	private String empresa_id;
	private String comentario;
	private int puntuacion;
	private Instant fecha;
}

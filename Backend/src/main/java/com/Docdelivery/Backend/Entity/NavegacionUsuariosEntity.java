package com.Docdelivery.Backend.Entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "navegacion_usuarios")
public class NavegacionUsuariosEntity {
	@Id
	private String id;
	private List<Eventos> eventosList;

	@Data
	@AllArgsConstructor
	@NoArgsConstructor
	public static class Eventos {
		private String tipo;
		private String valor;
		private Instant timestamp;
	}
}

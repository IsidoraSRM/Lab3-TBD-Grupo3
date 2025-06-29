package com.Docdelivery.Backend.Entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "navegacion_usuarios")
public class NavegacionUsuariosEntity {

	@Id
	private String id;

	@Field("cliente_id")
	private String clienteId;

	private List<Evento> eventos;
	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	public static class Evento {
		private String tipo;
		private String valor;
		private LocalDateTime timestamp;
	}
}

package com.Docdelivery.Backend.Entity;

import org.springframework.data.annotation.Id;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "historial_repartidores")
public class HistorialRepartidoresEntity {
	@Id
	private String id;
	private List<Rutas> rutasList;

	@Data
	@AllArgsConstructor
	@NoArgsConstructor
	public static class Rutas {
		private Double lat;
		private Double lng;
		private Instant timestamp;
	}
}

package com.Docdelivery.Backend.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MedioDePagoEntity {
	private long idMetodoDePago;
	private String nombreMetodoDePago;
	// MP oneToOne P
}

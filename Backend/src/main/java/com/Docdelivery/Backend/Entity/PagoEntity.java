package com.Docdelivery.Backend.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PagoEntity {
	private Long idPago;
		// P oneToOne P(O)
	private Long idPedido;
	// MP oneToOne P
	private Long idMedioDePago;
	private BigDecimal monto;

}

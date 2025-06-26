package com.Docdelivery.Backend.Repository;


import com.Docdelivery.Backend.Entity.PagoEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class PagoRepository {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	// RowMapper para mapear los resultados a PagoEntity
	private static class PagoRowMapper implements RowMapper<PagoEntity> {
		@Override
		public PagoEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
			return new PagoEntity(
					rs.getLong("id_pago"),
					rs.getLong("id_pedido"),
					rs.getLong("id_medio_de_pago"),
					rs.getBigDecimal("monto")
			);
		}
	}

	// Insertar pago en la base de datos
	public void save(PagoEntity pago) {
		String sql = "INSERT INTO pago (id_pedido, id_medio_de_pago, monto) VALUES (?, ?, ?)";
		jdbcTemplate.update(sql, pago.getIdPedido(), pago.getIdMedioDePago(), pago.getMonto());
	}

	// Buscar pago por ID
	public Optional<PagoEntity> findById(Long idPago) {
		String sql = "SELECT * FROM pago WHERE id_pago = ?";
		try {
			return Optional.ofNullable(jdbcTemplate.queryForObject(sql, new PagoRowMapper(), idPago));
		} catch (Exception e) {
			return Optional.empty();
		}
	}

	// Obtener todos los pagos
	public List<PagoEntity> findAll() {
		String sql = "SELECT * FROM pago";
		return jdbcTemplate.query(sql, new PagoRowMapper());
	}

	// ¿Qué medio de pago se utiliza más en pedidos urgentes?
	public List<Map<String, Object>> findMedioDePagoMasUsadoEnUrgentes() {
		String sql = """
    SELECT mediodepago.nombremetododepago, COUNT(*) AS cantidad
    FROM pago
    JOIN orderentity ON orderentity.idpedido = pago.idpedido
    JOIN mediodepago ON mediodepago.idmetododepago = pago.idmediodepago
    WHERE orderentity.prioridadpedido ILIKE 'ALTA' OR orderentity.prioridadpedido ILIKE 'URGENTE'
    GROUP BY mediodepago.nombremetododepago
    ORDER BY COUNT(*) DESC
    LIMIT 1
    """;

		return jdbcTemplate.queryForList(sql);
	}
}

package com.Docdelivery.Backend.Repository;

import com.Docdelivery.Backend.Entity.OrderSummaryEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
public class OrderSummaryRepository {
    private final JdbcTemplate jdbcTemplate;

    public OrderSummaryRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private static class OrderSummaryRowMapper implements RowMapper<OrderSummaryEntity> {
        @Override
        public OrderSummaryEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
            OrderSummaryEntity summary = new OrderSummaryEntity();
            summary.setClienteId(rs.getLong("cliente_id"));
            summary.setNombreCliente(rs.getString("nombre_cliente"));
            summary.setEmailCliente(rs.getString("email_cliente"));
            summary.setTotalPedidos(rs.getInt("total_pedidos"));
            summary.setMontoTotalGastado(rs.getDouble("monto_total_gastado"));
            return summary;
        }
    }

    // Create
    public void save(OrderSummaryEntity summary) {
        String sql = "INSERT INTO order_summary (cliente_id, nombre_cliente, email_cliente, total_pedidos, monto_total_gastado) " +
                    "VALUES (?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql,
            summary.getClienteId(),
            summary.getNombreCliente(),
            summary.getEmailCliente(),
            summary.getTotalPedidos(),
            summary.getMontoTotalGastado()
        );
    }

    // Read All
    public List<OrderSummaryEntity> findAll() {
        String sql = "SELECT * FROM order_summary";
        return jdbcTemplate.query(sql, new OrderSummaryRowMapper());
    }

    // Read by ID
    public Optional<OrderSummaryEntity> findById(Long clienteId) {
        String sql = "SELECT * FROM order_summary WHERE cliente_id = ?";
        List<OrderSummaryEntity> results = jdbcTemplate.query(sql, new OrderSummaryRowMapper(), clienteId);
        return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
    }

    // Update
    public boolean update(OrderSummaryEntity summary) {
        String sql = "UPDATE order_summary SET nombre_cliente = ?, email_cliente = ?, " +
                    "total_pedidos = ?, monto_total_gastado = ? WHERE cliente_id = ?";
        int rowsAffected = jdbcTemplate.update(sql,
            summary.getNombreCliente(),
            summary.getEmailCliente(),
            summary.getTotalPedidos(),
            summary.getMontoTotalGastado(),
            summary.getClienteId()
        );
        return rowsAffected > 0;
    }

    // Delete
    public boolean deleteById(Long clienteId) {
        String sql = "DELETE FROM order_summary WHERE cliente_id = ?";
        int rowsAffected = jdbcTemplate.update(sql, clienteId);
        return rowsAffected > 0;
    }
}
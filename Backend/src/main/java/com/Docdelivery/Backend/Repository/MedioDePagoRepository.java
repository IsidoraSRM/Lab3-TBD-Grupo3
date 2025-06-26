package com.Docdelivery.Backend.Repository;

import com.Docdelivery.Backend.Entity.MedioDePagoEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
public class MedioDePagoRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static class MedioDePagoRowMapper implements RowMapper<MedioDePagoEntity> {
        @Override
        public MedioDePagoEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new MedioDePagoEntity(
                rs.getLong("idMetodoDePago"),
                rs.getString("nombreMetodoDePago")
            );
        }
    }

    // Create
    public void save(MedioDePagoEntity medioDePago) {
        String sql = "INSERT INTO MedioDePago (nombreMetodoDePago) VALUES (?)";
        jdbcTemplate.update(sql, medioDePago.getNombreMetodoDePago());
    }

    // Read - Get All
    public List<MedioDePagoEntity> findAll() {
        String sql = "SELECT * FROM MedioDePago";
        return jdbcTemplate.query(sql, new MedioDePagoRowMapper());
    }

    // Read - Get by ID
    public Optional<MedioDePagoEntity> findById(Long id) {
        String sql = "SELECT * FROM MedioDePago WHERE idMetodoDePago = ?";
        List<MedioDePagoEntity> results = jdbcTemplate.query(sql, new MedioDePagoRowMapper(), id);
        return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
    }

    // Update
    public boolean update(MedioDePagoEntity medioDePago) {
        String sql = "UPDATE MedioDePago SET nombreMetodoDePago = ? WHERE idMetodoDePago = ?";
        int rowsAffected = jdbcTemplate.update(sql, 
            medioDePago.getNombreMetodoDePago(),
            medioDePago.getIdMetodoDePago()
        );
        return rowsAffected > 0;
    }

    // Delete
    public boolean deleteById(Long id) {
        String sql = "DELETE FROM MedioDePago WHERE idMetodoDePago = ?";
        int rowsAffected = jdbcTemplate.update(sql, id);
        return rowsAffected > 0;
    }
}
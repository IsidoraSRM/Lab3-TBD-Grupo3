package com.Docdelivery.Backend.Repository;

import com.Docdelivery.Backend.Entity.RatingEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
public class RatingRepository {
        @Autowired
        private JdbcTemplate jdbcTemplate;

        // RowMapper para RatingEntity
        private static class RatingRowMapper implements RowMapper<RatingEntity> {
            @Override
            public RatingEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
                return new RatingEntity(
                        rs.getLong("idcalificacion"),
                        rs.getLong("idpedido"),
                        rs.getInt("score"),
                        rs.getString("comment")
                );
            }
        }

        // Guardar una nueva calificación
        public Long save(RatingEntity rating) {
            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(
                        "INSERT INTO rating (idpedido, score, comment) VALUES (?, ?, ?)",
                        new String[] {"idcalificacion"}
                );
                ps.setLong(1, rating.getIdPedido());
                ps.setInt(2, rating.getScore());
                ps.setString(3, rating.getComment());
                return ps;
            }, keyHolder);
            return keyHolder.getKey().longValue();
        }

        // Buscar por ID
        public Optional<RatingEntity> findById(Long id) {
            String sql = "SELECT * FROM rating WHERE idcalificacion = ?";
            try {
                return Optional.ofNullable(jdbcTemplate.queryForObject(sql, new RatingRowMapper(), id));
            } catch (Exception e) {
                return Optional.empty();
            }
        }

        // Obtener todos
        public List<RatingEntity> findAll() {
            String sql = "SELECT * FROM rating";
            return jdbcTemplate.query(sql, new RatingRowMapper());
        }

        // Actualizar calificación
        public boolean update(RatingEntity rating) {
            String sql = "UPDATE rating SET idpedido = ?, score = ?, comment = ? WHERE idcalificacion = ?";
            return jdbcTemplate.update(sql, rating.getIdPedido(), rating.getScore(), rating.getComment(), rating.getIdCalificacion()) > 0;
        }

        // Eliminar por ID
        public boolean delete(Long id) {
            String sql = "DELETE FROM rating WHERE idcalificacion = ?";
            return jdbcTemplate.update(sql, id) > 0;
        }
}


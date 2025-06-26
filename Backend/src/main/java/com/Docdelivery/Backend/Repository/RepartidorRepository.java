package com.Docdelivery.Backend.Repository;


import com.Docdelivery.Backend.Entity.RepartidorEntity;
import com.Docdelivery.Backend.dto.RepartidorDistanciaDTO;
import com.Docdelivery.Backend.dto.TopRepartidorDto;

import com.Docdelivery.Backend.dto.VistaRepartidorDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.locationtech.jts.geom.Point;

@Repository
public class RepartidorRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // RowMapper para mapear los resultados a RepartidorEntity
    private static class RepartidorRowMapper implements RowMapper<RepartidorEntity> {
        @Override
        public RepartidorEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new RepartidorEntity(
                    rs.getLong("repartidor_id"),
                    rs.getString("nombre"),
                    rs.getString("telefono"),
                    rs.getBoolean("disponible"),
                    rs.getObject("ubicacion_actual", Point.class)
            );
        }
    }

    // Insertar repartidor en la base de datos
    public void save(RepartidorEntity repartidor) {
        String sql = "INSERT INTO repartidor (nombre, telefono, disponible,ubicacion_actual) VALUES (?, ?, ?,ST_SetSRID(ST_MakePoint(?, ?), 4326) )";
        jdbcTemplate.update(sql, repartidor.getNombre(), repartidor.getTelefono(), repartidor.getDisponible(),repartidor.getUbicacion_actual().getX(), repartidor.getUbicacion_actual().getY());
    }

    // Buscar repartidor por ID
    public Optional<RepartidorEntity> findById(Long id) {
        String sql = "SELECT * FROM repartidor WHERE repartidor_id = ?";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, new RepartidorRowMapper(), id));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    // Obtener todos los repartidores
    public List<RepartidorEntity> findAll() {
        String sql = "SELECT * FROM repartidor";
        return jdbcTemplate.query(sql, new RepartidorRowMapper());
    }


    // obtener repartidor por idUsuario
    public Optional<Long> findRepartidorIdByUsuarioId(Long usuarioId) {
    String sql = "SELECT r.repartidor_id " +
                 "FROM repartidor r " +
                 "JOIN usuarios u ON u.name_param = r.nombre AND u.phone = r.telefono " +
                 "WHERE u.id_usuario = ?";
    
    try {
        return Optional.ofNullable(jdbcTemplate.queryForObject(sql, Long.class, usuarioId));
    } catch (Exception e) {
        return Optional.empty();
    }
}

    public List<Map<String, Object>> getPedidosConClienteYDetalleByRepartidorId(Long repartidorId) {
        String sql = "SELECT o.*, c.*, dp.* " +
                    "FROM OrderEntity o " +
                    "JOIN cliente c ON o.cliente_id = c.cliente_id " +
                    "JOIN detallepedido dp ON o.idpedido = dp.idpedido " +
                    "WHERE o.repartidor_id = ? " +
                    "ORDER BY o.fechapedido DESC";
        
        try {
            return jdbcTemplate.queryForList(sql, repartidorId);
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }
     public List<TopRepartidorDto> findTop3ByRendimiento() {
        String sql = "SELECT r.repartidor_id, r.nombre, r.telefono, r.disponible, " +
                "COUNT(o.idPedido) as entregas_completadas, " +
                "COALESCE(AVG(rt.score), 0) as puntuacion_promedio, " +
                "(COUNT(o.idPedido) * 0.6 + COALESCE(AVG(rt.score), 0) * 0.4) as rendimiento " +
                "FROM repartidor r " +
                "LEFT JOIN orderentity o ON r.repartidor_id = o.repartidor_id AND o.estadoPedido = 'ENTREGADO' " +
                "LEFT JOIN rating rt ON o.idPedido = rt.idPedido " +
                "GROUP BY r.repartidor_id, r.nombre, r.telefono, r.disponible " +
                "ORDER BY rendimiento DESC " +
                "LIMIT 3";

        return jdbcTemplate.query(sql, topRepartidorRowMapper);
    }

    private final RowMapper<TopRepartidorDto> topRepartidorRowMapper = (rs, rowNum) -> {
        TopRepartidorDto dto = new TopRepartidorDto();
        dto.setRepartidorId(rs.getLong("repartidor_id"));
        dto.setNombre(rs.getString("nombre"));
        dto.setTelefono(rs.getString("telefono"));
        dto.setDisponible(rs.getBoolean("disponible"));
        dto.setEntregasCompletadas(rs.getLong("entregas_completadas"));
        dto.setPuntuacionPromedio(rs.getDouble("puntuacion_promedio"));
        dto.setRendimiento(rs.getDouble("rendimiento"));
        return dto;
    };

    public List<VistaRepartidorDto> obtenerDesempenoRepartidores() {
        String sql = "SELECT * FROM VistaDesempenoRepartidores";

        return jdbcTemplate.query(sql, (rs, rowNum) -> new VistaRepartidorDto(
                rs.getString("repartidor"),
                rs.getInt("total_entregas"),
                rs.getDouble("tiempo_promedio_horas"),
                rs.getDouble("calificacion_promedio")
        ));
    }

    public boolean update(RepartidorEntity repartidor) {
        String sql = "UPDATE repartidor SET nombre = ?, telefono = ?, disponible = ? WHERE repartidor_id = ?";
        int rowsAffected = jdbcTemplate.update(sql,
                repartidor.getNombre(),
                repartidor.getTelefono(),
                repartidor.getDisponible(),
                repartidor.getRepartidorId()
        );
        return rowsAffected > 0;
    }

    public boolean deleteById(Long id) {
        String sql = "DELETE FROM repartidor WHERE repartidor_id = ?";
        int rowsAffected = jdbcTemplate.update(sql, id);
        return rowsAffected > 0;
    }

    public List<RepartidorDistanciaDTO> calcularDistanciaRecorridaUltimoMes() {
        String sql = """
        SELECT 
            r.repartidor_id,
            r.nombre,
            SUM(ST_Distance(dp.ubicacionInicio::geography, dp.ubicacionDestino::geography)) AS distancia_total_metros
        FROM 
            repartidor r
        JOIN 
            orderentity o ON o.repartidor_id = r.repartidor_id
        JOIN 
            detallepedido dp ON dp.idpedido = o.idpedido
        WHERE 
            o.fechaentrega >= NOW() - INTERVAL '1 month'
            AND dp.ubicacionInicio IS NOT NULL
            AND dp.ubicacionDestino IS NOT NULL
        GROUP BY 
            r.repartidor_id, r.nombre;
    """;

        return jdbcTemplate.query(sql, (rs, rowNum) ->
                new RepartidorDistanciaDTO(
                        rs.getLong("repartidor_id"),
                        rs.getString("nombre"),
                        rs.getDouble("distancia_total_metros")
                )
        );
    }

}

package com.Docdelivery.Backend.Repository;


import com.Docdelivery.Backend.Entity.ClienteEntity;
import com.Docdelivery.Backend.dto.ClienteConTotalGastadoDTO;
import org.locationtech.jts.geom.Point;
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

@Repository
public class  ClienteRepository implements ClienteRepositoryCustom {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // RowMapper para mapear los resultados a ClienteEntity
    private static class ClienteRowMapper implements RowMapper<ClienteEntity> {
        @Override
        public ClienteEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new ClienteEntity(
                    rs.getLong("cliente_id"),
                    rs.getString("nombre"),
                    rs.getString("direccion"),
                    rs.getString("email"),
                    rs.getString("telefono"),
                    rs.getObject("ubicacion", Point.class)
            );
        }
    }

    // Insertar cliente en la base de datos
    public void save(ClienteEntity cliente) {
        String sql = "INSERT INTO cliente (nombre, direccion, email, telefono,ubicacion) VALUES (?, ?, ?, ?,ST_SetSRID(ST_MakePoint(?, ?), 4326) )";
        jdbcTemplate.update(sql, cliente.getNombre(), cliente.getDireccion(), cliente.getEmail(), cliente.getTelefono(),cliente.getUbicacion().getX(), cliente.getUbicacion().getY());
    }

    // Buscar cliente por ID
    public Optional<ClienteEntity> findById(Long id) {
        String sql = "SELECT * FROM cliente WHERE cliente_id = ?";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, new ClienteRowMapper(), id));
        } catch (Exception e) {
            return Optional.empty(); // Manejar caso donde el cliente no existe
        }
    }

    // Obtener todos los clientes
    public List<ClienteEntity> findAll() {
        String sql = "SELECT * FROM cliente";
        return jdbcTemplate.query(sql, new ClienteRowMapper());
    }


    public Optional<ClienteConTotalGastadoDTO> obtenerClienteQueMasHaGastado() {
        String sql = """ 
    SELECT 
        c.cliente_id,
        c.nombre,
        c.email,
        c.direccion,
        c.telefono,
        SUM(p.monto) AS total_gastado
    FROM OrderEntity o
    JOIN Pago p ON o.idPedido = p.idPedido
    JOIN Cliente c ON o.cliente_id = c.cliente_id
    WHERE o.estadoPedido = 'ENTREGADO'
    GROUP BY c.cliente_id, c.nombre, c.email, c.direccion, c.telefono
    ORDER BY total_gastado DESC
    LIMIT 1
    """;

        try {
            // Se crea el DTO para retornar
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, (rs, rowNum) -> new ClienteConTotalGastadoDTO(
                    rs.getLong("cliente_id"),
                    rs.getString("nombre"),
                    rs.getString("email"),
                    rs.getString("direccion"),
                    rs.getString("telefono"),
                    rs.getDouble("total_gastado") // Mapeamos total_gastado
            )));
        } catch (Exception e) {
            return Optional.empty();
        }
    }
    //obtener cliente por idUusuario
    public Optional<Long> findClienteIdByUsuarioId(Long usuarioId) {
        String sql = "SELECT c.cliente_id " +
                "FROM cliente c " +
                "JOIN usuarios u ON u.name_param = c.nombre AND u.phone = c.telefono " +
                "WHERE u.id_usuario = ?";

        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, Long.class, usuarioId));
        } catch (Exception e) {
            return Optional.empty();
        }
    }
    //funcuionalidad para actualizar un cliente
    public void update(ClienteEntity cliente) {
        String sql = "UPDATE cliente SET nombre = ?, direccion = ?, email = ?, telefono = ? WHERE cliente_id = ?";
        jdbcTemplate.update(sql,
                cliente.getNombre(),
                cliente.getDireccion(),
                cliente.getEmail(),
                cliente.getTelefono(),
                cliente.getClienteId());
    }

    public void removeCliente(Long id) {
        String sql = "DELETE FROM cliente WHERE cliente_id = ?";
        jdbcTemplate.update(sql, id);
    }


    // --------------------------- Lab 2 ---------------------------

    // Consulta 6: Determinar los clientes que están a más de 5km de cualquier empresa o farmacia.
    public List<Map<String, Object>> obtenerClientesMasDe5Km() {
        String sql = "SELECT c.cliente_id, c.nombre, MIN(ST_Distance( " +
                "    ST_Transform(c.ubicacion, 3857), " +
                "    ST_Transform(e.ubicacion, 3857) " +
                ")) AS distancia_minima " +
                "FROM cliente c " +
                "JOIN EmpresaAsociada e ON TRUE " +
                "WHERE c.ubicacion IS NOT NULL " +
                "  AND e.ubicacion IS NOT NULL " +
                "  AND ST_X(c.ubicacion) <> 0 AND ST_Y(c.ubicacion) <> 0 " +
                "  AND ST_X(e.ubicacion) <> 0 AND ST_Y(e.ubicacion) <> 0 " +
                "GROUP BY c.cliente_id, c.nombre " +
                "HAVING MIN(ST_Distance(ST_Transform(c.ubicacion, 3857), ST_Transform(e.ubicacion, 3857))) > 5000 ";

        try {
            return jdbcTemplate.queryForList(sql);
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }


    //Consulta 2: Verificar si los clientes están dentro de una zona de cobertura (con buffer de 1km)
    @Override
    public List<Map<String, Object>> verificarClienteEnZona(Long clienteId) {
        String sql = """
        SELECT 
            c.cliente_id,
            c.nombre,
            z.zona_id,
            z.nombre AS nombre_zona
        FROM 
            cliente c
        JOIN 
            zonas_cobertura z
        ON 
            ST_Intersects(
                ST_Buffer(c.ubicacion::geography, 1000)::geometry,
                z.geom
            )
        WHERE 
            c.cliente_id = ?
        """;

        return jdbcTemplate.queryForList(sql, clienteId);
    }


}

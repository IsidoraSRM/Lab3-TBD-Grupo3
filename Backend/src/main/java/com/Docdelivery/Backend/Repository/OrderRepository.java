package com.Docdelivery.Backend.Repository;

import com.Docdelivery.Backend.Entity.OrderEntity;
import com.Docdelivery.Backend.dto.ClusterZoneDto;

import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.io.WKTReader;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKBReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.jdbc.core.*;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;


@Repository
public class OrderRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    // RowMapper para OrderEntity
    private static class OrderRowMapper implements RowMapper<OrderEntity> {
        @Override
        public OrderEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
            OrderEntity order = new OrderEntity();
            order.setIdPedido(rs.getLong("idpedido"));
            order.setClienteId(rs.getLong("cliente_id"));
            Long repartidorId = rs.getObject("repartidor_id") != null ? rs.getLong("repartidor_id") : null;
            order.setRepartidorId(repartidorId);
            order.setFechaPedido(rs.getTimestamp("fechapedido").toLocalDateTime());
            Timestamp fechaEntrega = rs.getTimestamp("fechaEntrega");
            order.setFechaEntrega(fechaEntrega != null ? fechaEntrega.toLocalDateTime() : null);

            order.setEstadoPedido(rs.getString("estadopedido"));
            order.setPrioridadPedido(rs.getString("prioridadpedido"));
            return order;
        }
    }

    // Guardar pedido y devolver el ID generado
    public Long save(OrderEntity order) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(
                "INSERT INTO orderentity (cliente_id, repartidor_id, fechaPedido, estadoPedido, prioridadPedido) " +
                "VALUES (?, ?, ?, ?, ?)",
                new String[] {"idpedido"}
            );
            ps.setLong(1, order.getClienteId());
            if (order.getRepartidorId() != null) {
                ps.setLong(2, order.getRepartidorId());
            } else {
                ps.setNull(2, Types.BIGINT);
            }
            ps.setTimestamp(3, Timestamp.valueOf(order.getFechaPedido()));
            ps.setString(4, order.getEstadoPedido());
            ps.setString(5, order.getPrioridadPedido());
            return ps;
        }, keyHolder);
        
        return Objects.requireNonNull(keyHolder.getKey()).longValue();
    }

    // Buscar pedido por ID
    public Optional<OrderEntity> findById(Long id) {
        String sql = "SELECT * FROM OrderEntity WHERE idPedido = ?";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, new OrderRowMapper(), id));
        } catch (Exception e) {
            return Optional.empty();
        }
    }


    public List<OrderEntity> findByClienteId(Long clienteId) {
        String sql = "SELECT * FROM OrderEntity WHERE cliente_id = ? ORDER BY fechaPedido DESC";
        return jdbcTemplate.query(sql, new OrderRowMapper(), clienteId);
    }

    // Si necesitas más detalles (con joins a otras tablas)
    public List<Map<String, Object>> getPedidosConDetallesByClienteId(Long clienteId) {
        String sql = "SELECT o.*, dp.* " +
                "FROM OrderEntity o " +
                "JOIN detallepedido dp ON o.idpedido = dp.idpedido " +
                "WHERE o.cliente_id = ? " +
                "ORDER BY o.fechapedido DESC";

        try {
            return jdbcTemplate.queryForList(sql, clienteId);
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }


    // Buscar pedidos por repartidor
    public List<OrderEntity> findByRepartidorId(Long repartidorId) {
        String sql = "SELECT * FROM OrderEntity WHERE repartidor_id = ?";
        return jdbcTemplate.query(sql, new OrderRowMapper(), repartidorId);
    }

    // Obtener todos los pedidos
    public List<OrderEntity> findAll() {
        String sql = "SELECT * FROM OrderEntity";
        return jdbcTemplate.query(sql, new OrderRowMapper());
    }

    //Procedure registerOrder
    public int registerOrder(int clienteId, String prioridad, String nombreMetodo, int monto,String nombre_servicio,String descripcion, String categoria, String direccionInicio,String direccionDestino) {
        String query = "CALL register_order(?,?,?,?,?,?,?,?,?,?)";
        
        try {
            return jdbcTemplate.execute(
                query,
                (CallableStatementCallback<Integer>) cs -> {
                    // Configurar parámetros de entrada
                    cs.setInt(1, clienteId);
                    cs.setString(2, prioridad);
                    cs.setString(3,nombreMetodo);
                    cs.setInt(4, monto);
                    cs.setString(5, nombre_servicio);
                    cs.setString(6, descripcion);
                    cs.setString(7, categoria);
                    cs.setString(8, direccionInicio);
                    cs.setString(9, direccionDestino);
                    

                    
                    // Registrar parámetro de salida
                    cs.registerOutParameter(10, Types.INTEGER);
                    
                    // Ejecutar el procedimiento
                    cs.execute();
                    
                    // Recuperar el ID generado
                    return cs.getInt(10);
                }
            );
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error al registrar orden: " + e.getMessage());
            return -1;
        }
    }
    //Procedimiento para actualizar el estado de los pedidos
    public boolean cambiarEstadoPedido(int idPedido, String nuevoEstado) {
        String query = "CALL cambiar_estado_pedido(?, ?)";

        try {
            jdbcTemplate.execute(query, (CallableStatementCallback<Void>) cs -> {
                cs.setInt(1, idPedido);
                cs.setString(2, nuevoEstado);
                cs.execute();
                return null;
            });
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error al cambiar el estado del pedido: " + e.getMessage());
            return false;
        }
    }
    //Procedure confirmarPedido
    public int confirmarPedido(int idPedido) {
        String query = "CALL confirmar_pedido(?)";
        
        try {
            jdbcTemplate.execute(
                query,
                (CallableStatementCallback<Integer>) cs -> {
                    // parametro de entrada
                    cs.setInt(1, idPedido);
                    
                    // ejecutar el procedimiento
                    cs.execute();
                    
                    
                    return 1;
                }
            );
            return 1; // bien
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error al confirmar pedido: " + e.getMessage());
            return -1; // Error
        }
    }

    //Obtener pedido con cliente y detallePedido por idRepartidor
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


    // Listar las empresas asociadas con más entregas fallidas
    public List<Map<String, Object>> getEntregasFallidasPorEmpresa() {
        String sql = "SELECT ea.nombreEmpresa, COUNT(*) AS entregas_fallidas " +
                "FROM OrderEntity o " +
                "JOIN EmpresaAsociada ea ON o.idEmpresaAsociada = ea.idEmpresaAsociada " +
                "WHERE o.estadoPedido ILIKE 'FALLIDO'  " +
                "GROUP BY ea.nombreEmpresa " +
                "ORDER BY entregas_fallidas DESC";

        try {
            return jdbcTemplate.queryForList(sql);
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    // Calcular el tiempo promedio entre pedido y entrega por repartidor
    public List<Map<String, Object>> getTiempoPromedioEntregaPorRepartidor() {
        String sql = "SELECT r.nombre AS nombre_repartidor, " +
                "       ROUND(AVG(EXTRACT(EPOCH FROM (o.fechaEntrega - o.fechaPedido)) / 60)) AS tiempo_promedio_minutos " +
                "FROM OrderEntity o " +
                "JOIN repartidor r ON o.repartidor_id = r.repartidor_id " +
                "WHERE o.fechaEntrega IS NOT NULL AND o.fechaPedido IS NOT NULL " +
                "GROUP BY r.nombre " +
                "ORDER BY tiempo_promedio_minutos";

        try {
            return jdbcTemplate.queryForList(sql);
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }


    // --------------------------- Lab 2 ---------------------------

    // Consulta 5: Listar todos los pedidos cuya ruta estimada cruce más de 2 zonas de reparto.
    public List<Map<String, Object>> getPedidosConClienteYDetalleByRutaEstimadaCruce2Zonas() {
        String sql = "SELECT o.idPedido, e.nombreEmpresa, COUNT(z.zona_id) AS zonas_cruzadas " +
                "FROM OrderEntity o " +
                "JOIN EmpresaAsociada e ON o.idEmpresaAsociada = e.idEmpresaAsociada " +
                "JOIN zonas_cobertura z ON ST_Intersects(o.ruta_estimada, z.geom) " +
                "GROUP BY o.idPedido, e.nombreEmpresa " +
                "HAVING COUNT(z.zona_id) > 2";

        try {
            return jdbcTemplate.queryForList(sql);
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }


    // --------------------------------EXTRAS-----------------------------

    // EXTRA 1 : Implementar una función que calcule automáticamente la zona a la que pertenece un cliente.
    public String calcularZonaCliente(int clienteId) {
        String sql = "SELECT calcular_zona_cliente(?)";
        return jdbcTemplate.queryForObject(
          sql,
          new Object[]{clienteId},
          String.class
        );
    }


    // EXTRA 2 : Detectar zonas con alta densidad de pedidos mediante agregación de puntos.
    public List<ClusterZoneDto> findHighDensityZones() {
        String sql = """
            WITH order_points AS (
            SELECT c.ubicacion AS punto
            FROM cliente c
            JOIN OrderEntity o ON c.cliente_id = o.cliente_id
            ),
            clusters AS (
            SELECT
                ST_ClusterDBSCAN(punto, 0.005, 2) OVER () AS cluster_id,
                punto
            FROM order_points
            ),
            cluster_stats AS (
            SELECT
                cluster_id,
                COUNT(*)                       AS cantidad_pedidos,
                ST_Centroid(ST_Collect(punto)) AS centro_cluster,
                ST_Collect(punto)              AS geom_cluster
            FROM clusters
            WHERE cluster_id >= 0
            GROUP BY cluster_id
            )
            SELECT
            cs.cluster_id,
            z.nombre                 AS zona_cobertura,
            cs.cantidad_pedidos,
            ST_AsText(cs.centro_cluster)     AS centro_wkt,
            ST_AsGeoJSON(cs.geom_cluster)    AS geom_geojson
            FROM cluster_stats cs
            LEFT JOIN public.zonas_cobertura z
            ON ST_Within(cs.centro_cluster, z.geom)
            ORDER BY cs.cantidad_pedidos DESC;
        """;

        return jdbcTemplate.query(sql, (rs, i) ->
        new ClusterZoneDto(
            rs.getInt("cluster_id"),
            rs.getString("zona_cobertura"),
            rs.getInt("cantidad_pedidos"),
            rs.getString("centro_wkt"),
            rs.getString("geom_geojson")
        )
        );
    }

}







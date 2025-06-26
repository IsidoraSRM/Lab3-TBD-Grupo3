package com.Docdelivery.Backend.Repository;



import com.Docdelivery.Backend.Entity.EmpresaAsociadaEntity;
import com.Docdelivery.Backend.dto.VistaEmpresaDto;

import org.locationtech.jts.geom.Point;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class EmpresaRepository {

    private final JdbcTemplate jdbcTemplate;

    public EmpresaRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<VistaEmpresaDto> obtenerEmpresasConRanking1() {
        String sql = """
        WITH Ranking AS (
            SELECT empresa, total_servicios_entregados,
                   RANK() OVER (ORDER BY total_servicios_entregados DESC) AS ranking
            FROM VistaEmpresasTopServicios
        )
        SELECT empresa, total_servicios_entregados, ranking
        FROM Ranking
        WHERE ranking = 1;
    """;

        return jdbcTemplate.query(sql, (rs, rowNum) ->
                new VistaEmpresaDto(
                        rs.getString("empresa"),
                        rs.getInt("total_servicios_entregados"),
                        rs.getInt("ranking") // Agregar ranking en el DTO
                ));
    }

    //CRUD

    //  Obtener todas las empresas con paginación
    public List<EmpresaAsociadaEntity> findAll(int limit, int offset) {
        String sql = "SELECT * FROM EmpresaAsociada LIMIT ? OFFSET ?";
        return jdbcTemplate.query(sql, new Object[]{limit, offset}, empresaRowMapper());
    }

    //  Buscar empresa por ID
    public Optional<EmpresaAsociadaEntity> findById(Long id) {
        String sql = "SELECT * FROM EmpresaAsociada WHERE idEmpresaAsociada = ?";
        List<EmpresaAsociadaEntity> empresas = jdbcTemplate.query(sql, new Object[]{id}, empresaRowMapper());
        return empresas.stream().findFirst();
    }


        //  Guardar o actualizar empresa
        public void save(EmpresaAsociadaEntity empresa) {
            String sql = "INSERT INTO EmpresaAsociada (idEmpresaAsociada, nombreEmpresa, direccionEmpresa, tipoServicio) " +
                    "VALUES (?, ?, ?, ?) ON CONFLICT (idEmpresaAsociada) DO UPDATE " +
                    "SET nombreEmpresa = EXCLUDED.nombreEmpresa, direccionEmpresa = EXCLUDED.direccionEmpresa, tipoServicio = EXCLUDED.tipoServicio";
            jdbcTemplate.update(sql, empresa.getIdEmpresaAsociada(), empresa.getNombreEmpresa(), empresa.getDireccionEmpresa(), empresa.getTipoServicio());
        }

        //  Eliminar empresa por ID
        public void deleteById(Long id) {
            String sql = "DELETE FROM EmpresaAsociada WHERE idEmpresaAsociada = ?";
            jdbcTemplate.update(sql, id);
        }

    //  Mapper para convertir filas de la BD en objetos `EmpresaAsociadaEntity`
    private RowMapper<EmpresaAsociadaEntity> empresaRowMapper() {
        return (rs, rowNum) -> new EmpresaAsociadaEntity(
                rs.getLong("idEmpresaAsociada"),
                rs.getString("nombreEmpresa"),
                rs.getString("direccionEmpresa"),
                rs.getString("tipoServicio"),
                rs.getObject("ubicacion", Point.class)
        );
    }

}

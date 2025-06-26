package com.Docdelivery.Backend.Repository;

import com.Docdelivery.Backend.Entity.UsuarioEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.io.WKTReader;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
public class UsuarioRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // RowMapper para mapear los resultados de la consulta a UsuarioEntity
    private static class UsuarioRowMapper implements RowMapper<UsuarioEntity> {
        @Override
        public UsuarioEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
            UsuarioEntity usuario = new UsuarioEntity();
            usuario.setIdUsuario(rs.getLong("id_usuario"));
            usuario.setRut(rs.getString("rut"));
            usuario.setNameParam(rs.getString("name_param"));
            usuario.setEmail(rs.getString("email"));
            usuario.setPhone(rs.getString("phone"));
            usuario.setBirthdate(rs.getDate("birthdate"));
            usuario.setPassword(rs.getString("password"));
            usuario.setRole(rs.getString("role"));
            usuario.setUbicacion(rs.getObject("ubicacion", org.locationtech.jts.geom.Point.class));
            return usuario;
        }
    }

    // Método para encontrar un usuario por email utilizando Optional
public Optional<UsuarioEntity> findByEmail(String email) {
    String sql =
      "SELECT id_usuario, rut, name_param, email, phone, birthdate, password, role, " +
      "       ST_AsText(ubicacion) AS ubicacion_wkt " +
      "FROM usuarios WHERE email = ?";

    try {
        return Optional.ofNullable(
          jdbcTemplate.queryForObject(sql,
            (rs, rowNum) -> {
              UsuarioEntity u = new UsuarioEntity();
              // — mapear TODOS los campos normales —
              u.setIdUsuario(   rs.getLong(  "id_usuario")   );
              u.setRut(         rs.getString("rut")          );
              u.setNameParam(   rs.getString("name_param")   );
              u.setEmail(       rs.getString("email")        );
              u.setPhone(       rs.getString("phone")        );
              u.setBirthdate(   rs.getDate(  "birthdate")    );
              u.setPassword(    rs.getString("password")     );
              u.setRole(        rs.getString("role")         );
              // — ahora la geometría —
              String wkt = rs.getString("ubicacion_wkt");
              if (wkt != null) {
                try {
                  org.locationtech.jts.geom.Geometry geom = new WKTReader().read(wkt);
                  if (geom instanceof org.locationtech.jts.geom.Point) {
                    u.setUbicacion((org.locationtech.jts.geom.Point) geom);
                  } else {
                    throw new IllegalStateException("No era POINT sino "+geom.getGeometryType());
                  }
                } catch (org.locationtech.jts.io.ParseException ex) {
                  throw new RuntimeException("Error parseando WKT", ex);
                }
              }
              return u;
            },
            email
          )
        );
    } catch (EmptyResultDataAccessException e) {
        return Optional.empty();
    }
}

    // Método para encontrar el ID de un usuario por email utilizando Optional
    public Optional<Long> findIdByEmail(String email) {
        String sql = "SELECT id_usuario FROM usuarios WHERE email = ?";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, Long.class, email));
        } catch (org.springframework.dao.EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    // Método para insertar un nuevo usuario
    public int save(UsuarioEntity usuario) {
        String sql =
      "INSERT INTO usuarios(" +
      "  rut, name_param, email, phone, birthdate, password, role, ubicacion" +
      ") VALUES ( ?, ?, ?, ?, ?, ?, ?, ST_SetSRID(ST_MakePoint(?, ?), 4326) )";

        return jdbcTemplate.update(sql, usuario.getRut(), usuario.getNameParam(), usuario.getEmail(), usuario.getPhone(),
                usuario.getBirthdate(), usuario.getPassword(), usuario.getRole(), usuario.getUbicacion().getX(), usuario.getUbicacion().getY());
    }

    // Método para obtener todos los usuarios
    public List<UsuarioEntity> findAll() {
        String sql = "SELECT * FROM usuarios";
        return jdbcTemplate.query(sql, new UsuarioRowMapper());
    }
}
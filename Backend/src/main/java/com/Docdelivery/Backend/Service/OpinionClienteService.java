package com.Docdelivery.Backend.Service;

import com.Docdelivery.Backend.Entity.ClienteEntity;
import com.Docdelivery.Backend.Entity.OpinionClienteEntity;
import com.Docdelivery.Backend.Repository.ClienteRepository;
import com.Docdelivery.Backend.Repository.OpinionClienteRepository;
import com.Docdelivery.Backend.dto.OpinionClienteDTO;
import com.Docdelivery.Backend.dto.OpinionesPorHoraDto;
import com.Docdelivery.Backend.dto.PromedioPuntuacionEmpresaDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OpinionClienteService {

    @Autowired
    private OpinionClienteRepository opinionRepo;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private MongoTemplate mongoTemplate;

    public List<PromedioPuntuacionEmpresaDto> getPromedioPorEmpresaConNombre() {
        // 1. Crear agregación usando MongoTemplate
        Aggregation aggregation = Aggregation.newAggregation(
            Aggregation.group("empresa_id")
                .avg("puntuacion").as("promedio_puntuacion")
                .count().as("total_opiniones"),
            Aggregation.sort(org.springframework.data.domain.Sort.Direction.DESC, "promedio_puntuacion")
        );

        // 2. Ejecutar agregación
        AggregationResults<Map> results = mongoTemplate.aggregate(
            aggregation, "opiniones_clientes", Map.class
        );

        List<Map> resultadosMongo = results.getMappedResults();

        // 3. Debug
        System.out.println("Resultados MongoDB con MongoTemplate:");
        for (int i = 0; i < resultadosMongo.size(); i++) {
            Map<String, Object> doc = resultadosMongo.get(i);
            System.out.println("Elemento " + i + ": " + doc);
        }

        // 4. Mapear resultados
        return resultadosMongo.stream()
            .map(doc -> {
                String empresaId = (String) doc.get("_id");
                Double promedio = ((Number) doc.get("promedio_puntuacion")).doubleValue();
                Integer total = ((Number) doc.get("total_opiniones")).intValue();

                // Buscar nombre de empresa en PostgreSQL
                String nombreEmpresa = obtenerNombreEmpresa(empresaId);

                return new PromedioPuntuacionEmpresaDto(
                    empresaId,
                    nombreEmpresa,
                    promedio,
                    total
                );
            })
            .collect(Collectors.toList());
    }

    public List<OpinionesPorHoraDto> getOpinionesPorHora() {
        // Agregación para agrupar por hora del día
        Aggregation aggregation = Aggregation.newAggregation(
            // Extraer la hora de la fecha
            Aggregation.project()
                .andExpression("hour(fecha)").as("hora")
                .and("puntuacion").as("puntuacion"),

            // Agrupar por hora
            Aggregation.group("hora")
                .avg("puntuacion").as("promedio_puntuacion")
                .count().as("total_opiniones")
                .max("puntuacion").as("puntuacion_maxima")
                .min("puntuacion").as("puntuacion_minima"),

            // Ordenar por hora
            Aggregation.sort(org.springframework.data.domain.Sort.Direction.ASC, "_id")
        );

        // Ejecutar agregación
        AggregationResults<Map> results = mongoTemplate.aggregate(
            aggregation, "opiniones_clientes", Map.class
        );

        List<Map> resultadosMongo = results.getMappedResults();

        // Debug
        System.out.println("Opiniones agrupadas por hora:");
        for (int i = 0; i < resultadosMongo.size(); i++) {
            Map<String, Object> doc = resultadosMongo.get(i);
            System.out.println("Elemento " + i + ": " + doc);
        }

        // Mapear resultados
        return resultadosMongo.stream()
            .map(doc -> {
                Integer hora = (Integer) doc.get("_id");
                Double promedio = ((Number) doc.get("promedio_puntuacion")).doubleValue();
                Integer total = ((Number) doc.get("total_opiniones")).intValue();
                Double maxima = ((Number) doc.get("puntuacion_maxima")).doubleValue();
                Double minima = ((Number) doc.get("puntuacion_minima")).doubleValue();

                return new OpinionesPorHoraDto(
                    hora,
                    promedio,
                    total,
                    maxima,
                    minima
                );
            })
            .collect(Collectors.toList());
    }


    private String obtenerNombreCliente(String clienteId) {
        try {
            String sql = "SELECT nombre FROM cliente WHERE cliente_id = ?";
            Long clienteIdLong = Long.parseLong(clienteId);
            return jdbcTemplate.queryForObject(sql, String.class, clienteIdLong);
        } catch (NumberFormatException e) {
            return "ID de cliente inválido";
        } catch (Exception e) {
            return "Cliente no encontrado";
        }
    }


    public OpinionClienteDTO convertToDto(OpinionClienteEntity entity) {
        OpinionClienteDTO dto = new OpinionClienteDTO();
        dto.setCliente_id(entity.getCliente_id());
        dto.setEmpresa_id(entity.getEmpresa_id());
        dto.setComentario(entity.getComentario());
        dto.setPuntuacion(entity.getPuntuacion());
        dto.setFecha(entity.getFecha());
        dto.setNombreCliente(obtenerNombreCliente(entity.getCliente_id()));
        return dto;
    }


    public List<OpinionClienteDTO> buscarOpinionesConPalabrasClave() {
        return opinionRepo.findByPalabrasClave()
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }


    private String obtenerNombreEmpresa(String empresaId) {
        try {
            String sql = "SELECT nombreEmpresa FROM EmpresaAsociada WHERE idEmpresaAsociada = ?";
            Long empresaIdLong = Long.parseLong(empresaId);
            return jdbcTemplate.queryForObject(sql, String.class, empresaIdLong);
        } catch (NumberFormatException e) {
            return "ID de empresa inválido";
        } catch (Exception e) {
            return "Empresa no encontrada";
        }
    }
}
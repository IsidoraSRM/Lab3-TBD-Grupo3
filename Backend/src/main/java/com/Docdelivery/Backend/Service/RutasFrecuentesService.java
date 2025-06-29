package com.Docdelivery.Backend.Service;

import com.Docdelivery.Backend.Entity.HistorialRepartidoresEntity;
import com.Docdelivery.Backend.Repository.RepartidorRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class RutasFrecuentesService {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private RepartidorRepository repartidorRepository;

    public List<TramoFrecuenteDto> obtenerTramosFrecuentesUltimos7Dias(int topN) {
        Date fechaLimite = Date.from(Instant.now().minusSeconds(7 * 24 * 60 * 60));

        // Usa la nueva función para obtener solo id y nombre
        Map<Long, String> mapaRepartidores = repartidorRepository.findAllIdAndNombre()
            .stream()
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        Aggregation agg = Aggregation.newAggregation(
            Aggregation.unwind("rutas"),
            Aggregation.match(Criteria.where("rutas.timestamp").gte(fechaLimite)),
            Aggregation.sort(Sort.by("repartidor_id").ascending().and(Sort.by("rutas.timestamp").ascending())),
            Aggregation.group("repartidor_id")
                .push("rutas").as("rutas")
        );

        AggregationResults<Map> results = mongoTemplate.aggregate(
            agg, "historial_repartidores", Map.class
        );

        Map<String, TramoFrecuenteDto> tramoFrecuencia = new HashMap<>();

        for (Map doc : results.getMappedResults()) {
            Object repartidorIdObj = doc.get("_id");
            final Long repartidorId;
            if (repartidorIdObj instanceof Number) {
                repartidorId = ((Number) repartidorIdObj).longValue();
            } else if (repartidorIdObj instanceof String) {
                Long tempId;
                try {
                    tempId = Long.parseLong((String) repartidorIdObj);
                } catch (NumberFormatException e) {
                    continue;
                }
                repartidorId = tempId;
            } else {
                continue;
            }
            // Busca el nombre en el mapa
            String nombreRepartidor = mapaRepartidores.getOrDefault(repartidorId, "Desconocido");

            List<Map<String, Object>> rutas = (List<Map<String, Object>>) doc.get("rutas");
            if (rutas == null || rutas.size() < 2) continue;

            for (int i = 0; i < rutas.size() - 1; i++) {
                Map<String, Object> from = rutas.get(i);
                Map<String, Object> to = rutas.get(i + 1);

                String key = repartidorId + ":" + from.get("lat") + "," + from.get("lng") + "->" + to.get("lat") + "," + to.get("lng");

                tramoFrecuencia.compute(key, (k, v) -> {
                    if (v == null) {
                        return new TramoFrecuenteDto(
                            repartidorId,
                            nombreRepartidor,
                            (Double) from.get("lat"), (Double) from.get("lng"),
                            (Double) to.get("lat"), (Double) to.get("lng"),
                            1
                        );
                    } else {
                        v.setFrecuencia(v.getFrecuencia() + 1);
                        return v;
                    }
                });
            }
        }

        return tramoFrecuencia.values().stream()
                .sorted(Comparator.comparingInt(TramoFrecuenteDto::getFrecuencia).reversed())
                .limit(topN)
                .toList();
    }

    // DTO para devolver los tramos frecuentes
    public static class TramoFrecuenteDto {
        private Long repartidorId;
        private String nombreRepartidor;
        private Double latDesde;
        private Double lngDesde;
        private Double latHasta;
        private Double lngHasta;
        private int frecuencia;

        public TramoFrecuenteDto(Long repartidorId, String nombreRepartidor,Double latDesde, Double lngDesde, Double latHasta, Double lngHasta, int frecuencia) {
            this.repartidorId = repartidorId;
            this.nombreRepartidor = nombreRepartidor;
            this.latDesde = latDesde;
            this.lngDesde = lngDesde;
            this.latHasta = latHasta;
            this.lngHasta = lngHasta;
            this.frecuencia = frecuencia;
        }

        // Getters y setters
        public Long getRepartidorId() { return repartidorId; }
        public String getNombreRepartidor() { return nombreRepartidor; }
        public Double getLatDesde() { return latDesde; }
        public Double getLngDesde() { return lngDesde; }
        public Double getLatHasta() { return latHasta; }
        public Double getLngHasta() { return lngHasta; }
        public int getFrecuencia() { return frecuencia; }
        public void setFrecuencia(int frecuencia) { this.frecuencia = frecuencia; }
    }
}
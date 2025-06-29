package com.Docdelivery.Backend.Service;

import com.Docdelivery.Backend.dto.ClienteSinCompraDTO;
import com.Docdelivery.Backend.Entity.NavegacionUsuariosEntity;
import com.Docdelivery.Backend.Repository.NavegacionUsuariosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class NavegacionUsuariosServices {

    @Autowired
    private NavegacionUsuariosRepository navegacionRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    /**
     * Detectar clientes que realizaron búsquedas sin concretar pedidos
     * Implementación usando MongoDB Aggregation Framework
     */
    public List<ClienteSinCompraDTO> detectarClientesSinCompra() {

        // Agregación MongoDB para detectar clientes con búsquedas pero sin compras
        Aggregation aggregation = Aggregation.newAggregation(
                // Etapa 1: Desenrollar el array de eventos
                Aggregation.unwind("eventos"),

                // Etapa 2: Agrupar por cliente y tipo de evento
                Aggregation.group("cliente_id")
                        .push("eventos").as("todosEventos")
                        .addToSet("eventos.tipo").as("tiposEventos")
                        .count().as("totalEventos"),

                // Etapa 3: Filtrar solo clientes que tienen búsquedas pero NO tienen compras
                Aggregation.match(
                        Criteria.where("tiposEventos").in("busqueda")
                                .and("tiposEventos").not().in("compra")
                ),

                // Etapa 4: Proyectar los campos necesarios
                Aggregation.project()
                        .andExpression("_id").as("clienteId")
                        .andExpression("todosEventos").as("eventos")
                        .andExpression("totalEventos").as("totalEventos")
        );

        // Ejecutar la agregación
        List<Map> resultados = mongoTemplate.aggregate(
                aggregation, "navegacion_usuarios", Map.class
        ).getMappedResults();

        // Procesar los resultados y crear los DTOs
        return resultados.stream()
                .map(this::procesarResultadoAggregation)
                .collect(Collectors.toList());
    }

    /**
     * Método alternativo usando programación Java (menos eficiente pero más legible)
     */
    public List<ClienteSinCompraDTO> detectarClientesSinCompraAlternativo() {
        List<NavegacionUsuariosEntity> todasNavegaciones = navegacionRepository.findAll();

        return todasNavegaciones.stream()
                .filter(this::tieneNavegacionSinCompra)
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    /**
     * Procesar resultado de la agregación MongoDB
     */
    private ClienteSinCompraDTO procesarResultadoAggregation(Map resultado) {
        String clienteId = (String) resultado.get("clienteId");
        List<Map> eventos = (List<Map>) resultado.get("eventos");

        // Contar búsquedas y clicks
        long busquedas = eventos.stream()
                .filter(evento -> "busqueda".equals(evento.get("tipo")))
                .count();

        long clicks = eventos.stream()
                .filter(evento -> "click".equals(evento.get("tipo")))
                .count();

        // Obtener términos buscados
        List<String> terminosBuscados = eventos.stream()
                .filter(evento -> "busqueda".equals(evento.get("tipo")))
                .map(evento -> (String) evento.get("valor"))
                .distinct()
                .collect(Collectors.toList());

        // Obtener última búsqueda
        LocalDateTime ultimaBusqueda = eventos.stream()
                .filter(evento -> "busqueda".equals(evento.get("tipo")))
                .map(evento -> (LocalDateTime) evento.get("timestamp"))
                .max(LocalDateTime::compareTo)
                .orElse(null);

        return new ClienteSinCompraDTO(
                clienteId,
                (int) busquedas,
                (int) clicks,
                ultimaBusqueda,
                terminosBuscados
        );
    }

    /**
     * Verificar si una navegación tiene búsquedas pero no compras
     */
    private boolean tieneNavegacionSinCompra(NavegacionUsuariosEntity navegacion) {
        if (navegacion.getEventos() == null) return false;

        boolean tieneBusqueda = navegacion.getEventos().stream()
                .anyMatch(evento -> "busqueda".equals(evento.getTipo()));

        boolean tieneCompra = navegacion.getEventos().stream()
                .anyMatch(evento -> "compra".equals(evento.getTipo()));

        return tieneBusqueda && !tieneCompra;
    }

    /**
     * Convertir NavegacionUsuario a DTO
     */
    private ClienteSinCompraDTO convertirADTO(NavegacionUsuariosEntity navegacion) {
        List<NavegacionUsuariosEntity.Evento> eventos = navegacion.getEventos();

        int cantidadBusquedas = (int) eventos.stream()
                .filter(e -> "busqueda".equals(e.getTipo()))
                .count();

        int cantidadClicks = (int) eventos.stream()
                .filter(e -> "click".equals(e.getTipo()))
                .count();

        List<String> terminosBuscados = eventos.stream()
                .filter(e -> "busqueda".equals(e.getTipo()))
                .map(NavegacionUsuariosEntity.Evento::getValor)
                .distinct()
                .collect(Collectors.toList());

        LocalDateTime ultimaBusqueda = eventos.stream()
                .filter(e -> "busqueda".equals(e.getTipo()))
                .map(NavegacionUsuariosEntity.Evento::getTimestamp)
                .max(LocalDateTime::compareTo)
                .orElse(null);

        return new ClienteSinCompraDTO(
                navegacion.getClienteId(),
                cantidadBusquedas,
                cantidadClicks,
                ultimaBusqueda,
                terminosBuscados
        );
    }
}
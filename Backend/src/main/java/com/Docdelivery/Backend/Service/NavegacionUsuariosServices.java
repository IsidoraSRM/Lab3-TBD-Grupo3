package com.Docdelivery.Backend.Service;

import com.Docdelivery.Backend.Entity.ClienteEntity;
import com.Docdelivery.Backend.Repository.ClienteRepository;
import com.Docdelivery.Backend.dto.ClienteSinCompraDTO;
import com.Docdelivery.Backend.Entity.NavegacionUsuariosEntity;
import com.Docdelivery.Backend.Repository.NavegacionUsuariosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class NavegacionUsuariosServices {

    @Autowired
    private NavegacionUsuariosRepository navegacionRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    /**
     * MÉTODO PRINCIPAL: Detectar clientes con búsquedas sin compra
     */
    public List<ClienteSinCompraDTO> detectarClientesSinCompra() {
        try {
            // 1. Obtener TODAS las navegaciones de MongoDB
            List<NavegacionUsuariosEntity> navegaciones = navegacionRepository.findAll();
            System.out.println("Total navegaciones en MongoDB: " + navegaciones.size());

            // 2. Filtrar solo las que tienen búsquedas SIN compras
            List<NavegacionUsuariosEntity> navegacionesSinCompra = navegaciones.stream()
                    .filter(this::tieneBusquedaSinCompra)
                    .collect(Collectors.toList());

            System.out.println("Navegaciones con búsqueda sin compra: " + navegacionesSinCompra.size());

            // 3. Convertir a DTO obteniendo datos de PostgreSQL
            List<ClienteSinCompraDTO> resultado = new ArrayList<>();

            for (NavegacionUsuariosEntity navegacion : navegacionesSinCompra) {
                try {
                    // Convertir cliente_id a Long
                    Long clienteId = Long.parseLong(navegacion.getClienteId().trim());
                    System.out.println("Procesando cliente ID: " + clienteId);

                    // Buscar cliente en PostgreSQL sin cargar ubicación
                    Optional<ClienteEntity> clienteOpt = clienteRepository.findByIdSinUbicacion(clienteId);


                    if (clienteOpt.isPresent()) {
                        ClienteEntity cliente = clienteOpt.get();
                        System.out.println("Cliente encontrado: " + cliente.getNombre());

                        // Crear DTO
                        ClienteSinCompraDTO dto = crearDTO(cliente, navegacion.getEventos());
                        resultado.add(dto);
                    } else {
                        System.out.println("Cliente no encontrado en PostgreSQL: " + clienteId);
                    }

                } catch (NumberFormatException e) {
                    System.err.println("Error convirtiendo cliente_id: " + navegacion.getClienteId());
                }
            }

            System.out.println("Total resultado final: " + resultado.size());
            return resultado;

        } catch (Exception e) {
            System.err.println("Error en detectarClientesSinCompra: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    /**
     * Verificar si tiene búsquedas pero NO tiene compras
     */
    private boolean tieneBusquedaSinCompra(NavegacionUsuariosEntity navegacion) {
        if (navegacion.getEventos() == null || navegacion.getEventos().isEmpty()) {
            return false;
        }

        // Contar búsquedas
        long busquedas = navegacion.getEventos().stream()
                .filter(evento -> "busqueda".equals(evento.getTipo()))
                .count();

        // Contar compras
        long compras = navegacion.getEventos().stream()
                .filter(evento -> "compra".equals(evento.getTipo()))
                .count();

        boolean resultado = busquedas > 0 && compras == 0;

        System.out.println("Cliente " + navegacion.getClienteId() +
                " - Búsquedas: " + busquedas +
                ", Compras: " + compras +
                ", Sin compra: " + resultado);

        return resultado;
    }

    /**
     * Crear DTO con estadísticas de navegación
     */
    private ClienteSinCompraDTO crearDTO(ClienteEntity cliente, List<NavegacionUsuariosEntity.Evento> eventos) {
        // Contar búsquedas
        int cantidadBusquedas = (int) eventos.stream()
                .filter(e -> "busqueda".equals(e.getTipo()))
                .count();

        // Contar clicks
        int cantidadClicks = (int) eventos.stream()
                .filter(e -> "click".equals(e.getTipo()))
                .count();

        // Obtener términos buscados únicos
        List<String> terminosBuscados = eventos.stream()
                .filter(e -> "busqueda".equals(e.getTipo()))
                .map(NavegacionUsuariosEntity.Evento::getValor)
                .distinct()
                .collect(Collectors.toList());

        // Fecha de última búsqueda
        LocalDateTime ultimaBusqueda = eventos.stream()
                .filter(e -> "busqueda".equals(e.getTipo()))
                .map(NavegacionUsuariosEntity.Evento::getTimestamp)
                .max(LocalDateTime::compareTo)
                .orElse(null);

        return new ClienteSinCompraDTO(
                cliente.getClienteId(),
                cliente.getNombre(),
                cliente.getEmail(),
                cliente.getTelefono(),
                cantidadBusquedas,
                cantidadClicks,
                ultimaBusqueda,
                terminosBuscados
        );
    }

    /**
     * MÉTODO DE VERIFICACIÓN: Para debug
     */
    public Map<String, Object> verificarDatos() {
        Map<String, Object> info = new HashMap<>();

        try {
            // MongoDB
            List<NavegacionUsuariosEntity> navegaciones = navegacionRepository.findAll();
            info.put("mongodb_total", navegaciones.size());

            List<Map<String, Object>> mongoInfo = new ArrayList<>();
            for (NavegacionUsuariosEntity nav : navegaciones) {
                Map<String, Object> navInfo = new HashMap<>();
                navInfo.put("cliente_id", nav.getClienteId());
                navInfo.put("eventos_total", nav.getEventos() != null ? nav.getEventos().size() : 0);

                if (nav.getEventos() != null) {
                    List<String> tipos = nav.getEventos().stream()
                            .map(NavegacionUsuariosEntity.Evento::getTipo)
                            .collect(Collectors.toList());
                    navInfo.put("tipos_eventos", tipos);
                    navInfo.put("tiene_busqueda", tipos.contains("busqueda"));
                    navInfo.put("tiene_compra", tipos.contains("compra"));
                }

                mongoInfo.add(navInfo);
            }
            info.put("mongodb_data", mongoInfo);

            // PostgreSQL
            List<ClienteEntity> clientes = clienteRepository.findAll();
            info.put("postgresql_total", clientes.size());

            List<Map<String, Object>> pgInfo = new ArrayList<>();
            for (ClienteEntity cliente : clientes) {
                Map<String, Object> clienteInfo = new HashMap<>();
                clienteInfo.put("cliente_id", cliente.getClienteId());
                clienteInfo.put("nombre", cliente.getNombre());
                pgInfo.add(clienteInfo);
            }
            info.put("postgresql_data", pgInfo);

        } catch (Exception e) {
            info.put("error", e.getMessage());
        }

        return info;
    }
}
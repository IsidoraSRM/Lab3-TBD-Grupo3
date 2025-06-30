package com.Docdelivery.Backend.Service;

import com.Docdelivery.Backend.Entity.LogsPedidosEntity;
import com.Docdelivery.Backend.Repository.LogsPedidosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class LogsPedidosService {

    @Autowired
    private LogsPedidosRepository repo;

    public List<String> getPedidosConCambiosRapidos() {
        List<String> resultado = new ArrayList<>();

        List<LogsPedidosEntity> pedidos = repo.findAll();

        for (LogsPedidosEntity pedido : pedidos) {
            List<LogsPedidosEntity.EventoPedido> eventos = pedido.getEventos();

            if (eventos == null || eventos.size() <= 3) continue;

            List<Instant> timestamps = eventos.stream()
                    .map(LogsPedidosEntity.EventoPedido::getTimestamp)
                    .sorted()
                    .toList();

            for (int i = 0; i < timestamps.size(); i++) {
                int cuenta = 1;
                Instant inicio = timestamps.get(i);
                for (int j = i + 1; j < timestamps.size(); j++) {
                    if (Duration.between(inicio, timestamps.get(j)).toMinutes() <= 10) {
                        cuenta++;
                        if (cuenta > 3) {
                            resultado.add(pedido.getPedido_id());
                            break;
                        }
                    } else break;
                }
                if (resultado.contains(pedido.getPedido_id())) break;
            }
        }

        return resultado;
    }
}

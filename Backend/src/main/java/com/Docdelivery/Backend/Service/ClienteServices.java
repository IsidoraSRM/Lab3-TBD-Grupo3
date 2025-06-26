package com.Docdelivery.Backend.Service;

import com.Docdelivery.Backend.Entity.ClienteEntity;
import com.Docdelivery.Backend.Repository.ClienteRepository;
import com.Docdelivery.Backend.dto.ClienteConTotalGastadoDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class ClienteServices {
    @Autowired
    private ClienteRepository clienteRepository;

    //consulta numero 1
    public Optional<ClienteConTotalGastadoDTO> obtenerClienteMasAGastado() {return clienteRepository.obtenerClienteQueMasHaGastado();}


    //cliente por idUsuario
    public Optional<Long> findClienteIdByUsuarioId(Long usuarioId) {return clienteRepository.findClienteIdByUsuarioId(usuarioId);}

    public void actualizarCliente(Long id) {
        ClienteEntity cliente = clienteRepository.findById(id).get();
        clienteRepository.update(cliente);
    }

    public void eliminarCliente(Long id) {
        clienteRepository.removeCliente(id);
    }


    // --------------------------- Lab 2 ---------------------------

    // Consulta 6: Determinar los clientes que están a más de 5km de cualquier empresa o farmacia.
    public List<Map<String, Object>> obtenerClientesMasDe5Km() {
        return clienteRepository.obtenerClientesMasDe5Km();
    }

    // Consulta 2: Verificar si los clientes están dentro de una zona de cobertura (con buffer de 1km)
    public List<Map<String, Object>> verificarClienteEnZona(Long clienteId) {
        return clienteRepository.verificarClienteEnZona(clienteId);
    }

}

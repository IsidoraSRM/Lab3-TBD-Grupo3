package com.Docdelivery.Backend.Repository;

import java.util.List;
import java.util.Map;

public interface ClienteRepositoryCustom {
    List<Map<String, Object>> verificarClienteEnZona(Long clienteId);

}


// src/main/java/com/Docdelivery/Backend/Service/RepartidorService.java
package com.Docdelivery.Backend.Service;

import com.Docdelivery.Backend.Entity.RepartidorEntity;
import com.Docdelivery.Backend.dto.RepartidorDistanciaDTO;
import com.Docdelivery.Backend.dto.TopRepartidorDto;
import com.Docdelivery.Backend.Repository.RepartidorRepository;
import com.Docdelivery.Backend.dto.VistaRepartidorDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RepartidorService {

    @Autowired
    private RepartidorRepository repartidorRepository;

    public void createRepartidor(RepartidorEntity repartidor) {
        repartidorRepository.save(repartidor);
    }

    public List<RepartidorEntity> getAllRepartidores() {
        return repartidorRepository.findAll();
    }

    public Optional<RepartidorEntity> getRepartidorById(Long id) {
        return repartidorRepository.findById(id);
    }

    public boolean updateRepartidor(RepartidorEntity repartidor) {
        return repartidorRepository.update(repartidor);
    }

    public boolean deleteRepartidor(Long id) {
        return repartidorRepository.deleteById(id);
    }


    public List<TopRepartidorDto> findTop3ByRendimiento() {
        return repartidorRepository.findTop3ByRendimiento();
    }

        public Optional<Long> getRepartidorIdByUsuarioId(Long usuarioId) {
    return repartidorRepository.findRepartidorIdByUsuarioId(usuarioId);
}
    public List<VistaRepartidorDto> obtenerDesempeno() {
        return repartidorRepository.obtenerDesempenoRepartidores();
    }

    public List<RepartidorDistanciaDTO> obtenerDistanciasUltimoMes() {
        return repartidorRepository.calcularDistanciaRecorridaUltimoMes();
    }
}
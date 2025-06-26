package com.Docdelivery.Backend.Service;

import com.Docdelivery.Backend.Entity.MedioDePagoEntity;
import com.Docdelivery.Backend.Repository.MedioDePagoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MedioDePagoService {

    private final MedioDePagoRepository medioDePagoRepository;

    @Autowired
    public MedioDePagoService(MedioDePagoRepository medioDePagoRepository) {
        this.medioDePagoRepository = medioDePagoRepository;
    }

    // Create
    public void save(MedioDePagoEntity medioDePago) {
        medioDePagoRepository.save(medioDePago);
    }

    // Read - Get All
    public List<MedioDePagoEntity> findAll() {
        return medioDePagoRepository.findAll();
    }

    // Read - Get by ID
    public Optional<MedioDePagoEntity> findById(Long id) {
        return medioDePagoRepository.findById(id);
    }

    // Update
    public boolean update(MedioDePagoEntity medioDePago) {
        return medioDePagoRepository.update(medioDePago);
    }

    // Delete
    public boolean deleteById(Long id) {
        return medioDePagoRepository.deleteById(id);
    }
}
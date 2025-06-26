package com.Docdelivery.Backend.Service;

import com.Docdelivery.Backend.Entity.ServiciosEntity;
import com.Docdelivery.Backend.Repository.ServiciosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ServiciosServices {
    @Autowired
    ServiciosRepository serviciosRepository;

    public Optional<ServiciosEntity> getServicioById(Long Id){
        return serviciosRepository.findbById(Id);
    }
}

package com.Docdelivery.Backend.Service;

import com.Docdelivery.Backend.Entity.ClienteEntity;
import com.Docdelivery.Backend.Entity.UsuarioEntity;
import com.Docdelivery.Backend.Repository.ClienteRepository;
import com.Docdelivery.Backend.Repository.UsuarioRepository;

import org.locationtech.jts.geom.Point;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
public class UsuarioService {
    @Autowired
    private UsuarioRepository userRepo;

    public UsuarioEntity register(String rut, String name, String email, String phone, Date fechaDeNacimiento, String password, String rol, Point ubicacion) {
        Optional<UsuarioEntity> existenteEmail = userRepo.findByEmail(email);
        if (existenteEmail.isPresent()) { // Si el email ya está registrado
            throw new RuntimeException("El correo " + email + " ya está registrado. Usa uno diferente.");
        }

        UsuarioEntity user = new UsuarioEntity(0, rut, name, email, phone, fechaDeNacimiento, password, rol,ubicacion);
        userRepo.save(user); // Guardar el usuario en la base de datos
        return user;
    }

    public int login(String email, String password) {
        Optional<UsuarioEntity> optionalUser = userRepo.findByEmail(email);

        if (optionalUser.isPresent()) { // Si el usuario existe
            UsuarioEntity user = optionalUser.get();
            if (password.equals(user.getPassword())) { // Verificar la contraseña
                // Retornar un valor diferente según el rol del usuario
                switch (user.getRole()) {
                    case "ADMIN":
                        return 1;
                    case "TRABAJADOR":
                        return 2;
                    case "CLIENTE":
                        return 3;
                }
            }
        }
        return 0; // Retornar 0 si no se cumple ninguna condición
    }

    public Optional<UsuarioEntity> getUserById(long id) {
        return userRepo.findAll().stream()
                .filter(u -> u.getIdUsuario() == id)
                .findFirst();
    }

    public Optional<Long> getIdByEmail(String email) {
        return userRepo.findIdByEmail(email);
    }

    public Optional<UsuarioEntity> getUserByEmail(String email) {
        return userRepo.findByEmail(email);
    }
}
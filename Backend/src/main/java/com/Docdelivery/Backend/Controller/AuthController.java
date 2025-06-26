package com.Docdelivery.Backend.Controller;

import org.locationtech.jts.geom.Point;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import com.Docdelivery.Backend.Config.JwtUtil;
import com.Docdelivery.Backend.dto.RegisterDto;
import com.Docdelivery.Backend.dto.LoginDto;
import com.Docdelivery.Backend.Repository.UsuarioRepository;
import com.Docdelivery.Backend.Repository.ClienteRepository;
import com.Docdelivery.Backend.Repository.RepartidorRepository;
import com.Docdelivery.Backend.Entity.UsuarioEntity;
import com.Docdelivery.Backend.Entity.ClienteEntity;
import com.Docdelivery.Backend.Entity.RepartidorEntity;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/auth")
@CrossOrigin("*")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UsuarioRepository usuarioRepository;
    private final ClienteRepository clienteRepository;
    private final RepartidorRepository repartidorRepository;
    private final PasswordEncoder passwordEncoder;
    private final GeometryFactory geometryFactory = new GeometryFactory();
    @Autowired
    public AuthController(
            AuthenticationManager authenticationManager,
            JwtUtil jwtUtil,
            UsuarioRepository usuarioRepository,
            ClienteRepository clienteRepository,
            RepartidorRepository repartidorRepository,
            PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.usuarioRepository = usuarioRepository;
        this.clienteRepository = clienteRepository;
        this.repartidorRepository = repartidorRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDto loginDto) {
        System.out.println("Datos recibidos en el back: " + loginDto);

        try {
            Optional<UsuarioEntity> optionalUser = usuarioRepository.findByEmail(loginDto.getEmail());

            if (optionalUser.isEmpty()) {
                System.out.println("No se encontró el usuario con el correo proporcionado.");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("El correo no está registrado. Por favor, regístrese primero.");
            }

            UsuarioEntity user = optionalUser.get();

            UsernamePasswordAuthenticationToken loginToken = new UsernamePasswordAuthenticationToken(
                    loginDto.getEmail(),
                    loginDto.getPassword()
            );

            System.out.println("Datos del login: \n\tcorreo: " + loginToken.getPrincipal().toString());
            try {
                authenticationManager.authenticate(loginToken);
                System.out.println("Autenticación exitosa.");
            } catch (Exception e) {
                System.out.println("Error en el AuthenticationManager: " + e.getMessage());
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciales incorrectas.");
            }

            String jwt = jwtUtil.createToken(loginDto.getEmail(), user.getRole());

            Map<String, Object> response = new HashMap<>();
            response.put("token", jwt);
            response.put("userId", user.getIdUsuario());
            response.put("role", user.getRole());
            response.put("ubicacion", user.getUbicacion().toString());

            return ResponseEntity.ok(response);

        } catch (BadCredentialsException e) {
            System.out.println("Credenciales incorrectas: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Credenciales incorrectas. Verifique su correo y contraseña.");
        } catch (Exception e) {
            System.out.println("Error en el proceso de autenticación: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error interno en el servidor.");
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterDto registerDto) {
        Optional<UsuarioEntity> foundUser = usuarioRepository.findByEmail(registerDto.getEmail());
        if (foundUser.isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("El correo ya está registrado.");
        }

        try {
            String rol = (registerDto.getRole() == null || registerDto.getRole().isEmpty()) ? "CLIENTE" : registerDto.getRole();
            String hashedPassword = passwordEncoder.encode(registerDto.getPassword());
            Point ubicacion = geometryFactory.createPoint(
                new Coordinate(
                    registerDto.getLongitud(),
                    registerDto.getLatitud()
                )
            );
            UsuarioEntity newUser = new UsuarioEntity(
                    0,
                    registerDto.getRut(),
                    registerDto.getNameParam(),
                    registerDto.getEmail(),
                    registerDto.getPhone(),
                    registerDto.getBirthdate(),
                    hashedPassword,
                    rol,
                    ubicacion
            );

            usuarioRepository.save(newUser);

            if ("CLIENTE".equalsIgnoreCase(rol)) {

                ClienteEntity cliente = new ClienteEntity(0L, registerDto.getNameParam(), "Sin dirección", registerDto.getEmail(), registerDto.getPhone(),ubicacion);
                clienteRepository.save(cliente);
            } else if ("TRABAJADOR".equalsIgnoreCase(rol)) {
                RepartidorEntity repartidor = new RepartidorEntity(0L, registerDto.getNameParam(), registerDto.getPhone(), true, ubicacion);
                repartidorRepository.save(repartidor);
            }

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body("Usuario registrado con éxito en la tabla correspondiente.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error interno en el servidor.");
        }
    }


}
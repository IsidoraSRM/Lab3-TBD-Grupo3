package com.Docdelivery.Backend.Config;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.context.annotation.Configuration;

import java.util.Date;
import java.util.concurrent.TimeUnit;

@Configuration
public class JwtUtil {

    // Se encarga de generar, validar y extraer información del token JWT
    private static final String SECRET_KEY = "alvolante";
    private static final Algorithm ALGORITHM = Algorithm.HMAC256(SECRET_KEY);

    // Método para crear un JWT
    public String createToken(String email, String role) {
        System.out.println("En la funcin creatoToken para crear el token.");
        try{
            return JWT.create()
                    .withSubject(email)
                    .withIssuer("tbd")// es para ver quien emitio el token, to be defined
                    .withIssuedAt(new Date())
                    .withExpiresAt(new Date(System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(666))) // Duración del token
                    .withClaim("role", role) 
                    .sign(ALGORITHM);
        } catch (Exception e) {
            System.out.println("Error al generar el token JWT: " + e.getMessage());
            throw new RuntimeException("Error al generar el token JWT.", e);
        }

    }

    // Método para validar un JWT
    public boolean isValid(String jwt) {
        try {
            JWT.require(ALGORITHM)
                    .build()
                    .verify(jwt);
            return true; // Si no lanza excepción, el token es válido
        } catch (JWTVerificationException e) {
            System.out.println("Token inválido: " + e.getMessage());
            return false;
        }
    }

    // extrae el email del JWT
    public String getEmail(String jwt) {
        DecodedJWT decodedJWT = JWT.require(ALGORITHM)
                .build()
                .verify(jwt);
        return decodedJWT.getSubject(); // Retorna el "subject" del token
    }

}

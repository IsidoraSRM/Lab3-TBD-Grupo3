package com.Docdelivery.Backend.Config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import org.springframework.http.HttpHeaders;

@Component
public class JwtFilter extends OncePerRequestFilter {
    // el filtro intercepta todas las solicitudes entrantes y verifica si el token es valido

    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;

    @Autowired
    public JwtFilter(JwtUtil jwtUtil, UserDetailsService userDetailsService) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    @Override
protected void doFilterInternal(HttpServletRequest request,
                                HttpServletResponse response,
                                FilterChain filterChain)
        throws ServletException, IOException {

    System.out.println("==== JwtFilter procesando solicitud: " + request.getMethod() + " " + request.getRequestURI() + " ====");

    String requestUri = request.getRequestURI();
    // Ignorar rutas públicas como /auth/**
    if (requestUri.startsWith("/auth/")) {
        System.out.println("Ruta pública (/auth/), ignorando filtro JWT");
        filterChain.doFilter(request, response);
        return; // Salir del filtro
    }

    // 1. Se obtiene el header Authorization
    String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
    System.out.println("Authorization header: " + (authHeader != null ? "presente" : "ausente"));
    
    if (authHeader == null || authHeader.isEmpty() || !authHeader.startsWith("Bearer ")) {
        System.out.println("Token JWT no encontrado o inválido en el header");
        filterChain.doFilter(request, response);
        return;
    }

    // Se extra el token del encabezado
    String jwt = authHeader.split(" ")[1].trim();
    System.out.println("Token JWT extraído: " + jwt.substring(0, Math.min(10, jwt.length())) + "...");

    // 2. Validar que el token sea válido
    if (!this.jwtUtil.isValid(jwt)) {
        System.out.println("Token JWT inválido o expirado");
        filterChain.doFilter(request, response);
        return;
    }
    System.out.println("Token JWT válido");

    // 3. Extraer el correo del token
    String email = this.jwtUtil.getEmail(jwt);
    System.out.println("Email extraído del token: " + email);

    //4 se busca por email
    try {
        UserDetails userDetails = userDetailsService.loadUserByUsername(email);
        System.out.println("Usuario cargado: " + userDetails.getUsername());
        System.out.println("Roles/Autoridades del usuario: " + userDetails.getAuthorities());
        
        // 5. Cargar al usuario en el contexto de seguridad
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(
                        userDetails.getUsername(),
                        userDetails.getPassword(),
                        // se añade el rol
                        userDetails.getAuthorities());

        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        System.out.println("Autenticación exitosa: " + authenticationToken);
        
        // Verificar si el usuario tiene ROLE_CLIENTE o ROLE_ADMIN
        boolean tieneRolCliente = userDetails.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_CLIENTE"));
        boolean tieneRolAdmin = userDetails.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));
                
        System.out.println("¿Tiene ROLE_CLIENTE? " + tieneRolCliente);
        System.out.println("¿Tiene ROLE_ADMIN? " + tieneRolAdmin);
        
    } catch (Exception e) {
        System.out.println("Error al cargar el usuario: " + e.getMessage());
        e.printStackTrace();
    }

    System.out.println("==== Fin del procesamiento en JwtFilter ====");
    filterChain.doFilter(request, response);
}
}
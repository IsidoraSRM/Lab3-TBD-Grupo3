package com.Docdelivery.Backend.dto;

public class ServiciosDto {
    private String servicio;
    private String categoria;
    private int cantidadPedidos;

    public ServiciosDto(String servicio, String categoria, int cantidadPedidos) {
        this.servicio = servicio;
        this.categoria = categoria;
        this.cantidadPedidos = cantidadPedidos;
    }

    public String getServicio() { return servicio; }
    public String getCategoria() { return categoria; }
    public int getCantidadPedidos() { return cantidadPedidos; }
}


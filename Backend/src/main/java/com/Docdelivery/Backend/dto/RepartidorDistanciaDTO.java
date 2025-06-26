package com.Docdelivery.Backend.dto;

public class RepartidorDistanciaDTO {
    private Long repartidorId;
    private String nombre;
    private double distanciaTotalMetros;

    public RepartidorDistanciaDTO(Long repartidorId, String nombre, double distanciaTotalMetros) {
        this.repartidorId = repartidorId;
        this.nombre = nombre;
        this.distanciaTotalMetros = distanciaTotalMetros;
    }

    public Long getRepartidorId() {
        return repartidorId;
    }

    public void setRepartidorId(Long repartidorId) {
        this.repartidorId = repartidorId;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public double getDistanciaTotalMetros() {
        return distanciaTotalMetros;
    }

    public void setDistanciaTotalMetros(double distanciaTotalMetros) {
        this.distanciaTotalMetros = distanciaTotalMetros;
    }
}

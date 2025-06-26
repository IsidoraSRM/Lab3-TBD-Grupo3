package com.Docdelivery.Backend.dto;

public class VistaRepartidorDto {
    private String repartidor;
    private int totalEntregas;
    private double tiempoPromedioHoras;
    private double calificacionPromedio;

    public VistaRepartidorDto(String repartidor, int totalEntregas, double tiempoPromedioHoras, double calificacionPromedio) {
        this.repartidor = repartidor;
        this.totalEntregas = totalEntregas;
        this.tiempoPromedioHoras = tiempoPromedioHoras;
        this.calificacionPromedio = calificacionPromedio;
    }

    public String getRepartidor() { return repartidor; }
    public int getTotalEntregas() { return totalEntregas; }
    public double getTiempoPromedioHoras() { return tiempoPromedioHoras; }
    public double getCalificacionPromedio() { return calificacionPromedio; }
}

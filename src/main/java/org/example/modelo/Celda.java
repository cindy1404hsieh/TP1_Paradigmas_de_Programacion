package org.example.modelo;


public class Celda {
    private final Coordenada posicion;
    private Estado estado;

    public Celda(Estado estado, Coordenada posicion) {
        this.estado = estado;
        this.posicion = posicion;
    }

    public Coordenada getPosicion() {
        return posicion;
    }

    public enum Estado {
        LIBRE,
        OCUPADA,
        INCENDIADA
    }

    public Estado getEstado() {
        return estado;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }

    public boolean isLibre() {
        return estado == Estado.LIBRE;
    }

    public boolean isIncendiada() {
        return estado == Estado.INCENDIADA;
    }
}

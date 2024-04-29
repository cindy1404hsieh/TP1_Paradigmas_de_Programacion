package org.example.modelo;

public enum Direccion {
    ARRIBA(-1, 0),
    ABAJO(1, 0),
    IZQUIERDA(0, -1),
    DERECHA(0, 1),
    ARRIBA_IZQUIERDA(-1, -1),
    ARRIBA_DERECHA(-1, 1),
    ABAJO_IZQUIERDA(1, -1),
    ABAJO_DERECHA(1, 1),
    PERMANECER(0, 0);

    private final Coordenada movimiento;

    Direccion(int fila, int columna) {
        this.movimiento = new Coordenada(fila, columna);
    }
    public Coordenada getMovimiento() {
        return movimiento;
    }
}
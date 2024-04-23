package org.example.vista;

import org.example.modelo.Direccion;
import org.example.modelo.Tablero;

public class ActionMover implements Action{
    private final Direccion direccion;

    public ActionMover(Direccion direccion) {
        this.direccion = direccion;
    }

    @Override
    public void apply(Tablero tablero) {
        tablero.getJugador().mover(tablero, direccion);
        tablero.moverRobots();
    }
}

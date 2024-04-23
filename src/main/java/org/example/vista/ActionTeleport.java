package org.example.vista;

import org.example.modelo.Tablero;

public class ActionTeleport implements Action{
    @Override
    public void apply(Tablero tablero) {
        tablero.getJugador().teletransportarse(tablero);
        tablero.moverRobots();

    }
}

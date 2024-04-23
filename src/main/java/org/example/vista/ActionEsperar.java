package org.example.vista;

import org.example.modelo.Tablero;

public class ActionEsperar implements Action{
    @Override
    public void apply(Tablero tablero) {
        tablero.moverRobots();
        tablero.verificarColisiones();
        tablero.actualizarCeldasIncendiadas();
    }
}

package org.example.vista;

import org.example.modelo.Coordenada;
import org.example.modelo.Direccion;
import org.example.modelo.Tablero;

public class ActionMover implements Action {
    private final Direccion direccion;

    public ActionMover(Direccion direccion) {
        this.direccion = direccion;
    }

    @Override
    public void apply(Tablero tablero) {
        tablero.getJugador().mover(tablero, direccion);
    }
}

class ActionMoverACelda implements Action {
    private final Coordenada siguienteCoord;

    public ActionMoverACelda(Coordenada coordenada) {
        this.siguienteCoord = coordenada;
    }
    @Override
    public void apply(Tablero tablero) {
        tablero.getJugador().setPosicion(siguienteCoord);
    }
}

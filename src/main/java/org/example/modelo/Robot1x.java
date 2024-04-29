package org.example.modelo;

public class Robot1x extends Robot {

    public Robot1x(Coordenada posicion) {
        super(posicion);
    }

    public String getTipo() {
        return "Robot1x";
    }

    /*mueve el robot hacia la posicion del jugador en el tablero.
     este robot se mueve un paso hacia el jugador cada vez que se invoca este metodo.
     el tablero de juego donde se encuentra el robot,
     retorna la nueva posicion del robot despues de moverse hacia el jugador*/
    public Coordenada mover(Tablero tablero) {
        return moverHaciaJugador(tablero);
    }
}

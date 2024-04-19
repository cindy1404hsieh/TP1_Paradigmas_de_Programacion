package org.example.modelo;

public class Robot1x extends Robot {

    public Robot1x(Coordenada posicion) {
        super(posicion);
    }
    @Override
    public String getTipo() {
        return "Robot1x";
    }
    /*Mueve el robot hacia la posición del jugador en el tablero.
     Este robot se mueve un paso hacia el jugador cada vez que se invoca este método.
     El tablero de juego donde se encuentra el robot,
     retorna la nueva posición del robot después de moverse hacia el jugador.*/
    public Coordenada mover(Tablero tablero) {
        return moverHaciaJugador(tablero, 1);
    }
}

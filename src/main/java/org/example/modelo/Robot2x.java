package org.example.modelo;

public class Robot2x extends Robot {

    public Robot2x(Coordenada posicion) {
        super(posicion);
    }

    public String getTipo() {
        return "Robot2x";
    }
    /*Mueve el robot hacia la posición del jugador en el tablero.
     Este robot se mueve un paso hacia el jugador cada vez que se invoca este método.
     El tablero de juego donde se encuentra el robot,
     retorna la nueva posición del robot después de moverse hacia el jugador.*/

    public Coordenada mover(Tablero tablero) {
        for (int i = 0; i < 2; i++) {
            Coordenada siguientePosicion = moverHaciaJugador(tablero);
            if (tablero.esCeldaLibre(siguientePosicion)) {
                setPosicion(siguientePosicion);
                tablero.verificarColisiones();
            } else {
                break;
            }
        }
        return getPosicion();
    }

}
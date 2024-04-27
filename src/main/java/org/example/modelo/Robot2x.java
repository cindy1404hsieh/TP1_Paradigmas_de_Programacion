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

    /*public Coordenada mover(Tablero tablero) {
        for (int i = 0; i < 2; i++) {
            Coordenada siguientePosicion = moverHaciaJugador(tablero);
            setPosicion(siguientePosicion);
        }
        return getPosicion();
    }*/
    public Coordenada mover(Tablero tablero) {
        Coordenada siguientePosicion = getPosicion();
        for (int i = 0; i < 2; i++) {
            siguientePosicion = moverHaciaJugador(tablero);
            setPosicion(siguientePosicion);
            if (tablero.getCelda(siguientePosicion).getEstado() == Celda.Estado.INCENDIADA) {
                // Marcar el robot para eliminación o devolver null para indicar que debe ser eliminado
                return null;
            }
        }
        return siguientePosicion;
    }

}

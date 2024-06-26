package org.example.modelo;

public class Robot2x extends Robot {

    public Robot2x(Coordenada posicion) {
        super(posicion);
    }

    public String getTipo() {
        return "Robot2x";
    }

    /*Mueve el robot hacia la posicion del jugador en el tablero.
     Este robot se mueve dos pasos hacia el jugador cada vez que se invoca este metodo.
     El tablero de juego donde se encuentra el robot,
     retorna la nueva posicion del robot despues de moverse hacia el jugador
     o null si la celda esta incendiada.*/
    public Coordenada mover(Tablero tablero) {
        Coordenada siguientePosicion = getPosicion();
        for (int i = 0; i < 2; i++) {
            siguientePosicion = moverHaciaJugador(tablero);
            setPosicion(siguientePosicion);
            if (tablero.getCelda(siguientePosicion).getEstado() == Celda.Estado.INCENDIADA) {
                return null;
            }
        }
        return siguientePosicion;
    }

}

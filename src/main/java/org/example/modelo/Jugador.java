package org.example.modelo;

import java.util.Map;

public class Jugador {
    private Coordenada posicion;
    private int teletransportacionesDisponibles;
    public Jugador(Coordenada posicion) {
        this.posicion = posicion;
        this.teletransportacionesDisponibles = 1;
    }
    /*Mueve al jugador en la direccion especificada
     si la nueva posicion es válida.*/
    public void mover(Tablero tablero, Direccion direccion) {
        Coordenada movimiento = direccion.getMovimiento();
        Coordenada posicionActual = getPosicion();
        int nuevaFila = posicionActual.getFila() + movimiento.getFila();
        int nuevaColumna = posicionActual.getColumna() + movimiento.getColumna();
        Coordenada nuevaPosicion = new Coordenada(nuevaFila, nuevaColumna);

        if (tablero.esCeldaValida(nuevaPosicion)) {
            setPosicion(nuevaPosicion);
        }
    }
    /*Teletransporta al jugador a una posicion aleatoria válida en el tablero.*/
    public void teletransportarse(Tablero tablero) {
        int nuevaFila, nuevaColumna;
        Coordenada nuevaPosicion;

        do {
            nuevaFila = (int) (Math.random() * tablero.getFilas());
            nuevaColumna = (int) (Math.random() * tablero.getColumnas());
            nuevaPosicion = new Coordenada(nuevaFila, nuevaColumna);
        } while (!tablero.esCeldaValida(nuevaPosicion));

        setPosicion(nuevaPosicion);
    }
    /*Teletransporta al jugador a una posicion especifica si es valida,
    está libre y quedan teletransportaciones disponibles.*/
    public void teletransportarseSeguro(Tablero tablero, Coordenada destino) {
        if (teletransportacionesDisponibles > 0 && tablero.esCeldaValida(destino) && tablero.getCelda(destino).isLibre()) {
            setPosicion(destino);
            teletransportacionesDisponibles--;

        }
    }
    /*Devuelve la posicion actual del jugador.*/
    public Coordenada getPosicion() {
        return posicion;
    }
    /*Establece una nueva posicion para el jugador.*/
    public void setPosicion(Coordenada posicion) {
        this.posicion = posicion;
    }
    /*Devuelve el numero de teletransportaciones disponibles.*/
    public int getTeletransportacionesDisponibles() {
        return teletransportacionesDisponibles;
    }
    /*Establece el numero de teletransportaciones disponibles para el jugador.*/
    public void setTeletransportacionesDisponibles(int teletransportacionesDisponibles) {
        this.teletransportacionesDisponibles = teletransportacionesDisponibles;
    }
}

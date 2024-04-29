package org.example.modelo;

public class Jugador {
    private Coordenada posicion;
    private int teletransportacionesDisponibles;

    public Jugador(Coordenada posicion) {
        this.posicion = posicion;
        this.teletransportacionesDisponibles = 1;
    }

    /*Mueve al jugador en la dirección especificada
     si la nueva posición es válida.*/
    public void mover(Tablero tablero, Direccion direccion) {
        Coordenada nuevaPosicion = new Coordenada(this.posicion.getFila(), this.posicion.getColumna());

        switch (direccion) {

            case ARRIBA:
                nuevaPosicion.setFila(nuevaPosicion.getFila() - 1);
                break;
            case ABAJO:
                nuevaPosicion.setFila(nuevaPosicion.getFila() + 1);
                break;
            case IZQUIERDA:
                nuevaPosicion.setColumna(nuevaPosicion.getColumna() - 1);
                break;
            case DERECHA:
                nuevaPosicion.setColumna(nuevaPosicion.getColumna() + 1);
                break;
            case ARRIBA_IZQUIERDA:
                nuevaPosicion.setFila(nuevaPosicion.getFila() - 1);
                nuevaPosicion.setColumna(nuevaPosicion.getColumna() - 1);
                break;
            case ARRIBA_DERECHA:
                nuevaPosicion.setFila(nuevaPosicion.getFila() - 1);
                nuevaPosicion.setColumna(nuevaPosicion.getColumna() + 1);
                break;
            case ABAJO_IZQUIERDA:
                nuevaPosicion.setFila(nuevaPosicion.getFila() + 1);
                nuevaPosicion.setColumna(nuevaPosicion.getColumna() - 1);
                break;
            case ABAJO_DERECHA:
                nuevaPosicion.setFila(nuevaPosicion.getFila() + 1);
                nuevaPosicion.setColumna(nuevaPosicion.getColumna() + 1);
                break;
            case PERMANECER:
                break;
        }

        if (tablero.esCeldaValida(nuevaPosicion)) {
            setPosicion(nuevaPosicion);
        }

    }

    /*Teletransporta al jugador a una posición aleatoria válida en el tablero.*/
    public void teletransportarse(Tablero tablero) {
        int nuevaFila = (int) (Math.random() * tablero.getFilas());
        int nuevaColumna = (int) (Math.random() * tablero.getColumnas());

        Coordenada nuevaPosicion = new Coordenada(nuevaFila, nuevaColumna);

        while (!tablero.esCeldaValida(nuevaPosicion)) {
            nuevaFila = (int) (Math.random() * tablero.getFilas());
            nuevaColumna = (int) (Math.random() * tablero.getColumnas());
            nuevaPosicion = new Coordenada(nuevaFila, nuevaColumna);
        }

        setPosicion(nuevaPosicion);

    }

    /*Teletransporta al jugador a una posición específica si es válida,
    está libre y quedan teletransportaciones disponibles.*/
    public void teletransportarseSeguro(Tablero tablero, Coordenada destino) {
        if (teletransportacionesDisponibles > 0 && tablero.esCeldaValida(destino) && tablero.getCelda(destino).isLibre()) {
            setPosicion(destino);
            teletransportacionesDisponibles--;

        }
    }

    /*Devuelve la posición actual del jugador.*/
    public Coordenada getPosicion() {
        return posicion;
    }

    /*Establece una nueva posición para el jugador.*/
    public void setPosicion(Coordenada posicion) {
        this.posicion = posicion;
    }

    /*Devuelve el número de teletransportaciones disponibles.*/
    public int getTeletransportacionesDisponibles() {
        return teletransportacionesDisponibles;
    }

    /*Establece el número de teletransportaciones disponibles para el jugador.*/
    public void setTeletransportacionesDisponibles(int teletransportacionesDisponibles) {
        this.teletransportacionesDisponibles = teletransportacionesDisponibles;
    }
}

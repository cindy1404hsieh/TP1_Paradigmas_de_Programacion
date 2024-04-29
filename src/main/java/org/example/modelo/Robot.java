package org.example.modelo;

public abstract class Robot {

    private Coordenada posicion;

    public Robot(Coordenada posicion) {
        this.posicion = posicion;
    }

    /*es implementado por las subclases
    para definir cómo se mueve el robot en el tablero.*/
    public abstract Coordenada mover(Tablero tablero);

    /*mueve el robot hacia la posicion del jugador en el tablero,
    avanzando un numero especificado de pasos.
    calcula la direccion de movimiento comparando la posicion del jugador
    con la del robot y ajusta la posicion del robot en consecuencia.*/
    public Coordenada moverHaciaJugador(Tablero tablero) {
        Jugador jugador = tablero.getJugador();
        Coordenada posicionJugador = jugador.getPosicion();
        Coordenada posicionRobot = getPosicion();
        //esto devuelve -1, 0, o 1 dependiendo de si la primera coordenada es menor, igual o mayor que la segunda
        int direccionFila = Integer.compare(posicionJugador.getFila(), posicionRobot.getFila());
        int direccionColumna = Integer.compare(posicionJugador.getColumna(), posicionRobot.getColumna());

        int nuevaFila = posicionRobot.getFila() + direccionFila;
        int nuevaColumna = posicionRobot.getColumna() + direccionColumna;

        return new Coordenada(nuevaFila, nuevaColumna);
    }

    public abstract String getTipo();

    /*devuelve la posicion actual del robot.*/
    public Coordenada getPosicion() {
        return posicion;
    }

    public void setPosicion(Coordenada nuevaPosicion) {
        posicion = nuevaPosicion;
    }


}



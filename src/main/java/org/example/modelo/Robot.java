package org.example.modelo;

public abstract class Robot {

    private Coordenada posicion;

    public Robot(Coordenada posicion) {
        this.posicion = posicion;
    }

    /*es implementado por las subclases
    para definir cómo se mueve el robot en el tablero.*/
    public abstract Coordenada mover(Tablero tablero);

    /*Mueve el robot hacia la posición del jugador en el tablero,
    avanzando un número especificado de pasos.
    Calcula la dirección de movimiento comparando la posición del jugador
    con la del robot y ajusta la posición del robot en consecuencia.*/
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

    /*Devuelve la posición actual del robot.*/
    public Coordenada getPosicion() {
        return posicion;
    }

    public void setPosicion(Coordenada nuevaPosicion) {
        posicion = nuevaPosicion;
    }

    public abstract String getTipo();

}



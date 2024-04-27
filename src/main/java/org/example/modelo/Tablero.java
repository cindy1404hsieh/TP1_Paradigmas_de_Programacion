package org.example.modelo;

import java.util.*;

public class Tablero {
    private Celda[][] celdas;
    private final List<Robot> robots;
    private List<Celda> celdasIncendiadas;
    private final Jugador jugador;
    private int nivelActual;


    public Tablero(Coordenada dimensiones, int nivelInicial) {
        this.celdas = new Celda[dimensiones.getFila()][dimensiones.getColumna()];
        this.robots = new ArrayList<>();
        int filaJugador = dimensiones.getFila() / 2;
        int columnaJugador = dimensiones.getColumna() / 2;
        Coordenada coordenadaJugador = new Coordenada(filaJugador, columnaJugador);
        this.jugador = new Jugador(coordenadaJugador);
        celdasIncendiadas = new ArrayList<>();
        this.nivelActual = nivelInicial;
        inicializar(nivelInicial);
    }

    /**
     * cambia el tamaño del tablero y vuelve a cargar a los enemigos desde el nivel actual.
     */
    public void cambiarTamanio(Coordenada dimensiones) {
        celdas = new Celda[dimensiones.getFila()][dimensiones.getColumna()];
        inicializar(nivelActual);
    }

    /*Incrementa el nivel del juego
    y reinicializa el tablero y los robots para el nuevo nivel.*/
    public void siguienteNivel() {
        nivelActual++;
        celdasIncendiadas.clear();
        inicializar(nivelActual);
        int teletransportacionesDisponibles = jugador.getTeletransportacionesDisponibles();
        jugador.setTeletransportacionesDisponibles(teletransportacionesDisponibles+1);

    }

    /* Prepara el tablero para el juego al establecer todas las celdas a LIBRE
    y coloca robots en posiciones aleatorias.*/
    public void inicializar(int nivel) {
        robots.clear();
        celdasIncendiadas.clear();
        nivelActual = nivel;

        for (int fila = 0; fila < celdas.length; fila++) {
            for (int columna = 0; columna < celdas[fila].length; columna++) {
                celdas[fila][columna] = new Celda(Celda.Estado.LIBRE, new Coordenada(fila, columna));
            }
        }
        int filaCentral = celdas.length / 2;
        int columnaCentral = celdas[0].length / 2;
        Coordenada posicionCentral = new Coordenada(filaCentral, columnaCentral);
        jugador.setPosicion(posicionCentral);
        inicializarRobots(nivel);
    }

    /*Coloca una cantidad de robots que depende del nivel del juego
    en posiciones aleatorias en el tablero.*/
    private void inicializarRobots(int nivel) {
        int baseRobots = Math.max(2, (celdas.length * celdas[0].length) / 3);//un tercio del total de celdas en el tablero
        int cantidadRobots = (int) (Math.random() * baseRobots * nivel / 10 + 2); // a medida que el nivel aumenta, la cantidad de robots tambien aumenta de manera controlada
        Set<Coordenada> posicionesOcupadas = new HashSet<>();
        posicionesOcupadas.add(jugador.getPosicion());
        for (int i = 0; i < cantidadRobots; i++) {
            Coordenada coordenadaRobot;
            do {
                coordenadaRobot = generarCoordenadaAleatoria();
            } while (posicionesOcupadas.contains(coordenadaRobot));

            posicionesOcupadas.add(coordenadaRobot);
            //asegura que aproximadamente genera mitad y mitad de dos tipos de robot
            if (Math.random() < 0.5) {
                robots.add(new Robot1x(coordenadaRobot));
            } else {
                robots.add(new Robot2x(coordenadaRobot));
            }

            setCeldaEstado(coordenadaRobot, Celda.Estado.OCUPADA);
        }
    }

    /*Genera una coordenada aleatoria en el tablero
    que no sea la posicion actual del jugador.*/
    private Coordenada generarCoordenadaAleatoria() {
        int fila, columna;
        do {
            fila = (int) (Math.random() * celdas.length);
            columna = (int) (Math.random() * celdas[0].length);
        } while (fila == jugador.getPosicion().getFila() && columna == jugador.getPosicion().getColumna());
        return new Coordenada(fila, columna);
    }

    /*mueve cada robot en el tablero, actualiza las celdas de LIBRE a OCUPADA
    segun la nueva posicion valida del robot y ajusta la posicion del robot..*/
    public void moverRobots() {
        Iterator<Robot> iterator = robots.iterator();
        while (iterator.hasNext()) {
            Robot robot = iterator.next();
            setCeldaEstado(robot.getPosicion(), Celda.Estado.LIBRE);
            Coordenada nuevaPosicion = robot.mover(this);
            if (nuevaPosicion == null) {
                iterator.remove();
            } else if (esCeldaValida(nuevaPosicion)) {
                robot.setPosicion(nuevaPosicion);
                setCeldaEstado(robot.getPosicion(), Celda.Estado.OCUPADA);
            }else {
                iterator.remove();
            }
        }
        verificarColisiones();
        actualizarCeldasIncendiadas();
    }

    /*verifica si el jugador ha perdido el juego
    al pisar una celda incendiada o ser alcanzado por un robot.*/
    public boolean jugadorSigueVivo() {
        boolean jugadorEstaVivo = true;
        Celda celdaJugador = getCelda(jugador.getPosicion());
        if (celdaJugador != null && celdaJugador.isIncendiada()) {
            //El jugador ha perdido por pisar una celda incendiada
            jugadorEstaVivo = false;
        }
        for (Robot robot : robots) {
            if (getCelda(robot.getPosicion()) == celdaJugador) {
                jugadorEstaVivo = false;
                break;
            }
        }
        return jugadorEstaVivo;
    }

    public List<Celda> getCeldasIncendiadas() {
        return celdasIncendiadas;
    }

    /*se encarga de actualizar el estado de las celdas incendiadas
    y de eliminar los robots que pisen estas celdas.*/
    public void actualizarCeldasIncendiadas() {
        for (Celda incendiada : celdasIncendiadas) {
            eliminarRobotsEnCelda(incendiada);
        }
    }

    /*verifica si hay colisiones entre robots que ocupan la misma posicion en el tablero.
    Si se detecta una colision en una posicion,
    esa celda se incendia y todos los robots en esa posicion son eliminados.*/
    public void verificarColisiones() {
        Map<Celda, List<Robot>> posiciones = new HashMap<>();
        for (Robot robot : robots) {
            posiciones.computeIfAbsent(getCelda(robot.getPosicion()), k -> new ArrayList<>()).add(robot);
        }

        for (Map.Entry<Celda, List<Robot>> entrada : posiciones.entrySet()) {
            if (entrada.getValue().size() > 1) {
                incendiarCelda(entrada.getKey());
                eliminarRobotsEnCelda(entrada.getKey());
            }
        }
    }

    public void incendiarCelda(Celda celda) {
        if (esCeldaValida(celda.getPosicion())) {
            celda.setEstado(Celda.Estado.INCENDIADA);
            celdasIncendiadas.add(celda);
        }
    }

    /*Elimina todos los robots que se encuentran en la coordenada especificada.*/
    private void eliminarRobotsEnCelda(Celda celda) {
        robots.removeIf(robot -> getCelda(robot.getPosicion()).equals(celda));
    }

    public void setCeldaEstado(Coordenada coordenada, Celda.Estado estado) {
        if (esCeldaValida(coordenada)) {
            getCelda(coordenada).setEstado(estado);
        }
    }

    /*Verifica si una coordenada estadentro de los limites del tablero.*/
    public boolean esCeldaValida(Coordenada coordenada) {
        return coordenada.getFila() >= 0 && coordenada.getFila() < celdas.length && coordenada.getColumna() >= 0 && coordenada.getColumna() < celdas[0].length;
    }
/*    public boolean esCeldaLibre(Coordenada coordenada) {
        Celda celda = getCelda(coordenada);
        return celda != null && celda.getEstado() == Celda.Estado.LIBRE;
    }*/
    /*Devuelve la celda en la coordenada especificada si es válida.*/
    public Celda getCelda(Coordenada coordenada) {
        if (esCeldaValida(coordenada)) {
            return celdas[coordenada.getFila()][coordenada.getColumna()];
        } else {
            return null;
        }
    }

    /*Devuelven las dimensiones del tablero.*/
    public int getFilas() {
        return celdas.length;
    }
    /*Devuelven las dimensiones del tablero.*/
    public int getColumnas() {
        return celdas[0].length;
    }
    /*Devuelve el objeto Jugador asociado con el tablero.*/
    public Jugador getJugador() {
        return jugador;
    }
    /*Devuelve la lista de robots en el tablero.*/
    public List<Robot> getRobots() {
        return robots;
    }
    /*Devuelve el nivel actual del juego.*/
    public int getNivelActual() {
        return nivelActual;
    }

}
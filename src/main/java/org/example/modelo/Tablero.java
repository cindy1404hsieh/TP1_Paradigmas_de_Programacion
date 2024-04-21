package org.example.modelo;

import java.util.*;

public class Tablero {
    private final Celda[][] celdas;
    private final List<Robot> robots;
    private List<Coordenada> celdasIncendiadas;
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
        this.nivelActual = nivelInicial; // Inicializa el nivel con el valor proporcionado
        inicializar(nivelInicial); // Llama a inicializar pasando el nivel inicial
    }
    /*Incrementa el nivel del juego
    y reinicializa el tablero y los robots para el nuevo nivel.*/
    public void siguienteNivel() {
        nivelActual++;
        inicializar(nivelActual);
        int teletransportacionesDisponibles = jugador.getTeletransportacionesDisponibles();
        jugador.setTeletransportacionesDisponibles(teletransportacionesDisponibles+1);
    }
    /* Prepara el tablero para el juego al establecer todas las celdas a LIBRE
    y coloca robots en posiciones aleatorias.*/
    public void inicializar(int nivel) {
        for (int fila = 0; fila < celdas.length; fila++) {
            for (int columna = 0; columna < celdas[fila].length; columna++) {
                if (celdas[fila][columna] == null) {
                    celdas[fila][columna] = new Celda(Celda.Estado.LIBRE, new Coordenada(fila, columna));
                }
            }
        }
        inicializarRobots(nivel);
    }
    /*Coloca una cantidad de robots que depende del nivel del juego
    en posiciones aleatorias en el tablero.*/
    private void inicializarRobots(int nivel) {
        int baseRobots = Math.max(2, (celdas.length * celdas[0].length) / 3);//un tercio del total de celdas en el tablero
        int cantidadRobots = (int) (Math.random() * baseRobots * nivel / 10 + 2); // a medida que el nivel aumenta, la cantidad de robots también aumenta de manera controlada

        for (int i = 0; i < cantidadRobots; i++) {
            Coordenada coordenadaRobot = generarCoordenadaAleatoria();

            while (!esCeldaValida(coordenadaRobot) || jugador.getPosicion().equals(coordenadaRobot)) {
                coordenadaRobot = generarCoordenadaAleatoria();
            }
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
    que no sea la posición actual del jugador.*/
    private Coordenada generarCoordenadaAleatoria() {
        int fila, columna;
        do {
            fila = (int) (Math.random() * celdas.length);
            columna = (int) (Math.random() * celdas[0].length);
        } while (fila == jugador.getPosicion().getFila() && columna == jugador.getPosicion().getColumna());
        return new Coordenada(fila, columna);
    }
    /*mueve cada robot en el tablero, actualiza las celdas de LIBRE a OCUPADA
    según la nueva posición válida del robot y ajusta la posición del robot..*/
    public void moverRobots() {
        // Primero, marca todas las celdas como libres antes de mover los robots
        for (Robot robot : robots) {
            setCeldaEstado(robot.getPosicion(), Celda.Estado.LIBRE);
        }

        // Mueve cada robot y actualiza las celdas a ocupadas
        for (Robot robot : robots) {
            Coordenada posicionActual = robot.getPosicion();
            Coordenada nuevaPosicion = robot.mover(this);
            if (esCeldaValida(nuevaPosicion)) {
                robot.setPosicion(nuevaPosicion);
            }
            setCeldaEstado(robot.getPosicion(), Celda.Estado.OCUPADA);
        }

        // Verifica colisiones después de mover todos los robots
        verificarColisiones();
    }
    /*verifica si el jugador ha perdido el juego
    al pisar una celda incendiada o ser alcanzado por un robot.*/
    public boolean jugadorSigueVivo() {
        boolean jugadorEstaVivo = true;
        // Verifica si el jugador pisa una celda incendiada
        Celda celdaJugador = getCelda(jugador.getPosicion());
        if (celdaJugador != null && celdaJugador.isIncendiada()) {
            //El jugador ha perdido por pisar una celda incendiada
            jugadorEstaVivo = false;
        }

        // Verifica si algún robot ha alcanzado al jugador
        for (Robot robot : robots) {
            if (robot.getPosicion().equals(jugador.getPosicion())) {
                //El jugador ha perdido por ser alcanzado por un robot.
                jugadorEstaVivo = false;
            }
        }
        return jugadorEstaVivo;
    }

    public List<Coordenada> getCeldasIncendiadas() {
        return celdasIncendiadas;
    }
    /*se encarga de actualizar el estado de las celdas incendiadas
    y de eliminar los robots que pisen estas celdas.*/
    public void actualizarCeldasIncendiadas() {
        for (Coordenada incendiada : celdasIncendiadas) {
            eliminarRobotsEnCoordenada(incendiada);
        }
    }
    /*verifica si hay colisiones entre robots que ocupan la misma posición en el tablero.
    Si se detecta una colisión en una posición,
    esa celda se incendia y todos los robots en esa posición son eliminados.*/
    public boolean verificarColisiones() {
        Map<Coordenada, List<Robot>> posiciones = new HashMap<>();
        for (Robot robot : robots) {
            posiciones.computeIfAbsent(robot.getPosicion(), k -> new ArrayList<>()).add(robot);
        }

        boolean huboColision = false;
        for (Map.Entry<Coordenada, List<Robot>> entrada : posiciones.entrySet()) {
            if (entrada.getValue().size() > 1) { // Más de un robot en la misma celda
                incendiarCelda(entrada.getKey());
                eliminarRobotsEnCoordenada(entrada.getKey());
                huboColision = true;
            }
        }
        return huboColision;
    }
    public void incendiarCelda(Coordenada coordenada) {
        if (esCeldaValida(coordenada)) {
            setCeldaEstado(coordenada, Celda.Estado.INCENDIADA);
            celdasIncendiadas.add(coordenada);
        }
    }
    /*Elimina todos los robots que se encuentran en la coordenada especificada.*/
    private void eliminarRobotsEnCoordenada(Coordenada coordenada) {
        robots.removeIf(robot -> robot.getPosicion().equals(coordenada));
    }
    public void setCeldaEstado(Coordenada coordenada, Celda.Estado estado) {
        if (esCeldaValida(coordenada)) {
            getCelda(coordenada).setEstado(estado);
        }
    }
    /*Verifica si una coordenada está dentro de los límites del tablero.*/
    public boolean esCeldaValida(Coordenada coordenada) {
        return coordenada.getFila() >= 0 && coordenada.getFila() < celdas.length && coordenada.getColumna() >= 0 && coordenada.getColumna() < celdas[0].length;
    }
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
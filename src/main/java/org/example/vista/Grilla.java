package org.example.vista;

import javafx.scene.input.MouseEvent;
import javafx.scene.layout.TilePane;
import javafx.scene.paint.Color;
import org.example.modelo.*;

public class Grilla extends TilePane {
    public static final int LADO_CASILLA = 20;
    private final Tablero tablero;
    private Casilla[][] casillas;
    private Casilla casillaResaltada;
    private boolean modoEspera;
    private boolean modoTeleport;
    private Coordenada siguienteDireccion;

    public Grilla(Tablero tablero) {
        super();
        this.tablero = tablero;
        this.modoEspera = false;
        this.modoTeleport = false;

        super.addEventHandler(CeldaSeleccionadaEvent.CELDA_SELECCIONADA, celdaSeleccionadaEvent -> {
            if (modoTeleport) {
                Coordenada destino = new Coordenada(celdaSeleccionadaEvent.getI(), celdaSeleccionadaEvent.getJ());
                tablero.getJugador().teletransportarseSeguro(tablero, destino);
                modoTeleport = false;
                update();
                celdaSeleccionadaEvent.consume();
            }
        });
        super.addEventHandler(SafeTeleportEvent.SAFE_TELEPORT, e -> {
            modoTeleport = true;
            e.consume();
        });
        super.setOnMouseClicked(mouseEvent -> {
            if (!modoTeleport) {
                aplicarAccion(new ActionMoverACelda(siguienteDireccion));
            }
            siguienteDireccion(mouseEvent);
            if (modoTeleport) {
                mouseEvent.consume();
            }
        });
        super.addEventHandler(JugadorMurioEvent.JUGADOR_MURIO, e -> {
            modoEspera = true;
        });
        super.setOnMouseMoved(this::siguienteDireccion);
        super.addEventHandler(ReiniciarJuegoEvent.RESET, reiniciarJuegoEvent -> {
            modoEspera = false;
            clear();
            super.getChildren().clear();
            tablero.inicializar(1);
            dibujarGrilla();
            dibujarEntidades();
        });

        dibujarGrilla();
        dibujarEntidades();
    }

    /**
     * Crea una grilla del tamaño del tablero.
     */
    public void dibujarGrilla() {
        this.casillas = new Casilla[tablero.getFilas()][tablero.getColumnas()];
        int columnas = tablero.getColumnas();
        int filas = tablero.getFilas();
        super.setPrefColumns(columnas);
        super.setPrefRows(filas);
        super.setMaxSize(columnas * LADO_CASILLA, filas * LADO_CASILLA);

        boolean colorGrilla = true;
        for (int i = 0; i < filas; i++) {
            // si la cantidad de columnas es impar, una fila empieza y termina con el mismo color. En ese caso actualizo el valor de colorGrilla
            colorGrilla = (columnas % 2 != 0) == colorGrilla;
            for (int j = 0; j < columnas; j++) {
                Casilla nuevaCasilla = getCasilla(colorGrilla, i, j);
                casillas[i][j] = nuevaCasilla;
                super.getChildren().add(nuevaCasilla);
                colorGrilla = !colorGrilla;

            }
        }

    }

    /**
     * Crea una nueva casilla con su listener implementado.
     * @param colorGrilla indica el color que debe tener la casilla.
     * @param i fila de la nueva casilla
     * @param j columna de la nueva casilla
     */
    private Casilla getCasilla(boolean colorGrilla, int i, int j) {
        Color color = colorGrilla ? Color.LIGHTSTEELBLUE : Color.GAINSBORO;
        Casilla nuevaCasilla = new Casilla(color);

        nuevaCasilla.setOnMouseClicked(mouseEvent -> {
            if (modoTeleport) {
                this.fireEvent(new CeldaSeleccionadaEvent(i, j));
                modoTeleport = false;
                mouseEvent.consume();
            }
        });
        return nuevaCasilla;
    }

    /**
     * aplica la accion que recibe y vuelve a cargar las entidades.
     */
    public void aplicarAccion(Action action) {
        if (modoEspera) return;
        action.apply(tablero);
        update();

    }

    /**
     * mueve los robots y vuelve a cargar las entidades.
     */
    public void update() {
        if (modoEspera) return;
        tablero.moverRobots();
        clear();
        dibujarEntidades();
    }

    /**
     * Añade las imagenes necesarias a la grilla.
     */
    public void dibujarEntidades() {
        Casilla casillaJugador = obtenerCasilla(tablero.getJugador().getPosicion());
        casillaJugador.agregarEntidad("jugador");

        for (Robot robot : tablero.getRobots()) {
            Casilla casillaRobot = obtenerCasilla(robot.getPosicion());
            casillaRobot.agregarEntidad(robot.getTipo());
        }

        for (Celda celda : tablero.getCeldasIncendiadas()) {
            Casilla casilla = obtenerCasilla(celda.getPosicion());
            casilla.agregarEntidad("explosion");

        }
    }

    /**
     * Borra todas las entidades de la grilla.
     */
    private void clear() {
        for (Casilla[] filaCasilla : casillas) {
            for (Casilla casilla : filaCasilla) {
                casilla.eliminarEntidad();
            }
        }
    }

    /**
     * Devuelve la casilla que refieren las coordenadas.
     */
    private Casilla obtenerCasilla(Coordenada coordenada) {
        int i = coordenada.getFila();
        int j = coordenada.getColumna();
        return casillas[i][j];
    }

    /**
     * Guarda la direccion del movimiento del jugador segun la posicion del mouse.
     * Resalta el casillero al cual el jugador se movera en caso de hacer click con el mouse.
     */
    private void siguienteDireccion(MouseEvent mouseEvent) {
        if (modoTeleport) {
            mouseEvent.consume();
            return;
        }
        if (casillaResaltada != null) casillaResaltada.desResaltar();

        Coordenada deltaCoordenada = getDeltaCoordenada(mouseEvent);
        siguienteDireccion = sumarCoordenadas(tablero.getJugador().getPosicion(), deltaCoordenada);
        casillaResaltada = obtenerCasilla(siguienteDireccion);
        casillaResaltada.resaltar();

    }

    /**
     * Devuelve la direccion del mouse respecto al jugador.
     */
    private Coordenada getDeltaCoordenada(MouseEvent mouseEvent) {
        int playerX = tablero.getJugador().getPosicion().getColumna();
        int playerY = tablero.getJugador().getPosicion().getFila();

        int mouseX = (int) mouseEvent.getX();
        int mouseY = (int) mouseEvent.getY();

        int gridX = mouseX / LADO_CASILLA;
        int gridY = mouseY / LADO_CASILLA;

        if (gridX >= tablero.getColumnas()) gridX = tablero.getColumnas() - 1;
        if (gridY >= tablero.getFilas()) gridY = tablero.getFilas() - 1;

        int deltaX = gridX - playerX;
        int deltaY = gridY - playerY;

        return new Coordenada((int)Math.signum(deltaY), (int)Math.signum(deltaX));
    }

    /**
     * @param c1 (a, b)
     * @param c2 (c, d)
     * @return Coordenada(a + c, b + d)
     */
    private Coordenada sumarCoordenadas(Coordenada c1, Coordenada c2) {
        int nuevaFila = c1.getFila() + c2.getFila();
        int nuevaColumna = c1.getColumna() + c2.getColumna();

        return new Coordenada(nuevaFila, nuevaColumna);
    }

}


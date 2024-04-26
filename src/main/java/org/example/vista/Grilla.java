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
    private Direccion siguienteDireccion;

    public Grilla(Tablero tablero) {
        super();
        this.tablero = tablero;
        this.modoEspera = false;
        this.modoTeleport = false;

        super.addEventHandler(CeldaSeleccionadaEvent.CELDA_SELECCIONADA, celdaSeleccionadaEvent -> {
            if (modoTeleport) {
                Coordenada destino = new Coordenada(celdaSeleccionadaEvent.getI(), celdaSeleccionadaEvent.getJ());
                tablero.getJugador().teletransportarseSeguro(tablero, destino);
                modoTeleport = false; // Resetear el estado aquí
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
                aplicarAccion(new ActionMover(siguienteDireccion));
            }
            siguienteDireccion(mouseEvent);
            // Consumir el evento para evitar que se propague si estamos en modo teleport
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
    public void dibujarGrilla(){
        this.casillas = new Casilla[tablero.getFilas()][tablero.getColumnas()];
        int columnas = tablero.getColumnas();
        int filas = tablero.getFilas();
        super.setPrefColumns(columnas);
        super.setPrefRows(filas);

        boolean colorGrilla = true;
        super.setMaxSize(columnas*LADO_CASILLA, filas*LADO_CASILLA);

        for (int i = 0; i < filas; i++) {
            // si la cantidad de columnas es impar, una fila empieza y termina con el mismo color. En ese caso actualizo el valor de colorGrilla
            colorGrilla = (columnas % 2 != 0) == colorGrilla;
            for (int j = 0; j < columnas; j++) {
                Color color = (colorGrilla) ? Color.LIGHTBLUE : Color.BLUE;
                Casilla nuevaCasilla = new Casilla(color);
                int tmpI = i;
                int tmpJ = j;
                nuevaCasilla.setOnMouseClicked(mouseEvent -> {
                    if (modoTeleport) {
                        this.fireEvent(new CeldaSeleccionadaEvent(tmpI, tmpJ));
                        modoTeleport = false; // Resetear el estado aquí\
                        mouseEvent.consume();
                    }
                });

                casillas[i][j] = nuevaCasilla;
                super.getChildren().add(nuevaCasilla);
                colorGrilla = !colorGrilla;

            }
        }

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
     * Calcula la direccion del movimiento del jugador segun la posicion del mouse.
     * Resalta el casillero al cual el jugador se moverá en caso de hacer click con el mouse.
     */
    private void siguienteDireccion(MouseEvent mouseEvent) {
        if (modoTeleport) {
            mouseEvent.consume();
            return; // No calcular dirección si está en modo teletransporte
        }
        if (casillaResaltada != null) casillaResaltada.desResaltar();
        int playerX = tablero.getJugador().getPosicion().getColumna();
        int playerY = tablero.getJugador().getPosicion().getFila();

        int mouseX = (int) mouseEvent.getX();
        int mouseY = (int) mouseEvent.getY();

        int gridX = mouseX / LADO_CASILLA;
        int gridY = mouseY / LADO_CASILLA;

        int deltaX = gridX - playerX;
        int deltaY = gridY - playerY;
        Coordenada siguiente = new Coordenada(playerY, playerY);

        if (deltaX != 0 && deltaY != 0 && Math.abs(deltaX) == Math.abs(deltaY)) {
            // Movimiento diagonal
            if (deltaX > 0 && deltaY > 0) {
                siguiente.setFila(playerY+1); siguiente.setColumna(playerX+1);
                siguienteDireccion = Direccion.ABAJO_DERECHA;
            } else if (deltaX > 0) {
                siguiente.setFila(playerY-1); siguiente.setColumna(playerX+1);
                siguienteDireccion = Direccion.ARRIBA_DERECHA;
            } else if (deltaY > 0) {
                siguiente.setFila(playerY+1); siguiente.setColumna(playerX-1);
                siguienteDireccion = Direccion.ABAJO_IZQUIERDA;
            } else {
                siguiente.setFila(playerY-1); siguiente.setColumna(playerX-1);
                siguienteDireccion = Direccion.ARRIBA_IZQUIERDA;
            }
        } else if (Math.abs(deltaX) > Math.abs(deltaY)) {
            // Movimiento horizontal
            if (deltaX > 0) {
                siguiente.setFila(playerY); siguiente.setColumna(playerX+1);
                siguienteDireccion = Direccion.DERECHA;
            } else {
                siguiente.setFila(playerY); siguiente.setColumna(playerX-1);
                siguienteDireccion = Direccion.IZQUIERDA;
            }
        } else if (Math.abs(deltaY) > Math.abs(deltaX)) {
            // Movimiento vertical
            if (deltaY > 0) {
                siguiente.setFila(playerY+1); siguiente.setColumna(playerX);
                siguienteDireccion = Direccion.ABAJO;
            } else {
                siguiente.setFila(playerY-1); siguiente.setColumna(playerX);
                siguienteDireccion = Direccion.ARRIBA;
            }
        } else {
            siguiente = tablero.getJugador().getPosicion();
            siguienteDireccion = Direccion.PERMANECER;
        }
        casillaResaltada = obtenerCasilla(siguiente);
        casillaResaltada.resaltar();

    }

}


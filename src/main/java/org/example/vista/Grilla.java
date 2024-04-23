package org.example.vista;

import javafx.scene.input.MouseEvent;
import javafx.scene.layout.TilePane;
import javafx.scene.paint.Color;
import org.example.modelo.*;

public class Grilla extends TilePane {
    public static final int LADO_CASILLA = 20;
    private final Tablero tablero;
    private final Casilla[][] casillas;
    private Coordenada casillaSeleccionada;
    private Casilla casillaResaltada;
    private boolean modoEspera;
    private Direccion siguienteDireccion;

    public Grilla(Tablero tablero) {
        super();
        this.tablero = tablero;
        this.casillas = new Casilla[tablero.getFilas()][tablero.getColumnas()];
        this.modoEspera = false;

        super.addEventHandler(SafeTeleportEvent.SAFE_TELEPORT, e -> {
            modoEspera = true;
        });
        super.setOnMouseClicked(mouseEvent -> {
            update(new ActionMover(siguienteDireccion));
            siguienteDireccion(mouseEvent);
        });
        super.addEventHandler(CeldaSeleccionadaEvent.CELDA_SELECCIONADA, e -> {
            this.update(new ActionSafeTeleport(casillaSeleccionada));
        });
        super.setOnMouseMoved(this::siguienteDireccion);

        dibujarGrilla();
        dibujarEntidades();
    }

    /**
     * Crea una grilla del tamaño del tablero.
     */
    private void dibujarGrilla(){
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
                    if (modoEspera) {
                        modoEspera = false;
                        this.casillaSeleccionada = new Coordenada(tmpI, tmpJ);
                        this.fireEvent(new CeldaSeleccionadaEvent());
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
    public void update(Action action) {
        if (modoEspera) {
            System.out.println("no");
            return;
        }
        action.apply(tablero);
        clear();
        dibujarEntidades();

    }

    /**
     * Añade las imagenes necesarias a la grilla.
     */
    private void dibujarEntidades() {

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
        for (int i = 0; i < casillas.length; i++) {
            for (int j = 0; j < casillas[i].length; j++) {
                casillas[i][j].eliminarEntidad();
            }
        }
    }

    /**
     * Devuelve la casilla que refieren las coordenadas.
     */
    public Casilla obtenerCasilla(Coordenada coordenada) {
        int i = coordenada.getFila();
        int j = coordenada.getColumna();
        return casillas[i][j];
    }

    /**
     *
     * @param mouseEvent
     */
    private void siguienteDireccion(MouseEvent mouseEvent) {
        if (casillaResaltada != null) casillaResaltada.desResaltar();
        int playerX = tablero.getJugador().getPosicion().getColumna();
        int playerY = tablero.getJugador().getPosicion().getFila();

        int mouseX = (int) mouseEvent.getX();
        int mouseY = (int) mouseEvent.getY();

        int gridX = mouseX / Grilla.LADO_CASILLA;
        int gridY = mouseY / Grilla.LADO_CASILLA;

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
        } else if (Math.abs(deltaY) > Math.abs(deltaX)){
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


package org.example.vista;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.TilePane;
import javafx.scene.paint.Color;
import org.example.modelo.Coordenada;
import org.example.modelo.Robot;
import org.example.modelo.Tablero;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;

public class Grilla extends TilePane {
    public static final int LADO_CASILLA = 20;
    private final Tablero tablero;
    private final Casilla[][] casillas;
    private Coordenada casillaSeleccionada;
    private boolean modoEspera;

    public Grilla(Tablero tablero) {
        super();
        this.tablero = tablero;
        this.casillas = new Casilla[tablero.getFilas()][tablero.getColumnas()];
        this.modoEspera = false;

        super.addEventHandler(SafeTeleportEvent.SAFE_TELEPORT, e -> {
            modoEspera = true;
            e.consume();
        });

        super.addEventHandler(CeldaSeleccionadaEvent.CELDA_SELECCIONADA, e -> {
            this.update(new ActionSafeTeleport(casillaSeleccionada));
            e.consume();
        });

        dibujarGrilla();
        dibujarEntidades();
    }

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
                        this.fireEvent(new CeldaSeleccionadaEvent());
                    }
                    this.casillaSeleccionada = new Coordenada(tmpI, tmpJ);
                });
                casillas[i][j] = nuevaCasilla;
                super.getChildren().add(nuevaCasilla);
                colorGrilla = !colorGrilla;

            }
        }

    }

    public void update(Action action) {
        if (modoEspera) {
            return;
        }
        action.apply(tablero);
        clear();
        dibujarEntidades();

    }

    public void update() {
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
            casillaRobot.agregarEntidad("robot1x");
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

    /*Devuelve la casilla que refieren las coordenadas.*/
    private Casilla obtenerCasilla(Coordenada coordenada) {
        int i = coordenada.getFila();
        int j = coordenada.getColumna();
        return casillas[i][j];
    }

    public Coordenada getCasillaSeleccionada() {
        return casillaSeleccionada;
    }
}


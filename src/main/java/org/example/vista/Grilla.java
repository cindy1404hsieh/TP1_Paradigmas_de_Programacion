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
        });

        super.addEventHandler(CeldaSeleccionadaEvent.CELDA_SELECCIONADA, e -> {
            System.out.println("chau");
            this.update(new ActionSafeTeleport(casillaSeleccionada));
        });

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
                        System.out.printf("nueva posicion: %d %d", tmpI, tmpJ);
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
            casillaRobot.agregarEntidad(robot.getTipo());
        }

        for (Coordenada celda : tablero.getCeldasIncendiadas()) {
            Casilla casilla = obtenerCasilla(celda);
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
    private Casilla obtenerCasilla(Coordenada coordenada) {
        int i = coordenada.getFila();
        int j = coordenada.getColumna();
        return casillas[i][j];
    }

}


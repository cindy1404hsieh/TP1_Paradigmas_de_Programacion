package org.example.vista;

import javafx.scene.layout.TilePane;
import javafx.scene.paint.Color;
import org.example.modelo.Coordenada;
import org.example.modelo.Robot;
import org.example.modelo.Tablero;

import java.util.Dictionary;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;

public class Grilla extends TilePane {
    public static final int LADO_CASILLA = 20;
    private final Tablero tablero;
    private final Casilla[][] casillas;

    public Grilla(Tablero tablero) {
        super();

        this.tablero = tablero;
        this.casillas = new Casilla[tablero.getFilas()][tablero.getColumnas()];
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
                casillas[i][j] = nuevaCasilla;
                super.getChildren().add(nuevaCasilla);
                colorGrilla = !colorGrilla;

            }
        }

    }

    public void dibujarEntidades() {

        Casilla casillaJugador = obtenerCasilla(tablero.getJugador().getPosicion());
        casillaJugador.agregarEntidad("jugador");

        for (Robot robot : tablero.getRobots()) {
            Casilla casillaRobot = obtenerCasilla(robot.getPosicion());
            casillaRobot.agregarEntidad("robot1x");
            System.out.println(1);
        }
    }

    private Casilla obtenerCasilla(Coordenada coordenada) {
        int i = coordenada.getFila();
        int j = coordenada.getColumna();
        return casillas[i][j];
    }


}

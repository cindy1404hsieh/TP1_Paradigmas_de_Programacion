package org.example.vista;

import javafx.scene.layout.TilePane;
import javafx.scene.paint.Color;

import java.util.Random;

public class Grilla extends TilePane {
    public static final int LADO_CASILLA = 20;
    private int columnas;
    private int filas;
    private Casilla[][] matriz;
    private Entidad jugador;

    Grilla(int columnas, int filas) {
        super();
        this.columnas = columnas;
        this.filas = filas;
        crearGrilla();
        agregarEntidades();
    }

    private void crearGrilla(){
        super.setPrefColumns(columnas);
        super.setPrefRows(filas);

        matriz = new Casilla[filas][columnas];
        boolean colorGrilla = true;
        super.setMaxSize(columnas*LADO_CASILLA, filas*LADO_CASILLA);

        for (int i = 0; i < filas; i++) {
            // si la cantidad de columnas es impar, una fila empieza y termina con el mismo color. En ese caso actualizo el valor de colorGrilla
            colorGrilla = (columnas % 2 != 0) == colorGrilla;
            for (int j = 0; j < columnas; j++) {
                Color color = (colorGrilla) ? Color.LIGHTBLUE : Color.BLUE;
                Casilla nuevaCasilla = new Casilla(i, j, color);
                matriz[i][j] = nuevaCasilla;
                super.getChildren().add(nuevaCasilla);
                colorGrilla = !colorGrilla;

            }
        }

    }

    public void agregarEntidades() {
        agregarJugador();
        Random rd = new Random();
        for (int i = 0; i < 8; i++) {
            int randX = rd.nextInt(columnas);
            int randY = rd.nextInt(filas);
            while (matriz[randY][randX].estaOcupada()) {
                randX = rd.nextInt(columnas);
                randY = rd.nextInt(filas);
            }
            matriz[randY][randX].agregarEntidad("robot1x");
        }
    }

    public void actualizarTamanio(int columnas, int filas) {
        this.columnas = columnas;
        this.filas = filas;

        super.getChildren().clear();
        crearGrilla();
    }

    public int getColumnas() {
        return columnas;
    }

    public int getFilas() {
        return filas;
    }

    public Casilla obtenerCasilla(int x, int y) {
        return matriz[x][y];
    }

    private void agregarJugador() {
        matriz[filas/2][columnas/2].agregarEntidad("jugador");
    }

}

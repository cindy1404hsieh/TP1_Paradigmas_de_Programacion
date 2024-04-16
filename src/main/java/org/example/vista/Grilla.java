package org.example.vista;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.TilePane;
import javafx.scene.paint.Color;

import java.io.File;
import java.io.FileInputStream;

public class Grilla extends TilePane {
    public static final int LADO_CASILLA = 20;
    private int columnas;
    private int filas;
    private Casilla[][] matriz;

    Grilla(int columnas, int filas) {
        super();
        this.columnas = columnas;
        this.filas = filas;
        crearGrilla();
        matriz[filas/2][columnas/2].agregarEntidad("jugador");
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

}
